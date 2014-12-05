package Resource;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import connect.java.Connection;

public class Rack implements GetInformation {

	private int id;
	ArrayList<Host> hosts;
	int floorID;
	String name;
	String description;
	double xAxis;
	double yAxis;
	String URL;
	String URL1;
	Connection c;
	double power;
	Long timestamp;

	public Rack(int id, int floor, int dc) {
		this.id = id;
		floorID = id;
		this.URL = "datacenters/" + dc + "/floors/" + floor + "/racks/" + id;
		this.URL1 = "datacenters/" + dc + "/allhosts";
		c = new Connection();
		JSONObject responce = c.getInformationResource(URL);

		name = (String) responce.get("name");
		description = (String) responce.get("description");
		
		hosts= new ArrayList<Host>();
		JSONObject fs=c.getInformationResource(URL1);
		JSONObject json = (JSONObject)fs.get("hostExtended");
		String s = json.get("hostId").toString();
		int i = Integer.parseInt(s);
		Host r=new Host(id,i,floor,dc);
		hosts.add(r);
	}

	
	public int getID() {
		return id;
	}

	public void setId(int x) {
		id = x;
	}

	public int getFloorID() {
		return floorID;
	}

	public void setFloorId(int f) {
		floorID = f;
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

	public void addHost(Host h) {
		hosts.add(h);
	}

	

	@Override
	public void fetchPower(long starttime, long endtime) {
		// fetch through URL and update power and timestamp

		JSONObject response = c.getInformationResource(URL
				+ "/power?starttime=" + starttime + "&endtime=" + endtime);
		//System.out.println(response);
		if ((endtime - starttime) != 60) {

			JSONArray jsonArray = (JSONArray) response.get("power");

			//System.out.println(jsonArray);
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
