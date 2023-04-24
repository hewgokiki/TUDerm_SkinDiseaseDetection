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

public class patientAdapter extends RecyclerView.Adapter<patientAdapter.PatientAdapterVH> {

    Context context;
    ArrayList<patientModel> patientModelArrayList;
    SQLiteDatabase sqLiteDatabase;
    private final RecycleViewInterface recycleViewInterface;
    public patientAdapter(Context context, int row_patient, ArrayList<patientModel> patientModelArrayList, SQLiteDatabase sqLiteDatabase, RecycleViewInterface recycleViewInterface) {
        this.context = context;
        this.patientModelArrayList = patientModelArrayList;
        this.sqLiteDatabase = sqLiteDatabase;
        this.recycleViewInterface = recycleViewInterface;
    }

    @NonNull
    @Override
    public PatientAdapterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.row_patient,parent,false);
        return new PatientAdapterVH(v,recycleViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientAdapterVH holder, int position) {
        //set layout
        final patientModel model = patientModelArrayList.get(position);
        Bitmap bitmap = BitmapFactory.decodeByteArray(model.getProfileimage(),0,model.getProfileimage().length);
        holder.image.setImageBitmap(bitmap);
        holder.fullname.setText(model.getFullname());
    }

    @Override
    public int getItemCount() {
        return patientModelArrayList.size();
    }

    public static class PatientAdapterVH extends RecyclerView.ViewHolder {
        TextView fullname;
        ImageView image;
        public PatientAdapterVH(@NonNull View itemView, RecycleViewInterface recycleViewInterface) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.profileimage);
            fullname = (TextView) itemView.findViewById(R.id.fullname);

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

    public void setFilteredList(ArrayList<patientModel> filteredList){
        this.patientModelArrayList = filteredList;
        notifyDataSetChanged();
    }


}
