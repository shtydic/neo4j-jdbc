package com.shtydic.neo4j.model;

import java.util.List;

/**
 * Created by shtydic-a on 2017/3/1.
 */
public class RelationTwoGroupModel {
    /**
     * 应用场景，查询出两个群体的关系节点
     */
    private List<String> idList1;
    private List<String> idList2;
    private String label1Name;
    private String label2Name;
    private String label1Id;
    private String label2Id;
    private List<String> relationType;
    private int depth;

    public RelationTwoGroupModel(List<String> idList1, List<String> idList2, String label1Name, String label2Name, String label1Id, String label2Id, List<String> relationType, int depth) {
        this.idList1 = idList1;
        this.idList2 = idList2;
        this.label1Name = label1Name;
        this.label2Name = label2Name;
        this.label1Id = label1Id;
        this.label2Id = label2Id;
        this.relationType = relationType;
        this.depth = depth;
    }

    public RelationTwoGroupModel(List<String> idList1, List<String> idList2, String label1Name, String label2Name, String label1Id, String label2Id, int depth) {
        this.idList1 = idList1;
        this.idList2 = idList2;
        this.label1Name = label1Name;
        this.label2Name = label2Name;
        this.label1Id = label1Id;
        this.label2Id = label2Id;
        this.depth = depth;
    }

    public List<String> getIdList1() {
        return idList1;
    }

    public void setIdList1(List<String> idList1) {
        this.idList1 = idList1;
    }

    public List<String> getIdList2() {
        return idList2;
    }

    public void setIdList2(List<String> idList2) {
        this.idList2 = idList2;
    }

    public String getLabel1Name() {
        return label1Name;
    }

    public void setLabel1Name(String label1Name) {
        this.label1Name = label1Name;
    }

    public String getLabel2Name() {
        return label2Name;
    }

    public void setLabel2Name(String label2Name) {
        this.label2Name = label2Name;
    }

    public String getLabel1Id() {
        return label1Id;
    }

    public void setLabel1Id(String label1Id) {
        this.label1Id = label1Id;
    }

    public String getLabel2Id() {
        return label2Id;
    }

    public void setLabel2Id(String label2Id) {
        this.label2Id = label2Id;
    }

    public List<String> getRelationType() {
        return relationType;
    }

    public void setRelationType(List<String> relationType) {
        this.relationType = relationType;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }
}
