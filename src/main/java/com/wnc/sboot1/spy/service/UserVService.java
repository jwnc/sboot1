
package com.wnc.sboot1.spy.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wnc.sboot1.spy.zhihu.active.UserV;
import com.wnc.sboot1.spy.zhihu.rep.UserVRepository;

@Component
public class UserVService
{
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UserVRepository userVRepository;

    public List<UserV> getUserVList()
    {
        List<UserV> list = new ArrayList<>();
        String sql = "select user_token, last_spy_time from user_v where status = 0 and hash_id='ba13e8f380592c123d3218d8f25529fa' order by followers desc";
        Query createNativeQuery = entityManager.createNativeQuery( sql );
        List<Object[]> resultList = createNativeQuery.getResultList();
        UserV v = null;
        for ( Object[] object : resultList )
        {
            v = new UserV( String.valueOf( object[0] ), (Date)object[1] );
            list.add( v );
        }
        return list;
    }

    public void updateSpyTime( String userToken, Date beginSpyDate )
    {
        userVRepository.updateSpyTime( userToken, beginSpyDate );
    }
}
