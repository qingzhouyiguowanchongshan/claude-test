package com.shop.order;

import com.shop.product.Product;
import com.shop.product.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderRepository orderRepo;
    private final ProductRepository productRepo;
    private final EmailService emailService;

    public OrderController(OrderRepository orderRepo, ProductRepository productRepo, EmailService emailService) {
        this.orderRepo = orderRepo;
        this.productRepo = productRepo;
        this.emailService = emailService;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> placeOrder(@RequestBody OrderRequest req) {
        List<Long> productIds = req.getItems().stream()
                .map(OrderRequest.LineItem::getProductId)
                .toList();

        // Lock product rows to prevent race conditions on concurrent orders
        Map<Long, Product> productMap = productRepo.findByIdsForUpdate(productIds)
                .stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));

        // Validate stock for all items before making any changes
        for (OrderRequest.LineItem line : req.getItems()) {
            Product product = productMap.get(line.getProductId());
            if (product == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Product not found: " + line.getProductId()));
            }
            if (product.getStock() < line.getQuantity()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("error", "\"" + product.getName() + "\" only has "
                                + product.getStock() + " left in stock."));
            }
        }

        // Build order
        Order order = new Order();
        order.setCustomerName(req.getCustomerName());
        order.setEmail(req.getEmail());
        order.setAddress(req.getAddress());
        order.setCity(req.getCity());
        order.setZip(req.getZip());

        BigDecimal total = BigDecimal.ZERO;
        for (OrderRequest.LineItem line : req.getItems()) {
            Product product = productMap.get(line.getProductId());

            // Decrement stock
            product.setStock(product.getStock() - line.getQuantity());

            // Build order item
            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(line.getQuantity());
            item.setUnitPrice(product.getPrice());
            order.getItems().add(item);

            total = total.add(product.getPrice().multiply(BigDecimal.valueOf(line.getQuantity())));
        }
        order.setTotal(total);

        Order saved = orderRepo.save(order);
        emailService.sendOrderConfirmation(saved);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("orderId", saved.getId(), "total", saved.getTotal()));
    }
}
