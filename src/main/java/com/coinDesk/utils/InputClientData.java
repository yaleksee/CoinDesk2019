package com.coinDesk.utils;


import com.coinDesk.data.CoinDeskDataImpl;

import java.net.URL;
import java.time.LocalDate;
import java.util.Scanner;

/**
 * Created by alekseenkoyuri1989@gmail.com
 * 28.10.2019
 * <p>
 * It controls the process of inputting information from the client,
 * validates,
 * and calls the class to send Get requests to the service coindesk.
 */

public class InputClientData {
    private LocalDate date;
    private TextPredicate textPredicate = String::isEmpty;

    public void getDataFromConsole(int period, String currentGetData, String archiveGetData) {
        date = LocalDate.now();
        System.out.print("Enter the currency's type, or exit: ");
        try (Scanner scannerInput = new Scanner(System.in)) {
            String inputData = "";
            for (; ; ) {
                inputData = scannerInput.nextLine();
                if (inputData.toLowerCase().equals("exit")) {
                    System.out.println("Program finished");
                    break;
                } else if (textPredicate.isEmpty(inputData)) {
                    System.out.println("Currency is empty, try again!");
                    continue;
                } else {
                    CoinDeskDataImpl data = new CoinDeskDataImpl();
                    URL url = data.createURL(currentGetData.
                            replace(".json", String.format("/%s.json", inputData))
                    );

                    if (data.getBPI(inputData, url)) {
                        LocalDate start = date.minusDays(period);
                        URL archiveUrl = data.createURL(String.format(
                                "%s?start=%s&end=%s&currency=%s",
                                archiveGetData,
                                start,
                                date,
                                inputData
                        ));
                        data.getArchiveBPI(archiveUrl);
                    } else {
                        System.out.print("No info for this time, try again: ");
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println("\n" + ex.getMessage());
        }
    }
}
