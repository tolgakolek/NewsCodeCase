package com.humaturk.newscodecase.ui.article

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.humaturk.newscodecase.R
import com.humaturk.newscodecase.data.model.Article
import com.humaturk.newscodecase.databinding.FragmentArticleBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ArticleFragment : Fragment(), ArticleAdapter.ArticleItemListener {

    private val viewModel: ArticleViewModel by viewModels()
    private var articleAdapter: ArticleAdapter? = null
    private val binding: FragmentArticleBinding by viewBinding()
    private var articles : List<Article> ?= emptyList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activityBar = (activity as AppCompatActivity).supportActionBar
        activityBar?.setDisplayHomeAsUpEnabled(true)
        arguments?.getString("id")?.let {
            viewModel.start(it)
            activityBar?.setTitle("${it.uppercase()} ARTICLES")
            articleAdapter = ArticleAdapter(this)
            lifecycleScope.launchWhenResumed {
                launch {
                    viewModel.viewState.collect {
                        if(!it.articles.equals(articles)){
                            articles = it.articles
                            articleAdapter?.let {
                                it.setItems(articles!!)
                            }
                            if(viewModel.viewState.value.newArticalCheck){
                                Toast.makeText(requireContext(),"Yeni Haberler Yüklendi.",Toast.LENGTH_LONG).show()
                                viewModel.newArticleCheckNot()
                            }
                        }
                    }
                }
            }
        }
        binding.articleRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.articleRecyclerView.adapter = articleAdapter
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_article, container, false)
    }

    override fun onClickedNewsSource(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    override fun addReadListArticle(article: Article) {
        viewModel.addArticleDao(article)
    }

    override fun deleteReadListArticleByID(articleId: String) {
        viewModel.deleteArticleDao(articleId)
    }
}