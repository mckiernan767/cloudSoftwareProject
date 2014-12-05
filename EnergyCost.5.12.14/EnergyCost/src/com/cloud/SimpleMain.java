package com.cloud;


public class SimpleMain {
	public static String output, cpuUsage;
	public static final String test = "this is a test";
	public static int energyCost;
	public static String tarriffLevel;
	public static String data;
	
	public static void simple(){
	
		long startTime = 1414597440; 
		Host targetHost = new Host(1,1,1,1,startTime);
			
		ProcessList processList = new ProcessList();
		ProcessSegment[] topProcesses = new ProcessSegment[3];
		
			for (int x = 0; x < 1440; x+=10) {
				for (int i = 0; i < 3; i++) {
					topProcesses[i] = new ProcessSegment();
					topProcesses[i].setStart(x);
					topProcesses[i].setCpuUsage(targetHost.getCPU(startTime + x*60, x, i));
					topProcesses[i].setName(targetHost.getName(startTime + x*60, x, i));
				}
				processList.addNewDataPoints(topProcesses);
			}
			output = processList.dataForGanttChart();
			cpuUsage = processList.dataForCpuUsageChart();
			energyCost = processList.getEnergyCost();
			tarriffLevel = processList.getZones();
			data = processList.dataForTable();
		}
	}
