package com.nt.gamemingle.ui.addfavgame;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.nt.gamemingle.R;
import com.nt.gamemingle.model.BoardGame;
import com.nt.gamemingle.ui.common.BaseFragment;


public class AddFavGameFragment extends BaseFragment {

    private AddFavGameViewModel mViewModel;
    private TextView titleGame;
    private Button btnAddFavGame;

    private ImageView imageView;
    NavController navController;
    BoardGame boardGame;
    public AddFavGameFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        boardGame = getArguments().getParcelable("game");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boardGame = getArguments().getParcelable("game");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_fav_game, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setToolBarVisibility(true);

        mViewModel = new AddFavGameViewModel(appViewModel);
        navController = appViewModel.getNavController().getValue();

        titleGame = getActivity().findViewById(R.id.titleGame);
        titleGame.setText(boardGame.getGameName());

        // TODO refactor this
        int imageResource = getActivity().getResources()
                .getIdentifier(boardGame.getGameName().toLowerCase().replaceAll("\\s", ""), "drawable", getActivity().getPackageName());

        imageView = getActivity().findViewById(R.id.ImgGame);
        if (imageResource != 0) {
            imageView.setImageResource(imageResource);
        } else{
            imageView.setImageResource(R.drawable.icon);
        }

        btnAddFavGame = getActivity().findViewById(R.id.btn_add_fav_game);
        btnAddFavGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialCheckBox checkBox = getActivity().findViewById(R.id.checkBoxAddFavGame);
                addFavGameForUser(boardGame.getBoardGameId(), appViewModel.mAuth.getCurrentUser().getUid(), checkBox.isChecked());
            }
        });
    }

    private void addFavGameForUser(String gameID, String userID, boolean inLibrary){
        mViewModel.addFavGame(gameID, userID, inLibrary, getContext());

    }
}