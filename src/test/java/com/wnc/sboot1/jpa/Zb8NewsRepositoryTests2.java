
package com.wnc.sboot1.jpa;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
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
import com.wnc.sboot1.spy.rep.HotCommentRepository;
import com.wnc.sboot1.spy.zuqiu.HotComment;

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

    // @Test

}