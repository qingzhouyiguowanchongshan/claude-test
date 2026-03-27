package com.shop.order;

import java.util.List;

public class OrderRequest {

    private String customerName;
    private String email;
    private String address;
    private String city;
    private String zip;
    private List<LineItem> items;

    public static class LineItem {
        private Long productId;
        private int quantity;

        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }

        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
    }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getZip() { return zip; }
    public void setZip(String zip) { this.zip = zip; }

    public List<LineItem> getItems() { return items; }
    public void setItems(List<LineItem> items) { this.items = items; }
}
