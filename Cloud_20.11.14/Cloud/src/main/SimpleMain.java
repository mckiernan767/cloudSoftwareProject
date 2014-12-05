package main;

import Resource.Host;
import process.ProcessList;
import process.ProcessSegment;

public class SimpleMain {
	public static void main(String[] args){
		long startTime = 1414597440;
		Host targetHost = new Host(startTime);
		//targetHost.getName(startTime, 50, 0);
		//targetHost.getCPU(startTime, 50, 0);
		ProcessList processList = new ProcessList();
		ProcessSegment[] topProcesses = new ProcessSegment[3];
		
		
		for (int x = 0; x < 1000; x++) {
			for (int i = 0; i < 3; i++) {
				topProcesses[i] = new ProcessSegment();
				topProcesses[i].setStart(x);
				topProcesses[i].setCpuUsage(targetHost.getCPU(startTime + x*60, x, i));
				topProcesses[i].setName(targetHost.getName(startTime + x*60, x, i));
			}
			processList.addNewDataPoints(topProcesses);
		}
		
		System.out.println(processList.dataForGanttChart());
		System.out.println(processList.dataForCpuUsageChart());
	}
}
