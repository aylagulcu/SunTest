package org.sunwatch.activity;

import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.Spinner;

import org.sunwatch.R;
import org.sunwatch.model.Enums;
import org.sunwatch.model.UIValueHolder;
import org.sunwatch.util.Utility;
import org.sunwatch.view.DayViewLegacy;
import org.sunwatch.view.SunView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<String> locationList = new ArrayList<String>();
    List<String> dateList = new ArrayList<String>();
    List<String> modeList = new ArrayList<String>();
    SunView sunView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationList.clear();
        dateList.clear();
        modeList.clear();

        populateCitySpinner();
        populateDaySpinner();
        populateModeSpinner();

        UIValueHolder.model.setDate(Utility.getDate(dateList.get(0), "dd.MM.yyyy"));
        UIValueHolder.model.setLocation(Enums.Location.values()[0]);
        UIValueHolder.model.setMethod(Enums.Method.values()[0]);

        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.frame);
        frameLayout.addView(new DayViewLegacy(this));
        sunView = new SunView(this);


        frameLayout.addView(sunView);


        SeekBar seekBar = (SeekBar) findViewById(R.id.daySeekBar);
        seekBar.setProgress(50);
        seekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    int progress = 0;

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                        progress = progresValue;
//                        System.out.println( "progress : "+ (progress) );
                        sunView.redraw(progress);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        // Do something here,
                        //if you want to do anything at the start of
                        // touching the seekbar
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        // Display the value in textview

                    }
                });


        final Spinner dateSpinner = (Spinner) findViewById(R.id.day_spinner);
        dateSpinner.setOnItemSelectedListener(
                new Spinner.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String selectedDate =  dateSpinner.getSelectedItem().toString();
                        UIValueHolder.model.setDate( Utility.getDate( selectedDate, "dd.MM.yyyy" ) );
                        sunView.redraw();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                }
        );
    }

    private void populateCitySpinner() {

        for(int i=0; i<Enums.Location.values().length; i++){
            locationList.add( Enums.Location.values()[i].name() );
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, locationList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner citySpinner = (Spinner) findViewById(R.id.city_spinner);
        citySpinner.setAdapter(dataAdapter);
    }

    private void populateDaySpinner() {

        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());


        for( int i=0; i<4; i++ ){
            c.add(Calendar.DATE, 1);  // number of days to add
            dateList.add( dateFormat.format(c.getTime()) );
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, dateList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner daySpinner = (Spinner) findViewById(R.id.day_spinner);
        daySpinner.setAdapter(dataAdapter);
    }

    private void populateModeSpinner() {


        modeList.add("Normal");
        modeList.add("Temkinli");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, modeList);
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
