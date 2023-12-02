package com.nt.gamemingle.ui.creategame;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nt.gamemingle.R;
import com.nt.gamemingle.databinding.FragmentCreateGameBinding;
import com.nt.gamemingle.model.Requests;
import com.nt.gamemingle.ui.common.BaseFragment;
import com.squareup.picasso.Picasso;

public class CreateGameFragment extends BaseFragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int RESULT_OK = -1;
    private Uri mImageUri;
    private CreateGameViewModel mViewModel;
    FragmentCreateGameBinding binding;
    NavController navController;

    Requests requestedGame;

    public CreateGameFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            requestedGame = bundle.getParcelable("requestedGame");
            setToolBarVisibility(true);
        } else {
            setToolBarVisibility(false);
        }

        //Reset after coming back
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation_admin);
        Menu menu = bottomNavigationView.getMenu();
        menu.findItem(R.id.menuCreateGame).setChecked(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCreateGameBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = appViewModel.getNavController().getValue();
        mViewModel = new CreateGameViewModel(appViewModel);

        if(requestedGame != null){
            binding.gameName.setText(requestedGame.getRequestedGameName());
        }

        binding.imgGameBtnUpl.setOnClickListener(v -> {
            openFileChooser();
        });
        binding.btnAddGame.setOnClickListener(v -> {
            performAddGame();
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    private void performAddGame(){
        String gameName = binding.gameName.getText().toString();
        String gameCategory = binding.category.getText().toString();
        String gameDescription = binding.gameDescription.getText().toString();
        String gameMinPlayers = binding.minPlayers.getText().toString();
        String gameMaxPlayers = binding.maxPlayers.getText().toString();

        if(mImageUri == null){
            binding.imgGameBtnUpl.setError("Game image is required");
            binding.imgGameBtnUpl.requestFocus();
            return;
        }

        if (gameName.isEmpty()) {
            binding.gameName.setError("Game name is required");
            binding.gameName.requestFocus();
            return;
        }

        if (gameDescription.isEmpty()) {
            binding.gameDescription.setError("Game description is required");
            binding.gameDescription.requestFocus();
            return;
        }

        if (gameMinPlayers.isEmpty()) {
            binding.minPlayers.setError("Game min players is required");
            binding.minPlayers.requestFocus();
            return;
        }

        if (gameMaxPlayers.isEmpty()) {
            binding.maxPlayers.setError("Game max players is required");
            binding.maxPlayers.requestFocus();
            return;
        }

        if (gameCategory.isEmpty()) {
            binding.category.setError("Game category is required");
            binding.category.requestFocus();
            return;
        }

        String addedGameId = mViewModel.addGame(gameName, gameCategory, gameDescription, gameMinPlayers, gameMaxPlayers, mImageUri, getContext());

        if (requestedGame != null){
            mViewModel.deleteRequest(requestedGame, addedGameId);

        }
        navController.navigate(R.id.action_createGameFragment_to_searchGamesFragment);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();
            Picasso.with(requireContext())
                    .load(mImageUri)
                    .into(binding.imgGameUpl);
        }
    }
}