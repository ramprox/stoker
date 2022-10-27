package ru.stoker.util;

import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.List;

public class DatabaseFacade {

    private final TestEntityManager entityManager;

    private final TransactionTemplate template;

    protected static final String RESET_SEQUENCE_TO_1_QUERY = "ALTER SEQUENCE %s RESTART WITH 1";

    private static final String DELETE_FROM_TABLE_QUERY = "DELETE FROM %s";

    private final List<String> tables = new ArrayList<>();

    private final List<String> sequences = new ArrayList<>();

    public DatabaseFacade(TransactionTemplate template,
                          TestEntityManager entityManager) {
        this.template = template;
        this.entityManager = entityManager;
    }

    public void addTables(List<String> tables) {
        this.tables.addAll(tables);
    }

    public void addSequences(List<String> sequences) {
        this.sequences.addAll(sequences);
    }

    public void clean() {
        template.executeWithoutResult(status -> {
            if(!tables.isEmpty()) {
                tables.forEach(table -> entityManager.getEntityManager()
                        .createNativeQuery(String.format(DELETE_FROM_TABLE_QUERY, table))
                        .executeUpdate());
            }
            if(!sequences.isEmpty()) {
                sequences.forEach(sequence -> entityManager.getEntityManager()
                        .createNativeQuery(String.format(RESET_SEQUENCE_TO_1_QUERY, sequence))
                        .executeUpdate());
            }
        });
    }

    public void executeInTransaction(Runnable runnable) {
        template.executeWithoutResult(transactionStatus -> runnable.run());
    }

}
