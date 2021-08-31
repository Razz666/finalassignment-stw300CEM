package com.rajkumarmagar.locationbasedanythingfinder.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rajkumarmagar.locationbasedanythingfinder.R
import com.rajkumarmagar.locationbasedanythingfinder.adapter.PostAdapter
import com.rajkumarmagar.locationbasedanythingfinder.repository.PostRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val homeRecyclerView: RecyclerView = root.findViewById(R.id.homeRecyclerView)

        val context: Context = requireContext()

        CoroutineScope(Dispatchers.IO).launch {
            val repository = PostRepository()
            val response = repository.getAllPost(context)

            withContext(Dispatchers.Main) {
                val adapter = PostAdapter(response, context)
                homeRecyclerView.layoutManager = LinearLayoutManager(context)
                homeRecyclerView.adapter = adapter
            }
        }

        return root
    }
}