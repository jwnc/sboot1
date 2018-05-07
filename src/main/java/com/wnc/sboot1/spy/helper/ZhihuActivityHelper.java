
package com.wnc.sboot1.spy.helper;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wnc.sboot1.spy.zhihu.active.Activity;
import com.wnc.sboot1.spy.zhihu.active.TaskErrLog;
import com.wnc.sboot1.spy.zhihu.active.target.Answer;
import com.wnc.sboot1.spy.zhihu.active.target.Article;
import com.wnc.sboot1.spy.zhihu.active.target.Collection;
import com.wnc.sboot1.spy.zhihu.active.target.Question;
import com.wnc.sboot1.spy.zhihu.active.target.RoundTable;
import com.wnc.sboot1.spy.zhihu.active.target.Target;
import com.wnc.sboot1.spy.zhihu.active.target.Topic;
import com.wnc.sboot1.spy.zhihu.active.target.ZColumn;
import com.wnc.sboot1.spy.zhihu.rep.ActivityRepository;
import com.wnc.sboot1.spy.zhihu.rep.AnswerRepository;
import com.wnc.sboot1.spy.zhihu.rep.ArticleRepository;
import com.wnc.sboot1.spy.zhihu.rep.CollectionRepository;
import com.wnc.sboot1.spy.zhihu.rep.ColumnRepository;
import com.wnc.sboot1.spy.zhihu.rep.QuestionRepository;
import com.wnc.sboot1.spy.zhihu.rep.RoundTableRepository;
import com.wnc.sboot1.spy.zhihu.rep.TaskErrLogRepository;
import com.wnc.sboot1.spy.zhihu.rep.TopicRepository;

@Component
public class ZhihuActivityHelper {
	private static Logger logger = Logger.getLogger(ZhihuActivityHelper.class);
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
	ActivityRepository actRepository;
	@Autowired
	TaskErrLogRepository taskErrLogRepository;

	public void save(List<Activity> activityList) {
		Target entity = new Target();
		for (Activity activity : activityList) {
			try {
				activity.convertTargetAndId();
				actRepository.save(activity);
				entity = activity.getEntity();

				if (entity instanceof Answer) {
					answerRepository.save((Answer) entity);
				} else if (entity instanceof Article) {
					articleRepository.save((Article) entity);
				} else if (entity instanceof Collection) {
					collectionRepository.save((Collection) entity);
				} else if (entity instanceof ZColumn) {
					columnRepository.save((ZColumn) entity);
				} else if (entity instanceof Question) {
					questionRepository.save((Question) entity);
				} else if (entity instanceof RoundTable) {
					roundTableRepository.save((RoundTable) entity);
				} else if (entity instanceof Topic) {
					topicRepository.save((Topic) entity);
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(activity.getAction().getAction_text() + " " + entity.getUrl() + e.toString());
			}
		}
	}

	public void errLog(TaskErrLog taskErrLog) {
		try {
			taskErrLogRepository.save(taskErrLog);
		} catch (Exception e) {
			logger.error(taskErrLog.getUrl() + " " + e.toString());
			e.printStackTrace();
		}
	}

}
