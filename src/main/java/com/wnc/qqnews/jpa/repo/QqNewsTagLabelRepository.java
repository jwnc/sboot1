
package com.wnc.qqnews.jpa.repo;

import com.wnc.qqnews.jpa.entity.QqCertInfo;
import com.wnc.qqnews.jpa.entity.QqNewsTagLabel;
import org.springframework.data.repository.CrudRepository;

public interface QqNewsTagLabelRepository
        extends CrudRepository<QqNewsTagLabel, String>
{
}