package com.shtydic.neo4j.test;

public class GxTest {
	private String gxName;
	private String gxInfo;
	
	
	public GxTest() {
		super();
	}
	public GxTest(String gxName, String gxInfo) {
		super();
		this.gxName = gxName;
		this.gxInfo = gxInfo;
	}
	public String getGxName() {
		return gxName;
	}
	public void setGxName(String gxName) {
		this.gxName = gxName;
	}
	public String getGxInfo() {
		return gxInfo;
	}
	public void setGxInfo(String gxInfo) {
		this.gxInfo = gxInfo;
	}
	@Override
	public String toString() {
		return "GxTest [gxName=" + gxName + ", gxInfo=" + gxInfo + "]";
	}
	
}
