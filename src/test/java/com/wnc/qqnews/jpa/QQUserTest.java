package com.wnc.qqnews.jpa;

import com.alibaba.fastjson.JSONObject;
import com.wnc.qqnews.QqConsts;
import com.wnc.qqnews.jpa.entity.QqUser;
import com.wnc.qqnews.service.QqDbService;
import com.wnc.sboot1.SpringContextUtils;
import com.wnc.tools.FileOp;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class QQUserTest {

    @Test
    public void t() {
        String user = "{\"nick\":\"宋石男\",\"head\":\"http://thirdqq.qlogo.cn/g?b=sdk&k=Idwn5Suwbedia09Ol1YZo6w&s=40&t=1533657600\",\"certinfo\":{\"certhead\":\"http://puep.qpic.cn/coral/Q3auHgzwzM4fgQ41VTF2rON0PRxtIoKicrEribNSZoyRUuf9d4DFU7Kg/100\",\"certinfo\":\"作家，时评人\",\"certnick\":\"宋石男\",\"certidentityid\":\"10001\",\"certappids\":\"10002\"},\"userid\":\"796516161\"}\n";
        getUser(user);

        user = "{\"nick\":\"小萍萍\uD83C\uDF3C\",\"head\":\"http://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTK5iawvyI6AmsOQpZfsW0YscgkrVHkH4G8I8hnB9RKPHF0ctvvCOTVD1icJWzzAL4R4vicolHlMSmA8A/132\",\"uidex\":\"ecdbb75d609ccb00ce9e64ed2ea45a295cbc3785a17bfde40e37c5c9f1dd23ec1e\",\"thirdlogin\":\"1\",\"gender\":\"2\",\"wbuserinfo\":[],\"viptype\":\"0\",\"region\":\"中国:湖北:黄石\",\"userid\":\"843411721\"}\n";
        getUser(user);
    }

    private QqUser getUser(String user) {
        return JSONObject.parseObject(user, QqUser.class).splitRegion().computeCertId();
    }

    private static Logger logger = Logger.getLogger(QQUserTest.class);

    @Test
    public void testAlluser() {
        QqDbService qqDbService = (QqDbService) SpringContextUtils.getContext().getBean("qqDbService");
        List<String> strings = FileOp.readFrom(QqConsts.USERS_TXT);
        for (String user : strings) {
            try {
                qqDbService.singleUser(getUser(user));
            } catch (Exception ex) {
                logger.error("json解析QQ用户出错:" + user, ex);
            }
        }
    }
}
