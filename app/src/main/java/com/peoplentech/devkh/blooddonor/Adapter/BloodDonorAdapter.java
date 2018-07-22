package com.peoplentech.devkh.blooddonor.Adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.peoplentech.devkh.blooddonor.DonorDetailsActivity;
import com.peoplentech.devkh.blooddonor.R;
import com.peoplentech.devkh.blooddonor.SearchDonorActivity;
import com.peoplentech.devkh.blooddonor.helper.SharedPrefManager;
import com.peoplentech.devkh.blooddonor.model.User;

import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by User on 7/17/2018.
 */

public class BloodDonorAdapter extends RecyclerView.Adapter<BloodDonorAdapter.ViewHolder> {

    private List<User> users;
    private Context mCtx;

    public BloodDonorAdapter(List<User> users, Context mCtx) {
        this.users = users;
        this.mCtx = mCtx;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_donor, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BloodDonorAdapter.ViewHolder holder, int position) {
        User user = users.get(position);
        holder.textViewName.setText(user.getName());
        holder.imageButtonMessage.setText(user.getArea());
        holder.textbloodGroup.setText(user.getBlood());

        if (user.getGender().equals("Male") || user.getGender().equals("male")) {
            holder.personimg.setImageResource(R.drawable.male);
        } else {
            holder.personimg.setImageResource(R.drawable.femaleb);
        }
    }


    @Override
    public int getItemCount() {
        return users.size();
    }




    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewName;
        public TextView imageButtonMessage;
        public TextView textbloodGroup;
        public CardView myCard;
        public ImageView personimg;

        public ViewHolder(View itemView) {
            super(itemView);

            textViewName = (TextView) itemView.findViewById(R.id.donorName);
            imageButtonMessage = (TextView) itemView.findViewById(R.id.donorArea);
            textbloodGroup = (TextView) itemView.findViewById(R.id.bloodType);
            personimg = (ImageView) itemView.findViewById(R.id.imgPerson);
            myCard = (CardView) itemView.findViewById(R.id.cardviewDonor);
            myCard.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Intent intent = new Intent(mCtx.getApplicationContext(), DonorDetailsActivity.class);
                    intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("donor_name", users.get(position).getName());
                    intent.putExtra("donor_area", users.get(position).getArea());
                    intent.putExtra("donor_email", users.get(position).getEmail());
                    intent.putExtra("donor_phone", users.get(position).getPhone());
                    intent.putExtra("donor_blood", users.get(position).getBlood());
                    intent.putExtra("donor_gender", users.get(position).getGender());

                    //Pair[] pairs = new Pair[1];
                    //pairs[0] = new Pair<View, String> (myCard, "cardTransition");

                    //String tarnsitionName = "cardTransition";
                    mCtx.startActivity(intent);

                }
            });
        }
    }
}