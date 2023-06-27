package com.dicoding.githubuserexplorer.ui.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dicoding.githubuserexplorer.FollowFragment

class SectionPageAdapter(activity: AppCompatActivity): FragmentStateAdapter(activity){

    var username :String = ""
    override fun createFragment(position: Int): Fragment {

        var fragment: Fragment = FollowFragment()
        fragment.arguments = Bundle().apply {
            putInt(FollowFragment.ARG_POSITION, position+1)
            putString(FollowFragment.ARG_USERNAME, username)
        }
        return fragment
    }

    override fun getItemCount(): Int {
        return 2
    }
}