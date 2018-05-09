
package com.wnc.sboot1.spy.zhihu.rep;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.wnc.sboot1.spy.zhihu.active.aggre.TargetAggreInfo;
import com.wnc.sboot1.spy.zhihu.active.aggre.TargetAggreKey;

public interface TargetAggreInfoRepository
        extends PagingAndSortingRepository<TargetAggreInfo, TargetAggreKey>,
        JpaSpecificationExecutor<TargetAggreInfo>
{

}