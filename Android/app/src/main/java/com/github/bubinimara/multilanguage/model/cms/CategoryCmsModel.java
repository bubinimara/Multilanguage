package com.github.bubinimara.multilanguage.model.cms;

import java.util.Arrays;

/**
 * Created by davide on 5/08/16.
 *
 * Class used to manage the content
 *
 *
 *
 */

/*  {
      "id": 1,
      "priority": 1,
      "languages": [
        {
          "id": 123,
          "name": "category 1"
        },
        {
          "id": 456,
          "name": "categoria 1"
        }
      ]
    }

*/
public class CategoryCmsModel {
    private int id;
    private int priority;
    Language[] languages;

    public CategoryCmsModel() {
    }

    public CategoryCmsModel(int id, int priority, Language[] languages) {
        this.id = id;
        this.priority = priority;
        this.languages = Arrays.copyOf(languages,languages.length);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
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

        public Language() {
        }

        public Language(int id, String name) {
            this.id = id;
            this.name = name;
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

    }
}
