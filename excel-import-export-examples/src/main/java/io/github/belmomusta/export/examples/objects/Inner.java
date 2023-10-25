package io.github.belmomusta.export.examples.objects;

import io.github.belmomusta.exporter.api.annotation.ToColumn;

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
