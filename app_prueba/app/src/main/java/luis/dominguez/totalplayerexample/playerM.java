package luis.dominguez.totalplayerexample;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class playerM extends AppCompatActivity {

    public VideoView video;
    private String videoPath ="url";
    private static ProgressDialog progressDialog;
    public ImageButton botondeCast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_m);

        video = (VideoView) findViewById(R.id.videoView);
        Intent intent = getIntent();
        String uri = intent.getStringExtra("url");
        mediaPlayer(uri,video);


        botondeCast = findViewById(R.id.imageButton);
        botondeCast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toast1 = Toast.makeText(getApplicationContext(),"Fede", Toast.LENGTH_SHORT);
                toast1.show();
            }
        });

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(botondeCast.getVisibility()==View.VISIBLE) {
            botondeCast.setVisibility(View.INVISIBLE);
            return true;
        }else {
            botondeCast.setVisibility(View.VISIBLE);
            return true;
        }
    }
    /**
     *
     * @param url
     * @param video
     */
    public void mediaPlayer(String url, final VideoView video){
        // Get the URL from String VideoURL
        try
        {
            progressDialog = ProgressDialog.show(this, "", "Cargando Contenido...", true);
            progressDialog.setCancelable(true);
            getWindow().setFormat(PixelFormat.TRANSLUCENT);
            MediaController mediaController = new MediaController(this);
            mediaController.setMediaPlayer(video);
            mediaController.setAnchorView(video);
            video.setMediaController(mediaController);
            video.setVideoURI(Uri.parse(url));
            video.requestFocus();
            video.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
            {

                public void onPrepared(MediaPlayer mp)
                {
                    progressDialog.dismiss();
                    video.start();
                }
            });


        }
        catch(Exception e)
        {
            progressDialog.dismiss();
            System.out.println("Video Play Error :"+e.toString());
            finish();
        }

        video.start();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

            this.getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            this.getWindow().clearFlags(
                    WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {

            this.getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            this.getWindow().clearFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

        }
    }
}
