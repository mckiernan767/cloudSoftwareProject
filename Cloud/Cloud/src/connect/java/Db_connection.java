package connect.java;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.omg.CORBA.portable.OutputStream;

public class Db_connection {


	private void sendData(String data, String url, String httpMethod) {
		try {
			HttpURLConnection connection = (HttpURLConnection) (new URL(url))
					.openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod(httpMethod); // or GET, PUT, PATH,
														// DELETE
			connection.setRequestProperty("Accept", "application/json"); // or
																			// application/xml
			connection.setRequestProperty("Content-Type", "application/json"); // or
			System.out.println("hello"); // application/xml

			if (data != null && data.length() > 0) {
				try (OutputStream outputStream = (OutputStream) connection
						.getOutputStream()) {
					OutputStreamWriter writer = new OutputStreamWriter(
							outputStream);
					writer.write(data);
					writer.close();
					System.out.println("hello");
				}
			}

			String responseMsg = connection.getResponseMessage();
			System.out.print("response " + responseMsg);

			if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
				// something went wrong
			} else {
				// yeah - it worked
			}
		} catch (IOException | NumberFormatException ex) {
			// something went terribly wrong
		}
	}
}
