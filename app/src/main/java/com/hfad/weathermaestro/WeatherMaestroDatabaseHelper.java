package com.hfad.weathermaestro;

import android.content.ContentValues;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.hfad.weathermaestro.R;

class WeatherMaestroDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "weathermaestro"; // the name of our database
    private static final int DB_VERSION = 2; // the version of the database

    WeatherMaestroDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        updateMyDatabase(db, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateMyDatabase(db, oldVersion, newVersion);
    }

    private void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 1) {
            db.execSQL("CREATE TABLE Drawables (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "Drawable TEXT, "
                    + "Icon TEXT);");
            insertWeatherIcon(db, String.valueOf(R.drawable.d01), "01d");
            insertWeatherIcon(db, String.valueOf(R.drawable.d02), "02d");
            insertWeatherIcon(db, String.valueOf(R.drawable.d03), "03d");
            insertWeatherIcon(db, String.valueOf(R.drawable.d04), "04d");
            insertWeatherIcon(db, String.valueOf(R.drawable.d09), "09d");
            insertWeatherIcon(db, String.valueOf(R.drawable.d10), "10d");
            insertWeatherIcon(db, String.valueOf(R.drawable.d11), "11d");
            insertWeatherIcon(db, String.valueOf(R.drawable.d13), "13d");
            insertWeatherIcon(db, String.valueOf(R.drawable.d50), "50d");
        }
        if (oldVersion < 2) {
            db.execSQL("CREATE TABLE SavedPlaces (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "PlaceName TEXT," +
                    "Favorite BOOLEAN CHECK (Favorite IN (0,1)));");
                    //Sqlite doesn't actually have a boolean type, it's an alias for INTEGER,
                    // so the check constraint is to make sure only 1(true) and 0(false) are allowed
        }
    }

    private static void insertWeatherIcon(SQLiteDatabase db, String drawable, String icon) {
        ContentValues weatherValues = new ContentValues();
        weatherValues.put("Drawable", drawable);
        weatherValues.put("Icon", icon);
        db.insert("Drawables", null, weatherValues);
    }

    public static void insertSavedPlace(SQLiteDatabase db, String placeName) {
        ContentValues placeValues = new ContentValues();
        placeValues.put("PlaceName", placeName);
        placeValues.put("Favorite", 0);
        db.insert("SavedPlaces",null,placeValues);
    }

    public void changeFavoritePlace(String placeName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues placeValues = new ContentValues();
        placeValues.put("Favorite", 0);
        // zero out whatever the current favorite is
        db.update("SavedPlaces",placeValues,null,null);

        placeValues.put("Favorite",1);
        //set the new favorite
        db.update("SavedPlaces", placeValues, "PlaceName = ?", new String[]{placeName});
    }
}
