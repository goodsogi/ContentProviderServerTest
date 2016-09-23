package com.commax.contentproviderservertest.sqlite;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.commax.contentproviderservertest.Constants;


public class SIPDeviceInfoProvider extends ContentProvider {
	
	private static final UriMatcher sUriMatcher = buildUriMatcher();
	private DBHelper mOpenHelper;

	// Codes for the UriMatcher //////
	private static final int DEVICE = 100;
	private static final int DEVICE_WITH_ID = 200;
	private static final int MY_DEVICE = 300;
	private static final int MY_DEVICE_WITH_ID = 400;
	private static final int CONFIGURE = 500;
	private static final int CONFIGURE_WITH_ID = 600;
	////////

	private static UriMatcher buildUriMatcher(){
		// Build a UriMatcher by adding a specific code to return based on a match
		// It's common to use NO_MATCH as the code for this case.
		final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
		final String authority = DBConstants.CONTENT_AUTHORITY;

		// add a code for each type of URI you want
		matcher.addURI(authority, DBConstants.DeviceEntry.TABLE_NAME, DEVICE);
		matcher.addURI(authority, DBConstants.DeviceEntry.TABLE_NAME + "/#", DEVICE_WITH_ID);
		matcher.addURI(authority, DBConstants.MyDeviceEntry.TABLE_NAME, MY_DEVICE);
		matcher.addURI(authority, DBConstants.MyDeviceEntry.TABLE_NAME + "/#", MY_DEVICE_WITH_ID);
		matcher.addURI(authority, DBConstants.ConfigureEntry.TABLE_NAME, CONFIGURE);
		matcher.addURI(authority, DBConstants.ConfigureEntry.TABLE_NAME + "/#", CONFIGURE_WITH_ID);

		return matcher;	
	}

	@Override
	public boolean onCreate(){
		mOpenHelper = new DBHelper(getContext());

		return true;
	}

	@Override
	public String getType(Uri uri){
		final int match = sUriMatcher.match(uri);

		switch (match){
			case DEVICE:{
				return DBConstants.DeviceEntry.CONTENT_DIR_TYPE;
			}
			case DEVICE_WITH_ID:{ 
				return DBConstants.DeviceEntry.CONTENT_ITEM_TYPE;
			}
			case MY_DEVICE:{
				return DBConstants.MyDeviceEntry.CONTENT_DIR_TYPE;
			}
			case MY_DEVICE_WITH_ID:{
				return DBConstants.MyDeviceEntry.CONTENT_ITEM_TYPE;
			}
			case CONFIGURE:{
				return DBConstants.ConfigureEntry.CONTENT_DIR_TYPE;
			}
			case CONFIGURE_WITH_ID:{
				return DBConstants.ConfigureEntry.CONTENT_ITEM_TYPE;
			}
			default:{
				throw new UnsupportedOperationException("Unknown uri: " + uri);
			}
		}
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder){
		Cursor retCursor;
		switch(sUriMatcher.match(uri)){
			// All Flavors selected
			case DEVICE:{    
				retCursor = mOpenHelper.getReadableDatabase().query(
						DBConstants.DeviceEntry.TABLE_NAME,
						projection,
						selection,
						selectionArgs,
						null,
						null,
						sortOrder);
				return retCursor;
			}
			// Individual DEVICE based on Id selected
			case DEVICE_WITH_ID:{
				retCursor = mOpenHelper.getReadableDatabase().query(
						DBConstants.DeviceEntry.TABLE_NAME,
						projection,
						DBConstants.DeviceEntry._ID + " = ?",
						new String[] {String.valueOf(ContentUris.parseId(uri))},
						null,
						null,
						sortOrder);
				return retCursor;
			}
			// All Flavors selected
			case MY_DEVICE:{
				retCursor = mOpenHelper.getReadableDatabase().query(
						DBConstants.MyDeviceEntry.TABLE_NAME,
						projection,
						selection,
						selectionArgs,
						null,
						null,
						sortOrder);
				return retCursor;
			}
			// Individual DEVICE based on Id selected
			case MY_DEVICE_WITH_ID:{
				retCursor = mOpenHelper.getReadableDatabase().query(
						DBConstants.MyDeviceEntry.TABLE_NAME,
						projection,
						DBConstants.MyDeviceEntry._ID + " = ?",
						new String[] {String.valueOf(ContentUris.parseId(uri))},
						null,
						null,
						sortOrder);
				return retCursor;
			}
			// All Flavors selected
			case CONFIGURE:{
				retCursor = mOpenHelper.getReadableDatabase().query(
						DBConstants.ConfigureEntry.TABLE_NAME,
						projection,
						selection,
						selectionArgs,
						null,
						null,
						sortOrder);
				return retCursor;
			}
			// Individual DEVICE based on Id selected
			case CONFIGURE_WITH_ID:{
				retCursor = mOpenHelper.getReadableDatabase().query(
						DBConstants.ConfigureEntry.TABLE_NAME,
						projection,
						DBConstants.ConfigureEntry._ID + " = ?",
						new String[] {String.valueOf(ContentUris.parseId(uri))},
						null,
						null,
						sortOrder);
				return retCursor;
			}
			default:{
				// By default, we assume a bad URI
				throw new UnsupportedOperationException("Unknown uri: " + uri);
			}
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values){
		final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		Uri returnUri;
		switch (sUriMatcher.match(uri)) {
			case DEVICE: {
				long _id = db.insert(DBConstants.DeviceEntry.TABLE_NAME, null, values);
				// insert unless it is already contained in the database
				if (_id > 0) {
					returnUri = DBConstants.DeviceEntry.buildFlavorsUri(_id);
				} else {
					throw new android.database.SQLException("Failed to insert row into: " + uri);
				}
				break;
			}
			case MY_DEVICE: {
				long _id = db.insert(DBConstants.MyDeviceEntry.TABLE_NAME, null, values);
				// insert unless it is already contained in the database
				if (_id > 0) {
					returnUri = DBConstants.MyDeviceEntry.buildFlavorsUri(_id);
				} else {
					throw new android.database.SQLException("Failed to insert row into: " + uri);
				}
				break;
			}
			case CONFIGURE: {
				long _id = db.insert(DBConstants.ConfigureEntry.TABLE_NAME, null, values);
				// insert unless it is already contained in the database
				if (_id > 0) {
					returnUri = DBConstants.ConfigureEntry.buildFlavorsUri(_id);
				} else {
					throw new android.database.SQLException("Failed to insert row into: " + uri);
				}
				break;
			}

			default: {
				throw new UnsupportedOperationException("Unknown uri: " + uri);

			}
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return returnUri;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs){
		final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		final int match = sUriMatcher.match(uri);
		int numDeleted;
		switch(match){
			case DEVICE:
				numDeleted = db.delete(
						DBConstants.DeviceEntry.TABLE_NAME, selection, selectionArgs);
				// reset _ID
				db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
						DBConstants.DeviceEntry.TABLE_NAME + "'");
				break;
			case DEVICE_WITH_ID:
				numDeleted = db.delete(DBConstants.DeviceEntry.TABLE_NAME,
						DBConstants.DeviceEntry._ID + " = ?",
						new String[]{String.valueOf(ContentUris.parseId(uri))});
				// reset _ID
				db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + 
						DBConstants.DeviceEntry.TABLE_NAME + "'");

				break;
			case MY_DEVICE:
				numDeleted = db.delete(
						DBConstants.MyDeviceEntry.TABLE_NAME, selection, selectionArgs);
				// reset _ID
				db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
						DBConstants.MyDeviceEntry.TABLE_NAME + "'");
				break;
			case MY_DEVICE_WITH_ID:
				numDeleted = db.delete(DBConstants.MyDeviceEntry.TABLE_NAME,
						DBConstants.MyDeviceEntry._ID + " = ?",
						new String[]{String.valueOf(ContentUris.parseId(uri))});
				// reset _ID
				db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
						DBConstants.MyDeviceEntry.TABLE_NAME + "'");

				break;
			case CONFIGURE:
				numDeleted = db.delete(
						DBConstants.ConfigureEntry.TABLE_NAME, selection, selectionArgs);
				// reset _ID
				db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
						DBConstants.ConfigureEntry.TABLE_NAME + "'");
				break;
			case CONFIGURE_WITH_ID:
				numDeleted = db.delete(DBConstants.ConfigureEntry.TABLE_NAME,
						DBConstants.ConfigureEntry._ID + " = ?",
						new String[]{String.valueOf(ContentUris.parseId(uri))});
				// reset _ID
				db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
						DBConstants.ConfigureEntry.TABLE_NAME + "'");

				break;
			default:
				throw new UnsupportedOperationException("Unknown uri: " + uri);
		}

		return numDeleted;
	}

	@Override
	public int bulkInsert(Uri uri, ContentValues[] values){
		final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		final int match = sUriMatcher.match(uri);
		switch(match){
			case DEVICE:
				// allows for multiple transactions
				db.beginTransaction();

				// keep track of successful inserts
				int numInserted = 0;
				try{
					for(ContentValues value : values){
						if (value == null){
							throw new IllegalArgumentException("Cannot have null content values");
						}
						long _id = -1;
						try{
							_id = db.insertOrThrow(DBConstants.DeviceEntry.TABLE_NAME,
									null, value);
						}catch(SQLiteConstraintException e) {
							Log.w(Constants.LOG_TAG, "Attempting to insert " +
									value.getAsString(
											DBConstants.DeviceEntry.COLUMN_NAME_DEVICE_TYPE)
									+ " but value is already in database.");
						}
						if (_id != -1){
							numInserted++;
						}
					}
					if(numInserted > 0){
						// If no errors, declare a successful transaction.
						// database will not populate if this is not called
						db.setTransactionSuccessful();
					}
				} finally {
					// all transactions occur at once
					db.endTransaction();
				}
				if (numInserted > 0){
					// if there was successful insertion, notify the content resolver that there
					// was a change
					getContext().getContentResolver().notifyChange(uri, null);
				}
				return numInserted;
			default:
				return super.bulkInsert(uri, values);
		}
	}

	@Override
	public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs){
		final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		int numUpdated = 0;

		if (contentValues == null){
			throw new IllegalArgumentException("Cannot have null content values");
		}

		switch(sUriMatcher.match(uri)){
			case DEVICE:{
				numUpdated = db.update(DBConstants.DeviceEntry.TABLE_NAME,
						contentValues,
						selection,
						selectionArgs);
				break;
			}
			case DEVICE_WITH_ID: {
				numUpdated = db.update(DBConstants.DeviceEntry.TABLE_NAME,
						contentValues,
						DBConstants.DeviceEntry._ID + " = ?",
						new String[] {String.valueOf(ContentUris.parseId(uri))});
				break;
			}
			case MY_DEVICE:{
				numUpdated = db.update(DBConstants.MyDeviceEntry.TABLE_NAME,
						contentValues,
						selection,
						selectionArgs);
				break;
			}
			case MY_DEVICE_WITH_ID: {
				numUpdated = db.update(DBConstants.MyDeviceEntry.TABLE_NAME,
						contentValues,
						DBConstants.MyDeviceEntry._ID + " = ?",
						new String[] {String.valueOf(ContentUris.parseId(uri))});
				break;
			}
			case CONFIGURE:{
				numUpdated = db.update(DBConstants.ConfigureEntry.TABLE_NAME,
						contentValues,
						selection,
						selectionArgs);
				break;
			}
			case CONFIGURE_WITH_ID: {
				numUpdated = db.update(DBConstants.ConfigureEntry.TABLE_NAME,
						contentValues,
						DBConstants.ConfigureEntry._ID + " = ?",
						new String[] {String.valueOf(ContentUris.parseId(uri))});
				break;
			}
			default:{
				throw new UnsupportedOperationException("Unknown uri: " + uri);
			}
		}

		if (numUpdated > 0){
			getContext().getContentResolver().notifyChange(uri, null);
		}

		return numUpdated;
	}




}
