package com.gegosoft.schoolteacherapp.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gegosoft.schoolteacherapp.R;
import com.gegosoft.schoolteacherapp.databinding.PdfwebviewBinding;
import com.rajat.pdfviewer.PdfViewerActivity;
import com.rajat.pdfviewer.util.saveTo;

import java.util.HashMap;

public class PDFWebViewActivity extends AppCompatActivity {

    PdfwebviewBinding binding;
    String url;
    ImageView back;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = PdfwebviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

       back = findViewById(R.id.back);
       back.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               getOnBackPressedDispatcher().onBackPressed();
           }
       });
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            url = String.valueOf(Uri.parse(extras.getString("url")));
            launchPdfFromUrl(url);
        }

    }

    private void launchPdfFromUrl(String url) {

        Intent intent = PdfViewerActivity.Companion.launchPdfFromUrl(
                PDFWebViewActivity.this,
                url,
                "PDF",
                saveTo.DOWNLOADS,
                false,
                new HashMap<>());

        startActivity(intent);
        finish();
    }

}


