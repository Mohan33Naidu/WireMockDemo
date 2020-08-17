package com.MockAPI.QA;


public class CarPrice implements Comparable<CarPrice> {

	Float fCarPrice=0.0f;
	String sVinNum=null;
	public CarPrice(String vin, Float Price) {
		
		this.sVinNum=vin;
		this.fCarPrice=Price;
	}



	public int compareTo(CarPrice oCar) {
		// TODO Auto-generated method stub
		return (int)(this.fCarPrice - oCar.fCarPrice);
	}

}
