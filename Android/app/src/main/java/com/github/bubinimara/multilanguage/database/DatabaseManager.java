package com.github.bubinimara.multilanguage.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.support.annotation.NonNull;

import com.github.bubinimara.multilanguage.model.CategoryModel;
import com.github.bubinimara.multilanguage.model.LanguageModel;
import com.github.bubinimara.multilanguage.model.ProductModel;
import com.github.bubinimara.multilanguage.model.cms.CategoryCmsModel;
import com.github.bubinimara.multilanguage.model.cms.ProductCmsModel;

import java.util.ArrayList;

/**
 * Created by davide on 3/08/16.
 *
 * Can be, in future a wrapper of the content provider
 */
public class DatabaseManager {
    private Context context;
    private DatabaseHelper databaseHelper;

    public DatabaseManager(Context context) {
        this.context = context;
        databaseHelper = DatabaseHelper.getInstance(context);
    }

    public Context getContext() {
        return context;
    }


    /**
     * Destructive clear all table in the database
     */
    public void clearAll(){
        databaseHelper.deleteAllTables();
    }

    /************************************************************************************/
    /**************************** LANGUAGE **********************************************/
    /************************************************************************************/

    /**
     * Add the language to the database
     * @param languageModel the language to add
     */
    public void addLanguage(@NonNull LanguageModel languageModel){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        try {
            db.insertOrThrow(DatabaseContract.Language.TABLE_LANGUAGE,null, ContentValueUtils.createContentValue(languageModel));
        } catch (SQLException e) {
            String[] whereArgs = {String.valueOf(languageModel.getId())};
            String where = DatabaseContract.Language.COLUMN_ID + " = ?";
            db.update(DatabaseContract.Language.TABLE_LANGUAGE, ContentValueUtils.createContentValue(languageModel),where,whereArgs);
        }
        db.close();
    }

    /**
     * Perfom a bulk insert of all items passed as paramter
     * @param list the items to add
     */
    public void addLanguage(@NonNull ArrayList<LanguageModel> list){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.beginTransaction();

        for (LanguageModel languageModel :
                list) {
            try {
                db.insertOrThrow(DatabaseContract.Language.TABLE_LANGUAGE, null, ContentValueUtils.createContentValue(languageModel));
            } catch (SQLException e) {
                String[] whereArgs = {String.valueOf(languageModel.getId())};
                String where = DatabaseContract.Language.COLUMN_ID + " = ?";
                db.update(DatabaseContract.Language.TABLE_LANGUAGE, ContentValueUtils.createContentValue(languageModel), where, whereArgs);
            }
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();

    }
    /**
     * Get the lanugage with id passe as parameter
     * @param id the language id
     * @return the languageModel or null if no language are found with the id
     */
    public LanguageModel getLanguage(int id){
        LanguageModel languageModel = null;
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        String selection = "";

        builder.setTables(DatabaseContract.Language.TABLE_LANGUAGE);
        builder.appendWhere(DatabaseContract.Language.COLUMN_ID + " = " + id);

        Cursor c = builder.query(db,null,null,null,null,null,null);
        if(c!=null && c.moveToFirst()){
            languageModel = ContentValueUtils.createLanguageModel(c);
        }
        if(c!=null){
            c.close();
        }
        db.close();
        return languageModel;
    }

    /**
     * Get all languages in db
     * @return the list of all languages
     */
    public ArrayList<LanguageModel> getAllLanguages(){
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        ArrayList<LanguageModel> list = new ArrayList<>();

        Cursor c = db.query(DatabaseContract.Language.TABLE_LANGUAGE,null,null,null,null,null,null);
        if(c!=null ){
            while(c.moveToNext()){
                list.add(ContentValueUtils.createLanguageModel(c));
            }
            c.close();
        }
        return list;
    }

    public int getLanguageCount(){
        int count = -1;
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor c = db.query(DatabaseContract.Language.TABLE_LANGUAGE, DatabaseContract.Language.PROJECTION_COUNT, null, null, null, null, null);
        if(c!=null){
            count = 0;
            if(c.moveToFirst()){
                count = c.getInt(c.getColumnIndex(DatabaseContract.Language.COLUMN_COUNT));
            }
            c.close();
        }
        db.close();
        return count;
    }

    public int deleteLanguage(int id){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        String where = DatabaseContract.Language.COLUMN_ID + " = ? ";
        String[] whereArgs = { String.valueOf(id) };

        int row = db.delete(DatabaseContract.Language.TABLE_LANGUAGE, where, whereArgs);
        db.close();
        return row;
    }

    public int deleteAllLanguage(){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int row = db.delete(DatabaseContract.Language.TABLE_LANGUAGE, null, null);
        db.close();
        return row;
    }

    /************************************************************************************/
    /**************************** CATEGORY **********************************************/
    /************************************************************************************/

    /**
     * Add category no translatable fields to the table
     *
     * @param categoryId the category id
     * @param order the order
     * @return true on success, false otherwise
     */
    public boolean addCategory(int categoryId, int order){
        int rows;
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseContract.Category.COLUMN_ID,categoryId);
        contentValue.put(DatabaseContract.Category.COLUMN_ORDER,order);
        try {
            db.insertOrThrow(DatabaseContract.Category.TABLE_CATEGORY,null,contentValue);
            rows = 1;
        } catch (SQLException e) {
            String[] whereArgs = {String.valueOf(categoryId)};
            String where = DatabaseContract.Category.COLUMN_ID + " = ?";
            rows = db.update(DatabaseContract.Category.TABLE_CATEGORY,contentValue,where,whereArgs);
        }
        db.close();
        return rows !=0;
    }

    /**
     * Get the list of category.
     *
     * @return the list of category ids
     */
    public ArrayList<Integer> getCategoryId(){
        ArrayList<Integer> list = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        Cursor c = db.query(DatabaseContract.Category.TABLE_CATEGORY,null,null,null,null,null,DatabaseContract.Category.COLUMN_ORDER);
        if(c!=null ){
            while(c.moveToNext()){
                list.add(c.getInt(c.getColumnIndex(DatabaseContract.Category.COLUMN_ID)));
            }
            c.close();
        }

        return list;
    }

    /**
     * Get the category count
     * @return the number of category.
     */
    public int getCategoryCount(){
        int count = -1;
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor c = db.query(DatabaseContract.Category.TABLE_CATEGORY, DatabaseContract.Category.PROJECTION_COUNT, null, null, null, null, null);
        if(c!=null){
            count = 0;
            if(c.moveToFirst()){
                count = c.getInt(c.getColumnIndex(DatabaseContract.Category.COLUMN_COUNT));
            }
            c.close();
        }
        db.close();
        return count;
    }

    /**
     * Delete the category and all related translations
     * @param id the id of the category to delete
     * @return the number of rows affected
     */
    public int deleteCategory(int id){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        String where = DatabaseContract.Category.COLUMN_ID + " = ? ";
        String[] whereArgs = { String.valueOf(id) };

        int row = db.delete(DatabaseContract.Category.TABLE_CATEGORY, where, whereArgs);
        db.close();
        return row;
    }

    /**
     * Delete all categories and related translations
     * @return the number of rows deleted
     */
    public int deleteAllCategory(){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int row = db.delete(DatabaseContract.Category.TABLE_CATEGORY, null, null);
        db.close();
        return row;
    }


    /************************************************************************************/
    /**************************** CATEGORY LANG *****************************************/
    /************************************************************************************/

    /**
     * Add language depended translation of category fields
     * @param categoryId the category id, one of getCategory() id
     * @param languageId the language id, one of getLanguage() id
     * @param name
     */
    public void addCategoryLang(int categoryId, int languageId,String name){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseContract.Category.COLUMN_ID,categoryId);
        contentValue.put(DatabaseContract.Language.COLUMN_ID,languageId);
        contentValue.put(DatabaseContract.Category.COLUMN_NAME,name);

        try {
            db.insertOrThrow(DatabaseContract.Category.TABLE_CATEGORY_LANG,null,contentValue);
        } catch (SQLException e) {
            String[] whereArgs = {String.valueOf(categoryId)};
            String where = DatabaseContract.Language.COLUMN_ID + " = ?";
            db.update(DatabaseContract.Category.TABLE_CATEGORY_LANG,contentValue,where,whereArgs);
        }
        db.close();
    }

    /**
     * Bulk insert of categoryLang
     * @param contentValues the content value
     */
    public void addCategoryLang(@NonNull ContentValues[] contentValues){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.beginTransaction();
        for (ContentValues contentValue :
                contentValues) {

            try {
                db.insertOrThrow(DatabaseContract.Category.TABLE_CATEGORY_LANG, null, contentValue);
            } catch (SQLException e) {
                int categoryId = contentValue.getAsInteger(DatabaseContract.Language.COLUMN_ID);
                String[] whereArgs = {String.valueOf(categoryId)};
                String where = DatabaseContract.Language.COLUMN_ID + " = ?";
                db.update(DatabaseContract.Category.TABLE_CATEGORY_LANG, contentValue, where, whereArgs);
            }
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    /**
     * Get the list of category.
     *
     * @return the list of category ids
     */
    public ArrayList<Integer> getCategoryLangId(){
        ArrayList<Integer> list = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        Cursor c = db.query(true, DatabaseContract.Category.TABLE_CATEGORY_LANG,null,null,null,null,null,null,null);
        if(c!=null ){
            while(c.moveToNext()){
                list.add(c.getInt(c.getColumnIndex(DatabaseContract.Category.COLUMN_ID)));
            }
            c.close();
        }

        return list;
    }

    /**
     * Get the category count
     * @return the number of category.
     */
    public int getCategoryLangCount(){
        int count = -1;
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor c = db.query(DatabaseContract.Category.TABLE_CATEGORY_LANG, DatabaseContract.Category.PROJECTION_COUNT, null, null, null, null, null);
        if(c!=null){
            count = 0;
            if(c.moveToFirst()){
                count = c.getInt(c.getColumnIndex(DatabaseContract.Category.COLUMN_COUNT));
            }
            c.close();
        }
        db.close();
        return count;
    }

    /**
     * Delete the category translation
     * @param id the id of the category translation to delete
     * @return the number of rows affected
     */
    public int deleteCategoryLang(int id){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        String where = DatabaseContract.Category.COLUMN_ID + " = ? ";
        String[] whereArgs = { String.valueOf(id) };

        int row = db.delete(DatabaseContract.Category.TABLE_CATEGORY_LANG, where, whereArgs);
        db.close();
        return row;
    }

    /**
     * Delete all categories translation
     * @return the number of rows deleted
     */
    public int deleteAllCategoryLang(){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int row = db.delete(DatabaseContract.Category.TABLE_CATEGORY_LANG, null, null);
        db.close();
        return row;
    }


    // WORKING WITH MODEL
    // !! CHANGE RAW WITH CONSTANTS !!!
    /**
     * Add category no translatable fields to the table
     *
     * @param categoryId the category id
     * @param order the order
     * @return true on success, false otherwise
     */
    private boolean addCategoryDb(SQLiteDatabase db,int categoryId, int order){
        int rows;

        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseContract.Category.COLUMN_ID,categoryId);
        contentValue.put(DatabaseContract.Category.COLUMN_ORDER,order);
        try {
            db.insertOrThrow(DatabaseContract.Category.TABLE_CATEGORY,null,contentValue);
            rows = 1;
        } catch (SQLException e) {
            String[] whereArgs = {String.valueOf(categoryId)};
            String where = DatabaseContract.Category.COLUMN_ID + " = ?";
            rows = db.update(DatabaseContract.Category.TABLE_CATEGORY,contentValue,where,whereArgs);
        }

        return rows !=0;
    }

    /**
     * Add language depended translation of category fields
     * @param categoryId the category id, one of getCategory() id
     * @param languageId the language id, one of getLanguage() id
     * @param name
     */
    private void addCategoryLangDb(SQLiteDatabase db,int categoryId, int languageId,String name){

        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseContract.Category.COLUMN_ID,categoryId);
        contentValue.put(DatabaseContract.Language.COLUMN_ID,languageId);
        contentValue.put(DatabaseContract.Category.COLUMN_NAME,name);

        try {
            db.insertOrThrow(DatabaseContract.Category.TABLE_CATEGORY_LANG,null,contentValue);
        } catch (SQLException e) {
            String[] whereArgs = {String.valueOf(categoryId)};
            String where = DatabaseContract.Language.COLUMN_ID + " = ?";
            db.update(DatabaseContract.Category.TABLE_CATEGORY_LANG,contentValue,where,whereArgs);
        }

    }

    public void addCategoryModel(@NonNull CategoryCmsModel categoryCmsModel){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.beginTransaction();
        CategoryCmsModel.Language[] languages = categoryCmsModel.getLanguages();
        // sanity check, skip if no languages
        if( languages != null && languages.length > 0) {
            addCategoryDb(db, categoryCmsModel.getId(), categoryCmsModel.getPriority());
            for (int i = 0; i < languages.length; i++) {
                addCategoryLangDb(db, categoryCmsModel.getId(), languages[i].getId(), languages[i].getName());
            }
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    public void addAllCategoryModel(@NonNull ArrayList<CategoryCmsModel> categories){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.beginTransaction();
        for(CategoryCmsModel categoryCmsModel:categories){
            CategoryCmsModel.Language[] languages = categoryCmsModel.getLanguages();
            // sanity check, skip if no languages
            if( languages == null || languages.length == 0)
                continue;

            addCategoryDb(db,categoryCmsModel.getId(),categoryCmsModel.getPriority());
            for (int i = 0; i < languages.length; i++) {
                addCategoryLangDb(db,categoryCmsModel.getId(),languages[i].getId(),languages[i].getName());
            }
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    public ArrayList<CategoryModel> getAllCategoryModel(int languageId){
        ArrayList<CategoryModel> list = new ArrayList<>();
        String raw = "SELECT c.categoryId, c.priority," +
                " tr.name " +
                "FROM category c " +
                "LEFT OUTER JOIN categoryLang tr " +
                "ON c.categoryId=tr.categoryId and tr.languageId=? "+
                " ORDER BY c.priority";

        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String[] selectionArgs = {String.valueOf(languageId)};
         Cursor c = db.rawQuery(raw, selectionArgs);
        if(c!=null){
            while (c.moveToNext()){
                list.add(ContentValueUtils.createCategoryModel(c));
            }
            c.close();
        }

        return list;
    }


    /**
     * Get the categories with the language passed as parameter,
     * if the translation is not found than use the default language passed as parameter
     * @param languageId the language
     * @param defaultLanguageId the language used if the first is not found
     * @return the list of categories
     */
    public ArrayList<CategoryModel> getAllCategoryModel(int languageId,int defaultLanguageId){
        ArrayList<CategoryModel> list = new ArrayList<>();
        String raw = "SELECT c.categoryId, c.priority," +
                " IFNULL(tr.name,df.name) name " +
                "FROM category c " +
                "LEFT OUTER JOIN categoryLang tr " +
                "ON c.categoryId=tr.categoryId and tr.languageId=? "+
                " LEFT OUTER JOIN categoryLang df " +
                "On c.categoryId = df.categoryId and df.languageId=? "+
                " ORDER BY c.priority";

        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String[] selectionArgs = {String.valueOf(languageId),String.valueOf(defaultLanguageId)};
        Cursor c = db.rawQuery(DatabaseContract.Category.RAW_SELECT_DEFAULT_LANGUAGE, selectionArgs);
        if(c!=null){
            while (c.moveToNext()){
                list.add(ContentValueUtils.createCategoryModel(c));
            }
            c.close();
        }

        return list;
    }
    /**
     * Helpful method to build and return model of category in language dependent format
     * Make a join of two category's table
     *
     * @param categoryId Category id interested in
     * @param languageId Language id, one of getLanguage() method
     * @return The category Model in language depended format
     */
    public CategoryModel getCategoryModel(int categoryId,int languageId){
        CategoryModel category=null;
        String raw = "SELECT c.categoryId, c.priority," +
                " tr.name " +
                "FROM category c " +
                "LEFT OUTER JOIN categoryLang tr " +
                "ON c.categoryId=tr.categoryId and tr.languageId=? "+
              " WHERE c.categoryId=?";

        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String[] selectionArgs = {String.valueOf(languageId),String.valueOf(categoryId)};
        Cursor c = db.rawQuery(DatabaseContract.Category.RAW_SELECT, selectionArgs);
        if(c!=null){
            while (c.moveToNext()){
               category = (ContentValueUtils.createCategoryModel(c));
            }
            c.close();
        }
        return category;
    }


    /************************************************************************************/
    /********************************** PRODUCT *****************************************/
    /************************************************************************************/



    /**
     * Add product no translatable fields to the table
     *
     * @param categoryId the category id
     * @param order the order
     * @return true on success, false otherwise
     */
    public boolean addProduct(int categoryId,int productId,int order,String image){
        int rows;
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseContract.Category.COLUMN_ID,categoryId);
        contentValue.put(DatabaseContract.Product.COLUMN_ID,productId);
        contentValue.put(DatabaseContract.Product.COLUMN_ORDER,order);
        contentValue.put(DatabaseContract.Product.COLUMN_IMAGE,image);

        try {
            db.insertOrThrow(DatabaseContract.Product.TABLE_PRODUCT,null,contentValue);
            rows = 1;
        } catch (SQLException e) {
            String[] whereArgs = {String.valueOf(productId)};
            String where = DatabaseContract.Product.COLUMN_ID + " = ?";
            rows = db.update(DatabaseContract.Category.TABLE_CATEGORY,contentValue,where,whereArgs);
        }
        db.close();
        return rows !=0;
    }

    /**
     * Get the list of products ids.
     *
     * @return the list of category ids
     */
    public ArrayList<Integer> getProductId(){
        ArrayList<Integer> list = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        Cursor c = db.query(DatabaseContract.Product.TABLE_PRODUCT,null,null,null,null,null,DatabaseContract.Product.COLUMN_ORDER);
        if(c!=null ){
            while(c.moveToNext()){
                list.add(c.getInt(c.getColumnIndex(DatabaseContract.Product.COLUMN_ID)));
            }
            c.close();
        }

        return list;
    }

    /**
     * Get the products count
     * @return the number of products.
     */
    public int getProductCount(){
        int count = -1;
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor c = db.query(DatabaseContract.Product.TABLE_PRODUCT, DatabaseContract.Product.PROJECTION_COUNT, null, null, null, null, null);
        if(c!=null){
            count = 0;
            if(c.moveToFirst()){
                count = c.getInt(c.getColumnIndex(DatabaseContract.Product.COLUMN_COUNT));
            }
            c.close();
        }
        db.close();
        return count;
    }

    /**
     * Delete the product witht the id passed as parameter and all related translations
     * @param id the id of the product to delete
     * @return the number of rows affected
     */
    public int deleteProduct(int id){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        String where = DatabaseContract.Product.COLUMN_ID + " = ? ";
        String[] whereArgs = { String.valueOf(id) };

        int row = db.delete(DatabaseContract.Product.TABLE_PRODUCT, where, whereArgs);
        db.close();
        return row;
    }

    /**
     * Delete all products and related translations
     * @return the number of rows deleted
     */
    public int deleteAllProduct(){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int row = db.delete(DatabaseContract.Product.TABLE_PRODUCT, null, null);
        db.close();
        return row;
    }


    // FROM HERE LANGUAGE RELATED TABLE

    /**
     * Add language depended translation of category fields
     * @param productId the product id
     * @param languageId the language id, one of getLanguage() id
     * @param name name of the product in the language passed as parameter
     *             @param description the description of the product passed as parameter
     */
    public void addProductLang(int productId, int languageId,String name,String description){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseContract.Product.COLUMN_ID,productId);
        contentValue.put(DatabaseContract.Language.COLUMN_ID,languageId);
        contentValue.put(DatabaseContract.Product.COLUMN_NAME,name);
        contentValue.put(DatabaseContract.Product.COLUMN_DESCRIPTION,description);

        try {
            db.insertOrThrow(DatabaseContract.Product.TABLE_PRODUCT_LANG,null,contentValue);
        } catch (SQLException e) {
            String[] whereArgs = {String.valueOf(productId)};
            String where = DatabaseContract.Product.COLUMN_ID + " = ?";
            db.update(DatabaseContract.Product.TABLE_PRODUCT_LANG,contentValue,where,whereArgs);
        }
        db.close();
    }


    /**
     * Get the list of product ids.
     *
     * @return the list of category ids
     */
    public ArrayList<Integer> getProductLangId(){
        ArrayList<Integer> list = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        Cursor c = db.query(true, DatabaseContract.Product.TABLE_PRODUCT_LANG,null,null,null,null,null,null,null);
        if(c!=null ){
            while(c.moveToNext()){
                list.add(c.getInt(c.getColumnIndex(DatabaseContract.Product.COLUMN_ID)));
            }
            c.close();
        }

        return list;
    }


    /**
     * Only used for test
     *
     * @return
     */
    public ArrayList<String> getProductLangTest(int languageId){
        ArrayList<String> list = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        String selection = DatabaseContract.Language.COLUMN_ID + "=?";
        String[] selectionArgs = {String.valueOf(languageId)};
        Cursor c = db.query(true, DatabaseContract.Product.TABLE_PRODUCT_LANG,null,selection,selectionArgs,null,null,null,null);
        if(c!=null ){
            while(c.moveToNext()){
                list.add(c.getString(c.getColumnIndex(DatabaseContract.Product.COLUMN_NAME)));
            }
            c.close();
        }

        return list;
    }
    /**
     * Get the product translation  count
     * @return the number of translation products .
     */
    public int getProductLangCount(){
        int count = -1;
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor c = db.query(DatabaseContract.Product.TABLE_PRODUCT_LANG, DatabaseContract.Product.PROJECTION_COUNT, null, null, null, null, null);
        if(c!=null){
            count = 0;
            if(c.moveToFirst()){
                count = c.getInt(c.getColumnIndex(DatabaseContract.Product.COLUMN_COUNT));
            }
            c.close();
        }
        db.close();
        return count;
    }

    /**
     * Delete the Product translation
     * @param id the id of the product translation to delete
     * @return the number of rows affected
     */
    public int deleteProductLang(int id){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        String where = DatabaseContract.Product.COLUMN_ID + " = ? ";
        String[] whereArgs = { String.valueOf(id) };

        int row = db.delete(DatabaseContract.Product.TABLE_PRODUCT_LANG, where, whereArgs);
        db.close();
        return row;
    }

    /**
     * Delete all products translation
     * @return the number of rows deleted
     */
    public int deleteAllProductLang(){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int row = db.delete(DatabaseContract.Product.TABLE_PRODUCT_LANG, null, null);
        db.close();
        return row;
    }


    // WORKING WITH MODEL
    // !! CHANGE RAW WITH CONSTANTS !!!

    /**
     * Add product no translatable fields to the table
     *
     * @param categoryId the category id
     * @param order the order
     * @return true on success, false otherwise
     */
    private boolean addProduct(SQLiteDatabase db,int categoryId,int productId,int order,String image){
        int rows;
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseContract.Category.COLUMN_ID,categoryId);
        contentValue.put(DatabaseContract.Product.COLUMN_ID,productId);
        contentValue.put(DatabaseContract.Product.COLUMN_ORDER,order);
        contentValue.put(DatabaseContract.Product.COLUMN_IMAGE,image);

        try {
            db.insertOrThrow(DatabaseContract.Product.TABLE_PRODUCT,null,contentValue);
            rows = 1;
        } catch (SQLException e) {
            String[] whereArgs = {String.valueOf(productId)};
            String where = DatabaseContract.Product.COLUMN_ID + " = ?";
            rows = db.update(DatabaseContract.Product.TABLE_PRODUCT,contentValue,where,whereArgs);
        }
        return rows !=0;
    }

    /**
     * Add language depended translation of category fields
     * @param db opened database
     * @param productId the product id
     * @param languageId the language id, one of getLanguage() id
     * @param name name of the product in the language passed as parameter
     *             @param description the description of the product passed as parameter
     */
    private void addProductLang(SQLiteDatabase db,int productId, int languageId,String name,String description){
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseContract.Product.COLUMN_ID,productId);
        contentValue.put(DatabaseContract.Language.COLUMN_ID,languageId);
        contentValue.put(DatabaseContract.Product.COLUMN_NAME,name);
        contentValue.put(DatabaseContract.Product.COLUMN_DESCRIPTION,description);

        try {
            db.insertOrThrow(DatabaseContract.Product.TABLE_PRODUCT_LANG,null,contentValue);
        } catch (SQLException e) {
            String[] whereArgs = {String.valueOf(productId)};
            String where = DatabaseContract.Product.COLUMN_ID + " = ?";
            db.update(DatabaseContract.Product.TABLE_PRODUCT_LANG,contentValue,where,whereArgs);
        }
    }

    /**
     * Add all pructs with all translations
     * @param products
     */
    public void addAllProductModel(ArrayList<ProductCmsModel> products){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.beginTransaction();

        for (ProductCmsModel p :
                products) {
            ProductCmsModel.Language[] languge = p.getLanguages();
            if(languge == null || languge.length <=0)
                continue; // sanity check

            addProduct(db,p.getCategory(),p.getId(),p.getPriority(),p.getImage());
            for (int i = 0; i < languge.length; i++) {
                addProductLang(db,p.getId(),languge[i].getId(),languge[i].getName(),languge[i].getDescription());
            }
        }

        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }
    /**
     * Get all products in the language specified as parameter
     * @param languageId the language
     * @return the products list in the language passed as parameter
     */
    public ArrayList<ProductModel> getAllProductModel(int languageId){
        ArrayList<ProductModel> list = new ArrayList<>();

        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String[] selectionArgs = {String.valueOf(languageId)};
        Cursor c = db.rawQuery(DatabaseContract.Product.RAW_SELECT_ALL, selectionArgs);
        if(c!=null){
            while (c.moveToNext()){
                list.add(ContentValueUtils.createProductModel(c));
            }
            c.close();
        }

        return list;
    }

    /**
     * Get all products in the language specified as parameter
     * @param languageId the language
     * @return the products list in the language passed as parameter
     */
    public ArrayList<ProductModel> getAllProductModel(int categoryId,int languageId){
        ArrayList<ProductModel> list = new ArrayList<>();

        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String[] selectionArgs = {String.valueOf(languageId),String.valueOf(categoryId)};
        Cursor c = db.rawQuery(DatabaseContract.Product.RAW_SELECT_BY_CATEGORY, selectionArgs);
        if(c!=null){
            while (c.moveToNext()){
                list.add(ContentValueUtils.createProductModel(c));
            }
            c.close();
        }

        return list;
    }

    /**
     * Get all products with the language passed as parameter,
     * if the translation is not found than use the default language passed as parameter
     * @param languageId the language
     * @param defaultLanguageId the language used if the first is not found
     * @return the list of categories
     */
    public ArrayList<ProductModel> getAllProductModel(int categoryId,int languageId,int defaultLanguageId){
        ArrayList<ProductModel> list = new ArrayList<>();

        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String[] selectionArgs = {String.valueOf(languageId),String.valueOf(defaultLanguageId),String.valueOf(categoryId)};
        Cursor c = db.rawQuery(DatabaseContract.Product.RAW_SELECT_BY_CATEGORY_DEFAULT, selectionArgs);
        if(c!=null){
            while (c.moveToNext()){
                list.add(ContentValueUtils.createProductModel(c));
            }
            c.close();
        }

        return list;
    }
    /**
     * Helpful method to build and return model of category in language dependent format
     * Make a join of two category's table
     *
     * @param productId Product id interested in
     * @param languageId Language id, one of getLanguage() method
     * @return The product Model in language depended format
     */
    public ProductModel getProductModel(int productId,int languageId){
        ProductModel category=null;

        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String[] selectionArgs = {String.valueOf(languageId),String.valueOf(productId)};
        Cursor c = db.rawQuery(DatabaseContract.Product.RAW_SELECT_BY_ID, selectionArgs);
        if(c!=null){
            while (c.moveToNext()){
                category = (ContentValueUtils.createProductModel(c));
            }
            c.close();
        }
        return category;
    }


    /************************************************************************************/
    /*********************************** UTILITY ****************************************/
    /************************************************************************************/



    /**
     * Util class to convert contentvalue to model and model to contentvalue
     */
    public static class ContentValueUtils{
        public static ContentValues createContentValue(@NonNull LanguageModel languageModel){
            ContentValues contentValues = new ContentValues(4);
            contentValues.put(DatabaseContract.Language.COLUMN_ID,languageModel.getId());
            contentValues.put(DatabaseContract.Language.COLUMN_ISO,languageModel.getIso());
            contentValues.put(DatabaseContract.Language.COLUMN_NAME,languageModel.getName());
            contentValues.put(DatabaseContract.Language.COLUMN_IS_DEFAULT,languageModel.getIsDefault());
            return contentValues;
        }

        public static LanguageModel createLanguageModel(Cursor c) {
            int id = c.getInt(c.getColumnIndex(DatabaseContract.Language.COLUMN_ID));
            String name = c.getString(c.getColumnIndex(DatabaseContract.Language.COLUMN_NAME));
            String iso = c.getString(c.getColumnIndex(DatabaseContract.Language.COLUMN_ISO));
            int isDefault = c.getInt(c.getColumnIndex(DatabaseContract.Language.COLUMN_IS_DEFAULT));
            return new LanguageModel(id,name,iso,isDefault);
        }

        public static CategoryModel createCategoryModel(Cursor c) {
            int id = c.getInt(c.getColumnIndex(DatabaseContract.Category.COLUMN_ID));
            String name = c.getString(c.getColumnIndex(DatabaseContract.Category.COLUMN_NAME));
            int order = c.getInt(c.getColumnIndex(DatabaseContract.Category.COLUMN_ORDER));

            return new CategoryModel(id,name,order);
        }


        public static ProductModel createProductModel(Cursor c) {
            int productId = c.getInt(c.getColumnIndex(DatabaseContract.Product.COLUMN_ID));
            int categoryId = c.getInt(c.getColumnIndex(DatabaseContract.Category.COLUMN_ID));
            int order = c.getInt(c.getColumnIndex(DatabaseContract.Product.COLUMN_ORDER));
            String image = c.getString(c.getColumnIndex(DatabaseContract.Product.COLUMN_IMAGE));
            String name = c.getString(c.getColumnIndex(DatabaseContract.Product.COLUMN_NAME));
            String descritpion = c.getString(c.getColumnIndex(DatabaseContract.Product.COLUMN_DESCRIPTION));
            return new ProductModel(productId,categoryId,name,descritpion,image,order);
        }
    }

}
