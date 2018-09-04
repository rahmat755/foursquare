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
    lateinit var lat:String
    lateinit var lng: String
    private lateinit var name: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lat = intent.getStringExtra("lat")
        lng = intent.getStringExtra("lng")
        name = intent.getStringExtra("name")
        setContentView(R.layout.activity_maps)
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val venueLoc = LatLng(lat.toDouble(), lng.toDouble())
        mMap.addMarker(MarkerOptions().position(venueLoc).title(name))
        val yourLocation = CameraUpdateFactory.newLatLngZoom(venueLoc, 11.0f)
        mMap.animateCamera(yourLocation)
    }
}
