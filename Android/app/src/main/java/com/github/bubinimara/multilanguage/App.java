package com.github.bubinimara.multilanguage;

import android.app.Application;

import com.github.bubinimara.multilanguage.database.Session;
import com.github.bubinimara.multilanguage.network.HttpRequests;

import java.util.Observable;

/**
 * Created by davide on 8/09/16.
 */
public class App extends Application {

    private static final String HOST = "http://10.0.2.2:8080";

    private static volatile Configurations configurations = new Configurations();


    @Override
    public void onCreate() {
        super.onCreate();
        HttpRequests.init(this,HOST);
        configurations.setLanguage(Session.getDefaultLanguage(this)); // set default language
    }

    /**
     * Global configuration
     * @return Global configuration
     */
    public static Configurations getConfigurations(){
        return configurations;
    }

    /**
     * Runtime configurations
     */
    public static class Configurations extends Observable {
        /**
         * Current language
         */
        private int language;

        public Configurations() {

        }

        public int getLanguage() {
            return language;
        }

        public void setLanguage(int language) {
            this.language = language;
            setChanged();
            notifyObservers();
        }
    }
}
