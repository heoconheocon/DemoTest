package com.hoangnhu.demo.Until;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DataUntil {
    private static Bitmap decodeFile(File f){
        try {
            //decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f),null,o);
            final int REQUIRED_SIZE = 400;
            int widthTmp = o.outWidth, heightTmp = o.outHeight;
            int scale = 1;
            while(true){
                if(widthTmp/2 < REQUIRED_SIZE || heightTmp/2 < REQUIRED_SIZE)
                    break;
                widthTmp /= 2;
                heightTmp /= 2;
                scale *= 2;
            }

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            options.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static class AsyncTaskDownloadImage extends AsyncTask<String, String, Bitmap> {
        private ProgressDialog dialog;
        private String imagePath;
        private Bitmap bitmapMovie;
        private Context mContext;
        public AsyncTaskDownloadImage(Context context){
            mContext = context;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(mContext);
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setProgress(0);
            dialog.setMessage("Please wait...It is downloading");
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.setMax(100);
            dialog.show();
        }
        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                imagePath = strings[0];
                URL ImageUrl = new URL(imagePath);
                HttpURLConnection conn = (HttpURLConnection) ImageUrl.openConnection();
                conn.setDoInput(true);
                conn.connect();
                // save image to sdcard
                String targetFileName="movieImage"+".png";
                int lenghtOfFile = conn.getContentLength();
                String path = Environment.getExternalStorageDirectory()+ "/image/";
                File folder = new File(path);
                if(!folder.exists()){
                    folder.mkdir();
                }
                InputStream input = new BufferedInputStream(ImageUrl.openStream());
                File fileImage = new File(path+targetFileName);
                if(!fileImage.exists()){
                    fileImage.createNewFile();
                }
                OutputStream output = new FileOutputStream(fileImage.getAbsolutePath());
                byte data[] = new byte[1024];
                long total = 0;
                int count ;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    final int progess = (int)(total*100/lenghtOfFile);
                    publishProgress (String.valueOf(progess));
                    output.write(data, 0, count);
                    Log.d("TAG","progess: "+progess );


                }
                output.flush();
                output.close();
                input.close();


                // get bitmap
//                InputStream is = conn.getInputStream();
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//                bitmapMovie = BitmapFactory.decodeStream(is, null, options);
                bitmapMovie = decodeFile(fileImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmapMovie;
        }
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            dialog.dismiss();
        }

        @Override
        protected void onProgressUpdate(final String... values) {
            super.onProgressUpdate(values);
            ((Activity)mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dialog.setProgress(Integer.parseInt(values[0]));
                }
            });
        }
    }
}
