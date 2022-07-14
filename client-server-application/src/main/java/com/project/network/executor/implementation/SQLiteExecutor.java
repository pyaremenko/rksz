package com.project.network.executor.implementation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import com.project.network.executor.SQLExecutor;
import com.project.network.dao.RowMapper;

public class SQLiteExecutor implements SQLExecutor {
    private static String databaseName = null;
    private static Connection connection;

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void setDatabaseName(final String name) {
        try {
            if (connection != null) {
                connection.close();
            }
            databaseName = name;
            connection = DriverManager.getConnection("jdbc:sqlite:" + databaseName);
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void checkDatabaseName() {
        if (databaseName == null || connection == null) {
            System.out.println("No database setted");
            System.exit(1);
        }
    }

    public <T> List<T> executeQuery(final String sql, final RowMapper<T> mapper)
            throws SQLException {
        checkDatabaseName();
        try (PreparedStatement statement = connection.prepareStatement(sql);) {
            LinkedList<T> result = new LinkedList<>();
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    result.add(mapper.map(rs));
                }
            }
            return result;
        }
    }

    @Override
    public <T> List<T> executeQuery(String sql, List<Object> parameterList, RowMapper<T> mapper)
            throws SQLException {
        checkDatabaseName();
        try (PreparedStatement statement = connection.prepareStatement(sql);) {
            for (int i = 1; i <= parameterList.size(); ++i) {
                statement.setObject(i, parameterList.get(i - 1));
            }
            LinkedList<T> result = new LinkedList<>();
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    result.add(mapper.map(rs));
                }
            }
            return result;
        }
    }

    @Override
    public int update(String sql) throws SQLException {
        checkDatabaseName();
        try (PreparedStatement statement = connection.prepareStatement(sql);) {
            return statement.executeUpdate();
        }
    }

    @Override
    public int update(String sql, List<Object> parameterList) throws SQLException {
        checkDatabaseName();
        try (PreparedStatement statement = connection.prepareStatement(sql);) {
            for (int i = 1; i <= parameterList.size(); ++i) {
                statement.setObject(i, parameterList.get(i - 1));
            }
            return statement.executeUpdate();
        }
    }

    @Override
    public void query(String sql) throws SQLException {
        checkDatabaseName();
        try (PreparedStatement statement = connection.prepareStatement(sql);) {
            statement.execute();
        }
    }

    @Override
    public void query(String sql, List<Object> parameterList) throws SQLException {
        checkDatabaseName();
        try (PreparedStatement statement = connection.prepareStatement(sql);) {
            for (int i = 1; i <= parameterList.size(); ++i) {
                statement.setObject(i, parameterList.get(i - 1));
            }
            statement.execute();
        }
    }

}
