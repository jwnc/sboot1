
package com.wnc.wynews.jpa.repo;

import com.wnc.wynews.jpa.entity.WyNewsKeyword;
import org.springframework.data.repository.CrudRepository;

public interface WyNewsKeyWordRepository
        extends CrudRepository<WyNewsKeyword, String>
{
}