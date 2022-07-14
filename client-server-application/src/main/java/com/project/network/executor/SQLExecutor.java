package com.project.network.executor;

import java.sql.SQLException;
import java.util.List;

import com.project.network.dao.RowMapper;

public interface SQLExecutor {
        <T> List<T> executeQuery(final String sql, final RowMapper<T> mapper) throws SQLException;

        <T> List<T> executeQuery(final String sql, final List<Object> parameterList, final RowMapper<T> mapper)
                        throws SQLException;

        int update(final String sql) throws SQLException;

        int update(final String sql, final List<Object> parameterList) throws SQLException;

        void query(final String sql) throws SQLException;

        void query(final String sql, final List<Object> parameterList) throws SQLException;
}