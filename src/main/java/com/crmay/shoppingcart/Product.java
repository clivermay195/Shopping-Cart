package com.crmay.shoppingcart;

import java.math.BigDecimal;

/**
 * A class which contains product related data.
 *
 * Created by Clive May on 26/11/2016.
 */
public class Product {
    private String name;
    private BigDecimal price;

    /**
     * Instantiates a new Product.
     *
     * @param name          the name
     * @param price         the price
     */
    public Product(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets price.
     *
     * @return the price
     */
    public BigDecimal getPrice() {
        return price;
    }
}
