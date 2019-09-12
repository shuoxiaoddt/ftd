package com.xs.middle.compent.ftd.unittest;

import com.xs.middle.compent.ftd.util.CompareUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.assertj.core.util.Throwables;
import org.dom4j.Attribute;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Component;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/**
 * 自定义单元测试监听
 *
 */
@Slf4j
@Component
public class UnitTestExecutionListener extends AbstractTestExecutionListener {

    private PlatformTransactionManager txManager;

    private Connection conn;

    private TransactionStatus status;

    @Override
    public final int getOrder() {
        return 6000;
    }

    @Override
    public void prepareTestInstance(TestContext testContext) {
        try {
            txManager = testContext.getApplicationContext().getBean(PlatformTransactionManager.class);
        } catch (Exception e) {
            log.error("end prepareTestInstance error:{}", Throwables.getStackTrace(e));
            throw e;
        }
    }

    @Override
    public void beforeTestMethod(TestContext testContext) throws DocumentException, SQLException, IOException {
        try {
            log.info("start test method:{}", testContext.getTestMethod());
            status = buildTransactionStatus();
            conn = buildConnection(testContext);
            executeUnitTestAnnotation(testContext);
        } catch (Exception e) {
            log.error("end beforeTestMethod error:{}", Throwables.getStackTrace(e));
            throw e;
        }
    }

    @Override
    public void afterTestMethod(TestContext testContext) throws SQLException, IOException, DocumentException, IllegalAccessException, IntrospectionException, InvocationTargetException {
        try {
            executeCompareAnnotaion(testContext);
            log.info("end test method:{}", testContext.getTestMethod());
        } catch (Exception e) {
            log.error("end afterTestMethod error:{}", Throwables.getStackTrace(e));
            throw e;
        } finally {
            txManager.rollback(status);
            status = null;
            if (!conn.isClosed()) {
                conn.close();
            }
        }
    }

    /**
     * CompareAnnotaion注解动作
     */
    private void executeCompareAnnotaion(TestContext testContext) throws IOException, DocumentException, SQLException, IllegalAccessException, IntrospectionException, InvocationTargetException {
        CompareAnnotaion annotaion = testContext.getTestMethod().getAnnotation(CompareAnnotaion.class);
        if (annotaion != null && annotaion.resource().length() != 0) {
            List<SQLObj> sqlObjs = buildXMLObj(annotaion.resource());
            for (SQLObj sqlObj : sqlObjs) {
                String statement = buildSelSQL(sqlObj);
                if (CompareUtil.compare(sqlObjToMap(sqlObj), runSelSQL(statement, sqlObj), annotaion.convertToString()).size() > 0) {
                    throw new RuntimeException();
                }
            }
        }
    }

    /**
     * 执行UnitTestAnnotation注解动作
     */
    private void executeUnitTestAnnotation(TestContext testContext) throws SQLException, IOException, DocumentException {
        UnitTestAnnotation annotation = testContext.getTestMethod().getAnnotation(UnitTestAnnotation.class);
        if (annotation != null && annotation.resource().length() != 0) {
            List<String> statements = buildInsSQL(annotation.resource());
            runInsSQL(statements);
        }
    }

    /**
     * 创建数据库session连接
     */
    private Connection buildConnection(TestContext testContext) throws SQLException {
        Connection connection = testContext.getApplicationContext().getBean(SqlSessionFactory.class).openSession(false).getConnection();
        connection.setAutoCommit(false);
        return connection;
    }

    /**
     * 创建事物
     */
    private TransactionStatus buildTransactionStatus() {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        def.setIsolationLevel(TransactionDefinition.ISOLATION_READ_UNCOMMITTED);
        return txManager.getTransaction(def);
    }

    /**
     * 生成查询sql
     */
    private String buildSelSQL(SQLObj sqlObj) {
        StringBuffer columns = new StringBuffer();
        StringBuffer conditions = new StringBuffer();
        for (Map.Entry<String, Object> entry : sqlObj.getAttributes().entrySet()) {
            columns = columns.append(",").append(entry.getKey());
            conditions = conditions.append(" and ")
                    .append(entry.getKey())
                    .append(" = ")
                    .append("'")
                    .append(entry.getValue())
                    .append("'");
        }
        for (Map.Entry<String, Object> entry : sqlObj.getElement().entrySet()) {
            columns = columns.append(",").append(entry.getKey());
        }
        return String.format("SELECT %s FROM %s %s;", columns.substring(1), sqlObj.tableName, conditions.length() == 0 ? "" : conditions.replace(0, 4, "WHERE"));
    }

    /**
     * 执行查询sql
     */
    private Map<String, Object> runSelSQL(String statement, SQLObj sqlObj) throws SQLException {
        Map<String, Object> map = new HashMap<>();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(statement);
        while (rs.next()) {
            for (Map.Entry<String, Object> entry : sqlObj.getAttributes().entrySet()) {
                map.put(entry.getKey(), rs.getObject(entry.getKey()));
            }
            for (Map.Entry<String, Object> entry : sqlObj.getElement().entrySet()) {
                map.put(entry.getKey(), rs.getObject(entry.getKey()));
            }
        }
        return map;
    }

    /**
     * 执行插入sql语句
     */
    private void runInsSQL(List<String> statements) throws SQLException {
        Statement stmt = conn.createStatement();
        for (String statement : statements) {
            try {
                stmt.executeUpdate(statement);
                log.info("execute sql statement:{}", statement);
            } catch (SQLException e) {
                log.error("execute sql statement error:{}", Throwables.getStackTrace(e));
                throw e;
            }
        }
    }

    /**
     * 生成插入sql语句
     */
    private List<String> buildInsSQL(String resource) throws IOException, DocumentException {
        List<String> sqlList = new ArrayList<>();
        List<SQLObj> sqlObjs = buildXMLObj(resource);
        for (SQLObj sqlObj : sqlObjs) {
            StringBuffer columns = new StringBuffer();
            StringBuffer values = new StringBuffer();
            for (Map.Entry<String, Object> entry : sqlObj.getAttributes().entrySet()) {
                columns = columns.append(",").append(entry.getKey());
                if ("NULL".equals(entry.getValue().toString().toUpperCase())) {
                    values = values.append(",").append(entry.getValue());
                } else {
                    values = values.append(",").append("'").append(entry.getValue()).append("'");
                }
            }
            for (Map.Entry<String, Object> entry : sqlObj.getElement().entrySet()) {
                columns = columns.append(",").append(entry.getKey());
                if ("NULL".equals(entry.getValue().toString().toUpperCase())) {
                    values = values.append(",").append(entry.getValue());
                } else {
                    values = values.append(",").append("'").append(entry.getValue()).append("'");
                }
            }
            String sql = String.format("INSERT INTO %s(%s) VALUES(%s);", sqlObj.tableName, columns.substring(1), values.substring(1));
            sqlList.add(sql);
        }
        return sqlList;
    }

    /**
     * 读取xml数据为对象
     */
    private List<SQLObj> buildXMLObj(String resource) throws IOException, DocumentException {
        List<SQLObj> sqlObjs = new ArrayList<>();
        try (InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource)) {
            if (null == input) {
                throw new java.io.FileNotFoundException("The resource \"" + resource + "\" not find!");
            }
            Element rootElement = new SAXReader().read(input).getRootElement();
            Iterator rootIterator = rootElement.elementIterator();
            while (rootIterator.hasNext()) {
                SQLObj sqlObj = new SQLObj();
                Element tableElement = (Element) rootIterator.next();
                sqlObj.setTableName(tableElement.getName());
                List<Attribute> tableAttributes = tableElement.attributes();
                if (tableAttributes != null && tableAttributes.size() > 0) {
                    for (Attribute attribute : tableAttributes) {
                        sqlObj.getAttributes().put(attribute.getName(), attribute.getValue());
                    }
                }
                Iterator columnsIterator = tableElement.elementIterator();
                while (columnsIterator.hasNext()) {
                    Element columnsElement = (Element) columnsIterator.next();
                    sqlObj.getElement().put(columnsElement.getName(), columnsElement.getData());
                }
                sqlObjs.add(sqlObj);
            }
            return sqlObjs;
        }
    }

    /**
     * SQLObj转Map
     */
    private Map<String, Object> sqlObjToMap(SQLObj sqlObj) {
        Map<String, Object> map = new HashMap<>();
        for (Map.Entry<String, Object> entry : sqlObj.getAttributes().entrySet()) {
            map.put(entry.getKey(), entry.getValue());
        }
        for (Map.Entry<String, Object> entry : sqlObj.getElement().entrySet()) {
            map.put(entry.getKey(), entry.getValue());
        }
        return map;
    }

    @Data
    private class SQLObj {
        private String tableName;
        private Map<String, Object> attributes = new HashMap<>();
        private Map<String, Object> element = new HashMap<>();
    }
}
