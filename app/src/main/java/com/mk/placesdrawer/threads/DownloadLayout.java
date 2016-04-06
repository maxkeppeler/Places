package com.mk.placesdrawer.threads;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.ViewGroup;

import com.mk.placesdrawer.R;
import com.mk.placesdrawer.utilities.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import butterknife.Bind;

/**
 * Created by max on 06.04.16.
 */
public class DownloadLayout extends AsyncTask<String, Integer, String> {

    private Context context;
    private String location;

    public DownloadLayout(Context context, String location) {
        this.context = context;
        this.location = location;
    }

    @Override
    protected String doInBackground(String... sUrl) {
        InputStream input = null;
        OutputStream output = null;



//        Canvas and Paint
//        1. Find viewgroup
//        2. Combine canvas object with layout


//          TODO grab viewgroup of detail view, create a new bitmap and download it with a special name

            String imageName = location + " .jpg";
            File path = new File(Environment.getExternalStorageDirectory().toString());
            File myDir = new File(path, context.getResources().getString(R.string.app_name));

            if (!myDir.exists()) myDir.mkdir();
            File file = new File(myDir, imageName);

//            output = new FileOutputStream(file);
        return null;
    }

}

