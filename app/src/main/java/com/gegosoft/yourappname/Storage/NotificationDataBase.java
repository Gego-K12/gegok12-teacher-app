package com.gegosoft.yourappname.Storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.gegosoft.yourappname.Models.NotificationPoJo;

public class NotificationDataBase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Notification DB";
    private static final String TABLE_NAME = "Notification_Table";

    private static final int DATABASE_VERSION = 1;

    private static final String KEY_ID = "_id";
    private static final String NOTIFICATION_EVENT = "event_notification";
    private static final String NOTIFICATION_GALLERY=  "gallery_notification";
    private static final String NOTIFICATION_MEETING = "meeting_notification";
    private static final String NOTIFICATION_MESSAGE= "message_notification";

    private static final String NOTIFICATION_MAGAZINE = "magzine_notification";

    private static final String NOTIFICATION_GROUPS = "groups_notification";
    private static final String NOTIFICATION_BIRTHDAY = "birthday_notification";
    private String SQL_CREATE_TABLE = " CREATE TABLE "+ TABLE_NAME +
            "(" +
            KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            NOTIFICATION_EVENT + " TEXT, " +
            NOTIFICATION_GALLERY + " TEXT, " +
            NOTIFICATION_MEETING + " TEXT, " +
            NOTIFICATION_MESSAGE + " TEXT, " +

            NOTIFICATION_MAGAZINE + " TEXT, " +
            NOTIFICATION_GROUPS + " TEXT, " +

            NOTIFICATION_BIRTHDAY + " TEXT" +


            ")";


    public NotificationDataBase( Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addMessageNotification(NotificationPoJo notificationPoJo){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NOTIFICATION_MESSAGE, notificationPoJo.isPrivateMessage());

        db.insert(TABLE_NAME,null,values);
        db.close();

    }
    public NotificationPoJo getIsPrivateMessage(){
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        NotificationPoJo  notificationPoJo  = new NotificationPoJo();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);;

        return notificationPoJo;
    }
}
