package com.oltpbenchmark.benchmarks.ecommerce;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.configuration.XMLConfiguration;
import org.apache.log4j.Logger;

import com.oltpbenchmark.WorkloadConfiguration;
import com.oltpbenchmark.api.BenchmarkModule;
import com.oltpbenchmark.api.Loader;
import com.oltpbenchmark.api.TransactionGenerator;
import com.oltpbenchmark.api.Worker;
import com.oltpbenchmark.benchmarks.ecommerce.data.ProductHistograms;
import com.oltpbenchmark.benchmarks.ecommerce.procedures.AddToCart;
import com.oltpbenchmark.benchmarks.ecommerce.util.EcommerceTransactionGenerator;
import com.oltpbenchmark.benchmarks.ecommerce.util.EcommerceTransactionSelector;
import com.oltpbenchmark.benchmarks.ecommerce.util.EcommerceOperation;
import com.oltpbenchmark.util.TextGenerator;
import com.oltpbenchmark.util.RandomDistribution.FlatHistogram;

public class EcommerceBenchmark extends BenchmarkModule {
    private static final Logger LOG = Logger.getLogger(EcommerceBenchmark.class);

    private final File traceInput;
    private final File traceOutput;
    private final int traceSize;

    protected final FlatHistogram<Integer> productNameLength;
    protected final FlatHistogram<Integer> productPrice;
    protected final FlatHistogram<Integer> priceChangeDelta;

    @SuppressWarnings("unchecked")
    public EcommerceBenchmark(WorkloadConfiguration workConf) {
        super("ecommerce", workConf, true);

        XMLConfiguration xml = workConf.getXmlConfig();
        this.traceInput = (xml != null && xml.containsKey("tracefile"))
                ? new File(xml.getString("tracefile"))
                : null;

        if (xml != null && xml.containsKey("traceOut")) {
            this.traceSize = xml.getInt("traceOut");
            this.traceOutput = new File(xml.getString("tracefile"));
        } else {
            this.traceSize = 0;
            this.traceOutput = null;
        }

        this.productNameLength = new FlatHistogram<>(this.rng(), ProductHistograms.getNameLength());
        this.productPrice = new FlatHistogram<>(this.rng(), ProductHistograms.getProductPrice());
        this.priceChangeDelta = new FlatHistogram<>(this.rng(), ProductHistograms.getPriceVariation());
    }

    public File getTraceInput() {
        return this.traceInput;
    }

    public File getTraceOutput() {
        return this.traceOutput;
    }

    public int getTraceSize() {
        return this.traceSize;
    }

    protected char[] generateProductVariationText(char[] original) {
        int delta = this.priceChangeDelta.nextValue();

        if (original.length + delta <= 0) {
            delta = -1 * (int) Math.round(original.length / 1.5);
            if (Math.abs(delta) == original.length && delta < 0)
                delta /= 2;
        }
        if (delta != 0) {
            original = TextGenerator.resizeText(rng(), original, delta);
        }

        original = TextGenerator.permuteText(rng(), original);
        return original;
    }

    @Override
    protected Package getProcedurePackageImpl() {
        return AddToCart.class.getPackage();
    }

    @Override
    protected List<Worker> makeWorkersImpl(boolean verbose) throws IOException {
        LOG.info(String.format(
                "Initializing %d %s using '%s' as the input trace file",
                workConf.getTerminals(), EcommerceWorker.class.getSimpleName(),
                this.traceInput));

        EcommerceTransactionSelector transSel = new EcommerceTransactionSelector(this.traceInput,
                workConf.getTransTypes());
        List<EcommerceOperation> trace = Collections.unmodifiableList(transSel.readAll());
        LOG.info("Total Number of Sample Operations: " + trace.size());

        ArrayList<Worker> workers = new ArrayList<>();
        for (int i = 0; i < workConf.getTerminals(); ++i) {
            TransactionGenerator<EcommerceOperation> generator = new EcommerceTransactionGenerator(trace);
            EcommerceWorker worker = new EcommerceWorker(i, this, generator);
            workers.add(worker);
        }
        return workers;
    }

    @Override
    protected Loader makeLoaderImpl(Connection conn) throws SQLException {
        return new EcommerceLoader(this, conn);
    }
}