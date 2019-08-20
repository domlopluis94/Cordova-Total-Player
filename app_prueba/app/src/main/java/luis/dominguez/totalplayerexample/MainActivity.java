package luis.dominguez.totalplayerexample;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import org.json.JSONException;

import java.net.URI;

public class MainActivity extends AppCompatActivity {


    EditText texturl;
    Button baceptar, vlcpl, exob;
    Intent i;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        texturl = findViewById(R.id.editText);



        baceptar= findViewById(R.id.button2);
        baceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i= new Intent(MainActivity.this, playerM.class);
                i.putExtra("url", texturl.getText().toString());
                startActivity(i);
            }
        });

        vlcpl= findViewById(R.id.button3);
        vlcpl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playvlc(texturl.getText().toString());
            }
        });

        exob= findViewById(R.id.button4);
        exob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }


    //---------- VLC Player ---------

    /**
     *
     * @param url
     * @return
     */
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
