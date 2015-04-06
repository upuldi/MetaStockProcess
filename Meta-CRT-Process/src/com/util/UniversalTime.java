package com.util;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class UniversalTime {

    private static Logger log = Logger.getLogger(UniversalTime.class);



	public UniversalTime() {
		super();
	}

	public static Date getTime() throws FailingHttpStatusCodeException, MalformedURLException, IOException, ParseException {

		URL cseStockOverview = new URL("http://www.timeanddate.com/worldclock/fullscreen.html?n=389");
		URLConnection yc = cseStockOverview.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
		String inputLine;
		String fileString = "";
		int lineNo=0;

		Date dateObj = null;

		while ((inputLine = in.readLine()) != null) {

			//log.debug(" lineNo " + lineNo + inputLine);

			if(lineNo == 60) {

				//log.debug(" LINE : " + inputLine);


				String [] dateStr = inputLine.split("<div id=i_time>");

				String timeStr = dateStr[1].substring(0, 11);
				log.debug(" TIME STR : " + timeStr);

				String DATE_FORMAT = "h:mm:ss a";
			    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

			    Calendar calendar = Calendar.getInstance();
			    dateObj = sdf.parse(timeStr);

			    dateObj.setDate(calendar.getTime().getDate());
			    dateObj.setYear(calendar.getTime().getYear());
			    dateObj.setMonth(calendar.getTime().getMonth());

			    log.debug("DATE OBJ : " + dateObj);


			}
			lineNo++;
		}
		in.close();

		return dateObj;
	}


}
