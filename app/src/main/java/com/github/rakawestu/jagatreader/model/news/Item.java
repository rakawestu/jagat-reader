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
public class Item {
    @Element(name = "guid")
    private Guid guid;
    @Element(name = "pubDate")
    private String pubDate;
    @Element(name = "title")
    private String title;
    @ElementList(entry= "category", required = false, inline = true)
    private List<String> category;
    @Element(name = "description")
    private String description;
    @Element(name = "link")
    private String link;
    @Element(name = "creator")
    private String creator;
    @Element(name = "commentRss")
    private String commentRss;
    @ElementList(inline = true, required = false, entry = "comments")
    private List<String> comments;
    @Element(name = "encoded")
    private String content;
    @Element(name = "enclosure", required = false)
    private Enclosure enclosure;

    public Guid getGuid() {
        return guid;
    }

    public void setGuid(Guid guid) {
        this.guid = guid;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
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
}
