package com.codecool.shop.dao;

import com.codecool.shop.model.order_model.BaseOrder;


public interface OrderDao extends BaseDAO<BaseOrder> {

    BaseOrder getCurrent();
}
