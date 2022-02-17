package com.example.j_restoraunt;

import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestarauntPageActivity extends AppCompatActivity {

    public GerritAPI gerritAPI;
    private Call<RequestGetRestarauntsInfo> call_get_resraraunt_info;
    public TextView act_restaraunt_description;
    public Button act_restaraunt_btn_booking;
    private ViewPager viewPager;
    private ViewPager viewPagerMenu;
    ImageView first_page_menu;
    public TextView btn_to_main;
    private Button btn_cancel_activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaraunt);

        act_restaraunt_description = (TextView) findViewById(R.id.act_restaraunt_description);
        act_restaraunt_btn_booking = (Button) findViewById(R.id.act_restaraunt_btn_booking);
        first_page_menu = (ImageView) findViewById(R.id.first_page_menu);

        btn_cancel_activity = (Button) findViewById(R.id.act_restaraunt_btn_cancel_booking);

        btn_cancel_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RestarauntPageActivity.this, CancelBookingActivity.class);
                startActivity(intent);
            }
        });

        btn_to_main = (TextView) findViewById(R.id.btn_to_main);

        btn_to_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RestarauntPageActivity.this, FirstPageActivity.class);
                startActivity(intent);
            }
        });

        final Bundle arguments = getIntent().getExtras();

        final String BASE_URL = "http://185.46.10.22";
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        gerritAPI = retrofit.create(GerritAPI.class);
        GetRestarauntInfo((String) arguments.get("restaraunt_label"));

        act_restaraunt_btn_booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RestarauntPageActivity.this, BookingActivity.class);
                intent.putExtra("restaraunt_label", (String) arguments.get("restaraunt_label"));
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
        PopupMenu popupMenu = new PopupMenu(RestarauntPageActivity.this, view);
        popupMenu.setGravity(Gravity.END);
        popupMenu.getMenuInflater().inflate(R.menu.main_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (String.valueOf(menuItem.getTitle())) {
                    case ("Мои брони"):
                        Log.d("click", "1");
                        break;
                    case ("Отменить бронь"):
                        Intent intent = new Intent(RestarauntPageActivity.this, CancelBookingActivity.class);
                        startActivity(intent);
                        break;
                    case("Личный кабинет"):
                        Log.d("click", "3");
                        break;
                }
                return false;
            }
        });
        popupMenu.show();
    }

    private void GetRestarauntInfo(String label) {
        call_get_resraraunt_info = gerritAPI.getRestarauntInfo(new RequestGetRestarauntsInfoBody(label));
        call_get_resraraunt_info.enqueue(new Callback<RequestGetRestarauntsInfo>() {
            @Override
            public void onResponse(Call<RequestGetRestarauntsInfo> call, Response<RequestGetRestarauntsInfo> response) {
                act_restaraunt_description.setText(response.body().description);


                viewPager = (ViewPager) findViewById(R.id.act_restaraunt_viewPager);
                ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(RestarauntPageActivity.this, response.body().photos);

                viewPagerMenu = (ViewPager) findViewById(R.id.act_restaraunt_viewPager_menu);
                ViewPagerAdapter viewPagerAdapterMenu = new ViewPagerAdapter(RestarauntPageActivity.this, response.body().menu);

                viewPager.setAdapter(viewPagerAdapter);
                viewPagerMenu.setAdapter(viewPagerAdapterMenu);

            }

            @Override
            public void onFailure(Call<RequestGetRestarauntsInfo> call, Throwable t) {
                Log.d("error", t.toString());
            }
        });
    }
}
