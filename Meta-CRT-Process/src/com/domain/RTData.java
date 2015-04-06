package com.domain;

/**
 * Created by IntelliJ IDEA.
 * User: udoluweera
 * Date: 4/28/11
 * Time: 4:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class RTData {

    private long id;
    private String ticker;
    private String date;
    private String time;
    private String open;
    private String high;
    private String low;
    private String close;
    private long vol;
    private String lastTreaded;

    public String getLastTreaded() {
        return lastTreaded;
    }

    public void setLastTreaded(String lastTreaded) {
        this.lastTreaded = lastTreaded;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getClose() {
        return close;
    }

    public void setClose(String close) {
        this.close = close;
    }

    public long getVol() {
        return vol;
    }

    public void setVol(long vol) {
        this.vol = vol;
    }

    public String getInsetSQL(String tableName) {

        String query = "INSERT INTO CSEDAILYDATA."+ tableName +" (`ID`, `TICKER`, `DATE`, `TIME`, `OPEN`, `HIGH`, `LOW`, `CLOSE`,`VOL`,`PROCESSED`) " +
                "VALUES (" + id + " , '"+ ticker +"', '"+ date+"', '" + time +"', '" + open + "', '" + high+ "', '"+ low +"', '" + close+ " ',' " + vol + " ',' " + "N"  +  " ' );";
        return query;
    }
}
