package com.coinDesk;


import com.coinDesk.utils.InputClientData;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by alekseenkoyuri1989@gmail.com
 * 28.10.2019
 * <p>
 * Loads property from file app.properties, and run reading data from client's console
 */
public class CoinDeskRun {
    private static InputClientData inputClientData = new InputClientData();

    public static void main(String[] args) {
        int period = 0;
        String currentGetData = "";
        String archiveGetData = "";
        Properties prop = new Properties();
        try (InputStream is = CoinDeskRun.class.getClassLoader().getResourceAsStream("app.properties")) {
            prop.load(is);
            period = Integer.parseInt(prop.getProperty("CALCULATION_RANGE"));
            currentGetData = prop.getProperty("API_GET_DATA");
            archiveGetData = prop.getProperty("ARCHIVE_GET_DATA");
            inputClientData.getDataFromConsole(period, currentGetData, archiveGetData);
        } catch (IOException e) {
            System.out.println("app.properties не обнаружено");
        }



    }

}
