package com.MockAPI.QA;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;



import io.restassured.RestAssured;
import io.restassured.response.Response;

public class Carlist {

	public static void main(String[] args) {
		String sURL="http://localhost:3000/carlist";
		RestAssured.baseURI=sURL;
		Response resp=RestAssured.get("Car.make");
		int iBlueCount=0;
		//System.out.println(resp.jsonPath().getList("Car.metadata[0]").toString());//gets list of all elements under username
	
		List<HashMap<String,String>> ltCarMetadata=resp.jsonPath().getList("Car.metadata");
		
		System.out.println("Question 1: ");
		for(int i=0;i<ltCarMetadata.size();i++) {
			
			
			if(ltCarMetadata.get(i).get("Color").equalsIgnoreCase("Blue") )
			{
				iBlueCount++;
				System.out.println("vin:"+resp.jsonPath().get("Car.vin"));
				System.out.println("Notes for Blue Tesla No."+(i+1)+" "+ltCarMetadata.get(i).get("Notes"));
			}
		}
		
      System.out.println("Count of blue cars "+ iBlueCount);
		
      //Question 2a:
      List<Float> lsRentalPrice=resp.jsonPath().getList("Car.perdayrent.Price");
     // System.out.println(lsRentalPrice);
      List<Float> lsOrder=new ArrayList<Float>();
      lsOrder.addAll(lsRentalPrice);
      Collections.sort(lsRentalPrice);
     
      //System.out.println(lsOrder);
      int k=0;
      for (int i=0;i<lsOrder.size();i++) {
		
    	  if(lsOrder.get(i)==lsRentalPrice.get(0))
    	  {
    		  k=i;
    		//System.out.println(i);  
    	  }
    	  
	}
      System.out.println("Question 2a:");
      System.out.println(resp.jsonPath().getList("Car").get(k));
      
      //Question 2b:
      
      List<HashMap<String,Integer>> Rent =resp.jsonPath().getList("Car.perdayrent");
      int iResCost=0;
      Integer iCostMin=0;
      for (int i=0;i<Rent.size();i++) {
		//System.out.println(Rent.get(i).get("Discount"));
		Integer Cost=(Rent.get(i).get("Price"))-(Rent.get(i).get("Price")-Rent.get(i).get("Discount")/100);
    	  if(i==0)
    	  {
    		  iCostMin=Cost;
    		  iResCost=i;
    	  }
    	  if(Cost<iCostMin) {
    		  iCostMin=Cost;
    		  iResCost=i;
    	  }
      }
      
  
      System.out.println("Question 2b:");
      System.out.println(resp.jsonPath().getList("Car").get(iResCost));
      
      
      //Question 3:
      
      List<HashMap<String,Float>> metrics =resp.jsonPath().getList("Car.metrics");
      int iResExpense=0;
      Float fValMin=0.0f;
      for (int i=0;i<metrics.size();i++) {
		//System.out.println(metrics.get(i).get("yoymaintenancecost")*metrics.get(i).get("depreciation"));
    	  Float Prod=metrics.get(i).get("yoymaintenancecost")-metrics.get(i).get("depreciation");
    	  if(i==0)
    	  {
    		  fValMin=Prod;
    		  iResExpense=i;
    	  }
    	  if(Prod<fValMin) {
    		  fValMin=Prod;
    		  iResExpense=i;
    	  }
	}
      System.out.println("Question 3:");
      System.out.println(resp.jsonPath().getList("Car").get(iResExpense));
      
      //hashmap compare
      
      List<HashMap<String,Integer>> lsRent=resp.jsonPath().getList("Car.perdayrent");
     //Collections.sort(lsRent);
     
         
     /*Collections.sort(lsRent, new Comparator<HashMap<String, String>>(){ 
         public int compare(HashMap<String, String> one, HashMap<String, String> two) { 
             return one.get("Price").compareTo(two.get("Price"));
         } 
 });*/
	}
	
	  public int compare(Map.Entry<String, Integer> obj1,Map.Entry<String, Integer> obj2) 
		{ 
		return (obj1.getValue()).compareTo(obj2.getValue()); 
		} 
}
