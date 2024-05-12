package com.example.bizpole


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bizpole.adapter.SongAdapter
import com.example.bizpole.api.ApiService
import com.example.bizpole.databinding.ActivityHomeBinding
import com.example.bizpole.model.Song
import com.example.bizpole.utility.UserManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var apiService: ApiService
    private lateinit var songAdapter: SongAdapter
    private lateinit var toolbar: Toolbar
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://itunes.apple.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService = retrofit.create(ApiService::class.java)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        songAdapter = SongAdapter(emptyList()) { song ->
            navigateToSongDetail(song)
        }
        recyclerView.adapter = songAdapter
        searchSongs("jack johnson")
        val userId = UserManager.getCurrentUser(this) ?: return
        Log.d("UserManager", "Retrieved user ID: $userId")

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                showLogoutConfirmationDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showLogoutConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes") { dialog, which ->
                logout()
            }
            .setNegativeButton("No") { dialog, which -> }
            .show()
    }

    private fun searchSongs(query: String) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val result = apiService.searchSongs(query)
                songAdapter.updateData(result.results)
            } catch (e: Exception) {
                Log.e("HomeActivity", "Error: ${e.message}")
            }
        }
    }

    private fun navigateToSongDetail(song: Song) {
        val intent = Intent(this, SongDetailActivity::class.java)
            val bundle = Bundle().apply {
                putLong("songId", song.trackId)
                putString("trackName", song.trackName)
                putString("artistName", song.artistName)
                putDouble("price", song.trackHdPrice)
                putString("primaryGenreName", song.primaryGenreName)
                putString("longDescription", song.longDescription)
                putString("artworkUrl100", song.artworkUrl100)
            }
            intent.putExtras(bundle)
            startActivity(intent)

    }
    private fun logout() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
