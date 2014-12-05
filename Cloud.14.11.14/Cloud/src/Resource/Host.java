package Resource;

import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import connect.java.Connection;

public class Host implements GetInformation{
	private int id;
	public int rackId;
	public int moduleGroupId;
	String name;
	String description;
	String hostType;
	String URL;
	String URL1;
	double IP;
	Connection c;
	double power;
	Long timestamp;
	
	public Host(int id){
		
	}
	
	public Host(int id, int rack, int floor, int dc) {
		this.id = id;
		rackId = id;
	
		this.URL = "datacenters/" + dc + "/floors/" + floor + "/racks/"
				+ rack + "/hosts/" + id;
		c = new Connection();
		JSONObject responce = c.getInformationResource(URL);

		name = (String) responce.get("name");
		description = (String) responce.get("description");
	}

	
	public JSONObject getActivity(long startTime) {
		// this.id = id;
		JSONObject activity;
		long endTime = startTime + 60;
		rackId = id;
		URL1 = "datacenters/" + 1 + "/floors/" + 1 + "/racks/" + 1
				+ "/hosts/" + id + "/activity?starttime=" + startTime
				+ "&endtime=" + endTime;
		Connection c = new Connection();
		activity = c.getInformationResource(URL1);

		return activity;
	}
	
	public JSONArray getActivityArray(long startTime) {
		// this.id = id;
		JSONArray activity;
		long endTime = startTime + 60;
		rackId = id;
		this.URL1 = "datacenters/" + 1 + "/floors/" + 1 + "/racks/" + 1
				+ "/hosts/" + id + "/activity?starttime=" + startTime
				+ "&endtime=" + endTime;
		Connection c = new Connection();
		activity = c.getInformationResources(URL1);

		return activity;
	}

	public String getName(long startTime,  int x) {
		JSONObject activity = getActivity(startTime);
		String n = "";
		JSONObject structure = (JSONObject) activity.get("activity");
		JSONObject jo = (JSONObject) structure.get("apps");

		JSONArray arr = (JSONArray) jo.get("app");

		n = ((JSONObject) arr.get(x)).get("name").toString();

		return n;
	}

	public double getCPU(long startTime, int x) {
		JSONObject activity = getActivity(startTime);
		String cpu = null;
		double foo = 0;
		JSONObject structure = (JSONObject) activity.get("activity");
		JSONObject jo = (JSONObject) structure.get("apps");
		JSONArray arr = (JSONArray) jo.get("app");

		cpu = ((JSONObject) arr.get(x)).get("cpu").toString();
		foo = Double.parseDouble(cpu);

		return foo;
	}
	
	public int getActivityId(long startTime) {
		JSONObject activity = getActivity(startTime);
		String activityId = null;
		int foo = 0;
		JSONObject structure = (JSONObject) activity.get("activity");
		JSONObject jo = (JSONObject) structure.get("apps");
		JSONArray arr = (JSONArray) jo.get("app");

		activityId = ((JSONObject) arr.get(0)).get("activityId").toString();
		foo = Integer.parseInt(activityId);

		return foo;
	}

	public int getID() {
		return id;
	}

	public void setID(int x) {
		id = x;
	}

	public int getRackID() {
		return rackId;
	}

	public void setRackID(int x) {
		rackId = x;
	}

	public int getModuleGroupID() {
		return moduleGroupId;
	}

	public void setModuleGroupID(int x) {
		moduleGroupId = x;
	}

	public String getName() {
		return name;
	}

	public void setName(String s) {
		name = s;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String d) {
		description = d;
	}

	public String getHostType() {
		return hostType;
	}

	public void setHostType(String d) {
		hostType = d;
	}

	public double getIP() {
		return IP;
	}

	public void setIP(int x) {
		IP = x;
	}

	@Override
	public void fetchPower(long starttime, long endtime) {
		// fetch through URL and update power and timestamp

		JSONObject response = c.getInformationResource(URL
				+ "/power?starttime=" + starttime + "&endtime=" + endtime);
		System.out.println(response);
		if ((endtime - starttime) != 60) {

			JSONArray jsonArray = (JSONArray) response.get("power");

			System.out.println(jsonArray);
			for (int i = 0; i <= jsonArray.size(); i++) {
				String s = ((JSONObject) jsonArray.get(0)).get("power")
						.toString();
				String t = ((JSONObject) jsonArray.get(0)).get("timeStamp")
						.toString();

				power = power + Double.parseDouble(s);
				timestamp = timestamp + Long.parseLong(t);
			}
		} else {
			JSONObject json = (JSONObject) response.get("power");

			String p = json.get("power").toString();
			String t = json.get("timeStamp").toString();
			power = Double.parseDouble(p);
			timestamp = Long.parseLong(t);

		}

		// fetchpower of floors
		// for (int i = 0; i < floors.size(); i++) {
		// floors.get(i).fetchPower(starttime, endtime);
		// }

	}

	public double getPower() {
		return power;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void refreshInterval(long interval) {
		// TODO Auto-generated method stub
		
	}
}
