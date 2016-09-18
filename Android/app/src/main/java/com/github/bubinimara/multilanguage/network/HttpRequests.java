package com.github.bubinimara.multilanguage.network;

import android.content.Context;
import android.provider.Settings;

import com.github.bubinimara.multilanguage.model.LanguageModel;
import com.github.bubinimara.multilanguage.model.cms.CategoryCmsModel;
import com.github.bubinimara.multilanguage.model.cms.ProductCmsModel;
import com.github.bubinimara.multilanguage.util.JsonToModelConverter;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.CacheControl;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by davide on 4/08/16.
 *
 * Manage the http requests
 */
public class HttpRequests {

    private static final OkHttpClient client = new OkHttpClient();

    // the host, usually is a constants
    private static String HOST;
    // device id
    public static String ANDROID_ID;


    /**
     * Initialize the http request class
     * @param context the application context
     */
    public static void init(Context context,String host){
        ANDROID_ID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        HOST = host;
    }

    public static String getAndroidId() {
        return ANDROID_ID;
    }

    public static OkHttpClient getClient() {
        return client;
    }

    /************************************************************************/
    /*************************** DB RELATED CALLS ***************************/
    /************************************************************************/

    /**
     * Get all languages
     * @return list of supported languages
     * @throws IOException if an error occurs
     */
    public static ArrayList<LanguageModel> getLanguages() throws IOException {
        Request request = new Builder().addUrlPath(Queries.LANGUAGES).build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        String body = response.body().string();

        try {
            return JsonToModelConverter.createLanguages(body);
        } catch (JsonSyntaxException e) {
            throw new IOException("Malformed data "+body);
        }
    }

    /**
     * Get all categories in all languages
     * @return the list of CategoryCmsModel
     * @throws IOException if an error occurs
     */
    public static ArrayList<CategoryCmsModel> getCategories() throws IOException {
        Request request = new Builder().addUrlPath(Queries.CATEGORIES).build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        String body = response.body().string();

        try {
            return JsonToModelConverter.createCategories(body);
        } catch (JsonSyntaxException e) {
            throw new IOException("Malformed data "+body);
        }
    }

    /**
     * Get all categories in all languages
     * @return the list of ProductCmsModel
     * @throws IOException if an error occurs
     */
    public static ArrayList<ProductCmsModel> getProducts() throws IOException {
        Request request = new Builder().addUrlPath(Queries.PRODUCTS).build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        String body = response.body().string();

        try {
            return JsonToModelConverter.createProducts(body);
        } catch (JsonSyntaxException e) {
            throw new IOException("Malformed data "+body);
        }
    }


    /**
     * Api queries
     */
    public static final class Queries{
        private Queries() { }

        /**
         * Get all languages
         */
        public static final String LANGUAGES = "api/v1/languages";

        /**
         * Get all categories with all translations
         */
        public static final String CATEGORIES = "api/v1/categories";

        /**
         * Get all products with all translations
         */
        public static final String PRODUCTS = "api/v1/products";

    }

    /**
     * Automatically add some parameters to the requests
     * Header, Host, No-cache
     */
    private static class Builder extends Request.Builder{
        public static final String HEADER_ID = "X-MyApp-id";

        HttpUrl.Builder urlBuilder = HttpUrl.parse(HOST).newBuilder();

        public Builder() {
            addHeader(HEADER_ID,ANDROID_ID);
            cacheControl(CacheControl.FORCE_NETWORK);
        }

        public Builder addUrlPath(String path){
            urlBuilder.addPathSegments(path);
            return this;
        }

        @Override
        public Request build() {
            url(urlBuilder.build());
            return super.build();
        }
    }
}
