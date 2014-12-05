package Resource;

import org.json.simple.JSONObject;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import connect.java.Connection;

public class Host {
	private int hostId, rackId, floorId, datacenterId;
	long startTime;
	String hostName, hostDescription, URL;
	Connection c;
	JsonObject activities;
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
		long endTime = startTime + 86400;
		this.URL = "datacenters/" + datacenterId + "/floors/" + floorId
				+ "/racks/" + rackId + "/hosts/" + hostId
				+ "/activity?starttime=" + startTime + "&endtime=" + endTime;
		c = new Connection();

		activities = c.getInformationResource(URL);

	}

	public String getName(long startTime, int appNumber, int x) {
		String name = "";

		JsonArray activity = (JsonArray) activities.get("activity");

		JsonObject apps = activity.get(appNumber).getAsJsonObject();
		JsonObject apps1 = (JsonObject) apps.get("apps");
		JsonArray apps2 = (JsonArray) apps1.get("app");
		JsonObject jo = apps2.get(x).getAsJsonObject();
		name = jo.get("name").toString();
		System.out.println(name);

		return name;
	}

	public double getCPU(long startTime, int appNumber, int x) {
		String cpu = null;
		double cpuUsage = 300;// see below
		/*
		 * JsonArray activity = (JsonArray) activities.get("activity");
		 * 
		 * JsonObject apps = activity.get(appNumber).getAsJsonObject();
		 * JsonObject apps1 = (JsonObject) apps.get("apps"); JsonArray apps2 =
		 * (JsonArray)apps1.get("app"); JsonObject jo =
		 * apps2.get(x).getAsJsonObject(); cpu = jo.get("cpu").toString();
		 * cpuUsage = Double.parseDouble(cpu);//retrieving string cpu ok but
		 * this line is not working
		 */

		return cpuUsage;
	}
}
