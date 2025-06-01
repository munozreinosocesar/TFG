package com.oltpbenchmark.benchmarks.ecommerce;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.oltpbenchmark.api.Procedure;
import com.oltpbenchmark.api.Procedure.UserAbortException;
import com.oltpbenchmark.api.TransactionGenerator;
import com.oltpbenchmark.api.TransactionType;
import com.oltpbenchmark.api.Worker;
import com.oltpbenchmark.benchmarks.ecommerce.procedures.*;
import com.oltpbenchmark.benchmarks.ecommerce.util.EcommerceOperation;
import com.oltpbenchmark.types.TransactionStatus;
import com.oltpbenchmark.util.RandomDistribution.Flat;
import com.oltpbenchmark.util.TextGenerator;

public class EcommerceWorker extends Worker {
    private static final Logger LOG = Logger.getLogger(EcommerceWorker.class);
    private final TransactionGenerator<EcommerceOperation> generator;

    private final Flat usersRng;
    private final int numUsers;

    public EcommerceWorker(int id, EcommerceBenchmark benchmarkModule,
                           TransactionGenerator<EcommerceOperation> generator) {
        super(benchmarkModule, id);
        this.generator = generator;
        int scaledUsers = (int) Math.round(EcommerceConstants.USUARIOS
                    * this.getWorkloadConfiguration().getScaleFactor());


        this.numUsers = scaledUsers;
        this.usersRng = new Flat(rng(), 1, this.numUsers);
    }

    private String generateUserIP() {
        return String.format("%d.%d.%d.%d",
                rng().nextInt(255) + 1,
                rng().nextInt(256),
                rng().nextInt(256),
                rng().nextInt(256));
    }

    @Override
    protected TransactionStatus executeWork(TransactionType nextTransaction)
            throws UserAbortException, SQLException {

        EcommerceOperation t = null;
        Class<? extends Procedure> procClass = nextTransaction.getProcedureClass();

        boolean needUser = (procClass.equals(AddToCart.class)
                || procClass.equals(RemoveFromCart.class)
                || procClass.equals(ViewOrderHistory.class)
                || procClass.equals(Checkout.class));

        while (t == null) {
            t = this.generator.nextTransaction();
            if (needUser && (t == null || t.userId == 0)) {
                t = null;
            }
        }

        assert t != null;

        if (t.userId != 0)
            t.userId = this.usersRng.nextInt();

        String userIP = generateUserIP();

        try {
            if (procClass.equals(AddToCart.class)) {
                simulateDelay(5);
                addToCart(t.userId, t.productId);
            } else if (procClass.equals(RemoveFromCart.class)) {
                simulateDelay(3);
                removeFromCart(t.userId, t.productId);
            } else if (procClass.equals(Checkout.class)) {
                simulateDelay(7);
                checkout(t.userId);
            } else if (procClass.equals(SearchProduct.class)) {
                simulateDelay(2);
                int length = 5 + rng().nextInt(10);
                String dummyQuery = TextGenerator.randomStr(rng(), length);
                searchProduct(t.productId); // dummyQuery no se usa aún
            } else if (procClass.equals(ViewOrderHistory.class)) {
                simulateDelay(4);
                viewOrderHistory(t.userId);
            }

        } catch (SQLException e) {
            LOG.error("Error ejecutando transacción: " + nextTransaction.getName(), e);
            return TransactionStatus.ABORTED;
        }

        return TransactionStatus.SUCCESS;
    }

    private void simulateDelay(int maxMillis) {
        try {
            Thread.sleep(rng().nextInt(maxMillis));
        } catch (InterruptedException ignored) {}
    }

    public void searchProduct(int productId) throws SQLException {
        SearchProduct proc = this.getProcedure(SearchProduct.class);
        assert proc != null;
        proc.run(conn, productId);
    }

    public void viewOrderHistory(int userId) throws SQLException {
        ViewOrderHistory proc = this.getProcedure(ViewOrderHistory.class);
        assert proc != null;
        proc.run(conn, userId);
    }

    public void addToCart(int userId, int productId) throws SQLException {
        AddToCart proc = this.getProcedure(AddToCart.class);
        assert proc != null;
        proc.run(conn, userId, productId);
    }

    public void removeFromCart(int userId, int productId) throws SQLException {
        RemoveFromCart proc = this.getProcedure(RemoveFromCart.class);
        assert proc != null;
        proc.run(conn, userId, productId);
    }

    public void checkout(int userId) throws SQLException {
        Checkout proc = this.getProcedure(Checkout.class);
        assert proc != null;
        proc.run(conn, userId);
    }
}
