package com.nt.gamemingle.ui.searchgames;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.nt.gamemingle.R;
import com.nt.gamemingle.app.AppViewModel;
import com.nt.gamemingle.ui.chat.ChatViewModel;

public class RequestGameDialogFragment extends DialogFragment {

    private SearchGamesViewModel searchGamesViewModel;
    public RequestGameDialogFragment(AppViewModel appViewModel) {
        searchGamesViewModel = new SearchGamesViewModel(appViewModel);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_request, null);

        EditText txtGameName = view.findViewById(R.id.gameNameReq);
        EditText txtGameDescription = view.findViewById(R.id.gameDescriptionReq);

        return new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Request a game")
                .setView(view)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        searchGamesViewModel
                                .requestGame(txtGameName.getText().toString(), txtGameDescription.getText().toString());
                        Toast.makeText(requireContext(), "Request has been sended.", Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
    }
}

