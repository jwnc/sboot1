
package com.wnc.qqnews.jpa.repo;

import com.wnc.qqnews.jpa.entity.QqNewsKeyWord;
import org.springframework.data.repository.CrudRepository;

public interface QqNewsKeywordRepository
        extends CrudRepository<QqNewsKeyWord, String>
{
}