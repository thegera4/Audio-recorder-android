package com.grabadora.audio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private Button btnRec, btnPlay;
    private MediaRecorder grabacion;
    private String archivoSalida = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnRec = (Button)findViewById(R.id.btnRec);
        btnPlay = (Button)findViewById(R.id.btnPlay);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, 1000);
        }

        btnRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (grabacion == null){
                    archivoSalida = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Grabacion.mp3";
                    grabacion = new MediaRecorder();
                    grabacion.setAudioSource(MediaRecorder.AudioSource.MIC);
                    grabacion.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                    grabacion.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
                    grabacion.setOutputFile(archivoSalida);

                    try {
                        grabacion.prepare();
                        grabacion.start();
                    } catch (IOException e) {

                    }

                    btnRec.setBackgroundResource(R.drawable.rec);
                    Toast.makeText(MainActivity.this, "Grabando...", Toast.LENGTH_SHORT).show();

                } else if (grabacion != null){
                   grabacion.stop();
                    grabacion.release();
                    grabacion = null;
                    btnRec.setBackgroundResource(R.drawable.stop_rec);
                    Toast.makeText(MainActivity.this, "Grabacion finalizada", Toast.LENGTH_SHORT).show();

                }
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MediaPlayer MP = new MediaPlayer();
                try {
                    MP.setDataSource(archivoSalida);
                    MP.prepare();
                } catch (IOException e){
                }
                MP.start();
                Toast.makeText(MainActivity.this, "Reproduciendo audio", Toast.LENGTH_SHORT).show();
            }
        });
    }
}