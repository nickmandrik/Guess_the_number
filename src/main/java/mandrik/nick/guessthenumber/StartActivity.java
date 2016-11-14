package mandrik.nick.guessthenumber;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StartActivity extends AppCompatActivity{

    public static final String APP_PREFERENCES = "settings_counts";
    public static final String APP_PREFERENCES_GUESSES = "count_guesses";
    public static final String APP_PREFERENCES_CLICKS = "count_clicks";

    Button startButton;
    TextView infoText;
    Button clearButton;

    SharedPreferences countSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        startButton = (Button)findViewById(R.id.startButton);
        infoText = (TextView)findViewById(R.id.infoStartActivity);
        clearButton = (Button)findViewById(R.id.clearButton);

        countSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        boolean hasVisited = countSettings.getBoolean("hasVisited", false);

        if (!hasVisited) {
            SharedPreferences.Editor e = countSettings.edit();
            e.putBoolean("hasVisited", true);
            e.putInt(APP_PREFERENCES_GUESSES, 0);
            e.putInt(APP_PREFERENCES_CLICKS, 0);
            e.commit();
            e.apply();
            clearButton.setVisibility(View.INVISIBLE);
        }
        else {
            Integer countGuesses = countSettings.getInt(APP_PREFERENCES_GUESSES, 0);
            if(countGuesses != 0) {
                infoText.setText(getResources().getString(R.string.right_guess) + " " + countGuesses.toString() + "\n");
                startButton.setText(getResources().getString(R.string.play_more));
            }
            else {
                infoText.setText("");
            }

            Integer countClicks = countSettings.getInt(APP_PREFERENCES_CLICKS, 0);
            // if countGuesses != 0 => countClick != 0
            if(countClicks != 0) {
                infoText.setText(infoText.getText() + getResources().getString(R.string.print_count)
                        + " " +countClicks.toString());
            }
            else {
                infoText.setText("");
                clearButton.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void onClick(View v) {
        Intent intent = new Intent(this, GuessActivity.class);
        startActivity(intent);
    }

    public void clear(View v) {
        SharedPreferences.Editor e = countSettings.edit();
        e.clear();
        e.commit();
        infoText.setText("");
        startButton.setText(getResources().getString(R.string.start));
        clearButton.setVisibility(View.INVISIBLE);
    }

}
