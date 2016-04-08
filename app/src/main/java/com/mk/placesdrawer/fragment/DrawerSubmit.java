package com.mk.placesdrawer.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mk.placesdrawer.R;
import com.mk.placesdrawer.utilities.Utils;

import butterknife.Bind;

public class DrawerSubmit extends Fragment {

//    TODO - finish submit fragment and function to send it via email without intent

    private Context context;
    //    INPUT LAYOUTS
    @Bind(R.id.inputLayoutPlace) TextInputLayout layoutPlace;
    @Bind(R.id.inputLayoutDesc) TextInputLayout layoutDesc;
    @Bind(R.id.inputLayoutAdditionalInfo) TextInputLayout layoutInfo;
    @Bind(R.id.inputLayoutMail) TextInputLayout layoutMail;

    //    EDITTEXTS
    @Bind(R.id.inputPlace) EditText inputPlace;
    @Bind(R.id.inputDesc) EditText inputDesc;
    @Bind(R.id.inputAdditionalInfo) EditText inputInfo;
    @Bind(R.id.inputMail) EditText inputMail;

    @Bind(R.id.btnSend) Button button;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.drawer_submit, container, false);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        context = getActivity();



    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.toolbar_places_details, menu);
    }

}
