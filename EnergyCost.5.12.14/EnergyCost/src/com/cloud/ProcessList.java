package com.cloud;

import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeSet;

public class ProcessList {
	ArrayList<ProcessSegment> processList = new ArrayList<ProcessSegment>();

	ProcessSegment[] previousRunning = new ProcessSegment[3];
	ProcessSegment[] currentRunning = new ProcessSegment[3];

	int currentDuration, matchedDuration;
	int absoluteMatch, runningMatch;
	double currentCpuUsage, matchedCpuUsage;

	private static final int zoneOneEdge = 479;
	private static final int zoneTwoEdge = 959;
	private static final double zoneOnePowerCost = 0.5;
	private static final double zoneTwoPowerCost = 1.0;
	private static final double zoneThreePowerCost = 2.0;
	private static final double maxTotalCost = 144000.0; //max cpu usage for the whole duration (1440 mins)
	private static double totalCost = 0;
	
	public void addNewDataPoints(ProcessSegment[] processGroup) {
		for (int i = 0; i < 3; i++)
			currentRunning[i] = processGroup[i];
		for (ProcessSegment p : currentRunning) {
			absoluteMatch = compareToAllPrevs(p);
			if (absoluteMatch == -1)
				processList.add(p);
			else {
				ProcessSegment matchedEntry = processList.get(absoluteMatch);
				runningMatch = compareToAllPrevsRunning(p);
				if (runningMatch == -1) {
					processList.add(absoluteMatch + 1, p);
				} else {
					matchedDuration = matchedEntry.getDuration();
					matchedCpuUsage = matchedEntry.getCpuUsage();
					currentDuration = p.getDuration();
					currentCpuUsage = p.getCpuUsage();
					matchedEntry.setDuration(matchedDuration + 10);
					matchedEntry
							.setCpuUsage((matchedCpuUsage * matchedDuration + currentCpuUsage)
									/ (matchedDuration + 10));
				}
			}
		}
		for (int i = 0; i < 3; i++)
			previousRunning[i] = currentRunning[i];
	}

	private int compareToAllPrevs(ProcessSegment dp) {
		int index = processList.size() - 1;
		ArrayList<ProcessSegment> tempList = new ArrayList<ProcessSegment>();
		for (ProcessSegment p : processList)
			tempList.add(p);
		Collections.reverse(tempList);
		for (ProcessSegment p : tempList) {
			if (p.getName().equals(dp.getName())) {
				return index;
			}
			index--;
		}
		return -1;
	}

	private int compareToAllPrevsRunning(ProcessSegment dp) {
		int index = 0;
		for (ProcessSegment p : previousRunning) {
			if (p.getName().equals(dp.getName()))
				return index;
			index++;
		}
		return -1;
	}

	public String dataForGanttChart() {
		int length = processList.size();
		int currentElement = 1;
		String s = "[";
		for (ProcessSegment p : processList) {
			if (currentElement < length)
				s = s + ProcessSegment.processData(p) + ", ";
			else
				s = s + ProcessSegment.processData(p);
			currentElement++;
		}
		s = s + "]";
		return s;
	}

	public String dataForCpuUsageChart() {
		TreeSet<Long> criticalPoints = new TreeSet<Long>();
		//String s = "[['Time', 'CPU'],[";
		String s = "[{";
		double totalCpuUsage = 0.0;
		for (ProcessSegment p : processList) {
			criticalPoints.add(p.getStart());
			criticalPoints.add(p.getEnd());
		}
		int pointCounter = 1;
		int totalPoints = criticalPoints.size();
		for (long i : criticalPoints) {
			totalCpuUsage = 0;
			for (ProcessSegment p : processList) {
				if (p.isRunning(i))
					totalCpuUsage = totalCpuUsage + p.getCpuUsage();
			}
			if (pointCounter < totalPoints - 1)
				s = s + "x:" + (i/10 + 1) + ",y:" + (int) totalCpuUsage/16 + "}, {";
			else if (pointCounter < totalPoints)
				s = s + "x:" + (i/10 + 1) + ",y:" + (int) totalCpuUsage/16 + "}]";
			pointCounter++;
		}
		return s;
	}
	public int getEnergyCost(){
		totalCost = 0;
		for(ProcessSegment p : processList){
			if(p.getStart()+p.getDuration()/2 < zoneOneEdge)
				totalCost = totalCost + p.getCpuUsage()*p.getDuration()*zoneOnePowerCost/16;
			
			else if(p.getStart()+p.getDuration()/2 < zoneTwoEdge)
				totalCost = totalCost + p.getCpuUsage()*p.getDuration()*zoneTwoPowerCost/16;
			
			else
				totalCost = totalCost + p.getCpuUsage()*p.getDuration()*zoneThreePowerCost/16;
		}
		return (int) (totalCost*100/maxTotalCost);
	}
	
	public String getZones(){
		String zones="";
		for(ProcessSegment p: processList){
			if(p.getStart()+p.getDuration()/2 < zoneOneEdge)
				zones = zones+"0.5";
			else if(p.getStart()+p.getDuration()/2 < zoneTwoEdge)
				zones = zones+"1";
			else
				zones = zones+"2";
			if(!p.equals(processList.get(processList.size()-1)))
				zones = zones + ",";
		}
		return zones;
	}
	
	public String dataForTable(){
		String currentName;
		String s = "";
		String previousName=processList.get(0).getName();
		int duration = 0;
		int run = 0;
		for(ProcessSegment p: processList){
			currentName = p.getName();
			if(currentName.equals(previousName)){
				run++;
				duration = duration+p.getDuration();
			}
			else{
				s = s + "<tr>" + "<td>" + previousName + "</td>" + "<td>" + run + "</td>" + "<td>" + duration + "</td>" + "</tr>";
				run = 1;
				duration = p.getDuration();
			}
			previousName = currentName;
			if(p.equals(processList.get(processList.size()-1)))
				s = s + "<tr>" + "<td>" + currentName + "</td>" + "<td>" + run + "</td>" + "<td>" + duration + "</td>" + "</tr>";

		}
		return s;
	}
}
