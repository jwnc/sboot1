
package com.wnc.sboot1.spy.zhihu.rep;


import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.wnc.sboot1.spy.zhihu.active.ActivityCronLog;


public interface ActivityCronLogRepository extends PagingAndSortingRepository<ActivityCronLog, Integer>, JpaSpecificationExecutor<ActivityCronLog>
{

}