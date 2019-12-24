package alarmitron.buttonz;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    public static final String MESSAGE = "alarmitron.buttonz.mainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void next(View view){
        TextInputLayout til = findViewById(R.id.til);
        til.setErrorEnabled(false);
        Intent intent = new Intent(this, numberGenerator.class);
        EditText edit = (EditText) findViewById(R.id.user_value);
        String s = edit.getText().toString();
        int num = Integer.parseInt(s);

        if(num > 10 | num < 0){
            til.setErrorEnabled(true);
            til.setError("Your guess needs to be between 0-10. OPTIMUS!!!");
        }

        else{
            intent.putExtra(MESSAGE, num);
            startActivity(intent);
        }

    }




}
