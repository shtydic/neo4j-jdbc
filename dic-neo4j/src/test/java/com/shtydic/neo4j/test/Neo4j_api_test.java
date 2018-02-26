package com.shtydic.neo4j.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.junit.Test;

import com.shtydic.neo4j.model.MatchModel.DirType;
import com.shtydic.neo4j.model.NodeModel;
import com.shtydic.neo4j.model.RelationInnerGroupModel;
import com.shtydic.neo4j.model.RelationshipModel;
import com.shtydic.neo4j.model.SocialRel;
import com.shtydic.neo4j.service.RelationService;
import com.shtydic.neo4j.service.impl.RelationServiceImpl;
import com.shtydic.neo4j.utils.Constants;
import com.shtydic.neo4j.utils.Master;
import com.shtydic.neo4j.utils.Neo4jConnection;

public class Neo4j_api_test {
	@Test
	public void test1(){
//		Person p = new Person();
//		p.setGender("男");
//		p.setHeight("1.96m");
//		p.setName("李");
//		
//		Person guanxi = new Person();
//		guanxi.setGender("男");
//		guanxi.setHeight("1.96m");
//		guanxi.setName("我是关系");
//		
//		Person guanxi2 = new Person();
//		guanxi2.setGender("男");
//		guanxi2.setHeight("1.96m");
//		guanxi2.setName("我是关系22222");
//		
//		Person p2 = new Person();
//		p2.setGender("男");
//		p2.setHeight("1.96m");
//		p2.setName("22222");
//		
//		Master m = new Master();
//		NodeModel nodel = m.createNodeModel().createWhereNodeObj("p", p);
//		System.out.println("节点：" + nodel.nodeStr());
//		
//		NodeModel nodel2 = m.createNodeModel().createWhereNodeObj("p2", p2);
//		RelationshipModel r = m.createRelationshipModel().createWhereRelationshipObj("guanxi", guanxi);
//		RelationshipModel r2 = m.createRelationshipModel().createWhereRelationshipObj("guanxi2222", guanxi2);
//		
//		String where1 = nodel.addContain("name");
//		String where2 = nodel2.addGt("height");
//		String where3 = r.addGtEq("height");
//		m.createWhereModel().includeStart().start(where1).and(where2).includeEnd().or(where3);
//		
//		String a = m.start(nodel, r, nodel2, DirType.eq).addNote(r2, nodel).addResult(nodel).createComplexQueryStr();
//		System.out.println(a);
		
	}
	
	@Test
	public void test_test(){
//		Person p1 = new Person();
//		p1.setGender("男");
//		p1.setHeight("1.96m");
//		p1.setName("小明");
//		
//		Relatives r = new Relatives();
//		r.setName("朋友");
//		
//		Person p2 = new Person();
//		p2.setGender("女");
//		p2.setName("李");
//		
//		
//		Master master = new Master();
//		NodeModel xiaoming_node = master
//								.createNodeModel()
//								.createWhereNodeObj("xiaoming", p1);
//		NodeModel li_node = master
//							.createNodeModel()
//							.createWhereNodeObj("li", p2);
//		RelationshipModel qinshu_relation = master
//											.createRelationshipModel()
//											.createWhereRelationshipObj("qinshu", r);
//		String t1 = li_node.addContain("name");
//		master.createWhereModel().start(t1);
//		String neo4j = master.start(xiaoming_node, qinshu_relation, li_node, DirType.eq).addResult(li_node).createComplexQueryStr();
//		System.out.println(neo4j);
	}
	
	@Test
	public void test_test2(){
		/*Person p1 = new Person();
		p1.setGender("男");
		p1.setHeight("1.96m");
		p1.setName("小明");
		
		Person p2 = new Person();
		p2.setGender("女");
		p2.setName("李");
		
		Person p3 = new Person();
		
		Relatives r = new Relatives();
		r.setName("朋友");
		
		Relatives r2 = new Relatives();
		r2.setName("同学");
		
		Neo4jConnection conn = new Neo4jConnection("192.168.128.59", "neo4j", "tydic");
		Master master = conn.createMaster();
		NodeModel xiaoming_node = master.createNodeModel().createWhereNodeObj("xiaoming", p1);
		NodeModel p3_node = master.createNodeModel().createWhereNode(p3);
		NodeModel li_node = master.createNodeModel().createWhereNodeObj("li", p2);
		
		RelationshipModel pengyou_relation = master.createRelationshipModel().createWhereRelationshipObj("pengyou", r);
		RelationshipModel tongxue_relation = master.createRelationshipModel().createWhereRelationshipObj("tongxue", r2);
		
		String t1 = li_node.addContain("name");
		master.createWhereModel().start(t1);
		String neo4j = master.start(xiaoming_node, tongxue_relation, p3_node, DirType.eq)
							.addNote(pengyou_relation, li_node).addResult(li_node).createComplexQueryStr();
		
		
//		String neo4j_url = "192.168.128.59:7474"; 
//		String neo4j_json = master.start(xiaoming_node, tongxue_relation, p3_node, DirType.eq)
//				.addNote(pengyou_relation, li_node).addResult(li_node).request(neo4j_url);
//		System.out.println(neo4j);
*/	}
	
	@Test
	public void test_test3(){
		//MATCH(a:Person{gender:'男',name:'童延说'})-[qinshu:SocialRel{}]-(b:Person{}) return (b)
		Person p1 = new Person();
		p1.setGender("男");
		p1.setName("童延说");
		
		Person p2 = new Person();
		
		SocialRel socialRel = new SocialRel();
//		socialRel.setName("gx测试");
		
		Neo4jConnection conn = new Neo4jConnection("192.168.128.59:7474", "neo4j", "tydic");
		Master master = conn.createMaster();
		NodeModel a = master.createNodeModel().createWhereNodeObj("a", p1);
		NodeModel b = master.createNodeModel().createWhereNodeObj("b", p2);
		
		RelationshipModel socialRel_relation = master.createRelationshipModel().createWhereRelationshipObj("socialRel", socialRel);
		socialRel_relation.setDepth("0..1");
//		master.createWhereModel().start(socialRel_relation.addContain("name"));
//		String result = master.start(a, socialRel_relation, b, DirType.eq).addResult(b).createComplexQueryStr();
		String result = master.start(a, socialRel_relation, b, Master.DirType.eq).addResult(b).request();
		System.out.println(result);
		
	}
	
	@Test
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
	
	@Test
	public void test2(){
		RelationService service = new RelationServiceImpl();
		List<String> list = service.findAttrByNodeType("People", "ZJHM");
		Random r = new Random();
		for(int i = 0 ; i < 101 ; i++){
			list.get(r.nextInt(list.size()-1));
			System.out.println(list.get(r.nextInt(list.size()-1)));
		}
	}
	
	@Test
	public void test3(){
		RelationService service = new RelationServiceImpl();
		service.createDas5RRGXById("610909090995729540", "610909090989562953", "BO3", "火车同行2");
	}
	
	@Test
	public void insertRelation(){
		RelationService service = new RelationServiceImpl();
		List<String> zjhmlist = service.findAttrByNodeType("People","ZJHM");
		java.util.Map<String, String> gxMap = Constants.getAllGXMCMap();
		Set<String> gxbmSet = gxMap.keySet();
		List<String> gxbmList = new ArrayList<String>(gxbmSet);
		Random zjhm1 = new Random();
		Random zjhm2 = new Random();
		Random gxbmR = new Random();
		for(int i = 0; i < 100000; i++){
			String gx = gxbmList.get(gxbmR.nextInt(gxbmList.size()-1));
			String gxmc = gxMap.get(gx);
			if(gx.startsWith("B")||gx.startsWith("C")){
				String zjhm1Str = zjhmlist.get(zjhm1.nextInt(zjhmlist.size()-1));
				String zjhm2Str = zjhmlist.get(zjhm2.nextInt(zjhmlist.size()-1));
				service.createDas5RRGXById(zjhm1Str, zjhm2Str, gx, gxmc);
				System.out.println((i+1));
			}
			
		}
	}
	
	@Test
	public void test_test5(){
		People a = new People("610909090931132412");
		List<String> gxList = new ArrayList<String>();
		People b = new People("610909090963856658");
		gxList.add("C01");
		gxList.add("B05");
		//创建neo4j连接
		Neo4jConnection conn = new Neo4jConnection("192.168.128.59:7474", "neo4j", "tydic");
		String resultStr = conn.createRelationService().selectDepthOwnQuery(a, gxList, 1, null, RelationService.Neo4jResult.all_result);	
		System.out.println(resultStr);
	}
}
