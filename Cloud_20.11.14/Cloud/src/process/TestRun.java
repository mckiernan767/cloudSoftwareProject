package process;

public class TestRun {
	public static void main(String[] args) {
		ProcessList processes = new ProcessList();

		// For test run
		ProcessSegment p1 = new ProcessSegment(0, 1, "TestProcess1", 10);
		ProcessSegment p2 = new ProcessSegment(0, 1, "TestProcess2", 10);
		ProcessSegment p3 = new ProcessSegment(0, 1, "TestProcess3", 10);
		ProcessSegment p4 = new ProcessSegment(1, 1, "TestProcess1", 10);
		ProcessSegment p5 = new ProcessSegment(1, 1, "TestProcess2", 10);
		ProcessSegment p6 = new ProcessSegment(1, 1, "TestProcess4", 10);
		ProcessSegment p7 = new ProcessSegment(2, 1, "TestProcess3", 10);
		ProcessSegment p8 = new ProcessSegment(2, 1, "TestProcess1", 10);
		ProcessSegment p9 = new ProcessSegment(2, 1, "TestProcess4", 10);
		ProcessSegment p10 = new ProcessSegment(3, 1, "TestProcess5", 10);
		ProcessSegment p11 = new ProcessSegment(3, 1, "TestProcess2", 5);
		ProcessSegment p12 = new ProcessSegment(3, 1, "TestProcess3", 10);
		ProcessSegment[] pGroup1 = { p1, p2, p3 };
		ProcessSegment[] pGroup2 = { p4, p5, p6 };
		ProcessSegment[] pGroup3 = { p7, p8, p9 };
		ProcessSegment[] pGroup4 = { p10, p11, p12 };
		processes.addNewDataPoints(pGroup1);
		processes.addNewDataPoints(pGroup2);
		processes.addNewDataPoints(pGroup3);
		processes.addNewDataPoints(pGroup4);
		System.out.println(processes.dataForGanttChart());
		System.out.println(processes.dataForCpuUsageChart());
	}
}