package com.example.expensetracker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class Delete_Category_RecylerAdapter extends RecyclerView.Adapter<Delete_Category_RecylerAdapter.ViewHolder>
{
    private Context delete_category;
    private ArrayList<Category_List> category_list;

    public Delete_Category_RecylerAdapter(Delete_Category delete_category, ArrayList<Category_List> category_list)
    {
        this.delete_category = delete_category;
        this.category_list = category_list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.delete_category_recycleradapter,parent,false);
        return new Delete_Category_RecylerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        holder.t1.setText(category_list.get(position).getCategory());

    }


    @Override
    public int getItemCount() {
        return category_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView t1;
        CardView c1;
        AlertDialog.Builder mBuilder;

        public ViewHolder(View view) {
            super(view);
            mBuilder = new AlertDialog.Builder(delete_category);
            t1 = view.findViewById(R.id.t1);
            c1 = view.findViewById(R.id.c1);

            c1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    String type = category_list.get(0).getType();
                    final int id =category_list.get(getAdapterPosition()).getId();

                    if (type == "Income")
                    {
                        final CategoryDB categoryDBS;
                        categoryDBS = new CategoryDB(delete_category);

                        mBuilder.setMessage("Do you want to Delete this Transaction")
                                .setTitle("Delete")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        removeItem(categoryDBS,id,getAdapterPosition());
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(delete_category,"Cancelled",Toast.LENGTH_SHORT).show();
                                    }
                                });

                        AlertDialog alertDialog = mBuilder.create();
                        alertDialog.setTitle("Delete");
                        alertDialog.show();
                    }

                    else if (type== "Expense")
                    {
                        final CategoryDBExpense categoryDBS;
                        categoryDBS = new CategoryDBExpense(delete_category);
                        Log.d("ssss", "removeItem1: "+id);


                        mBuilder.setMessage("Do you want to Delete this Transaction")
                                .setTitle("Delete")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        removeItem1(categoryDBS,id,getAdapterPosition());
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(delete_category,"Cancelled",Toast.LENGTH_SHORT).show();
                                    }
                                });

                        AlertDialog alertDialog = mBuilder.create();
                        alertDialog.setTitle("Delete");
                        alertDialog.show();

                    }
                }
            });
        }
    }
    private void removeItem(CategoryDB categoryDBS,int id,int position)
    {
        String query = "Delete from CategoryDB where ID='"+id+"'";
        categoryDBS.openconnection();
        Boolean bool =categoryDBS.executequery(query);
        if (bool)
        {
            category_list.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, category_list.size());
            Toast.makeText(delete_category,"Deleted",Toast.LENGTH_SHORT).show();
            notifyDataSetChanged();
        }
        else
        {
            Toast.makeText(delete_category,"Cannot be Deleted",Toast.LENGTH_SHORT).show();
        }
    }

    private void removeItem1(CategoryDBExpense categoryDBS,int id,int position)
    {
        String query = "Delete from CategoryDBExpense where ID='"+id+"'";
        categoryDBS.openconnection();
        Boolean bool =categoryDBS.executequery(query);
        if (bool)
        {
            category_list.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, category_list.size());
            Toast.makeText(delete_category,"Deleted",Toast.LENGTH_SHORT).show();
            notifyDataSetChanged();
        }
        else
        {
            Toast.makeText(delete_category,"Cannot be Deleted",Toast.LENGTH_SHORT).show();
        }
    }


}
