
package com.wnc.sboot1.spy.zhihu.active;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.wnc.sboot1.spy.zhihu.active.target.Answer;
import com.wnc.sboot1.spy.zhihu.active.target.Article;
import com.wnc.sboot1.spy.zhihu.active.target.Collection;
import com.wnc.sboot1.spy.zhihu.active.target.Question;
import com.wnc.sboot1.spy.zhihu.active.target.RoundTable;
import com.wnc.sboot1.spy.zhihu.active.target.Target;
import com.wnc.sboot1.spy.zhihu.active.target.Topic;
import com.wnc.sboot1.spy.zhihu.active.target.ZColumn;

@Entity
@Table( name = "ZH_ACTIVITY" )
public class Activity
{
    @Transient
    private static Logger logger = Logger.getLogger( Activity.class );
    @Id
    @GeneratedValue
    private Long id;

    @Transient
    private Actor actor;
    private String actor_id;
    @Transient
    private Action action;
    private Integer action_id;
    @Transient
    private String verb;
    @Transient
    private String action_text;

    private Long created_time;
    private String type;

    @Transient
    private JSONObject target;
    /**
     * 最终的实体
     */
    @Transient
    private Target entity;
    private String target_id;

    public void convertTargetAndId()
    {
        String verb = getVerb();
        switch ( verb )
        {
            case "MEMBER_CREATE_ARTICLE":
                action( 1 );
            case "MEMBER_VOTEUP_ARTICLE":
                action( 2 );
                entity = target.toJavaObject( Article.class );
                Article article = (Article)entity;
                article.setActor_id( article.getAuthor().getId() );
                article.setColumn_id( article.getColumn().getId() );
                article.setInfo( article.getTitle() );
                break;
            case "ANSWER_VOTE_UP":
                action( 3 );
            case "ANSWER_CREATE":
                action( 4 );
                entity = target.toJavaObject( Answer.class );
                Answer answer = (Answer)entity;
                answer.setActor_id( answer.getAuthor().getId() );
                answer.setQuestion_id( answer.getQuestion().getId() );
                entity.setInfo( answer.getExcerpt_new() );
                break;
            case "QUESTION_CREATE":
                action( 5 );
            case "QUESTION_FOLLOW":
                action( 6 );
                entity = target.toJavaObject( Question.class );
                Question question = (Question)entity;
                question.setActor_id( question.getAuthor().getId() );
                entity.setInfo( question.getTitle() );
                break;
            case "MEMBER_FOLLOW_COLUMN":
                action( 7 );
                entity = target.toJavaObject( ZColumn.class );
                ZColumn zColumn = (ZColumn)entity;
                zColumn.setActor_id( zColumn.getAuthor().getId() );
                entity.setInfo( zColumn.getTitle() );
                break;
            case "TOPIC_FOLLOW":
                action( 8 );
                entity = target.toJavaObject( Topic.class );
                entity.setInfo( ((Topic)entity).getName() );
                break;
            case "MEMBER_FOLLOW_COLLECTION":
                action( 9 );
                entity = target.toJavaObject( Collection.class );
                entity.setInfo( ((Collection)entity).getTitle() );
                break;
            case "MEMBER_FOLLOW_ROUNDTABLE":
                action( 10 );
                entity = target.toJavaObject( RoundTable.class );
                entity.setInfo( ((RoundTable)entity).getName() );
                break;
            default:
                // log err
                logger.error( this.getAction_id() + "行为不支持:" + verb );
                return;
        }

        setActor_id( getActor().getId() );
        setTarget_id( getEntity().getId() );
    }

    private void action( int i )
    {
        if ( getAction_id() == null )
        {
            setAction_id( i );
        }
    }

    public String pingInsertSql()
    {
        String sql = "";
        return sql;
    }

    public Actor getActor()
    {
        return actor;
    }

    public void setActor( Actor actor )
    {
        this.actor = actor;
    }

    public Long getCreated_time()
    {
        return created_time;
    }

    public void setCreated_time( Long created_time )
    {
        this.created_time = created_time;
    }

    public Long getId()
    {
        return id;
    }

    public void setId( Long id )
    {
        this.id = id;
    }

    public String getType()
    {
        return type;
    }

    public void setType( String type )
    {
        this.type = type;
    }

    public JSONObject getTarget()
    {
        return target;
    }

    public void setTarget( JSONObject target )
    {
        this.target = target;
    }

    public Target getEntity()
    {
        return entity;
    }

    public void setEntity( Target entity )
    {
        this.entity = entity;
    }

    public String getActor_id()
    {
        return actor_id;
    }

    public void setActor_id( String actor_id )
    {
        this.actor_id = actor_id;
    }

    public String getTarget_id()
    {
        return target_id;
    }

    public void setTarget_id( String target_id )
    {
        this.target_id = target_id;
    }

    public Action getAction()
    {
        return action;
    }

    public void setAction( Action action )
    {
        this.action = action;
    }

    public Integer getAction_id()
    {
        return action_id;
    }

    public void setAction_id( Integer action_id )
    {
        this.action_id = action_id;
    }

    public String getVerb()
    {
        return verb;
    }

    public void setVerb( String verb )
    {
        this.verb = verb;
    }

    public String getAction_text()
    {
        return action_text;
    }

    public void setAction_text( String action_text )
    {
        this.action_text = action_text;
    }

}
