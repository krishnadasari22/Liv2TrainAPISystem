package com.televisory.capitalmarket.dto;

import java.io.Serializable;
import java.util.List;

public class IndicatorTypeDTO implements Serializable {
	
	private static final long serialVersionUID = 6206150032636315911L;
	private int id;
	private String name;
	private List<IndicatorDataDTO_old> indicatorData;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<IndicatorDataDTO_old> getIndicatorData() {
		return indicatorData;
	}
	public void setIndicatorData(List<IndicatorDataDTO_old> indicatorData) {
		this.indicatorData = indicatorData;
	}
	@Override
	public String toString() {
		return "IndicatorTypeDTO [id=" + id + ", name=" + name + ", indicatorData=" + indicatorData + "]";
	}
}
