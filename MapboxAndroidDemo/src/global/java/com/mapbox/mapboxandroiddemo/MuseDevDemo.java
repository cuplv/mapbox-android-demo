package com.mapbox.mapboxandroiddemo;

import android.os.Bundle;
import android.widget.Toast;

import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

/**
 * The most basic example of adding a map to an activity.
 */
@SuppressWarnings("ALL")
public class MuseDevDemo extends AppCompatActivity {

  private MapView mapView;
  private MapDatabase database;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    database = new MapDatabase(getApplicationContext());

    Mapbox.getInstance(this, getString(R.string.access_token));

    setContentView(R.layout.activity_basic_simple_mapview);
    mapView = findViewById(R.id.mapView);
    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync(new OnMapReadyCallback() {
      @Override
      public void onMapReady(@NonNull MapboxMap mapboxMap) {
        mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
          @Override
          public void onStyleLoaded(@NonNull Style style) {
            // 1. Load the data on the map
            for (LatLng point : database.getPoints())
              mapboxMap.addMarker(new MarkerOptions().position(point));

            // 2. Record a new point when clicking the map
            mapboxMap.addOnMapClickListener(new MapboxMap.OnMapClickListener() {
              @Override
              public boolean onMapClick(@NonNull LatLng point) {
                if (database.hasPoint(point, 200)) {
                  Toast.makeText(MuseDevDemo.this,
                      R.string.activity_demo_point_exists,
                      Toast.LENGTH_SHORT).show();
                } else {
                  database.insert(point);
                  mapboxMap.addMarker(new MarkerOptions().position(point));
                  Toast.makeText(MuseDevDemo.this,
                      R.string.activity_demo_point_added,
                      Toast.LENGTH_LONG).show();
                }
                return true;
              }
            });
          }
        });
      }
    });
  }

  // Add the mapView lifecycle to the activity's lifecycle methods
  @Override
  public void onResume() {
    super.onResume();
    mapView.onResume();
  }

  @Override
  protected void onStart() {
    super.onStart();
    mapView.onStart();
  }

  @Override
  protected void onStop() {
    super.onStop();
    mapView.onStop();
  }

  @Override
  public void onPause() {
    super.onPause();
    mapView.onPause();
  }

  @Override
  public void onLowMemory() {
    super.onLowMemory();
    mapView.onLowMemory();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    mapView.onDestroy();
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    mapView.onSaveInstanceState(outState);
  }
}
