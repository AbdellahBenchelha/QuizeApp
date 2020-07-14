package com.example.quizapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.quizapp.R;
import com.example.quizapp.Util;
import com.example.quizapp.model.LeaderListItem;

import java.util.List;

public class HistoriqueAdapter extends RecyclerView.Adapter<HistoriqueAdapter.MyViewHolder>{

    List<LeaderListItem> leaderBoardList;
    private Context context;



    public HistoriqueAdapter(List<LeaderListItem> leaderBoardList, Context context) {
        this.leaderBoardList = leaderBoardList;
        this.context = context;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        ImageView imgPersonImage;
        TextView tvPersonName, tvPersonPoing, tvPersonRank;
        public MyViewHolder(View v) {
            super(v);
            imgPersonImage = itemView.findViewById(R.id.img_leaderboard_person);
            tvPersonName = itemView.findViewById(R.id.tv_leaderboard_name);
            tvPersonPoing = itemView.findViewById(R.id.tv_leaderboard_point);
            tvPersonRank = itemView.findViewById(R.id.tv_leaderboard_rank);
        }
    }
    @NonNull
    @Override
    public HistoriqueAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_leaderboard_recycler_row, parent, false);
        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.tvPersonName.setText(leaderBoardList.get(position).getDate().toString());
        holder.tvPersonRank.setText(String.valueOf(leaderBoardList.get(position).getScore()));
        holder.tvPersonPoing.setText(leaderBoardList.get(position).getQuize());
        Glide.with(context).load(leaderBoardList.get(position).getPhoto()).placeholder(R.drawable.avater).into(holder.imgPersonImage);


    }

    @Override
    public int getItemCount()
        {
            return leaderBoardList.size();
    }
}
