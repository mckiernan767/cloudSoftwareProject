package Conversion;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class TimeConversion {
	String dt;
	
	public String getDt() {
		return dt;
	}

	public void setDt(String dt) {
		this.dt = dt;
	}

	public long unixTimeStampToLong(){
		DateFormat formatter;
		java.util.Date date = null;
		long unixtime;
		formatter = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
		try {
			date = formatter.parse(dt);
		} catch (ParseException ex) {

			ex.printStackTrace();
		}
		unixtime = date.getTime() / 1000L;
		return unixtime;
	}

}
