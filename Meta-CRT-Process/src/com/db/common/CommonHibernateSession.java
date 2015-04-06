package com.db.common;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Created by IntelliJ IDEA.
 * User: udoluweera
 * Date: 5/3/11
 * Time: 5:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class CommonHibernateSession {

    protected static Session getSession() {

        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        return sessionFactory.openSession();

    }
}
