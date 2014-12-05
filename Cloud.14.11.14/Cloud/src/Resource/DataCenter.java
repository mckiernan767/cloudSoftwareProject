package Resource;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import connect.java.Connection;

public class DataCenter implements GetInformation {
	private int id;
	String name;
	String description;
	double latitude;
	double longitude;
	String URL;
	double power;
	Long timestamp;
	ArrayList<Floor> floors;
	Floor f;
	Connection c;

	public DataCenter(int id) {
		// getting datacenter
		this.id = id;
		this.URL = "datacenters/" + id;

		c = new Connection();
		JSONObject response = c.getInformationResource(URL);

		name = (String) response.get("name");
		description = (String) response.get("description");

		this.setName(name);
		// getting floor information
		floors = new ArrayList<Floor>();
		JSONObject fs = c.getInformationResource(URL + "/allfloors");
		JSONObject json = (JSONObject) fs.get("floorExtended");
		String s = json.get("floorId").toString();
		int i = Integer.parseInt(s);
		Floor f = new Floor(i, id);
		floors.add(f);
	}

	public int getID() {
		return id;
	}

	public void setID(int x) {
		id = x;
	}

	public String getName() {
		return name;
	}

	public void setName(String n) {
		name = n;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String d) {
		description = d;
	}

	public void setLatitude(double x) {
		latitude = x;
	}

	public void setLongitude(double y) {
		longitude = y;
	}

	public void addFloor(Floor f) {
		floors.add(f);
	}

	public ArrayList<Floor> getFloor() {
		return floors;
	}

	@Override
	/*
	 * public void fetchPower(long starttime, long endtime) { // fetch through
	 * URL and update power and timestamp JSONObject response =
	 * c.getInformationResource(URL + "/power?starttime=" + starttime +
	 * "&endtime=" + endtime);
	 * 
	 * JSONObject json = (JSONObject)response.get("power");
	 * 
	 * String p = json.get("power").toString(); String t =
	 * json.get("timeStamp").toString(); power = Double.parseDouble(p);
	 * timestamp = Long.parseLong(t);
	 * 
	 * // fetchpower of floors //for (int i = 0; i < floors.size(); i++) {
	 * //floors.get(i).fetchPower(starttime, endtime); //} }
	 */
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
	public void refreshInterval(long interval) {
		Long currenttime = System.currentTimeMillis() / 1000L;
		this.fetchPower(currenttime - interval, currenttime);
		// TODO Auto-generated method stub

	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub

	}
}
