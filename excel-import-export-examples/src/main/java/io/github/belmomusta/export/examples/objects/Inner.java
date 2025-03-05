package io.github.mustabelmo.export.examples.objects;

import io.github.mustabelmo.exporter.api.annotation.ToColumn;

public class Inner {
    @ToColumn()
    private String id;
    
    private String anotherId;
    
    public String getId() {
        return id;
    }
    
    @ToColumn
    public String getAnotherId() {
        return id;
    }
    
}
