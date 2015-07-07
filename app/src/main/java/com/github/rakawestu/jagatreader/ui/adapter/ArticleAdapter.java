package com.github.rakawestu.jagatreader.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.rakawestu.jagatreader.R;
import com.github.rakawestu.jagatreader.model.Article;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * @author rakawm
 */
public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>{
    private Context context;
    private List<Article> articles;

    public ArticleAdapter(Context context) {
        articles = new ArrayList<>();
        this.context = context;
    }

    public Article getItemData(int position){
        return articles.get(position);
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public void addArticles(List<Article> articles) {
        this.articles.addAll(articles);
    }

    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ArticleViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_article, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ArticleViewHolder articleViewHolder, int i) {
        Article article = articles.get(i);
        articleViewHolder.title.setText(article.getTitle());
        articleViewHolder.date.setText(article.getDateTime());
        if(article.getImageUrl()!=null) {
            Picasso.with(context)
                    .load(article.getImageUrl())
                    .into(articleViewHolder.image);
        } else {
            articleViewHolder.image.setVisibility(View.GONE);
        }
        articleViewHolder.creator.setText(article.getCreator());
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    class ArticleViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.article_title)
        TextView title;
        @InjectView(R.id.article_date)
        TextView date;
        @InjectView(R.id.article_image)
        ImageView image;
        @InjectView(R.id.article_creator)
        TextView creator;

        public ArticleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }
}
