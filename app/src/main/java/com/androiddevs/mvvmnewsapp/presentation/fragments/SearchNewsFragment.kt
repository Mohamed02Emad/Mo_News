package com.androiddevs.mvvmnewsapp.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
                        viewModel.searchForNews(editable.toString())
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
                        myAdapter.differ.submitList(it.articles.toList())
                        val totalPages = it.totalResults / 20 + 2   // 20 is page size
                        isLastPage = viewModel.searchNewsPage == totalPages
                        if (isLastPage) {
                            binding.rvSearchNews.setPadding(0, 0, 0, 40)
                        }
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
        isLoading = false
    }

    private fun showProgressbar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }


    private fun setupRecyclerView() {
        myAdapter = NewsAdapter(
            NewsAdapter.OnArticleClickListener { article ->
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
            addOnScrollListener(this@SearchNewsFragment.scrollListener)

        }

    }


    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    val scrollListener = object : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= 20 //page size

            val shouldPaginate =
                isLastItem && isNotAtBeginning && isNotLoadingAndNotLastPage && isTotalMoreThanVisible && isScrolling

            if (shouldPaginate) {
                viewModel.searchForNews(binding.etSearch.text.toString())
                isScrolling = false
            }

        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }


}