package org.sunwatch.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;

import org.sunwatch.R;
import org.sunwatch.view.DayView;
import org.sunwatch.view.DayViewLegacy;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        populateCitySpinner();
        populateDaySpinner();
        populateModeSpinner();

        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.frame);
        frameLayout.addView(new DayViewLegacy(this));
//        frameLayout.addView(new DayView(this));
    }

    private void populateCitySpinner() {

        List<String> list = new ArrayList<String>();
        list.add("Istanbul");
        list.add("Ankara");
        list.add("Mekke");
        list.add("London");
        list.add("Boston");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner citySpinner = (Spinner) findViewById(R.id.city_spinner);
        citySpinner.setAdapter(dataAdapter);
    }

    private void populateDaySpinner() {

        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        List<String> list = new ArrayList<String>();

        for( int i=0; i<4; i++ ){
            c.add(Calendar.DATE, 1);  // number of days to add
            list.add( dateFormat.format( c.getTime() ) );
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner daySpinner = (Spinner) findViewById(R.id.day_spinner);
        daySpinner.setAdapter(dataAdapter);
    }

    private void populateModeSpinner() {

        List<String> list = new ArrayList<String>();
        list.add("Normal");
        list.add("Temkinli");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner modeSpinner = (Spinner) findViewById(R.id.mode_spinner);
        modeSpinner.setAdapter(dataAdapter);
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
