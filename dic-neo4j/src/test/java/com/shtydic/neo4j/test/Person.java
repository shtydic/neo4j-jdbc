package com.shtydic.neo4j.test;

public class Person {
	private String name;
	private String gender;
	private String height;
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	@Override
	public String toString() {
		return "Person [name=" + name + ", gender=" + gender + ", height=" + height + "]";
	}
	
}
