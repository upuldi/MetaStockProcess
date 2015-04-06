package com.db;

import com.db.common.CommonHibernateSession;
import com.domain.RTData;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: udoluweera
 * Date: 4/29/11
 * Time: 12:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class JDBCRTEData extends CommonHibernateSession {

        private static Logger log = Logger.getLogger(JDBCRTEData.class);


    public static List<RTData> getRTData(long lastId) throws ClassNotFoundException, SQLException {

        List<RTData> rtDatasList = null;

        Session session = getSession();
        Query query = session.getNamedQuery("getAllRTDataAfterID");
        query.setLong("id", lastId);

        rtDatasList = query.list();

        log.debug("List Size : " + rtDatasList.size() );

        session.close();




        return rtDatasList;
    }


}
