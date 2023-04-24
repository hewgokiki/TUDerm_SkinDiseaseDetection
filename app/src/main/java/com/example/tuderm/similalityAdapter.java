package com.example.tuderm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class similalityAdapter extends RecyclerView.Adapter<similalityAdapter.similalityAdapterVH> {
    int deleteindex;
    ArrayList<similalityModel> similalityModelArrayList;
    Context context;
    SQLiteDatabase sqLiteDatabase;
    private final RecycleViewInterface recycleViewInterface;
    public similalityAdapter(Context context, int row_similality, ArrayList<similalityModel> similalityModelArrayList, SQLiteDatabase sqLiteDatabase, RecycleViewInterface recycleViewInterface) {
        this.context = context;
        this.similalityModelArrayList = similalityModelArrayList;
        this.sqLiteDatabase = sqLiteDatabase;
        this.recycleViewInterface = recycleViewInterface;
    }

    @NonNull
    @Override
    public similalityAdapterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.row_similality,parent,false);
        return new similalityAdapterVH(v,recycleViewInterface,this);
    }

    @Override
    public void onBindViewHolder(@NonNull similalityAdapterVH holder, int position) {
        //set layout
        final similalityModel model = similalityModelArrayList.get(position);
        holder.text.setText(model.gettext());
        holder.disease.setText(model.getdisease());
    }

    @Override
    public int getItemCount() {
        return similalityModelArrayList.size();
    }

    public static class similalityAdapterVH extends RecyclerView.ViewHolder {
        private TextView text;
        private TextView disease;
        ImageButton delete;
        similalityAdapter adapter;
        public similalityAdapterVH(@NonNull View itemView, RecycleViewInterface recycleViewInterface, similalityAdapter adapter) {
            super(itemView);

            text = itemView.findViewById(R.id.text);
            disease = itemView.findViewById(R.id.disease);
            delete = itemView.findViewById(R.id.deletebtn);
            this.adapter = adapter;
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
        dbh.deleteSimilality(position);
        similalityModelArrayList.remove(position);
        System.out.println("now size = "+ similalityModelArrayList.size());
        dbh.rearrange_similality();
        notifyItemRemoved(position);
    }


}
