package com.example.socialmediaapp

import android.app.ActionBar
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.socialmediaapp.daos.PostDao
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.OnProgressListener
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_create_post.*
import kotlinx.android.synthetic.main.item_post.*
import java.util.*


class CreatePost : AppCompatActivity() {
    private lateinit var postDao: PostDao
    private lateinit var imageUri: Uri
    private lateinit var storageReference: StorageReference
    private  lateinit var  storage: FirebaseStorage
    private var postingText: Boolean = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)
        storage = FirebaseStorage.getInstance()
        storageReference = storage.reference
        postDao = PostDao()
        postButton.setOnClickListener {
            val input = postInput.text.toString().trim()
            if(postingText) {
                if(input != "" && input != null) {
                    postDao.addPost(input)
                    finish()
                } else
                    Toast.makeText(this, "please write/upload something!!", Toast.LENGTH_SHORT).show()
            }
            else {
                if(imageUri.toString() != "") {
                    postDao.addPost(imageUri)
                    finish()
                } else
                    Toast.makeText(this, "please write/upload something!!", Toast.LENGTH_SHORT).show()
            }
        }

        postEditButton.setOnClickListener {
            postImagePreview.setImageURI(Uri.parse(""))
            postImagePreview.visibility = View.GONE
            postInput.visibility = View.VISIBLE
        }

        postImageButton.setOnClickListener{
            choosePicture()
        }
    }

    private fun choosePicture() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1 && resultCode == RESULT_OK && data != null && data.data != null) {
            imageUri = data.data!!
            postImagePreview.setImageURI(imageUri)
            postImagePreview.visibility = View.VISIBLE
            postInput.visibility = View.GONE
            uploadPicture()
            postingText = false
        } else {
//            Toast.makeText(this, "activity result failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadPicture() {
        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Image is uploading")
        progressDialog.show()
        val randomUUid =  UUID.randomUUID().toString()
        val riversRef: StorageReference = storageReference.child("images/$randomUUid")

        riversRef.putFile(imageUri)
            .addOnSuccessListener { taskSnapshot -> // Get a URL to the uploaded content
                progressDialog.dismiss()
                Snackbar.make(findViewById(R.id.postImageContent), "Image Uploaded", Snackbar.LENGTH_LONG).show()
            }
            .addOnFailureListener {
                progressDialog.dismiss()
                Toast.makeText(this, "Failed uploading", Toast.LENGTH_SHORT).show()
            }
            .addOnProgressListener {
                val progresspercent = (100.00 * it.bytesTransferred / it.totalByteCount)
                progressDialog.setMessage("${progresspercent.toInt()}%")
            }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.about_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d("success", "on option selected")
        val itemId = item.itemId
        val back = R.id.back
        val intent: Intent
        if(itemId == back) {
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}