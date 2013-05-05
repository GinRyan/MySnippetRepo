package com.outman;

import java.util.List;

public class Car {
	private String id;

	private String type;

	private List<String> engineInfo;

	private List<String> shapeInfo;

	private List<String> speedInfo;

	public List<String> getSpeedInfo() {
		return speedInfo;
	}

	public void setSpeedInfo(List<String> speedInfo) {
		this.speedInfo = speedInfo;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<String> getEngineInfo() {
		return engineInfo;
	}

	public void setEngineInfo(List<String> engineInfo) {
		this.engineInfo = engineInfo;
	}

	public List<String> getShapeInfo() {
		return shapeInfo;
	}

	public void setShapeInfo(List<String> shapeInfo) {
		this.shapeInfo = shapeInfo;
	}
}
