package com.example.koombeatest.ui

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.koombeatest.R
import com.example.koombeatest.adapters.UserPostsAdapter
import com.example.koombeatest.databinding.FragmentUserPostsBinding
import com.example.koombeatest.databinding.PopupImageBinding
import com.example.koombeatest.ui.dialogs.PopupImageDialog
import com.example.koombeatest.utils.Resource
import com.example.koombeatest.utils.Status
import com.example.koombeatest.utils.addBlurEffect
import jp.wasabeef.blurry.Blurry
import timber.log.Timber
import javax.inject.Inject

class UserPostsFragment @Inject constructor(
    var viewModel: UserPostsViewModel?
) : Fragment() {

    constructor() : this(null)

    private lateinit var binding: FragmentUserPostsBinding

    private lateinit var userPostsAdapter: UserPostsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_user_posts, container, false
        )

        binding.lifecycleOwner = this

        viewModel = viewModel ?: ViewModelProvider(requireActivity()).get(UserPostsViewModel::class.java)

        binding.viewModel = viewModel

        setupRecyclerView()
        subscribeToObservers()

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel?.refreshUserPosts()
            binding.swipeRefreshLayout.isRefreshing = false
        }

        return binding.root
    }

    private fun subscribeToObservers(){
        viewModel?.userPosts?.observe(viewLifecycleOwner, {
            Timber.d("Data arrived to the fragment: %s", it.data)
            it.data?.let { userPost -> userPostsAdapter.submitListDiffer(userPost.data) }
            if(it.status == Status.ERROR){
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
            }
            if(it.data == null){
                startShimmer()
            }
        })
        viewModel?.status?.observe(viewLifecycleOwner, {
            when(it) {
                Status.LOADING -> {
                    Timber.d("Loading")
                    startShimmer()
                }
                Status.ERROR -> {
                    Timber.d("Loading error")
                    startShimmer()
                }
                Status.SUCCESS -> {
                    Timber.d("Loading complete")
                    stopShimmer()
                }
                else -> {}
            }
        })
    }

    private fun setupRecyclerView(){
        binding.userPostsRv.apply {
            adapter = UserPostsAdapter(UserPostsAdapter.OnClickListener{
                Timber.d("Clicked on big picture: ${it.post.pics[0]}")
                addBlurEffect(binding.root)
                PopupImageDialog(requireContext(), it.post.pics[0], binding.root).show()
            })
            userPostsAdapter = adapter as UserPostsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun startShimmer(){
        binding.userPostsRv.visibility = View.GONE
        binding.shimmerLayout.visibility = View.VISIBLE
        binding.shimmerLayout.startShimmer()
    }

    private fun stopShimmer(){
        binding.userPostsRv.visibility = View.VISIBLE
        binding.shimmerLayout.visibility = View.GONE
        binding.shimmerLayout.stopShimmer()
    }
}