package com.example.fella.foursquare.view

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.fella.foursquare.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var lat: Double? = null
    private var lng: Double? = null
    private lateinit var name: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lat = intent.getDoubleExtra("lat", 0.0)
        lng = intent.getDoubleExtra("lng",0.0)
        name = intent.getStringExtra("name")
        setContentView(R.layout.activity_maps)
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val venueLoc = LatLng(lat!!, lng!!)
        mMap.addMarker(MarkerOptions().position(venueLoc).title(name))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(venueLoc))
        mMap.animateCamera( CameraUpdateFactory.zoomTo( 17.0f ) )
    }
}
