package com.humaturk.newscodecase.ui.source

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.humaturk.newscodecase.R
import com.humaturk.newscodecase.databinding.FragmentNewsSourceBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewsSourceFragment : Fragment(), NewsSourceAdapter.NewsSourceItemListener {
    private val viewModel: NewsSourceViewModel by viewModels()
    private var newsSourceAdapter: NewsSourceAdapter? = null
    private val binding: FragmentNewsSourceBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activityBar = (activity as AppCompatActivity).supportActionBar
        activityBar?.setDisplayHomeAsUpEnabled(false)
        activityBar?.setTitle("NEWS SOURCE")
        newsSourceAdapter = NewsSourceAdapter(this).apply {
            lifecycleScope.launchWhenResumed {
                launch {
                    viewModel.viewState.collect {
                        setItems(it.newsSource)
                    }
                }
            }
        }
        binding.newsSourceRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.newsSourceRecyclerView.adapter = newsSourceAdapter
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_news_source, container, false)
    }

    override fun onClickedNewsSource(articleId: String) {
        findNavController().navigate(
            R.id.action_newsSourceFragment_to_articleFragment,
            bundleOf("id" to articleId)
        )
    }
}