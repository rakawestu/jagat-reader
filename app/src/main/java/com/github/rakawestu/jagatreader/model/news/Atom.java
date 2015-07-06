package com.github.rakawestu.jagatreader.model.news;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * Created by raka on 02/07/2015.
 */
public class Atom {
    @Attribute(required = false)
    private String rel;
    @Attribute(required = false)
    private String type;
    @Attribute(required = false)
    private String href;

    public String getRel() {
        return rel;
    }

    public void setRel(String rel) {
        this.rel = rel;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }
}
