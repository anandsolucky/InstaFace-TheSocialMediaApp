package com.example.socialmediaapp.startup

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.socialmediaapp.MainActivity
import com.example.socialmediaapp.R
import com.example.socialmediaapp.daos.UserDao
import com.example.socialmediaapp.models.User
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_create_post.*
import kotlinx.android.synthetic.main.activity_sign_up_basic_info.*
import java.util.*

class SignUpBasicInfoActivity : AppCompatActivity() {
    private lateinit var imageUri: Uri
    private lateinit var storageReference: StorageReference
    private lateinit var auth: FirebaseAuth
    private  lateinit var  storage: FirebaseStorage
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_basic_info)
        auth = Firebase.auth
        storage = FirebaseStorage.getInstance()
        storageReference = storage.reference
        val currentUser = auth.currentUser
        if (currentUser != null) {
            Toast.makeText(this, "auth: ${currentUser.uid}", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "could not get current user", Toast.LENGTH_SHORT).show()
        }
        uploadProfileButton.setOnClickListener {
            choosePicture()
        }

        getStarted.setOnClickListener {
            registerUser()
        }
    }

    fun registerUser() {
        val firstName = FistName.text
        val lastName = LastName.text
        if (auth.currentUser == null) {
            Toast.makeText(this, "error! can't get current user", Toast.LENGTH_SHORT).show()
        } else {

            val user = User(uid = auth.currentUser!!.uid, imageUrl = imageUri.toString(), displayName = "$firstName $lastName")
            val usersDao = UserDao()
            usersDao.addUser(user)
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

     fun choosePicture() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.data != null) {
            imageUri = data.data!!
            Glide.with(profilePhotoUpload.context).load(imageUri).circleCrop().into(profilePhotoUpload)
            profilePhotoUpload.setImageURI(imageUri)
            uploadPicture()

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
                    //Snackbar.make(findViewById(R.id.postImageContent), "Image Uploaded", Snackbar.LENGTH_LONG).show()
                    Toast.makeText(this, "image uploaded", Toast.LENGTH_SHORT).show()
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


}