package com.shtydic.neo4j.utils;

import org.apache.commons.lang.StringUtils;

import com.shtydic.neo4j.service.impl.RelationServiceImpl;

public class Neo4jConnection {
	private String neo4j_ip_port;
	private String user;
	private String password;
	
	public Neo4jConnection() {
		super();
	}
	
	
	public Neo4jConnection(String neo4j_ip_port,String user,String password){
		this.neo4j_ip_port = neo4j_ip_port;
		this.user = user;
		this.password = password;
	}
	
	public Master createMaster(){
		if(StringUtils.isNotBlank(neo4j_ip_port) && StringUtils.isNotBlank(user)  &&  StringUtils.isNotBlank(password)){
			return new Master(neo4j_ip_port,user,password);
		}
		return null;
	}
	
	public RelationServiceImpl createRelationService(){
		return new RelationServiceImpl(neo4j_ip_port,user,password);
	}
	
	
	
	
	

	public String getNeo4j_ip_port() {
		return neo4j_ip_port;
	}

	public void setNeo4j_ip_port(String neo4j_ip_port) {
		this.neo4j_ip_port = neo4j_ip_port;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	
}
