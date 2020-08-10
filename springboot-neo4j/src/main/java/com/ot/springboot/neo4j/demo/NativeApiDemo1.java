package com.ot.springboot.neo4j.demo;


import com.ot.springboot.neo4j.enums.MyLabel;
import com.ot.springboot.neo4j.enums.MyRelationships;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.io.File;

public class NativeApiDemo1 {

    public static void main(String[] args) {
        GraphDatabaseFactory databaseFactory = new GraphDatabaseFactory();
        //创建数据库
        GraphDatabaseService db = databaseFactory
                .newEmbeddedDatabase(new File("E:\\develop\\Neo4j\\neo4j-community-3.5.0-windows\\data\\databases\\test.db"));
        Transaction tx = db.beginTx();
        try {
            //创建2个新节点
            Node javaNode = db.createNode(MyLabel.JAVA);
            Node scalaNode = db.createNode(MyLabel.SCALA);
            //为新创建的2个节点创建关系
            javaNode.setProperty("TutorialID", "JAVA001");
            javaNode.setProperty("Title", "Learn Java");
            javaNode.setProperty("NoOfChapters", "25");
            javaNode.setProperty("Status", "Completed");

            scalaNode.setProperty("TutorialID", "SCALA001");
            scalaNode.setProperty("Title", "Learn Scala");
            scalaNode.setProperty("NoOfChapters", "20");
            scalaNode.setProperty("Status", "Completed");

            //创建节点与节点之间的关系,并为关系设置属性
            Relationship relationship = javaNode.createRelationshipTo(scalaNode, MyRelationships.JVM_LANGIAGES);
            relationship.setProperty("Id", "1234");
            relationship.setProperty("OOPS", "YES");
            relationship.setProperty("FP", "YES");

            tx.success();
        } catch (Exception e) {
            tx.failure();
        } finally {

        }

    }
}
