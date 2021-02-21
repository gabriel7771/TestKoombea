package com.example.koombeatest.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.koombeatest.R
import com.example.koombeatest.databinding.MediumImageItemBinding
import kotlinx.android.synthetic.main.medium_image_item.view.*
import timber.log.Timber

class HorizontalPostAdapter (private val onClickListener: OnClickListener) : ListAdapter<String, HorizontalPostAdapter.HorizontalPostViewHolder>(DiffCallback) {

    lateinit var pictureList : List<String>

    constructor(pictureList: List<String>) : this(OnClickListener {  }) {
        Timber.d("Constructor pictureList: ${pictureList}")
        this.pictureList = pictureList
    }

    class HorizontalPostViewHolder(private var binding: MediumImageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(picture: String) {
            binding.picture = picture
            binding.executePendingBindings()
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HorizontalPostViewHolder {
        /*
        val inflater = LayoutInflater.from(parent.context)
        //val itemView = inflater.inflate(R.layout.medium_image_item, parent, false)
        val itemView = MediumImageItemBinding.inflate(inflater).root
        val layoutParams = itemView.layoutParams
        layoutParams.width = (parent.width * 0.3).toInt()
        itemView.layoutParams = layoutParams
        return HorizontalPostViewHolder(itemView)
        */
        return HorizontalPostViewHolder(MediumImageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: HorizontalPostViewHolder, position: Int) {
        Timber.d("Picture list: ${pictureList}")
        val picture = pictureList[position]
        holder.itemView.medium_image.setOnClickListener {
            onClickListener.onClick(picture)
        }
        Timber.d("Medium picture : $picture")
        holder.bind(picture)
    }

    class OnClickListener(val clickListener: (picture : String) -> Unit){
        fun onClick(picture: String) = clickListener(picture)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, DiffCallback)

    fun submitListDiffer(list: List<String>) = differ.submitList(list)

    override fun getItemCount(): Int {
        return pictureList.size
    }
}