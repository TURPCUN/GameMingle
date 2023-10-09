package com.nt.gamemingle.app;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.navigation.NavController;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AppViewModel extends ViewModel {
    /** Firebase Instances **/
    public final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public final FirebaseDatabase database = FirebaseDatabase.getInstance();
    public final DatabaseReference databaseReference = database.getReference();

    private final MutableLiveData<NavController> navController = new MutableLiveData<>();

    public void setNavController(NavController controller) {
        navController.setValue(controller);
    }

    public LiveData<NavController> getNavController() {
        return navController;
    }

    public void signOut() {
        mAuth.signOut();
    }

    public static AppViewModel instance = new AppViewModel();
    private AppViewModel() {
        super();
    }

}
