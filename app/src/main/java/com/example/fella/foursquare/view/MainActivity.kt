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
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.support.v4.app.ActivityCompat
import com.google.android.gms.location.LocationRequest
import android.location.LocationManager
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.fella.foursquare.App
import com.example.fella.foursquare.presenter.MVPContract
import com.example.fella.foursquare.R
import com.example.fella.foursquare.db.VenueItem
import com.example.fella.foursquare.di.venues.allvenues.AllVenuesModule
import com.example.fella.foursquare.di.venues.allvenues.DaggerAllVenuesComponent
import com.example.fella.foursquare.di.venues.DbModule
import com.example.fella.foursquare.presenter.AllVenuesPresenter
import com.example.fella.foursquare.util.EqualSpacingItemDecoration
import com.example.fella.foursquare.util.FROM_API
import com.example.fella.foursquare.util.FROM_DB
import com.example.fella.foursquare.util.isNetworkAvailable
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.core.ImagePipeline

private const val PERMISSION_REQUEST_CODE = 123

class MainActivity : AppCompatActivity(), GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, VenuesAdapter.OnViewSelectedListener, MVPContract.AllVenuesView {
    override fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    override fun displayError(e: Throwable, func: () -> Unit) {
        Snackbar.make(venues_recyclerview, e.localizedMessage, Snackbar.LENGTH_INDEFINITE)
                .setAction("Повторить попытку") { func() }.show()
        if (swipe_refresh_layout.isRefreshing)
            swipe_refresh_layout.isRefreshing = false
    }

    override fun displayData(venueList: ArrayList<VenueItem>) {
        venuesAdapter.removeAllItems()
        venuesAdapter.addItems(venueList, currentLocation)
    }


    override fun showProgressBar(flag: Boolean) {
        if (flag)
            progressBar.visibility = View.VISIBLE
        else {
            if (swipe_refresh_layout.isRefreshing) {
                swipe_refresh_layout.isRefreshing = false
            }
            progressBar.visibility = View.GONE
        }
    }

    override fun onItemSelected(id: String?) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("venueId", id)
        startActivity(intent)
    }


    @Inject
    lateinit var venuesPresenter: AllVenuesPresenter
    private var mGoogleApiClient: GoogleApiClient? = null
    private var mLocation: Location? = null
    private var mLocationRequest: LocationRequest? = null
    private var latitude: Double? = null
    private var longitude: Double? = null
    lateinit var currentLocation: Location
    private val mLayoutManager = LinearLayoutManager(this)

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

    public override fun onResume() {
        super.onResume()
        mGoogleApiClient?.connect()
    }

    public override fun onStop() {
        super.onStop()
        if (mGoogleApiClient!!.isConnected) {
            mGoogleApiClient?.disconnect()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        venuesPresenter.destroy()
    }

    override fun onLocationChanged(location: Location) {
        longitude = location.longitude
        latitude = location.latitude
    }


    private lateinit var venuesAdapter: VenuesAdapter
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.clear_cache) {
            val imagePipeline: ImagePipeline = Fresco.getImagePipeline()
            imagePipeline.clearCaches()
            venuesPresenter.dropData()
            venuesAdapter.removeAllItems()
        }
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val itemDecoration = EqualSpacingItemDecoration(16, 1)
        setSupportActionBar(this.findViewById(R.id.toolbar_main))
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            showGPSDisabledAlertToUser()
        }
        mGoogleApiClient = GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()
        currentLocation = Location("")

        venuesAdapter = VenuesAdapter(this)
        DaggerAllVenuesComponent.builder()
                .appComponent(App.appComponent)
                .dbModule(DbModule(application))
                .allVenuesModule(AllVenuesModule(this))
                .build()
                .inject(this)

        venues_recyclerview.apply {
            layoutManager = mLayoutManager
            adapter = venuesAdapter
            addItemDecoration(itemDecoration)
        }
        swipe_refresh_layout.setOnRefreshListener {
            getVenues()
        }
    }

    private fun showGPSDisabledAlertToUser() {
        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setMessage("GPS is disabled in your device. Would you like to enable it?")
        builder.setPositiveButton("Goto Settings Page To Enable GPS") { dialog, which ->
            val callGPSSettingIntent = Intent(
                    android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(callGPSSettingIntent)
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun getVenues() {
        currentLocation.longitude = longitude!!
        currentLocation.latitude = latitude!!
        if (this.isNetworkAvailable())
            venuesPresenter.loadData(latitude.toString(), longitude.toString(), FROM_API)
        else
            venuesPresenter.loadData(latitude.toString(), longitude.toString(), FROM_DB)

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
        System.exit(0)
    }
}
