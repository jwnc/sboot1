
package com.wnc.sboot1.readlog;


import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface DictionaryRepository
        extends PagingAndSortingRepository<Dictionary, Long>,
        JpaSpecificationExecutor<Dictionary>
{
}