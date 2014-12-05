package Resource;

import java.util.ArrayList;

public class DataCenter {
	private int id;
	String name;
	String description;
	double latitude;
	double longitude;
	ArrayList<Floor> floors;

	public int getID() {
		return id;
	}
	public void setID(int x){
		id = x;
	}
	
	public String getName(){
		return name;
	}
	public void setName(String n){
		name = n;
	}
	
	public String getDescription(){
		return description;
	}
	public void setDescription(String d){
		description = d;
	}
	
	public void setLatitude(double x) {
		latitude = x;
	}

	public void setLongitude(double y) {
		longitude = y;
	}
	
	
	
	public void addFloor(Floor f){
		floors.add(f);
	}
}
