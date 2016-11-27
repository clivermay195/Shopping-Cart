package com.crmay.shoppingcart;

import java.math.BigDecimal;

/**
 * A class which contains product related data.
 * <p>
 * Created by Clive May on 26/11/2016.
 */
public class Product {
    private String name;
    private BigDecimal price;
    private PriceCalculatorFunction priceCalcFunc;

    /**
     * Instantiates a new Product.
     *
     * @param name          the name
     * @param price         the price
     * @param priceCalcFunc the price calc func
     */
    public Product(String name, BigDecimal price, PriceCalculatorFunction priceCalcFunc) {
        this.name = name;
        this.price = price;
        this.priceCalcFunc = priceCalcFunc;
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

    /**
     * Gets price calc func.
     *
     * @return the price calc func
     */
    public PriceCalculatorFunction getPriceCalcFunc() {
        return priceCalcFunc;
    }
}
