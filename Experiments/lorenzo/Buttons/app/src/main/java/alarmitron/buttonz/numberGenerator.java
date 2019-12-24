package alarmitron.buttonz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.Random;

public class numberGenerator extends AppCompatActivity {

    private int userNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_generator);

        Intent numIntent = getIntent();
        userNum = numIntent.getIntExtra(MainActivity.MESSAGE, 5);


        Button button = findViewById(R.id.generate_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tView1 = findViewById(R.id.numberTextViewResult);
                TextView tView2 = findViewById(R.id.numberTextViewNumber);

                int r = randomNumberGenerator();
                if (userNum != r) {
                    String message = "oh no! better luck next time!";
                    tView1.setText(message);
                    String message2 = "Your guess : " + String.valueOf(userNum) + "\nActual number: " + r;
                    tView2.setText(message2);
                    Button b = findViewById(R.id.generate_button);
                    b.setVisibility(View.GONE);
                } else {
                    String message = "oh wow! you guessed correctly!";
                    tView1.setText(message);
                    String message2 = "Your guess : " + String.valueOf(userNum) + "\nActual number: " + r;
                    tView2.setText(message2);
                    Button b = findViewById(R.id.generate_button);
                    b.setVisibility(View.GONE);
                }

            }
        });




    }

    private int randomNumberGenerator(){
        Random r = new Random();
        int random = r.nextInt(11);
        return random;
    }
}
