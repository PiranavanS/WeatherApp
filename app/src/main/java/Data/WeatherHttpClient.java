package Data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import Util.Utils;

/**
 * Created by piranavan on 2017-01-18.
 */

public class WeatherHttpClient {

    public String getWeatherData(String place){
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        try {
            String urlLink = (Utils.BASE_URL + place + Utils.API_KEY).replace(" ","");
            connection = (HttpURLConnection) (new URL(urlLink)).openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();

            //read response
            StringBuffer stringBuffer = new StringBuffer();
            inputStream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;

            while ((line = bufferedReader.readLine()) != null){
                stringBuffer.append(line + "\r\n");
            }

            inputStream.close();
            connection.disconnect();
            return stringBuffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
