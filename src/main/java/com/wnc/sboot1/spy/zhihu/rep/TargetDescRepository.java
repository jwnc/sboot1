
package com.wnc.sboot1.spy.zhihu.rep;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.wnc.sboot1.spy.zhihu.active.aggre.TargetDesc;

public interface TargetDescRepository
        extends PagingAndSortingRepository<TargetDesc, String>,
        JpaSpecificationExecutor<TargetDesc>
{

}