
package com.wnc.sboot1.jpa;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSONObject;
import com.wnc.sboot1.spy.zuqiu.HotComment;
import com.wnc.sboot1.spy.zuqiu.Zb8News;
import com.wnc.sboot1.spy.zuqiu.rep.HotCommentRepository;

@RunWith( SpringRunner.class )
@SpringBootTest
public class Zb8NewsRepositoryTests2
{

    @Autowired
    private HotCommentRepository hotCommentRepository;

    @Test
    public void test2() throws Exception
    {
        Sort sort = new Sort( Sort.Direction.DESC, "updateDate" ); // 创建时间降序排序
        // sort = new Sort( Sort.Direction.DESC, "zb8News.createtime" ); //
        // 创建时间降序排序
        Pageable pageable = new PageRequest( 1, 10, sort );
        Page<HotComment> findAll = this.hotCommentRepository
                .findAll( pageable );
        System.out.println( findAll.getContent().size() );
        System.out.println( JSONObject.toJSONString( findAll ) );
        // System.out.println( hotCommentRepository.findOne( 98802363 )
        // .getZb8News().getTitle() );
    }

    @Test
    public void test3()
    {
        System.out.println( JSONObject.toJSONString( findAllByPingYin(
                "2018_04_11-news-zuqiu-5acd49a6ddce3", 1, 10 ) ) );
    }

    public Page<HotComment> findAllByPingYin( final String name, int page,
            int count )
    {
        Specification<HotComment> specification = new Specification<HotComment>()
        {
            public Predicate toPredicate( Root<HotComment> root,
                    CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder )
            {
                Path<String> _name = root.get( "filename" );
                Predicate _key = criteriaBuilder.equal( _name, name );
                return criteriaBuilder.and( _key );
            }
        };
        Sort sort = new Sort( Direction.DESC, "up" );
        Pageable pageable = new PageRequest( page - 1, count, sort );
        return hotCommentRepository.findAll( specification, pageable );
    }

    @Test
    public void d()
    {
        int newsOrder = 1;
        final String day = "2018-10-08";
        final String type = "nba";
        Specification<HotComment> specification = new Specification<HotComment>()
        {
            public Predicate toPredicate( Root<HotComment> root,
                    CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder )
            {
                // 左连接查询
                Join<HotComment, Zb8News> join = root.join( "zb8News",
                        JoinType.LEFT );

                Predicate _key = criteriaBuilder.like(
                        join.get( "createtime" ).as( String.class ),
                        day + "%" );

                Predicate _key2 = criteriaBuilder
                        .equal( join.get( "type" ).as( String.class ), type );

                query.where( _key, _key2 );

                return null;
            }
        };
        String sortField = "createtime";
        if ( newsOrder == 1 )
        {
            sortField = "zb8News.createtime";
        }
        Sort sort = new Sort( Sort.Direction.DESC, sortField ); // 新闻按创建时间降序排序
        sort = sort.and( new Sort( Sort.Direction.DESC, "up" ) );// 同一新闻按up降序
        Pageable pageable = new PageRequest( 0, 10, sort );
        Page<HotComment> findAll = this.hotCommentRepository
                .findAll( specification, pageable );
        System.out.println( findAll );
    }

}