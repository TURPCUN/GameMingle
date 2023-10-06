package com.nt.gamemingle.ui.common;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.navigation.NavController;

import com.google.firebase.auth.FirebaseAuth;

abstract public class BaseViewModel extends ViewModel {

    protected FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private final MutableLiveData<NavController> navController = new MutableLiveData<>();

    public void setNavController(NavController controller) {
        navController.setValue(controller);
    }

    public LiveData<NavController> getNavController() {
        return navController;
    }


}
