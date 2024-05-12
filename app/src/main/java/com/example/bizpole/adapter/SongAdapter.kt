package com.example.bizpole.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bizpole.R
import com.example.bizpole.model.Song
import com.example.bizpole.utility.FavoriteManager
import com.example.bizpole.utility.UserManager

class SongAdapter(private var songs: List<Song>, private val onItemClick: (Song) -> Unit) : RecyclerView.Adapter<SongAdapter.SongViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_song, parent, false)
        return SongViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        holder.bind(songs[position])
    }

    override fun getItemCount(): Int = songs.size

    fun updateData(newSongs: List<Song>) {
        songs = newSongs
        notifyDataSetChanged()
    }

    inner class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewSongName: TextView = itemView.findViewById(R.id.textViewSongName)
        private val textViewArtistName: TextView = itemView.findViewById(R.id.textViewArtistName)
        private val buttonFavorite: ImageButton = itemView.findViewById(R.id.favoriteButton)


        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(songs[position])
                }
            }
            buttonFavorite.setOnClickListener {
                Log.d("SongAdapter", "Favorite button clicked at position: $adapterPosition")
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val song = songs[position]
                    val userId = UserManager.getCurrentUser(itemView.context) ?: return@setOnClickListener
                    if (song.isFavorite) {
                        FavoriteManager.removeFavoriteSong(itemView.context, userId, song.trackId)
                    } else {
                        FavoriteManager.addFavoriteSong(itemView.context, userId, song.trackId)
                    }
                    song.isFavorite = !song.isFavorite
                    notifyItemChanged(position)
                }
            }
        }

        fun bind(song: Song) {
            textViewSongName.text = song.trackName
            textViewArtistName.text = song.artistName
            val userId = UserManager.getCurrentUser(itemView.context) ?: return
            val isFavorite = FavoriteManager.getFavoriteSongs(itemView.context, userId).contains(song.trackId)
            if (isFavorite) {
                buttonFavorite.setImageResource(R.drawable.baseline_favorite_24)
            } else {
                buttonFavorite.setImageResource(R.drawable.baseline_favorite_border_24)
            }
        }
    }
}
