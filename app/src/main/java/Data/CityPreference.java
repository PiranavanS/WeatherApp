package Data;

import android.app.Activity;
import android.content.SharedPreferences;

/**
 * Created by piranavan on 2017-01-19.
 */

public class CityPreference {
    SharedPreferences prefs;

    public CityPreference(Activity activity){
        prefs = activity.getPreferences(Activity.MODE_PRIVATE);
    }

    public String getCity(){
        return prefs.getString("city", "Toronto,CA");
    }

    public void setCity(String city){
        prefs.edit().putString("city", city).commit();
    }
}
