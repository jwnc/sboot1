
package com.wnc.sboot1.spy.zhihu.rep;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.wnc.sboot1.spy.zhihu.active.Activity;
import com.wnc.sboot1.spy.zhihu.active.ActivityKey;

public interface ActivityRepository
        extends PagingAndSortingRepository<Activity, ActivityKey>,
        JpaSpecificationExecutor<Activity>
{

}