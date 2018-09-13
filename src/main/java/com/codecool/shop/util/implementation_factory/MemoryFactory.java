package com.codecool.shop.util.implementation_factory;

import com.codecool.shop.dao.*;
import com.codecool.shop.dao.implementation.Memory.*;

public class MemoryFactory implements ImplementationFactory {
    @Override
    public CartDao getCartDataManagerInstance() {
        return CartDaoMem.getInstance();
    }

    @Override
    public CustomerDao getCustomerDataManagerInstance() {
        return CustomerDaoMem.getInstance();
    }

    @Override
    public ProductCategoryDao getProductCategoryDataManagerInstance() {
        return ProductCategoryDaoMem.getInstance();
    }

    @Override
    public ProductDao getProductDataManagerInstance() {
        return ProductDaoMem.getInstance();
    }

    @Override
    public SupplierDao getSupplierDataManagerInstance() {
        return SupplierDaoMem.getInstance();
    }
}
