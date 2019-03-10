
package com.wnc.wynews.jpa.repo;

import com.wnc.wynews.jpa.entity.WyRedNameInfo;
import org.springframework.data.repository.CrudRepository;

public interface RedNameInfoRepository
        extends CrudRepository<WyRedNameInfo, String>
{
}