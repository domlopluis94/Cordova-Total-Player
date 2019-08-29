package total.player;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.fragment.app.FragmentActivity;
import androidx.mediarouter.app.MediaRouteButton;
import androidx.mediarouter.media.MediaRouteSelector;

import com.google.android.gms.cast.CastMediaControlIntent;
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

import java.util.List;

public class playerM extends FragmentActivity {

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
            mSessionManager = mCastContext.getSessionManager();

            MediaRouteSelector mMediaRouteSelector = new MediaRouteSelector.Builder().addControlCategory(CastMediaControlIntent.categoryForCast(getString(R.string.app_id))).build();
            mMediaRouteButton =(MediaRouteButton) findViewById(R.id.media_route_button);
            mMediaRouteButton.setRouteSelector(mMediaRouteSelector);
            CastButtonFactory.setUpMediaRouteButton(this, mMediaRouteButton);
            Log.e("","Exception CastButtonFactory. : " + this + mMediaRouteButton);

        }catch (Exception e){

            Log.e("","Exception cast : "+e);
        }

        try{
            videoMetadata = new MediaMetadata(MediaMetadata.MEDIA_TYPE_TV_SHOW);
            videoMetadata.putString(MediaMetadata.KEY_TITLE, "Total Player");
            videoMetadata.putString(MediaMetadata.KEY_SUBTITLE, "");
        }catch (Exception e){
            Log.e("","Exception videomNetadata : "+e);
        }

        video = findViewById(R.id.videoView);

        withland = video.getWidth();
        video.post(new Runnable() {

            @Override
            public void run() {
                CastButtonFactory.setUpMediaRouteButton(getApplicationContext(), mMediaRouteButton);
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
            mMediaRouteButton.setVisibility(View.VISIBLE);
        }catch (Exception e){
            Log.e("","Exception resume : "+e);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try{
            mSessionManager.removeSessionManagerListener(mSessionManagerListener);
            mCastSession = null;
        }catch (Exception e){
            Log.e("","Exception cast : "+e);
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
            MediaInfo mediaInfo = new MediaInfo.Builder(uri).setStreamType(MediaInfo.STREAM_TYPE_BUFFERED).setContentType("videos/hls").setMetadata(videoMetadata).build();
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

    public class CastOptionsProvider implements OptionsProvider {

        @Override
        public CastOptions getCastOptions(Context context) {
            Log.e("","get cast options : "+ context.getString(R.string.app_id));
            return new CastOptions.Builder().setReceiverApplicationId(context.getString(R.string.app_id)).build();

        }

        @Override
        public List<SessionProvider> getAdditionalSessionProviders(Context context) {
            return null;
        }
    }
}
