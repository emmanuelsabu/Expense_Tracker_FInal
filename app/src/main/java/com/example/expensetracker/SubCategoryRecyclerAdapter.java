package com.example.expensetracker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

class SubCategoryRecyclerAdapter extends RecyclerView.Adapter<SubCategoryRecyclerAdapter.ViewHolder>
{
    Context context;
    ArrayList<Values_Transaction> value_array;

    public SubCategoryRecyclerAdapter(Subcategory subcategory, ArrayList<Values_Transaction> value_array)
    {
        this.context = subcategory;
        this.value_array = value_array;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subcategoryrecycleradapter,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        holder.t1.setText(value_array.get(position).getCategory());
        holder.t2.setText(""+value_array.get(position).getCost());
        holder.t3.setText(""+value_array.get(position).getDates());
    }


    @Override
    public int getItemCount() {
        return value_array.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView t1,t2,t3;
        ImageView t4,t5;
        AlertDialog.Builder mBuilder;
        AlertDialog.Builder mBuilderUpdate;

        IncomeDB mIncomeDB;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            t1=itemView.findViewById(R.id.t1111);
            t2=itemView.findViewById(R.id.t2222);
            t3=itemView.findViewById(R.id.t3333);
            t4=itemView.findViewById(R.id.t4444);
            t5=itemView.findViewById(R.id.t55);

            mBuilder = new AlertDialog.Builder(context);
            mBuilderUpdate = new AlertDialog.Builder(context);

            mIncomeDB = new IncomeDB(context);

            t4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {

                    final int id = value_array.get(getAdapterPosition()).getId();
                    mBuilder.setMessage("Do you want to Delete this Transaction")
                            .setTitle("Delete")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    String query = "Delete from IncomeDB where ID='"+id+"'";
                                    mIncomeDB.openconnection();
                                    Boolean bool =mIncomeDB.executequery(query);
                                    if (bool)
                                    {
                                        removeItem(mIncomeDB,id,getAdapterPosition());
                                    }
                                    else
                                    {
                                        Toast.makeText(context,"Cannot be Deleted",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(context,"Cancelled",Toast.LENGTH_SHORT).show();
                                }
                            });

                    AlertDialog alertDialog = mBuilder.create();
                    alertDialog.setTitle("Delete");
                    alertDialog.show();
                }
            });


        }

    }
    private void removeItem(IncomeDB mIncomeDB,int id,int position)
    {
        String query = "Delete from IncomeDB where ID='"+id+"'";
        mIncomeDB.openconnection();
        Boolean bool =mIncomeDB.executequery(query);
        if (bool)
        {
            value_array.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, value_array.size());
            Toast.makeText(context,"Deleted",Toast.LENGTH_SHORT).show();
            notifyDataSetChanged();
        }
        else
        {
            Toast.makeText(context,"Cannot be Deleted",Toast.LENGTH_SHORT).show();
        }
    }

}
