package connect.java;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import Resource.Host;

public class Connection {
	String baseURL = "http://127.0.0.1:8080/papillonserver/rest/";

	public JsonObject getInformationResource(String URL) {
		JsonObject output=null;
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

			JsonParser jp = new JsonParser();
			output = (JsonObject) jp.parse(builder.toString());

			//System.out.println("jsonObject = " + output);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return output;

	}

		public JsonArray getInformationResources(String URL) {
		JsonArray output=null;
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

			JsonParser jp = new JsonParser();
			output = (JsonArray) jp.parse(builder.toString());

			
			//System.out.println("jsonArray = " + output);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return output;

	}
}