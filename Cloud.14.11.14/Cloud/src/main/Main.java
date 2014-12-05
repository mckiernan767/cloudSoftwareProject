package main;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import connect.java.Connection;
import Conversion.TimeConversion;
import Resource.DataCenter;
import Resource.Floor;
import Resource.Host;
import Resource.Rack;

public class Main {

	/* pass the following details in */

	static int dcID = 1;
	static int floorID = 1;
	static int rackID = 1;
	static int hostID = 1;
	static int appNumber = 0;
	
	public static void main(String[] args) {
		String date = "2014-10-29 16:45:20";
		TimeConversion tc = new TimeConversion();
		tc.setDt(date);
		long startTime = tc.unixTimeStamp2long();
		
		DataCenter dc = new DataCenter(dcID);
		Floor floor = new Floor(floorID, dc.getID());
		Rack rack = new Rack(rackID, floor.getID(), dc.getID());
		Host host = new Host(hostID, rack.getID(), floor.getID(), dc.getID());

		dc.addFloor(floor);
		System.out.println(dc.getFloor());
		String name = host.getName(startTime, appNumber);// pass in startTime
															// and application
															// required (0-2)
		double cpu = host.getCPU(startTime, appNumber);
		int activityId = host.getActivityId(startTime);

		System.out.println("name of " + appNumber + " application is " + name);
		System.out.println("CPU usage " + name + " is " + cpu);
		System.out.println("activityId is " + activityId);

		Connection c = new Connection(); 
		JSONObject fs = c.getInformationResource("datacenters/1/allfloors");
		
		
		JSONObject json = (JSONObject)fs.get("floorExtended");
		String s = json.get("floorId").toString();
		
		System.out.println(s);
		
		
		dc.fetchPower(startTime, startTime+120);
		//System.out.println("timestamp " + dc.getTimestamp());
		System.out.println("power " + dc.getPower());
		
		floor.fetchPower(startTime, startTime+120);
		System.out.println("floor power" + floor.getPower());
		
		
		//Floor floor = new Floor(1, 1);
		
		
		//String s = (structure.get("floorId")).toString();
		//System.out.println(p);
		
		//System.out.println("fs " + floorId);
				
		}
		
		
		/*
		DataCenter dc = new DataCenter(dcID);
		Floor floor = new Floor(floorID, dc.getID());
		Rack rack = new Rack(rackID, floor.getID(), dc.getID());
		Host host = new Host(hostID, rack.getID(), floor.getID(), dc.getID());
		
		String date = "2014-10-29 16:45:20";
		TimeConversion tc = new TimeConversion();
		tc.setDt(date);
		long startTime = tc.unixTimeStamp2long();
		
		JSONObject o = host.getActivity(startTime);
		System.out.println(o);
		
		JSONArray a = host.getActivityArray(startTime);
		System.out.println(a);*/
	

}
