
package com.wnc.wynews.jpa.repo;

import com.wnc.wynews.jpa.entity.WyNews;
import org.springframework.data.repository.CrudRepository;

public interface WyNewsRepository
        extends CrudRepository<WyNews, String>
{
}