package per.edward.wechatautomationutil;

import org.junit.Test;

import java.util.ArrayList;
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

        readData("helloworld123\n哈哈\nhelloworld12345");
    }

    private void readData(String temp) {
        List<String> list = new ArrayList<String>();
        //        String[] listData = temp.split("\n");
        list.add("测1试");
        list.add("测2试");
        list.add("测3试");
        list.set(1,list.get(1)+ "123");
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }

//        for (int i = 0; i < list.size(); i++) {
//        }
    }

}