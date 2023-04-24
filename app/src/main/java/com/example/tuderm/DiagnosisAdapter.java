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

public class DiagnosisAdapter extends RecyclerView.Adapter<DiagnosisAdapter.DiagnosisAdapterVH>{

    Context context;
    ArrayList<diagnosisModel> diagnosisModelArrayList;
    SQLiteDatabase sqLiteDatabase;
    private final RecycleViewInterface recycleViewInterface;

    public DiagnosisAdapter(Context context, int row_patient, ArrayList<diagnosisModel> diagnosisModelArrayList, SQLiteDatabase sqLiteDatabase, RecycleViewInterface recycleViewInterface) {
        this.context = context;
        this.diagnosisModelArrayList = diagnosisModelArrayList;
        this.sqLiteDatabase = sqLiteDatabase;
        this.recycleViewInterface = recycleViewInterface;
    }

    @NonNull
    @Override
    public DiagnosisAdapter.DiagnosisAdapterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.row_diagnosis,parent,false);
        return new DiagnosisAdapter.DiagnosisAdapterVH(v,recycleViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull DiagnosisAdapter.DiagnosisAdapterVH holder, int position) {
        //set layout
        final diagnosisModel model = diagnosisModelArrayList.get(position);
        holder.fulldia.setText(model.getdiagnosisbydoctor());
        holder.datetime.setText(model.getdatetime());
        if (model.getDiaimage() != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(model.getDiaimage(), 0, model.getDiaimage().length);
            holder.exampleimage.setImageBitmap(bitmap);
        } else {
            holder.exampleimage.setImageResource(R.drawable.defaultimage);
        }
    }

    @Override
    public int getItemCount() {
        return diagnosisModelArrayList.size();
    }

    public static class DiagnosisAdapterVH extends RecyclerView.ViewHolder {
        TextView fulldia,datetime;
        ImageView exampleimage;
        public DiagnosisAdapterVH(@NonNull View itemView, RecycleViewInterface recycleViewInterface) {
            super(itemView);
            exampleimage = (ImageView) itemView.findViewById(R.id.exampleimage);
            fulldia = (TextView) itemView.findViewById(R.id.diagnosistxt);
            datetime = (TextView) itemView.findViewById(R.id.datetimetxt);

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
