package com.example.stopgamerssapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stopgamerssapp.Interface.ItemClickListener;
import com.example.stopgamerssapp.Model.Feed;
import com.example.stopgamerssapp.Model.RSSObject;
import com.example.stopgamerssapp.R;
import com.squareup.picasso.Picasso;

class FeedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
    public TextView txtTitle;
    public TextView txtContent;
    public TextView txtPubDate;
    public ImageView imgNews;
    private ItemClickListener itemClickListener;

    public FeedViewHolder(@NonNull View itemView) {
        super(itemView);

        txtTitle = itemView.findViewById(R.id.txtTitle);
        txtContent = itemView.findViewById(R.id.txtContent);
        txtPubDate = itemView.findViewById(R.id.txtPubDate);
        imgNews = itemView.findViewById(R.id.imgNews);

        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }

    @Override
    public boolean onLongClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
        return true;
    }
}

public class FeedAdapter extends RecyclerView.Adapter<FeedViewHolder>{
    private RSSObject rssObject;
    private Context mContext;
    private LayoutInflater layoutInflater;

    public FeedAdapter(RSSObject rssObject, Context mContext) {
        this.rssObject = rssObject;
        this.mContext = mContext;
        layoutInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewHolder = layoutInflater.inflate(R.layout.row, parent, false);
        return new FeedViewHolder(viewHolder);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedViewHolder holder, int position) {
        holder.txtTitle.setText(rssObject.getItems().get(position).getTitle());
        holder.txtPubDate.setText(rssObject.getItems().get(position).getPubDate());
        holder.txtContent.setText(rssObject.getItems().get(position).getContent());

        String imgUrl = rssObject.getItems().get(position).getEnclosure().toString();
        Picasso.get().load(imgUrl).into(holder.imgNews);
    }

    @Override
    public int getItemCount() {
        return rssObject.items.size();
    }
}
