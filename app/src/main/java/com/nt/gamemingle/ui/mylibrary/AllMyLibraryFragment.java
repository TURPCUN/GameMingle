package com.nt.gamemingle.ui.mylibrary;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.nt.gamemingle.R;
import com.nt.gamemingle.adapters.MyGamesLibrarySeeAllAdapter;
import com.nt.gamemingle.model.BoardGame;
import com.nt.gamemingle.ui.common.BaseFragment;

import java.util.ArrayList;


public class AllMyLibraryFragment extends BaseFragment implements MyGamesLibrarySeeAllAdapter.ItemClickListener {

    private AllMyLibraryViewModel mViewModel;
    ArrayList<BoardGame> myLibraryBoardGameList;
    private MyGamesLibrarySeeAllAdapter myLibrarySeeAllAdapter;
    RecyclerView recyclerMyLibrary;
    NavController navController;

    ExtendedFloatingActionButton fabCreateEvent;

    @Override
    public void onResume() {
        super.onResume();
        myLibraryBoardGameList = getArguments().getParcelableArrayList("myLibraryBoardGameList");
        myLibrarySeeAllAdapter = new MyGamesLibrarySeeAllAdapter(requireContext(), myLibraryBoardGameList, this);
        recyclerMyLibrary.setAdapter(myLibrarySeeAllAdapter);
    }

    public AllMyLibraryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myLibraryBoardGameList = getArguments().getParcelableArrayList("myLibraryBoardGameList");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_my_library, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setToolBarVisibility(true);

        navController = appViewModel.getNavController().getValue();
        mViewModel = new AllMyLibraryViewModel(appViewModel);

        recyclerMyLibrary = getActivity().findViewById(R.id.recycler_all_my_lib);
        recyclerMyLibrary.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false  ));
        myLibrarySeeAllAdapter = new MyGamesLibrarySeeAllAdapter(requireContext(), myLibraryBoardGameList, this);
        recyclerMyLibrary.setAdapter(myLibrarySeeAllAdapter);
        myLibrarySeeAllAdapter.notifyDataSetChanged();

        ItemTouchHelper.SimpleCallback callback =
                new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {

                    @Override
                    public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
                        return 0.1f;
                    }

                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                        int position = viewHolder.getAdapterPosition();
                        if (direction == ItemTouchHelper.LEFT) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle("Remove from My Library");
                            builder.setMessage("Are you sure you want to remove this game from your library, it will stay as your favourite game?");
                            builder.setPositiveButton("Yes", (dialog, which) -> {
                                mViewModel.removeMyLibraryGame(myLibraryBoardGameList.get(position));
                                myLibraryBoardGameList.remove(position);
                                myLibrarySeeAllAdapter.setMyLibraryBoardGameList(myLibraryBoardGameList);
                            });
                            builder.setNegativeButton("No", (dialog, which) -> {
                                myLibrarySeeAllAdapter.setMyLibraryBoardGameList(myLibraryBoardGameList);
                                dialog.dismiss();
                            });
                            builder.show();
                        }
                    }
                };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerMyLibrary);

    }

        @Override
        public void onItemClick(View view, int position) {
            Bundle bundle = new Bundle();
            bundle.putParcelable("game", myLibraryBoardGameList.get(position));
            bundle.putBoolean("hideSomeFields", true);
            navController.navigate(R.id.action_allMyLibraryFragment_to_addFavGameFragment, bundle);
        }
}