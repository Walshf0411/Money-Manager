package in.slwsolutions.moneymanager.database;

import androidx.room.TypeConverter;

import java.util.Date;

public class DateConverter {

    @TypeConverter
    public static Date toTimestamp(Long dateLong){
        return dateLong == null ? null: new Date(dateLong);
    }

    @TypeConverter
    public static Long fromTimestamp(Date date){
        return date == null ? null : date.getTime();
    }

}