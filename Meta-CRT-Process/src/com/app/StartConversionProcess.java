package com.app;

import com.process.MetaStockConverter;

import java.io.IOException;
import java.text.ParseException;
import java.util.Timer;

/**
 * Created by IntelliJ IDEA.
 * User: udoluweera
 * Date: 4/29/11
 * Time: 12:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class StartConversionProcess {

    public static void main (String [] args) throws IOException, ParseException {

        System.out.println("Start Converting.........");

		Timer timer = new Timer();
        timer.schedule(new MetaStockConverter(),0,60*1000);


    }


}
