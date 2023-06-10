package com.androiddevs.mvvmnewsapp.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.androiddevs.mvvmnewsapp.data.api.Resource
import com.androiddevs.mvvmnewsapp.databinding.FragmentBreakingNewsBinding
import com.androiddevs.mvvmnewsapp.presentation.newsActivity.NewsActivity
import com.androiddevs.mvvmnewsapp.presentation.newsActivity.NewsViewModel
import com.androiddevs.mvvmnewsapp.presentation.recyclerViews.NewsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BreakingNewsFragment : Fragment() {

    private lateinit var viewModel: NewsViewModel
    private lateinit var myAdapter: NewsAdapter
    private lateinit var binding: FragmentBreakingNewsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBreakingNewsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel
        setupRecyclerView()
        setObservers()
    }


    private fun setObservers() {
        viewModel.breakingNews.observe(viewLifecycleOwner) { response ->
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
                        BreakingNewsFragmentDirections.actionBreakingNewsFragmentToArticleFragment(
                            article.url
                        )
                    )
            }
        )

        binding.rvBreakingNews.apply {
            adapter = myAdapter
            layoutManager = LinearLayoutManager(activity)
        }

    }

}