package com.example.socialmediaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.socialmediaapp.daos.PostDao
import com.example.socialmediaapp.models.Post
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), IPostAdapter {
    private lateinit var adapter: PostAdapter
     var postDao: PostDao = PostDao()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        addPost.setOnClickListener {
//            val intent = Intent(this, CreatePost::class.java)
//
//            startActivity(intent)
//        }

        setUpRecyclerView()

    }
    private fun setUpRecyclerView() {
        val postCollections = postDao.postCollection
        val query = postCollections.orderBy("createdAt", Query.Direction.DESCENDING)
        val recyclerOptions = FirestoreRecyclerOptions.Builder<Post>().setQuery(query, Post::class.java).build()
        adapter = PostAdapter(recyclerOptions, this)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    override fun onLikeClicked(postId: String) {
        postDao.updateLikes(postId)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.bottom_navigation_menu, menu)
        Log.d("success", "menu inflated")
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val itemId = item.itemId
        val search = R.id.search_menu
        Log.d("success", "option is selected")
        return super.onOptionsItemSelected(item)
    }
}