package com.example.administrator.lab6;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Administrator on 2017/10/22.
 * */

public abstract class CommonAdapter<T> extends RecyclerView.Adapter<View_Holder>{

    protected Context mContext;
    protected int mLayoutId;
    protected List<T> mDatas;
    protected LayoutInflater mInflater;

    private OnItemClickListener mOnItemClickListener = null;

    public void setmOnItemClickListener(OnItemClickListener onItemClickListener)
    {
        this.mOnItemClickListener=onItemClickListener;
    }


    public CommonAdapter(Context context , int layoutid, List datas)
    {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mLayoutId = layoutid;
        mDatas = datas;
    }

    @Override
    public View_Holder onCreateViewHolder(final ViewGroup parent, int viewType)
    {
        View_Holder view_holder = View_Holder.get(mContext,null,parent,mLayoutId,-1);
        return view_holder;
    }

    protected int getPosition(RecyclerView.ViewHolder viewHolder)
    {
        return viewHolder.getAdapterPosition();
    }

    protected boolean isEnabled(int viewType)
    {
        return true;
    }



    @Override
    public void onBindViewHolder(final View_Holder holder, int position)
    {

        convert(holder,mDatas.get(position));
        if(mOnItemClickListener!=null)
        {
            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    mOnItemClickListener.onClick(holder.getAdapterPosition());
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View v)
                {
                    mOnItemClickListener.onLongClick(holder.getAdapterPosition());
                    return false;
                }
            });

        }
    }

    public abstract void convert(View_Holder holder,T t);

    @Override
    public  int  getItemCount()
    {
        return mDatas.size();
    }

    public interface OnItemClickListener
    {
        void onClick(int position);
        void onLongClick(int position);
    }

    public void Remove(int position)
    {
        mDatas.remove(position);
        notifyItemRemoved(position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener)
    {
        this. mOnItemClickListener=onItemClickListener;
    }

}

