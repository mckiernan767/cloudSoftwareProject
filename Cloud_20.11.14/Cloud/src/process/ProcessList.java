package process;

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
					matchedEntry.setDuration(matchedDuration + 1);
					matchedEntry
							.setCpuUsage((matchedCpuUsage * matchedDuration + currentCpuUsage)
									/ (matchedDuration + 1));
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
		String s = "[['Time', 'CPU'],[";
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
				s = s + "\'Run " + (i + 1) + "\', " + (int) totalCpuUsage + "], [";
			else if (pointCounter < totalPoints)
				s = s + "\'Run " + (i + 1) + "\', " + (int) totalCpuUsage + "]]";
			pointCounter++;
		}
		return s;
	}
}
