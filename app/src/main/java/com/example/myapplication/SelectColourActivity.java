package com.example.myapplication;

import android.app.Activity;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.myapplication.adapters.AppsAdapter;
import com.example.myapplication.adapters.FavouratesAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SelectColourActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setResult(RESULT_CANCELED);

        loadData();
    }

    public void loadData() {
        try {
            ArrayList<String> colors = new ArrayList<>();

            for (int i = 0; i < assesmentApplication.mFavourites.size(); i++) {
                String col = assesmentApplication.mFavourites.get(i).name;
                if (!colors.contains(col))
                    colors.add(col);
            }


            final ListView listView = (ListView) findViewById(R.id.listViewMain);
            final FavouratesAdapter appsAdapter = new FavouratesAdapter(this, colors);
            listView.setAdapter(appsAdapter);

            Bundle extras = getIntent().getExtras();
            int personID = extras.getInt("PERSONID", 0);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    String sql = "IF EXISTS (Select * FROM " + SQLiteHelper.TABLE_FAVOURITES +
//                            " WHERE id = " + personID + ") " +
//                            "BEGIN " +
//                            "UPDATE " + SQLiteHelper.TABLE_FAVOURITES + " SET name = '" + colors.get(position) + "' " +
//                            "WHERE id = " + personID +
//                            " END" +
//                            " ELSE" +
//                            " BEGIN" +
//                            " INSET INTO " + SQLiteHelper.TABLE_FAVOURITES + " (id, name) VALUES(" + personID + ", name = "+
//                            "'" + colors.get(position) + "') " +
//                            " END";

                    String sql = "insert or replace into " + SQLiteHelper.TABLE_FAVOURITES + " (_fid, name) values (" +
                            +  personID + ", '" +colors.get(position) +"')";

                    SQLiteHelper helper = new SQLiteHelper(SelectColourActivity.this);

                    try (SQLiteDatabase db = helper.getWritableDatabase()) {
                        // Insert the new entry into the DB.
                        db.execSQL(sql);

                        setResult(RESULT_OK);
                        finish();

                    } catch (SQLException sqle) {

                        Log.e("AAAA", sqle.getLocalizedMessage());

                    }



                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
