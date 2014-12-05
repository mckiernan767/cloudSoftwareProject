package Resource;

import java.util.ArrayList;

public class Floor {
	private int id;
	double xAxis;
	double yAxis;
	int dataCenterId;
	String name;
	String description;
	ArrayList<Rack> racks;

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
	
	public String getName(){
		return name;
	}
	public void setName(String s){
		name = s;
	}
	
	public String getDescription(){
		return description;
	}
	public void setDescription(String d){
		description = d;
	}
	
	public void addRack(Rack r){
		racks.add(r);
	}
}
