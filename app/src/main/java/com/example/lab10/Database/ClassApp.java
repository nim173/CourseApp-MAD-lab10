package com.example.lab10.Database;

import android.provider.BaseColumns;

public class ClassApp {
    static class User implements BaseColumns {
        static final String TABLE_NAME = "user";
        static final String COLUMN_NAME_NAME = "name";
        static final String COLUMN_NAME_PASSWORD = "password";
        static final String COLUMN_NAME_TYPE = "type";
    }

    public static final class Message implements BaseColumns {
        static final String TABLE_NAME = "message";
        public static final String COLUMN_NAME_USER = "user";
        public static final String COLUMN_NAME_SUBJECT = "subject";
        public static final String COLUMN_NAME_MESSAGE = "message";
    }
}
