package com.crmay.shoppingcart;

import java.math.BigDecimal;

/**
 * Function interface for all functions called by the <code>PriceCalculator</code> which calculate the total price
 * for all items of the same product in the shopping cart.
 * <p>
 * Created by crmay on 27/11/2016.
 */
@FunctionalInterface
public interface PriceCalculatorFunction {
    /**
     * Calculate the total price for all items of the same product.
     *
     * @param numberOfItems the number of items
     * @param price         the price
     * @return the total price
     */
    BigDecimal apply (Long numberOfItems, BigDecimal price);
}
