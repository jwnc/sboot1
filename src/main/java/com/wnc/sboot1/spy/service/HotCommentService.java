
package com.wnc.sboot1.spy.service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.wnc.sboot1.spy.zuqiu.HotComment;
import com.wnc.sboot1.spy.zuqiu.Zb8News;
import com.wnc.sboot1.spy.zuqiu.rep.HotCommentRepository;

@Component
public class HotCommentService
{
    @Autowired
    private HotCommentRepository hotCommentRepository;

    public void favorite( Integer id ) throws Exception
    {
        this.hotCommentRepository.favorite( id );
    }

    public Page<HotComment> paginationFav( int page, int size,
            final String day ) throws Exception
    {
        Specification<HotComment> specification = new Specification<HotComment>()
        {
            public Predicate toPredicate( Root<HotComment> root,
                    CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder )
            {
                Path<String> _name = root.get( "createtime" );

                Path<String> favorite = root.get( "favorite" );
                Predicate equal = criteriaBuilder.equal( favorite, 1 );
                if ( day != null )
                {
                    Predicate timeLike = criteriaBuilder.like( _name,
                            day + "%" );
                    return criteriaBuilder.and( equal, timeLike );
                }
                return criteriaBuilder.and( equal );
            }
        };
        String sortField = "zb8News.createtime";
        Sort sort = new Sort( Sort.Direction.DESC, sortField ); // 新闻按创建时间降序排序
        sort = sort.and( new Sort( Sort.Direction.DESC, "up" ) );// 同一新闻按up降序
        Pageable pageable = new PageRequest( page - 1, size, sort );
        return this.hotCommentRepository.findAll( specification, pageable );
    }

    public Page<HotComment> paginationByDay( int page, int size,
            final String day, final String type, int newsOrder )
            throws Exception
    {
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

                if ( StringUtils.isNoneBlank( type ) )
                {
                    Predicate _key2 = criteriaBuilder.equal(
                            join.get( "type" ).as( String.class ), type );

                    query.where( _key, _key2 );
                } else
                {
                    query.where( _key );
                }

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
        Pageable pageable = new PageRequest( page - 1, size, sort );
        return this.hotCommentRepository.findAll( specification, pageable );
    }

    public Page<HotComment> paginationByNid( int page, int size,
            final String newsId )
    {
        Specification<HotComment> specification = new Specification<HotComment>()
        {
            public Predicate toPredicate( Root<HotComment> root,
                    CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder )
            {
                Path<String> _name = root.get( "filename" );
                Predicate _key = criteriaBuilder.equal( _name, newsId );
                return criteriaBuilder.and( _key );
            }
        };
        Sort sort = new Sort( Direction.DESC, "up" );
        Pageable pageable = new PageRequest( page - 1, size, sort );
        return hotCommentRepository.findAll( specification, pageable );
    }
}
