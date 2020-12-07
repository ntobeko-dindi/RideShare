package com.xcoding.rideshare.adapters;

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

import com.xcoding.rideshare.R;
import com.xcoding.rideshare.modals.LongDistanceCommuteModal;
import com.xcoding.rideshare.modals.RequestRideModel;

import java.util.List;

public class RequestsAdapter extends RecyclerView.Adapter {

    List<RequestRideModel> requestRideModel;
    Context context;

    public RequestsAdapter(List<RequestRideModel> requestRideModel,Context context) {
        this.requestRideModel = requestRideModel;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rides_list, parent, false);
        ViewHolderClass viewHolderClass = new ViewHolderClass(view);
        return viewHolderClass;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ViewHolderClass viewHolderClass = (ViewHolderClass) holder;
        RequestRideModel modal = requestRideModel.get(position);
        String time = modal.getTime().substring(9);
        viewHolderClass.date.setText(time);

        String loc = modal.getBeginning();
        String dest = modal.getEnd();
        String day = modal.getDate();

        String completeDetails = "Looking for a ride from around " + loc + " to " + dest + " on " + day + "\n";

        viewHolderClass.description.setText(completeDetails);
        String date = modal.getTime().substring(0, 9);
        viewHolderClass.share.setText(date);
        viewHolderClass.name.setText(modal.getRideSourceName());

        viewHolderClass.offer.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setCancelable(true);
            builder.setTitle("CONFIRMATION PROMPT");
            builder.setMessage("Are you sure you want to offer a ride?");
            builder.setPositiveButton("Yes!",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(context,"Ride Offered!",Toast.LENGTH_LONG).show();
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
        return requestRideModel.size();
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
        }
    }
}

