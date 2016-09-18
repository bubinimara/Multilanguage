package com.github.bubinimara.multilanguage.task;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.github.bubinimara.multilanguage.App;
import com.github.bubinimara.multilanguage.database.DatabaseManager;
import com.github.bubinimara.multilanguage.model.CategoryModel;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by davide on 9/08/16.
 */
public class CategoryListLoader extends AsyncTaskLoader<ArrayList<CategoryModel>> implements Observer {

    private static final String TAG = "CategoryLoader";
    DatabaseManager databaseManager;
    App.Configurations configurations;

    private ArrayList<CategoryModel> data;

    public CategoryListLoader(Context context) {
        super(context);
        databaseManager = new DatabaseManager(context.getApplicationContext());
        configurations = App.getConfigurations();
    }


    @Override
    public ArrayList<CategoryModel> loadInBackground() {
        ArrayList<CategoryModel> allCategoryModel = databaseManager.getAllCategoryModel(configurations.getLanguage());
        if(data == null){
            data = new ArrayList<>(allCategoryModel.size());
        }
        data.addAll(allCategoryModel);
        return data;
    }

    @Override
    public void deliverResult(ArrayList<CategoryModel> data) {
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
        // unregister

    }

    @Override
    public void registerListener(int id, OnLoadCompleteListener<ArrayList<CategoryModel>> listener) {
        super.registerListener(id, listener);
        App.getConfigurations().addObserver(this);

    }

    @Override
    public void unregisterListener(OnLoadCompleteListener<ArrayList<CategoryModel>> listener) {
        super.unregisterListener(listener);
        App.getConfigurations().deleteObserver(this);

    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        if(isStarted())
            forceLoad();
    }

    @Override
    public void update(Observable observable, Object o) {
        if(isStarted()) {
            data = null;
            forceLoad();
        }
    }
}
