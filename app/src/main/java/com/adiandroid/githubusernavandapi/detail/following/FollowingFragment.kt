package com.adiandroid.githubusernavandapi.detail.following

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.adiandroid.githubusernavandapi.R
import com.adiandroid.githubusernavandapi.databinding.UserfollowFragmentBinding
import com.adiandroid.githubusernavandapi.main.ListDataAdapter
import com.adiandroid.githubusernavandapi.detail.DetailUser
import com.adiandroid.githubusernavandapi.model.User

class FollowingFragment : Fragment(R.layout.userfollow_fragment) {

    private var _binding: UserfollowFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: FollowingViewModel
    private lateinit var adapter: ListDataAdapter
    private lateinit var username: String

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = arguments
        username = args?.getString(DetailUser.EXTRA_USERNAME).toString()

        _binding = UserfollowFragmentBinding.bind(view)

        adapter = ListDataAdapter()
        adapter.setOnItemClickCallBack(object : ListDataAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                val intent = Intent(requireContext(), DetailUser::class.java)
                intent.putExtra(DetailUser.EXTRA_USERNAME, data.login)
                intent.putExtra(DetailUser.EXTRA_ID, data.id)
                intent.putExtra(DetailUser.EXTRA_AVATAR, data.avatarUrl)
                intent.putExtra(DetailUser.EXTRA_URL, data.htmlUrl)
                startActivity(intent)
            }
        })

        binding.apply {
            userRv.setHasFixedSize(true)
            userRv.layoutManager = LinearLayoutManager(activity)
            userRv.adapter = adapter
        }

        viewModel = ViewModelProvider(this).get(FollowingViewModel::class.java)
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }

        viewModel.listFollowing.observe(viewLifecycleOwner) { followings ->
            if (followings.isNullOrEmpty()) {
                binding.userRv.visibility = View.GONE
                binding.imageViewNoFollowing.visibility = View.VISIBLE
            } else {
                binding.userRv.visibility = View.VISIBLE
                binding.imageViewNoFollowing.visibility = View.GONE
                adapter.setList(followings)
            }
        }

        viewModel.setListFollowing(username) { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }
}
