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
import com.example.stopgamerssapp.Model.StopGameNews;
import com.example.stopgamerssapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

class FeedViewHolder extends RecyclerView.ViewHolder {
    public TextView txtTitle;
    public TextView txtContent;
    public TextView txtPubDate;
    public ImageView imgNews;

    public FeedViewHolder(@NonNull View itemView, ItemClickListener itemClickListener) {
        super(itemView);

        txtTitle = itemView.findViewById(R.id.txtTitle);
        txtContent = itemView.findViewById(R.id.txtContent);
        txtPubDate = itemView.findViewById(R.id.txtPubDate);
        imgNews = itemView.findViewById(R.id.imgNews);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemClickListener != null){
                    int position = getAdapterPosition();

                    if(position != RecyclerView.NO_POSITION){
                        itemClickListener.itemOnClick(position);
                    }
                }
            }
        });
    }
}

public class FeedAdapter extends RecyclerView.Adapter<FeedViewHolder>{
    private ArrayList<StopGameNews> stopGameNews;
    private Context mContext;
    private LayoutInflater layoutInflater;
    private final ItemClickListener itemClickListener;

    public FeedAdapter(ArrayList<StopGameNews> stopGameNews, Context mContext, ItemClickListener itemClickListener) {
        this.stopGameNews = stopGameNews;
        this.mContext = mContext;
        layoutInflater = LayoutInflater.from(mContext);
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewHolder = layoutInflater.inflate(R.layout.row, parent, false);
        return new FeedViewHolder(viewHolder, itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedViewHolder holder, int position) {
        holder.txtTitle.setText(stopGameNews.get(position).getTitle());
        holder.txtPubDate.setText(stopGameNews.get(position).getPubDate());
        holder.txtContent.setText(stopGameNews.get(position).getContent());

        String imgUrl = stopGameNews.get(position).getImageURL();
        Picasso.get().load(imgUrl).into(holder.imgNews);
    }

    @Override
    public int getItemCount() {
        return stopGameNews.size();
    }
}
