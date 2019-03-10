package jijin.spider;

import com.wnc.jijin.CurrentValueData;
import com.wnc.jijin.JijinSpider;
import com.wnc.jijin.ValueData;
import org.junit.Test;

import java.util.List;

public class JinjinSpiderTest {
    @Test
    public void getLatelyValueDataTest() throws Exception {
        List<ValueData> latelyValueData = new JijinSpider("001593").getLatelyValueData();
        System.out.println(latelyValueData);
    }

    @Test
    public void getCurrentValueData() throws Exception {
        CurrentValueData currentValueData = new JijinSpider("001593").getCurrentValueData();
        System.out.println(currentValueData);
    }

    @Test
    public void downloadPic() throws Exception {
        new JijinSpider("001593").downloadPic();
    }
}
