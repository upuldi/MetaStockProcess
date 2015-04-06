package com.db;

import com.db.common.CommonJDBCMetaProcess;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: udoluweera
 * Date: 5/3/11
 * Time: 3:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class JDBCMainProcessData {


    private static Logger log = Logger.getLogger(JDBCMainProcessData.class);


    public static long getLastProcessedId() throws ClassNotFoundException, SQLException {

        Connection connection = CommonJDBCMetaProcess.getConnection();
        long lastId = 0L;

        String findSql = "select * from metaprocess.MAIN_PROCESS where" +
                " ID = (select MAX(id) from metaprocess.MAIN_PROCESS );";

        PreparedStatement prepGetTrade = connection.prepareStatement(findSql);

        ResultSet resultSet = prepGetTrade.executeQuery();
        int count = 0;

        while (resultSet.next()) {
            lastId = resultSet.getLong("LAST_ID");
            count = count + 1;
        }

        connection.close();

        if(count > 0 && lastId == 0) {
            throw new RuntimeException(" UN EXPECTED BEHAVIOR...... DATA ISSUE....... ");
        }

        return lastId;
    }

    public static void updateLastProcessedId(long lastProcessedId, String date) throws
            ClassNotFoundException, SQLException {

        Connection connectionFind = CommonJDBCMetaProcess.getConnection();
        String findSql = "select * from  `metaprocess`.`MAIN_PROCESS` p  where p.DATE = ? ;";
        PreparedStatement prepFindProcess = connectionFind.prepareStatement(findSql);
        prepFindProcess.setString(1, date);
        ResultSet results = prepFindProcess.executeQuery();

        long recordId = 0L;
        long lastRecordCount = 0L;

        while (results.next()) {

            recordId = results.getLong("ID");
            lastRecordCount = results.getLong("LAST_ID");
        }

        connectionFind.close();

        if (recordId > 0) {

            /* Update */

            log.debug(lastProcessedId - lastRecordCount  + " Records converted.... ");

            Connection connectionUpdate = CommonJDBCMetaProcess.getConnection();
            String updateSql = "UPDATE `metaprocess`.`MAIN_PROCESS` SET `LAST_ID`= ? WHERE `ID`= ?;";
            PreparedStatement prepUpdateProcess = connectionUpdate.prepareStatement(updateSql);
            prepUpdateProcess.setLong(1,lastProcessedId);
            prepUpdateProcess.setLong(2,recordId);

            prepUpdateProcess.executeUpdate();
            connectionUpdate.close();


        } else {

            /* Insert */

            log.debug("Insert a process record for the day : " + date);

            Connection connectionInsert = CommonJDBCMetaProcess.getConnection();
            String insertSql = "INSERT INTO `metaprocess`.`MAIN_PROCESS` (`ID`, `LAST_ID`, `DATE`) VALUES (null, ? , ?);";

            PreparedStatement prepInsertProcess = connectionInsert.prepareStatement(insertSql);
            prepInsertProcess.setLong(1, lastProcessedId);
            prepInsertProcess.setString(2, date);

            prepInsertProcess.executeUpdate();
            connectionInsert.close();


        }
    }

}
