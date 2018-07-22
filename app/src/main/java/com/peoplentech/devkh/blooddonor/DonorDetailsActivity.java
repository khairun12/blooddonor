package com.peoplentech.devkh.blooddonor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class DonorDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_details);

        final ViewGroup transition = (ViewGroup) findViewById(R.id.layoutDonor);

        ImageView profileImage = (ImageView) transition.findViewById(R.id.imgDetailsPerson);
        TextView donor_name = (TextView) transition.findViewById(R.id.textViewName);
        Animation slideAnimationLeft = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_left);
        profileImage.startAnimation(slideAnimationLeft);

        //added
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            String donorName = (String) bundle.get("donor_name");
            String donorArea = (String) bundle.get("donor_area");
            String donorEmail = (String) bundle.get("donor_email");
            String donorPhone = (String) bundle.get("donor_phone");
            String donorBlood = (String) bundle.get("donor_blood");
            String donorGender = (String) bundle.get("donor_gender");

            assert donorGender != null;
            if (donorGender.equals("Male") || donorGender.equals("male")) {
                profileImage.setImageResource(R.drawable.male);
            }
            else{
                profileImage.setImageResource(R.drawable.femaleb);
            }

            donor_name.setText(donorName);
        }
    }
}