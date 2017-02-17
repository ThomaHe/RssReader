package com.example.thenry.rssnews;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by thenry on 16/02/2017.
 */

public class Article extends RealmObject{

    @PrimaryKey
    public int id;
    public String title;
    public String date;
    public String imgLink;
    public String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImgLink() {

        return imgLink;
    }

    public void setImgLink(String imgLink) {
        this.imgLink = imgLink;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
