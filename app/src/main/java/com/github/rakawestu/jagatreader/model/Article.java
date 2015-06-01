package com.github.rakawestu.jagatreader.model;

/**
 * @author rakawm
 */
public class Article {

    private int id;
    private String title;
    private String description;
    private String dateTime;
    private String imageUrl;

    public Article(String title, String description, String dateTime, String imageUrl){
        this.title = title;
        this.description = description;
        this.dateTime = dateTime;
        this.imageUrl = imageUrl;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
