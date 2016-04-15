package com.mymarket.app.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by bastosbf on 12/8/15.
 */
public class Search implements Serializable {
    private Product product;
    private Market market;
    private Double price;
    private Date lastUpdate;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Market getMarket() {
        return market;
    }

    public void setMarket(Market market) {
        this.market = market;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }


}
