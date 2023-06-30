package com.voicetranslator.translate.languagetranslator.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.voicetranslator.translate.languagetranslator.model.Conversation;
import com.voicetranslator.translate.languagetranslator.model.History;

import java.util.ArrayList;


public class DataBaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "conversation_db";
    public static final int VERSION = 2;

    // Conversation + History Table
    public static final String CONVERSATION_TABLE = "conversation";     // Conversation table name
    public static final String HISTORY_TABLE = "history";               // History table name
    private static final String ID_COL = "id";                          // Conversation + History Field
    public static final String SOURCE_CODE = "sourceCode";              // Conversation + History Field
    public static final String SOURCE_TEXT = "sourceText";              // Conversation + History Field
    public static final String TARGET_CODE = "targetCode";              // Conversation + History Field
    public static final String TARGET_TEXT = "targetText";              // Conversation + History Field
    public static final String SOURCE_DATE = "sourceDate";              // Conversation + History Field
    public static final String TARGET_DATE = "targetDate";              // Conversation Field
    public static final String Is_SOURCE = "isSource";                  // Conversation Field --> Boolean 0 -> False, 1 -> True
    public static final String Is_FAVORITE = "isFavorite";              // History Field --> Boolean 0 -> False, 1 -> True


    public DataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        Log.d("============> DB SUC", " Database Initialize");
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try {
            String query = "CREATE TABLE " + CONVERSATION_TABLE + " ("
                    + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + SOURCE_CODE + " TEXT,"
                    + SOURCE_TEXT + " TEXT,"
                    + TARGET_CODE + " TEXT,"
                    + TARGET_TEXT + " TEXT,"
                    + SOURCE_DATE + " TEXT,"
                    + TARGET_DATE + " TEXT,"
                    + Is_SOURCE + " INTEGER)";
            sqLiteDatabase.execSQL(query);
            Log.d("============> DB SUC ", CONVERSATION_TABLE + " Table Created");
        } catch (SQLException ex) {
            Log.d("============> DB ERR ", ex.toString());
        }

        try {
            String query = "CREATE TABLE " + HISTORY_TABLE + " ("
                    + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + SOURCE_CODE + " TEXT,"
                    + SOURCE_TEXT + " TEXT,"
                    + TARGET_CODE + " TEXT,"
                    + TARGET_TEXT + " TEXT,"
                    + SOURCE_DATE + " TEXT,"
                    + Is_FAVORITE + " INTEGER)";
            sqLiteDatabase.execSQL(query);
            Log.d("============> DB SUC ", HISTORY_TABLE + " Table Created");
        } catch (SQLException ex) {
            Log.d("============> DB ERR ", ex.toString());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CONVERSATION_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + HISTORY_TABLE);
        onCreate(sqLiteDatabase);
    }


    public void addConversationData(Conversation conversation) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(SOURCE_CODE, conversation.getSourceCode());
        values.put(SOURCE_TEXT, conversation.getSourceText());
        values.put(TARGET_CODE, conversation.getTargetCode());
        values.put(TARGET_TEXT, conversation.getTargetText());
        values.put(SOURCE_DATE, conversation.getSourceDate());
        values.put(TARGET_DATE, conversation.getTargetDate());
        values.put(Is_SOURCE, conversation.getIsSource());

        db.insert(CONVERSATION_TABLE, null, values);
        Log.d("============> DB SUC", " Data Inserted");
        db.close();
    }

    @SuppressLint("Recycle")
    public ArrayList<Conversation> getAllConversation() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + CONVERSATION_TABLE, null);
        ArrayList<Conversation> list = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                list.add(new Conversation(
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getInt(7)
                ));
            } while (cursor.moveToNext());
        }
        return list;
    }


    public void addHistoryData(History history) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(SOURCE_CODE, history.getSourceCode());
        values.put(SOURCE_TEXT, history.getSourceText());
        values.put(TARGET_CODE, history.getTargetCode());
        values.put(TARGET_TEXT, history.getTargetText());
        values.put(SOURCE_DATE, history.getSourceDate());
        values.put(Is_FAVORITE, 0);

        db.insert(HISTORY_TABLE, null, values);
        Log.d("============> DB SUC", " Data Inserted");
        db.close();
    }

    @SuppressLint("Recycle")
    public ArrayList<History> getAllHistory() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + HISTORY_TABLE, null);
        ArrayList<History> list = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                list.add(new History(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getInt(6)
                ));
            } while (cursor.moveToNext());
        }
        return list;
    }

    public void updateHistoryData(int id, int isFavorite) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Is_FAVORITE, isFavorite);
        values.put(ID_COL, id);
        try {
            db.update(HISTORY_TABLE, values, ID_COL + "=? ", new String[]{String.valueOf(id)});
            Log.d("============> DB SUC", " Data Updated");
            db.close();
        } catch (SQLException ex) {
            Log.d("============> DB ERR ", ex.toString());
        }

    }


}
