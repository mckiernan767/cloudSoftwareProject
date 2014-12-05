package com.cloud;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


public class Host {
	private int hostId, rackId, floorId, datacenterId;
	long startTime;
	String hostName, hostDescription, URL;
	Connection c;
	JSONObject activities;
	static int currentName, currentCpuUsage = 0;

	public Host(int hostId, int rackId, int floorId, int datacenterId,
			long startTime) {
		this.hostId = hostId;
		this.rackId = rackId;
		this.floorId = floorId;
		this.datacenterId = datacenterId;
		this.startTime = startTime;
		currentName = currentCpuUsage = 0;
		connect(startTime);
	}

	public Host(long startTime) {
		this.hostId = 1;
		this.rackId = 1;
		this.floorId = 1;
		this.datacenterId = 1;
		this.startTime = startTime;
		currentName = currentCpuUsage = 0;
		connect(startTime);
	}

	private void connect(long startTime) {
		long endTime = startTime + 600000;
		this.URL = "datacenters/" + datacenterId + "/floors/" + floorId
				+ "/racks/" + rackId + "/hosts/" + hostId
				+ "/activity?starttime=" + startTime + "&endtime=" + endTime;
		c = new Connection();

		activities = c.getInformationResource(URL);
	}

	public String getName(long startTime, int appNumber, int x) {
		String name = "";

		JSONArray activity = (JSONArray) activities.get("activity");

		JSONObject apps = (JSONObject) activity.get(appNumber);
		JSONObject apps1 = (JSONObject) apps.get("apps");
		JSONArray apps2 = (JSONArray) apps1.get("app");
		JSONObject jo = (JSONObject) apps2.get(x);
		name = jo.get("name").toString();

		return name;
	}

	public double getCPU(long startTime, int appNumber, int x) {
		String cpu = null;
		float cpuUsage=0;
		
		 JSONArray activity = (JSONArray) activities.get("activity");
		 
		 JSONObject apps = (JSONObject) activity.get(appNumber);
		 JSONObject apps1 = (JSONObject) apps.get("apps"); 
		 JSONArray apps2 = (JSONArray)apps1.get("app"); 
		 JSONObject jo = (JSONObject) apps2.get(x); 
		 cpu = jo.get("cpu").toString();
		 cpuUsage = Float.parseFloat(cpu);

		return cpuUsage;
	}
}
