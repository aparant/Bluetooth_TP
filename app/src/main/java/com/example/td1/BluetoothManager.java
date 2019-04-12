package com.example.td1;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BluetoothManager extends AppCompatActivity {

    private BluetoothAdapter mBluetoothAdapter;
    private ArrayList<String> mAppTrouver;
    private ListView mListAppDiscover;
    private Button bScan;
    private ProgressBar circularProgressBar;
    private boolean bluetoothDisc;

    /**
     * Création de l'interface et affichage des appareils appairés
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_manager);
        ListView mListAppAppairer= findViewById(R.id.appApairer);
        circularProgressBar=findViewById(R.id.simpleProgressBar);
        mBluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        ArrayList<String> devices = new ArrayList<>();
        for (BluetoothDevice bt : pairedDevices) {
            devices.add(bt.getName() + "\n" + bt.getAddress());
        }
        ArrayAdapter arrayAdapter =
                new ArrayAdapter(this, android.R.layout.simple_list_item_1, devices);
        mListAppAppairer.setAdapter(arrayAdapter);

        mAppTrouver=new ArrayList<>();
        mListAppDiscover= findViewById(R.id.appDecouvert);
        bScan=findViewById(R.id.bScan);


        bluetoothDisc=false;


        bScan.setOnClickListener(new View.OnClickListener() {
            /**
             * Méthode appelée lorsque l'on clique sur le bouton
             * @param v
             */
            @Override
            public void onClick(View v) {
                if(bluetoothDisc==false){
                    mAppTrouver.clear();
                    IntentFilter filter=new IntentFilter(BluetoothDevice.ACTION_FOUND);
                    registerReceiver(mReceiver,filter);
                    mBluetoothAdapter.startDiscovery();
                    circularProgressBar.setVisibility(View.VISIBLE);
                    bluetoothDisc=true;
                }
                else{
                    mBluetoothAdapter.cancelDiscovery();
                    circularProgressBar.setVisibility(View.INVISIBLE);
                    unregisterReceiver(mReceiver);
                    bluetoothDisc=false;
                }
            }
        });

    }

    /**
     * Méthode appelée lors de la destruction de l'activité
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bluetoothDisc==true) unregisterReceiver(mReceiver);
    }

    private final BroadcastReceiver mReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                mAppTrouver.add(device.getName());
                ArrayAdapter arrayAdapter =
                        new ArrayAdapter(context, android.R.layout.simple_list_item_1, mAppTrouver);
                mListAppDiscover.setAdapter(arrayAdapter);
            }

        }
    };
}
