package com.mk.places.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.balysv.materialripple.MaterialRippleLayout;
import com.mk.places.R;

import io.smooch.core.Smooch;
import io.smooch.ui.ConversationActivity;

public class DrawerSupport extends Fragment {

    final private String TOKEN = "4k2knjsi7zbut95wk28dgqtvu";
    final private String TAG = "DrawerSupport";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.drawer_support, container, false);

        MaterialRippleLayout liveChat = (MaterialRippleLayout) view.findViewById(R.id.rippleLiveChat);
        MaterialRippleLayout mail = (MaterialRippleLayout) view.findViewById(R.id.rippleMail);

        liveChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "OnClick on Live Chat.");

//                TODO - dark theme for library activity?
                ConversationActivity.show(getContext());

            }
        });

        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "OnClick on E-Mail.");

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, "chvent94@gmail.com");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Places - Support Mail");
                startActivity(Intent.createChooser(intent, "Send Email with"));
            }
        });




        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Smooch.init(getActivity().getApplication(), TOKEN);




    }

}