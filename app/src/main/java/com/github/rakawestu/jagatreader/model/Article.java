package com.github.rakawestu.jagatreader.model;

import com.github.rakawestu.jagatreader.utils.TimeUtil;

import java.io.Serializable;

/**
 * @author rakawm
 */
public class Article implements Serializable{

    private int id;
    private String title;
    private String description;
    private String dateTime;
    private String imageUrl;
    private String content;
    private String creator;
    private String url;

    public Article() {

    }

    public Article(String title,
                   String description,
                   String dateTime,
                   String imageUrl,
                   String content,
                   String creator,
                   String url) {
        setTitle(title);
        setDescription(description);
        setDateTime(dateTime);
        setImageUrl(imageUrl);
        setContent(content);
        setCreator(creator);
        setUrl(url);
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

    public String getFormattedDateTime() {
        return TimeUtil.getFormattedDate(TimeUtil.fromFeed(dateTime));
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
