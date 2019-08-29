package total.player;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.dash.manifest.DashManifestParser;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.Util;

public class ExoplayerPlayer extends AppCompatActivity {
    SimpleExoPlayer player;
    PlayerView playerView;
    PlayerControlView playerControlView;
    String uri;
    Player.EventListener eventListener;
    SimpleExoPlayer exoPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exoplayer_player);

        //---
        // Si la versión android es menor que Jellybean, usa este llamado para esconder la barra de estatus.
        if (Build.VERSION.SDK_INT > 21) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        //---
        Intent intent = getIntent();
        uri = intent.getStringExtra("url");

        //-----
        player = ExoPlayerFactory.newSimpleInstance(this);

        try {
            playerView = findViewById(R.id.player_view);

           // playerControlView = findViewById(R.id.player_control_view);
        }catch (Exception e){
            Log.e("Exoplayer","Exception vista : "+e);
        }

        // Add a listener to receive events from the player.

        //player.addListener(eventListener);
        preparePlayer(Uri.parse(uri));

        //Evitar que se apague la pantalla
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
    private void pausePlayer(){
        exoPlayer.setPlayWhenReady(false);
        exoPlayer.getPlaybackState();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        exoPlayer.stop(true);
    }
    @Override
    protected void onPause(){
        super.onPause();
        pausePlayer();
    }
    @Override
    public void onBackPressed() {
        finish();
    }
    /**
     *
     */
    private void initializePlayer() {
        // Instantiate the player.
        player = ExoPlayerFactory.newSimpleInstance(this);
        // Attach player to the view.
        playerControlView.setPlayer(player);
        // Prepare the player with the dash media source.
        player.prepare(createMediaSource());
        // Add a listener to receive events from the player.
        //player.addListener(eventListener);
    }

    /**
     *
     * @return
     */
    private MediaSource createMediaSource() {
        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, "TotalPlayer"));
        // This is the MediaSource representing the media to be played.
        MediaSource videoSource = new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(uri));
        // Prepare the player with the source.
        return  videoSource;
    }

    private void preparePlayer(Uri uri) {

        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        //TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveVideoTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector();
        LoadControl loadControl = new DefaultLoadControl();

        exoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);

        MediaSource mediaSource = getMediaSource(uri, bandwidthMeter);
        if (mediaSource != null) {
            exoPlayer.prepare(mediaSource);
            playerView.setPlayer(exoPlayer);
            exoPlayer.setPlayWhenReady(true);
        }
        else {

        }
    }

    private MediaSource getMediaSource(Uri uri, DefaultBandwidthMeter bandwidthMeter) {
        //String userAgent = Util.getUserAgent(this, );
        Handler mainHandler = new Handler();

        int retryCount = 111;
        int connectTimeout=111;
        int readTimeout = 122;
        DataSource.Factory dataSourceFactory = null;
        HttpDataSource.Factory httpDataSourceFactory=null;
        try {
            httpDataSourceFactory = new DefaultHttpDataSourceFactory("ExoAgent");
            dataSourceFactory = new DefaultDataSourceFactory(this, bandwidthMeter, httpDataSourceFactory);
        }catch (Exception e){
            Log.e("Exoplayer","Exception get media source : "+e);
        }
        MediaSource mediaSource;
        int type = Util.inferContentType(uri);
        switch (type) {
            case C.TYPE_DASH:
                //long livePresentationDelayMs = DashMediaSource.DEFAULT_LIVE_PRESENTATION_DELAY_PREFER_MANIFEST_MS;
                DefaultDashChunkSource.Factory dashChunkSourceFactory = new DefaultDashChunkSource.Factory(dataSourceFactory);
                // Last param is AdaptiveMediaSourceEventListener
                retryCount = 111;
                mediaSource = new DashMediaSource.Factory(dashChunkSourceFactory, dataSourceFactory).setManifestParser(new DashManifestParser()).createMediaSource(uri);
                //mediaSource = new DashMediaSource(uri, dataSourceFactory, dashChunkSourceFactory, retryCount, livePresentationDelayMs, mainHandler, null);
                break;
            case C.TYPE_HLS:
                // Last param is AdaptiveMediaSourceEventListener
                retryCount = 111;
                mediaSource = new HlsMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
                break;
            case C.TYPE_SS:
                //DefaultSsChunkSource.Factory ssChunkSourceFactory = new DefaultSsChunkSource.Factory(dataSourceFactory);
                // Last param is AdaptiveMediaSourceEventListener
                mediaSource = new SsMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
                //mediaSource = new SsMediaSource(uri, dataSourceFactory, ssChunkSourceFactory, mainHandler, null);
                break;
            default:
                ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
                mediaSource = new ExtractorMediaSource(uri, dataSourceFactory, extractorsFactory, mainHandler, null);
                break;
        }
            return mediaSource;
    }

}
