package connect.java;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import Resource.Host;

public class Connection {
	String baseURL = "http://127.0.0.1:8080/papillonserver/rest/";

	public JSONObject getInformationResource(String URL) {
		JSONObject output=null;
		try {
			HttpURLConnection connection = (HttpURLConnection) (new URL(baseURL
					+ URL)).openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Accept", "application/json");
			connection.setRequestProperty("Content-Type", "application/json");

			if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
				System.out
						.println("not working" + connection.getResponseCode());
				return null;
			}
			// start of response now contains the raw data stream sent back from
			// the server
			InputStream inputStream = connection.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream));
			
			StringBuilder builder = new StringBuilder();
			String inputLine;
			while ((inputLine = reader.readLine()) != null) {
				builder.append(inputLine);
			}
			reader.close();

			JSONParser jp = new JSONParser();
			output = (JSONObject) jp.parse(builder.toString());

		} catch (Exception e) {
			// TODO: handle exception
		}
		return output;

	}
	
	public JSONArray getInformationResources(String URL) {
		JSONArray output=null;
		try {
			HttpURLConnection connection = (HttpURLConnection) (new URL(baseURL
					+ URL)).openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Accept", "application/json");
			connection.setRequestProperty("Content-Type", "application/json");

			if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
				System.out
						.println("not working" + connection.getResponseCode());
				return null;
			}
			// start of response now contains the raw data stream sent back from
			// the server
			InputStream inputStream = connection.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream));
			
			StringBuilder builder = new StringBuilder();
			String inputLine;
			while ((inputLine = reader.readLine()) != null) {
				builder.append(inputLine);
			}
			reader.close();

			JSONParser jp = new JSONParser();
			output = (JSONArray) jp.parse(builder.toString());

		} catch (Exception e) {
			// TODO: handle exception
		}
		return output;

	}

	

	/*public static void main(String[] args) {

		Connection connection = new Connection();

		String f = connection.sendData();
		System.out.println(f);

		try {
			
			System.out.println("j" + j);

			JSONObject structure = (JSONObject) j.get("activity");
			JSONObject jo = (JSONObject) structure.get("apps");
			System.out.println("j" + jo);
			System.out.println("description: " + structure.get("powerMode"));
			System.out.println("description: " + jo.get("name"));

			JSONArray arr = (JSONArray) jo.get("app");
			for (int i = 0; i < arr.size(); i++) {
				System.out.println("first" + arr.get(i));
			}

			Iterator i = arr.iterator();
			// take each value from the json array separately
			JSONObject innerObj = null;
			while (i.hasNext()) {
				innerObj = (JSONObject) i.next();
				System.out.println("application " + innerObj.get("name")
						+ " with cpu " + innerObj.get("cpu"));
			}

			// Host host1 = new Host();
			// host1.setID((int)innerObj.get("appId"));

		} catch (Exception pe) {
			System.out.println("position: " + pe);
			System.out.println(pe);
		}
	}*/

	
}