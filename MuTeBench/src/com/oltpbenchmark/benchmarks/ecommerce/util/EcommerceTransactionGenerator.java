package com.oltpbenchmark.benchmarks.ecommerce.util;

import java.util.List;
import java.util.Random;

import com.oltpbenchmark.api.TransactionGenerator;

public class EcommerceTransactionGenerator implements TransactionGenerator<EcommerceOperation> {

    private final Random rng = new Random();
    private final List<EcommerceOperation> transactions;

    public EcommerceTransactionGenerator(List<EcommerceOperation> transactions) {
        this.transactions = transactions;
    }

    @Override
    public EcommerceOperation nextTransaction() {
        if (transactions == null || transactions.isEmpty()) return null;
        int transactionIndex = rng.nextInt(transactions.size());
        return transactions.get(transactionIndex);
    }
}
