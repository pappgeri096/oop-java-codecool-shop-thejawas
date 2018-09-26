package com.codecool.shop.util.implementation_factory;

import com.codecool.shop.dao.*;

public interface ImplementationFactory {

    CartDao getCartDataManagerInstance();
    CustomerDao getCustomerDataManagerInstance();
    ProductCategoryDao getProductCategoryDataManagerInstance();
    ProductDao getProductDataManagerInstance();
    SupplierDao getSupplierDataManagerInstance();
}
