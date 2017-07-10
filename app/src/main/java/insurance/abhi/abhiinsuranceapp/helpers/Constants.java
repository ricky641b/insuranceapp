package insurance.abhi.abhiinsuranceapp.helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import insurance.abhi.abhiinsuranceapp.models.Post;

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
    public static String getDateFormatString(Date date) {

        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd MMM, yyyy", Locale.getDefault());


        return dateFormat.format(date);
    }
    public static Date getOnlyDate(String date) {

        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd MMM, yyyy", Locale.getDefault());
        try {
            return dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}
