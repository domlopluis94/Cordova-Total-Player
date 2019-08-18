package luis.dominguez.totalplayerexample;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import org.json.JSONException;

import java.net.URI;

public class MainActivity extends AppCompatActivity {

    public VideoView video;
    private String videoPath ="url";
    private static ProgressDialog progressDialog;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        video = (VideoView) findViewById(R.id.videoView);
        //
        //


        mediaPlayer("http://almacenvideos.placertv.com/SUBIR_VIDEOS/15716_3.mp4",video);


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




    //---------- VLC Player ---------
    private boolean playvlc(String url) {
        return playv(url);
    }

    private boolean playv(final String url) {
        int vlcRequestCode = 42; //request code used when finished playing, not necessary if you only use startActivity(vlcIntent)
        Intent vlcIntent = new Intent(Intent.ACTION_VIEW);
        vlcIntent.setPackage("org.videolan.vlc");
        vlcIntent.setDataAndTypeAndNormalize(Uri.parse(url), "video/*");
        vlcIntent.putExtra("title", "ÑÑTV VLC");
        startActivityForResult(vlcIntent, vlcRequestCode);
        return true;
    }
}
