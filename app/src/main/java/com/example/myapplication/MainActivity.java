package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.myapplication.adapters.AppsAdapter;
import com.example.myapplication.classes.favourites;
import com.example.myapplication.classes.names;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    SQLiteHelper mHelper;
    int mPosition = 0;


    private static final String[] COUNT_COLUMN = {"COUNT(1)"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHelper = new SQLiteHelper(this);

        if (!hasUsers()) {
            insertUsers();
        }
        if (!hasFavourites()) {
            insertFavourites();
        }

        loadData();

    }

    public boolean hasUsers() {
        try (SQLiteDatabase db = mHelper.getReadableDatabase();
             Cursor cursor = db.query(mHelper.TABLE_USERS, COUNT_COLUMN, null, null, null, null, null)) {
            cursor.moveToFirst();
            return cursor.getInt(0) > 0;
        } catch (SQLException sqle) {
            return false;
        }
    }

    public boolean hasFavourites() {
        try (SQLiteDatabase db = mHelper.getReadableDatabase();
             Cursor cursor = db.query(mHelper.TABLE_FAVOURITES, COUNT_COLUMN, null, null, null, null, null)) {
            cursor.moveToFirst();
            return cursor.getInt(0) > 0;
        } catch (SQLException sqle) {
            return false;
        }
    }

    private void insertUsers() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(getAssets().open("users.txt")));

            String mLine;
            while ((mLine = reader.readLine()) != null) {
                String line = mLine.trim().replace("\t", " ");
                int pos = line.indexOf(" ");

                int id = Integer.parseInt(line.trim().substring(0, pos));
                String name = line.substring(pos).trim();


                SQLiteHelper helper = new SQLiteHelper(this);

                String stat = "INSERT INTO " + helper.TABLE_USERS + " ("
                        + SQLiteHelper.COLUMN_USERID + ", "
                        + SQLiteHelper.COLUMN_NAME + ", "
                        + SQLiteHelper.COLUMN_COLOUR
                        + ") Values ('" + id + "' , '" + name + "', 'none')";

                try (SQLiteDatabase db = helper.getWritableDatabase()) {
                    // Insert the new entry into the DB.
                    db.execSQL(stat);
                } catch (SQLException sqle) {

                    sqle.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void insertFavourites() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(getAssets().open("favourites.txt")));

            String mLine;
            while ((mLine = reader.readLine()) != null) {
                String line = mLine.trim().replace("\t", " ");
                int pos = line.indexOf(" ");

                int id = Integer.parseInt(line.trim().substring(0, pos));
                String name = line.substring(pos).trim();


                SQLiteHelper helper = new SQLiteHelper(this);

                String stat = "INSERT INTO " + helper.TABLE_FAVOURITES + " ("
                        + SQLiteHelper.COLUMN_FAVORITESID + ", "
                        + SQLiteHelper.COLUMN_NAME
                        + ") Values ('" + id + "' , '" + name + "')";

                try (SQLiteDatabase db = helper.getWritableDatabase()) {
                    // Insert the new entry into the DB.
                    db.execSQL(stat);
                } catch (SQLException sqle) {

                    Log.e("AAAA", sqle.getLocalizedMessage());

                }
            }
        } catch (IOException e) {
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
        }
    }

    private void loadData() {
        List<favourites> favs = new ArrayList<>();
        List<Integer> favsID = new ArrayList<>();
        List<String> favsName = new ArrayList<>();
        try (SQLiteDatabase db = mHelper.getReadableDatabase();
             Cursor cursor = db.query(mHelper.TABLE_FAVOURITES, null, null, null, null, null, SQLiteHelper.COLUMN_NAME)) {
            cursor.moveToFirst();
            do {
                int fID = cursor.getInt(0);
                String colour = cursor.getString(1);

                favourites fav = new favourites();
                fav.id = fID;
                fav.name = colour;

                favs.add(fav);

                favsID.add(fID);
                favsName.add(colour);

            } while (cursor.moveToNext());
        } catch (SQLException sqle) {
        }

        assesmentApplication.mFavourites = favs;


        List<names> users = new ArrayList<>();
        try (SQLiteDatabase db = mHelper.getReadableDatabase();
             Cursor cursor = db.query(mHelper.TABLE_USERS, null, null, null, null, null, SQLiteHelper.COLUMN_NAME)) {
            cursor.moveToFirst();
            do {
                int fID = cursor.getInt(0);
                String colour = cursor.getString(1);

                names name = new names();
                name.id = fID;
                name.name = colour;

                int pos = favsID.indexOf(fID);

                try {
                    String col = favs.get(pos).name;
                    name.colour = col;

                } catch (Exception e) {

                }

                users.add(name);

            } while (cursor.moveToNext());
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }


        assesmentApplication.mUsers = users;

        try {

            final ListView listView = (ListView) findViewById(R.id.listViewMain);
            final AppsAdapter appsAdapter = new AppsAdapter(this, users);
            listView.setAdapter(appsAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    mPosition = position;
                    
                    Intent intent = new Intent(MainActivity.this, SelectColourActivity.class);
                    intent.putExtra("PERSONID", assesmentApplication.mUsers.get(position).id);

                    startActivityForResult(intent, 100);

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 100) {
            loadData();
        }
    }
}