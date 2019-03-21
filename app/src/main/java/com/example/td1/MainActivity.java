package com.example.td1;

import android.os.PersistableBundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private Slider mSlider;
    private TextView affichage ;
    private float value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSlider = findViewById(R.id.slider);
        if(savedInstanceState!=null)value=savedInstanceState.getFloat("VALUE_SAVE");
        else value = mSlider.getValue();
        affichage=findViewById(R.id.affichage);
        affichage.setText("Value: " + Double.toString(value));
        mSlider.setSliderChangeListener(new Slider.SliderChangeListener() {
            @Override
            public void onChange() {
                value = mSlider.getValue();
                affichage.setText("Value: " + Double.toString(value));
            }
        });
    }

   /* @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        value=savedInstanceState.getFloat("VALUE_SAVE");
    }*/


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putFloat("VALUE_SAVE",value);
    }
}
