package com.mycompany.project_mid;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Administrator on 2017/10/20 0020.
 */

public abstract class CommonAdapter<T> extends RecyclerView.Adapter<ViewHolder>{

    private Context mContext;
    private  int mLayoutId;
    private  List<T> mDatas;
    private  OnItemClickListener mOnItemClickListener = null;

    public CommonAdapter(Context context, int layoutId, List<T> datas){
        mContext = context;
        mLayoutId = layoutId;
        mDatas = datas;
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType){
        ViewHolder viewHolder = ViewHolder.get(mContext, parent, mLayoutId);
        return  viewHolder;
    }

    public abstract void convert(ViewHolder holder, T t);

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position){
        convert(holder, mDatas.get(position));

        if(mOnItemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    mOnItemClickListener.onClick(holder.getAdapterPosition());
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public  boolean onLongClick(View v){
                    mOnItemClickListener.onLongClick(holder.getAdapterPosition());
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount(){return mDatas.size();}

}
