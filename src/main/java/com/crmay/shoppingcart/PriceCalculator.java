package com.crmay.shoppingcart;

import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

/**
 * Calculates the total price of all products.
 *
 * Created by Clive May on 26/11/2016.
 */
public class PriceCalculator {

    private Map<String, Product> productMap;

    /**
     * Calculate the total price.
     *
     * @param productList the products
     * @return the price
     */
    public BigDecimal calculate(List<String> productList) {
        productList.forEach((p) -> {
            if (StringUtils.isBlank(p)) {
                throw new IllegalArgumentException("Null or blank products are not permitted");
            }
        });

        List<String> productKeyList = productList.stream().map(p -> p.trim().toLowerCase()).collect(toList());

        Set<String> unknownProductSet =
                productKeyList.stream().filter(p -> !productMap.containsKey(p)).collect(toSet());
        if (!unknownProductSet.isEmpty()) {
            throw new IllegalArgumentException("Unknown products : " + new TreeSet<>(unknownProductSet));
        }

        // Need to calculate the number of each product in the list so that price calculations can be performed.
        // Therefore create a map where key is the product and the value is the number of instances of that product in
        // the productKeyList.
        Map<Product, Long> productCountMap =
                productKeyList.stream().collect(groupingBy(p -> productMap.get(p), counting()));

        // For each product in the map, product and the productCountMap to calculate the total price for that product
        // and finally return the total price for all products.
        return productCountMap.entrySet().stream()
                .map(e -> e.getKey().getPriceCalcFunc().apply(e.getKey(), productCountMap))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Sets the product map keyed by the product name.
     *
     * @param productMap the product map
     */
    public void setProductMap(Map<String, Product> productMap) {
        this.productMap = new CaseInsensitiveMap<>(productMap);
    }
}
