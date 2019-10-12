package com.example.lab10.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.lab10.Model.MessageModel;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "classApp.db";
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQL_CREATE_USER = String.format("CREATE TABLE %s(%s TEXT PRIMARY KEY,%s TEXT,%s TEXT)", ClassApp.User.TABLE_NAME, ClassApp.User.COLUMN_NAME_NAME, ClassApp.User.COLUMN_NAME_PASSWORD, ClassApp.User.COLUMN_NAME_TYPE);
        String SQL_CREATE_MESSAGE = String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY,%s TEXT,%s TEXT,%s TEXT)", ClassApp.Message.TABLE_NAME, ClassApp.Message._ID, ClassApp.Message.COLUMN_NAME_USER, ClassApp.Message.COLUMN_NAME_SUBJECT, ClassApp.Message.COLUMN_NAME_MESSAGE);
        sqLiteDatabase.execSQL(SQL_CREATE_USER);
        sqLiteDatabase.execSQL(SQL_CREATE_MESSAGE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(String.format("DROP TABLE IF EXISTS %s", ClassApp.Message.TABLE_NAME));
        sqLiteDatabase.execSQL(String.format("DROP TABLE IF EXISTS %s", ClassApp.User.TABLE_NAME));
        onCreate(sqLiteDatabase);
    }

    public boolean insertUser(String username, String password, String type) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ClassApp.User.COLUMN_NAME_NAME, username);
        contentValues.put(ClassApp.User.COLUMN_NAME_PASSWORD, password);
        contentValues.put(ClassApp.User.COLUMN_NAME_TYPE, type);
        return sqLiteDatabase.insert(ClassApp.User.TABLE_NAME, null, contentValues) >= 1;
    }

    public boolean insertMessage(String user, String subject, String message) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ClassApp.Message.COLUMN_NAME_USER, user);
        contentValues.put(ClassApp.Message.COLUMN_NAME_SUBJECT, subject);
        contentValues.put(ClassApp.Message.COLUMN_NAME_MESSAGE, message);
        return sqLiteDatabase.insert(ClassApp.Message.TABLE_NAME,null, contentValues) >= 1;
    }

//    public List readAllMessages() {
//        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
//        String[] projection = { ClassApp.Message._ID, ClassApp.Message.COLUMN_NAME_USER, ClassApp.Message.COLUMN_NAME_SUBJECT, ClassApp.Message.COLUMN_NAME_MESSAGE};
//        String sortOrder = ClassApp.Message._ID + " DESC";
//        Cursor cursor = sqLiteDatabase.query(
//                ClassApp.Message.TABLE_NAME,
//                projection,
//                null,
//                null,
//                null,
//                null,
//                sortOrder
//        );
//        List<MessageModel> list = new ArrayList<>();
//        while (cursor.moveToNext()) {
//            MessageModel messageModel = new MessageModel();
//            messageModel.setId(cursor.getInt(cursor.getColumnIndexOrThrow(ClassApp.Message._ID)));
//            messageModel.setUser(cursor.getString(cursor.getColumnIndexOrThrow(ClassApp.Message.COLUMN_NAME_USER)));
//            messageModel.setSubject(cursor.getString(cursor.getColumnIndexOrThrow(ClassApp.Message.COLUMN_NAME_SUBJECT)));
//            messageModel.setMessage(cursor.getString(cursor.getColumnIndexOrThrow(ClassApp.Message.COLUMN_NAME_MESSAGE)));
//            list.add(messageModel);
//        }
//        cursor.close();
//        return list;
//    }

    public Cursor getMessagesCursor() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String[] projection = { ClassApp.Message._ID, ClassApp.Message.COLUMN_NAME_USER, ClassApp.Message.COLUMN_NAME_SUBJECT, ClassApp.Message.COLUMN_NAME_MESSAGE};
        String sortOrder = ClassApp.Message._ID + " DESC";
        return sqLiteDatabase.query(
                ClassApp.Message.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );
    }

    public int readUser(String username, String password) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String[] projection = {ClassApp.User.COLUMN_NAME_TYPE};
        String selection = ClassApp.User.COLUMN_NAME_NAME + " = ? AND " + ClassApp.User.COLUMN_NAME_PASSWORD + " = ?";
        String[] selectionArgs = {username, password};
        Cursor cursor = sqLiteDatabase.query(
                ClassApp.User.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            if ("Teacher".equals(cursor.getString(cursor.getColumnIndexOrThrow(ClassApp.User.COLUMN_NAME_TYPE)))) {
                return 1;
            } else if ("Student".equals(cursor.getString(cursor.getColumnIndexOrThrow(ClassApp.User.COLUMN_NAME_TYPE)))) {
                return 2;
            } else {
                return 0;
            }
        } else {
            cursor.close();
            return 0;
        }
    }

    public MessageModel readMessage(int id) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String[] projection = { ClassApp.Message._ID, ClassApp.Message.COLUMN_NAME_USER, ClassApp.Message.COLUMN_NAME_SUBJECT, ClassApp.Message.COLUMN_NAME_MESSAGE};
        String selection = ClassApp.Message._ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = sqLiteDatabase.query(
                ClassApp.Message.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        cursor.moveToFirst();
        MessageModel messageModel = new MessageModel();
        messageModel.setId(id);
        messageModel.setUser(cursor.getString(cursor.getColumnIndexOrThrow(ClassApp.Message.COLUMN_NAME_USER)));
        messageModel.setSubject(cursor.getString(cursor.getColumnIndexOrThrow(ClassApp.Message.COLUMN_NAME_SUBJECT)));
        messageModel.setMessage(cursor.getString(cursor.getColumnIndexOrThrow(ClassApp.Message.COLUMN_NAME_MESSAGE)));
        cursor.close();
        return messageModel;
    }

    public boolean validateUsername(String username) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String[] projection = {ClassApp.User.COLUMN_NAME_NAME};
        Cursor cursor = sqLiteDatabase.query(
                ClassApp.User.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );
        while (cursor.moveToNext()) {
            if (username.equals(cursor.getString(cursor.getColumnIndexOrThrow(ClassApp.User.COLUMN_NAME_NAME)))) {
                cursor.close();
                return false;       // invalid username
            }
        }
        return true;    // valid username
    }

    public MessageModel getLastMessage() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(String.format("SELECT MAX(%s) FROM %s", ClassApp.Message._ID, ClassApp.Message.TABLE_NAME), null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            int maxId = cursor.getInt(0);
            return readMessage(maxId);
        } else {
            cursor.close();
            return null;
        }
    }
}
