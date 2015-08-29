package org.sunwatch.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by gungor on 26/08/15.
 */
public class Utility {

    public static Date getDate(String dateString, String format){
        try {
            SimpleDateFormat df = new SimpleDateFormat(format);
            return df.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

}
