package com.chriskinyua.collaborativeplaylist.ui.dashboard

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chriskinyua.collaborativeplaylist.MainActivity
import com.chriskinyua.collaborativeplaylist.R
import com.chriskinyua.collaborativeplaylist.RecommendationDialog
import com.chriskinyua.collaborativeplaylist.adapter.SearchResultAdapter
import com.chriskinyua.collaborativeplaylist.state.GlobalState
import com.chriskinyua.collaborativeplaylist.ui.SharedViewModel
import com.chriskinyua.shoppinglist.touch.ListRecyclerTouchCallback


class DashboardFragment : Fragment() {

    private lateinit var state: GlobalState
    private lateinit var sharedViewModel: SharedViewModel
    private val TAG = DashboardFragment::class.java.simpleName
    private lateinit var searchResultAdapter: SearchResultAdapter
    private lateinit var rvSearchResults: RecyclerView
    private var myContext: FragmentActivity? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        sharedViewModel = activity?.run {
                ViewModelProviders.of(this)[SharedViewModel::class.java]
            } ?: throw Exception("Invalid Activity")

        rvSearchResults = root.findViewById(R.id.rvSearchResults)
        val searchInput = root.findViewById<EditText>(R.id.searchInput)
        val btnSearch = root.findViewById<Button>(R.id.btnSearch)

        state = activity?.application as GlobalState

        btnSearch.setOnClickListener {
            if(searchInput.text.isNotEmpty()){
                sharedViewModel.performSearch(searchInput.text.toString())
            } else {
                searchInput.error = "Search query cannot be empty"
            }
        }


        initRecyclerView()

        return root
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.myContext = context as MainActivity
    }

    private fun initRecyclerView() {

        searchResultAdapter = SearchResultAdapter(myContext, state.spotifyAppRemote!!)
        rvSearchResults.adapter = searchResultAdapter
        rvSearchResults.layoutManager = LinearLayoutManager(activity)
        val touchCallbackList = ListRecyclerTouchCallback(searchResultAdapter)
        val itemTouchHelper = ItemTouchHelper(touchCallbackList)
        itemTouchHelper.attachToRecyclerView(rvSearchResults)

        val itemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)

        rvSearchResults.addItemDecoration(itemDecoration)

        sharedViewModel.searchResult.observe(this, Observer {
            searchResultAdapter.searchResults = it
            searchResultAdapter.notifyDataSetChanged()
        })
    }


}