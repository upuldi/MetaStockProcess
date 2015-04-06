package com.util;

import org.apache.log4j.Logger;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by IntelliJ IDEA.
 * User: udoluweera
 * Date: 5/2/11
 * Time: 6:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class CSEData {


    private static Logger log = Logger.getLogger(CSEData.class);

    public static String getMarketStatusData() throws IOException, MalformedURLException {

        String marketStatus = "";

        try {

            // Construct data
            String data = URLEncoder.encode("callCount", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8");
            data += "&" + URLEncoder.encode("page", "UTF-8") + "=" + URLEncoder.encode("/header.htm", "UTF-8");
            data += "&" + URLEncoder.encode("page", "UTF-8") + "=" + URLEncoder.encode("/header.htm", "UTF-8");
            data += "&" + URLEncoder.encode("httpSessionId", "UTF-8") + "=" + URLEncoder.encode("", "UTF-8");
            // data += "&" + URLEncoder.encode("scriptSessionId", "UTF-8") + "=" + URLEncoder.encode("B625D5F8D9678D8AD05CA0E7D2107C4F18", "UTF-8");
            data += "&" + URLEncoder.encode("scriptSessionId", "UTF-8") + "=" + URLEncoder.encode("", "UTF-8");
            data += "&" + URLEncoder.encode("c0-scriptName", "UTF-8") + "=" + URLEncoder.encode("MarketDataJS", "UTF-8");
            data += "&" + URLEncoder.encode("c0-methodName", "UTF-8") + "=" + URLEncoder.encode("getMarketStatus", "UTF-8");
            data += "&" + URLEncoder.encode("c0-id", "UTF-8") + "=" + URLEncoder.encode("0", "UTF-8");
            data += "&" + URLEncoder.encode("page", "UTF-8") + "=" + URLEncoder.encode("21", "UTF-8");


            // Send data
            URL url = new URL("http://www.cse.lk/dwr/call/plaincall/MarketDataJS.getMarketStatus.dwr");
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);

            conn.setRequestProperty("Accept-Encoding", "gzip,deflate");
            conn.setRequestProperty("Keep-Alive", "111");
            conn.setRequestProperty("Connection", "keep-alive");
            conn.setRequestProperty("Content-Type", "text/plain; charset=UTF-8");
            conn.setRequestProperty("Referer", "http://www.cse.lk/header.htm");
            conn.setRequestProperty("Content-Length", "171");
            conn.setRequestProperty("POSTDATA", "callCount=1\\n" +
                    "page=/header.htm\\n" +
                    "httpSessionId=\\n" +
                    "scriptSessionId=\\n" +
                    "c0-scriptName=MarketDataJS\\n" +
                    "c0-methodName=getMarketStatus\\n" +
                    "c0-id=0\\n" +
                    "batchId=1");


            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();

            // Get the response
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            StringBuilder completeResponse = new StringBuilder();


            while ((line = rd.readLine()) != null) {
                //log.debug(line);
                completeResponse.append(line);
            }
            wr.close();
            rd.close();

            log.debug("Complete Response : " + completeResponse.toString());

            String valueSec =  completeResponse.substring(completeResponse.indexOf("(")+1,completeResponse.indexOf(")"));
            log.debug(valueSec);


            String [] values = valueSec.split("[,]");

            for (String split : values) {

                if (split.contains("~")) {

                    marketStatus = split.substring(1,split.indexOf("~"));
                    log.debug(marketStatus);
                }
            }

        } catch (Exception e) {
        }

        return marketStatus;

    }

}
