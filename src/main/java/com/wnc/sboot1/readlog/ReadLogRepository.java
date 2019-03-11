
package com.wnc.sboot1.readlog;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ReadLogRepository
        extends PagingAndSortingRepository<ReadLog, Long>,
        JpaSpecificationExecutor<ReadLog>
{
}