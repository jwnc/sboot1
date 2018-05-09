
package com.wnc.sboot1.spy.zhihu.rep;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.wnc.sboot1.spy.zhihu.active.target.Collection;

public interface CollectionRepository
        extends PagingAndSortingRepository<Collection, String>,
        JpaSpecificationExecutor<Collection>
{

}