package com.example.quizapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.R;
import com.example.quizapp.Util;

public class QuizModelAdapter extends RecyclerView.Adapter<QuizModelAdapter.MyViewHolder>{

    private String[] Options;
    private Context context;
    private int rowindex=-1;


    public QuizModelAdapter(String[] options, Context context) {
        Options = options;
        this.context = context;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        private ImageView quizImageOverLap;
        public MyViewHolder(View v) {
            super(v);
            mTextView = v.findViewById(R.id.tv_quiz_option_name);
            quizImageOverLap = itemView.findViewById(R.id.img_quiz_overlap);
        }
    }
    @NonNull
    @Override
    public QuizModelAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.quiz_options_recycler_row, parent, false);
        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.mTextView.setText(Options[position]);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rowindex=position;
                notifyDataSetChanged();

            }
        });

         if(rowindex==position)
        {
            Util.ITEMSELECT=position;
            holder.mTextView.setTextColor(holder.itemView.getResources().getColor(R.color.color_white));
            holder.quizImageOverLap.setVisibility(View.VISIBLE);
        }else
        {
            holder.mTextView.setTextColor(holder.itemView.getResources().getColor(R.color.color_text));

            holder.quizImageOverLap.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return Options.length;
    }
}
