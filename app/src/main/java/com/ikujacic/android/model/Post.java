package com.ikujacic.android.model;

public class Post {

    private Integer id;
    private String title;
    private String text;
    private String author;
    private String communityName;
    private Integer karma;
    private String flair;

    public Post() {}

    public Post(Integer id, String title, String text, String author, String communityName, Integer karma, String flair) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.author = author;
        this.communityName = communityName;
        this.karma = karma;
        this.flair = flair;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public Integer getKarma() {
        return karma;
    }

    public void setKarma(Integer karma) {
        this.karma = karma;
    }

    public String getFlair() {
        return flair;
    }

    public void setFlair(String flair) {
        this.flair = flair;
    }
}