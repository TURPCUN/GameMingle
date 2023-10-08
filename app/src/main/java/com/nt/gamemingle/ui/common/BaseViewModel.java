package com.nt.gamemingle.ui.common;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.navigation.NavController;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

abstract public class BaseViewModel extends ViewModel {

    /** Firebase Instances **/
    protected FirebaseAuth mAuth = FirebaseAuth.getInstance();
    protected FirebaseDatabase database = FirebaseDatabase.getInstance();
    protected DatabaseReference databaseReference = database.getReference();

    private final MutableLiveData<NavController> navController = new MutableLiveData<>();

    public void setNavController(NavController controller) {
        navController.setValue(controller);
    }

    public LiveData<NavController> getNavController() {
        return navController;
    }


}
