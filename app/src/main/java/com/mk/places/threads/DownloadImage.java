package com.mk.places.threads;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import com.mk.places.R;
import com.mk.places.utilities.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadImage extends AsyncTask<String, Integer, String> {

    private Activity context;
    private String location;
    private String TAG = DownloadImage.class.getName();
    private int color;
    private File file;

    public DownloadImage(Activity context, String location, int color) {
        this.context = context;
        this.location = location;
        this.color = color;
    }

    @Override
    protected String doInBackground(String... sUrl) {

        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;

        try {

            URL url = new URL(sUrl[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            // expect HTTP 200
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
                return "HTTP Issue: Server returned HTTP " + connection.getResponseCode() + " " + connection.getResponseMessage();

            input = connection.getInputStream();
            File myDir = new File(Environment.getExternalStorageDirectory().toString(), context.getResources().getString(R.string.app_name));

            if (!myDir.exists()) {
                myDir.mkdir();
                Log.d(TAG, "Places folder was created: ");

            } else
                Log.d(TAG, "Folder already exist. Image will be downloaded in this folder.");

            file = new File(myDir, location + ".jpeg");
            output = new FileOutputStream(file);

            byte data[] = new byte[4096];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                total += count;
                output.write(data, 0, count);
            }
        } catch (Exception e) {
            return e.toString();
        } finally {
            try {
                if (output != null)
                    output.close();
                if (input != null)
                    input.close();

            } catch (IOException ignored) {
            }

            if (connection != null) {
                connection.disconnect();
                Log.d(TAG, "Image was successfully downloaded.");
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {

        View layout = context.findViewById(R.id.coordinatorLayout);
        Snackbar snackbar = Snackbar.make(layout, R.string.downloadImageText, Snackbar.LENGTH_LONG)
                .setActionTextColor(ContextCompat.getColor(context, R.color.white))
                .setAction(R.string.downloadImageAction, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.fromFile(file), "image/*");
                        context.startActivity(intent);

                    }
                });

        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(Utils.colorVariant(color, 1.06f));
        snackbar.show();

        super.onPostExecute(s);
    }

}

