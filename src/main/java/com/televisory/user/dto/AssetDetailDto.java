package com.televisory.user.dto;

import java.io.Serializable;

public class AssetDetailDto implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String assetName;
	private double assetValue;
	private double encumbered;
	
	public String getAssetName() {
		return assetName;
	}
	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}
	public double getAssetValue() {
		return assetValue;
	}
	public void setAssetValue(double assetValue) {
		this.assetValue = assetValue;
	}
	public double getEncumbered() {
		return encumbered;
	}
	public void setEncumbered(double encumbered) {
		this.encumbered = encumbered;
	}
	
	@Override
	public String toString() {
		return "AssetDetailDto [assetName=" + assetName + ", assetValue="
				+ assetValue + ", encumbered=" + encumbered + "]";
	}
	
	

}
