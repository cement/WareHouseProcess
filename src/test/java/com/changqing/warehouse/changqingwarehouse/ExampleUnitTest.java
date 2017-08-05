package com.changqing.warehouse.changqingwarehouse;





import org.junit.Test;

import java.text.MessageFormat;

import static junit.framework.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }
    @Test
    public void messageFormatTest(){
        String url = "http://192.168.0.246:8000/StartRev?DevId={0}&DispNo={1}";
        url = MessageFormat.format(url,new Object[]{"80001707","123456"});
        System.out.println("messageFormatTest() called\r\n"+url);
    }
}