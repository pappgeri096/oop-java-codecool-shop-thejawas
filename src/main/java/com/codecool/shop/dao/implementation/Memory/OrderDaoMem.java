package com.codecool.shop.dao.implementation.Memory;

import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.model.order_model.BaseOrder;

import java.util.ArrayList;
import java.util.List;

public class OrderDaoMem implements OrderDao {
    private List<BaseOrder> data = new ArrayList<>();
    private static OrderDao instance = null;

    /* A private Constructor prevents any other class from instantiating.
     */
    private OrderDaoMem() {
    }

    public static OrderDao getInstance() {
        if (instance == null) {
            instance = new OrderDaoMem();
        }
        return instance;
    }

    @Override
    public BaseOrder getCurrent() {
        return data.get(data.size() - 1);
    }

    @Override
    public void add(BaseOrder objectType) {
        objectType.setId(data.size() + 1);
        data.add(objectType);
    }

    @Override
    public BaseOrder find(int id) {
        return data.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
    }

    @Override
    public void remove(int id) {
        data.remove(find(id));
    }

    @Override
    public List<BaseOrder> getAll() {
        return data;
    }
}
