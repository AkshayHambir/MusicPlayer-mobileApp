package com.akshay.music;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    TextView playerPosition,playerDuration,songName;
    SeekBar seekBar;
    ImageView btrew,btplay,btpause,btff,btstop,btnext,btprev;

    MediaPlayer mediaPlayer;
    Handler handler = new Handler();
    Runnable runnable;
    ListView listView ;
    ArrayList<String> arrayList;
    ArrayAdapter arrayAdapter;
    int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playerPosition = findViewById(R.id.player_position);
        playerDuration = findViewById(R.id.player_duration);
        seekBar = findViewById(R.id.seek_bar);
        btrew = findViewById(R.id.bt_rew);
        btplay = findViewById(R.id.bt_play);
        btpause = findViewById(R.id.bt_pause);
        btff = findViewById(R.id.bt_ff);
        btstop = findViewById(R.id.bt_stop);
        btnext = findViewById(R.id.bt_next);
        btprev = findViewById(R.id.bt_prev);
        listView = findViewById(R.id.lstView);
        songName = findViewById(R.id.songName);

        arrayList = new ArrayList<>();
        Field[] fields = R.raw.class.getFields();
        for(int i = 0;i<fields.length;i++){
            arrayList.add(fields[i].getName());
        }

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,arrayList);

        listView.setAdapter(arrayAdapter);

        mediaPlayer = MediaPlayer.create(this, R.raw.song1);
        songName.setText(arrayList.get(0));

        runnable = new Runnable() {
            @Override
            public void run() {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                //Handler post delay for 0.5 second
                handler.postDelayed(this,500);
            }
        };

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                position = i;
                if(mediaPlayer != null){
                    mediaPlayer.stop();
                    mediaPlayer.release();
                }
                int resId = getResources().getIdentifier(arrayList.get(i),"raw",getPackageName());
                mediaPlayer = MediaPlayer.create(MainActivity.this, resId);
                songName.setText(arrayList.get(position));
                btplay.setVisibility(View.GONE);
                //show pause button
                btpause.setVisibility(View.VISIBLE);
                //start mediaplayer
                mediaPlayer.start();
                //set max on seek bar
                seekBar.setMax(mediaPlayer.getDuration());
                //start handler
                handler.postDelayed(runnable,0);
                //get duration of media player
                int duration = mediaPlayer.getDuration();
                //convert milliseconds to minutes and seconds
                String sDuration = convertFormat(duration);
                //set duration on text view
                playerDuration.setText(sDuration);

            }
        });

        btnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();

                position = (position+1)%arrayList.size();

                int resId = getResources().getIdentifier(arrayList.get(position),"raw",getPackageName());
                mediaPlayer = MediaPlayer.create(MainActivity.this, resId);
                songName.setText(arrayList.get(position));
                btplay.setVisibility(View.GONE);
                //show pause button
                btpause.setVisibility(View.VISIBLE);
                //start mediaplayer
                mediaPlayer.start();
                //set max on seek bar
                seekBar.setMax(mediaPlayer.getDuration());
                //start handler
                handler.postDelayed(runnable,0);
                //get duration of media player
                int duration = mediaPlayer.getDuration();
                //convert milliseconds to minutes and seconds
                String sDuration = convertFormat(duration);
                //set duration on text view
                playerDuration.setText(sDuration);


            }
        });

        btprev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();

                position = ((position-1)<0)?(arrayList.size()-1):(position-1);

                int resId = getResources().getIdentifier(arrayList.get(position),"raw",getPackageName());
                mediaPlayer = MediaPlayer.create(MainActivity.this, resId);
                songName.setText(arrayList.get(position));
                btplay.setVisibility(View.GONE);
                //show pause button
                btpause.setVisibility(View.VISIBLE);
                //start mediaplayer
                mediaPlayer.start();
                //set max on seek bar
                seekBar.setMax(mediaPlayer.getDuration());
                //start handler
                handler.postDelayed(runnable,0);
                //get duration of media player
                int duration = mediaPlayer.getDuration();
                //convert milliseconds to minutes and seconds
                String sDuration = convertFormat(duration);
                //set duration on text view
                playerDuration.setText(sDuration);
            }
        });


        //get duration of media player
        int duration = mediaPlayer.getDuration();
        //convert milliseconds to minutes and seconds
        String sDuration = convertFormat(duration);
        //set duration on text view
        playerDuration.setText(sDuration);

        btstop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stop_player();
                handler.removeCallbacks(runnable);
                playerDuration.setText("00:00");
                playerPosition.setText("00:00");
                //Hide pause button
                btpause.setVisibility(View.GONE);
                //show play button
                btplay.setVisibility(View.VISIBLE);
                songName.setText(arrayList.get(0));
            }
        });


        btplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mediaPlayer == null){
                    fill_player();
                }
                //Hide play button
                btplay.setVisibility(View.GONE);
                //show pause button
                btpause.setVisibility(View.VISIBLE);
                //start mediaplayer
                mediaPlayer.start();
                //set max on seek bar
                seekBar.setMax(mediaPlayer.getDuration());
                //start handler
                handler.postDelayed(runnable,0);
            }
        });

        btpause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Hide pause button
                btpause.setVisibility(View.GONE);
                //show play button
                btplay.setVisibility(View.VISIBLE);
                //pause the mediaplayer
                mediaPlayer.pause();
                //stop handler
                handler.removeCallbacks(runnable);
            }
        });

        btff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //current position
                int currentPosition = mediaPlayer.getCurrentPosition();
                //duration of media player
                int duration = mediaPlayer.getDuration();
                //check condition
                if(mediaPlayer.isPlaying() && duration!=currentPosition){
                    //fast forward for 5 seconds
                    currentPosition = currentPosition + 5000;
                    //set current position on text view
                    playerPosition.setText(convertFormat(currentPosition));
                    //set progress on seek bar
                    mediaPlayer.seekTo(currentPosition);

                }
            }
        });

        btrew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get current position
                int currentPosition = mediaPlayer.getCurrentPosition();
                //check
                if(mediaPlayer.isPlaying() && currentPosition > 5000){
                    //Rewind for 5 seconds
                    currentPosition = currentPosition - 5000;
                    //set current position on text view
                    playerPosition.setText(convertFormat(currentPosition));
                    //set progress on seek bar
                    mediaPlayer.seekTo(currentPosition);
                }
            }
        });


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                //check
                if(b){
                    //when drag the seekbar
                    //set progress on seekbar
                    mediaPlayer.seekTo(i);
                }
                //set current position on text view
                playerPosition.setText(convertFormat(mediaPlayer.getCurrentPosition()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                //hide pause button
                btpause.setVisibility(View.GONE);
                //show play button
                btplay.setVisibility(View.VISIBLE);
                //set media player to initial position
                mediaPlayer.seekTo(0);
            }
        });

    }

    private void stop_player(){
        if(mediaPlayer!=null){
            mediaPlayer.release();
            mediaPlayer = null;
            Toast.makeText(this, "Media player is empty", Toast.LENGTH_SHORT).show();
        }
    }

    private void fill_player(){
        mediaPlayer = MediaPlayer.create(this, R.raw.song1);
        int duration = mediaPlayer.getDuration();
        //convert milliseconds to minutes and seconds
        String sDuration = convertFormat(duration);
        //set duration on text view
        playerDuration.setText(sDuration);
    }


    private String convertFormat(int duration) {
        return String.format("%02d:%02d"
        , TimeUnit.MILLISECONDS.toMinutes(duration)
        ,TimeUnit.MILLISECONDS.toSeconds(duration) -
        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));
    }
}