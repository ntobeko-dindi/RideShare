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

    public LongDistanceAdapter(List<LongDistanceCommuteModal> longDistanceCommuteModalList,Context context) {
        this.longDistanceCommuteModalList = longDistanceCommuteModalList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rides_list,parent,false);
        ViewHolderClass viewHolderClass = new ViewHolderClass(view);
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

        String completeDetails = "Looking for " + passengers + " passengers from around " + loc + " to " + dest + " on " + day + "\n" +
                " Price per passenger is R" + pric;

        viewHolderClass.offer.setText("BOOK A SIT");
        viewHolderClass.description.setText(completeDetails);
        String date = modal.getTime().substring(0,9);
        viewHolderClass.share.setText(date);
        viewHolderClass.name.setText(modal.getRideSourceName());

        viewHolderClass.offer.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setCancelable(true);
            builder.setTitle("CONFIRMATION PROMPT");
            builder.setMessage("Are you sure you want to book a sit in this ride?");
            builder.setPositiveButton("Yes!",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(context,"Sit secured!",Toast.LENGTH_LONG).show();

                            String message = "you have successfully booked a ride with RideShare";
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                                NotificationChannel channel =
                                        new NotificationChannel("n","n",NotificationManager.IMPORTANCE_DEFAULT);

                                NotificationManager manager = context.getSystemService(NotificationManager.class);
                                manager.createNotificationChannel(channel);
                            }

                            NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"n")
                                    .setContentText("RideShare")
                                    .setSmallIcon(R.drawable.ic_notifications)
                                    .setAutoCancel(true)
                                    .setColorized(true)
                                    .setAllowSystemGeneratedContextualActions(true)
                                    .setContentText(message);

                            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
                            managerCompat.notify(999,builder.build());
                        }
                    });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(context,"Operation cancelled!!",Toast.LENGTH_LONG).show();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }

    @Override
    public int getItemCount() {
        return longDistanceCommuteModalList.size();
    }
    public class ViewHolderClass extends RecyclerView.ViewHolder {

        ImageView image;
        TextView description, date, name, offer, like, share;

        public ViewHolderClass(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            date = itemView.findViewById(R.id.date);
            offer = itemView.findViewById(R.id.txt_offer);
            like = itemView.findViewById(R.id.txt_like);
            share = itemView.findViewById(R.id.txt_share);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int vv = v.getId();
                    Toast.makeText(context,String.valueOf(vv),Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
