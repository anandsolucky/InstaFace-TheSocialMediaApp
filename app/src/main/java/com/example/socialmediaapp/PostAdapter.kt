package com.example.socialmediaapp

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.socialmediaapp.models.Post
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class PostAdapter(options: FirestoreRecyclerOptions<Post>, val listener: IPostAdapter) : FirestoreRecyclerAdapter<Post, PostAdapter.PostViewHolder>(options) {

    class PostViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val postText: TextView = itemView.findViewById(R.id.postText)
        val userText: TextView = itemView.findViewById(R.id.postUserName)
        val createdAt: TextView = itemView.findViewById(R.id.postCreatedAt)
        val likeCount: TextView = itemView.findViewById(R.id.postLikesCount)
        val userImage: ImageView = itemView.findViewById(R.id.postProfile)
        val likeButton: ImageView = itemView.findViewById(R.id.postLikeButton)
        val postImage: ImageView = itemView.findViewById(R.id.postImage)
        val constraintLayout: ConstraintLayout = itemView.findViewById(R.id.itemConstraintLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val viewHolder =  PostViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false))
        viewHolder.likeButton.setOnClickListener {

            listener.onLikeClicked(snapshots.getSnapshot(viewHolder.adapterPosition).id)
        }


        return  viewHolder
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int, model: Post) {
        holder.postText.text = model.text
        holder.userText.text = model.createdBy.displayName
        //Glide.with(holder.userImage.context).load(model.createdBy.imageUrl).circleCrop().into(holder.userImage)
        Glide.with(holder.userImage.context).load(Uri.parse(model.createdBy.imageUrl)).circleCrop().into(holder.userImage)
        if(model.imageUri != "") {
            Log.d("Success","Got inside the image uri: ${model.imageUri}")
            Glide.with(holder.postImage.context).load(Uri.parse(model.imageUri)).circleCrop().into(holder.postImage)
            holder.postText.visibility = View.GONE
            holder.postImage.visibility = View.VISIBLE
            val constraintSet = ConstraintSet()
            constraintSet.clone(holder.constraintLayout)
            constraintSet.connect(R.id.postLikeButton, ConstraintSet.TOP, R.id.postImage, ConstraintSet.BOTTOM, 30)
            constraintSet.applyTo(holder.constraintLayout)
        } else {
            holder.postText.visibility = View.VISIBLE
            val constraintSet = ConstraintSet()
            constraintSet.clone(holder.constraintLayout)
            constraintSet.connect(R.id.postLikeButton, ConstraintSet.TOP, R.id.postText, ConstraintSet.BOTTOM, 30)
            constraintSet.applyTo(holder.constraintLayout)
            holder.postImage.visibility = View.GONE
        }
        holder.likeCount.text = model.likedBy.size.toString()
        holder.createdAt.text = Utils.getTimeAgo(model.createdAt)

        val auth = Firebase.auth
        val user = auth.currentUser!!.uid
        val isLiked = model.likedBy.contains(user)
        if(isLiked) {
            holder.likeButton.setImageDrawable(ContextCompat.getDrawable(holder.likeButton.context, R.drawable.ic_like))
        } else {
            holder.likeButton.setImageDrawable(ContextCompat.getDrawable(holder.likeButton.context, R.drawable.ic_unlike))
        }
    }
}

interface  IPostAdapter {
    fun onLikeClicked(postId: String)
}