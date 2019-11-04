package com.taufani.kiwari.utilities;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by dtaufani@gmail.com on 04/11/19.
 */

public class DateParser {
    public static String formatDate(String outputFormat, Date inputDate) {
        SimpleDateFormat dateFormatOutput = new SimpleDateFormat(outputFormat);
        return dateFormatOutput.format(inputDate);
    }
}
