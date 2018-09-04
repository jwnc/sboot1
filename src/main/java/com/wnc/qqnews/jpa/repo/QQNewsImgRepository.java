
package com.wnc.qqnews.jpa.repo;

import com.wnc.qqnews.jpa.entity.QqNewsImg;
import com.wnc.qqnews.jpa.entity.QqUser;
import org.springframework.data.repository.CrudRepository;

public interface QQNewsImgRepository
        extends CrudRepository<QqNewsImg, String>
{
}