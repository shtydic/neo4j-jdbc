package com.shtydic.neo4j.test;

import java.util.ArrayList;
import java.util.List;

import com.shtydic.neo4j.model.AttrDisplayModel;
import com.shtydic.neo4j.model.NodeModel;
import com.shtydic.neo4j.model.RelationInnerGroupModel;
import com.shtydic.neo4j.model.RelationshipModel;
import com.shtydic.neo4j.model.SocialRel;
import com.shtydic.neo4j.service.impl.RelationServiceImpl;
import com.shtydic.neo4j.utils.Master;
import com.shtydic.neo4j.utils.Neo4jConnection;

public class Test {
	/**
	 * 创建节点
	 */
	@org.junit.Test
	public void createNodeTest(){
		Neo4jConnection conn = new Neo4jConnection("192.168.128.59:7474", "neo4j", "tydic");
		PersonTo p1 = new PersonTo("邓艺超", "25");
		conn.createRelationService().createNode(p1,AttrDisplayModel.java_standard);
	}
	/**
	 * 创建索引
	 */
	@org.junit.Test
	public void testObjectAttr(){
		Neo4jConnection conn = new Neo4jConnection("192.168.128.59:7474", "neo4j", "tydic");
		List<String> attrList = new ArrayList<String>();
		attrList.add("name");
		attrList.add("age");
		conn.createRelationService().createIndex("PersonTo", attrList);
	}
	
	/**
	 * 删除节点
	 */
	@org.junit.Test
	public void testRemoveNode(){
		Neo4jConnection conn = new Neo4jConnection("192.168.128.59:7474", "neo4j", "tydic");
		RelationServiceImpl relationServiceImpl = conn.createRelationService();
		PersonTo p1 = new PersonTo("邓艺超","25");
		relationServiceImpl.removeNode(p1,AttrDisplayModel.java_standard);
	}
	
	/**
	 * 创建关系
	 */
	@org.junit.Test
	public void testRelationship(){
		Neo4jConnection conn = new Neo4jConnection("192.168.128.59:7474", "neo4j", "tydic");
		RelationServiceImpl relationServiceImpl = conn.createRelationService();
		PersonTo p1 = new PersonTo("邓艺超", "25");
		GxTest gx = new GxTest("同学", "第一条测试数据");
		PersonTo p2 = new PersonTo("蔡勇", "26");
		relationServiceImpl.createRelation(p1, p2, gx);
	}
	
	@org.junit.Test
	public void testGxDepth(){
		Neo4jConnection conn = new Neo4jConnection("192.168.128.59:7474", "neo4j", "tydic");
		Master master = conn.createMaster();
		PersonTo p1 = new PersonTo("邓艺超", "25");
		GxTest gx = new GxTest();
		PersonTo p2 = new PersonTo("蔡勇", "26");
		NodeModel node_a = master.createNodeModel().createWhereNodeObj("a", p1);
		NodeModel node_b = master.createNodeModel().createWhereNodeObj("b", p2);
		RelationshipModel gx_model = master.createRelationshipModel().createWhereRelationshipObj("r", gx);
		gx_model.setDepth("0..3");
		String neo4jStr = master.start(node_a, gx_model, node_b, Master.DirType.eq).addResult(gx_model).request();
		System.out.println(neo4jStr);
	}
	
	/**
	 * 带条件的语句
	 */
	@org.junit.Test
	public void test_test3(){
		//MATCH(a:Person{gender:'男',name:'童延说'})-[qinshu:SocialRel{}]-(b:Person{}) return (b)
		Person p1 = new Person();
		p1.setGender("男");
		p1.setName("童延说");
		Person p2 = new Person();
		p2.setName("薛");
		SocialRel socialRel = new SocialRel();
		Neo4jConnection conn = new Neo4jConnection("192.168.128.59:7474", "neo4j", "tydic");
		Master master = conn.createMaster();
		NodeModel a = master.createNodeModel().createWhereNodeObj("a", p1);
		NodeModel b = master.createNodeModel().createWhereNodeObj("b", p2);
		
		RelationshipModel socialRel_relation = master.createRelationshipModel().createWhereRelationshipObj("socialRel", socialRel);
		
		
		master.createWhereModel().start(b.addContain("name"));
//		String result = master.start(a, socialRel_relation, b, DirType.eq).addResult(b).createComplexQueryStr();
		String result = master.start(a, socialRel_relation, b, Master.DirType.eq).addResult(b).request();
		System.out.println(result);
		
	}
	
	/**
	 * 群体识别测试
	 */
	@org.junit.Test
	public void test_test4(){
		Neo4jConnection conn = new Neo4jConnection("192.168.128.59:7474", "neo4j", "tydic");
		RelationServiceImpl r = conn.createRelationService();
		RelationInnerGroupModel rg = new RelationInnerGroupModel();
		rg.setDepth(1);
		List<String> pList = new ArrayList<String>();
		pList.add("342622195310307128");
		pList.add("342622199105227091");
		pList.add("740793197207011279");
		pList.add("266242195802036384");
		rg.setIdList(pList);
		rg.setLabelId("ZJHM");
		rg.setLabelName("People");
		String result = r.queryInnerGroup(rg);
		
		System.out.println(result);
	}
	
	@org.junit.Test
	public void testAddContain(){
		Neo4jConnection conn = new Neo4jConnection("192.168.128.59:7474", "neo4j", "tydic");
		Master master = conn.createMaster();
		Person p1 = new Person();
		p1.setGender("男");
		p1.setName("童延说");
		NodeModel node_a = master.createNodeModel().createWhereNodeObj("a", p1);
		node_a.addContain("name");
		
	}
}
