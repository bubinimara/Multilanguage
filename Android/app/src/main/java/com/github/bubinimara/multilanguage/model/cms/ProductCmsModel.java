package com.github.bubinimara.multilanguage.model.cms;

import java.util.Arrays;

/**
 * Created by davide on 8/08/16.
 * Manage content
 */

/*
 {
      "id": 1,
      "category": 1,
      "priority": 1,
      "image": "http://www.hela.es/assets/images/contenido/5_1.jpg",
      "prize: "23.9",
      "languages": [
        {
          "id": 123,
          "name": "salad 1",
          "description": "description"
        },
        {
          "id": 456,
          "name": "ensalada 1",
          "description": "description"
        }
      ]
    },
 */
public class ProductCmsModel {
    private int id;
    private int category;
    private int priority;
    private String image;
    Language[] languages;

    public ProductCmsModel() {
    }

    public ProductCmsModel(int id, int category, int priority, String image, Language[] languages) {
        this.id = id;
        this.category = category;
        this.priority = priority;
        this.image = image;
        this.languages = Arrays.copyOf(languages,languages.length);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Language[] getLanguages() {
        return languages;
    }

    public void setLanguages(Language[] languages) {
        this.languages = languages;
    }

    public static class Language{
        private int id;
        private String name;
        private String description;

        public Language() {
        }

        public Language(int id, String name, String description) {
            this.id = id;
            this.name = name;
            this.description = description;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}
