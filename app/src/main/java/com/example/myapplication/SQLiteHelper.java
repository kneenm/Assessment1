package com.example.myapplication;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "assesment.db";
	public static final int DATABASE_VERSION = 1;

	public static final String TABLE_USERS = "users";
	public static final String TABLE_FAVOURITES = "favourites";


	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_COLOUR = "col";
	public static final String COLUMN_USERID = "_uid";
	public static final String COLUMN_FAVORITESID = "_fid";

	public static final String CREATE_USERS_TABLE = "create table if not exists " + TABLE_USERS + " (" + COLUMN_USERID +" integer"
			+ " ," + COLUMN_NAME + " VCHAR(20) not null, "  + COLUMN_COLOUR + " VCHAR(20));";

	public static final String CREATE_FAVOURITES_TABLE = "create table if not exists " + TABLE_FAVOURITES
			+ " (" + COLUMN_FAVORITESID +" integer"
			+ " ," + COLUMN_NAME + " VCHAR(20) not null); ";



	public SQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			db.execSQL(CREATE_USERS_TABLE);
			db.execSQL(CREATE_FAVOURITES_TABLE);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVOURITES);

		onCreate(db);
	}
}
