package com.shtydic.neo4j.model;




import java.util.ArrayList;
import java.util.List;

import com.shtydic.neo4j.utils.MatchBuilder;

/**
 * Created by wanghong on 2016/11/22 11:00.
 */
public class QueryModel extends Neo4jModel {
    public static final String START = "START";
    public static final String MATCH = "MATCH";
    public static final String WHERE = "WHERE";
    public static final String RETURN = "RETURN";
    private SelectType selectType;
    private List<Long> ids;
    private List<StartModel> startModels = new ArrayList<>();
    private List<MatchModel> matchModels = new ArrayList<>();
    private List<WhereModel2> whereModels = new ArrayList<>();
    private List<ReturnModel2> returnModels = new ArrayList<>();

    private String matchStr;
    public String getMatchStr() {
        return matchStr;
    }

    public void setMatchStr(String matchStr) {
        this.matchStr = matchStr;
    }




    /*getter setter*/
    public List<ReturnModel2> getReturnModels() {
        return returnModels;
    }

    public void setReturnModels(List<ReturnModel2> returnModels) {
        this.returnModels = returnModels;
    }

    public List<WhereModel2> getWhereModels() {
        return whereModels;
    }

    public void setWhereModels(List<WhereModel2> whereModels) {
        this.whereModels = whereModels;
    }

    public SelectType getSelectType() {
        return selectType;
    }

    public void setSelectType(SelectType selectType) {
        this.selectType = selectType;
    }

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }

    public List<StartModel> getStartModels() {
        return startModels;
    }

    public void setStartModels(List<StartModel> startModels) {
        this.startModels = startModels;
    }

    public List<MatchModel> getMatchModels() {
        return matchModels;
    }

    public void setMatchModels(List<MatchModel> matchModels) {
        this.matchModels = matchModels;
    }

    public MatchBuilder createMatchBuilder() {
        return new MatchBuilder();
    }
}
