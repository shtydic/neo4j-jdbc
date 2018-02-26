package com.shtydic.neo4j.model;

/**
 * Created by wanghong on 2016/11/25 14:12.
 */
public class RelationshipModel2 extends Neo4jModel {
    private String nodeId1;
    private String nodeId2;
    private String relationship;


    public RelationshipModel2(String nodeId1, String nodeId2, String relationship) {
        this.nodeId1 = nodeId1;
        this.nodeId2 = nodeId2;
        this.relationship = relationship;
    }

    public String getNodeId1() {
        return nodeId1;
    }

    public void setNodeId1(String nodeId1) {
        this.nodeId1 = nodeId1;
    }

    public String getNodeId2() {
        return nodeId2;
    }

    public void setNodeId2(String nodeId2) {
        this.nodeId2 = nodeId2;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }
}
