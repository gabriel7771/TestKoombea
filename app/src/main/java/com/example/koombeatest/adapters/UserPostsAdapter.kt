package com.example.koombeatest.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.example.koombeatest.data.remote.Data
import com.example.koombeatest.databinding.UserItemBinding
import com.example.koombeatest.ui.dialogs.PopupImageDialog
import com.example.koombeatest.utils.CustomItemDecorator
import com.example.koombeatest.utils.addBlurEffect
import com.example.koombeatest.utils.shortDate
import kotlinx.android.synthetic.main.user_item.view.*
import timber.log.Timber

class UserPostsAdapter (private val onClickListener: OnClickListener) : ListAdapter<Data, UserPostsAdapter.UserPostsViewHolder>(DiffCallback) {

    class UserPostsViewHolder(private var binding: UserItemBinding) :
            RecyclerView.ViewHolder(binding.root) {
                fun bind(userData: Data) {
                    userData.post.date = shortDate(userData.post.date)
                    binding.userData = userData
                    binding.executePendingBindings()
                }
            }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserPostsViewHolder {
        return UserPostsViewHolder(UserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: UserPostsViewHolder, position: Int) {
        val userData = getItem(position)
        val context = holder.itemView.context
        holder.itemView.big_image_view.setOnClickListener {
            onClickListener.onClick(userData)
        }
        Timber.d("Pics size: ${userData.post.pics.size}")
        when(userData.post.pics.size){
            1 -> {
                holder.itemView.medium_images_rv.visibility = View.GONE
                holder.itemView.big_image_view.visibility = View.VISIBLE
                holder.bind(userData)
            }
            2 -> {
                holder.itemView.medium_images_rv.visibility = View.VISIBLE
                holder.itemView.big_image_view.visibility = View.GONE
                val mediumPictures = userData.post.pics
                Timber.d("Holder item view width: ${holder.itemView.layoutParams.width}")
                setupInnerHorizontalRV(holder.itemView.medium_images_rv, context, mediumPictures)
                holder.bind(userData)
            }
            else -> {
                holder.itemView.medium_images_rv.visibility = View.VISIBLE
                holder.itemView.big_image_view.visibility = View.VISIBLE
                val mediumPictures = userData.post.pics.toMutableList().drop(1)
                Timber.d("Holder item view width: ${holder.itemView.layoutParams.width}")
                setupInnerHorizontalRV(holder.itemView.medium_images_rv, context, mediumPictures)
                holder.bind(userData)
            }
        }
    }

    class OnClickListener(val clickListener: (userData : Data) -> Unit){
        fun onClick(userData: Data) = clickListener(userData)
    }


    companion object DiffCallback : DiffUtil.ItemCallback<Data>() {
        override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem.uid === newItem.uid
        }

        override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, DiffCallback)

    fun submitListDiffer(list: List<Data>) = differ.submitList(list)

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private fun setupInnerHorizontalRV(recyclerView: RecyclerView, context: Context, list: List<String>){
        val horizontalPostAdapter = HorizontalPostAdapter(list, HorizontalPostAdapter.OnClickListener { picture ->
            Timber.d("Clicked on medium picture: $picture")
            addBlurEffect(recyclerView.rootView)
            PopupImageDialog(context, picture, recyclerView.rootView).show()
        } )
        recyclerView.adapter = horizontalPostAdapter
        if(recyclerView.itemDecorationCount == 0) {
            recyclerView.addItemDecoration(CustomItemDecorator(0, 0))
        }
        recyclerView.layoutManager = object : LinearLayoutManager(context, HORIZONTAL, false) {
            override fun checkLayoutParams(lp: RecyclerView.LayoutParams?): Boolean {
                Timber.d("Width: $width")
                lp?.width = (width * 0.48).toInt()
                lp?.height = (width * 0.48).toInt()
                return true
            }
        }
        horizontalPostAdapter.submitListDiffer(list)
    }
}