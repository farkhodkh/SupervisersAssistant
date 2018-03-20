package ru.haknazarovfarkhod.supervisersAssistant.DAO.daoHelperClasses;

import java.io.Serializable;

/**
 * Created by USER on 27.02.2018.
 */

public class OrderLine implements Serializable {
    private Integer lineNumber;
    private Long productId;
    private String productName;
    public Integer quantity;
    private Long lineUniqNimber;
    private Product product;

    public Long getLineUniqNimber() {
        return lineUniqNimber;
    }

    public void setLineUniqNimber(Long lineUniqNimber) {
        this.lineUniqNimber = lineUniqNimber;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public OrderLine(Product product) {
        this.product = product;
    }

    public OrderLine(Long productId, String productName) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = 0;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public OrderLine(Long lineUniqNimber, Integer lineNumber, String productName, Long productId, Integer quantity) {
        this.lineNumber = lineNumber;
        this.lineUniqNimber = lineUniqNimber;
        this.productId = productId;
        this.quantity = quantity;
        this.productName = productName;
    }

    public OrderLine(long orderDetailsCursorLong, long aLong, Long productId) {
        this.productId = productId;
        this.quantity = 0;
    }

    public Integer getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(Integer lineNumber) {
        this.lineNumber = lineNumber;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}
