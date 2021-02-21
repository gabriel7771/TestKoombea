package com.example.koombeatest.adapters

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.koombeatest.R
import com.example.koombeatest.data.remote.Data
import timber.log.Timber
import javax.inject.Inject



@BindingAdapter("listData")
fun bindDataRecyclerView(recyclerView: RecyclerView, data: List<Data>?){
    Timber.d("List to show in recycler view: $data")
    val adapter = recyclerView.adapter as UserPostsAdapter
    adapter.submitList(data)
}

@BindingAdapter("listString")
fun bindStringRecyclerView(recyclerView: RecyclerView, data: List<String>?){
    Timber.d("List to show in recycler view: $data")
    recyclerView.adapter ?: return
    val adapter = recyclerView.adapter as HorizontalPostAdapter
    adapter.submitList(data)
}

@BindingAdapter("loadUrl")
fun loadUrl(imageView: ImageView, url:String?) {
    if(url.isNullOrEmpty()) {return}
    Glide.with(imageView.context).setDefaultRequestOptions(
        RequestOptions()
            .placeholder(R.drawable.ic_image)
            .error(R.drawable.ic_image)
    ).load(url).into(imageView)
}