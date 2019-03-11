
package com.wnc.sboot1.readlog;

import java.util.Date;

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
@Entity(name="itbook_log")
public class ReadLog
{
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    @Id
    private Long id;
    
    @Column( length = 4000 )
    private String content;
    
    private Long dictId;
    
    @Column( length = 32 )
    private String device;
    
    private Date logTime;
    
    private Integer type;

    private Integer deleted;
}
