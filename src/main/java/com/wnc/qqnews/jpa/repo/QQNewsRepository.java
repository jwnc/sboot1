
package com.wnc.qqnews.jpa.repo;

import com.wnc.qqnews.jpa.entity.QqNews;
import com.wnc.qqnews.jpa.entity.QqUser;
import org.springframework.data.repository.CrudRepository;

public interface QQNewsRepository
        extends CrudRepository<QqNews, String>
{
}