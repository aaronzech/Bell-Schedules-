package schoolschedules_2019;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
//import android.support.v4.app.ActivityCompat;
import androidx.core.app.ActivityCompat;

//import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import schoolschedules_2019.High.MGSH;
import schoolschedules_2019.High.osh;
import schoolschedules_2019.High.pcsh;
import schoolschedules_2019.Middle.NVMS;
import schoolschedules_2019.Middle.bms;
import schoolschedules_2019.Middle.mgms.mgmsSelect;
import schoolschedules_2019.Middle.oms.omsSelect;

//import schoolschedules_2019.R;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Button btnGetLoc;
    ImageButton settingBtn;

    private static boolean firstRun=true;
    public static boolean showOnlyActiveClass=false;

    public static String versionNumber="0.7.7.3";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {


        SharedPreferences spf = PreferenceManager.getDefaultSharedPreferences(this);

        //YOUR FIRST RUN CODE HERE
        if(firstRun)
        {

            String storeSchool = spf.getString(getString(R.string.key_Schools),"zzz");
            loadPreference(storeSchool);
        }

        firstRun = false;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        versionNumber(versionNumber);

      //  getLocalIpAddress();


      // Spinner Logic
      Spinner mySpinner = (Spinner) findViewById(R.id.spinnerSchool);

      ArrayAdapter<String> myAdapter = new ArrayAdapter<>(MainActivity.this,
              android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.Schools));

      myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


      mySpinner.setAdapter(myAdapter);

      //mySpinner.setSelection(0);

      mySpinner.setOnItemSelectedListener(this);



      // Settings
        settingBtn = (ImageButton) findViewById(R.id.settingsBTN);

        settingBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                startActivity(new Intent(MainActivity.this,Settings.class));

            }

        });



      // Location
       btnGetLoc = (Button) findViewById(R.id.getLoc);
        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},123);





        btnGetLoc.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v)
           {
               // Refresh Peferences
               upDatePreferences();

               GPStracker g = new GPStracker(getApplicationContext());




               Location l = g.getLocation();
               if(l!=null)
               {
                   double lat = l.getLatitude();
                   double lon = l.getLongitude();
                   //Log.i("LOCATATION","LAT:"+lat+"\nLON:"+lon);

                   // MGSH
                   //45.137   -93.465
                   //45.130   -93.457
                   if(lat>=45.130  && lat<=45.137 && lon<=-93.457 && lon>=-93.465)
                   {
                       startActivity(new Intent(MainActivity.this,MGSH.class));
                   }
                   // OSH
                   //45.118   -93.405
                   //45.122   -93.409
                   else if(lat>=45.117  && lat<=45.122 && lon<= -93.403 && lon>= -93.409)
                   {
                       Toast.makeText(getApplicationContext(),"OSH", Toast.LENGTH_LONG).show();
                       startActivity(new Intent(MainActivity.this,osh.class));
                   }
                   // PCSH
                   //45.090367  -93.343099
                   //45.087887   -93.345160
                   else if(lat>=45.087887  && lat<=45.090367 && lon<=-93.343099 && lon>=-93.345160)
                   {
                       Toast.makeText(getApplicationContext(),"PCSH", Toast.LENGTH_LONG).show();
                       startActivity(new Intent(MainActivity.this,pcsh.class));
                   }
                   // Brooklyn Middle
                   //45.090367  -93.342874
                   //45.087887   -93.341321
                   else if(lat>=45.087887  && lat<=45.090367 && lon<=-93.341321 && lon>=-93.342874)
                   {
                       Toast.makeText(getApplicationContext(),"BMS", Toast.LENGTH_LONG).show();
                       startActivity(new Intent(MainActivity.this,bms.class));
                   }
                   // Osseo Middle
                   //45.123349  -93.409583
                   //45.121804  -93.413510
                   else if(lat>=45.121804 && lat<=45.123349 && lon<=-93.409583 && lon>=-93.413510)
                   {
                       Toast.makeText(getApplicationContext(),"OMS", Toast.LENGTH_LONG).show();
                       startActivity(new Intent(MainActivity.this,omsSelect.class));
                   }
                   // NVMS
                   //45.079979 -93.355690
                   //45.077950  -93.35249
                   else if(lat>=45.077950 && lat<=45.079979 && lon<=-93.35249 && lon>=-93.355690)
                   {
                       Toast.makeText(getApplicationContext(),"NVMS", Toast.LENGTH_LONG).show();
                       startActivity(new Intent(MainActivity.this,NVMS.class));
                   }
                   // MGMS
                   //45.081709 -93.430931
                   //45.081717 -93.425070
                   else if(lat>=45.080225 && lat<=45.08366 && lon<=-93.425070 && lon>=-93.430931)
                   {
                       Toast.makeText(getApplicationContext(),"MGMS", Toast.LENGTH_LONG).show();
                       startActivity(new Intent(MainActivity.this,mgmsSelect.class));
                   }
                   else
                   {
                       Toast.makeText(getApplicationContext(),"Not Near a School", Toast.LENGTH_LONG).show();
                   }
               }

               else {
                        // NO location service message
                        Toast.makeText(getApplicationContext(),"NO LOCATION SERVICE\nFEATURE NOT AVAILABLE ON CHROMEBOOKS", Toast.LENGTH_LONG).show();

                        // Hides button to prevent future presses
                        btnGetLoc.setVisibility(v.INVISIBLE);
               }
           }
       });




    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {

        // Refresh Peferences
        upDatePreferences();

        String s = parent.getItemAtPosition(position).toString();
        //Toast.makeText(this,s,Toast.LENGTH_LONG).show();
        if(s.equals("Maple Grove Senior"))
        {
           startActivity(new Intent(MainActivity.this,MGSH.class));
        }
        if(s.equals("Osseo Senior"))
        {
            startActivity(new Intent(MainActivity.this,osh.class));
        }
        if(s.equals("Brooklyn Middle"))
        {
            startActivity(new Intent (MainActivity.this,bms.class));
        }
        if(s.equals("Osseo Middle"))
        {
            startActivity(new Intent (MainActivity.this,omsSelect.class));
        }

        if(s.equals("Park Center Senior"))
        {
           // Toast.makeText(this,s,Toast.LENGTH_LONG).show();
            startActivity(new Intent (MainActivity.this,pcsh.class));
        }
        if(s.equals("North View Middle"))
        {
            startActivity(new Intent (MainActivity.this,NVMS.class));
        }
        if(s.equals("Maple Grove Middle"))
        {
            startActivity(new Intent (MainActivity.this,mgmsSelect.class));
        }

    }

    public void onNothingSelected(AdapterView<?> parent)
    {

    }

    private void versionNumber(String number)
    {
        TextView versionNumber = (TextView) findViewById(R.id.version_text_view);
        versionNumber.setText("Version: "+number);

    }
    public void loadPreference(String pref)
    {
        if(pref.equals("Maple Grove Senior"))
        {
            startActivity(new Intent(MainActivity.this,MGSH.class));
        }
        if(pref.equals("Osseo Senior"))
        {
            startActivity(new Intent(MainActivity.this,osh.class));
        }
        if(pref.equals("Brooklyn Middle"))
        {
            startActivity(new Intent (MainActivity.this,bms.class));
        }
        if(pref.equals("Osseo Middle"))
        {
            startActivity(new Intent (MainActivity.this,omsSelect.class));
        }

        if(pref.equals("Park Center Senior"))
        {
            // Toast.makeText(this,pref,Toast.LENGTH_LONG).show();
            startActivity(new Intent (MainActivity.this,pcsh.class));
        }
        if(pref.equals("North View Middle"))
        {
            startActivity(new Intent (MainActivity.this,NVMS.class));
        }
        if(pref.equals("Maple Grove Middle"))
        {
            startActivity(new Intent (MainActivity.this,mgmsSelect.class));
        }
    }

    private void upDatePreferences()
    {
        // Refresh perfence setting
        SharedPreferences spf = PreferenceManager.getDefaultSharedPreferences(this);
        showOnlyActiveClass = spf.getBoolean(getString(R.string.switch1),false);
    }
}

