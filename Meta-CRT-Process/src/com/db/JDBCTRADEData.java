package com.db;

import com.domain.Trade;
import org.apache.log4j.Logger;

import java.sql.*;

/**
 * Created by IntelliJ IDEA.
 * User: udoluweera
 * Date: 4/29/11
 * Time: 1:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class JDBCTRADEData {


    private static Logger log = Logger.getLogger(JDBCTRADEData.class);
    private static final String USER_NAME = "root";
    private static final String PASS_WD = "root";


    /**
     * This method is used to insert the trade count and understand whether a trade has been
     * happned durring the time. If the trade has been happned it will return the trade volumen
     * during the time. If it returns the same value as the previous volume there has not been
     * any trade happned durring that time.
     *
     *
     * @param trade
     * @return
     */
    public static Trade getTradeCount(Trade trade) {

        Connection con = null;
        String url = "jdbc:mysql://172.16.0.189:3306/";
        String db = "CSEDAILYDATA";
        String driver = "com.mysql.jdbc.Driver";
        String table = "RT_DATA";

        Trade returnTrade = new Trade();
        returnTrade.setDate(trade.getDate());
        returnTrade.setTicker(trade.getTicker());
        returnTrade.setTradeCount(trade.getTradeCount());

        long previousTradeCount = 0L;
        long previousVol = 0L;

        long newTradeCount = trade.getTradeCount();
        long newVol = trade.getLastVol();

        try {

            Class.forName(driver);
            con = DriverManager.getConnection(url + db, USER_NAME, PASS_WD);
            try {

                String findSql = "select * from  `CSEDAILYDATA`.`DAILY_TRADES` t " +
                        "where t.date = ? and t.ticker = ? ;";

                PreparedStatement prepGetTrade = con.prepareStatement(findSql);
                prepGetTrade.setString(1, trade.getDate());
                prepGetTrade.setString(2, trade.getTicker());
                ResultSet resultSet = prepGetTrade.executeQuery();

                while (resultSet.next()) {

                    previousTradeCount = resultSet.getLong("TRADES");
                    previousVol = resultSet.getLong("PRE_VOL");
                }

                log.debug("Trade count :  " + previousTradeCount + " previous vol : " + previousVol);
                returnTrade.setPreviousTradeCount(previousTradeCount);
                prepGetTrade.close();

                if (0L == previousTradeCount) {

                    log.debug("No trade count found for the day for "+ trade.getTicker() +" .. inserting.");

                    /* Here the trade will be inserted with the recorded volume. */
                    returnTrade.setLastVol(trade.getLastVol());

                    String tableTrades = "DAILY_TRADES";

                    Statement insertTrade = con.createStatement();
                    int val = insertTrade.executeUpdate(trade.getSqlString(tableTrades));
                    log.debug("Trade Inserted : " + val);
                    insertTrade.close();

                } else if (newTradeCount > previousTradeCount && 0L < previousTradeCount) {

                    log.debug("New Trades found for "+  trade.getTicker()  +" .. updating...");

                    /* Here the volume needed to be calculated. */
                    returnTrade.setLastVol(newVol - previousVol);


                    String updateSql = "update `CSEDAILYDATA`.`DAILY_TRADES` " +
                            "set TRADES = ? , PRE_VOL =  ? " +
                            "where date = ? and ticker = ? ;";

                    PreparedStatement prepUpdateTrade = con.prepareStatement(updateSql);
                    prepUpdateTrade.setLong(1, trade.getTradeCount());
                    prepUpdateTrade.setLong(2, newVol - previousVol);
                    prepUpdateTrade.setString(3, trade.getDate());
                    prepUpdateTrade.setString(4, trade.getTicker());
                    prepUpdateTrade.executeUpdate();
                    prepUpdateTrade.close();

                    //returnTrade.setPreviousTradeCount(trade.getTradeCount());

                }


            } catch (SQLException s) {
                log.debug("SQL statement is not executed for  " + table + " !");
                s.printStackTrace();
            } finally {
                con.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnTrade;
    }

}
