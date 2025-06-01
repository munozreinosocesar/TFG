package com.oltpbenchmark.benchmarks.ecommerce.data;

import com.oltpbenchmark.util.Histogram;

public abstract class ProductHistograms {

    public static Histogram<Integer> getNameLength() {
        Histogram<Integer> histogram = new Histogram<>();
        histogram.put(5, 5);
        histogram.put(10, 30);
        histogram.put(15, 40);
        histogram.put(20, 20);
        histogram.put(25, 5);
        return histogram;
    }

    public static Histogram<Integer> getProductPrice() {
        Histogram<Integer> histogram = new Histogram<>();
        histogram.put(5, 10);
        histogram.put(10, 20);
        histogram.put(20, 40);
        histogram.put(50, 20);
        histogram.put(100, 10);
        return histogram;
    }

    public static Histogram<Integer> getPriceVariation() {
        Histogram<Integer> histogram = new Histogram<>();
        // Evitamos valores negativos para simular aumentos o ajustes hacia cero
        histogram.put(0, 40);
        histogram.put(1, 10);
        histogram.put(2, 15);
        histogram.put(3, 20);
        histogram.put(5, 15);
        return histogram;
    }
}
