package com.andrewgrey.framework.gui;

public class Plugin {

    private final String name;
    private final String link;

    public Plugin(String name, String link) {
        this.name = name;
        this.link = link;
    }

    public String getName() {
        return this.name;
    }

    public String getLink() {
        return this.link;
    }

//    @Override
//    public String toString() {
////        return "Plugin[" +
////                "text=" + this.name + ", " +
////                "link=" + this.link + ']';
//        return "{ \"text\": \"" + this.name + "\"," +
//                " \"link\": \"" + this.link + "\"}";
//    }
}
