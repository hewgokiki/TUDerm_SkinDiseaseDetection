package com.example.tuderm;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class limitimageAdapter extends RecyclerView.Adapter<limitimageAdapter.limitimageAdapterVH>{
    Context context;
    ArrayList<imageModel> imageModelArrayList;
    SQLiteDatabase sqLiteDatabase;
    private final RecycleViewInterface recycleViewInterface;
    int deleteindex;
    public limitimageAdapter(Context context, int row_patient, ArrayList<imageModel> imageModelArrayList, SQLiteDatabase sqLiteDatabase, RecycleViewInterface recycleViewInterface) {
        this.context = context;
        this.imageModelArrayList = imageModelArrayList;
        this.sqLiteDatabase = sqLiteDatabase;
        this.recycleViewInterface = recycleViewInterface;
    }

    @NonNull
    @Override
    public limitimageAdapter.limitimageAdapterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.row_image_withmenu,parent,false);
        return new limitimageAdapter.limitimageAdapterVH(v,recycleViewInterface,this);
    }

    @Override
    public void onBindViewHolder(@NonNull limitimageAdapter.limitimageAdapterVH holder, int position) {
        //set layout
        final imageModel model = imageModelArrayList.get(position);
        Bitmap bitmap = BitmapFactory.decodeByteArray(model.getimage(),0,model.getimage().length);
        holder.eximage.setImageBitmap(bitmap);
        if(model.getText()==null){
            holder.checkbyai.setVisibility(View.GONE);
        }
    } //for limit size recycleview

    @Override
    public int getItemCount() {
        return imageModelArrayList.size();
    } //for limit size recycleview

    public static class limitimageAdapterVH extends RecyclerView.ViewHolder {
        ImageView eximage,checkbyai;
        TextView aitext;
        DBHelper dbh;
        limitimageAdapter adapter;
        ImageButton view,delete;
        public limitimageAdapterVH(@NonNull View itemView, RecycleViewInterface recycleViewInterface,  limitimageAdapter adapter) {
            super(itemView);
            eximage = itemView.findViewById(R.id.examimage);
            checkbyai = itemView.findViewById(R.id.checkbyai);
            view = itemView.findViewById(R.id.viewbtn);
            delete = itemView.findViewById(R.id.deletebtn);
            this.adapter = adapter;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recycleViewInterface != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            //System.out.println("pos = "+position);
                        }
                    }
                }
            });

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recycleViewInterface != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            //System.out.println(position);
                            Intent intent = new Intent(view.getContext(), ViewImageActivity.class);
                            intent.putExtra("position",position);
                            view.getContext().startActivity(intent);
                        }
                    }
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recycleViewInterface != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            //System.out.println(position);
                            adapter.deleteItem(position);
                            Toast.makeText(view.getContext(), "ลบรูปภาพที่เลือกแล้ว", Toast.LENGTH_SHORT).show();
                            //reinsex after delete
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
            });

        }

    }

    private void deleteItem(int position) {
        System.out.println("delete index at "+position);
        deleteindex=position;
        DBHelper dbh = new DBHelper(context);
        dbh.deleteSpecifyTemp(position);
        imageModelArrayList.remove(position);
        System.out.println("now size = "+ imageModelArrayList.size());
        dbh.rearrange();
        notifyItemRemoved(position);
    }


}
