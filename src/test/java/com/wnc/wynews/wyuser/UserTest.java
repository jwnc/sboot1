
package com.wnc.wynews.wyuser;

import com.alibaba.fastjson.JSONObject;
import com.wnc.tools.FileOp;
import com.wnc.wynews.consts.WyConsts;
import com.wnc.wynews.model.Comment;
import com.wnc.wynews.model.User;
import com.wnc.wynews.jpa.EntityConvertor;
import com.wnc.wynews.service.WyDbService;
import com.wnc.wynews.utils.WyNewsUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserTest {

    @Test
    public void testUserAll() {
        WyDbService wyDbService = WyNewsUtil.getWyDbService();

        List<String> userStrs = FileOp.readFrom(WyConsts.USERS_TXT);
        for (String s : userStrs) {
            try {
                User user = getUser(s);
                if (user.getWyRedNameInfo() != null && user.getWyIncentiveInfoList() != null) {
                    System.out.println(user);
                }
                wyDbService.singleUser(EntityConvertor.userToEntity(user));
            }catch (Exception e){
                e.printStackTrace();
                System.err.println(s);
                throw  e;
            }
        }
    }

    @Test
    public void testUser() {
        String userStr = "{\"avatar\":\"http://mobilepics.nosdn.127.net/netease_subject/1e05a0480ec35b8377065348db5f63c11532481577274387\",\"incentiveInfoList\":[\"{\\\"url\\\":\\\"http://cms-bucket.nosdn.127.net/2018/07/09/2c4b45c0a97342da9c2893773db8f3e3.png\\\",\\\"info\\\":\\\"网易红人\\\"}\"],\"location\":\"河北省石家庄市\",\"nickname\":\"燕赵慷慨悲歌\",\"redNameInfo\":[\"{\\\"image\\\":\\\"http://cms-bucket.nosdn.127.net/68e4f9b1662d4041a18496792d3b92ce20180326140742.png\\\",\\\"titleName\\\":\\\"易粉同城会会长\\\",\\\"titleId\\\":\\\"DDR3ASIN\\\",\\\"url\\\":\\\"http://tie.163.com/gt/18/0307/17/DCAHPLDA003097U2.html\\\",\\\"info\\\":\\\"展示会长身份\\\"}\"],\"userId\":94676230}\n";
        System.out.println(getUser(userStr));
    }

    public User getUser(String userStr) {
        String text = userStr.replace("\\", "").replace("[\"{", "[{").replace("}\"]", "}]").replace(",\"{", ",{").replace("\"}\",", "\"},");
//        System.out.println(text);
        return JSONObject.parseObject(text, User.class);
    }

    private void testCmt() {
        String text;
        text = "{\n" +
                "      \"against\": 0,\n" +
                "      \"anonymous\": false,\n" +
                "      \"buildLevel\": 1,\n" +
                "      \"commentId\": 265636821,\n" +
                "      \"content\": \"上班，大致浏览了下。收藏，空了看，\",\n" +
                "      \"createTime\": \"2018-08-29 09:42:27\",\n" +
                "      \"ext\": {\n" +
                "        \n" +
                "      },\n" +
                "      \"favCount\": 0,\n" +
                "      \"ip\": \"123.147.*.*\",\n" +
                "      \"isDel\": false,\n" +
                "      \"postId\": \"DQA3N8NH0525L5Q1_265636821\",\n" +
                "      \"productKey\": \"a2869674571f77b5a0867c3d71db5856\",\n" +
                "      \"shareCount\": 0,\n" +
                "      \"siteName\": \"网易\",\n" +
                "      \"source\": \"ph\",\n" +
                "      \"unionState\": false,\n" +
                "      \"user\": {\n" +
                "        \"avatar\": \"http://cms-bucket.nosdn.127.net/b8e780b546fc41b3af0bf94924dc709320161214212255.jpg\",\n" +
                "        \"incentiveInfoList\": [\n" +
                "          \n" +
                "        ],\n" +
                "        \"location\": \"重庆市\",\n" +
                "        \"nickname\": \"sefico\",\n" +
                "        \"redNameInfo\": [\n" +
                "          {\n" +
                "            \"image\": \"http://cms-bucket.nosdn.127.net/68e4f9b1662d4041a18496792d3b92ce20180326140742.png\",\n" +
                "            \"info\": \"展示会长身份\",\n" +
                "            \"titleId\": \"DDR3ASIN\",\n" +
                "            \"titleName\": \"易粉同城会会长\",\n" +
                "            \"url\": \"http://tie.163.com/gt/18/0307/17/DCAHPLDA003097U2.html\"\n" +
                "          }\n" +
                "        ],\n" +
                "        \"title\": {\n" +
                "          \"image\": \"http://cms-bucket.nosdn.127.net/68e4f9b1662d4041a18496792d3b92ce20180326140742.png\",\n" +
                "          \"info\": \"展示会长身份\",\n" +
                "          \"titleId\": \"DDR3ASIN\",\n" +
                "          \"titleName\": \"易粉同城会会长\",\n" +
                "          \"url\": \"http://tie.163.com/gt/18/0307/17/DCAHPLDA003097U2.html\"\n" +
                "        },\n" +
                "        \"userId\": 61322915,\n" +
                "        \"vipInfo\": \"vipw\"\n" +
                "      },\n" +
                "      \"vote\": 0\n" +
                "    }";
        Comment cmt = JSONObject.parseObject(text, Comment.class);
        System.out.println(cmt);
    }
}
