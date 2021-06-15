package com.hfad;

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
    private static void insertWeather(SQLiteDatabase db, String main,
                                      String description, String icon, String iconKey) {
        ContentValues weatherValues = new ContentValues();
        weatherValues.put("MAIN", main);
        weatherValues.put("DESCRIPTION", description);
        weatherValues.put("ICON", icon);
        db.insert("WEATHER", null, weatherValues);
    }
    private void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 1) {
            db.execSQL("CREATE TABLE WEATHER (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "MAIN TEXT, "
                    + "DESCRIPTION TEXT, "
                    + "ICON TEXT, "
                    + "ICONKEY TEXT);");
            insertWeather(db, "Clear", "clear sky", String.valueOf(R.drawable.d01), "01d");
            insertWeather(db, "Clouds", "few clouds", String.valueOf(R.drawable.d02), "02d");
            insertWeather(db, "Clouds", "scattered clouds", String.valueOf(R.drawable.d03), "03d");
            insertWeather(db, "Clouds", "broken clouds", String.valueOf(R.drawable.d04), "04d");
            insertWeather(db, "Rain", "shower rain", String.valueOf(R.drawable.d09), "09d");
            insertWeather(db, "Rain", "rain", String.valueOf(R.drawable.d10), "10d");
            insertWeather(db, "Thunderstorm", "thunderstorm", String.valueOf(R.drawable.d11), "11d");
            insertWeather(db, "Snow", "snow", String.valueOf(R.drawable.d13), "13d");
            insertWeather(db, "Mist", "mist", String.valueOf(R.drawable.d50), "50d");
        }
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE WEATHER ADD COLUMN FAVORITE NUMERIC;");
        }
    }
}
