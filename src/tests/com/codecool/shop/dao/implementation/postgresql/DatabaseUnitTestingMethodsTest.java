package com.codecool.shop.dao.implementation.postgresql;

import org.junit.jupiter.api.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseUnitTestingMethodsTest {

    private static DatabaseUnitTestingMethods dbUTMethodsObject;
    private static final String CREATE_DB_SCHEMA = "src/main/resources/sql/db_dumps/jawas_webshop_create_db_schema.sql";

    @BeforeAll
    public static void setUp() {
        dbUTMethodsObject = new DatabaseUnitTestingMethods();
    }

    @Disabled
    @AfterAll
    public static void tearDown() {
        // delete test data
        dbUTMethodsObject.executeUpdateFromFile(CREATE_DB_SCHEMA);
    }

    // SUPPLIER TABLE
	@Test
    public void testIsIdColumnOfSupplierTableNumber() {
        String expected = "22P02";

        Throwable exception = assertThrows(SQLException.class, () -> {
            dbUTMethodsObject.executeUpdate("INSERT INTO supplier VALUES('a', 'a', 'a')");
        });

        assertEquals(expected, ((SQLException) exception).getSQLState());
    }

    // SUPPLIER TABLE
    @Test
    public void testIsIdColumnOfSupplierTableNotNull() {
        String expected = "23502";

        Throwable exception = assertThrows(SQLException.class, () -> {
            dbUTMethodsObject.executeUpdate("INSERT INTO supplier VALUES(null, 'a', 'a');");
        });

        assertEquals(expected, ((SQLException)exception).getSQLState());
    }

    // SUPPLIER TABLE
    @Test
    public void testIsNameColumnOfSupplierTableNotNull() {
        String expected = "23502";

        Throwable exception = assertThrows(SQLException.class, () -> {
            dbUTMethodsObject.executeUpdate("INSERT INTO supplier VALUES(DEFAULT, null, 'a');");
        });

        assertEquals(expected, ((SQLException)exception).getSQLState());
    }

    // SUPPLIER TABLE
    @Test
    public void testIsDescriptionColumnOfSupplierTableNotNull() {
        String expected = "23502";

        Throwable exception = assertThrows(SQLException.class, () -> {
            dbUTMethodsObject.executeUpdate("INSERT INTO supplier VALUES(DEFAULT, 'a', null);");
        });

        assertEquals(expected, ((SQLException)exception).getSQLState());
    }

    // Product
	@Test
    public void testIsDefaultCurrencyColumnOfProductTableMax3CharLong() {
        String expected = "22001";

        Throwable exception = assertThrows(SQLException.class, () -> {
            dbUTMethodsObject.executeUpdate("INSERT INTO product VALUES(DEFAULT, 'a', 'a', 33.44, 'aaaa', 1, 1);");
        });

        assertEquals(expected, ((SQLException) exception).getSQLState());
    }

    @Test
    public void testIsDefaultPriceColumnOfProductTableNumber() {
        String expected = "22P02";

        Throwable exception = assertThrows(SQLException.class, () -> {
            dbUTMethodsObject.executeUpdate("INSERT INTO product VALUES(DEFAULT, 'a', 'a', 'a', 'aaa', 1, 1);");
        });

        assertEquals(expected, ((SQLException) exception).getSQLState());
    }



}