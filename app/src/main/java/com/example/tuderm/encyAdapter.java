package com.example.tuderm;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class encyAdapter extends RecyclerView.Adapter<encyAdapter.encyAdapterVH>{
    Context context;
    ArrayList<EncyModel> EncyModelArrayList;
    SQLiteDatabase sqLiteDatabase;
    private final RecycleViewInterface recycleViewInterface;


    public encyAdapter(Context context, int row_encyclop, ArrayList<EncyModel> EncyModelArrayList, SQLiteDatabase sqLiteDatabase, RecycleViewInterface recycleViewInterface) {
        this.context = context;
        this.EncyModelArrayList = EncyModelArrayList;
        this.sqLiteDatabase = sqLiteDatabase;
        this.recycleViewInterface = recycleViewInterface;
    }

    @NonNull
    @Override
    public encyAdapter.encyAdapterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.row_encyclop,parent,false);
        return new encyAdapter.encyAdapterVH(v,recycleViewInterface,this);
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
    public void onBindViewHolder(@NonNull encyAdapterVH holder, int position) {
        final EncyModel model = EncyModelArrayList.get(position % EncyModelArrayList.size());
        Bitmap bitmap = BitmapFactory.decodeByteArray(model.getimage(),0,model.getimage().length);
        holder.diseaseimage.setImageBitmap(bitmap);
        holder.diseasename.setText(model.getText());
    }

    @Override
    public int getItemCount() {
        return EncyModelArrayList.size();
        //return Integer.MAX_VALUE; //for unlimit size recycleview
    }

    public static class encyAdapterVH extends RecyclerView.ViewHolder {
        ImageView diseaseimage;
        TextView diseasename;
        encyAdapter adapter;
        ConstraintLayout constraintLayout;
        public encyAdapterVH(@NonNull View itemView, RecycleViewInterface recycleViewInterface,encyAdapter adapter) {
            super(itemView);
            diseaseimage = (ImageView) itemView.findViewById(R.id.diseaseimage);
            diseasename = itemView.findViewById(R.id.diseasename);
            constraintLayout = (ConstraintLayout) itemView.findViewById(R.id.fullbox);
            this.adapter = adapter;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(recycleViewInterface != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            recycleViewInterface.onItemClick(position);
                            //System.out.println("pos = "+position);
                        }
                    }
                }
            });
            constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recycleViewInterface != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            //System.out.println(position);
                            Intent intent = new Intent(view.getContext(), FullDiseaseActivity.class);
                            position++;
                            intent.putExtra("position",position);
                            view.getContext().startActivity(intent);
                        }
                    }
                }
            });
        }
    }
}
