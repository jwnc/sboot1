
package com.wnc.wynews.model;

public class Comment
{
    private boolean anonymous;
    private String buildLevel;
    private String content;
    private String createTime;
    private String ip;
    private String isDel;
    private String postId;
    private String productKey;
    private String siteName;
    private String unionState;

    private Integer against;
    private Integer commentId;
    private Integer favCount;
    private Integer shareCount;
    private Integer vote;

    private Integer userId;

    private User user;

    public boolean isAnonymous()
    {
        return anonymous;
    }

    public void setAnonymous( boolean anonymous )
    {
        this.anonymous = anonymous;
    }

    public String getBuildLevel()
    {
        return buildLevel;
    }

    public void setBuildLevel( String buildLevel )
    {
        this.buildLevel = buildLevel;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent( String content )
    {
        this.content = content;
    }

    public String getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime( String createTime )
    {
        this.createTime = createTime;
    }

    public String getIp()
    {
        return ip;
    }

    public void setIp( String ip )
    {
        this.ip = ip;
    }

    public String getIsDel()
    {
        return isDel;
    }

    public void setIsDel( String isDel )
    {
        this.isDel = isDel;
    }

    public String getPostId()
    {
        return postId;
    }

    public void setPostId( String postId )
    {
        this.postId = postId;
    }

    public String getProductKey()
    {
        return productKey;
    }

    public void setProductKey( String productKey )
    {
        this.productKey = productKey;
    }

    public String getSiteName()
    {
        return siteName;
    }

    public void setSiteName( String siteName )
    {
        this.siteName = siteName;
    }

    public String getUnionState()
    {
        return unionState;
    }

    public void setUnionState( String unionState )
    {
        this.unionState = unionState;
    }

    public Integer getAgainst()
    {
        return against;
    }

    public void setAgainst( Integer against )
    {
        this.against = against;
    }

    public Integer getCommentId()
    {
        return commentId;
    }

    public void setCommentId( Integer commentId )
    {
        this.commentId = commentId;
    }

    public Integer getFavCount()
    {
        return favCount;
    }

    public void setFavCount( Integer favCount )
    {
        this.favCount = favCount;
    }

    public Integer getShareCount()
    {
        return shareCount;
    }

    public void setShareCount( Integer shareCount )
    {
        this.shareCount = shareCount;
    }

    public Integer getVote()
    {
        return vote;
    }

    public void setVote( Integer vote )
    {
        this.vote = vote;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser( User user )
    {
        this.user = user;
    }

    public Integer getUserId()
    {
        return userId;
    }

    public void setUserId( Integer userId )
    {
        this.userId = userId;
    }
}
