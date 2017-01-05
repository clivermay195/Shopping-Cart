package com.crmay.shoppingcart;

import java.math.BigDecimal;
import java.util.Map;

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
     * @param product         the product to calculate the price for.
     * @param productCountMap the map keyed by product which contains the number of each item in the basket.
     * @return the total price
     */
    BigDecimal apply (Product product, Map<Product, Long> productCountMap);
}
