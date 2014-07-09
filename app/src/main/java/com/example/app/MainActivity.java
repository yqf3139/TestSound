package com.example.app;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import seu.lab.dolphin.echo.UltrasonicManager;

public class MainActivity extends Activity
        implements SeekBar.OnSeekBarChangeListener
{

    Handler handler = new Handler();

    SeekBar seekBar;
    TextView mProgressText;

    Activity context = this;

    private Button btn_stop;
    private Button btn_phone;
    private Button btn_media;
    private int index;
    private UltrasonicManager manager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);

        Button button_launch = (Button) findViewById(R.id.button1);
        button_launch.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v)
            {
                DisplayToast("click!");
                launch();
            }
        });

        seekBar = (SeekBar) findViewById(R.id.seekBar1);
        seekBar.setOnSeekBarChangeListener(this);
        mProgressText = (TextView) findViewById(R.id.progress);

        btn_stop = (Button)this.findViewById(R.id.btn_stop);
        btn_stop.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                stop();
            }
        });
        btn_phone = (Button) this.findViewById(R.id.phonespeaker);
        btn_media = (Button) this.findViewById(R.id.mediaspeaker);

        btn_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UltrasonicManager.switchToEarphoneSpeaker();
            }
        });
        btn_media.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UltrasonicManager.switchToLoudspeaker();
            }
        });

        manager = UltrasonicManager.getUltrasonicManager(
                (AudioManager) getSystemService(Context.AUDIO_SERVICE));
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void launch() {
        manager.emit(index);
        //manager.test();
    }

    public void stop(){
        manager.kill(index);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    public void onProgressChanged(SeekBar seekBar,int progress,boolean fromTouch){
        index = (UltrasonicManager.wavelenTable.length-1) * progress / 100;
        mProgressText.setText("now: " + UltrasonicManager.wavelenTable[index] + " wavelen");
    }

    public void onStartTrackingTouch(SeekBar seekBar){}

    public void onStopTrackingTouch(SeekBar seekBar){}


    public void DisplayToast(String str){
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
}

