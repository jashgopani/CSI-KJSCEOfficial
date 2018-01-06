package org.csikjsce.csi_kjsceofficial;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by sumit on 5/9/17.
 */

public class EurekaFragment extends android.support.v4.app.Fragment implements View.OnClickListener {
    private View view;
    TextView eureka_para_tv;
    String shareAppName, eurekaDescp="desc", registerUrl="url";
    ImageView fbIcon, instaIcon, whatsappIcon, shareIcon, posterImage;
    FloatingActionButton fab;
    Query query;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_eureka, container, false);
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.title_eureka));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        query = FirebaseDatabase.getInstance().getReference("eureka");

        posterImage = view.findViewById(R.id.eureka_poster_iv);
        eureka_para_tv = view.findViewById(R.id.eureka_textview);
        fbIcon = view.findViewById(R.id.facebook_share);
        instaIcon = view.findViewById(R.id.instagram_share);
        whatsappIcon = view.findViewById(R.id.whatsapp_share);
        shareIcon = view.findViewById(R.id.general_share);
        fab = view.findViewById(R.id.eureka_fab);

        fbIcon.setOnClickListener(this);
        instaIcon.setOnClickListener(this);
        whatsappIcon.setOnClickListener(this);
        shareIcon.setOnClickListener(this);
        fab.setOnClickListener(this);
        Glide
                .with(getContext())
                .load(getContext().getResources().getDrawable(R.drawable.eureka_poster))
                .into(posterImage);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                eurekaDescp = dataSnapshot.child("description").getValue(String.class);
                eureka_para_tv.setText(Html.fromHtml(eurekaDescp));
                registerUrl = dataSnapshot.child("registeration").getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        String msg = eurekaDescp+"<br>Register: "+registerUrl;
        switch (v.getId()) {

            case R.id.facebook_share:
                shareAppName = "com.facebook.katana";
                Utils.onShareClick(getContext(), msg, shareAppName);
                break;
            case R.id.instagram_share:
                shareAppName = "com.instagram.android";
                Utils.onShareClick(getContext(), msg, shareAppName);
                break;
            case R.id.whatsapp_share:
                shareAppName = "com.whatsapp";
                Utils.onShareClick(getContext(), msg, shareAppName);
                break;
            case R.id.general_share:
                intent = new Intent();
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, msg);
                startActivity(intent);
                break;
            case R.id.eureka_fab:
                intent = new Intent(getContext(), WebViewActivity.class);
                intent.putExtra("link",registerUrl);
                startActivity(intent);
        }

    }
}
