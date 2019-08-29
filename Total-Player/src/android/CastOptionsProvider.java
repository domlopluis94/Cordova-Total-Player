package total.player;

import com.google.android.gms.cast.framework.CastOptions;
import com.google.android.gms.cast.framework.OptionsProvider;
import com.google.android.gms.cast.framework.SessionProvider;

import android.content.Context;
import android.util.Log;

import java.util.List;


public class CastOptionsProvider implements OptionsProvider {

    @Override
    public CastOptions getCastOptions(Context context) {
        try {
            CastOptions castp = new CastOptions.Builder().setReceiverApplicationId(context.getString(R.string.app_id)).build();
            return castp;
        }catch (Exception e){
            Log.e("GetCastOptions","get cast options Error: "+ e);
        }
        return null;
    }

    @Override
    public List<SessionProvider> getAdditionalSessionProviders(Context context) {
        return null;
    }
}

