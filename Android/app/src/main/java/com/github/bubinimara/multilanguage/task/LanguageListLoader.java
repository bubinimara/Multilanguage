package com.github.bubinimara.multilanguage.task;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.github.bubinimara.multilanguage.database.DatabaseManager;
import com.github.bubinimara.multilanguage.model.LanguageModel;

import java.util.ArrayList;

/**
 * Created by davide on 9/08/16.
 */
public class LanguageListLoader extends AsyncTaskLoader<ArrayList<LanguageModel>> {

    DatabaseManager databaseManager;

    private ArrayList<LanguageModel> data;

    public LanguageListLoader(Context context) {
        super(context);
        databaseManager = new DatabaseManager(context.getApplicationContext());
    }


    @Override
    public ArrayList<LanguageModel> loadInBackground() {
        ArrayList<LanguageModel> languages = databaseManager.getAllLanguages();
        if(data == null){
            data = new ArrayList<>(languages.size());
        }
        data.addAll(languages);
        return data;
    }

    @Override
    public void deliverResult(ArrayList<LanguageModel> data) {
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
    public void onContentChanged() {
        super.onContentChanged();
    }
}
