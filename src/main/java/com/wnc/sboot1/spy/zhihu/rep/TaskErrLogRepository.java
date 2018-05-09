
package com.wnc.sboot1.spy.zhihu.rep;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.wnc.sboot1.spy.zhihu.active.TaskErrLog;

public interface TaskErrLogRepository
        extends PagingAndSortingRepository<TaskErrLog, Integer>,
        JpaSpecificationExecutor<TaskErrLog>
{

}