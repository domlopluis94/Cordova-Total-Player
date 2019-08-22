package luis.dominguez.totalplayerexample;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.MediaRouteButton;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.MediaMetadata;
import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastOptions;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.OptionsProvider;
import com.google.android.gms.cast.framework.Session;
import com.google.android.gms.cast.framework.SessionManager;
import com.google.android.gms.cast.framework.SessionManagerListener;
import com.google.android.gms.cast.framework.SessionProvider;
import com.google.android.gms.cast.framework.media.RemoteMediaClient;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.List;

import static com.google.android.gms.cast.framework.CastContext.getSharedInstance;

public class playerM extends AppCompatActivity {

    public VideoView video;
    private String videoPath ="url";
    private static ProgressDialog progressDialog;
    public ImageButton botondeCast;
    String uri;
    private androidx.mediarouter.app.MediaRouteButton mMediaRouteButton;
    private MenuItem mediaRouteMenuItem;
    private CastContext mCastContext;
    private CastSession mCastSession;
    private SessionManager mSessionManager;
    private SessionManagerListener mSessionManagerListener = new SessionManagerListennerImpl();
    private MediaMetadata videoMetadata;
    private int withport,withland,heithport,heigthland;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_m);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        try {
            mCastContext = CastContext.getSharedInstance(this);
            mMediaRouteButton = findViewById(R.id.media_route_button);
            CastButtonFactory.setUpMediaRouteButton(getApplicationContext(), mMediaRouteButton);
            mSessionManager = mCastContext.getSessionManager();
        }catch (Exception e){
            Log error;

        }

        try{
            videoMetadata = new MediaMetadata(MediaMetadata.MEDIA_TYPE_MOVIE);
            videoMetadata.putString(MediaMetadata.KEY_TITLE, "Total Player");
            videoMetadata.putString(MediaMetadata.KEY_SUBTITLE, "");
        }catch (Exception e){
            Log error;
        }

        video = findViewById(R.id.videoView);

        withland = video.getWidth();
        video.post(new Runnable() {

            @Override
            public void run() {
               // video.setDimensions(getScreenWidth(), (video.getHeight()));
               // Log.e("","datos del video : "+getScreenWidth()+" "+getScreenHeight());
            }
        });

        Intent intent = getIntent();
        uri = intent.getStringExtra("url");
        mediaPlayer(uri,video);

    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    @Override
    protected void onResume() {
        super.onResume();
        try{
            mCastSession = mSessionManager.getCurrentCastSession();
            mSessionManager.addSessionManagerListener(mSessionManagerListener);
        }catch (Exception e){

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try{
            mSessionManager.removeSessionManagerListener(mSessionManagerListener);
            mCastSession = null;
        }catch (Exception e){

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
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

            //video.setDimensions(20, 20);
            //video.getHolder().setFixedSize(20, 20);

        } else {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN, WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

            //video.setDimensions(35, 35);
            //video.getHolder().setFixedSize(35, 35);

        }
    }


    public void goooglecast(String url){

    }

    //Subclase que permite manipular las etapas de la sesión
    private class SessionManagerListennerImpl implements SessionManagerListener {

        @Override
        public void onSessionStarting(Session session) {}

        @Override
        public void onSessionStarted(Session session, String s) {
            mCastSession = mSessionManager.getCurrentCastSession();
            loadMedia(mCastSession);
        }
        private void loadMedia(CastSession castSession) {
            MediaInfo mediaInfo = new MediaInfo.Builder(uri)
                    .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
                    .setContentType("videos/mp4")
                    .setMetadata(videoMetadata)
                    .build();
            RemoteMediaClient remoteMediaClient = castSession.getRemoteMediaClient();
            remoteMediaClient.load(mediaInfo);
        }
        @Override
        public void onSessionStartFailed(Session session, int i) {}

        @Override
        public void onSessionEnding(Session session) {}

        @Override
        public void onSessionEnded(Session session, int i) {}

        @Override
        public void onSessionResuming(Session session, String s) {}

        @Override
        public void onSessionResumed(Session session, boolean b) {}

        @Override
        public void onSessionResumeFailed(Session session, int i) {}

        @Override
        public void onSessionSuspended(Session session, int i) {}
    }


}
