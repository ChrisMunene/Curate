package com.chriskinyua.collaborativeplaylist.adapter

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.chriskinyua.collaborativeplaylist.MainActivity
import com.chriskinyua.collaborativeplaylist.R
import com.chriskinyua.collaborativeplaylist.data.TrackModel
import com.chriskinyua.shoppinglist.touch.ListItemTouchHelperCallback
import com.spotify.android.appremote.api.SpotifyAppRemote
import kotlinx.android.synthetic.main.search_result_track.view.*
import java.util.*

class SearchResultAdapter: RecyclerView.Adapter<SearchResultAdapter.ViewHolder>,
    ListItemTouchHelperCallback {

    var searchResults: MutableList<TrackModel> = mutableListOf()
    private val context: MainActivity?
    private var spotifyAppRemote: SpotifyAppRemote
    private val TAG = SearchResultAdapter::class.java.simpleName

    constructor(context: FragmentActivity? , appRemote: SpotifyAppRemote){
        this.context = context as MainActivity
        this.spotifyAppRemote = appRemote
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =  LayoutInflater.from(context).inflate(
            R.layout.search_result_track, parent, false
        )

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return searchResults.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentTrack = searchResults[position]

        val images = currentTrack.album?.images

        Glide
            .with(context!!)
            .load(images?.get(0)?.url)
            .centerCrop()
            .placeholder(ColorDrawable(Color.BLACK))
            .listener(object: RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    Log.e(TAG, "Problem Loading Image", e)
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

            })
            .into(holder.ivTrackImage)


        holder.tvTrackName.text = currentTrack.name
        val artistNames = mutableListOf<String?>()
        currentTrack.artists?.forEach { artist ->
            artistNames.add(artist.name)
        }

        holder.tvArtists.text = artistNames.joinToString(separator = ", ")

        holder.searchResultCard.setOnClickListener {
            context.addToQueue(currentTrack)
            context.removeFromSearchResults(position)
            Toast.makeText(context, "Added to queue", Toast.LENGTH_LONG).show()
        }
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivTrackImage  = itemView.ivTrackImage
        val tvArtists = itemView.tvArtists
        val tvTrackName = itemView.tvTrackName
        val searchResultCard  = itemView.searchResultCard
    }

    override fun onDismissed(position: Int) {
        context?.removeFromSearchResults(position)
    }

    override fun onItemMoved(fromPosition: Int, toPosition: Int) {
        Collections.swap(searchResults, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }


}