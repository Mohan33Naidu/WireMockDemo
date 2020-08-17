package com.MockAPI.QA;

public class CarRevenue implements Comparable<CarRevenue>{

	String sVinNum=null;
	 float Revenue;
	 
	
	
	public CarRevenue(float Revenue,String sVin) {
		this.Revenue=Revenue;
		
		this.sVinNum=sVin;
	}

	public int compareTo(CarRevenue oCar) {
		
		
		return (int) (this.Revenue-(oCar.Revenue))*-1;
	}


}
