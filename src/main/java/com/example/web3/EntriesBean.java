package com.example.web3;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ManagedBean
@ApplicationScoped
public class EntriesBean implements Serializable {
    private static final String persistenceUnit = "StudsPU";

    private com.example.web3.Entry entry;
    private List<com.example.web3.Entry> entries;

    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private EntityTransaction transaction;

    public EntriesBean() {
        entry = new com.example.web3.Entry();
        entries = new ArrayList<>();

        connection();
        loadEntries();
    }

    private void connection() {
        entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnit);
        entityManager = entityManagerFactory.createEntityManager();
        transaction = entityManager.getTransaction();
    }

    private void loadEntries() {
        try {
            transaction.begin();
            Query query = entityManager.createQuery("SELECT e FROM Entry e");
            entries = query.getResultList();
            transaction.commit();
        } catch (RuntimeException exception) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw exception;
        }

    }

    public String addEntry() {
        try {
            transaction.begin();
            entry.checkHit();
            entityManager.persist(entry);
            entries.add(entry);
            entry = new com.example.web3.Entry();
            transaction.commit();
        } catch (RuntimeException exception) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw exception;
        }
        return "redirect";
    }

    public String clearEntries() {
        try {
            transaction.begin();
            Query query = entityManager.createQuery("DELETE FROM Entry");
            query.executeUpdate();
            entries.clear();
            transaction.commit();
        } catch (RuntimeException exception) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw exception;
        }
        return "redirect";
    }

    public com.example.web3.Entry getEntry() {
        return entry;
    }

    public void setEntry(com.example.web3.Entry entry) {
        this.entry = entry;
    }

    public List<com.example.web3.Entry> getEntries() {
        return entries;
    }

    public void setEntries(List<com.example.web3.Entry> entries) {
        this.entries = entries;
    }
}
