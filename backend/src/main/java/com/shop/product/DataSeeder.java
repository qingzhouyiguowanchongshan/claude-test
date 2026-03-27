package com.shop.product;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class DataSeeder implements ApplicationRunner {

    private final ProductRepository repo;

    public DataSeeder(ProductRepository repo) {
        this.repo = repo;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (repo.count() > 0) return;

        repo.saveAll(List.of(
            product("Wireless Headphones",    "Electronics", "79.99",  25, "🎧", "linear-gradient(135deg,#0f3460,#1a1a2e)", "Premium over-ear sound with 30-hour battery life."),
            product("Mechanical Keyboard",    "Electronics", "129.99", 15, "⌨️", "linear-gradient(135deg,#16213e,#0f3460)", "RGB backlit with tactile switches and aluminium frame."),
            product("USB-C Hub 7-in-1",       "Electronics", "49.99",  40, "🔌", "linear-gradient(135deg,#0a1628,#0f3460)", "HDMI 4K, 3× USB-A, SD card slot, and 100W PD charging."),
            product("Classic Hoodie",          "Clothing",    "45.00",  50, "🧥", "linear-gradient(135deg,#2d1b4e,#1a1a2e)", "Soft cotton blend, relaxed fit, unisex sizing."),
            product("Slim Chino Pants",        "Clothing",    "55.00",  30, "👖", "linear-gradient(135deg,#1e1040,#2d1b4e)", "Stretch fabric with a tapered leg and 5-pocket style."),
            product("Graphic Tee",             "Clothing",    "22.00",  60, "👕", "linear-gradient(135deg,#2d1b4e,#16213e)", "100% organic cotton with a minimalist print."),
            product("Clean Code",              "Books",       "35.00",  20, "📘", "linear-gradient(135deg,#1a3a2e,#0f3460)", "Robert C. Martin's essential guide to writing readable code."),
            product("The Pragmatic Programmer","Books",       "40.00",  18, "📗", "linear-gradient(135deg,#0f3a1a,#1a3a2e)", "Hunt & Thomas — 20th anniversary edition."),
            product("Adjustable Desk Lamp",    "Home",        "38.00",  35, "🪔", "linear-gradient(135deg,#3a1a2e,#16213e)", "LED, adjustable arm, with built-in USB charging port."),
            product("Ceramic Coffee Mug",      "Home",        "18.00",  45, "☕", "linear-gradient(135deg,#2e1a1a,#3a1a2e)", "12 oz, dishwasher-safe, matte glaze finish.")
        ));
    }

    private Product product(String name, String category, String price, int stock,
                             String emoji, String gradient, String description) {
        Product p = new Product();
        p.setName(name);
        p.setCategory(category);
        p.setPrice(new BigDecimal(price));
        p.setStock(stock);
        p.setEmoji(emoji);
        p.setGradient(gradient);
        p.setDescription(description);
        return p;
    }
}
