package com.dicoding.githubuserexplorer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuserexplorer.data.remote.ItemsItem
import com.dicoding.githubuserexplorer.databinding.FragmentFollowBinding
import com.dicoding.githubuserexplorer.ui.adapter.UserAdapter
import com.dicoding.githubuserexplorer.ui.main.MainViewModel
import com.google.android.material.snackbar.Snackbar

private lateinit var binding: FragmentFollowBinding

class FollowFragment : Fragment() {

    companion object {
        const val ARG_POSITION = "section_number"
        const val ARG_USERNAME = "key_username"
        private var position = 0
        var username = "initial_uname"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val mainViewModel = ViewModelProvider(this,
            ViewModelProvider.NewInstanceFactory())[MainViewModel::class.java]

        mainViewModel.listUserFollowers.observe(viewLifecycleOwner) {listUserFollow ->
            setFollowData(listUserFollow)}

        mainViewModel.listUserFollowing.observe(viewLifecycleOwner) {listUserFollow ->
            setFollowData(listUserFollow)}

        mainViewModel.isLoading.observe(viewLifecycleOwner){showLoading(it)}
        mainViewModel.snackbarError.observe(viewLifecycleOwner){
            it.getContentIfNotHandled()?.let { snackBarText ->
                Snackbar.make(binding.root,snackBarText, Snackbar.LENGTH_SHORT).setTextMaxLines(5).show()
            }
        }

        binding = FragmentFollowBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME, username)
        }

        val mainViewModel = ViewModelProvider(this,
            ViewModelProvider.NewInstanceFactory())[MainViewModel::class.java]

        if (position == 1){
            mainViewModel.findUserFollow(position,username)
        }else{
            mainViewModel.findUserFollow(position,username)
        }

        binding.rvFollow.setHasFixedSize(true)
        binding.rvFollow.layoutManager = LinearLayoutManager(requireActivity())
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }

    private fun setFollowData(listUserFollow : List<ItemsItem>) {
        val userFollowAdapter = UserAdapter(listUserFollow)
        binding.rvFollow.adapter = userFollowAdapter
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBarFollow.visibility = View.VISIBLE
        } else {
            binding.progressBarFollow.visibility = View.GONE
        }
    }

}