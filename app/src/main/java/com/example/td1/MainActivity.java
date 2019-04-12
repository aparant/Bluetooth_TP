package com.example.td1;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private final static int REQUEST_ENABLE_BT = 1;

    private Slider mSlider;
    private TextView affichage ;
    private float value;
    BluetoothAdapter mBluetoothAdapter;

    /**
     * Création de l'interface et du Slider
     * @param savedInstanceState état sauvegardé lors d'une rotation
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSlider = findViewById(R.id.slider);
        mBluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
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


    /**
     * Renvoie la valeur du curseur lorsqu'on relance l'activité
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putFloat("VALUE_SAVE",value);
    }

    /**
     * Permet de créer le menu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    /**
     * Action à effectuer selon l'item cliqué sur le menu
     * @param item item sélectionné par l'utilisateur
     * @return true si l'action s'est bien déroulée
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.connexion) {
            if(mBluetoothAdapter==null) {
                Toast.makeText(MainActivity.this,"Bluetooth non supporté par cet appareil!",
                        Toast.LENGTH_LONG).show();
            }
            if(!mBluetoothAdapter.isEnabled()){
                Intent enableBTIntent=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBTIntent,REQUEST_ENABLE_BT);
            }
            if(mBluetoothAdapter.isEnabled()){
                mBluetoothAdapter.disable();
                Toast.makeText(MainActivity.this,"Désactivation du Bluetooth en cours.",
                        Toast.LENGTH_LONG).show();
            }
            }
        if(item.getItemId()==R.id.addDevice){
            if(mBluetoothAdapter.isEnabled()) {
                Intent mBluetoothManager = new Intent(MainActivity.this, com.example.td1.BluetoothManager.class);
                startActivity(mBluetoothManager);
            }
            else{
                Toast.makeText(MainActivity.this,"Veuillez activer le Bluetooth !",
                        Toast.LENGTH_LONG).show();
            }
        }
        return true;
        }

}
