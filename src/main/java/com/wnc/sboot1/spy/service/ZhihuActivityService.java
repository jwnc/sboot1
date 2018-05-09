
package com.wnc.sboot1.spy.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.http.client.methods.HttpGet;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.crawl.core.util.HttpClientUtil;
import com.wnc.basic.BasicNumberUtil;
import com.wnc.sboot1.spy.util.SpiderUtils;
import com.wnc.sboot1.spy.zhihu.TT2;
import com.wnc.sboot1.spy.zhihu.active.aggre.TargetAggreInfo;
import com.wnc.sboot1.spy.zhihu.active.aggre.TargetAggreKey;
import com.wnc.sboot1.spy.zhihu.active.aggre.TargetDesc;
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
import com.wnc.sboot1.spy.zhihu.rep.TargetDescRepository;
import com.wnc.sboot1.spy.zhihu.rep.TopicRepository;

@Component
public class ZhihuActivityService {
	private static Logger logger = Logger.getLogger(ZhihuActivityService.class);
	public static final int FOLLOW_COUNT = 20;
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
	@Autowired
	TargetDescRepository targetDescRepository;

	@PersistenceContext
	private EntityManager entityManager;

	public void aggreYesterday() {
		String day1 = SpiderUtils.getYesterDayStr();
		String day2 = SpiderUtils.getDayWithLine();
		aggre(day1, day2, 1);
	}

	public void aggre(final String day1, final String day2, int aggreCode) {
		String sql = "select act.cnt,tar.id tid,tar.info title,tar.type from (SELECT target_id, count(*) cnt FROM zh_activity WHERE FROM_UNIXTIME(created_time) >= '"
				+ day1 + " 00:00;00' and FROM_UNIXTIME(created_time) < '" + day2
				+ " 00:00:00' group by target_id having count(*) > " + FOLLOW_COUNT
				+ ") act, zh_target tar where act.target_id=tar.id";
		Query createNativeQuery = entityManager.createNativeQuery(sql);
		List resultList = createNativeQuery.getResultList();
		for (Object object : resultList) {
			Object[] arr = (Object[]) object;
			TargetAggreInfo targetAggreInfo = new TargetAggreInfo();
			TargetDesc targetDesc = new TargetDesc();
			targetDesc.setTid(String.valueOf(arr[1]));
			targetDesc.setTitle(String.valueOf(arr[2]));
			targetDesc.setType(String.valueOf(arr[3]));

			TargetAggreKey key = new TargetAggreKey();
			key.setAggreCode(aggreCode);
			// 日期
			key.setDateStr(day1);
			key.setTid(targetDesc.getTid());

			targetAggreInfo.setId(key);
			targetAggreInfo.setCnt(BasicNumberUtil.getNumber(String.valueOf(arr[0])));
			targetAggreInfo.setTargetDesc(targetDesc);
			// targetAggreInfo.setAggreCode( 1 );
			// targetAggreInfo.setDateStr( "20180507" );
			// targetAggreInfo.setTid( targetDesc.getTid() );

			find(targetDesc);
			try {
				targetDescRepository.save(targetDesc);
				targetAggreInfoRepository.save(targetAggreInfo);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e);
			}
		}
	}

	private void find(TargetDesc targetAggreInfo) {
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
					HttpGet request = new HttpGet("https://api.zhihu.com/questions/" + question_id);
					request.setHeader("authorization", "oauth " + TT2.initAuthorization());
					String webPage = HttpClientUtil.getWebPage(request);
					title = JSONObject.parseObject(webPage).getString("title");
				} else {
					title = findOne2.getTitle();
				}

				description = findOne.getExcerpt_new();
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
		targetAggreInfo.setDescription(description);
	}
}
