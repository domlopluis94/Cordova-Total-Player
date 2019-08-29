package total.player;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
/**
 * This class echoes a string called from JavaScript.
 */
public class TotalPlayer extends CordovaPlugin {

    Intent i;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("playVlc")) {
            String message = args.getString(0);
            this.playVlc(message, callbackContext);
            return true;
        }else if (action.equals("exoPlayer")) {
            String message = args.getString(0);
            this.exoPlayer(message, callbackContext);
            return true;   
        }else if (action.equals("mediaPlayer")) {
            String message = args.getString(0);
            this.mediaPlayer(message, callbackContext);
            return true;   
        }
        return false;
    }

    private void playVlc(String message, CallbackContext callbackContext) {
        if (message != null && message.length() > 0) {
            try{
                playvlc(message);
                callbackContext.success(message);
            }catch(Exception e){
                callbackContext.error("Error en el Reproductor.");
            }
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }

    private void exoPlayer(String message, CallbackContext callbackContext) {
        if (message != null && message.length() > 0) {
            try{
                cordova.getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        i= new Intent(cordova.getActivity(), ExoplayerPlayer.class);
                        i.putExtra("url", message);
                        CordovaPlugin plug = new CordovaPlugin();
                        cordova.startActivityForResult(plug,i,42);
                        callbackContext.success(); // Thread-safe.
                    }
                });
            }catch(Exception e){
                callbackContext.error("Error en el Reproductor.");
            }
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }

    private void mediaPlayer(String message, CallbackContext callbackContext) {
        if (message != null && message.length() > 0) {
            try{
                cordova.getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        i= new Intent(cordova.getActivity(), playerM.class);
                        i.putExtra("url", message);
                        CordovaPlugin plug = new CordovaPlugin();
                        cordova.startActivityForResult(plug,i,42);
                        callbackContext.success(); // Thread-safe.
                    }
                });
            }catch(Exception e){
                callbackContext.error("Error en el Reproductor.");
            }
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }


    /**
     *
     * @param url
     * @return
     */
    private boolean playvlc(String url) {
        return playv(url);
    }

    private boolean playv(final String url) {
        cordova.getActivity().runOnUiThread(new Runnable() {
            public void run() {
                int vlcRequestCode = 42; //request code used when finished playing, not necessary if you only use startActivity(vlcIntent)
                Intent vlcIntent = new Intent(Intent.ACTION_VIEW);
                vlcIntent.setPackage("org.videolan.vlc");
                vlcIntent.setDataAndTypeAndNormalize(Uri.parse(url), "video/*");
                vlcIntent.putExtra("title", "ÑÑTV VLC");
                CordovaPlugin plug = new CordovaPlugin();
                cordova.startActivityForResult(plug,i,42);
            }
        });
        return true;
    }
}
