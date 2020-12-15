package com.xcoding.rideshare.adapters;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.xcoding.rideshare.HomeScreenActivity;
import com.xcoding.rideshare.LoginActivity;
import com.xcoding.rideshare.R;
import com.xcoding.rideshare.fragments.HomeFragment;
import com.xcoding.rideshare.modals.LongDistanceCommuteModal;

import java.util.List;

public class LongDistanceAdapter extends RecyclerView.Adapter {

    List<LongDistanceCommuteModal> longDistanceCommuteModalList;
    Context context;
    private OnRideListener mOnRideListener;

    public LongDistanceAdapter(List<LongDistanceCommuteModal> longDistanceCommuteModalList,Context context, OnRideListener mOnRideListener) {
        this.longDistanceCommuteModalList = longDistanceCommuteModalList;
        this.context = context;
        this.mOnRideListener = mOnRideListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rides_list,parent,false);
        ViewHolderClass viewHolderClass = new ViewHolderClass(view,mOnRideListener);
        return viewHolderClass;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ViewHolderClass viewHolderClass = (ViewHolderClass)holder;
        LongDistanceCommuteModal modal = longDistanceCommuteModalList.get(position);
        String time = modal.getTime().substring(9);
        viewHolderClass.date.setText(time);

        String passengers = modal.getSits();
        String loc = modal.getBeginning();
        String dest = modal.getDestination();
        String day = modal.getDate();
        String pric = modal.getPrice();
        String date = modal.getTime().substring(0,9);

        String completeDetails = "Looking for " + passengers + " passengers from around " +
                loc + " to " + dest + " on " + day + "\n" +
                " Price per passenger is R" + pric +"\n\nPosted on : " + date;

        if(day == null){
            completeDetails = "Looking for " + passengers + " passengers from around " +
                    loc + " to " + dest + "\n" +
                    " Price per passenger is R" + pric +"\n\nPosted on : " + date;
        }

        viewHolderClass.offer.setText("BOOK A SIT");
        viewHolderClass.description.setText(completeDetails);
        viewHolderClass.name.setText(modal.getRideSourceName());
    }

    @Override
    public int getItemCount() {
        return longDistanceCommuteModalList.size();
    }
    public class ViewHolderClass extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView image;
        TextView description, date, name, offer, like, share;

        OnRideListener onRideListener;


        public ViewHolderClass(@NonNull View itemView, OnRideListener onRideListener){
            super(itemView);
            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            date = itemView.findViewById(R.id.date);
            offer = itemView.findViewById(R.id.txt_offer);
            this.onRideListener = onRideListener;

            offer.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onRideListener.onRideClick(getAdapterPosition());
        }
    }
    public interface OnRideListener{
        void onRideClick(int position);
    }
}
