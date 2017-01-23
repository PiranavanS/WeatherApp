package com.satkunanathan.piranavan.weatherapp;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.text.DecimalFormat;
import java.util.Date;

import Data.CityPreference;
import Data.JSONWeatherParser;
import Data.WeatherHttpClient;
import Model.Weather;
import Util.Utils;

public class MainActivity extends AppCompatActivity {

    private TextView cityName;
    private TextView temp;
    private ImageView iconView;
    private TextView description;
    private TextView humididty;
    private TextView pressure;
    private TextView wind;
    private TextView sunrise;
    private TextView sunset;
    private TextView updated;

    Weather weather = new Weather();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        cityName = (TextView) findViewById(R.id.cityText);
        iconView = (ImageView) findViewById(R.id.thumbnailIcon);
        temp = (TextView) findViewById(R.id.tempText);
        description = (TextView) findViewById(R.id.cloudText);
        humididty = (TextView) findViewById(R.id.humidText);
        pressure = (TextView) findViewById(R.id.pressureText);
        wind = (TextView) findViewById(R.id.windText);
        sunrise = (TextView) findViewById(R.id.riseText);
        sunset = (TextView) findViewById(R.id.setText);
        updated = (TextView) findViewById(R.id.updateText);

        CityPreference cityPreference = new CityPreference(MainActivity.this);
        renderWeatherData(cityPreference.getCity());

    }

    public void renderWeatherData(String city){
        WeatherTask weatherTask = new WeatherTask();
        if (city == null || city.equals("")){
            city = "Toronto,CA";
        }
        weatherTask.execute(new String[]{city + "&units=metric"});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.change_city) {
            showInputDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    private void showInputDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Change City");
        final EditText cityInput = new EditText(MainActivity.this);
        cityInput.setInputType(InputType.TYPE_CLASS_TEXT);
        cityInput.setHint("Toronto,CA");
        builder.setView(cityInput);
        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                CityPreference cityPreference = new CityPreference(MainActivity.this);
                cityPreference.setCity(cityInput.getText().toString());
                String newCity = cityPreference.getCity();
                renderWeatherData(newCity);
            }
        });
        builder.show();
    }
    private class WeatherTask extends AsyncTask<String, Void, Weather>{
        @Override
        protected Weather doInBackground(String... params) {
            String data = ((new WeatherHttpClient()).getWeatherData(params[0]));

            weather = JSONWeatherParser.getWeather(data);

            return weather;
        }

        @Override
        protected void onPostExecute(Weather weather) {

            super.onPostExecute(weather);

            String sunriseString = (String) DateFormat.format("hh:mm:ss a", new Date(weather.place.getSunrise()));
            String sunsetString = (String) DateFormat.format("hh:mm:ss a", new Date(weather.place.getSunset()));
            String updateString = (String) DateFormat.format("hh:mm:ss a", new Date(weather.place.getLastupdate()));

            if(sunriseString == null || sunsetString == null || updateString == null){
                sunriseString = "";
                sunsetString = "";
                updateString = "";
            }

            DecimalFormat decimalFormat = new DecimalFormat("#.#");
            String tempFormat = decimalFormat.format(weather.currentCondition.getTemperature());

            cityName.setText(weather.place.getCity() + "," + weather.place.getCountry());
            temp.setText("" + tempFormat + "\u00b0" + "C");
            humididty.setText("Humidity: " + weather.currentCondition.getHumidity() + "%");
            pressure.setText(("Pressure: " + weather.currentCondition.getPressure() + " hPa"));
            wind.setText("Wind: " + weather.wind.getSpeed() + " mps");
            sunrise.setText("Sunrise: " + sunriseString);;
            sunset.setText("Sunset: "+ sunsetString);
            updated.setText("Last Updated: " + updateString);
            description.setText("Condition: " + weather.currentCondition.getCondition() + " ("
            + weather.currentCondition.getDescription() + ")");
            new DownloadImageTask().execute(weather.currentCondition.getIcon());

        }

    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap>{
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            iconView.setImageBitmap(bitmap);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            return downloadImage(params[0]);
        }


        private Bitmap downloadImage(String code){
            try {
                java.net.URL url = new java.net.URL(Utils.ICON_URL +code + ".png");
                HttpURLConnection connection = (HttpURLConnection) url
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

        }
    }

}
