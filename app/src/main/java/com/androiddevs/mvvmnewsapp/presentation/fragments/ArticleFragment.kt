package com.androiddevs.mvvmnewsapp.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.androiddevs.mvvmnewsapp.data.models.Article
import com.androiddevs.mvvmnewsapp.databinding.FragmentArticleBinding
import com.androiddevs.mvvmnewsapp.presentation.newsActivity.NewsActivity
import com.androiddevs.mvvmnewsapp.presentation.newsActivity.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "ArticleFragment"
@AndroidEntryPoint
class ArticleFragment : Fragment() {

    private lateinit var viewModel: NewsViewModel
    private lateinit var binding: FragmentArticleBinding
    private lateinit var article: String
    private val args by navArgs<ArticleFragmentArgs>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentArticleBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel
        setViews()
    }

    private fun setViews() {
        article = args.article
        binding.webView.apply {
            webViewClient = WebViewClient()
            loadUrl(article)
        }
    }
}