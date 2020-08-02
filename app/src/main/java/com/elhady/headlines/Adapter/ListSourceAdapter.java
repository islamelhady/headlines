package com.elhady.headlines.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.elhady.headlines.Interface.ItemClickListener;
import com.elhady.headlines.Models.Article;
import com.elhady.headlines.NewsDetails;
import com.elhady.headlines.R;
import com.elhady.headlines.Utils;

import java.util.List;

class ListSourceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    ItemClickListener itemClickListener;

    TextView source_title, source_name, source_publishAt, source_time;
    TextView source_desc;
    ImageView source_img;
    TextView source_author;
    ProgressBar progressBar;

    public ListSourceViewHolder(@NonNull View itemView) {
        super(itemView);

        source_title = itemView.findViewById(R.id.title);
        source_desc = itemView.findViewById(R.id.desc);
        source_author = itemView.findViewById(R.id.author);
        source_name = itemView.findViewById(R.id.source);
        source_publishAt = itemView.findViewById(R.id.publishedAt);
        source_img = itemView.findViewById(R.id.img);
        progressBar = itemView.findViewById(R.id.prograss_load_photo);

        itemView.setOnClickListener(this);
    }


    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);

    }
}
public class ListSourceAdapter extends RecyclerView.Adapter<ListSourceViewHolder>{

    private Context context;
    private List<Article> articles;

    public ListSourceAdapter(Context context, List<Article> articles) {
        this.context = context;
        this.articles = articles;
    }

    @NonNull
    @Override
    public ListSourceViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.source_layout, viewGroup, false);
        return new ListSourceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListSourceViewHolder holder, int position) {

       // Glide.with(context).load(articles.get(position).getUrlToImage()).into(holder.source_img);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(Utils.getRandomDrawbleColor());
        requestOptions.error(Utils.getRandomDrawbleColor());
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        requestOptions.centerCrop();

        Glide.with(context)
                .load(articles.get(position).getUrlToImage())
                .apply(requestOptions)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.source_img);

        holder.source_title.setText(articles.get(position).getTitle());
        holder.source_desc.setText(articles.get(position).getDescription());
        holder.source_author.setText(articles.get(position).getAuthor());
        holder.source_name.setText(articles.get(position).getSource().getName());
        holder.source_publishAt.setText(articles.get(position).getPublishedAt());

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClicked) {

                Intent intent = new Intent(context,NewsDetails.class);

                intent.putExtra("url",articles.get(position).getUrl());
                intent.putExtra("title",articles.get(position).getTitle());
                intent.putExtra("img",articles.get(position).getUrlToImage());
                intent.putExtra("date",articles.get(position).getPublishedAt());
                intent.putExtra("source",articles.get(position).getSource().getName());
                intent.putExtra("author",articles.get(position).getAuthor());

                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return articles.size();
    }
}
