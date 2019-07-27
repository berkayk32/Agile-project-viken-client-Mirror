package enal1586.ju.viken_passage;

import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.MODE_PRIVATE;

public class TestHelper{


    public boolean isValidMacAddress(String macaddress) {
        if(macaddress.equals("")){
            return false;
        }
        String MAC_PATTERN = "((?:[a-zA-Z0-9]{2}[:-]){5}[a-zA-Z0-9]{2})";

        Pattern pattern = Pattern.compile(MAC_PATTERN);
        Matcher matcher = pattern.matcher(macaddress);
        return matcher.matches();
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public String updateTimeLeft(LocalDateTime currentTime, LocalDateTime freePass) {
        long durationInSeconds = Duration.between(currentTime, freePass).getSeconds();
        String timeLeftString = String.format("%02d:%02d:%02d", TimeUnit.SECONDS.toHours(durationInSeconds),
                TimeUnit.SECONDS.toMinutes(durationInSeconds) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.SECONDS.toSeconds(durationInSeconds) % TimeUnit.MINUTES.toSeconds(1));

        if (TimeUnit.SECONDS.toHours(durationInSeconds) > 23) {
            return freePass.toString();
        }

        return timeLeftString;
    }
}
