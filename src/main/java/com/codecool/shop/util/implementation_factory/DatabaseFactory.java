package com.codecool.shop.util.implementation_factory;

import com.codecool.shop.dao.*;
import com.codecool.shop.dao.implementation.postgresql.*;

public class DatabaseFactory implements ImplementationFactory {
    @Override
    public CartDao getCartDataManagerInstance() {
        return CartDaoSql.getInstance();
    }

    @Override
    public CustomerDao getCustomerDataManagerInstance() {
        return CustomerDaoSql.getInstance();
    }

    @Override
    public ProductCategoryDao getProductCategoryDataManagerInstance() {
        return ProductCategoryDaoSql.getInstance();
    }

    @Override
    public ProductDao getProductDataManagerInstance() {
        return ProductDaoSql.getInstance();
    }

    @Override
    public SupplierDao getSupplierDataManagerInstance() {
        return SupplierDaoSql.getInstance();
    }

}
