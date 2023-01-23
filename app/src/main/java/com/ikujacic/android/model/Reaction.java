package com.ikujacic.android.model;

public class Reaction {

    private String type;
    private Integer postId;
    private Integer commentId;
    private String user;

    public Reaction() {
    }

    public Reaction(String type, Integer postId, Integer commentId, String user) {
        this.type = type;
        this.postId = postId;
        this.commentId = commentId;
        this.user = user;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}