package ru.n_develop.escape_from_lesson.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.Settings;

/**
 * Created by dim90 on 0909.2017.
 */

public class DBHelper extends SQLiteOpenHelper
{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "escaping";
    public static final String TABLE_START = "start";
    public static final String TABLE_SHARE = "share";

    /**
     * Таблица с хранением информации о первом запуске
     */
    // _ нужен для работы с курсорами, это особенность android
    public static final String KEY_ID= "_id";
    public static final String KEY_USER = "user";
    public static final String KEY_IS_START = "is_start";
    public static final String KEY_IS_SEND = "is_send";

    /**
     * Таблица типо калькуляторов
     */
    // _ нужен для работы с курсорами, это особенность android
//    public static final String KEY_ID= "_id";
    public static final String KEY_COUNT_SHARE = "count";
//    public static final String KEY_IS_SEND = "is_send";



    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        Long time = System.currentTimeMillis()/1000;
        /**
         * Создаем таблицу с подкатегориями
         */
        ContentValues contentValuesStart = new ContentValues();

        db.execSQL("CREATE TABLE " + TABLE_START +
                " ( " + KEY_ID + " integer primary key, " +
                KEY_USER + " text, " +
                KEY_IS_START + " integer, " +
                KEY_IS_SEND + " integer) "
        );

        contentValuesStart.put(KEY_USER, time);
        contentValuesStart.put(KEY_IS_START, 0);
        contentValuesStart.put(KEY_IS_SEND, 0);

        db.insert(TABLE_START, null, contentValuesStart);

        /**
         * Создаем таблицу с счетчиком поделиться
         */
        ContentValues contentValuesShare = new ContentValues();

        db.execSQL("CREATE TABLE " + TABLE_SHARE +
                " ( " + KEY_ID + " integer primary key, " +
                KEY_USER + " text, " +
                KEY_COUNT_SHARE + " integer, " +
                KEY_IS_SEND + " integer) "
        );

        contentValuesStart.put(KEY_USER, time);
        contentValuesShare.put(KEY_IS_START, 0);
        contentValuesShare.put(KEY_IS_SEND, 0);

        db.insert(TABLE_SHARE, null, contentValuesShare);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("drop table if exists " + TABLE_START);
        db.execSQL("drop table if exists " + TABLE_SHARE);

        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("drop table if exists " + TABLE_START);
        db.execSQL("drop table if exists " + TABLE_SHARE);

        onCreate(db);
    }
}
