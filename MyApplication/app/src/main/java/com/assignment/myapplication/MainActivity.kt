package com.assignment.myapplication

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.assignment.myapplication.data.NetworkResultState
import com.assignment.myapplication.databinding.ActivityMainBinding
import com.assignment.myapplication.domain.Constant.setVisible
import com.assignment.myapplication.domain.Question
import com.assignment.myapplication.presentation.viewmodel.SearchViewModel
import com.assignment.myapplication.presentation.adapter.SearchResultAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var adapter: SearchResultAdapter
    private lateinit var searchViewModel: SearchViewModel
    private lateinit var handler: Handler
    private val delay: Long = 500

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initializeUI()
    }

    /*
      Function to initialize UI elements and setup the ViewModel
     */
    private fun initializeUI() {

        adapter = SearchResultAdapter(emptyList())
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter
        }
        // Initialize ViewModel
        searchViewModel = ViewModelProvider(this)[SearchViewModel::class.java]

        // Initialize handler for handling delay
        handler = Handler(Looper.getMainLooper())

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                handler.removeCallbacksAndMessages(null)
            }

            override fun onTextChanged(
                charSequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
            }

            override fun afterTextChanged(editable: Editable?) {
                handler.postDelayed({
                    val keyword = editable.toString()
                    if (keyword.isNotEmpty()) {
                        searchViewModel.searchResults(keyword)
                    } else {
                        adapter.clearData()
                    }
                }, delay)
            }
        })

        // Observe ViewModel for changes in search results
        searchViewModel.questions.observe(this, Observer { result->
            handleSearchResults(result)
        })
    }


    // Function to handle different states of search results
    private fun handleSearchResults(result: NetworkResultState<List<Question>>) {
        when (result) {
            is NetworkResultState.Success -> {
                if(result.data.isEmpty()) {
                    binding.tvError.setVisible(true)
                    binding.tvError.text = getString(R.string.no_result_found)
                } else {
                    binding.tvError.setVisible(false)
                }
                adapter.updateData(result.data)
                binding.recyclerView.setVisible(true)
                binding.progressBar.setVisible(false)
            }
            is NetworkResultState.Error -> {
                binding.recyclerView.setVisible(true)
                binding.progressBar.setVisible(false)
                binding.tvError.setVisible(false)
                Toast.makeText(this, result.error, Toast.LENGTH_LONG).show()
            }
            is NetworkResultState.Loading -> {
                binding.progressBar.setVisible(true)
                binding.recyclerView.setVisible(false)
                binding.tvError.setVisible(false)
            }
        }
    }



}