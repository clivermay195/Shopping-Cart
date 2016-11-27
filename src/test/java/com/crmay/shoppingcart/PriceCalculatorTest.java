package com.crmay.shoppingcart;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;

/**
 * Created by Clive May on 26/11/2016.
 */
public class PriceCalculatorTest {

    private PriceCalculator priceCalculator;

    @Before
    public void setup() {
        priceCalculator = new PriceCalculator();

        PriceCalculatorFunction twoForOneOfferFunction =
                (numberOfItems, price) -> price.multiply(new BigDecimal(numberOfItems / 2 + numberOfItems % 2));

        PriceCalculatorFunction threeForTwoOfferFunction =
                (numberOfItems, price) -> price.multiply(new BigDecimal(numberOfItems - numberOfItems / 3));

        Map<String, Product> productMap = new HashMap<>();
        productMap.put("apple", new Product("apple", new BigDecimal("0.60"), twoForOneOfferFunction));
        productMap.put("orange", new Product("orange", new BigDecimal("0.25"), threeForTwoOfferFunction));
        priceCalculator.setProductMap(productMap);
    }

    @Test
    public void theOneWhereTheCorrectPriceIsCalculatedFor3ApplesAnd2Oranges() {
        String[] products = {"apple", "APPLE", "   Apple ", "orange", "Orange   "};
        assertEquals(new BigDecimal("1.70"), priceCalculator.calculate(Arrays.asList(products)));
    }

    @Test
    public void theOneWhereTheCorrectPriceIsCalculatedFor3ApplesAnd3Oranges() {
        String[] products = {"apple", "APPLE", "   Apple ", "orange", "Orange   ", "orange"};
        assertEquals(new BigDecimal("1.70"), priceCalculator.calculate(Arrays.asList(products)));
    }

    @Test
    public void theOneWhereTheCorrectPriceIsCalculatedFor2Apples() {
        String[] products = {"apple", "apple"};
        assertEquals(new BigDecimal("0.60"), priceCalculator.calculate(Arrays.asList(products)));
    }

    @Test
    public void theOneWhereTheCorrectPriceIsCalculatedFor7OrangesApples() {
        String[] products = {"orange", "orange", "orange", "orange", "orange", "orange", "orange"};
        assertEquals(new BigDecimal("1.25"), priceCalculator.calculate(Arrays.asList(products)));
    }

    @Test
    public void theOneWhereTheCorrectPriceIsCalculatedFor5Apples() {
        String[] products = {"apple", "apple", "apple", "apple", "apple"};
        assertEquals(new BigDecimal("1.80"), priceCalculator.calculate(Arrays.asList(products)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void theOneWhereThereIsANullProduct() {
        String[] products = {"apple", "orange", null};
        priceCalculator.calculate(Arrays.asList(products));
    }

    @Test(expected = IllegalArgumentException.class)
    public void theOneWhereThereIsABlankProduct() {
        String[] products = {"apple", "orange", " "};
        priceCalculator.calculate(Arrays.asList(products));
    }

    @Test(expected = IllegalArgumentException.class)
    public void theOneWhereThereAreMultipleInvalidProducts() {
        String[] products = {"apple", "APPLE", "pear", "Banana"};
        priceCalculator.calculate(Arrays.asList(products));
    }

    @Test
    public void theOneWhereThereAreMultipleInvalidProductsWithTheSameName() {
        try {
            String[] products = {"apple", "APPLE", "pear  ", "pear", "Banana"};
            priceCalculator.calculate(Arrays.asList(products));
            fail("IllegalArgumentException should have been thrown");
        } catch (Throwable t) {
            assertEquals("Unknown products : [banana, pear]", t.getMessage());
        }
    }
}
