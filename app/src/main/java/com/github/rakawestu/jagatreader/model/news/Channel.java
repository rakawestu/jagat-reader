package com.github.rakawestu.jagatreader.model.news;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.NamespaceList;

import java.util.List;

/**
 * Created by raka on 02/07/2015.
 */
@NamespaceList({
        @Namespace(reference = "http://www.w3.org/2005/Atom", prefix = "atom"),
        @Namespace(reference = "http://purl.org/rss/1.0/modules/syndication/", prefix = "sy"),
        @Namespace(reference = "http://purl.org/dc/elements/1.1/", prefix = "dc"),
        @Namespace(reference = "http://purl.org/rss/1.0/modules/slash/", prefix = "slash"),
        @Namespace(reference = "http://purl.org/rss/1.0/modules/content/", prefix = "content"),
        @Namespace(reference = "http://wellformedweb.org/CommentAPI/", prefix = "wfw"),

})
public class Channel {
    @Element(name = "title")
    private String title;
    @Element(name = "description")
    private String description;
    @Element(name = "lastBuildDate")
    private String lastBuildDate;
    @ElementList(entry = "link", inline = true, required = false)
    public List<Atom> links;
    @ElementList(inline = true, name = "item")
    private List<Item> item;
    @Element(name = "generator")
    private String generator;
    @Element(name = "language")
    private String language;
    @Element(name = "updatePeriod")
    private String updatePeriod;
    @Element(name = "updateFrequency")
    private int updateFrequency;

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

    public String getLastBuildDate() {
        return lastBuildDate;
    }

    public void setLastBuildDate(String lastBuildDate) {
        this.lastBuildDate = lastBuildDate;
    }

    public List<Item> getItem() {
        return item;
    }

    public void setItem(List<Item> item) {
        this.item = item;
    }

    public String getGenerator() {
        return generator;
    }

    public void setGenerator(String generator) {
        this.generator = generator;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
