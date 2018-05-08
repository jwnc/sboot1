
package com.wnc.sboot1.jpa.zhihu;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.HttpGet;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSONObject;
import com.crawl.core.util.HttpClientUtil;
import com.wnc.sboot1.spy.zhihu.TT2;
import com.wnc.sboot1.spy.zhihu.active.TargetAggreInfo;
import com.wnc.sboot1.spy.zhihu.active.target.Answer;
import com.wnc.sboot1.spy.zhihu.active.target.Article;
import com.wnc.sboot1.spy.zhihu.active.target.Question;
import com.wnc.sboot1.spy.zhihu.active.target.RoundTable;
import com.wnc.sboot1.spy.zhihu.active.target.ZColumn;
import com.wnc.sboot1.spy.zhihu.rep.AnswerRepository;
import com.wnc.sboot1.spy.zhihu.rep.ArticleRepository;
import com.wnc.sboot1.spy.zhihu.rep.CollectionRepository;
import com.wnc.sboot1.spy.zhihu.rep.ColumnRepository;
import com.wnc.sboot1.spy.zhihu.rep.QuestionRepository;
import com.wnc.sboot1.spy.zhihu.rep.RoundTableRepository;
import com.wnc.sboot1.spy.zhihu.rep.TargetAggreInfoRepository;
import com.wnc.sboot1.spy.zhihu.rep.TopicRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TargetDescriptionDay {
	private static Logger logger = Logger.getLogger(TargetDescriptionDay.class);
	@Autowired
	AnswerRepository answerRepository;
	@Autowired
	ArticleRepository articleRepository;
	@Autowired
	QuestionRepository questionRepository;
	@Autowired
	CollectionRepository collectionRepository;
	@Autowired
	ColumnRepository columnRepository;
	@Autowired
	RoundTableRepository roundTableRepository;
	@Autowired
	TopicRepository topicRepository;
	@Autowired
	TargetAggreInfoRepository targetAggreInfoRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Test
	public void a() {
		String sql = "select act.cnt,tar.id tid,tar.info title,tar.type, 1 aggre_code, '' description, '' url from (SELECT target_id, count(*) cnt FROM zh_activity WHERE FROM_UNIXTIME(created_time) >= '2018-05-07 00:00;00' and FROM_UNIXTIME(created_time) < '2018-05-08 00:00:00' group by target_id having count(*) > 10) act, zh_target tar where act.target_id=tar.id";
		Query createNativeQuery = entityManager.createNativeQuery(sql, TargetAggreInfo.class);
		List<TargetAggreInfo> resultList = createNativeQuery.getResultList();
		for (TargetAggreInfo targetAggreInfo : resultList) {
			find(targetAggreInfo);
		}
		targetAggreInfoRepository.save(resultList);
		System.out.println(resultList);
	}

	private void find(TargetAggreInfo targetAggreInfo) {
		String description = "";
		String url = "";
		try {
			String type = targetAggreInfo.getType();
			String tid = targetAggreInfo.getTid();
			switch (type) {
			case "answer":
				Answer findOne = answerRepository.findOne(tid);
				String question_id = findOne.getQuestion_id();
				Question findOne2 = questionRepository.findOne(question_id);
				String title = "";
				if (findOne2 == null) {
					HttpGet request = new HttpGet("https://api.zhihu.com/questions/19696146");
					request.setHeader("authorization", "oauth " + TT2.initAuthorization());
					String webPage = HttpClientUtil.getWebPage(request);
					title = JSONObject.parseObject(webPage).getString("title");
				} else {
					title = findOne2.getTitle();
				}
				if (StringUtils.isBlank(title)) {
					return;
				}
				description = findOne.getExcerpt_new();
				targetAggreInfo.setDescription(description);
				targetAggreInfo.setTitle(title);
				url = "https://www.zhihu.com/question/" + question_id + "/answer/" + tid;
				break;
			case "article":
				Article article = articleRepository.findOne(tid);
				description = article.getExcerpt_new();
				url = "https://zhuanlan.zhihu.com/p/" + tid;
				break;
			case "question":
				Question question = questionRepository.findOne(tid);
				description = question.getExcerpt();
				url = "https://www.zhihu.com/question/" + tid;
				break;
			case "topic":
				url = "https://www.zhihu.com/topic/" + tid + "/hot";
				break;
			case "column":
				ZColumn column = columnRepository.findOne(tid);
				description = column.getIntro();
				url = "https://zhuanlan.zhihu.com/" + tid;
				break;
			case "collection":
				url = "https://www.zhihu.com/collection/" + tid;
				break;
			case "roundtable":
				RoundTable roundTable = roundTableRepository.findOne(tid);
				description = roundTable.getDescription();
				url = "https://www.zhihu.com//" + tid;
				break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		targetAggreInfo.setUrl(url);
	}

}
