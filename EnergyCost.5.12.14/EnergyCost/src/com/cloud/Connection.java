package  com.cloud;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;



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
}
