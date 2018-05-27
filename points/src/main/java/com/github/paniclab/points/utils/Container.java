package com.github.paniclab.points.utils;


import java.util.*;


public abstract class Container<T> implements Iterable<T> {

    private List<T> content;


    public Container(Set<T> content) {
        this.content = new ArrayList<>(content);
    }


    @Override
    public Iterator<T> iterator() {
        return this.content.iterator();
    }

    public int size() {
        return content.size();
    }

    public abstract void shuffle();

    public List<T> getContent() {
        return Collections.unmodifiableList(content);
    }

    protected List<T> content() {
        return content;
    }
}
