package com.shtydic.neo4j.model;

/**
 * Created by dengyichao on 2017/2/24.
 */
public class WhereModel {
    //不能给予初始化
    private StringBuilder sb;
    public WhereModel(StringBuilder sb) {
        this.sb = sb;
    }

    //条件开始
    public WhereModel start(String whereStr){
        sb.append(" WHERE ");
        sb.append(whereStr);
        return this;
    }
    //    public WhereUtil include(String whereStr){
//        String str = sb.toString().replaceAll(whereStr,"("+whereStr+")");
//        sb = new StringBuilder(str);
////        sb"("+whereStr+")";
//        return this;
//    }
    public WhereModel or(String whereStr){
        if(whereStr == null || whereStr.length() == 0) throw new RuntimeException("当前属性不存在，或该属性并为赋值！");
        sb.append(" or "+whereStr);
        return this;
    }
    public WhereModel and(String whereStr){
        if(whereStr == null || whereStr.length() == 0) throw new RuntimeException("当前属性不存在，或该属性并为赋值！");
        sb.append(" and "+whereStr);
        return this;
    }
    public WhereModel includeStart(){
        sb.append("(");
//        sb"("+whereStr+")";
        return this;
    }
    public WhereModel includeEnd(){
        sb.append(")");
//        sb"("+whereStr+")";
        return this;
    }
    public WhereModel not(){
        sb.append(" not ");
        return this;
    }


}
