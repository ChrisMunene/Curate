package com.chriskinyua.weatherapp.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chriskinyua.collaborativeplaylist.R
import com.chriskinyua.shoppinglist.touch.ListItemTouchHelperCallback
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.protocol.types.Track
import kotlinx.android.synthetic.main.playlist_item.view.*
import java.io.File.separator

import java.util.*

class PlaylistAdapter: RecyclerView.Adapter<PlaylistAdapter.ViewHolder>, ListItemTouchHelperCallback {

    val playlist: MutableList<Track> = mutableListOf()
    private val context: Context
    private lateinit var spotifyAppRemote: SpotifyAppRemote
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

        spotifyAppRemote.imagesApi.getImage(currentTrack.imageUri).setResultCallback {image ->
            Glide
                .with(context)
                .load(image)
                .centerCrop()
                .placeholder(ColorDrawable(Color.BLACK))
                .into(holder.ivTrackImage)
        }.setErrorCallback {throwable ->
            //TODO: Set placeholder image
            Log.e(TAG, "Error fetching image: ${currentTrack.name}", throwable)
        }


        holder.tvTrackName.text = currentTrack.name
        val artistNames = mutableListOf<String>()
        currentTrack.artists.forEach{
            artistNames.add(it.name)
        }

        holder.tvArtists.text = artistNames.joinToString(separator = ", ")
    }



    fun addListItem(track: Track){
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

    fun updateListItem(track: Track, editIndex: Int){
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