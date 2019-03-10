
package com.wnc.wynews.jpa.repo;

import com.wnc.wynews.jpa.entity.WyIncentiveInfo;
import org.springframework.data.repository.CrudRepository;

public interface IncentiveInfoRepository
        extends CrudRepository<WyIncentiveInfo, String>
{
}