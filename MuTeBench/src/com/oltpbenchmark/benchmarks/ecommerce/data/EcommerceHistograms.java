package com.oltpbenchmark.benchmarks.ecommerce.data;

import com.oltpbenchmark.util.Histogram;

public abstract class EcommerceHistograms {
    /**
     * Histograma de precios de productos en la plataforma eCommerce
     */
    public static final Histogram<Integer> PRICE_DISTRIBUTION = new Histogram<Integer>() {
        {
            this.put(5, 1500);
            this.put(10, 2300);
            this.put(15, 1800);
            this.put(20, 2100);
            this.put(25, 1950);
            this.put(30, 1850);
            this.put(35, 1700);
            this.put(40, 1600);
            this.put(50, 1400);
            this.put(60, 1300);
            this.put(70, 1100);
            this.put(80, 1000);
            this.put(90, 900);
            this.put(100, 850);
            this.put(150, 600);
            this.put(200, 450);
            this.put(250, 300);
            this.put(300, 200);
            this.put(500, 100);
            this.put(1000, 50);
        }
    };

    /**
     * Histograma de la cantidad de productos por carrito de compras
     */
    public static final Histogram<Integer> CART_PRODUCT_DISTRIBUTION = new Histogram<Integer>() {
        {
            this.put(1, 5000);
            this.put(2, 4000);
            this.put(3, 3500);
            this.put(4, 3000);
            this.put(5, 2500);
            this.put(6, 2000);
            this.put(7, 1500);
            this.put(8, 1000);
            this.put(9, 800);
            this.put(10, 500);
        }
    };
    
    /**
     * Histograma de la cantidad de pedidos por usuario
     */
    public static final Histogram<Integer> ORDER_DISTRIBUTION = new Histogram<Integer>() {
        {
            this.put(1, 10000);
            this.put(2, 8000);
            this.put(3, 5000);
            this.put(4, 3000);
            this.put(5, 2000);
            this.put(6, 1000);
            this.put(7, 500);
            this.put(8, 300);
            this.put(9, 200);
            this.put(10, 100);
        }
    };
}