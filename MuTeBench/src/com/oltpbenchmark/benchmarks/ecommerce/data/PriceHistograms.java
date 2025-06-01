package com.oltpbenchmark.benchmarks.ecommerce.data;

import com.oltpbenchmark.util.Histogram;

public abstract class PriceHistograms {

    /**
     * The distribution of product prices in the eCommerce system
     */
    public static final Histogram<Integer> PRICE_DISTRIBUTION = new Histogram<>();

    static {
        PRICE_DISTRIBUTION.put(5, 1500);
        PRICE_DISTRIBUTION.put(10, 2300);
        PRICE_DISTRIBUTION.put(15, 1800);
        PRICE_DISTRIBUTION.put(20, 2100);
        PRICE_DISTRIBUTION.put(25, 1950);
        PRICE_DISTRIBUTION.put(30, 1850);
        PRICE_DISTRIBUTION.put(35, 1700);
        PRICE_DISTRIBUTION.put(40, 1600);
        PRICE_DISTRIBUTION.put(50, 1400);
        PRICE_DISTRIBUTION.put(60, 1300);
        PRICE_DISTRIBUTION.put(70, 1100);
        PRICE_DISTRIBUTION.put(80, 1000);
        PRICE_DISTRIBUTION.put(90, 900);
        PRICE_DISTRIBUTION.put(100, 850);
        PRICE_DISTRIBUTION.put(150, 600);
        PRICE_DISTRIBUTION.put(200, 450);
        PRICE_DISTRIBUTION.put(250, 300);
        PRICE_DISTRIBUTION.put(300, 200);
        PRICE_DISTRIBUTION.put(500, 100);
        PRICE_DISTRIBUTION.put(1000, 50);
    }
}
