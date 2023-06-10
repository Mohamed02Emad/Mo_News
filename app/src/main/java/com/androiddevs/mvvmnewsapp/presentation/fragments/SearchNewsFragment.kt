package com.androiddevs.mvvmnewsapp.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.androiddevs.mvvmnewsapp.data.api.Resource
import com.androiddevs.mvvmnewsapp.databinding.FragmentSearchNewsBinding
import com.androiddevs.mvvmnewsapp.presentation.newsActivity.NewsActivity
import com.androiddevs.mvvmnewsapp.presentation.newsActivity.NewsViewModel
import com.androiddevs.mvvmnewsapp.presentation.recyclerViews.NewsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchNewsFragment : Fragment() {

    private lateinit var myAdapter: NewsAdapter
    private lateinit var viewModel: NewsViewModel
    private lateinit var binding: FragmentSearchNewsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchNewsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel
        setupRecyclerView()
        setOnClicks()
        setObservers()
    }

    private fun setOnClicks() {
        var job: Job? = null
        binding.etSearch.doAfterTextChanged { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(500)
                editable?.let {
                    if (editable.toString().isNotEmpty()) {
                        viewModel.searchForNews(editable.toString(), 1)
                    }
                }
            }
        }

    }

    private fun setObservers() {
        viewModel.searchNews.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Sucess -> {
                    hideProgressbar()
                    response.data?.let {
                        myAdapter.differ.submitList(it.articles)
                    }
                }
                is Resource.Error -> {
                    hideProgressbar()
                    response.message?.let {
                        Log.d("mohamed", "an error occurred ${it}")
                    }
                }
                is Resource.Loading -> {
                    showProgressbar()
                }
            }

        }
    }

    private fun hideProgressbar() {
        binding.paginationProgressBar.visibility = View.INVISIBLE
    }

    private fun showProgressbar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
    }


    private fun setupRecyclerView() {
        myAdapter = NewsAdapter(
            NewsAdapter.OnArticleClickListener {article->
                    findNavController().navigate(
                        SearchNewsFragmentDirections.actionSearchNewsFragmentToArticleFragment(
                            article
                        )
                    )
            }
        )


        binding.rvSearchNews.apply {
            adapter = myAdapter
            layoutManager = LinearLayoutManager(activity)
        }

    }
}