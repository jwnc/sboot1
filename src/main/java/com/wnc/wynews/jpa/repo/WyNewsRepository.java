
package com.wnc.wynews.jpa.repo;

import com.wnc.wynews.jpa.entity.WyNews;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface WyNewsRepository
        extends CrudRepository<WyNews, String> {

    @Modifying
    @Query(value = "UPDATE WyNews t SET t.cmtCount = :cmtCount WHERE t.code= :code")
    void updateCmtCount(@Param("cmtCount") int cmtCount, @Param("code") String code);
}