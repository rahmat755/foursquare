package com.example.fella.foursquare.view

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.fella.foursquare.App
import com.example.fella.foursquare.R
import com.example.fella.foursquare.db.VenueItem
import com.example.fella.foursquare.di.venues.DbModule
import com.example.fella.foursquare.di.venues.detailvenues.DaggerDetailVenuesComponent
import com.example.fella.foursquare.di.venues.detailvenues.DetailVenueModule
import com.example.fella.foursquare.presenter.DetailVenuesPresenter
import com.example.fella.foursquare.presenter.MVPContract
import com.example.fella.foursquare.util.isNetworkAvailable
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.core.ImagePipeline
import javax.inject.Inject
import kotlinx.android.synthetic.main.detail_activity.*
import com.stfalcon.frescoimageviewer.ImageViewer


class DetailActivity : AppCompatActivity(), MVPContract.DetailVenuesView {
    private lateinit var venueId: String
    @Inject
    lateinit var presenter: DetailVenuesPresenter

    override fun displayData(venue: VenueItem) {
        venue_detail_address.text = venue.address
        venue_detail_name.text = venue.name
        venue_datail_rating.text = venue.tips
        venue_detail_photo.hierarchy.setFailureImage(R.drawable.ic_baseline_report_problem_24px)
        venue_detail_photo.setImageURI(venue.bestPhoto)
        if (venue.photos != null)
            venue_detail_photo.setOnClickListener { view ->
                ImageViewer.Builder(view.context, venue.photos)
                        .setStartPosition(0)
                        .show()
            }
        show_on_map_button.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java).apply {
                putExtra("lat", venue.lat.toString())
                putExtra("lng", venue.lng.toString())
                putExtra("name", venue.name)
            }
            startActivity(intent)
        }
    }

    override fun displayError(e: Throwable, func: () -> Unit) {
        Snackbar.make(this.show_on_map_button, e.localizedMessage, Snackbar.LENGTH_INDEFINITE)
                .setAction("Повторить попытку") { func() }.show()
    }

    override fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    override fun showProgressBar(flag: Boolean) {
        if (flag)
            detail_progressbar.visibility = View.VISIBLE
        else {
            detail_progressbar.visibility = View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.clear_cache) {
            val imagePipeline: ImagePipeline = Fresco.getImagePipeline()
            imagePipeline.clearCaches()
            presenter.dropData()
        }
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_activity)
        setSupportActionBar(this.findViewById(R.id.toolbar_main))
        DaggerDetailVenuesComponent.builder()
                .appComponent(App.appComponent)
                .dbModule(DbModule(application))
                .detailVenueModule(DetailVenueModule(this))
                .build()
                .inject(this)
        venueId = intent.getStringExtra("venueId")

        if (this.isNetworkAvailable())
            presenter.loadData(venueId)
        else
            presenter.loadData(venueId)

    }
}
