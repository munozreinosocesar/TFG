package com.oltpbenchmark.benchmarks.ecommerce.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.oltpbenchmark.api.TransactionTypes;
import com.oltpbenchmark.util.FileUtil;

public class EcommerceTransactionSelector {

    private static final Pattern SPACE_SPLIT = Pattern.compile(" ");
    private static final Pattern CLEAN_SUFFIX = Pattern.compile("(.*)[ ]+\\-[ ]*$"); // Remueve sufijos como " - "

    private final File file;
    private BufferedReader reader;
    private final TransactionTypes transTypes;

    public EcommerceTransactionSelector(File file, TransactionTypes transTypes) throws FileNotFoundException {
        this.file = file;
        this.transTypes = transTypes;

        if (file == null) {
            throw new FileNotFoundException(
                "Debe especificarse un archivo de transacciones en la configuración del benchmark.");
        }

        try {
            this.reader = FileUtil.getReader(this.file);
        } catch (IOException ex) {
            throw new RuntimeException("Error al abrir el archivo: " + file, ex);
        }

        if (this.reader == null) {
            throw new RuntimeException("No se pudo abrir el archivo de transacciones.");
        }
    }

    /**
     * Lee todas las transacciones del archivo.
     * Cada línea debe tener el formato: <userId> <productId> <action>
     */
    public List<EcommerceOperation> readAll() throws IOException {
        List<EcommerceOperation> transactions = new ArrayList<>();

        String line;
        while ((line = this.reader.readLine()) != null) {
            String[] tokens = SPACE_SPLIT.split(line.trim());

            if (tokens.length < 3) continue;

            try {
                int userId = Integer.parseInt(tokens[0]);
                int productId = Integer.parseInt(tokens[1]);
                String action = tokens[2].trim();

                Matcher m = CLEAN_SUFFIX.matcher(action);
                if (m.find()) {
                    action = m.group(1);
                }

                transactions.add(new EcommerceOperation(userId, productId, action));
            } catch (NumberFormatException e) {
                // Ignora líneas mal formateadas
            }
        }

        this.reader.close();
        return transactions;
    }

    /**
     * Escribe una entrada en el formato requerido por el benchmark.
     */
    public static void writeEntry(OutputStream out, int userId, int productId, String action) throws IOException {
        String line = String.format("%d %d %s\n", userId, productId, action);
        out.write(line.getBytes());
    }
}
