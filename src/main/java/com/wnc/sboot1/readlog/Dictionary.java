
package com.wnc.sboot1.readlog;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity(name="dictionary")
public class Dictionary
{
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    @Id
    private Long id;
    private Long topicId;
    
    @Column( length = 128 )
    private String topic_word;
    
    @Column( length = 1024 )
    private String meanCn;
    
    @Column( length = 128 )
    private String accent;
    
    @Column( length = 128 )
    private String orginal;

    private Integer weight;
}
