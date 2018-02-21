package com.mlc.stock.price.updater;

import java.io.Serializable;

public class PriceUpdate implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private final String companyName;
    private final double price;

    public PriceUpdate(String companyName, double price) {
        this.companyName = companyName;
        this.price = price;
    }

    public String getCompanyName() {
        return this.companyName;
    }

    public double getPrice() {
        return this.price;
    }

    @Override
    public String toString() {
        return companyName + " - " + price;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        PriceUpdate other = (PriceUpdate) obj;
        if (this.getCompanyName().equals(other.getCompanyName())) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return 1;
    }
}
