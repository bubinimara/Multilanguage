package com.github.bubinimara.multilanguage.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by davide on 3/08/16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "database";
    public static final int DB_VERSION = 1;

    private static final String CREATE_TABLE_LANGUAGE =
            "CREATE TABLE " + DatabaseContract.Language.TABLE_LANGUAGE +
                    " ( " +
                    DatabaseContract.Language._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    DatabaseContract.Language.COLUMN_ID + " INTEGER NOT NULL UNIQUE, " +
                    DatabaseContract.Language.COLUMN_NAME + " TEXT NOT NULL, " +
                    DatabaseContract.Language.COLUMN_ISO + " TEXT NOT NULL , " +
                    DatabaseContract.Language.COLUMN_IS_DEFAULT + " INTEGER DEFAULT 0, " +

                    DatabaseContract.Language.COLUMN_FLAG + " INTEGER DEFAULT 0, " +
                    DatabaseContract.Language.COLUMN_UPDATE + " DATETIME DEFAULT CURRENT_TIMESTAMP " +
                    " );";



    private static final String CREATE_TABLE_CATEGORY =
            "CREATE TABLE " + DatabaseContract.Category.TABLE_CATEGORY +
                    "( " +
                    DatabaseContract.Language._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    DatabaseContract.Category.COLUMN_ID + " INTEGER NOT NULL UNIQUE, " +

                    DatabaseContract.Category.COLUMN_ORDER + " INTEGER DEFAULT 0, " +

                    DatabaseContract.Category.COLUMN_FLAG + " INTEGER DEFAULT 0, " +
                    DatabaseContract.Category.COLUMN_UPDATE + " DATETIME DEFAULT CURRENT_TIMESTAMP " +
                    " );";

    private static final String CREATE_TABLE_CATEGORY_LANG =
            "CREATE TABLE " + DatabaseContract.Category.TABLE_CATEGORY_LANG +
                    "( " +
                    DatabaseContract.Category._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                    DatabaseContract.Category.COLUMN_NAME + " INTEGER DEFAULT 0, " +

                    DatabaseContract.Category.COLUMN_FLAG + " INTEGER DEFAULT 0, " +
                    DatabaseContract.Category.COLUMN_UPDATE + " DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                    // foregin key
                    DatabaseContract.Category.COLUMN_ID + " INTEGER NOT NULL, " +
                    DatabaseContract.Language.COLUMN_ID + " INTEGER NOT NULL, " +

                    // both unique
                    "UNIQUE ( "+
                    DatabaseContract.Category.COLUMN_ID + ", " +
                    DatabaseContract.Language.COLUMN_ID +
                    " ) ON CONFLICT REPLACE , "+

                    "FOREIGN KEY("+DatabaseContract.Category.COLUMN_ID+") REFERENCES "+ DatabaseContract.Category.TABLE_CATEGORY+"("+DatabaseContract.Category.COLUMN_ID+") ON DELETE CASCADE, " +
                    "FOREIGN KEY("+DatabaseContract.Language.COLUMN_ID+") REFERENCES "+ DatabaseContract.Language.TABLE_LANGUAGE+"("+DatabaseContract.Language.COLUMN_ID+") ON DELETE CASCADE " +

                    " );";

    private static final String CREATE_TABLE_PRODUCT =
            "CREATE TABLE " + DatabaseContract.Product.TABLE_PRODUCT +
                    "( " +
                    DatabaseContract.Product._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    DatabaseContract.Product.COLUMN_ID + " INTEGER NOT NULL UNIQUE, " +

                    DatabaseContract.Product.COLUMN_ORDER + " INTEGER DEFAULT 0, " +
                    DatabaseContract.Product.COLUMN_IMAGE + " TEXT, " +

                    DatabaseContract.Product.COLUMN_FLAG + " INTEGER DEFAULT 0, " +
                    DatabaseContract.Product.COLUMN_UPDATE + " DATETIME DEFAULT CURRENT_TIMESTAMP, " +

                    // FK
                    DatabaseContract.Category.COLUMN_ID + " INTEGER NOT NULL, " +
                    "FOREIGN KEY("+DatabaseContract.Category.COLUMN_ID+") REFERENCES "+ DatabaseContract.Category.TABLE_CATEGORY+"("+DatabaseContract.Category.COLUMN_ID+") ON DELETE CASCADE " +
                    " );";

    private static final String CREATE_TABLE_PRODUCT_LANG =
            "CREATE TABLE " + DatabaseContract.Product.TABLE_PRODUCT_LANG +
                    "( " +
                    DatabaseContract.Product._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                    DatabaseContract.Product.COLUMN_NAME + " TEXT NOT NULL, " +
                    DatabaseContract.Product.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +

                    DatabaseContract.Product.COLUMN_FLAG + " INTEGER DEFAULT 0, " +
                    DatabaseContract.Product.COLUMN_UPDATE + " DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                    // foregin key
                    DatabaseContract.Product.COLUMN_ID + " INTEGER NOT NULL, " +
                    DatabaseContract.Language.COLUMN_ID + " INTEGER NOT NULL, " +

                    // both unique
                    "UNIQUE ( "+
                    DatabaseContract.Product.COLUMN_ID + ", " +
                    DatabaseContract.Language.COLUMN_ID +
                    " ) ON CONFLICT REPLACE , "+

                    "FOREIGN KEY("+DatabaseContract.Product.COLUMN_ID+") REFERENCES "+ DatabaseContract.Product.TABLE_PRODUCT+"("+DatabaseContract.Product.COLUMN_ID+") ON DELETE CASCADE, " +
                    "FOREIGN KEY("+DatabaseContract.Language.COLUMN_ID+") REFERENCES "+ DatabaseContract.Language.TABLE_LANGUAGE+"("+DatabaseContract.Language.COLUMN_ID+") ON DELETE CASCADE " +

                    " );";


    private static final String DELETE_TABLE_LANGUAGE = "DROP TABLE IF EXISTS " + DatabaseContract.Language.TABLE_LANGUAGE;
    private static final String DELETE_TABLE_CATEGORY = "DROP TABLE IF EXISTS " + DatabaseContract.Category.TABLE_CATEGORY;
    private static final String DELETE_TABLE_CATEGORY_LANG = "DROP TABLE IF EXISTS " + DatabaseContract.Category.TABLE_CATEGORY_LANG;
    private static final String DELETE_TABLE_PRODUCT = "DROP TABLE IF EXISTS " + DatabaseContract.Product.TABLE_PRODUCT;
    private static final String DELETE_TABLE_PRODUCT_LANG = "DROP TABLE IF EXISTS " + DatabaseContract.Product.TABLE_PRODUCT_LANG;

    private static DatabaseHelper instance;

    private DatabaseHelper(Context context) {
        this(context, null);
    }

    private DatabaseHelper(Context context, SQLiteDatabase.CursorFactory factory) {
        super(context, DB_NAME, factory, DB_VERSION);
    }

    /**
     * Get single instance of this class
     * @param context the application context
     * @return the instance of this class
     */
    public static synchronized DatabaseHelper getInstance(Context context) {
        if(instance == null){
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_LANGUAGE);
        db.execSQL(CREATE_TABLE_CATEGORY);
        db.execSQL(CREATE_TABLE_CATEGORY_LANG);
        db.execSQL(CREATE_TABLE_PRODUCT);
        db.execSQL(CREATE_TABLE_PRODUCT_LANG);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DELETE_TABLE_LANGUAGE);
        db.execSQL(DELETE_TABLE_CATEGORY);
        db.execSQL(DELETE_TABLE_CATEGORY_LANG);
        db.execSQL(DELETE_TABLE_PRODUCT);
        db.execSQL(DELETE_TABLE_PRODUCT_LANG);

        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys=ON");
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DELETE_TABLE_LANGUAGE);
        db.execSQL(DELETE_TABLE_CATEGORY);
        db.execSQL(DELETE_TABLE_CATEGORY_LANG);
        db.execSQL(DELETE_TABLE_PRODUCT);
        db.execSQL(DELETE_TABLE_PRODUCT_LANG);

        onCreate(db);
    }

    public void deleteAllTables(){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(DatabaseContract.Language.TABLE_LANGUAGE,null,null);
        db.delete(DatabaseContract.Category.TABLE_CATEGORY,null,null);
        db.delete(DatabaseContract.Category.TABLE_CATEGORY_LANG,null,null);
        db.delete(DatabaseContract.Product.TABLE_PRODUCT,null,null);
        db.delete(DatabaseContract.Product.TABLE_PRODUCT_LANG,null,null);

    }
}
