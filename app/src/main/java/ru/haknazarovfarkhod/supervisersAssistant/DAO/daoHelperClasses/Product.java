package ru.haknazarovfarkhod.supervisersAssistant.DAO.daoHelperClasses;

/**
 * Created by USER on 26.02.2018.
 */

public class Product {
    public String productName;
    public String unitOfMeasurement;
    public Long minimumQuantity;
    public String acticle;

    public Product(String productName, String unitOfMeasurement, Long minimumQuantity, String acticle) {
        this.productName = productName;
        this.unitOfMeasurement = unitOfMeasurement;
        this.minimumQuantity = minimumQuantity;
        this.acticle = acticle;
    }

    public String getProductName() {

        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getUnitOfMeasurement() {
        return unitOfMeasurement;
    }

    public void setUnitOfMeasurement(String unitOfMeasurement) {
        this.unitOfMeasurement = unitOfMeasurement;
    }

    public Long getMinimumQuantity() {
        return minimumQuantity;
    }

    public void setMinimumQuantity(Long minimumQuantity) {
        this.minimumQuantity = minimumQuantity;
    }

    public String getActicle() {
        return acticle;
    }

    public void setActicle(String acticle) {
        this.acticle = acticle;
    }
}
