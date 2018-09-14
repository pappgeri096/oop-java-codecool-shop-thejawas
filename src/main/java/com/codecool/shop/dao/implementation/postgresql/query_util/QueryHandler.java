package com.codecool.shop.dao.implementation.postgresql.query_util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class QueryHandler extends DaoSqlConnectionDML {

    protected void executeIsertUpdateOrDelete(String query) {

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()
        ) {
            statement.executeUpdate(query);
            Class.forName("org.postgresql.Driver");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


}
