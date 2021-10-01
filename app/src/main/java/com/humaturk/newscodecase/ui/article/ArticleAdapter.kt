package com.humaturk.newscodecase.ui.article

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.humaturk.newscodecase.data.model.Article
import com.humaturk.newscodecase.databinding.ItemArticleBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ArticleAdapter(private val listener: ArticleItemListener) :
    RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {

    interface ArticleItemListener {
        fun onClickedNewsSource(newsSourceId: String)
        fun addReadListArticle(article: Article)
        fun deleteReadListArticleByID(articleId: String)
    }

    private val items = ArrayList<Article>()

    fun setItems(items: List<Article>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding: ItemArticleBinding =
            ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(binding, listener)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) =
        holder.bind(items[position])

    inner class ArticleViewHolder(
        private val itemBinding: ItemArticleBinding,
        private val listener: ArticleItemListener
    ) : RecyclerView.ViewHolder(itemBinding.root),
        View.OnClickListener {

        private lateinit var article: Article

        init {
            itemBinding.root.setOnClickListener(this)
            itemBinding.tvAddReadList.setOnClickListener {
                if (article.readListStatus) {
                    itemBinding.tvAddReadList.text = "Okuma Listeme Ekle"
                    listener.deleteReadListArticleByID(article.id)
                    article.readListStatus = false
                } else {
                    itemBinding.tvAddReadList.text = "Okuma Listemden Çıkar"
                    article.readListStatus = true
                    listener.addReadListArticle(article)
                }
            }
        }

        fun bind(item: Article) {
            this.article = item
            if (this.article.id == null) {
                this.article.id = UUID.randomUUID().toString().substring(0, 15)
            }
            if (item.readListStatus) {
                itemBinding.tvAddReadList.text = "Okuma Listemden Çıkar"
            } else {
                itemBinding.tvAddReadList.text = "Okuma Listeme Ekle"
            }
            itemBinding.tvArticleHeader.text = item.title
            val parser =  SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            val dateFormat = SimpleDateFormat("HH:mm:ss")
            itemBinding.tvArticleTime.text = dateFormat.format(parser.parse(item.publishedAt))

            Glide.with(itemBinding.root)
                .load(item.urlToImage)
                .centerCrop()
                .into(itemBinding.imgArticle)
        }

        override fun onClick(v: View?) {
            listener.onClickedNewsSource(article.url)
        }
    }
}
