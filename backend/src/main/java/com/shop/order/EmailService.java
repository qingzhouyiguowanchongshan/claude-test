package com.shop.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendOrderConfirmation(Order order) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(order.getEmail());
            msg.setSubject("Order Confirmed #" + order.getId());
            msg.setText(buildBody(order));
            mailSender.send(msg);
            log.info("Order confirmation email sent to {}", order.getEmail());
        } catch (Exception e) {
            log.error("Failed to send order confirmation email for order {}: {}", order.getId(), e.getMessage());
        }
    }

    private String buildBody(Order order) {
        StringBuilder sb = new StringBuilder();
        sb.append("Hi ").append(order.getCustomerName()).append(",\n\n");
        sb.append("Your order has been confirmed! Here's your summary:\n\n");
        sb.append("Order #").append(order.getId()).append("\n\n");

        for (OrderItem item : order.getItems()) {
            BigDecimal lineTotal = item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            sb.append(String.format("  %-30s x%d    $%.2f%n",
                    item.getProduct().getName(), item.getQuantity(), lineTotal));
        }

        sb.append(String.format("%nTotal: $%.2f%n", order.getTotal()));
        sb.append("\nShipping to:\n");
        sb.append("  ").append(order.getAddress()).append("\n");
        sb.append("  ").append(order.getCity()).append(", ").append(order.getZip()).append("\n");
        sb.append("\nThank you for shopping with us!\n");
        return sb.toString();
    }
}
