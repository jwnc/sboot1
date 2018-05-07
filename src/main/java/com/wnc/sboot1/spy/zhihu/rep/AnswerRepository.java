
package com.wnc.sboot1.spy.zhihu.rep;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.wnc.sboot1.spy.zhihu.active.target.Answer;

public interface AnswerRepository
        extends PagingAndSortingRepository<Answer, Integer>,
        JpaSpecificationExecutor<Answer>
{

}