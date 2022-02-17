package com.example.j_restoraunt;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class ActivityImageZoom extends AppCompatActivity {

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_zoom);
        imageView = (ImageView) findViewById(R.id.image_zoom);
        final Bundle arguments = getIntent().getExtras();

        Picasso.with(ActivityImageZoom.this)
                .load("http://185.46.10.22" + arguments.get("url"))
                .into(imageView);
    }
}
