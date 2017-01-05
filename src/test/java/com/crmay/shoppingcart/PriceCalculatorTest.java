package com.crmay.shoppingcart;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

        PriceCalculatorFunction noOfferFunction = (product, productCountMap) -> {
            long numberOfItems = productCountMap.get(product);
            return product.getPrice().multiply(new BigDecimal(numberOfItems));
        };

        PriceCalculatorFunction twoForOneCheapestOfferFunction = (product, productCountMap) -> {
            // Filter out products which do not use the twoForOneCheapestOfferFunction or have only one item so the
            // offer does not apply. i.e. if only 1 banana, apply offer to apples.
            List<Map.Entry<Product, Long>> twoForOneOfferProductMap = productCountMap.entrySet().stream().filter(
                    e -> e.getValue() > 1 && e.getKey().getPriceCalcFunc().equals(product.getPriceCalcFunc()))
                    .collect(Collectors.toList());

            if (twoForOneOfferProductMap.isEmpty()) {
                // Only one item so no offer to apply.
                return product.getPrice();
            }

            // Identify the cheapest product that twoForOne applies to.
            Product cheapestProduct = twoForOneOfferProductMap.stream().min(
                    Comparator.comparing(e -> e.getKey().getPrice())).get().getKey();

            if (product.equals(cheapestProduct)) {
                long numberOfItems = productCountMap.get(product);
                return product.getPrice().multiply(new BigDecimal(numberOfItems / 2 + numberOfItems % 2));
            } else {
                return noOfferFunction.apply(product, productCountMap);
            }
        };

        PriceCalculatorFunction threeForTwoOfferFunction =
                (product, productCountMap) -> {
                    long numberOfItems = productCountMap.get(product);
                    return product.getPrice().multiply(new BigDecimal(numberOfItems - numberOfItems / 3));
                };

        Map<String, Product> productMap = new HashMap<>();
        productMap.put("apple", new Product("apple", new BigDecimal("0.60"), twoForOneCheapestOfferFunction));
        productMap.put("orange", new Product("orange", new BigDecimal("0.25"), threeForTwoOfferFunction));
        productMap.put("banana", new Product("banana", new BigDecimal("0.20"), twoForOneCheapestOfferFunction));
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
    public void theOneWhereTheCorrectPriceIsCalculatedFor3Apples3OrangesAnd3Bananas() {
        String[] products = {"apple", "apple", "apple", "orange", "orange", "orange", "banana", "banana"};
        assertEquals(new BigDecimal("2.50"), priceCalculator.calculate(Arrays.asList(products)));
    }

    @Test
    public void theOneWhereTheCorrectPriceIsCalculatedFor3Apples3OrangesAnd1Bananas() {
        String[] products = {"apple", "apple", "apple", "orange", "orange", "orange", "banana"};
        assertEquals(new BigDecimal("1.90"), priceCalculator.calculate(Arrays.asList(products)));
    }

    @Test
    public void theOneWhereTheCorrectPriceIsCalculatedFor1Apples3OrangesAnd1Bananas() {
        String[] products = {"apple", "orange", "orange", "orange", "banana"};
        assertEquals(new BigDecimal("1.30"), priceCalculator.calculate(Arrays.asList(products)));
    }

    @Test
    public void theOneWhereTheCorrectPriceIsCalculatedFor1Apple() {
        String[] products = {"apple"};
        assertEquals(new BigDecimal("0.60"), priceCalculator.calculate(Arrays.asList(products)));
    }

    @Test
    public void theOneWhereTheCorrectPriceIsCalculatedFor1AppleAnd1Banana() {
        String[] products = {"apple", "banana"};
        assertEquals(new BigDecimal("0.80"), priceCalculator.calculate(Arrays.asList(products)));
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
        String[] products = {"apple", "APPLE", "pear", "grape"};
        priceCalculator.calculate(Arrays.asList(products));
    }

    @Test
    public void theOneWhereThereAreMultipleInvalidProductsWithTheSameName() {
        try {
            String[] products = {"apple", "APPLE", "pear  ", "pear", "grape"};
            priceCalculator.calculate(Arrays.asList(products));
            fail("IllegalArgumentException should have been thrown");
        } catch (Throwable t) {
            assertEquals("Unknown products : [grape, pear]", t.getMessage());
        }
    }
}
