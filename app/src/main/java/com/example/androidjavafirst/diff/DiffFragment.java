package com.example.androidjavafirst.diff;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.androidjavafirst.databinding.FragmentDiffBinding;
import com.example.androidjavafirst.dialog.CustomTimePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DiffFragment extends Fragment {

    private FragmentDiffBinding binding;

    private static final String DATE_FORMAT = "dd/MM/yyyy";
    private static final String TIME_FORMAT = "HH:mm:ss";
    private static final String DATE_TIME_FORMAT = DATE_FORMAT + " " + TIME_FORMAT;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDiffBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.dpStartDate.setOnClickListener(v -> showDatePickerDialog(binding.startDate));
        binding.tpStartTime.setOnClickListener(v -> showTimePickerDialog(binding.startTime));
        binding.dpEndDate.setOnClickListener(v -> showDatePickerDialog(binding.endDate));
        binding.tpEndTime.setOnClickListener(v -> showTimePickerDialog(binding.endTime));
        binding.calculateDiff.setOnClickListener(v -> calculateDifference());
    }
    private void showDatePickerDialog(EditText editText) {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(getContext(), (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
            editText.setText(sdf.format(calendar.getTime()));
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void showTimePickerDialog(EditText editText) {
        new CustomTimePickerDialog(getContext(), (hourOfDay, minute, second) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, second);
            SimpleDateFormat sdf = new SimpleDateFormat(TIME_FORMAT, Locale.getDefault());
            editText.setText(sdf.format(calendar.getTime()));
        }).show();
    }

    private void calculateDifference() {
        String startDateStr = binding.startDate.getText().toString();
        String startTimeStr = binding.startTime.getText().toString();
        String endDateStr = binding.endDate.getText().toString();
        String endTimeStr = binding.endTime.getText().toString();

        if (TextUtils.isEmpty(startDateStr) || TextUtils.isEmpty(startTimeStr) ||
                TextUtils.isEmpty(endDateStr) || TextUtils.isEmpty(endTimeStr)) {
            Toast.makeText(getActivity(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault());

        try {
            Date startDate = dateFormat.parse(startDateStr + " " + startTimeStr);
            Date endDate = dateFormat.parse(endDateStr + " " + endTimeStr);

            if (startDate != null && endDate != null) {
                long diffInMillis = endDate.getTime() - startDate.getTime();

                long days = TimeUnit.MILLISECONDS.toDays(diffInMillis);
                long hours = TimeUnit.MILLISECONDS.toHours(diffInMillis) % 24;
                long minutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis) % 60;
                long seconds = TimeUnit.MILLISECONDS.toSeconds(diffInMillis) % 60;

                String result = String.format(Locale.getDefault(), "%d ngày, %d giờ, %d phút, %d giây", days, hours, minutes, seconds);
                binding.resultDiff.setText(result);
            }

        } catch (ParseException e) {
            Toast.makeText(getActivity(), "Định dạng ngày giờ không hợp lệ", Toast.LENGTH_SHORT).show();
        }
    }
}
