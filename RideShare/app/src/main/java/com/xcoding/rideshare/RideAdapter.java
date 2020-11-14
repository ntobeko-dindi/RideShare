package com.xcoding.rideshare;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RideAdapter extends RecyclerView.Adapter<RideAdapter.ViewHolder>  {
    Rides[] rides;
    HomeFragment context;

    public RideAdapter(Rides[] myMovieData,HomeFragment activity) {
        this.rides = myMovieData;
        this.context = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.rides_list,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Rides myMovieDataList = rides[position];
        holder.description.setText(myMovieDataList.getDescription());
        holder.name.setText(myMovieDataList.getUsername());
        holder.date.setText(myMovieDataList.getDate());
        holder.image.setImageResource(myMovieDataList.getImage());

        holder.description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });
    }

    @Override
    public int getItemCount() {
        return rides.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView description;
        TextView date;
        TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            date = itemView.findViewById(R.id.date);

        }
    }
}
