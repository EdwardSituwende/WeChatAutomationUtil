package per.edward.wechatautomationutil;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {

//        readData("helloworld123\n哈哈\nhelloworld12345");
    }

    @Test
    public void readData11() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
       String t1=simpleDateFormat.format(new Date(System.currentTimeMillis()));
        System.out.println(t1);
//        for (int i = 0; i < list.size(); i++) {
//        }
    }

}