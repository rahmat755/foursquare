package com.example.fella.foursquare.view

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.example.fella.foursquare.App
import com.example.fella.foursquare.R
import com.example.fella.foursquare.util.isNetworkAvailable
import com.example.fella.foursquare.viewmodel.VenuesViewModel
import com.example.fella.foursquare.viewmodel.VenuesViewModelFactory
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.core.ImagePipeline
import javax.inject.Inject
import kotlinx.android.synthetic.main.detail_activity.*
import com.stfalcon.frescoimageviewer.ImageViewer


class DetailActivity : AppCompatActivity() {
    lateinit var venueId: String
    @Inject
    lateinit var viewModelFactory: VenuesViewModelFactory
    lateinit var model: VenuesViewModel

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
        setContentView(R.layout.detail_activity)
        setSupportActionBar(this.findViewById(R.id.toolbar_main))
        App.appComponent.injectDetailActivity(this)
        model = ViewModelProviders.of(this, viewModelFactory).get(VenuesViewModel::class.java)
        venueId = model._venueId.value!!
        Log.d("id", venueId)
        if (this.isNetworkAvailable())
            model.loadVenueDetailInfo(venueId)
        else
            model.loadVenueDetailFromDb(venueId)
        model.showDetailVenuesProgressBar.observe(this, Observer {
            if (it!!)
                detail_progressbar.visibility = View.VISIBLE
            else {
                detail_progressbar.visibility = View.GONE
            }
        })
        model.getVenueDetails().observe(this, Observer { venueItem ->
            venue_detail_address.text = venueItem?.address
            venue_detail_name.text = venueItem?.name
            venue_datail_rating.text = venueItem?.tips
            venue_detail_photo.setImageURI(venueItem?.bestPhoto)
            venue_detail_photo.setOnClickListener { view ->
                ImageViewer.Builder(view.context, venueItem?.photos)
                        .setStartPosition(0)
                        .show()
            }
            show_on_map_button.setOnClickListener {
                val intent = Intent(this, MapsActivity::class.java).apply {
                    putExtra("lat", venueItem?.lat)
                    putExtra("lng", venueItem?.lng)
                    putExtra("name", venueItem?.name)
                }
                startActivity(intent)
            }
        })


    }
}
