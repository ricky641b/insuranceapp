package insurance.abhi.abhiinsuranceapp.helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by rick on 07-07-2017.
 */

public class Constants {

    public static String getDateTime() {

        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        Date date = new Date();
        return dateFormat.format(date);
    }
    public static Date getDate(String dateTime) {

        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        try {
            return dateFormat.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}
