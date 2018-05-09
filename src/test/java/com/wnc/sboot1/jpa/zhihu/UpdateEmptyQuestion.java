
package com.wnc.sboot1.jpa.zhihu;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.wnc.sboot1.spy.zhihu.active.target.Question;
import com.wnc.sboot1.spy.zhihu.active.target.Target;
import com.wnc.sboot1.spy.zhihu.rep.QuestionRepository;
import com.wnc.sboot1.spy.zhihu.rep.TargetRepository;
import com.wnc.string.PatternUtil;
import com.wnc.tools.FileOp;

/**
 * 更新target表中存在空info的情况 select * from zh_target t where t.info is null;
 * 
 * @author wnc
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UpdateEmptyQuestion {
	@Autowired
	private QuestionRepository questionRepository;

	@Autowired
	private TargetRepository targetRepository;

	@Test
	public void a() {
		String question_id = "";
		try {
			for (String s : FileOp.readFrom("c:/emptyq.txt")) {
				question_id = PatternUtil.getFirstPattern(s, "\\d+");
				Question question = questionRepository.findOne(question_id);
				System.out.println(question.getTitle());
				Target target = targetRepository.findOne(question_id);
				target.setInfo(question.getTitle());
				targetRepository.save(target);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
