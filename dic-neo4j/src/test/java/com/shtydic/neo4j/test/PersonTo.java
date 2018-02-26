package com.shtydic.neo4j.test;

public class PersonTo {
	private String name;
	private String age;
	
	public PersonTo() {
		super();
	}
	public PersonTo(String name, String age) {
		super();
		this.name = name;
		this.age = age;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	@Override
	public String toString() {
		return "PersonTo [name=" + name + ", age=" + age + "]";
	}
	
}
