package alarmitron.experiements;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button moveScreen;  //don't know what this for
    Button location;   //toggle location on or off
    Button sharedAlarms;  //would turn toggle whether shared alarms was allowed for your preferences
    Button setting;  // would get you to settings
    Button volume;   //would control volume of alarm
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        moveScreen = (Button) findViewById(R.id.button);
        location = (Button) findViewById(R.id.button);
        sharedAlarms = (Button) findViewById(R.id.button);
        setting = (Button) findViewById(R.id.button);
        volume = (Button) findViewById(R.id.button);
        moveScreen.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent( MainActivity.this, NewScreen1.class);

                startActivity(intent);
            }
        });
    }
}
