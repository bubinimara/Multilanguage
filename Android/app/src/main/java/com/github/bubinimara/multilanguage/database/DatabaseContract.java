package com.github.bubinimara.multilanguage.database;

import android.provider.BaseColumns;

/**
 * Created by davide on 2/08/16.
 */
public final class DatabaseContract {
    private DatabaseContract() { }

    /**
     * Define columns and constants common to all tables
     */
    private interface ContractBaseColumns extends BaseColumns{

        // Timestamp
        String COLUMN_FLAG = "flag";

        // update time
        String COLUMN_UPDATE = "updateTime";

        // count column
        String COLUMN_COUNT = "count";

        // column for ordering
        public static final String COLUMN_ORDER = "priority";


        public static final String DEFAULT_SORT_ORDER = COLUMN_ORDER + " ASC";

        String PROJECTION_COUNT[] = {
                " COUNT(*) AS count "
        };

    }

    /**
     * Define Language Contracts
     */
    public static final class Language implements ContractBaseColumns {

        // table name
        static final String TABLE_LANGUAGE = "languages";
        // id
        public static final String COLUMN_ID = "languageId";

        // Displayed name
        public static final String COLUMN_NAME = "name";

        // ISO code of language
        public static final String COLUMN_ISO = "iso";

        public static final String COLUMN_IS_DEFAULT = "isDefault";

        // Commons
        public static final String PROJECTION_BASE[] = {
                _ID, COLUMN_ID, COLUMN_NAME
        };
        public static final String DEFAULT_SORT_ORDER = COLUMN_ID + " ASC";
    }

    /**
     * Define Category Contracts
     * It is an union of two tables, Category and CategoryLang
     */
    public static final class Category implements ContractBaseColumns {
        // Table name
        static final String TABLE_CATEGORY = "category";

        // Table for translation
        static final String TABLE_CATEGORY_LANG = "categoryLang";

        // Identifier
        public static final String COLUMN_ID = "categoryId";

        // Name of the category
        public static final String COLUMN_NAME = "name";

        /**
         * Select all categories with the language passed as argument.
         * If there aren't translation a null field is returned
         */
        public static final String RAW_SELECT = "SELECT c.categoryId, c.priority," +
                " tr.name " +
                "FROM category c " +
                "LEFT OUTER JOIN categoryLang tr " +
                "ON c.categoryId=tr.categoryId and tr.languageId=? "+
                " WHERE c.categoryId=?";

        /**
         * Raw query to select all categories
         */
        public static final String RAW_SELECT_DEFAULT_LANGUAGE = "SELECT c.categoryId, c.priority," +
                " IFNULL(tr.name,df.name) name " +
                "FROM category c " +
                "LEFT OUTER JOIN categoryLang tr " +
                "ON c.categoryId=tr.categoryId and tr.languageId=? "+
                " LEFT OUTER JOIN categoryLang df " +
                "On c.categoryId = df.categoryId and df.languageId=? "+
                " ORDER BY c.priority";


    }

    /**
     * Define Product Contracts
     * It is an union of two tables, Category and CategoryLang
     */
    public static final class Product implements ContractBaseColumns {
        // Table name
        static final String TABLE_PRODUCT = "product";

        // Table name for translation
        static final String TABLE_PRODUCT_LANG = "productLang";

        // Identifier
        public static final String COLUMN_ID = "productId";

        public static final String COLUMN_NAME = "name";

        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_IMAGE = "image";

        public static final String RAW_SELECT_ALL = "SELECT p.categoryId, p.productId, p.priority, p.image, " +
                " tr.name, tr.description " +
                "FROM product p " +
                "LEFT OUTER JOIN productLang tr " +
                "ON p.productId=tr.productId and tr.languageId=? "+
                " ORDER BY p.priority";

        public static final String  RAW_SELECT_BY_CATEGORY = "SELECT p.categoryId, p.productId, p.priority, p.image, " +
                " tr.name, tr.description " +
                "FROM product p " +
                "LEFT OUTER JOIN productLang tr " +
                "ON p.productId=tr.productId and tr.languageId=? "+
                " WHERE p.categoryId = ? "+
                " ORDER BY p.priority";

        public static final String RAW_SELECT_BY_CATEGORY_DEFAULT = "SELECT p.categoryId, p.productId, p.priority, p.image, " +
                " IFNULL(tr.name,df.name) name, " +
                " IFNULL(tr.description,df.description) description " +
                "FROM product p " +
                "LEFT OUTER JOIN productLang tr " +
                "ON p.productId=tr.productId and tr.languageId=? "+
                " LEFT OUTER JOIN productLang df " +
                "On p.productId = df.productId and df.languageId=? "+
                " WHERE p.categoryId=?"+
                " ORDER BY p.priority";

        public static final String  RAW_SELECT_BY_ID = "SELECT p.categoryId, p.productId, p.priority, p.image, " +
                " tr.name, tr.description " +
                "FROM product p " +
                "LEFT OUTER JOIN productLang tr " +
                "ON p.productId=tr.productId and tr.languageId=? "+
                " WHERE p.productId=?";

    }

}
