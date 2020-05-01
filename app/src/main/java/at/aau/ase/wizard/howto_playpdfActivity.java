package at.aau.ase.wizard;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


import com.github.barteksc.pdfviewer.PDFView;

public class howto_playpdfActivity extends AppCompatActivity {
    PDFView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_howto_playpdf);

        pdfView = (PDFView) findViewById(R.id.pdfView);
        pdfView.fromAsset("Spielanleitung_Wizard.pdf").load();
    }

}
