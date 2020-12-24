package com.example.socialmediaapp.models

import android.media.Image
import android.net.Uri

data class Post (
    val createdBy: User = User(),
    val createdAt: Long = 0L,
    val likedBy: ArrayList<String> = ArrayList()
) {
    var text: String = ""
    var imageUri: String = ""
}