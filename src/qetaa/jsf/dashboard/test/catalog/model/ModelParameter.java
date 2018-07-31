package qetaa.jsf.dashboard.test.catalog.model;

import java.io.Serializable;

public class ModelParameter implements Serializable{

	private static final long serialVersionUID = 1L;

	private String key;
	private String name;
	private String value;
	private String idx;
	
	
	public String getIdx() {
		return idx;
	}
	public void setIdx(String idx) {
		this.idx = idx;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	@Override
	public String toString() {
		return "ModelParameter [key=" + key + ", name=" + name + ", value=" + value + ", idx=" + idx + "]";
	}
	
	
	
}
