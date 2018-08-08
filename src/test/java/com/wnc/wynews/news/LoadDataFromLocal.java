
package com.wnc.wynews.news;

import com.alibaba.fastjson.JSONObject;
import com.wnc.basic.BasicFileUtil;
import com.wnc.tools.FileOp;
import com.wnc.wynews.consts.WyConsts;
import com.wnc.wynews.model.Comment;
import com.wnc.wynews.model.User;
import org.junit.Test;

import java.io.File;
import java.util.List;
import java.util.Set;

public class LoadDataFromLocal {
    /**
     * @Description 单文件测试
     * @Date 2018/7/26 16:34
     * @Param
     * @Return void
     */
    @Test
    public void cmtTest() throws Exception {
        String file = WyConsts.COMMENTS_DIR + "001\\cmt-DM1F1N6A000887BT - 副本.txt";
        List<String> strings = FileOp.readFrom(file, "UTF-8");
        for (String line : strings) {
//                System.out.println(line);
            try {
                JSONObject jsonObject = JSONObject.parseObject(line);
                JSONObject commentsJO = jsonObject.getJSONObject("comments");
                Set<String> keys = commentsJO.keySet();
                for (String key : keys) {
                    JSONObject cmtJO = commentsJO.getJSONObject(key);
                    Comment cmt = cmtJO.toJavaObject(Comment.class);
                    User user = cmt.getUser();

                    cmt.setUser(null);
                    cmt.setUserId(user.getUserId());

                    // 输出cmt到999目录

                    if (!cmt.isAnonymous()) {
                        System.out.println(user.getNickname() + " / " + user.getUserId());
                        if (user.getIncentiveInfoList() == null || user.getIncentiveInfoList().length == 0) {
                            user.setIncentiveInfoList(null);
                        }
                        if (user.getRedNameInfo() == null || user.getRedNameInfo().length == 0) {
                            user.setRedNameInfo(null);
                        }
                        // 输出user到user目录
                    }
                }
            } catch (Exception e) {
                System.out.println(line);
            }

        }
    }

    /**
     * @Description 文件夹测试
     * @Date 2018/7/26 16:34
     * @Param
     * @Return void
     */
    @Test
    public void cmtTestAll() throws Exception {
        String file = WyConsts.COMMENTS_DIR + "\\001\\";

        for (File f : new File(file).listFiles()) {
            try {
                List<String> strings = FileOp.readFrom(f.getAbsolutePath(), "UTF-8");
                for (String line : strings) {
//                System.out.println(line);
                    JSONObject jsonObject = JSONObject.parseObject(line);
                    JSONObject commentsJO = jsonObject.getJSONObject("comments");
                    Set<String> keys = commentsJO.keySet();
                    for (String key : keys) {
                        JSONObject cmtJO = commentsJO.getJSONObject(key);
                        Comment cmt = cmtJO.toJavaObject(Comment.class);
                        User user = cmt.getUser();

                        cmt.setUser(null);
                        cmt.setUserId(user.getUserId());

                        // 输出cmt到999目录
                        BasicFileUtil.writeFileString(WyConsts.COMMENTS_DIR + "999\\" + f.getName(),
                                JSONObject.toJSONString(cmt) + "\r\n", null, true);

                        if (!cmt.isAnonymous()) {
                            System.out.println(user.getNickname() + " / " + user.getUserId());
                            if (user.getIncentiveInfoList() == null || user.getIncentiveInfoList().length == 0) {
                                user.setIncentiveInfoList(null);
                            }
                            if (user.getRedNameInfo() == null || user.getRedNameInfo().length == 0) {
                                user.setRedNameInfo(null);
                            }
                            // 输出user到user目录
                            BasicFileUtil.writeFileString(WyConsts.USERS_TXT,
                                    JSONObject.toJSONString(user) + "\r\n", null, true);
                        }
                    }

                }
            } catch (Exception e) {
                System.out.println(e.getMessage() + " " + f.getName());
                BasicFileUtil.writeFileString(WyConsts.APP_DIR + "loaderr.txt",
                        f.getName() + "\r\n", null, true);
            }
        }
    }
}
