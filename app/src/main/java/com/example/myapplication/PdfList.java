package com.example.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class PdfList extends Fragment {


    RecyclerView recyclerView;
    private viewListPDF viewListPDF;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pdf_list,container,false);

       recyclerView = view.findViewById(R.id.rv_view_pdf);
       viewListPDF.listPDF(recyclerView);

        return view;

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof  viewListPDF){
            viewListPDF = (viewListPDF)context;
        }
    }

    public interface viewListPDF{
        void listPDF(RecyclerView recyclerView);
    }

}
