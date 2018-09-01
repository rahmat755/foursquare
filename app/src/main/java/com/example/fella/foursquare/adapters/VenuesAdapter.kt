package com.example.fella.foursquare.adapters

import android.content.Context
import android.location.Location
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.fella.foursquare.R
import com.example.fella.foursquare.db.VenueItem
import com.example.fella.foursquare.util.inflate
import com.example.fella.foursquare.util.loadImg
import com.facebook.drawee.view.SimpleDraweeView

class VenuesAdapter(val mContext: Context, val myLoc: Location, val viewActions: OnViewSelectedListener) : RecyclerView.Adapter<VenuesAdapter.VenuesViewHolder>() {
    interface OnViewSelectedListener {
        fun onItemSelected(id: String?)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VenuesViewHolder {
        val itemView = parent.inflate(R.layout.venue_item)
        return VenuesViewHolder(itemView)
    }

    private var previousPosition: Int? = null
    private var venuesItems: ArrayList<VenueItem> = arrayListOf()
    override fun onBindViewHolder(holder: VenuesViewHolder, position: Int) {
        holder.venueAddress?.text = venuesItems[position].address
        holder.venueName?.text = venuesItems[position].name
        holder.venueIcon?.loadImg( venuesItems[position].icon)
        val destination = Location("")
        destination.latitude = venuesItems[position].lat
        destination.longitude = venuesItems[position].lng
//        holder.venueAddress?.text = venuesItems[position].location?.address ?: " "
//        holder.venueName?.text = venuesItems[position].name
//        if (venuesItems[position].categories!!.isNotEmpty())
//            venuesItems[position].categories!!.first().let {
//                holder.venueIcon?.loadImg( it.icon?.prefix + "bg_64" + it.icon?.suffix)
//            }
//        val destination = Location("")
//        destination.latitude = venuesItems[position].location?.lat!!
//        destination.longitude = venuesItems[position].location?.lng!!
        val distance = myLoc.distanceTo(destination)
        val stringDistance = distance.toString()
        holder.distance?.text = "$stringDistance m"
        holder.itemView.setOnClickListener { viewActions.onItemSelected(venuesItems[position].id) }
    }


    fun addItems(items: ArrayList<VenueItem>) {
        previousPosition = venuesItems.size
        venuesItems.addAll(items)
        notifyDataSetChanged()
    }

    fun removeAllItems() {
        previousPosition = itemCount
        venuesItems.clear()
        notifyItemRangeRemoved(0, previousPosition!!)
    }

    override fun getItemCount(): Int = venuesItems.size


    inner class VenuesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var venueName: TextView? = null
        var venueAddress: TextView? = null
        //        var venueRating: TextView? = null
        var distance: TextView? = null
        var venueIcon: SimpleDraweeView? = null

        init {
            venueAddress = itemView.findViewById(R.id.venue_addr)
            venueName = itemView.findViewById(R.id.venue_name)
//            venueRating = itemView.findViewById(R.id.venue_categories)
            venueIcon = itemView.findViewById(R.id.venue_icon)
            distance = itemView.findViewById(R.id.distance)

        }
    }

}