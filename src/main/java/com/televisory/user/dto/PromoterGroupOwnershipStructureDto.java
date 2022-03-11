package com.televisory.user.dto;

import java.io.Serializable;

public class PromoterGroupOwnershipStructureDto implements Serializable {

	    private static final long serialVersionUID = 2390088035661484408L;
	
		private String title;
		private String name;
		private double perOfShareToday;
		private double perOfShareLatestFinYear;
		private double perOfSharePrevFinYear;
		
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public double getPerOfShareToday() {
			return perOfShareToday;
		}
		public void setPerOfShareToday(double perOfShareToday) {
			this.perOfShareToday = perOfShareToday;
		}
		public double getPerOfShareLatestFinYear() {
			return perOfShareLatestFinYear;
		}
		public void setPerOfShareLatestFinYear(double perOfShareLatestFinYear) {
			this.perOfShareLatestFinYear = perOfShareLatestFinYear;
		}
		public double getPerOfSharePrevFinYear() {
			return perOfSharePrevFinYear;
		}
		public void setPerOfSharePrevFinYear(double perOfSharePrevFinYear) {
			this.perOfSharePrevFinYear = perOfSharePrevFinYear;
		}
		public static long getSerialversionuid() {
			return serialVersionUID;
		}
		
		@Override
		public String toString() {
			return "PromoterGroupOwnershipStructureDto [title=" + title
					+ ", name=" + name + ", perOfShareToday=" + perOfShareToday
					+ ", perOfShareLatestFinYear=" + perOfShareLatestFinYear
					+ ", perOfSharePrevFinYear=" + perOfSharePrevFinYear + "]";
		}
}
