package com.codecool.shop.dao.implementation.Memory;

import com.codecool.shop.model.Supplier;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SupplierDaoMemTest {
    private SupplierDaoMem supplierDaoMem = SupplierDaoMem.getInstance();

    private void addSupplier(){
        Supplier supplier = new Supplier(supplierDaoMem.getAll().size()+1,"test","its a test");
        supplierDaoMem.add(supplier);
    }

    private Supplier addSupplierAndReturn() {
        Supplier supplier = new Supplier(supplierDaoMem.getAll().size()+1,"test","its a test");
        supplierDaoMem.add(supplier);
        return supplier;
    }

    @Test
    void getInstance() {
        assertNotNull(SupplierDaoMem.getInstance());
    }

    @Test
    void add() {
        assertEquals(addSupplierAndReturn(),supplierDaoMem.find(supplierDaoMem.getAll().size()));
    }

    @Test
    void find() {
        assertEquals(addSupplierAndReturn(),supplierDaoMem.find(supplierDaoMem.getAll().size()));
    }

    @Test
    void remove() {
        int size = supplierDaoMem.getAll().size();
        addSupplier();
        supplierDaoMem.remove(1);
        assertEquals(size,supplierDaoMem.getAll().size());
    }
}