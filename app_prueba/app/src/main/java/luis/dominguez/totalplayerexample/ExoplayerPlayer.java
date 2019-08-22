package luis.dominguez.totalplayerexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.Util;

import java.io.IOException;

public class ExoplayerPlayer extends AppCompatActivity {
    SimpleExoPlayer player;
    PlayerView playerView;
    PlayerControlView playerControlView;
    String uri;
    Player.EventListener eventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exoplayer_player);
        //---
        Intent intent = getIntent();
        uri = intent.getStringExtra("url");

        //-----
        player = ExoPlayerFactory.newSimpleInstance(this);

        playerView = findViewById(R.id.player_view);
        playerControlView = findViewById(R.id.player_control_view);
        // Add a listener to receive events from the player.

        //player.addListener(eventListener);
        initializePlayer();


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
        player.addListener(eventListener);
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


}
