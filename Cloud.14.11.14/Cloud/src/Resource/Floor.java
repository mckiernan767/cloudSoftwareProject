package Resource;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import Resource.DataCenter;
import connect.java.Connection;

public class Floor implements GetInformation {
	private int id;
	double xAxis;
	double yAxis;
	int dataCenterId;
	String name;
	String description;
	ArrayList<Rack> racks;
	String URL;
	String URL1;
	double power;
	Long timestamp;
	Connection c;

	public Floor(int id, int dc) {
		this.id = id;
		this.URL = "datacenters/" + dc + "/floors/" + id;
		this.URL1 = "datacenters/" + dc + "/allracks";
		c = new Connection();
		JSONObject response = c.getInformationResource(URL);

		name = (String) response.get("name");
		description = (String) response.get("description");
		//this.fetchPower();
		
		racks= new ArrayList<Rack>();
		JSONObject fs=c.getInformationResource(URL1);
		JSONObject json = (JSONObject)fs.get("rackExtended");
		String s = json.get("rackId").toString();
		int i = Integer.parseInt(s);
		Rack r=new Rack(id,i,dc);
		racks.add(r);
		
		
		
	}

	
	public void add() {
		this.getClass();
	}

	public int getID() {
		return id;
	}

	public void setId(int x) {
		id = x;
	}

	public double getxAxis() {
		return xAxis;
	}

	public double getyAxis() {
		return yAxis;
	}

	public void setxAxis(double x) {
		xAxis = x;
	}

	public void setyAxis(double y) {
		yAxis = y;
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

	public void addRack(Rack r) {
		racks.add(r);
	}
	public ArrayList<Rack> getRacks(){
		return racks;
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
