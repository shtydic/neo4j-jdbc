package com.shtydic.neo4j.model;

import java.util.List;

/**
 * Created by shtydic-a on 2017/3/2.
 */
public class RelationInnerGroupModel {
    private List<String> idList;    
    private String labelName;
    private String labelId;
    private List<String> relationType;
    private int depth;
    
    public RelationInnerGroupModel(){}
    
    public RelationInnerGroupModel(List<String> idList, String labelName, String labelId, List<String> relationType, int depth) {
        this.idList = idList;
        this.labelName = labelName;
        this.labelId = labelId;
        this.relationType = relationType;
        this.depth = depth;
    }

    public List<String> getIdList() {
        return idList;
    }

    public void setIdList(List<String> idList) {
        this.idList = idList;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public String getLabelId() {
        return labelId;
    }

    public void setLabelId(String labelId) {
        this.labelId = labelId;
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

	@Override
	public String toString() {
		return "RelationInnerGroupModel [idList=" + idList + ", labelName=" + labelName + ", labelId=" + labelId
				+ ", relationType=" + relationType + ", depth=" + depth + "]";
	}
    
}
