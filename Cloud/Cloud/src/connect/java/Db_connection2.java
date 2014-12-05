package connect.java;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

public class Db_connection2 {

	public static void main(String[] args) {

		Db_connection2 connection = new Db_connection2();
		String s = connection.sendData();
		System.out.println(s);
	}

	public String sendData() {
		String response = null;
		try {
			// The http access shown below will request the registration data
			// from the master

			String url = String
					.format("http://127.0.0.1:8080/papillonserver/rest/datacenters/1/floors/1/racks/1/hosts/1/activity?starttime=1414597400&endtime=1414597480");

			// The connections below enables either the default GET = xml or
			// JSON GET
			HttpURLConnection connection = (HttpURLConnection) (new URL(url))
					.openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Accept", "application/json");
			connection.setRequestProperty("Content-Type", "application/json");

			if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
				System.out
						.println("not working" + connection.getResponseCode());
				return "not working";
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

			response = builder.toString();
			System.out.println("builder" + builder);

			// End of response now contains the raw data stream sent back from
			// the server

		} catch (IOException | NumberFormatException ex) {
			return "exception";
		}
		return response;
	}
}