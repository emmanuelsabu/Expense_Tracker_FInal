package com.example.expensetracker;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>
{
    private Context context;
    private ArrayList<Values_Transaction> value_array;

    public RecyclerAdapter(Context context, ArrayList<Values_Transaction> value_array)
    {
        this.context = context;
        this.value_array = value_array;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleradapter,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        holder.t1.setText(value_array.get(position).getType());
        holder.t2.setText(value_array.get(position).getCategory());
        holder.t3.setText(""+value_array.get(position).getCost());
        holder.t4.setText(value_array.get(position).getDates());

        if(holder.t1.getText().equals("Income"))
        {
            holder.t1.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        }
        else
        {
            holder.t1.setTextColor(context.getResources().getColor(R.color.red));
        }
    }

    @Override
    public int getItemCount() {
        return value_array.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        TextView t1,t2,t3,t4;
        ImageView t5,t6;
        AlertDialog.Builder mBuilder;
        AlertDialog.Builder mBuilderUpdate;

        IncomeDB mIncomeDB;
        final Calendar myCalendar = Calendar.getInstance();
        int day2,month2,year2;
        String dates;
        EditText new_cost,new_date;

        public ViewHolder(View view)
        {
            super(view);
            t1=view.findViewById(R.id.t1);
            t2=view.findViewById(R.id.t2);
            t3=view.findViewById(R.id.t3);
            t4=view.findViewById(R.id.t4);
            t5=view.findViewById(R.id.t5);
            t6=view.findViewById(R.id.t6);
            mIncomeDB = new IncomeDB(context);

            mBuilder = new AlertDialog.Builder(context);
            mBuilderUpdate = new AlertDialog.Builder(context);

            t5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int id = value_array.get(getAdapterPosition()).getId();

                    mBuilder.setMessage("Do you want to Delete this Transaction")
                            .setTitle("Delete")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    removeItem(mIncomeDB,id,getAdapterPosition());
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
            t6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    View view= LayoutInflater.from(context).inflate(R.layout.mbuilderupdate,null);
                    mBuilderUpdate.setView(view);
                    new_cost = view.findViewById(R.id.amount1);
                    new_date = view.findViewById(R.id.datepicker);
                    TextView new_category = view.findViewById(R.id.categories);
                    TextView new_type = view.findViewById(R.id.type);

                    final int id = value_array.get(getAdapterPosition()).getId();
                    String date_new = value_array.get(getAdapterPosition()).getDates();
                    new_date.setText(date_new);
                    final String cost_new = String.valueOf(value_array.get(getAdapterPosition()).getCost());
                    new_cost.setText( cost_new);
                    new_category.setText(value_array.get(getAdapterPosition()).getCategory());
                    new_type.setText(value_array.get(getAdapterPosition()).getType());

                    day2 = value_array.get(getAdapterPosition()).getDay();
                    month2 = value_array.get(getAdapterPosition()).getMonth();
                    year2 = value_array.get(getAdapterPosition()).getYear();



                    final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear,
                                              int dayOfMonth) {
                            // TODO Auto-generated method stub

                            myCalendar.set(Calendar.YEAR, year);
                            myCalendar.set(Calendar.MONTH, monthOfYear);
                            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                            day2 =dayOfMonth;
                            month2 = monthOfYear;
                            year2=year;

                            updateLabel();



                        }
                    };
                    new_date.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            new DatePickerDialog(context, date, myCalendar
                                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                        }
                    });

                    mBuilderUpdate.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            String date = String.valueOf(new_date.getText());
                            int cost = Integer.parseInt(String.valueOf(new_cost.getText())) ;

                            String query = "update IncomeDB set cost ='"+cost+"' , date= '"+date+"' ,day ='"+day2+"', month ='"+month2+"',year ='"+year2+"' where ID ='"+id+"'";
                            mIncomeDB.openconnection();
                            Boolean bool = mIncomeDB.executequery(query);
                            if(bool)
                            {
                                Intent intent = new Intent(context,context.getClass());
                                intent.putExtra("Fragment",1);
                                context.startActivity(intent);

                                notifyItemChanged(getAdapterPosition());
                                Toast.makeText(context,"Updated",Toast.LENGTH_SHORT).show();


                            }
                            else
                            {
                                Toast.makeText(context,"Not Updated",Toast.LENGTH_SHORT).show();
                            }

                        }
                    })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(context,"Cancelled",Toast.LENGTH_SHORT).show();
                                }
                            });

                    AlertDialog alertDialog = mBuilderUpdate.create();
                    alertDialog.setTitle("Edit");
                    alertDialog.show();
                }
            });

        }
        public void updateLabel()
        {
            String myFormat = "dd/MMM/yy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            new_date.setText(sdf.format(myCalendar.getTime()));
            dates = String.valueOf(new_date.getText());

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
