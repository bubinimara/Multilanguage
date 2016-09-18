package com.github.bubinimara.multilanguage.task;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.github.bubinimara.multilanguage.App;
import com.github.bubinimara.multilanguage.database.DatabaseManager;
import com.github.bubinimara.multilanguage.model.ProductModel;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by davide on 9/08/16.
 */
public class ProductListLoader extends AsyncTaskLoader<ArrayList<ProductModel>> implements Observer {

    private static final String TAG = "ProductLoader";
    DatabaseManager databaseManager;
    App.Configurations configurations;
    private int categoryId;

    private ArrayList<ProductModel> data;


    public ProductListLoader(Context context, int categoryId) {
        super(context);
        this.categoryId = categoryId;
        databaseManager = new DatabaseManager(context.getApplicationContext());
        configurations = App.getConfigurations();
        App.getConfigurations().addObserver(this);
    }

    @Override
    public ArrayList<ProductModel> loadInBackground() {
        ArrayList<ProductModel> allCategoryModel = databaseManager.getAllProductModel(categoryId,App.getConfigurations().getLanguage());
        if(data == null){
            data = new ArrayList<>(allCategoryModel.size());
        }
        data.clear();
        data.addAll(allCategoryModel);
        return data;
    }

    @Override
    public void deliverResult(ArrayList<ProductModel> data) {
        this.data = data;
        if(isStarted()) {
            super.deliverResult(data);
        }
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();

        if(data!=null){
            deliverResult(data);
        }else{
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        super.onStopLoading();
        cancelLoad();
    }

    @Override
    protected void onReset() {
        super.onReset();
        App.getConfigurations().deleteObserver(this);
    }



    @Override
    public void onContentChanged() {
        super.onContentChanged();
        if(isStarted()){
            forceLoad();
        }
    }

    @Override
    public void update(Observable observable, Object o) {
        data = null;
        if(isStarted()){
            forceLoad();
        }
    }
}
