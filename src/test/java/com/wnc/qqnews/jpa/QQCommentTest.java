package com.wnc.qqnews.jpa;

import com.wnc.qqnews.QqCmtTask;
import com.wnc.wynews.model.NewsModule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class QQCommentTest {

    @Test
    public void t() {
        new QqCmtTask(new NewsModule("社会", new Date()), "2887593480").run();
    }
}
