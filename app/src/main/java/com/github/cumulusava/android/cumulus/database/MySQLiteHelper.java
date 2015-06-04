package com.github.cumulusava.android.cumulus.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Magnus on 2015-05-02
 * SQLiteHelper class to override upgrading and creating database table
 */
class MySQLiteHelper extends SQLiteOpenHelper{

    public static final String TABLE_ARTICLES = "articles";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_ARTICLE = "article";
    public static final String COLUMN_LINK = "link";

    private static final String DATABASE_NAME = "articles.db";
    private static final int DATABASE_VERSION = 1;

    //Creating database table with all columns
    private static final String DATABASE_CREATE = "create table "
            + TABLE_ARTICLES + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_ARTICLE
            + " text not null, " + COLUMN_LINK
            + " text not null);";

    public MySQLiteHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override /** Creates database */
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override /** Upgrades database */
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ARTICLES);
        onCreate(db);
    }
}