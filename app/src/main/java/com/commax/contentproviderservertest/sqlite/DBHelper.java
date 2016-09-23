package com.commax.contentproviderservertest.sqlite;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.commax.contentproviderservertest.sqlite.DBConstants.*;


/**
 * 디비 생성과 제거를 하는 클래스
 *
 * @author Jeonggyu Park
 */
class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "SIPDeviceInfo.db";
    private static final int DATABASE_VERSION = 2;

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_DEVICE_TABLE = "CREATE TABLE "
            + DeviceEntry.TABLE_NAME + " (" + DeviceEntry._ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT " + COMMA_SEP
            + DeviceEntry.COLUMN_NAME_DEVICE_TYPE + TEXT_TYPE + COMMA_SEP
            + DeviceEntry.COLUMN_NAME_IP_ADDRESS + TEXT_TYPE + COMMA_SEP
            + DeviceEntry.COLUMN_NAME_SIP_PHONE_NO + TEXT_TYPE + " )";

    private static final String SQL_CREATE_MYDEVICE_TABLE = "CREATE TABLE "
            + MyDeviceEntry.TABLE_NAME + " (" + MyDeviceEntry._ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT " + COMMA_SEP
            + MyDeviceEntry.COLUMN_NAME_DEVICE_TYPE + TEXT_TYPE + COMMA_SEP
            + MyDeviceEntry.COLUMN_NAME_IP_ADDRESS + TEXT_TYPE + COMMA_SEP
            + MyDeviceEntry.COLUMN_NAME_SIP_PHONE_NO + TEXT_TYPE + " )";

    private static final String SQL_CREATE_CONFIGURE_TABLE = "CREATE TABLE "
            + ConfigureEntry.TABLE_NAME + " (" + ConfigureEntry._ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT " + COMMA_SEP
            + ConfigureEntry.COLUMN_NAME_IS_CONFIGURED + TEXT_TYPE + " )";



    private static final String SQL_DELETE_DEVICE_TABLE = "DROP TABLE IF EXISTS "
            + DeviceEntry.TABLE_NAME;

    private static final String SQL_DELETE_MYDEVICE_TABLE = "DROP TABLE IF EXISTS "
            + MyDeviceEntry.TABLE_NAME;


    private static final String SQL_DELETE_CONFIGURE_TABLE = "DROP TABLE IF EXISTS "
            + ConfigureEntry.TABLE_NAME;



    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(SQL_CREATE_DEVICE_TABLE);
        db.execSQL(SQL_CREATE_MYDEVICE_TABLE);
        db.execSQL(SQL_CREATE_CONFIGURE_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 기존 테이블 삭제
        db.execSQL(SQL_DELETE_DEVICE_TABLE);
        db.execSQL(SQL_DELETE_MYDEVICE_TABLE);
        db.execSQL(SQL_DELETE_CONFIGURE_TABLE);


        // 새로 DB 생성
        onCreate(db);

    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}