package com.gegosoft.yourappname.Helper;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.DialogFragment;

import com.gegosoft.yourappname.R;
import com.github.barteksc.pdfviewer.PDFView;

public class DialogPdfViewer extends DialogFragment {
    PDFView pdfView;
    byte[] decodedString;
    OnFragmentCommunicationListener mListener;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Translucent);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_pdf_viewer, container, false);


        decodedString = getArguments().getByteArray("byte");


        ImageView close_image = view.findViewById(R.id.close_image);
        close_image .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onDismissDialog();
                dismiss();
            }
        });

        pdfView = view.findViewById(R.id.pdfView);
        pdfView.fromBytes(decodedString).load();
        return view;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentCommunicationListener) {
            mListener = (OnFragmentCommunicationListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentCommunicationListener");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentCommunicationListener {
        void onDismissDialog();

    }
}