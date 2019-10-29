package com.coinDesk.data;

import java.net.URL;

public interface ICoinDeskData {
    boolean getBPI(String currencyCode, URL url);
    void getArchiveBPI(URL url);
    URL createURL(String urlString);
    String getLastResponse();
}
