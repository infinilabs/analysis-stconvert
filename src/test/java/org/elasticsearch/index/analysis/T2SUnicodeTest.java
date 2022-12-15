package org.elasticsearch.index.analysis;

import junit.framework.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class T2SUnicodeTest {
    private static final Logger LOGGER = Logger.getLogger(T2SUnicodeTest.class.getName());
    private final Properties charMap = new Properties();

    public static String unicode(String s) {
        return s.chars().mapToObj(i -> String.format("\\u%04x", i)).collect(Collectors.joining());
    }

    @BeforeTest
    public void setUp() {
        InputStream file1;
        file1 = this.getClass().getResourceAsStream("/t2s.properties");
        InputStreamReader is = null;
        is = new InputStreamReader(file1, StandardCharsets.UTF_8);
        if (is != null) {
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(is);
                charMap.load(reader);
            } catch (FileNotFoundException e) {
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "IOException in loading charMap: " + e.getMessage(), e);
            } finally {
                try {
                    if (reader != null)
                        reader.close();
                    is.close();
                } catch (IOException e) {
                }
            }
        }
    }

    @Test
    void testUnicode() {
        Iterator iter = charMap.keySet().iterator();
        List<String> errMsgs = new ArrayList<>();
        String sep = "---";
        while (iter.hasNext()) {
            String key = (String) iter.next();
            String value = (String) charMap.get(key);
            if (key.toCharArray().length != value.toCharArray().length) {
                String rawValue = key + sep + value;
                String unicodeValue = unicode(key) + sep + unicode(value);
                errMsgs.add(rawValue + "\n" + unicodeValue);
            }
        }
        if (errMsgs.size() > 0) {
            int errCnt = errMsgs.size();
            String preMsg = "Unicode length not math. errCnt: " + errCnt;
            Assert.fail(preMsg + "\n" + String.join("\n", errMsgs));
        }
    }
}
