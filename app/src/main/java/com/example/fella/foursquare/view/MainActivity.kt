package com.example.fella.foursquare.view

import android.Manifest
import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.example.fella.foursquare.adapters.VenuesAdapter
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject
import com.google.android.gms.location.LocationServices
import android.content.pm.PackageManager
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.support.v4.app.ActivityCompat
import com.google.android.gms.location.LocationRequest
import android.location.LocationManager
import android.support.design.widget.Snackbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.fella.foursquare.App
import com.example.fella.foursquare.R
import com.example.fella.foursquare.util.EqualSpacingItemDecoration
import com.example.fella.foursquare.util.isNetworkAvailable
import com.example.fella.foursquare.viewmodel.VenuesViewModel
import com.example.fella.foursquare.viewmodel.VenuesViewModelFactory
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.core.ImagePipeline

private const val PERMISSION_REQUEST_CODE = 123

class MainActivity : AppCompatActivity(), GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, VenuesAdapter.OnViewSelectedListener {
    override fun onItemSelected(id: String?) {
        Log.d("id", id)
        model._venueId.value = id
        val intent = Intent(this, DetailActivity::class.java)
        startActivity(intent)
    }


    @Inject
    lateinit var viewModelFactory: VenuesViewModelFactory
    lateinit var model: VenuesViewModel
    private var mGoogleApiClient: GoogleApiClient? = null
    private var mLocation: Location? = null
    private var locationManager: LocationManager? = null
    private var mLocationRequest: LocationRequest? = null
    var latitude: Double? = null
    var longitude: Double? = null
    lateinit var currentLocation: Location
    private val mLayoutManager = LinearLayoutManager(this)

    fun showError(msg: String) {
        Snackbar.make(venues_recyclerview, msg, Snackbar.LENGTH_INDEFINITE)
                .setAction("Повторить попытку") { getVenues() }.show()
    }

    override fun onConnected(bundle: Bundle?) {
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                    PERMISSION_REQUEST_CODE)
            return
        }
        startLocationUpdates()
        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient)
        if (mLocation == null) {
            startLocationUpdates()
        }
        if (mLocation != null) {
            latitude = mLocation?.latitude
            longitude = mLocation?.longitude
            model._location.value = mLocation
            getVenues()

        } else {
            Toast.makeText(this, "Location not Detected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startLocationUpdates() {
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(11)
                .setFastestInterval(11)
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                    PERMISSION_REQUEST_CODE)
            return
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this)
    }

    override fun onConnectionSuspended(i: Int) {
        Log.i("1", "Connection Suspended")
        mGoogleApiClient?.connect()
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        Log.i("1", "Connection failed. Error: " + connectionResult.errorCode)
    }

    public override fun onStart() {
        super.onStart()
        mGoogleApiClient?.connect()
    }

    public override fun onStop() {
        super.onStop()
        if (mGoogleApiClient!!.isConnected) {
            mGoogleApiClient?.disconnect()
        }
    }

    override fun onLocationChanged(location: Location) {
        longitude = location.longitude
        latitude = location.latitude
        App.state = mLayoutManager.onSaveInstanceState()
    }


    private lateinit var venuesAdapter: VenuesAdapter
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.clear_cache) {
            val imagePipeline : ImagePipeline = Fresco.getImagePipeline()
            imagePipeline.clearCaches()
            model.dropData()
        }
        return true
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val itemDecoration = EqualSpacingItemDecoration(16, 1)
        setSupportActionBar(this.findViewById(R.id.toolbar_main))
        mGoogleApiClient = GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()
        currentLocation = Location("")

        venuesAdapter = VenuesAdapter(this, currentLocation, this)
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        App.appComponent.injectMainActivity(this)
        model = ViewModelProviders.of(this, viewModelFactory).get(VenuesViewModel::class.java)

        model.getVenuesList().observe(this, Observer {
            Log.d("location", mLocation.toString())
            venuesAdapter.removeAllItems()
            venuesAdapter.addItems(it!!)
        })
        model.showAllVenuesProgressBar.observe(this, Observer {
            if (it!!)
                progressBar.visibility = View.VISIBLE
            else {
                swipe_refresh_layout.isRefreshing = false
                progressBar.visibility = View.GONE
            }
        })
        model.location.observe(this, Observer {
            getVenues()
        })
        model.showNoDataError.observe(this, Observer {
            no_data_textview.visibility = View.VISIBLE
        })
        model.showError.observe(this, Observer { event ->
            showError(event?.getContentIfNotHandled()!!)
            if (swipe_refresh_layout.isRefreshing)
                swipe_refresh_layout.isRefreshing = false
        })
        venues_recyclerview.apply {
            layoutManager = mLayoutManager
            adapter = venuesAdapter
            addItemDecoration(itemDecoration)
        }
        swipe_refresh_layout.setOnRefreshListener {
            getVenues()
        }
    }

    private fun getVenues() {
        no_data_textview.visibility = View.GONE
        currentLocation.longitude = longitude!!
        currentLocation.latitude = latitude!!
        venuesAdapter = VenuesAdapter(this, currentLocation, this)
        venues_recyclerview.adapter = venuesAdapter
        if (this.isNetworkAvailable())
            model.loadVenues(latitude.toString(), longitude.toString())
        else
            model.loadVenuesFromDb()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
        System.exit(0)
    }
}