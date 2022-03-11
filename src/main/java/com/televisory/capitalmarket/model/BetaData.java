package com.televisory.capitalmarket.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class BetaData implements Serializable {
	
	private static final long serialVersionUID = 1030671457664888535L;
	private Integer indexId;
	private String indexName;
	private Date date;
	private List<Double> data;

	public Integer getIndexId() {
		return indexId;
	}

	public void setIndexId(Integer indexId) {
		this.indexId = indexId;
	}

	public String getIndexName() {
		return indexName;
	}

	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public List<Double> getData() {
		return data;
	}

	public void setData(List<Double> data) {
		this.data = data;
	}
	
}
