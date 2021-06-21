package com.hfad.weathermaestro;

import android.content.ContentValues;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.hfad.weathermaestro.R;

class WeatherMaestroDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "weathermaestro"; // the name of our database
    private static final int DB_VERSION = 1; // the version of the database

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
    }

    private static void insertWeatherIcon(SQLiteDatabase db, String drawable, String icon) {
        ContentValues weatherValues = new ContentValues();
        weatherValues.put("Drawable", drawable);
        weatherValues.put("Icon", icon);
        db.insert("Drawables", null, weatherValues);
    }
}
