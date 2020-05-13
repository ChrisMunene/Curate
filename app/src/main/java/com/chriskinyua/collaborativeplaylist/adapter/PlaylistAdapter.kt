package com.chriskinyua.weatherapp.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.chriskinyua.collaborativeplaylist.R
import com.chriskinyua.collaborativeplaylist.data.TrackModel
import com.chriskinyua.shoppinglist.touch.ListItemTouchHelperCallback
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.protocol.types.Track
import kotlinx.android.synthetic.main.playlist_item.view.*
import java.io.File.separator

import java.util.*

class PlaylistAdapter: RecyclerView.Adapter<PlaylistAdapter.ViewHolder>, ListItemTouchHelperCallback {

    var playlist: MutableList<TrackModel> = mutableListOf()
    private val context: Context
    private val spotifyAppRemote: SpotifyAppRemote
    private val TAG = PlaylistAdapter::class.java.simpleName

    constructor(context: Context, appRemote: SpotifyAppRemote ){
        this.context = context
        this.spotifyAppRemote = appRemote
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =  LayoutInflater.from(context).inflate(
            R.layout.playlist_item, parent, false
        )

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return playlist.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentTrack = playlist[position]
        val images = currentTrack.album?.images

        Glide
            .with(context)
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
    }



    fun addListItem(track: TrackModel){
        if(!playlist.contains(track)){
            playlist.add(0, track)
            notifyItemInserted(0)
            Log.d("PlaylistAdapter", "${track.name} added to list")
        }
    }

    fun deleteListItem(position: Int){
        playlist.removeAt(position)
        notifyItemRemoved(position)
//        (context as ScrollingActivity).showSnackBar(3)
    }

    fun deleteAllListItems(){
        playlist.clear()
        notifyDataSetChanged()
//        (context as ScrollingActivity).showSnackBar(4)
    }

    fun updateListItem(track: TrackModel, editIndex: Int){
        playlist.set(editIndex, track)
        notifyItemChanged(editIndex)
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivTrackImage  = itemView.ivTrackImage
        val tvArtists = itemView.tvArtists
        val tvTrackName = itemView.tvTrackName
    }

    override fun onDismissed(position: Int) {
        deleteListItem(position)
    }

    override fun onItemMoved(fromPosition: Int, toPosition: Int) {
        Collections.swap(playlist, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }


}