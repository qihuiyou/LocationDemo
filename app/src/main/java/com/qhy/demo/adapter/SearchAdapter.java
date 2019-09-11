package com.qhy.demo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.help.Tip;
import com.qhy.demo.R;
import com.qhy.demo.inter.OnItemClickListener;
import com.qhy.demo.utils.CommonUtils;

import java.util.ArrayList;

/**
 * Created by qhy on 2019/9/9.
 */
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {


    private Context mContext;
    private ArrayList<Tip> mList;
    private OnItemClickListener mItemClickListener;


    public void setItemClickListener(OnItemClickListener onItemClickListener) {
        this.mItemClickListener = onItemClickListener;
    }


    public SearchAdapter(Context context, ArrayList<Tip> searchContentList) {
        this.mContext = context;
        this.mList = searchContentList;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new SearchViewHolder(LayoutInflater.from(mContext).inflate(R.layout.itme_search_contnet, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder viewHolder, int position) {

        Tip tip = mList.get(position);


        viewHolder.mAddress.setText(tip.getName());
        viewHolder.mStreet.setText(tip.getAddress());
        LatLonPoint point = tip.getPoint();
        if(point != null){
            String distance = CommonUtils.getAmapDistance(point.getLatitude(), point.getLongitude());
            viewHolder.mDistance.setText(distance);
        }

        if (mList.size() != 1 && mList.size() != (position + 1)) {
            viewHolder.mLine.setVisibility(View.VISIBLE);
        } else {
            viewHolder.mLine.setVisibility(View.GONE);
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.onItemClick(v, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    class SearchViewHolder extends RecyclerView.ViewHolder {

        View mLine;
        TextView mAddress, mStreet, mDistance;

        SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            mLine = itemView.findViewById(R.id.item_view_line);
            mAddress = itemView.findViewById(R.id.item_tv_address);
            mStreet = itemView.findViewById(R.id.item_tv_street);
            mDistance = itemView.findViewById(R.id.item_tv_distance);
        }
    }

}
