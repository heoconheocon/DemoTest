package com.hoangnhu.demo.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hoangnhu.demo.Controller.MovieController;
import com.hoangnhu.demo.Model.Movie;
import com.hoangnhu.demo.R;
import com.hoangnhu.demo.Until.DataUntil;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 10 ;
    private ImageView thumbMovie;
    private TextView mTitleMovie;
    private String jsonData;
    private int position = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission();

    }

    private void startData() {
        jsonData = "{'title':'Civil War','image':['http://movie.phinf.naver.net/20151127_272/1448585271749MCMVs_JPEG/movie_image.jpg?type=m665_443_2','http://movie.phinf.naver.net/20151127_84/1448585272016tiBsF_JPEG/movie_image.jpg?type=m665_443_2','http://movie.phinf.naver.net/20151125_36/1448434523214fPmj0_JPEG/movie_image.jpg?type=m665_443_2']}";
        initView();
        Movie movie = getMovie(jsonData);
        loadMovie(movie);
    }

    private void checkPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(MainActivity.this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE: {
                startData();
            }
        }
    }
    // Get movie from json
    private Movie getMovie(String json) {
        Movie movie = null;
        if(json.isEmpty())
            return null;
        try {
            MovieController.AsyncTaskGetData task = new MovieController.AsyncTaskGetData(MainActivity.this);
             movie = task.execute(json).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return movie;
    }

    // binding movie to UI
    private void loadMovie(final Movie movie) {
        if(movie == null)
            return;
        mTitleMovie.setText(movie.getTitle());
        if(movie.getImage() != null && movie.getImage().size() >0) {
            loadImage(movie.getImage().get(position), thumbMovie);
        }
        thumbMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position < movie.getImage().size()-1){
                    position ++;
                    loadImage(movie.getImage().get(position),thumbMovie);
                }else{
                    position = 0;
                }
            }
        });

    }

    // binding data to imageview
    private void loadImage(String path,ImageView imageView) {
        DataUntil.AsyncTaskDownloadImage task = new DataUntil.AsyncTaskDownloadImage(MainActivity.this);
        try {
            Bitmap bitmap = task.execute(path).get();
            imageView.setImageBitmap(bitmap);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    // init view
    private void initView() {
        mTitleMovie = findViewById(R.id.titleMovie);
        thumbMovie = findViewById(R.id.thumbnailMovie1);
    }

}
