package com.example.justfit_minsun;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;


import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoHolder> {

    ArrayList<Video> items = null;
    Context mContext;
    int position;
    SparseBooleanArray mSelectedItems = new SparseBooleanArray(0); //position별 선택 상태를 저장하는 구조

    public interface OnItemClickListener{
        void onItemClick(View v , int position);
    }

    OnItemClickListener mListener= null;

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }

    public class VideoHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView title,teacher,part;
        public VideoHolder(View itemView){
            super(itemView);

            image = itemView.findViewById(R.id.item_image);
            title = itemView.findViewById(R.id.item_video_title);
            part = itemView.findViewById(R.id.item_video_part);
            teacher = itemView.findViewById(R.id.item_video_teacher);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                     position = getAdapterPosition();
                    if(mSelectedItems.get(position,false)){
                        mSelectedItems.put(position,false);
                        view.setBackgroundColor(Color.WHITE);
                    }else{
                        mSelectedItems.put(position,true);
                        view.setBackgroundColor(Color.YELLOW);
                    }
                    if( position != RecyclerView.NO_POSITION){
                        if(mListener!=null){
                            mListener.onItemClick(view,position);
                        }
                    }
                    else{
                        Log.e("RecyclerView Log","RecyclerView NoPosition!");
                    }
                }
            });

        }
    }

    public VideoAdapter(Context mContext, ArrayList<Video>items){
        this.items = items;
        this.mContext = mContext;
    }
    @NonNull
    @Override
    public VideoHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new VideoHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VideoHolder viewHolder, int position) {
        Video item =items.get(position);

        viewHolder.image.setBackground(item.getImage());
        viewHolder.title.setText(item.getTitle());
        viewHolder.teacher.setText(item.getTeacherName());
        viewHolder.part.setText(item.getPart());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
