package com.mapbox.mapboxandroiddemo.tagmap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.ArrayList;
import java.util.List;

public class MapDatabase extends SQLiteOpenHelper {

  static String POINTS_DB = "POINTSDB.db";
  static int DATABASE_VERSION = 2;
  static String TABLE = "POINTS_TABLE";
  static String LATITUDE = "latitude";
  static String LONGITUDE = "longitude";
  static String ALTITUDE = "ALTITUDE";

  public MapDatabase(Context context) {
    super(context, POINTS_DB, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL("CREATE TABLE IF NOT EXISTS Points(" + LATITUDE + " DOUBLE, " +
        LONGITUDE + " DOUBLE, " +
        ALTITUDE + " DOUBLE);");
  }

  /**
   * Insert a new point in the database
   */
  void insert(LatLng point) {
    SQLiteDatabase db = this.getWritableDatabase();

    ContentValues values = new ContentValues();
    values.put(LATITUDE, point.getLatitude());
    values.put(LONGITUDE, point.getLongitude());
    values.put(ALTITUDE, point.getAltitude());

    db.beginTransaction();
    try {
      db.insert("Points", null, values);
    } finally {
      db.endTransaction();
    }
    db.close();
  }

  /**
   * Returns true if there already exists a point in the map closer than max_distance
   */
  boolean hasPoint(LatLng point, double max_distance) {
    SQLiteDatabase db = this.getReadableDatabase();

    Cursor cursor = db.rawQuery("SELECT * FROM Points", null);
    if (cursor != null) {
      while(cursor.moveToNext()) {
        LatLng readPoint = new LatLng(cursor.getDouble(0), cursor.getDouble(1), cursor.getDouble(2));
        double distance = readPoint.distanceTo(point);

        if (distance <= max_distance) {
          return true;
        }
      }
    }

    db.close();
    return false;
  }

  /**
   * Returns the list of all points
   */
  List<LatLng> getPoints() {
    ArrayList<LatLng> points = new ArrayList<LatLng>();

    SQLiteDatabase db = this.getReadableDatabase();

    Cursor cursor = db.rawQuery("SELECT * FROM Points", null);
    if (cursor != null) {
      while (cursor.moveToNext()) {
        LatLng p = new LatLng(cursor.getDouble(0), cursor.getDouble(1), cursor.getDouble(2));
        points.add(p);
      }
    }

    db.close();
    return points;
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    // Not needed now
  }
}
