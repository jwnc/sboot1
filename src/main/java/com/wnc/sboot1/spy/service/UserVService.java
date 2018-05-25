
package com.wnc.sboot1.spy.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wnc.basic.BasicDateUtil;
import com.wnc.basic.BasicNumberUtil;
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
        String sql = "select user_token, last_spy_time from user_v where status = 0 order by followers desc";
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

    public List<UserV> getRetryUserVList()
    {
        List<UserV> list = new ArrayList<>();
        String sql = "select v.user_token, er.url, v.last_spy_time from user_v  v, zh_task_errlog er where v.`status` = 0 and v.last_spy_time < '2018-05' and v.user_token  = er.u_token";
        Query createNativeQuery = entityManager.createNativeQuery( sql );
        List<Object[]> resultList = createNativeQuery.getResultList();
        UserV v = null;
        for ( Object[] object : resultList )
        {
            v = new UserV( String.valueOf( object[0] ),
                    String.valueOf( object[1] ), (Date)object[2] );
            list.add( v );
        }
        return list;
    }

    public List<UserV> getContinueUserVList()
    {
        List<UserV> list = new ArrayList<>();
        String sql = "select v.user_token, act.mintime, v.last_spy_time from user_v v left join (select actor_id, min(created_time) mintime from zh_activity group by actor_id) act on v.hash_id = act.actor_id where v.`status`=0";
        Query createNativeQuery = entityManager.createNativeQuery( sql );
        List<Object[]> resultList = createNativeQuery.getResultList();
        UserV v = null;
        String url = null;
        String uToken = null;
        long defaultAfterId = new Date().getTime() / 1000;
        long afterId = defaultAfterId;
        for ( Object[] object : resultList )
        {
            afterId = object[1] == null ? defaultAfterId
                    : BasicNumberUtil
                            .getLongNumber( String.valueOf( object[1] ) );
            uToken = String.valueOf( object[0] );
            url = "https://www.zhihu.com/api/v4/members/" + uToken
                    + "/activities?limit=7&after_id=" + afterId
                    + "&desktop=True";
            v = new UserV( uToken, url, (Date)object[2] );
            list.add( v );
        }
        return list;
    }

    public List<UserV> getLastTaskRetryUserVList( Date lastTaskTime )
    {
        List<UserV> list = new ArrayList<>();
        String sql = "select v.user_token, v.last_spy_time, er.url from user_v v, zh_task_errlog er where er.u_token = v.user_token and er.task_begin_date='"
                + BasicDateUtil.getDateTimeString( lastTaskTime ) + "'";
        Query createNativeQuery = entityManager.createNativeQuery( sql );
        List<Object[]> resultList = createNativeQuery.getResultList();
        UserV v = null;
        String url = null;
        String uToken = null;
        for ( Object[] object : resultList )
        {
            uToken = String.valueOf( object[0] );
            url = String.valueOf( object[2] );
            v = new UserV( uToken, url, (Date)object[1] );
            list.add( v );
        }
        return list;
    }

    public void updateSpyTime( String userToken, Date beginSpyDate )
    {
        userVRepository.updateSpyTime( userToken, beginSpyDate );
    }
}
