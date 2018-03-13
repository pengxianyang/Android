package com.example.administrator.lab6;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;

import java.util.List;

/**
 * Created by Administrator on 2017/12/20.
 */

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

    private Context context;
    private List<Info>list;
    private OnItemClickListener onItemClickListener = null;

    public CardAdapter(Context context,List<Info> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public CardAdapter.CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CardViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_cardview,parent,false));
    }


    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {

        holder.textView_1.setText(list.get(position).s1);
        holder.textView_2.setText(list.get(position).s2);
        holder.textView_3.setText(list.get(position).s3);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnItemClickListener
    {
        void onClick(int position);
        void onLongClick(int position);
    }

    public void Remove(int position)
    {
        list.remove(position);
        notifyItemRemoved(position);
    }

    // 创建Holder类
    public static class CardViewHolder extends RecyclerView.ViewHolder{

        TextView textView_1;
        TextView textView_2;
        TextView textView_3;

        public CardViewHolder(View itemView) {
            super(itemView);
            textView_1 = (TextView) itemView.findViewById(R.id.textView_1);
            textView_2 = (TextView) itemView.findViewById(R.id.textView_2);
            textView_3 = (TextView) itemView.findViewById(R.id.textView_3);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }


    }
}


