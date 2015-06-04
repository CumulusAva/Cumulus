package com.github.cumulusava.android.cumulus.database;

/**
 * Created by Magnus on 2015-05-02
 * Model for holding an title to save in database or show in list
 */
public class Article {
    private final long id; //Id for the database
    private final String title; //Name of the title
    private final String link; //Web-link to article

    public Article(long id, String title, String link){
        this.id = id;
        this.title = title;
        this.link = link;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }
}