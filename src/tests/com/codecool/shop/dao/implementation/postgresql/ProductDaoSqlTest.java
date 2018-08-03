package com.codecool.shop.dao.implementation.postgresql;

import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.dao.implementation.Memory.ProductCategoryDaoMem;
import com.codecool.shop.dao.implementation.Memory.SupplierDaoMem;
import com.codecool.shop.model.Product;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.*;

class ProductDaoSqlTest {

    private static ProductDaoSql productDaoSql = null;
    private static ProductCategoryDaoMem productCategoryDaoMem = null;
    private static ProductCategoryDao productCategoryDaoSql = null;

    private static SupplierDaoMem supplierDaoMem = null;
    private static SupplierDao supplierDaoSql = null;

    String queryForMaxId = "SELECT max(id) FROM product;";

    @BeforeAll
    private static void simulateInitializer() {
        productDaoSql = ProductDaoSql.getSingletonInstance();
        productCategoryDaoMem = ProductCategoryDaoMem.getInstance();
        productCategoryDaoSql = ProductCategoryDaoSql.getInstance();
        productCategoryDaoMem.setData(productCategoryDaoSql.getAll());

        supplierDaoMem = SupplierDaoMem.getInstance();
        supplierDaoSql = SupplierDaoSql.getInstance();
        supplierDaoMem.setData(supplierDaoSql.getAll());

    }

    @Test
    void getSingletonInstance_notNull() {
        assertNotNull(productDaoSql);
    }

    @Disabled
    @Test
    void add_AssertEqual_True() {
        int expectedId = productDaoSql.getLastRecordsId(queryForMaxId);

        String expectedName = "test product";
        String expectedDescription = "test description";
        BigDecimal expectedPrice = new BigDecimal("22.22").setScale(2, RoundingMode.HALF_UP);
        String expectedCurrency = "EUR";

        Product testProduct = new Product(expectedName, expectedPrice, expectedCurrency, expectedDescription, productCategoryDaoMem.find(1), supplierDaoMem.find(1));
        productDaoSql.add(testProduct);

        Product productObjectFromDataBase = productDaoSql.find(expectedId);
        int actualId = productObjectFromDataBase.getId();
        String actualName = productObjectFromDataBase.getName();
        String actualDescription = productObjectFromDataBase.getDescription();
        BigDecimal actualPrice = productObjectFromDataBase.getDefaulPrice();
        String actualCurrency = productObjectFromDataBase.getDefaultCurrency().toString();

        assertSame(expectedId, actualId);
        assertEquals(expectedName, actualName);
        assertEquals(expectedDescription, actualDescription);
        assertEquals(expectedPrice, actualPrice);
        assertSame(expectedCurrency, actualCurrency);

    }

    @Test
    void find_AssertEquals_IfValidQuery() {
        int expectedId = 1;
        String expectedName = "Amazon Fire";
        String expectedDescription = "Digital content and services";
        BigDecimal expectedPrice = new BigDecimal(49.91).setScale(2, RoundingMode.HALF_UP);
        Currency expectedCurrency = Currency.getInstance("USD");

        Product productObjectFromDataBase = productDaoSql.find(expectedId);
        int actualId = productObjectFromDataBase.getId();
        String actualName = productObjectFromDataBase.getName();
        String actualDescription = productObjectFromDataBase.getDescription();
        BigDecimal actualPrice = productObjectFromDataBase.getDefaulPrice();
        Currency actualCurrency = productObjectFromDataBase.getDefaultCurrency();

        assertSame(expectedId, actualId);
        assertEquals(expectedName, actualName);
        assertEquals(expectedDescription, actualDescription);
        assertEquals(expectedPrice, actualPrice);
        assertSame(expectedCurrency, actualCurrency);
    }

    @ParameterizedTest
    @ValueSource(ints = {-212, -1, 0, 1_000_000})
    void find_ThrowsNullPointerException_IfInvalidId(int expectedId) {
        assertThrows(NullPointerException.class, () -> productDaoSql.find(expectedId).getId());
    }

    @Disabled
    @Test
    void remove() {
        int lastRecordsId = productDaoSql.getLastRecordsId(queryForMaxId);
        productDaoSql.remove(lastRecordsId);
        int newLastRecordsId = productDaoSql.getLastRecordsId(queryForMaxId);
        assertEquals(lastRecordsId, (newLastRecordsId + 1));
    }

    @Test
    void getAll_AssertProductListEqualsLastRecordsId() {
        int lastRecordsId = productDaoSql.getLastRecordsId(queryForMaxId);
        int lengthOfProductList = productDaoSql.getAll().size();
        assertEquals(lastRecordsId, lengthOfProductList);
    }

    @Test
    void getBy() {
    }

    @Test
    void getBy1() {
    }

    @Test
    void getBy2() {
    }

}