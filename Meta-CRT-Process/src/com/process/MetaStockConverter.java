package com.process;

import com.db.JDBCMainProcessData;
import com.db.JDBCRTEData;
import com.domain.RTData;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.util.FileUtils;
import com.util.Flags;

import org.apache.log4j.Logger;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.channels.FileChannel;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

public class MetaStockConverter extends TimerTask {

	private static Logger log = Logger.getLogger(MetaStockConverter.class);
	private static final String COMMA = ",";
	private static final String NEW_LINE = "\r\n";

	private static final String METASTOCK_CONVERTER_LOC = "C:\\MetaStockProcess\\Meta-CRT-Process\\asc2ms";
	private static final String TEXT_FILE_PATH = "C:\\MetaStockProcess\\Meta-CRT-Process\\";
	private static final String TEXT_FILE = "CONVERTED-RT-DATA.txt";

	private static final String DESTINATION_LOCATION = "C:\\MetaStockRealTimeData";
	private static final String MASTER_LOCATION = "C:\\MetaStockProcess\\Meta-CRT-Process\\META-MASTER";

	private Date currentTime;

	public MetaStockConverter() throws FailingHttpStatusCodeException,
			MalformedURLException, IOException, ParseException {
		super();

	}

	@Override
	public void run() {

		if (!Flags.isProcessRunning) {

			Flags.isProcessRunning = Boolean.TRUE;

			log.debug("Converting data..... ");

			long lastProcessedId = 0;
			try {

				lastProcessedId = JDBCMainProcessData.getLastProcessedId();
				log.debug("Last id :  " + lastProcessedId);

				List<RTData> rtDataList = JDBCRTEData
						.getRTData(lastProcessedId);
				log.debug(rtDataList.size()
						+ " records are needed to be converted.");

				if (rtDataList.size() > 0) {

					StringBuffer allcontent = new StringBuffer();
					StringBuilder headers = new StringBuilder();

					File convertedFile = new File(TEXT_FILE);
					Writer output = new BufferedWriter(new FileWriter(
							convertedFile));

					headers.append("<TICKER>");
					headers.append(COMMA);
					headers.append("<DTYYYYMMDD>");
					headers.append(COMMA);
					headers.append("<TIME>");
					headers.append(COMMA);
					headers.append("<OPEN>");
					headers.append(COMMA);
					headers.append("<HIGH>");
					headers.append(COMMA);
					headers.append("<LOW>");
					headers.append(COMMA);
					headers.append("<CLOSE>");
					headers.append(COMMA);
					headers.append("<VOL>");

					allcontent.append(headers);

					StringBuffer singleLineFormatted = null;
					long lastConvertedId = 0L;
					String lastConvertedDate = "";

					for (RTData rtData : rtDataList) {

						lastConvertedId = rtData.getId();
						lastConvertedDate = rtData.getDate();

						singleLineFormatted = new StringBuffer();
						singleLineFormatted.append(NEW_LINE);

						singleLineFormatted.append(rtData.getTicker());
						singleLineFormatted.append(COMMA);

						singleLineFormatted.append(rtData.getDate());
						singleLineFormatted.append(COMMA);

						singleLineFormatted.append(rtData.getTime());
						singleLineFormatted.append(COMMA);

						/* Open */
						singleLineFormatted.append(rtData.getOpen());
						singleLineFormatted.append(COMMA);

						/* High */
						singleLineFormatted.append(rtData.getHigh());
						singleLineFormatted.append(COMMA);

						/* Low */
						singleLineFormatted.append(rtData.getLow());
						singleLineFormatted.append(COMMA);

						/* Close */
						singleLineFormatted.append(rtData.getClose());
						singleLineFormatted.append(COMMA);

						/* Volume */
						singleLineFormatted.append(rtData.getVol());

						log.debug("Single Line  : " + singleLineFormatted);
						allcontent.append(singleLineFormatted);
					}

					log.debug("Writing the out put to the file ");
					output.write(allcontent.toString());
					output.close();

					Runtime runtime = Runtime.getRuntime();
					//Process process = Runtime.getRuntime().exec(METASTOCK_CONVERTER_LOC + " -f  " + TEXT_FILE_PATH + TEXT_FILE + " -o META-MASTER");
					//Process process = Runtime.getRuntime().exec("/home/udoluweera/Desktop/Meta-CRT-Process/asc2ms -f CONVERTED-RT-DATA.txt -o masterdir");
					Process process = Runtime.getRuntime().exec(
							"asc2ms " + " -f  " + TEXT_FILE_PATH + TEXT_FILE
									+ " -o META-MASTER");

					BufferedReader input = new BufferedReader(
							new InputStreamReader(process.getInputStream()));

					String line = null;

					while ((line = input.readLine()) != null) {
						log.debug(line);
					}

					int exitVal = process.waitFor();
					log.debug("Completed with Exit Value : " + exitVal);

					if (exitVal == 0) {

						log.debug("METASTOCK FILES CREATED SUCCESSFULLY.");

						log.debug("Last Converted Id : " + lastConvertedId);

						/* Update the last processed id. */
						JDBCMainProcessData.updateLastProcessedId(
								lastConvertedId, lastConvertedDate);

						fileHandling();

					} else {
						log.debug("ERROR IN CREATING METASTOCK FILES");
						System.exit(0);
					}

				} else {
					log.debug("All the files are up-to dated..");
				}

			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			Flags.isProcessRunning = Boolean.FALSE;
			log.debug("Process Completed !!! ");

		}else {
			log.debug("Another Process is running ..... waiting ........");
		}

	}

	private void fileHandling() throws IOException {

		log.info("deleting files");

		FileUtils.deleteFiles(DESTINATION_LOCATION, ".DAT");

		File emaster = new File(DESTINATION_LOCATION + "/" + "EMASTER");
		File master = new File(DESTINATION_LOCATION + "/" + "MASTER");

		try {

			emaster.delete();
			master.delete();

		} catch (Exception e) {
			log.info(e.getMessage());
			e.printStackTrace();
		}

		log.info("File Deleted");

		log.info("Copy Files");

		FileUtils.copyFile(MASTER_LOCATION, DESTINATION_LOCATION, ".DAT");

		File inEmaster = new File(MASTER_LOCATION + "/" + "EMASTER");
		File outEmaster = new File(DESTINATION_LOCATION + "/" + "EMASTER");

		FileChannel inChannelEmaster = new FileInputStream(inEmaster)
				.getChannel();
		FileChannel outChannelEmaster = new FileOutputStream(outEmaster)
				.getChannel();

		inChannelEmaster.transferTo(0, inChannelEmaster.size(),
				outChannelEmaster);

		File inMaster = new File(MASTER_LOCATION + "/" + "MASTER");
		File outMaster = new File(DESTINATION_LOCATION + "/" + "MASTER");

		FileChannel inChannelMaster = new FileInputStream(inMaster)
				.getChannel();
		FileChannel outChannelMaster = new FileOutputStream(outMaster)
				.getChannel();

		inChannelMaster.transferTo(0, inChannelMaster.size(), outChannelMaster);

	}
}
