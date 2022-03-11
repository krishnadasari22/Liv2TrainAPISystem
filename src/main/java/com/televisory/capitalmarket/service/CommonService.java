package com.televisory.capitalmarket.service;

import org.springframework.stereotype.Service;

@Service
public class CommonService {
	
	public Double percentageChange(Double newNumber , Double baseNumber){
		Double percent = null;
		if(baseNumber!=null && newNumber!=null){
			if(newNumber==0d && baseNumber==0d){
				return null;
			}else if(newNumber==0d){
				return null;
			}else{
				if(newNumber > 0 && baseNumber > 0){
					percent = ((newNumber-baseNumber)/baseNumber)*100;
				}else if(newNumber > 0 && baseNumber < 0){
					percent = Math.abs(((newNumber-baseNumber)/baseNumber)*100);
				}else if(newNumber < 0 && baseNumber > 0){
					percent = ((newNumber-baseNumber)/baseNumber)*100;
				}else if(newNumber < 0 && baseNumber < 0){
					if(newNumber > baseNumber){
						percent = Math.abs(((newNumber-baseNumber)/baseNumber)*100);
					}else{
						percent = -(((newNumber-baseNumber)/baseNumber)*100);
					}
				}
			}
		}else{
			return null;
		}
		return percent;
	}

}
