package io.github.mustabelmo.export.examples.objects;

import io.github.mustabelmo.exporter.api.annotation.ToColumn;

public abstract class AbstractCar {
    @ToColumn
    private String hhh;
    
    @ToColumn
    public abstract String getStringId();
    
    @ToColumn
    protected abstract String stringId();
    
    @ToColumn
    public String getFromAbstract() {
        return null;
    }
    
    public String getHhh() {
        return hhh;
    }
}
