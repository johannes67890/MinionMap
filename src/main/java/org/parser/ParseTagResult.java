package org.parser;

import java.math.BigDecimal;
import java.util.HashMap;

import org.Tags;

public class ParseTagResult {
    private Tag<Tags.Node, Number> node;
    private Tag<Tags.Adress, String> adress;
    private Tag<Tags.Bounds, BigDecimal> bounds;

    public static ParseTagResult fromNodeTag(Tag<Tags.Node, Number> n){
        ParseTagResult res = new ParseTagResult();
        res.node = n;
        return res;
    }
    public static ParseTagResult fromAdressTag(Tag<Tags.Adress, String> n){
        ParseTagResult res = new ParseTagResult();
        res.adress = n;
        return res;
    }
    public static ParseTagResult fromBoundsTag(Tag<Tags.Bounds, BigDecimal> n){
        ParseTagResult res = new ParseTagResult();
        res.bounds = n;
        return res;
    }

    public boolean isNode(){
        return this.node != null;
    }
    public boolean isAdress(){
        return this.adress != null;
    }
    public boolean isBounds(){
        return this.bounds != null;
    }

    public Tag getTag(){
        if(this.isNode()) return this.node;
        if(this.isAdress()) return this.adress;
        if(this.isBounds()) return this.bounds;
        return null;
    }


}
