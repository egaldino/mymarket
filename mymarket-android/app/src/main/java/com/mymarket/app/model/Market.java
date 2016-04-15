package com.mymarket.app.model;

import java.io.Serializable;

/**
 * Created by bastosbf on 12/8/15.
 */
public class Market implements Serializable {

    private Integer id;
    private String name;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof  Market) {
            return ((Market) o).getName().equals(name);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
