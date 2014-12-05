package main;

import org.json.simple.JSONObject;

import process.ProcessList;
import connect.java.Connection;
import Conversion.TimeConversion;
import Resource.DataCenter;
import Resource.Floor;
import Resource.Host;
import Resource.Rack;
import process.ProcessSegment;

public class Main {

	/* pass the following details in */

	static int dcID = 1;
	static int floorID = 1;
	static int rackID = 1;
	static int hostID = 1;

	static int appNumber = 0;

	public static void main(String[] args) {
		String date = "2014-11-18 21:45:00";
		// String date = "2014-10-29 01:00:00";
		TimeConversion tc = new TimeConversion();
		tc.setDt(date);
		long startTime = tc.unixTimeStampToLong();

		DataCenter dc = new DataCenter(dcID);
		Floor floor = new Floor(floorID, dc.getID());
		Rack rack = new Rack(rackID, floor.getID(), dc.getID());
		Host host = new Host(hostID, rack.getID(), floor.getID(), dc.getID());

		dc.addFloor(floor);
		String name = host.getName(startTime, appNumber);// pass
																			// in
																			// startTime
		// and application
		// required (0-2)
		double cpu = host.getCPU(startTime, appNumber);
		//int activityId = host.getActivityId(startTime, startTime + 60);

		Connection c = new Connection();
		JSONObject fs = c.getInformationResource("datacenters/1/allfloors");

		JSONObject json = (JSONObject) fs.get("floorExtended");
		String s = json.get("floorId").toString();

		dc.fetchPower(startTime, startTime + 120);

		floor.fetchPower(startTime, startTime + 60);

		// System.out.println(host.getCPU(startTime+(0*60),
		// startTime+((0+1)*60), 0));

		ProcessList processList = new ProcessList();
		/*
		Process[] topProcesses = new Process[3];
		topProcesses[0] = new Process();
		topProcesses[1] = new Process();
		topProcesses[2] = new Process();
		for (int x = 0; x < 1440; x+=60) {
			for (int i = 0; i < 3; i++) {
				topProcesses[i].setStart(startTime + x);
				topProcesses[i].setCpuUsage(host.getCPU(startTime + x, startTime + x+60, i));
				topProcesses[i].setName(host.getName(startTime + x, startTime + x+60, i));
			}
			processList.addNewDataPoints(topProcesses);
		}
		 */
		
		  for (long x = startTime; x < startTime + 1440; x += 60) {
		  
		  for (int i = 0; i < 1; i++) {
		  
		  ProcessSegment[] pGroup1 = { new ProcessSegment(x / 60 - startTime / 60 + 1, 1,
		  host.getName(x, i), host.getCPU(x, i)), new ProcessSegment(x
		 / 60 - startTime / 60 + 1, 1, host.getName(x, i + 1),
		  host.getCPU(x,  i + 1)), new ProcessSegment(x / 60 - startTime / 60 +
		  1, 1, host.getName(x, i + 2), host.getCPU(x, i + 2))
		  };
		  
		  processList.addNewDataPoints(pGroup1); } }
		 

		System.out.println(processList.dataForGanttChart());

	}
}
