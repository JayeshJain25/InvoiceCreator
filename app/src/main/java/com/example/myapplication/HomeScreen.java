package com.example.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HomeScreen extends Fragment implements  View.OnClickListener{
    private onFragmentBtnSelected onFragmentBtnSelected;
    private onFragmentBtnClicked onFragmentBtnClicked;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_screen,container,false);
        Button clickMe = view.findViewById(R.id.viewPDF);
        Button savePDF = view.findViewById(R.id.createPDF);

        clickMe.setOnClickListener(this);
        savePDF.setOnClickListener(this);

        return view;
    }
    @Override
    public void onAttach(@NonNull Context context) {
         super.onAttach(context);
        if(context instanceof onFragmentBtnSelected){
            onFragmentBtnSelected =(onFragmentBtnSelected) context;
        }if (context instanceof  onFragmentBtnClicked){
            onFragmentBtnClicked = (onFragmentBtnClicked) context;
        }
        else{
            throw new ClassCastException(context.toString() + " must implement listener");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.createPDF:
                onFragmentBtnSelected.onButtonSelected();
                break;
            case R.id.viewPDF:
                onFragmentBtnClicked.onButtonClicked();
                break;
        }
    }
    public interface onFragmentBtnSelected{
        void onButtonSelected();
    }
    public interface  onFragmentBtnClicked{
        void onButtonClicked();
    }

}




