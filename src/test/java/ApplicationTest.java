import com.coinDesk.CoinDeskRun;
import com.coinDesk.data.CoinDeskDataImpl;
import com.coinDesk.data.ICoinDeskData;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Properties;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;

/**
 * Created by alekseenkoyuri1989@gmail.com
 * 28.10.2019
 */
public class ApplicationTest {
    private static String currency = "USD";
    private static ICoinDeskData coinDeskDataImpl;
    private static int period;
    private static DateFormat dateFormat;
    private static LocalDate date;
    private static URL url;
    private static URL wrongUrl;
    private static URL archiveUrl;
    private static URL wrongArchiveUrl;

    public static void main(String args[]) {

    }

    @BeforeClass
    public static void init() {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        period = 30;
        coinDeskDataImpl = new CoinDeskDataImpl();
        String currentGetData = "";
        String archiveGetData = "";
        date = LocalDate.now();
        LocalDate start = date.minusDays(period);
        try (InputStream is = CoinDeskRun.class.getClassLoader().getResourceAsStream("app.properties")) {
            Properties prop = new Properties();
            prop.load(is);
            period = Integer.parseInt(prop.getProperty("CALCULATION_RANGE"));
            currentGetData = prop.getProperty("API_GET_DATA");
            archiveGetData = prop.getProperty("ARCHIVE_GET_DATA");
            url = coinDeskDataImpl.createURL(currentGetData.
                    replace(".json", String.format("/%s.json", currency))
            );
            wrongUrl = coinDeskDataImpl.createURL(currentGetData.
                    replace(".json", String.format("/%s.json", ""))
            );
            archiveUrl = coinDeskDataImpl.createURL(String.format(
                    "%s?start=%s&end=%s&currency=%s",
                    archiveGetData,
                    start,
                    date,
                    currency
            ));
            wrongArchiveUrl = coinDeskDataImpl.createURL(String.format(
                    "%s?start=%s&end=%s&currency=%s",
                    archiveGetData,
                    start,
                    date,
                    ""
            ));
        } catch (IOException e) {
            System.out.println("app.properties не обнаружено");
        }
    }

    @Test
    public void testValidCurrency() {
        try {
            coinDeskDataImpl = new CoinDeskDataImpl();
            coinDeskDataImpl.getBPI(currency, url);
            String result = coinDeskDataImpl.getLastResponse();
            assertNotNull(result);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    @Test
    public void testRealDataSuccess() {
        try {
            coinDeskDataImpl = new CoinDeskDataImpl();
            boolean success = coinDeskDataImpl.getBPI(currency, url);
            assertTrue(success);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    @Test
    public void testEmptyDataInput() {
        try {
            coinDeskDataImpl = new CoinDeskDataImpl();
            boolean success = coinDeskDataImpl.getBPI("", url);
            assertFalse(success);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    @Test
    public void testArchive() {
        try {
            coinDeskDataImpl = new CoinDeskDataImpl();
            coinDeskDataImpl.getArchiveBPI(archiveUrl);
            String result = coinDeskDataImpl.getLastResponse();
            assertNotNull(result);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    @Test
    public void testArchiveWrongParams() {
        try {
            coinDeskDataImpl = new CoinDeskDataImpl();
            coinDeskDataImpl.getArchiveBPI(wrongArchiveUrl);
            String result = coinDeskDataImpl.getLastResponse();
            assertEquals(null, result);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    @Test
    public void testTodayWrongParams() {
        try {
            coinDeskDataImpl = new CoinDeskDataImpl();
            boolean success = coinDeskDataImpl.getBPI(currency, wrongUrl);
            assertFalse(success);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }
}
