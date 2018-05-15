
package com.wnc.sboot1.spy.zuqiu.rep;

import org.springframework.data.repository.CrudRepository;

import com.wnc.sboot1.spy.zuqiu.Zb8News;

public interface Zb8NewsRepository extends CrudRepository<Zb8News, String>
{
    void deleteByUrl( String url );

}