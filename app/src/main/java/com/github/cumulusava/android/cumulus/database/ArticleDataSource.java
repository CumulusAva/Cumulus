package com.github.cumulusava.android.cumulus.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Magnus on 2015-05-02
 * DatabaseSource to handle the SQLiteDatabase, methods to open, close, read, write etc.
 */
public class ArticleDataSource {

    //Database fields
    private SQLiteDatabase database;
    private final MySQLiteHelper dbHelper;
    private static final String[] allColumns = { MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_ARTICLE, MySQLiteHelper.COLUMN_LINK};

    public ArticleDataSource(Context context){
        dbHelper = new MySQLiteHelper(context);
    }

    /** Opens the database to be able to read and write */
    public void open() throws SQLiteException {
        database = dbHelper.getWritableDatabase();
    }

    /** Closes database again */
    public void close() {
        dbHelper.close();
    }

    public Article createArticle(String article, String link){
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_ARTICLE, article);
        values.put(MySQLiteHelper.COLUMN_LINK, link);
        long insertId = database.insert(MySQLiteHelper.TABLE_ARTICLES, null, values);

        Cursor cursor = database.query(MySQLiteHelper.TABLE_ARTICLES,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Article newArticle = cursorToArticle(cursor);
        cursor.close();
        return newArticle;
    }

    /** Delete single article */
    public void deleteArticle(Article article) {
        database.delete(MySQLiteHelper.TABLE_ARTICLES, MySQLiteHelper.COLUMN_ID
                + " = " + article.getId(), null);
    }

    /** Get list with all saved articles */
    public List<Article> getAllArticles(){
        List<Article> articles = new ArrayList<>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_ARTICLES, allColumns,
                null, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Article article = cursorToArticle(cursor);
            articles.add(article);
            cursor.moveToNext();
        }

        cursor.close();
        return articles;
    }

    /** Return article-object from a cursor */
    private Article cursorToArticle(Cursor cursor) {
        return new Article(cursor.getLong(0), cursor.getString(1), cursor.getString(2));
    }
}