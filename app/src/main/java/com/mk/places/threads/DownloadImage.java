package com.mk.places.threads;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.design.widget.Snackbar;
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

    private File openPath;
    private Activity context;
    private String location;
    private String TAG = "ImageFromUrl/ Async Task";
    private int color;

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

            // expect HTTP 200 OK, so we don't mistakenly save error report instead of the file
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return "HTTP Issue: Server returned HTTP " + connection.getResponseCode() + " " + connection.getResponseMessage();
            }


            input = connection.getInputStream();

            String imageName = location + ".jpeg";
            File path = new File(Environment.getExternalStorageDirectory().toString());
            File myDir = new File(path, context.getResources().getString(R.string.app_name));

            if (!myDir.exists()) {
                myDir.mkdir();
                Log.d(TAG, "New Places folder created: ");

            } else
                Log.d(TAG, "Places folder does exist. Image will be downloaded in this folder. ");


            openPath = myDir;

            File file = new File(myDir, imageName);

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
                Log.d(TAG, "Image was successfully downloaded.");

                if (input != null)
                    input.close();

            } catch (IOException ignored) {
            }

            if (connection != null)
                connection.disconnect();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {

        View layout = context.findViewById(R.id.coordinatorLayout);
        Snackbar snackbar = Snackbar.make(layout, R.string.snackbarDownloadImageText, Snackbar.LENGTH_INDEFINITE)
                .setActionTextColor(Utils.getColor(context, R.color.white))
                .setAction(R.string.snackbarDownloadImageAction, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Uri uri = Uri.fromFile(openPath.getAbsoluteFile());
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(uri, "image/*");
//                        intent.setDataAndType(Uri.parse("file://" + uri), "image/*");
                        context.startActivity(intent);
//                        context.startActivity(Intent.createChooser(intent, text));

                    }
                });

        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(color);
        snackbar.show();

        super.onPostExecute(s);
    }

}

