package com.coinDesk.data;

import com.coinDesk.utils.TextPredicate;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * Created by alekseenkoyuri1989@gmail.com
 * 28.10.2019
 */

public class CoinDeskDataImpl implements ICoinDeskData {

    private URL url;
    private TextPredicate textPredicate = String::isEmpty;
    private String jsonResult;

    public CoinDeskDataImpl() {
    }

    /**
     * Generate current value for input currency.
     * from  https://api.coindesk.com/v1/bpi/currentprice/<код>. json
     * by input URL and currency's type
     * <p>
     * and prints it on console.
     *
     * @param currencyCode
     * @param url
     * @return true if service coindesk successfully return current value for input currency
     */
    @Override
    public boolean getBPI(String currencyCode, URL url) {
        try {
            jsonResult = getJsonResult(url);
            if (!textPredicate.isEmpty(jsonResult)) {
                JSONObject mainJsonObject = new JSONObject(jsonResult);
                JSONObject bpi = mainJsonObject.getJSONObject("bpi");
                JSONObject gbp = bpi.getJSONObject(currencyCode.toUpperCase());
                String code = gbp.getString("code");
                String rate = gbp.getString("rate");
                System.out.println("Today, the " + code + " exchange in bitcoins is = " + rate);
            } else {
                System.out.println("No Json data found.");
            }
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * Get Min and Max value on 30 days period.
     * from  https://api.coindesk.com/v1/bpi/historical/close.json?start=2013-09-01&end=2013-09-05
     * by input URL
     * <p>
     * and prints it on console.
     *
     * @param url
     */
    @Override
    public void getArchiveBPI(URL url) {
        try {
            jsonResult = getJsonResult(url);
            if (!textPredicate.isEmpty(jsonResult)) {
                JSONObject mainJsonObject = new JSONObject(jsonResult);
                JSONObject bpi = mainJsonObject.getJSONObject("bpi");
                Map<String, Double> map = new HashMap<>();
                ObjectMapper mapper = new ObjectMapper();
                map = mapper.readValue(bpi.toString(), new TypeReference<HashMap<String, Double>>() {
                });
                List valueList = new ArrayList(map.values());
                Collections.sort(valueList);

                System.out.println("    Min value in 30 days = " + valueList.get(0));
                System.out.println("    Max value in 30 days = " + valueList.get(valueList.size() - 1) + "\n");
                System.out.print("Input a new currency: ");
            } else {
                System.out.println("No Json archive data found.");
            }
        } catch (Exception ex) {
            System.out.println("Exception! Check the input.");
        }
    }

    /**
     * Generate Json request from https://www.coindesk.com/api   by input URL
     *
     * @param url
     * @return Json Result
     * @throws IOException
     */
    private String getJsonResult(URL url) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        HttpURLConnection httpUrlConnection = (HttpURLConnection) url.openConnection();
        httpUrlConnection.setRequestMethod("GET");
        httpUrlConnection.setRequestProperty("accept", "application/json");
        httpUrlConnection.setDoOutput(true);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpUrlConnection.getInputStream()));
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line).append("\n");
        }

        return stringBuilder.toString();
    }

    @Override
    public String getLastResponse() {
        return jsonResult;
    }

    /**
     * Create URL by input String
     *
     * @param urlString
     * @return
     */
    @Override
    public URL createURL(String urlString) {
        if (!textPredicate.isEmpty(urlString)) {
            try {
                url = new URL(urlString);
            } catch (MalformedURLException malformedURLException) {
                System.out.println("malformedURLException : " + malformedURLException.getMessage());
            }
        }
        return url;
    }

}
