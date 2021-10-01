package com.humaturk.newscodecase.ui.source

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.humaturk.newscodecase.data.model.NewsSource
import com.humaturk.newscodecase.databinding.ItemSourceBinding

class NewsSourceAdapter(private val listener: NewsSourceItemListener) :
    RecyclerView.Adapter<NewsSourceAdapter.NewsSourceViewHolder>() {

    interface NewsSourceItemListener {
        fun onClickedNewsSource(newsSourceId: String)
    }

    private val items = ArrayList<NewsSource>()

    fun setItems(items: List<NewsSource>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsSourceViewHolder {
        val binding: ItemSourceBinding =
            ItemSourceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsSourceViewHolder(binding, listener)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: NewsSourceViewHolder, position: Int) =
        holder.bind(items[position])

    inner class NewsSourceViewHolder(
        private val itemBinding: ItemSourceBinding,
        private val listener: NewsSourceItemListener
    ) : RecyclerView.ViewHolder(itemBinding.root),
        View.OnClickListener {

        private lateinit var newsSource: NewsSource

        init {
            itemBinding.root.setOnClickListener(this)
        }

        fun bind(item: NewsSource) {
            this.newsSource = item
            itemBinding.tvHeader.text = item.name
            itemBinding.tvDescription.text = item.description
        }

        override fun onClick(v: View?) {
            listener.onClickedNewsSource(newsSource.id)
        }
    }
}

