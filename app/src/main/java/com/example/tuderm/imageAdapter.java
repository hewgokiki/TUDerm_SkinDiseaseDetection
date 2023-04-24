package com.example.tuderm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class imageAdapter extends RecyclerView.Adapter<imageAdapter.imageAdapterVH>{
    Context context;
    ArrayList<imageModel> imageModelArrayList;
    SQLiteDatabase sqLiteDatabase;
    private final RecycleViewInterface recycleViewInterface;


    public imageAdapter(Context context, int row_patient, ArrayList<imageModel> imageModelArrayList, SQLiteDatabase sqLiteDatabase, RecycleViewInterface recycleViewInterface) {
        this.context = context;
        this.imageModelArrayList = imageModelArrayList;
        this.sqLiteDatabase = sqLiteDatabase;
        this.recycleViewInterface = recycleViewInterface;
    }

    @NonNull
    @Override
    public imageAdapter.imageAdapterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.row_image,parent,false);
        return new imageAdapter.imageAdapterVH(v,recycleViewInterface);
    }

    /*@Override
    public void onBindViewHolder(@NonNull imageAdapter.imageAdapterVH holder, int position) {
        //set layout
        final imagemodel model = imagemodelArrayList.get(position);
        Bitmap bitmap = BitmapFactory.decodeByteArray(model.getimage(),0,model.getimage().length);
        holder.eximage.setImageBitmap(bitmap);
    } //for limit size recycleview

    @Override
    public int getItemCount() {
        return imagemodelArrayList.size();
    }*/ //for limit size recycleview

    @Override
    public void onBindViewHolder(@NonNull imageAdapter.imageAdapterVH holder, int position) {
        //set layout
        final imageModel model = imageModelArrayList.get(position % imageModelArrayList.size());
        Bitmap bitmap = BitmapFactory.decodeByteArray(model.getimage(),0,model.getimage().length);
        holder.eximage.setImageBitmap(bitmap);
        holder.aitext.setText(model.getText());
    }
    @Override
    public int getItemCount() {
        return imageModelArrayList.size();
        //return Integer.MAX_VALUE; //for unlimit size recycleview
    }

    public static class imageAdapterVH extends RecyclerView.ViewHolder {
        ImageView eximage;
        TextView aitext;
        public imageAdapterVH(@NonNull View itemView, RecycleViewInterface recycleViewInterface) {
            super(itemView);
            eximage = (ImageView) itemView.findViewById(R.id.examimage);
            aitext = itemView.findViewById(R.id.aitext);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(recycleViewInterface != null){
                        int postion = getAdapterPosition();
                        if(postion != RecyclerView.NO_POSITION){
                            recycleViewInterface.onItemClick(postion);
                        }
                    }
                }
            });

        }
    }
}
