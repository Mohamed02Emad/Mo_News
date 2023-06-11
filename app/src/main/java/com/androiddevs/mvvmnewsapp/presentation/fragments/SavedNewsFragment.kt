package com.androiddevs.mvvmnewsapp.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androiddevs.mvvmnewsapp.databinding.FragmentSavedNewsBinding
import com.androiddevs.mvvmnewsapp.presentation.newsActivity.NewsActivity
import com.androiddevs.mvvmnewsapp.presentation.newsActivity.NewsViewModel
import com.androiddevs.mvvmnewsapp.presentation.recyclerViews.NewsAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SavedNewsFragment : Fragment() {

    private lateinit var viewModel: NewsViewModel
    private lateinit var binding: FragmentSavedNewsBinding
    private lateinit var myAdapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSavedNewsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel
        setupRecyclerView()
        setObservers()
    }

    private fun setObservers() {
        viewModel.getAllArticles().observe(viewLifecycleOwner) {
            myAdapter.differ.submitList(it)
        }
    }

    private fun setupRecyclerView() {
        myAdapter = NewsAdapter(
            NewsAdapter.OnArticleClickListener { article ->
                findNavController().navigate(
                    SavedNewsFragmentDirections.actionSavedNewsFragmentToArticleFragment(
                        article
                    )
                )
            }
        )

        binding.rvSavedNews.apply {
            adapter = myAdapter
            layoutManager = LinearLayoutManager(activity)
        }


        val itemTouchHelperCallBack = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
               return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article = myAdapter.differ.currentList[position]
                viewModel.deleteArticle(article)
                Snackbar.make(binding.root,"removed from favourite", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo"){
                        viewModel.insertAndUpdateArticle(article)
                    }
                    show()
                }

            }

        }

        ItemTouchHelper(itemTouchHelperCallBack).apply {
            attachToRecyclerView(binding.rvSavedNews)
        }


    }
}