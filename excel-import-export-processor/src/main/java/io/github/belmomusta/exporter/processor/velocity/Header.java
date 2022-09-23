package io.github.belmomusta.exporter.processor.velocity;

public class Header extends  FieldMethodPair {
    private final String name;
    public Header(String name){
        this(name, name);
    }
    public Header(String name, String method) {
        super(name, method);
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

}
