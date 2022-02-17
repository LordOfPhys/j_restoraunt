package com.example.j_restoraunt;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BookingActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private TextView booking_time;
    private EditText count_man;
    private Button btn_make_booking;
    int day, month, year, hour, minute;
    int dayFinal, monthFinal, yearFinal, hourFinal, minuteFinal;

    private Call<RequestMakeBooking> call_make_booking;
    public GerritAPI gerritAPI;
    ImageView first_page_menu;
    public TextView btn_to_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        btn_to_main = (TextView) findViewById(R.id.btn_to_main);

        btn_to_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BookingActivity.this, FirstPageActivity.class);
                startActivity(intent);
            }
        });

        final String BASE_URL = "http://185.46.10.22";
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        gerritAPI = retrofit.create(GerritAPI.class);

        final Bundle arguments = getIntent().getExtras();

        booking_time = (TextView) findViewById(R.id.booking_time);
        count_man = (EditText) findViewById(R.id.edit_booking);
        btn_make_booking = (Button) findViewById(R.id.btn_make_booking);


        first_page_menu = (ImageView) findViewById(R.id.first_page_menu);

        booking_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(BookingActivity.this, AlertDialog.THEME_HOLO_LIGHT, BookingActivity.this, year, month, day);
                datePickerDialog.show();
            }
        });

        first_page_menu.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                showMenu(view);
            }
        });

        btn_make_booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String month_for_request = "01";
                String day_for_request = "01";
                if (monthFinal < 10) {
                    month_for_request = "0" + String.valueOf(monthFinal);
                }
                if (dayFinal < 10) {
                    day_for_request = "0" + String.valueOf(dayFinal);
                }
                makeBooking((String) arguments.get("restaraunt_label"), count_man.getText().toString(), String.valueOf(hourFinal) + ":" + String.valueOf(minuteFinal),
                        String.valueOf(yearFinal) + "-" + month_for_request + "-" + day_for_request);
            }
        });
    }

    private void makeBooking(String label, String size, String time, String date) {
        call_make_booking = gerritAPI.makeBooking(new RequestMakeBookingBody(label, size, time, date));
        call_make_booking.enqueue(new Callback<RequestMakeBooking>() {
            @Override
            public void onResponse(Call<RequestMakeBooking> call, Response<RequestMakeBooking> response) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Номер вашей брони: " + "\n" + response.body().booking_number, Toast.LENGTH_LONG);
                toast.show();
                Intent intent = new Intent(BookingActivity.this, FirstPageActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<RequestMakeBooking> call, Throwable t) {
                Log.d("error", t.toString());
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void showMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(BookingActivity.this, view);
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
                        Intent intent = new Intent(BookingActivity.this, CancelBookingActivity.class);
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

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        yearFinal = i;
        monthFinal = i1 + 1;
        dayFinal = i2;

        Calendar calendar = Calendar.getInstance();
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(BookingActivity.this, AlertDialog.THEME_HOLO_LIGHT, BookingActivity.this, hour, minute, DateFormat.is24HourFormat(this));
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        hourFinal = i;
        minuteFinal = i1;

        booking_time.setText(dayFinal + "." + monthFinal + "." + yearFinal + "\n" + hourFinal + ":" + minuteFinal);

    }
}
