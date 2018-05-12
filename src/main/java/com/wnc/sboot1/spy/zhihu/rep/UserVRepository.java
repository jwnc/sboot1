
package com.wnc.sboot1.spy.zhihu.rep;

import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.wnc.sboot1.spy.zhihu.active.UserV;

public interface UserVRepository
        extends PagingAndSortingRepository<UserV, Integer>,
        JpaSpecificationExecutor<UserV>
{
    @Transactional
    @Modifying
    @Query( value = "UPDATE UserV v SET v.lastSpyTime=:lastSpyTime WHERE v.userToken=:userToken" )
    void updateSpyTime( @Param( "userToken" ) String userToken,
            @Param( "lastSpyTime" ) Date lastSpyTime );
}