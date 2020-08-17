package com.MockAPI.QA;


import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;

import com.github.tomakehurst.wiremock.core.WireMockConfiguration;


import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import  static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


public class WireMockCarProject {
	WireMockServer wireMockServer=null;
	static Response resp=null;
	@BeforeSuite
	public void initialize() {
	WireMockServer wireMockServer = new WireMockServer(9999); //No-args constructor will start on port 8080, no HTTPS
	wireMockServer.start();
	
	
	    wireMockServer.stubFor(get(urlEqualTo("/GetCars"))
	    		//.withHeader("Content-Type", equalTo("application/json;charset=UTF-8"))
	            .willReturn(aResponse()
	            		.withStatus(200)
	               // .withHeader("Content-Type","application/json;charset=UTF-8")
	                  .withBodyFile("GetApi.json")));
	   
	}
	
	
	
@Test (priority=1)
public  void getResponse() {
	
	String url="http://localhost:9999/GetCars";
	RestAssured.baseURI=url;
	//Response resp=RestAssured.given().contentType("application/json;charset=UTF-8").get();
	resp=RestAssured.get();
    assertEquals(resp.getStatusCode(),200);
	
}

@Test (priority=2)
public void CountBlueCars() {
	int iBlueCount=0;
	List<HashMap<String,String>> ltCarMetadata=resp.jsonPath().getList("Car.metadata");
	//System.out.println(ltCarMetadata.size());
	for(int i=0;i<ltCarMetadata.size();i++) {
		
		
		if(ltCarMetadata.get(i).get("Color").equalsIgnoreCase("Blue")&& resp.jsonPath().getList("Car.make").get(i).equals("Tesla"))
		{
			System.out.println("vin:"+resp.jsonPath().getList("Car.vin").get(i));
			iBlueCount++;
			System.out.println("Notes for Blue Tesla No."+(i+1)+" "+ltCarMetadata.get(i).get("Notes"));
		}
	}
	System.out.println();
	
}

@Test(priority=3)
public void qALowestRentalCost() {
	List<Integer> lCarPrice=resp.jsonPath().getList("Car.perdayrent.Price");
	List<CarPrice> lsCarQ_A=new ArrayList<CarPrice>();
	for (int i=0;i<lCarPrice.size();i++) {
		Float fPrice=(float) (lCarPrice.get(i));
		lsCarQ_A.add(new CarPrice(resp.jsonPath().getList("Car.vin").get(i).toString(),fPrice));
	}
	Collections.sort(lsCarQ_A);
	System.out.println("Lowest Price:"+lsCarQ_A.get(0).sVinNum+" Price:"+lsCarQ_A.get(0).fCarPrice);
	
	GetCarDetails(lsCarQ_A.get(0).sVinNum);
	System.out.println();
	
	
}

@Test(priority=4)
public void qBLowestRentalCost() {
	List<Integer> lCarPrice=resp.jsonPath().getList("Car.perdayrent.Price");
	List<Integer> lCarDiscount=resp.jsonPath().getList("Car.perdayrent.Discount");
	List<CarPrice> lsCarQ_B=new ArrayList<CarPrice>();
	for (int i=0;i<lCarPrice.size();i++) {
		Float fPrice=(float) (lCarPrice.get(i));
		fPrice=fPrice-(fPrice*lCarDiscount.get(i)/100);
		lsCarQ_B.add(new CarPrice(resp.jsonPath().getList("Car.vin").get(i).toString(),fPrice));
	}
	Collections.sort(lsCarQ_B);
	System.out.println("Lowest Price after discounts:"+lsCarQ_B.get(0).sVinNum+" Price:"+lsCarQ_B.get(0).fCarPrice);
	GetCarDetails(lsCarQ_B.get(0).sVinNum);
	System.out.println();
}

@Test(priority=5)
public void highRevenueCar() {
	List<Integer> lCarPrice=resp.jsonPath().getList("Car.perdayrent.Price");
	List<Integer> lCarDiscount=resp.jsonPath().getList("Car.perdayrent.Discount");
	//List<Integer> lCarMaintenance=resp.jsonPath().getList("Car.metrics.yoymaintenancecost");
	//List<Integer> lCarDepreciation=resp.jsonPath().getList("Car.metrics.depreciation");
	List<Integer> lCarYeartoDate=resp.jsonPath().getList("Car.metrics.rentalcount.yeartodate");
	
	List<CarRevenue> lsCar=new ArrayList<CarRevenue>();
	for (int i=0;i<lCarPrice.size();i++) {
		Float fPrice=(float) (lCarPrice.get(i));
		fPrice=fPrice-(fPrice*lCarDiscount.get(i)/100);
		Float expense=resp.jsonPath().getFloat("Car["+i+"].metrics.yoymaintenancecost");
		Float depreciation=resp.jsonPath().getFloat("Car["+i+"].metrics.depreciation");
		Float Revenue=fPrice*lCarYeartoDate.get(i)-(expense+depreciation);
		lsCar.add(new CarRevenue(Revenue,resp.jsonPath().getList("Car.vin").get(i).toString()));
	}
	Collections.sort(lsCar);
	int i=0;
	System.out.println("Revenue: "+lsCar.get(0).Revenue+"  ---- "+lsCar.get(0).sVinNum);
	
	GetCarDetails(lsCar.get(0).sVinNum);
	System.out.println();
}
private void GetCarDetails(String sVinNum) {
	// TODO Auto-generated method stub
	List<String> lsCarVin=resp.jsonPath().getList("Car.vin");
	for (int i=0;i<lsCarVin.size();i++) {
		if(lsCarVin.get(i).equals(sVinNum)) {
			
			
			System.out.println(resp.jsonPath().get("Car["+i+"]"));
		}
	}
}



@AfterSuite
public void stopServer() {
	wireMockServer.stop();
	System.out.println("stop server");
}


}