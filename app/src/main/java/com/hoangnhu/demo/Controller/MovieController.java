package com.hoangnhu.demo.Controller;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.hoangnhu.demo.Model.Movie;

public class MovieController {
    public static Movie getData(String jsonData) {
        Movie movie = Movie.getData(jsonData);
        return movie;
    }
    public static class AsyncTaskGetData extends AsyncTask<String, String, Movie> {
        private ProgressDialog dialog;
        private Context mContext;
        public AsyncTaskGetData(Context context){
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(mContext);
            dialog.setMessage("Please wait...It is get data");
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.show();
        }
        @Override
        protected Movie doInBackground(String... strings) {
            Movie movie = MovieController.getData(strings[0]);
            return movie;
        }
        @Override
        protected void onPostExecute(Movie bitmap) {
            super.onPostExecute(bitmap);
            dialog.hide();
        }
    }


}
