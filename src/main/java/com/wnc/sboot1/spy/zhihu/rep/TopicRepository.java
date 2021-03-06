
package com.wnc.sboot1.spy.zhihu.rep;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.wnc.sboot1.spy.zhihu.active.target.Topic;

public interface TopicRepository
        extends PagingAndSortingRepository<Topic, String>,
        JpaSpecificationExecutor<Topic>
{

}