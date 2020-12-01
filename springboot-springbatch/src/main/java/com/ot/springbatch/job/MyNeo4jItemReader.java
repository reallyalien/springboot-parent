package com.ot.springbatch.job;

import org.neo4j.driver.*;
//import org.neo4j.ogm.session.SessionFactory;
import org.springframework.batch.item.data.AbstractNeo4jItemReader;
import org.springframework.batch.item.data.AbstractPaginatedDataItemReader;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Field;
import java.util.*;

public class MyNeo4jItemReader extends AbstractPaginatedDataItemReader<com.ot.springbatch.job.Record>{

    private Driver driver;

    private String startStatement;
    private String returnStatement;
    private String matchStatement;
    private String whereStatement;
    private String orderByStatement;

    private static ThreadLocal<Integer> local=new ThreadLocal<>();

    /**
     * The start segment of the cypher query.  START is prepended
     * to the statement provided and should <em>not</em> be
     * included.
     *
     * @param startStatement the start fragment of the cypher query.
     */
    public void setStartStatement(String startStatement) {
        this.startStatement = startStatement;
    }

    /**
     * The return statement of the cypher query.  RETURN is prepended
     * to the statement provided and should <em>not</em> be
     * included
     *
     * @param returnStatement the return fragment of the cypher query.
     */
    public void setReturnStatement(String returnStatement) {
        this.returnStatement = returnStatement;
    }

    /**
     * An optional match fragment of the cypher query.  MATCH is
     * prepended to the statement provided and should <em>not</em>
     * be included.
     *
     * @param matchStatement the match fragment of the cypher query
     */
    public void setMatchStatement(String matchStatement) {
        this.matchStatement = matchStatement;
    }

    /**
     * An optional where fragment of the cypher query.  WHERE is
     * prepended to the statement provided and should <em>not</em>
     * be included.
     *
     * @param whereStatement where fragment of the cypher query
     */
    public void setWhereStatement(String whereStatement) {
        this.whereStatement = whereStatement;
    }

    /**
     * A list of properties to order the results by.  This is
     * required so that subsequent page requests pull back the
     * segment of results correctly.  ORDER BY is prepended to
     * the statement provided and should <em>not</em> be included.
     *
     * @param orderByStatement order by fragment of the cypher query.
     */
    public void setOrderByStatement(String orderByStatement) {
        this.orderByStatement = orderByStatement;
    }

    @Override
    protected Iterator<com.ot.springbatch.job.Record> doPageRead() {
        Session session = this.driver.session();

        Map<String, Object> params = new HashMap<>();
        Result result = session.run(generateLimitCypherQuery(), params);
        System.out.println("执行的cql语句：" + generateLimitCypherQuery());
        List<com.ot.springbatch.job.Record> records = handlerResultSet(result);
        return records.iterator();
    }

    protected String generateLimitCypherQuery() {
        StringBuilder query = new StringBuilder();

        query.append("START ").append(startStatement);
        query.append(matchStatement != null ? " MATCH " + matchStatement : "");
        query.append(whereStatement != null ? " WHERE " + whereStatement : "");
        query.append(" RETURN ").append(returnStatement);
        query.append(" ORDER BY ").append(orderByStatement);
        query.append(" SKIP " + (pageSize * page));
        query.append(" LIMIT " + pageSize);

        String resultingQuery = query.toString();


        return resultingQuery;
    }

    @Override
    protected void doClose() throws Exception {
        this.page = 0;
        this.results = null;
    }

    private static List<Record> handlerResultSet(Result result) {
        String idK = "id(p)";
        String propK = "properties(p)";
        List<org.neo4j.driver.Record> list = result.list();
        List<Record> etlRecords = new ArrayList<>();
        Record etlRecord = null;
        Map<String, Object> targetMap = null;
        Integer lastId=null;
        for (org.neo4j.driver.Record record : list) {
            Integer id = record.get(idK).asInt();
            lastId=id;
            Map<String, Object> objectMap = record.get(propK).asMap();
            targetMap = new HashMap<>();
            //objectMap不可被修改，因此将数据拷贝到另一个map
            mapCopy(objectMap, targetMap);
            targetMap.put("id", id);
            etlRecord = new DefaultRecord();
            etlRecord.setColumnAndValues(targetMap);
            etlRecord.setColumnNames(new ArrayList<>(targetMap.keySet()));
            etlRecords.add(etlRecord);
        }
        if (lastId!=null){
            local.set(lastId);
        }
        System.out.println("最后一次读取的数据的id："+local.get());
        return etlRecords;
    }

    private static void mapCopy(Map paramsMap, Map resultMap) {
        if (resultMap == null) resultMap = new HashMap();
        if (paramsMap == null) return;
        Iterator it = paramsMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            Object key = entry.getKey();
            resultMap.put(key, paramsMap.get(key) != null ? paramsMap.get(key) : "");
        }
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public static void main(String[] args) {
        MyNeo4jItemReader reader = new MyNeo4jItemReader();


    }

}
