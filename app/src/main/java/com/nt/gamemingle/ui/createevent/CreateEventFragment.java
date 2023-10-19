package com.nt.gamemingle.ui.createevent;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.nt.gamemingle.R;
import com.nt.gamemingle.databinding.FragmentCreateEventBinding;
import com.nt.gamemingle.model.BoardGame;
import com.nt.gamemingle.ui.common.BaseFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CreateEventFragment extends BaseFragment {

    private CreateEventViewModel mViewModel;

    FragmentCreateEventBinding binding;

    String formattedDate = "", formattedTime = "";
    private List<BoardGame> boardGameList = new ArrayList<>();


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

        binding.removeTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                formattedDate = "";
                formattedTime = "";
                binding.linearLayoutShowTime.setVisibility(View.GONE);
                binding.linearLayoutAddTime.setVisibility(View.VISIBLE);
            }
        });

        binding.btnCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performCreateEvent();
            }
        });

        mViewModel.getBoardGames(requireContext());
        final Observer<List<BoardGame>> isBoardGameLoaded = new Observer<List<BoardGame>>() {

            @Override
            public void onChanged(List<BoardGame> boardGamesLiveData) {
                if (boardGamesLiveData != null) {
                    boardGameList = mViewModel.boardGamesLiveDataCreateEvent.getValue();
                    String[] boardGamesNamesArray = new String[boardGameList.size()];
                    for (int i = 0; i < boardGameList.size(); i++) {
                        boardGamesNamesArray[i] = boardGameList.get(i).getGameName();
                    }
                    // AutoCompleteTextView for cities
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, boardGamesNamesArray);
                    binding.autoCompleteTextViewBoardGame.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }
        };
        mViewModel.boardGamesLiveDataCreateEvent.observe(getViewLifecycleOwner(), isBoardGameLoaded);
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

            calendar.add(Calendar.DAY_OF_MONTH, 1);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String formattedDate = sdf.format(calendar.getTime());
            binding.textViewShowTime.setText(formattedDate);
            this.formattedDate = formattedDate;
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

            int hour = picker.getHour();
            int minute = picker.getMinute();

            String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", hour, minute);

            binding.textViewShowTime.setText(binding.textViewShowTime.getText() + " - " + formattedTime);
            binding.linearLayoutShowTime.setVisibility(View.VISIBLE);
            binding.linearLayoutAddTime.setVisibility(View.GONE);
            this.formattedTime = formattedTime;
        });
    }

    private void performCreateEvent(){

        String eventName = binding.eventName.getText().toString();
        String eventLocation = binding.location.getText().toString();
        String eventDescription = binding.description.getText().toString();
        String selectedGame = binding.autoCompleteTextViewBoardGame.getText().toString();
        if(eventName.isEmpty()){
            binding.eventName.setError("Event name is required");
            return;
        }
        if(eventLocation.isEmpty()){
            binding.location.setError("Event location is required");
            return;
        }
        if(eventDescription.isEmpty()){
            binding.description.setError("Event description is required");
            return;
        }
        if(formattedDate.isEmpty()){
            binding.textViewShowTime.setError("Event date is required");
            return;
        }
        if(formattedTime.isEmpty()){
            binding.textViewShowTime.setError("Event time is required");
            return;
        }
        if(selectedGame.isEmpty()){
            binding.autoCompleteTextViewBoardGame.setError("Board game is required");
            return;
        }
        String gameId = "";
        for (int i = 0; i < boardGameList.size(); i++) {
            if(boardGameList.get(i).getGameName().equals(selectedGame)){
                gameId = boardGameList.get(i).getBoardGameId();
            }
        }
        mViewModel.createEvent(eventName, eventLocation, eventDescription, gameId, formattedDate, formattedTime);
    }

}