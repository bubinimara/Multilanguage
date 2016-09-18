package com.github.bubinimara.multilanguage.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;


import com.github.bubinimara.multilanguage.App;
import com.github.bubinimara.multilanguage.BuildConfig;
import com.github.bubinimara.multilanguage.database.DatabaseManager;
import com.github.bubinimara.multilanguage.database.Session;
import com.github.bubinimara.multilanguage.model.LanguageModel;
import com.github.bubinimara.multilanguage.model.cms.CategoryCmsModel;
import com.github.bubinimara.multilanguage.model.cms.ProductCmsModel;
import com.github.bubinimara.multilanguage.network.HttpRequests;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Handle actions to initialize and clear the database
 */
public class DatabaseIntentService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this

    private static final String ACTION_INIT = BuildConfig.APPLICATION_ID+".services.ACTION_INIT";
    private static final String ACTION_CLEAR = BuildConfig.APPLICATION_ID+".services.ACTION_CLEAR";;

    public static final String ACTION_INIT_ERROR = BuildConfig.APPLICATION_ID+".services.ACTION_INIT_ERROR";
    public static final String ACTION_INIT_SECCESS = BuildConfig.APPLICATION_ID+".services.ACTION_INIT_SUCCESS";


    private DatabaseManager databaseManager;

    public DatabaseIntentService() {
        super("DatabaseIntentService");
    }

    /**
     * Starts this service to perform action Init with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionInit(Context context) {
        Intent intent = new Intent(context, DatabaseIntentService.class);
        intent.setAction(ACTION_INIT);
        context.startService(intent);
    }

    /**
     * Start action for clear
     *
     * @param context
     */
    public static void startActionClear(Context context){
        Intent intent = new Intent(context, DatabaseIntentService.class);
        intent.setAction(ACTION_CLEAR);
        context.startService(intent);

    }
    /**
     * To register the receiver
     * @return the intent filter for register the receiver
     */
    public static IntentFilter getIntentFilter(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_INIT_ERROR);
        intentFilter.addAction(ACTION_INIT_SECCESS);
        return intentFilter;
    }

    LocalBroadcastManager localBroadcastManager;
    @Override
    public void onCreate() {
        super.onCreate();
        // this will be initialized by application class
        databaseManager = new DatabaseManager(this);
        localBroadcastManager = LocalBroadcastManager.getInstance(this);

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_INIT.equals(action)) {
                try {
                    handleActionInit();
                } catch (IOException e) {
                    // handle error here
                    e.printStackTrace();

                    databaseManager.clearAll();
                    broadcastAction(ACTION_INIT_ERROR);

                }
            }else if(ACTION_CLEAR.equals(action)){
                databaseManager.clearAll();
                Session.setInitialized(this,false);
            }
        }
    }

    /**
     * On exception will clear all database
     * @throws IOException
     */
    private void handleActionInit() throws IOException {

        if(Session.isInitialized(this)){
            return; // do nothing
        }
        // pupolate language
        ArrayList<LanguageModel> languages = HttpRequests.getLanguages();
        databaseManager.addLanguage(languages);
        if(languages.isEmpty()){
            throw new IOException("No Languages");
        }
        // populate category
        ArrayList<CategoryCmsModel> categories = HttpRequests.getCategories();
        databaseManager.addAllCategoryModel(categories);
        if(categories.isEmpty()){
            throw new IOException("No categories");
        }

        // populate products
        ArrayList<ProductCmsModel> products = HttpRequests.getProducts();
        databaseManager.addAllProductModel(products);


      /*  Session session = new Session(info);
        session.setReady(true);
        session.setTimestamp();
        // set default language

        session.setDefaultLanguage(languages.get(0).getId());*/
        int defaultLanguageId = languages.get(0).getId();
        for(LanguageModel languageModel : languages){
            if(languageModel.getIsDefault() == 1){
                defaultLanguageId = languageModel.getId();
                break;
            }
        }
        Session.setInitialized(this,true);
        Session.setDefaultLanguage(this,defaultLanguageId);
        App.getConfigurations().setLanguage(defaultLanguageId);
        broadcastAction(ACTION_INIT_SECCESS);
    }

    private void broadcastAction(String actions) {
        Intent intent = new Intent(actions);
        localBroadcastManager.sendBroadcast(intent);
    }

}
