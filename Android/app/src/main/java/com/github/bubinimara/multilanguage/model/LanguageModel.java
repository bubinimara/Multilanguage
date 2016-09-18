package com.github.bubinimara.multilanguage.model;

/**
 * Created by davide on 3/08/16.
 */
public class LanguageModel extends AbModel {
    private String name;
    private String iso;
    private int isDefault;

    /**
     *
     * @param id the id
     * @param name display name
     * @param iso iso code
     * @param isDefault 1 if is default 0 otherwise
     */
    public LanguageModel(int id, String name, String iso, int isDefault) {
        this.id = id;
        this.name = name;
        this.iso = iso;
        this.isDefault = isDefault;
    }

    public LanguageModel(int id, String name, String iso) {
        this(id,name,iso,0);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIso() {
        return iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    public int getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }

}
