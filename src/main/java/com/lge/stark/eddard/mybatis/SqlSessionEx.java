package com.lge.stark.eddard.mybatis;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;

public class SqlSessionEx implements SqlSession {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(SqlSessionEx.class);

    private SqlSession parent;

    public SqlSessionEx(SqlSession session, boolean readOnly) {
        parent = session;

        try {
            parent.getConnection().setReadOnly(readOnly);
        }
        catch (SQLException e) {
            logger.error(e.getMessage(), e);
            parent = null;
        }
    }

    public SqlSessionEx(SqlSession session) {
        parent = session;
    }

    @Override
    public <T> T selectOne(String statement) {
        return parent.selectOne(statement);
    }

    @Override
    public <T> T selectOne(String statement, Object parameter) {
        return parent.selectOne(statement, parameter);
    }

    @Override
    public <E> List<E> selectList(String statement) {
        return parent.selectList(statement);
    }

    @Override
    public <E> List<E> selectList(String statement, Object parameter) {
        return parent.selectList(statement, parameter);
    }

    @Override
    public <E> List<E> selectList(String statement, Object parameter, RowBounds rowBounds) {
        return parent.selectList(statement, parameter, rowBounds);
    }

    @Override
    public <K, V> Map<K, V> selectMap(String statement, String mapKey) {
        return parent.selectMap(statement, mapKey);
    }

    @Override
    public <K, V> Map<K, V> selectMap(String statement, Object parameter, String mapKey) {
        return parent.selectMap(statement, parameter, mapKey);
    }

    @Override
    public <K, V> Map<K, V> selectMap(String statement, Object parameter, String mapKey, RowBounds rowBounds) {
        return parent.selectMap(statement, parameter, mapKey, rowBounds);
    }

    @Override
    public void select(String statement, Object parameter, ResultHandler handler) {
        parent.select(statement, parameter, handler);
    }

    @Override
    public void select(String statement, ResultHandler handler) {
        parent.select(statement, handler);
    }

    @Override
    public void select(String statement, Object parameter, RowBounds rowBounds, ResultHandler handler) {
        parent.select(statement, parameter, rowBounds, handler);
    }

    @Override
    public int insert(String statement) {
        return parent.insert(statement);
    }

    @Override
    public int insert(String statement, Object parameter) {
        return parent.insert(statement, parameter);
    }

    @Override
    public int update(String statement) {
        return parent.update(statement);
    }

    @Override
    public int update(String statement, Object parameter) {
        return parent.update(statement, parameter);
    }

    @Override
    public int delete(String statement) {
        return parent.delete(statement);
    }

    @Override
    public int delete(String statement, Object parameter) {
        return parent.delete(statement, parameter);
    }

    @Override
    public void commit() {
        parent.commit();
    }

    @Override
    public void commit(boolean force) {
        parent.commit(force);
    }

    @Override
    public void rollback() {
        parent.rollback();
    }

    @Override
    public void rollback(boolean force) {
        parent.rollback(force);
    }

    @Override
    public List<BatchResult> flushStatements() {
        return parent.flushStatements();
    }

    public void close() {
        try {
            parent.getConnection().setReadOnly(false);
        }
        catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
        parent.close();
    }

    @Override
    public void clearCache() {
        parent.clearCache();
    }

    @Override
    public Configuration getConfiguration() {
        return parent.getConfiguration();
    }

    @Override
    public <T> T getMapper(Class<T> type) {
        return parent.getMapper(type);
    }

    @Override
    public Connection getConnection() {
        return parent.getConnection();
    }
}