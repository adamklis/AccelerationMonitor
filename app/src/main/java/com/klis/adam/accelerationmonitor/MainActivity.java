package com.klis.adam.accelerationmonitor;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.CatmullRomInterpolator;
import com.androidplot.xy.FastLineAndPointRenderer;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYGraphWidget;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeriesFormatter;

import java.sql.Timestamp;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Dictionary;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    XYPlot xyPlot;
    SimpleXYSeries seriesX;
    SimpleXYSeries seriesY;
    SimpleXYSeries seriesZ;
    SimpleXYSeries seriesSUM;

    Number[]seriesXVal;
    Number[]seriesYVal;
    Number[]seriesZVal;
    Number[]seriesSUMVal;


    TextView textView;
    SensorManager sensorManager;
    Sensor sensor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        seriesXVal=new Number[30];
        seriesYVal=new Number[30];
        seriesZVal=new Number[30];
        seriesSUMVal=new Number[30];

        for (int i=0; i<seriesXVal.length; i++){
            seriesXVal[i]=0;
            seriesYVal[i]=0;
            seriesZVal[i]=0;
            seriesSUMVal[i]=0;
        }

        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        sensor=null;

        if ((sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)) != null) {

        }
        else{
            Toast.makeText(getApplicationContext(),"Brak odpowiedniego sensora",Toast.LENGTH_LONG);
        }


        xyPlot = (XYPlot)findViewById(R.id.plot);
        seriesX = new SimpleXYSeries(Arrays.asList(seriesXVal), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "X");
        seriesY = new SimpleXYSeries(Arrays.asList(seriesYVal), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Y");
        seriesZ = new SimpleXYSeries(Arrays.asList(seriesZVal), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Z");
        seriesSUM = new SimpleXYSeries(Arrays.asList(seriesSUMVal), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "SUM");

        //seriesXFormat.setInterpolationParams(new CatmullRomInterpolator.Params(10, CatmullRomInterpolator.Type.Centripetal));

        xyPlot.addSeries(seriesX, new FastLineAndPointRenderer.Formatter(Color.RED, null, null));
        xyPlot.addSeries(seriesY, new FastLineAndPointRenderer.Formatter(Color.GREEN, null, null));
        xyPlot.addSeries(seriesZ, new FastLineAndPointRenderer.Formatter(Color.BLUE, null, null));
        xyPlot.addSeries(seriesSUM, new FastLineAndPointRenderer.Formatter(Color.LTGRAY, null, null));

        xyPlot.setRangeBoundaries(-15,15, BoundaryMode.FIXED);

        textView = findViewById(R.id.textView);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        textView.setText(sensorEvent.values[0] + " " + sensorEvent.values[1] + " " + sensorEvent.values[2] + " ");
        for (int i = 0; i < seriesXVal.length - 1; i++) {

            seriesX.setY(seriesX.getY(i + 1), i);
            seriesY.setY(seriesY.getY(i + 1), i);
            seriesZ.setY(seriesZ.getY(i + 1), i);
            seriesSUM.setY(seriesSUM.getY(i + 1), i);
        }
        seriesX.setY(sensorEvent.values[0], 29);
        seriesY.setY(sensorEvent.values[1], 29);
        seriesZ.setY(sensorEvent.values[2], 29);
        seriesSUM.setY(Math.abs(sensorEvent.values[0])+Math.abs(sensorEvent.values[1])+Math.abs(sensorEvent.values[2]), 29);
        xyPlot.redraw();

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this,sensor,SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

}
