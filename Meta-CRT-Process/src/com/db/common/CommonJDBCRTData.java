package com.db.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: udoluweera
 * Date: 5/3/11
 * Time: 4:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class CommonJDBCRTData {

    protected static String url = "jdbc:mysql://localhost:3306/";
    protected static String db = "CSEDAILYDATA";
    protected static String driver = "com.mysql.jdbc.Driver";
    protected static final String USER_NAME = "root";
    protected static final String PASS_WD = "root";

    public static Connection getConnection() throws ClassNotFoundException, SQLException {

        Class.forName(driver);
        Connection con = DriverManager.getConnection(url + db, USER_NAME, PASS_WD);
        return con;
    }
}
