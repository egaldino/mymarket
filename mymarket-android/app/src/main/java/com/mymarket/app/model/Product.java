package com.mymarket.app.model;

import java.io.Serializable;

/**
 * Created by bastosbf on 12/8/15.
 */
public class Product implements Serializable {

    private String barcode;
    private String name;

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name ;
    }
}
