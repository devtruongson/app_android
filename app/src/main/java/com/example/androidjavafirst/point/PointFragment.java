package com.example.androidjavafirst.point;

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

import com.example.androidjavafirst.R;
import com.example.androidjavafirst.databinding.FragmentPointBinding;
import com.example.androidjavafirst.dialog.CustomTimePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class PointFragment extends Fragment {
    private FragmentPointBinding binding;

    private static final String DATE_FORMAT = "dd/MM/yyyy";
    private static final String TIME_FORMAT = "HH:mm:ss";
    private static final String DATE_TIME_FORMAT = DATE_FORMAT + " " + TIME_FORMAT;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPointBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.dpStartDate.setOnClickListener(v -> showDatePickerDialog(binding.startDatePoint));
        binding.tpStartTime.setOnClickListener(v -> showTimePickerDialog(binding.startTimePoint));
        binding.endTime.setOnClickListener(v -> showTimePickerDialog(binding.timeOffset));
        binding.calculatePoint.setOnClickListener(v -> calculatePoint());
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

    private void calculatePoint() {
        String startDateStr = binding.startDatePoint.getText().toString();
        String startTimeStr = binding.startTimePoint.getText().toString();
        String daysOffsetStr = binding.daysOffset.getText().toString();
        String timeOffsetStr = binding.timeOffset.getText().toString();

        if (TextUtils.isEmpty(startDateStr) || TextUtils.isEmpty(startTimeStr) ||
                TextUtils.isEmpty(daysOffsetStr) || TextUtils.isEmpty(timeOffsetStr)) {
            Toast.makeText(getActivity(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault());

        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateFormat.parse(startDateStr + " " + startTimeStr));

            int daysOffset = Integer.parseInt(daysOffsetStr);
            String[] timeOffsetParts = timeOffsetStr.split(":");

            if (timeOffsetParts.length != 3) {
                Toast.makeText(getActivity(), "Định dạng thời gian chênh lệch không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }

            int hoursOffset = Integer.parseInt(timeOffsetParts[0]);
            int minutesOffset = Integer.parseInt(timeOffsetParts[1]);
            int secondsOffset = Integer.parseInt(timeOffsetParts[2]);

            if (binding.radioGroup.getCheckedRadioButtonId() == R.id.radioAfter) {
                calendar.add(Calendar.DAY_OF_MONTH, daysOffset);
                calendar.add(Calendar.HOUR_OF_DAY, hoursOffset);
                calendar.add(Calendar.MINUTE, minutesOffset);
                calendar.add(Calendar.SECOND, secondsOffset);
            } else {
                calendar.add(Calendar.DAY_OF_MONTH, -daysOffset);
                calendar.add(Calendar.HOUR_OF_DAY, -hoursOffset);
                calendar.add(Calendar.MINUTE, -minutesOffset);
                calendar.add(Calendar.SECOND, -secondsOffset);
            }

            String result = dateFormat.format(calendar.getTime());
            binding.resultPoint.setText(result);

        } catch (ParseException | NumberFormatException e) {
            Toast.makeText(getActivity(), "Định dạng ngày giờ không hợp lệ", Toast.LENGTH_SHORT).show();
        }
    }
}
