package com.domain;

/**
 * Created by IntelliJ IDEA.
 * User: udoluweera
 * Date: 4/29/11
 * Time: 2:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class Trade {

    private long id;
    private String date;
    private String ticker;
    private long tradeCount;
    private long lastVol;
    private long previousTradeCount;

    public long getPreviousTradeCount() {
        return previousTradeCount;
    }

    public void setPreviousTradeCount(long previousTradeCount) {
        this.previousTradeCount = previousTradeCount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public long getTradeCount() {
        return tradeCount;
    }

    public void setTradeCount(long tradeCount) {
        this.tradeCount = tradeCount;
    }

    public long getLastVol() {
        return lastVol;
    }

    public void setLastVol(long lastVol) {
        this.lastVol = lastVol;
    }

    public String getSqlString(String tableName) {

        return "INSERT INTO `CSEDAILYDATA`.`" +tableName +"` (`id`, `DATE`, `TICKER`, `PRE_VOL`, `TRADES`) " +
                "VALUES ('"+ id +"', '"+ date +"', '" + ticker + "', '"+ lastVol +"', '"+ tradeCount+"');";

    }

}
