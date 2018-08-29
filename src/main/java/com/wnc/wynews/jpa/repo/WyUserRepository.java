
package com.wnc.wynews.jpa.repo;

import com.wnc.wynews.jpa.entity.WyUser;
import org.springframework.data.repository.CrudRepository;

public interface WyUserRepository
        extends CrudRepository<WyUser, Integer>
{
}