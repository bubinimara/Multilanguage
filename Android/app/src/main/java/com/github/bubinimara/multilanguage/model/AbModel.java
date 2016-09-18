package com.github.bubinimara.multilanguage.model;

/**
 * Created by davide on 3/08/16.
 */
public class AbModel implements IModel {
    protected int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        try {
            AbModel ab = (AbModel) obj;
            return id==ab.id;
        } catch (Exception e) {

            return super.equals(obj);
        }
    }
}
