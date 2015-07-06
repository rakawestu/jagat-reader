package com.github.rakawestu.jagatreader.model.news;

import org.simpleframework.xml.Attribute;

/**
 * Created by raka on 06/07/2015.
 */
public class Enclosure {
    @Attribute(name = "url", required = false)
    private String url;
    @Attribute(name = "length", required = false)
    private String length;
    @Attribute(name = "type", required = false)
    private String type;
}
