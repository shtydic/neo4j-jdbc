package com.shtydic.neo4j.utils;

import com.shtydic.neo4j.model.Attrs;
import com.shtydic.neo4j.model.MatchModel;
import com.shtydic.neo4j.model.Neo4jModel;
import com.shtydic.neo4j.model.NodeModel2;
import com.shtydic.neo4j.model.QueryModel;
import com.shtydic.neo4j.model.RelationshipModel2;
import com.shtydic.neo4j.model.StartModel;



/**
 * Created by wanghong on 2016/11/22 11:14.
 */
public class Neo4jBuilder extends QueryBuilder {

    public static void main(String[] args) {

        Neo4jBuilder nb = new Neo4jBuilder();
        //查询语句
        QueryModel qm = new QueryModel();
        StartModel sm = new StartModel("a", "");
        sm.add("a", "=", 18);
        qm.getStartModels().add(sm);
        qm.setSelectType(Neo4jModel.SelectType.node);
        System.out.println(nb.getQuery(qm));
        //节点写入语句
        Attrs attrs = new Attrs();
        attrs.addAttr("name", "张三");
        attrs.addAttr("born", 1980);
        NodeModel2 nodeModel = new NodeModel2("p11", "Person", attrs.toString());


       /*
        String str = qm.createMatchBuilder().nexusStart("n", "同学", "s", MatchModel.DirType.right)
                .addNote("老s爸", "x")
                .addNote("同s事", "v")
                .addNote("宠s物", "b")
                .createComplexQueryStr();
        qm.setMatchStr(str);

        String strS = nb.getQuery(qm);
        System.out.println(strS);

                 */

        qm.setMatchStr(qm.createMatchBuilder().macthPath("s","n",15, MatchModel.MathPath.minimunAll));
       String strS =  nb.getQuery(qm);
        System.out.println(strS);


        System.out.println(nb.getInsertNode(nodeModel));
        //关系写入语句
        System.out.println(nb.getInsertRelationship(new RelationshipModel2("p1", "p2", "同事")));
    }

    /**
     * 获取查询语句
     *
     * @param qm
     * @return
     */
    public String getQuery(QueryModel qm) {
        StringBuilder stringBuilder = new StringBuilder();
        if (!qm.getStartModels().isEmpty()) {
            stringBuilder.append(builderStart(qm));
            stringBuilder.append(" ");
        }
        if (qm.getMatchStr()!= null && qm.getMatchStr().trim().length()>0) {
            stringBuilder.append(builderMatch(qm));
            stringBuilder.append(" ");
        }
        if (!qm.getWhereModels().isEmpty()) {
            stringBuilder.append(builderWhere(qm));
            stringBuilder.append(" ");
        }
        if (!qm.getReturnModels().isEmpty()) {

            stringBuilder.append(builderReturn(qm));
            stringBuilder.append(" ");
        }
        return stringBuilder.toString();
    }

    /**
     * 获取写入节点语句
     *
     * @param nm
     * @return
     */
    public String getInsertNode(NodeModel2 nm) {
        StringBuilder stringBuilder = new StringBuilder(nm.CREATE);
        stringBuilder.append(" (").append(nm.getId()).append(":").append(nm.getName()).append(nm.getProperty()).append(")");
        return stringBuilder.toString();
    }

    /**
     * 获取写入节点语句
     *
     * @param nm
     * @return
     */
    public String getInsertNodeEx(NodeModel2 nm) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(" (").append(nm.getId()).append(":").append(nm.getName()).append(nm.getProperty()).append(")");
        return stringBuilder.toString();
    }

    /**
     * 获取写入关系语句
     *
     * @param nm
     * @return
     */
    public String getInsertRelationship(RelationshipModel2 nm) {
        StringBuilder stringBuilder = new StringBuilder(nm.CREATE);
        stringBuilder.append(" (").append(nm.getNodeId1()).append(")-[:").append(nm.getRelationship()).append("]->(").append(nm.getNodeId2()).append(")");
        return stringBuilder.toString();
    }
}
