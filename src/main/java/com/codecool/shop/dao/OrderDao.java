package com.codecool.shop.dao;

import com.codecool.shop.model.WsOrder;
import java.math.BigDecimal;


public interface OrderDao extends BaseDAO<WsOrder> {

    WsOrder getCurrent();

    BigDecimal getTotalPrice();
}
