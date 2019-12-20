package com.example.expensetracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class CategoryMonthRA extends RecyclerView.Adapter<CategoryMonthRA.ViewHolder>
{
    Context context;
    ArrayList<Values_Transaction> value_array;



    public CategoryMonthRA(FragmentActivity activity, ArrayList<Values_Transaction> value_array)
    {
        this.context = activity;
        this.value_array = value_array;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.categorymonthra,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        holder.t1.setText(value_array.get(position).getCategory());
        holder.t2.setText(""+value_array.get(position).getCost());
    }

    @Override
    public int getItemCount() {
        return value_array.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView t1,t2;
        int month,year;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            t1=itemView.findViewById(R.id.t1);
            t2=itemView.findViewById(R.id.t2);
            month=value_array.get(0).getMonth();
            year=value_array.get(0).getYear();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    Intent intent = new Intent(context,Subcategory.class);
                    intent.putExtra("category",t1.getText());
                    intent.putExtra("month",month);
                    intent.putExtra("year",year);
                    context.startActivity(intent);
                    ((Activity)context).finish();

                }
            });
        }
    }
}
