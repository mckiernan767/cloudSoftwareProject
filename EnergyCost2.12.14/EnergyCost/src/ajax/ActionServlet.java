package ajax;

import java.io.IOException;
import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ActionServlet
 */

public class ActionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ActionServlet() {
		// TODO Auto-generated constructor stub
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String id = null;
		//String id2 = null;
		
		//id = "" + request.getParameter("eventStart")+ "," + request.getParameter("eventID");
		
		
		
		String processData = com.cloud.SimpleMain.output;
		processData = processData.substring(1, processData.length()-1);
		System.out.println(processData);
		String[] processDataArray = processData.split(", ");
		String eventStart = ""+request.getParameter("eventStart");
		String eventID = ""+request.getParameter("eventID");
		int eventId = Integer.parseInt(eventID);
		int newStart = Integer.parseInt(eventStart);
		//System.out.println(eventId);
		String element = processDataArray[eventId];
		System.out.println(element);
		element = element.substring(1, element.length()-1);
		String[] elemParts = element.split(",");
		//System.out.println(elemParts[0]);
		int oldStart= Integer.parseInt(elemParts[0]);
		int duration= Integer.parseInt(elemParts[1]);
		int cpuUsage = Integer.parseInt(elemParts[2]);
		//String newElement = "["+eventStart+","+elemParts[1]+","+elemParts[2]+","+elemParts[3];
		//System.out.println(newElement);
		//processDataArray[eventId] = newElement;
		//String out="[";
		//for(String p:processDataArray)
		//	out = out+p;
		//out = out+"]";
		double oldZone, newZone, oldEnergyCost;
		if(newStart + duration/2<479)
        	newZone = 0.5;
        else if (newStart + duration/2<959)
        	newZone = 1;
        else
        	newZone = 2;
		if(oldStart + duration/2<479)
        	oldZone = 0.5;
        else if (oldStart + duration/2<959)
        	oldZone = 1;
        else
        	oldZone = 2;
        double changeInEnergyCost = (newZone/oldZone - 1)*cpuUsage*duration/1440;
        System.out.println(changeInEnergyCost);
		
		
		
		
		id = eventStart + "," + eventID;
		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(changeInEnergyCost+"%");
		
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

	}

}