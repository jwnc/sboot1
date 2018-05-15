
package com.wnc.sboot1.spy.zuqiu.rep;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.wnc.sboot1.spy.zuqiu.HotComment;

public interface HotCommentRepository
        extends PagingAndSortingRepository<HotComment, Integer>,
        JpaSpecificationExecutor<HotComment>
{
    @Transactional
    @Modifying
    @Query( value = "UPDATE HotComment hc SET hc.favorite=1 WHERE hc.id=:id" )
    void favorite( @Param( "id" ) Integer id );
}