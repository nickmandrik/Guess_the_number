package mandrik.nick.guessthenumber;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.util.Random;

public class GuessActivity extends AppCompatActivity {

    public static final String APP_PREFERENCES = "settings_counts";
    public static final String APP_PREFERENCES_GUESSES = "count_guesses";
    public static final String APP_PREFERENCES_CLICKS = "count_clicks";

    TextView infoView;
    EditText editNumber;
    Button checkControl;
    TextView countInfo;

    // for this game
    Integer guessNumber;
    boolean isFinished;
    Integer countProb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess);

        infoView = (TextView)findViewById(R.id.infoView);
        editNumber = (EditText)findViewById(R.id.editText);
        checkControl = (Button)findViewById(R.id.button);
        countInfo = (TextView)findViewById(R.id.infoAttemps);

        // for this game
        Random rand = new Random();
        guessNumber = rand.nextInt(100);
        isFinished = false;
        countProb = 0;
        countInfo.setText("");

    }

    public void onClick(View v) {
        if (!isFinished){
            int inp = 100;
            try {
                inp = Integer.parseInt(editNumber.getText().toString());
            }
            catch(NumberFormatException ex) {
                infoView.setText(getResources().getString(R.string.error));
            }
            if(inp > 100 || inp < 0)
                infoView.setText(getResources().getString(R.string.error_input));
            else {
                if (inp > guessNumber)
                    infoView.setText(getResources().getString(R.string.ahead));
                if (inp < guessNumber)
                    infoView.setText(getResources().getString(R.string.behind));
                if (inp == guessNumber) {
                    MediaPlayer mPlayer = MediaPlayer.create(this, R.raw.win3);
                    mPlayer.start();
                    infoView.setText(getResources().getString(R.string.hit));
                    checkControl.setText(getResources().getString(R.string.play_more));
                    isFinished = true;

                    SharedPreferences countSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
                    SharedPreferences.Editor e = countSettings.edit();
                    e.putBoolean("hasVisited", true);
                    e.putInt(APP_PREFERENCES_GUESSES, countSettings.getInt(APP_PREFERENCES_GUESSES, 0) + 1);
                    e.putInt(APP_PREFERENCES_CLICKS, countSettings.getInt(APP_PREFERENCES_CLICKS, 0) + countProb + 1);
                    e.commit();
                    e.apply();

                    Intent intent = new Intent(this, StartActivity.class);
                    startActivity(intent);
                }
            }
            countProb++;
            countInfo.setText(getResources().getString(R.string.print_count) + " " + countProb);
        }
        else
        {
            countProb = 0;
            countInfo.setText("");
            Random rand = new Random();
            guessNumber = rand.nextInt(100);
            checkControl.setText(getResources().getString(R.string.input_value));
            infoView.setText(getResources().getString(R.string.try_to_guess));
            isFinished = false;
        }
        editNumber.setText("");
    }

    public void back(View v){
        switch (v.getId()) {
            case R.id.backButton:

                SharedPreferences countSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor e = countSettings.edit();
                e.putBoolean("hasVisited", true);
                e.putInt(APP_PREFERENCES_CLICKS, countSettings.getInt(APP_PREFERENCES_CLICKS, 0) + countProb );
                e.commit();
                e.apply();

                Intent intent = new Intent(this, StartActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
