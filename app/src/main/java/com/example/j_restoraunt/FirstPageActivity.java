package com.example.j_restoraunt;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FirstPageActivity extends AppCompatActivity {

    private ListView listView;
    public GerritAPI gerritAPI;
    private Call<RequestGetRestaraunts> call_get_resraraunts;
    private List<String> item_labels;
    private List<String> item_images;
    private ImageView first_page_menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);

        listView = (ListView) findViewById(R.id.first_page_list_view);
        first_page_menu = (ImageView) findViewById(R.id.first_page_menu);

        final String BASE_URL = "http://185.46.10.22";
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        gerritAPI = retrofit.create(GerritAPI.class);

        GetRestaraunts();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                Object o = listView.getItemAtPosition(i);
                String restaraunt_label = (String) o;
                Intent intent = new Intent(FirstPageActivity.this, RestarauntPageActivity.class);
                intent.putExtra("restaraunt_label", restaraunt_label);
                startActivity(intent);
            }
        });

        first_page_menu.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                showMenu(view);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void showMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(FirstPageActivity.this, view);
        popupMenu.setGravity(Gravity.END);
        popupMenu.getMenuInflater().inflate(R.menu.main_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (String.valueOf(menuItem.getTitle())) {
                    case ("Мои брони"):
                        break;
                    case ("Отменить бронь"):
                        Intent intent = new Intent(FirstPageActivity.this, CancelBookingActivity.class);
                        startActivity(intent);
                        break;
                    case("Личный кабинет"):
                        break;
                }
                return false;
            }
        });
        popupMenu.show();
    }

    private void GetRestaraunts() {
        call_get_resraraunts = gerritAPI.getRestaraunt(new RequestEmpty());
        call_get_resraraunts.enqueue(new Callback<RequestGetRestaraunts>() {
            @Override
            public void onResponse(Call<RequestGetRestaraunts> call, Response<RequestGetRestaraunts> response) {
                List<String> labels = new ArrayList<String>();
                for (int i = 0; i < response.body().items.size(); ++i) {
                    labels.add(response.body().items.get(i).get(0));
                }
                List<String> images = new ArrayList<String>();
                for (int i = 0; i < response.body().items.size(); ++i) {
                    images.add(response.body().items.get(i).get(1));
                }
                item_images = images;
                item_labels = labels;
                listView.setAdapter(new First_Page_Adapter(FirstPageActivity.this, R.layout.list_item_first_page, labels));
            }

            @Override
            public void onFailure(Call<RequestGetRestaraunts> call, Throwable t) {
                Log.d("error", t.toString());
            }
        });
    }


    private class First_Page_Adapter extends ArrayAdapter<String> {

        private String l;

        First_Page_Adapter (Context context, int textViewResourceId,
                            List<String> objects) {
            super(context, textViewResourceId, objects);
        }

        public String getString() {
            return l;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View row = inflater.inflate(R.layout.list_item_first_page, parent, false);
            TextView label = (TextView) row.findViewById(R.id.list_item_text);
            label.setText(item_labels.get(position));
            l = item_labels.get(position);
            ImageView iconImageView = (ImageView) row.findViewById(R.id.list_item_image);
            Picasso.with(FirstPageActivity.this)
                    .load("http://185.46.10.22" + item_images.get(position))
                    .transform(new RoundedCornersTransformation(10,10))
                    .resize(134, 96)
                    .into(iconImageView);
            return row;
        }
    }
}