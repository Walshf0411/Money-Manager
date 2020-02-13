package in.slwsolutions.moneymanager.database;

import androidx.room.TypeConverter;

import java.util.Date;

public class DateConverter {

    /*
    * this method is called whenever room has to save an date in the database
    * */
    @TypeConverter
    public static Long toTimestamp(Date date){
        return date == null ? null: date.getTime();
    }

    /*
     * This method is used to get convert the long value returned from the database
     * to a date object(Used when data is being retrieved from the database)
     */
    @TypeConverter
    public static Date fromTimestamp(Long dateLong){
        return dateLong == null ? null : new Date(dateLong);
    }

}