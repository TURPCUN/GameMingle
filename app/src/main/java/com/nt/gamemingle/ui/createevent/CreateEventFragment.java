package com.nt.gamemingle.ui.createevent;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.nt.gamemingle.databinding.FragmentCreateEventBinding;
import com.nt.gamemingle.ui.common.BaseFragment;

import java.util.Calendar;

public class CreateEventFragment extends BaseFragment {

    private CreateEventViewModel mViewModel;

    FragmentCreateEventBinding binding;

    public CreateEventFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCreateEventBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setToolBarVisibility(true);

        mViewModel = new CreateEventViewModel(appViewModel);

        binding.linearLayoutAddTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMaterialDatePicker();
            }
        });
    }

    private void showMaterialDatePicker(){
        // Makes only dates from today forward selectable.
        CalendarConstraints constraintsBuilder =
                new CalendarConstraints.Builder()
                        .setValidator(DateValidatorPointForward.now()).build();

        MaterialDatePicker datePicker =
                MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Select date")
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                        .setCalendarConstraints(constraintsBuilder)
                        .build();

        datePicker.show(getParentFragmentManager(), "tag");

        datePicker.addOnPositiveButtonClickListener(selection -> {
            // Respond to positive button click.
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis((Long) selection);
            binding.textViewShowTime.setText((calendar.get(Calendar.DAY_OF_MONTH)+1) + "/" + (calendar.get(Calendar.MONTH)+1) + "/" + calendar.get(Calendar.YEAR));
            showMaterialTimePicker();
        });
    }

    private void showMaterialTimePicker(){
        MaterialTimePicker picker =
                new MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_12H)
                        .setHour(12)
                        .setMinute(10)
                        .setTitleText("Select time")
                        .setInputMode(MaterialTimePicker.INPUT_MODE_KEYBOARD)
                        .build();

        picker.show(getParentFragmentManager(), "tag");

        picker.addOnPositiveButtonClickListener(selection -> {
            // Respond to positive button click.
            binding.textViewShowTime.setText(binding.textViewShowTime.getText() + " " + picker.getHour() + ":" + picker.getMinute());
            binding.linearLayoutShowTime.setVisibility(View.VISIBLE);
            binding.linearLayoutAddTime.setVisibility(View.GONE);
        });
    }
}