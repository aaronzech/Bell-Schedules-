package schoolschedules_2019.Middle.mgms;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
//import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

//import com.osseo.zechaaron.schoolschedules_2019.R;
import schoolschedules_2019.MainActivity;
import schoolschedules_2019.R;

public class mgmsSelect extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mgms_select);

        // Spinner Logic
        Spinner mySpinner = (Spinner) findViewById(R.id.spinnerTeam);

        ArrayAdapter<String> myAdapter = new ArrayAdapter<>(mgmsSelect.this,
                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.mgmsTeams));

        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mySpinner.setAdapter(myAdapter);

        mySpinner.setOnItemSelectedListener(this);

        // Displays Version Number of the App
        versionNumber(MainActivity.versionNumber);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String s = parent.getItemAtPosition(position).toString();

        if(s.equals("6A, 6B"))
        {
            startActivity(new Intent(mgmsSelect.this, mgms_6A6B.class));
        }
        if(s.equals("6C, 6D"))
        {
            startActivity(new Intent(mgmsSelect.this, mgms_6C6D.class));
        }
        if(s.equals("7A, 7B"))
        {

            startActivity(new Intent(mgmsSelect.this, mgms_7A7B.class));
        }
        if(s.equals("7C, 7D"))
        {
            startActivity(new Intent(mgmsSelect.this, mgms_7C7D.class));
        }
        if(s.equals("8A, 8B"))
        {
            startActivity(new Intent(mgmsSelect.this, mgms_8A8B.class));
        }
        if(s.equals("8C, 8D"))
        {
            // 8CD class
            startActivity(new Intent(mgmsSelect.this, mgms_8C8D.class));
        }


    }

    public void onNothingSelected(AdapterView<?> parent)
    {

    }

    // Go back button - Takes user to main app screen
    public void goHome(View view)
    {
        startActivity(new Intent(mgmsSelect.this,MainActivity.class));
    }

    private void versionNumber(String number)
    {
        TextView versionNumber = (TextView) findViewById(R.id.version_text_view);
        versionNumber.setText("Version: "+number);

    }

}
