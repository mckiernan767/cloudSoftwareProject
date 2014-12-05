package connect.java;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.security.auth.login.Configuration;

import org.omg.CORBA.portable.InputStream;

public class Db_connection1 {
	public void sendData() {
		String response = null;
		try {
		// The http access shown below will request the registration data from the master

			String response = null;
			try {
			// The http access shown below will request the registration data from the master

				String url = String.format("http://%s:%d/papillonserver/rest/agents/register?ipaddress=%s"",
						     Configuration.getInstance().getMasterIpAddress(),
						     Configuration.getInstance().getMasterPort(),
						     Configuration.getInstance().getClientIpAddress());

			// The connections below enables either the default GET = xml or JSON GET
			HttpURLConnection connection = (HttpURLConnection) (new URL(url)).openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod(Constant.HTTP_METHOD_GET);
			connection.setRequestProperty(Constant.HEADER_ACCEPT, "application/json");
			connection.setRequestProperty(Constant.HEADER_CONTENT_TYPE, "application/json");

			if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
			return;
			}
			// start of response now contains the raw data stream sent back from the server
			InputStream inputStream = connection.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			StringBuilder builder = new StringBuilder();

			String inputLine;
			while ((inputLine = reader.readLine()) != null) {
			builder.append(inputLine);
			}
			reader.close();

			response = builder.toString();

			//End of response now contains the raw data stream sent back from the server

			} catch (IOException | NumberFormatException ex) {
			return;
			}
		}}}