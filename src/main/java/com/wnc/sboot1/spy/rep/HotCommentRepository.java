
package com.wnc.sboot1.spy.rep;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.wnc.sboot1.spy.zuqiu.HotComment;

public interface HotCommentRepository
        extends PagingAndSortingRepository<HotComment, Integer>,
        JpaSpecificationExecutor<HotComment>
{

}