package com.example.androidjavafirst.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import androidx.appcompat.app.AlertDialog;

import com.example.androidjavafirst.R;

import java.util.Calendar;

public class CustomTimePickerDialog {
    private final Context context;
    private final OnTimeSetListener listener;
    private int hourOfDay;
    private int minute;
    private int second;

    public interface OnTimeSetListener {
        void onTimeSet(int hourOfDay, int minute, int second);
    }

    public CustomTimePickerDialog(Context context, OnTimeSetListener listener) {
        this.context = context;
        this.listener = listener;

        Calendar calendar = Calendar.getInstance();
        this.hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        this.minute = calendar.get(Calendar.MINUTE);
        this.second = calendar.get(Calendar.SECOND);
    }

    public void show() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_time_picker, null);

        TimePicker timePicker = view.findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        timePicker.setCurrentHour(hourOfDay);
        timePicker.setCurrentMinute(minute);

        NumberPicker secondPicker = view.findViewById(R.id.secondPicker);
        secondPicker.setValue(second);
        secondPicker.setMinValue(0);
        secondPicker.setMaxValue(59);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view)
                .setPositiveButton("Set", (dialog, which) -> {
                    hourOfDay = timePicker.getCurrentHour();
                    minute = timePicker.getCurrentMinute();
                    second = secondPicker.getValue();
                    listener.onTimeSet(hourOfDay, minute, second);
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.cancel())
                .create()
                .show();
    }
}