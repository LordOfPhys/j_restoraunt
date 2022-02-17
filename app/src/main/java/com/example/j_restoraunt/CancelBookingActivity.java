package com.example.j_restoraunt;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CancelBookingActivity extends AppCompatActivity {

    private EditText editText;
    private Button btn;
    ImageView first_page_menu;
    public TextView btn_to_main;
    public GerritAPI gerritAPI;
    private Call<RequestCancelBooking> call_cancel_booking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_booking);

        btn_to_main = (TextView) findViewById(R.id.btn_to_main);

        btn_to_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CancelBookingActivity.this, FirstPageActivity.class);
                startActivity(intent);
            }
        });

        editText = (EditText) findViewById(R.id.edit_cancel);
        btn = (Button) findViewById(R.id.btn_cancel);
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

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelBooking(editText.getText().toString());
            }
        });

        first_page_menu.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                showMenu(view);
            }
        });

        btn_to_main = (TextView) findViewById(R.id.btn_to_main);

        btn_to_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CancelBookingActivity.this, FirstPageActivity.class);
                startActivity(intent);
            }
        });
    }

    private void cancelBooking(String booking_number) {
        call_cancel_booking = gerritAPI.cancelBooking(new RequestCancelBookingBody(booking_number));
        call_cancel_booking.enqueue(new Callback<RequestCancelBooking>() {
            @Override
            public void onResponse(Call<RequestCancelBooking> call, Response<RequestCancelBooking> response) {
                if (response.body().response.equals("200")) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Бронь успешно отменена", Toast.LENGTH_LONG);
                    toast.show();
                    Intent intent = new Intent(CancelBookingActivity.this, FirstPageActivity.class);
                    startActivity(intent);
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Такого номера брони нет", Toast.LENGTH_LONG);
                    toast.show();
                }
            }

            @Override
            public void onFailure(Call<RequestCancelBooking> call, Throwable t) {
                Log.d("error", t.toString());
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void showMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(CancelBookingActivity.this, view);
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
}
