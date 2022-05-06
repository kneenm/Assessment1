package com.example.myapplication;

import android.app.Application;

import com.example.myapplication.classes.favourites;
import com.example.myapplication.classes.names;

import java.util.List;

public class assesmentApplication extends Application {

    public static List <favourites> mFavourites;
    public static List <names> mUsers;
    public static List <String> mFavNames;
}
