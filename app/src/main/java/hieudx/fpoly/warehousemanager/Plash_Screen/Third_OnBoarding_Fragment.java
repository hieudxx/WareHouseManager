package hieudx.fpoly.warehousemanager.Plash_Screen;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import hieudx.fpoly.warehousemanager.R;
import hieudx.fpoly.warehousemanager.Login_SignUp_Forget_Reset.Activity.Login_SignUp_Activity;

public class Third_OnBoarding_Fragment extends Fragment {

    public Third_OnBoarding_Fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_third__on_boarding_, container, false);

        FloatingActionButton fab = v.findViewById(R.id.fab);
        fab.setOnClickListener(view -> startActivity(new Intent(getActivity(), Login_SignUp_Activity.class)));

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView tvSkipThird = v.findViewById(R.id.tvSkipThird);
        tvSkipThird.setOnClickListener(view -> {
            startActivity(new Intent(getContext(), Login_SignUp_Activity.class));
        });
        return v;

    }
}