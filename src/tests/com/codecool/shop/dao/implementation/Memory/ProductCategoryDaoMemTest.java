package com.codecool.shop.dao.implementation.Memory;

import com.codecool.shop.dao.implementation.Memory.ProductCategoryDaoMem;
import com.codecool.shop.model.ProductCategory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductCategoryDaoMemTest {
    private ProductCategoryDaoMem productCategoryDaoMem = ProductCategoryDaoMem.getInstance();

    private void addProductCategory(){
        productCategoryDaoMem.add(new ProductCategory(productCategoryDaoMem.getAll().size()+1,"testPC","test","test"));
    }
    private ProductCategory addProductCategoryAndReturn(){
        ProductCategory productCategory = new ProductCategory(productCategoryDaoMem.getAll().size()+1,"testPC","test","test");
        productCategoryDaoMem.add(productCategory);
        return productCategory;
    }



    @Test
    void getInstance() {
        assertNotNull(ProductCategoryDaoMem.getInstance());
    }

    @Test
    void add() {
        assertEquals(addProductCategoryAndReturn(),productCategoryDaoMem.find(productCategoryDaoMem.getAll().size()));
    }

    @Test
    void find() {
        assertEquals(addProductCategoryAndReturn(),productCategoryDaoMem.find(productCategoryDaoMem.getAll().size()));
    }

    @Test
    void remove() {
        int size = productCategoryDaoMem.getAll().size();
        addProductCategory();
        productCategoryDaoMem.remove(1);
        assertEquals(size,productCategoryDaoMem.getAll().size());
    }
}