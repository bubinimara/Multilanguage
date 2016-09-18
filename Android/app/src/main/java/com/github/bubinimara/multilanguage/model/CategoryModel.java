package com.github.bubinimara.multilanguage.model;

/**
 * Created by davide on 3/08/16.
 */
public class CategoryModel extends AbModel{
    private String name;
    private int order;

    public CategoryModel(int id,String name, int order) {
        super.id = id;
        this.name = name;
        this.order = order;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
