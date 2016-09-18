package com.github.bubinimara.multilanguage.util;

import com.github.bubinimara.multilanguage.model.LanguageModel;
import com.github.bubinimara.multilanguage.model.cms.CategoryCmsModel;
import com.github.bubinimara.multilanguage.model.cms.ProductCmsModel;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;

/**
 * Created by davide on 4/08/16.
 *
 * Convert http json response to object model
 */
public class JsonToModelConverter {


    /**
     * Convert the string passed as argument into a list of LanguageModel object
     * @param jsonObject the json as string
     * @return the list of the LanguageModel objects
     * @throws JsonSyntaxException if the argument is malformed
     */
    public static ArrayList<LanguageModel> createLanguages(String jsonObject) throws JsonSyntaxException {
        JsonObject jo = (new JsonParser()).parse(jsonObject).getAsJsonObject();

        ArrayList<LanguageModel> languages = new ArrayList<>();
        JsonArray langs = jo.getAsJsonArray("languages");
        for (int i = 0; i < langs.size(); i++) {
            try {
                languages.add(createLanguage(langs.get(i).toString()));
            } catch (JsonSyntaxException e) {
                // skip
            }
        }

        return languages;
    }

    /**
     * Parse the string passed as argument and convert it into a LanguageModel
     * @param jsonObject
     * @return new LanguageModel object
     * @throws com.google.gson.JsonSyntaxException if the json is malformed
     */
    private static LanguageModel createLanguage(String jsonObject) {
        return new Gson().fromJson(jsonObject,LanguageModel.class);
    }

    /**
     * Convert the string passed as argument into a list of CategoryCmsModel objects
     * @param jsonObject the Json object as string
     * @return the list of CategoryCmsModel object
     * @throws JsonSyntaxException if the argument is malformed
     */
    public static ArrayList<CategoryCmsModel> createCategories(String jsonObject) throws JsonSyntaxException {
        ArrayList<CategoryCmsModel> list = new ArrayList<>();

        JsonObject jo = (new JsonParser()).parse(jsonObject).getAsJsonObject();
        JsonArray array = jo.getAsJsonArray("categories");
        for (int i = 0; i < array.size(); i++) {
            list.add(createCategory(array.get(i).toString()));
        }
        return list;
    }

    public static CategoryCmsModel createCategory(String json){
        return new Gson().fromJson(json,CategoryCmsModel.class);
        }

    /**
     * Convert the string passed as argument into a list of ProductCmsModel
     * @param jsonObject the json as string
     * @return the list of ProductCmsModel
     * @throws JsonSyntaxException if the argument is malformed
     */
    public static ArrayList<ProductCmsModel> createProducts(String jsonObject) throws JsonSyntaxException{
        ArrayList<ProductCmsModel> list = new ArrayList<>();

        JsonObject jo = (new JsonParser()).parse(jsonObject).getAsJsonObject();
        JsonArray array = jo.getAsJsonArray("products");
        for (int i = 0; i < array.size(); i++) {
            list.add(createProduct(array.get(i).toString())); // double conversion
        }
        return list;
    }

    public static ProductCmsModel createProduct(String json){
        return new Gson().fromJson(json,ProductCmsModel.class);
        }

}
