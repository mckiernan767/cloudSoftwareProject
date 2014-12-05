package com.cloud;

public class ProcessSegment {
	private long start;
	private int duration;
	private String name;
	private double cpuUsage;

	public ProcessSegment(long start, int duration, String name, double cpuUsage) {
		this.start = start;
		this.duration = duration;
		this.name = name;
		this.cpuUsage = cpuUsage;
	}

	public ProcessSegment(){
		this.duration = 10;
	}
	
	public void setStart(long start) {
		this.start = start;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setCpuUsage(double cpuUsage) {
		this.cpuUsage = cpuUsage;
	}

	public long getStart() {
		return start;
	}

	public int getDuration() {
		return duration;
	}

	public String getName() {
		return name;
	}

	public double getCpuUsage() {
		return cpuUsage;
	}

	public long getEnd(){
		return start+duration;
	}
	
	public boolean isRunning(long time){
		if(time>=start && time<start+duration)
			return true;
		else
			return false;
	}
	
	public static String processData(ProcessSegment p) {
		String s = "";
		s = s + "[" + p.start + "," + p.duration + ","+ (int)p.cpuUsage/16 +"," + "\'" + p.name
				+ "\']";
		return s;
	}
}
