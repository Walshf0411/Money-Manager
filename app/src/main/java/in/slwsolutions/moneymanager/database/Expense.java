package in.slwsolutions.moneymanager.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class Expense {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public double amount;

    public String notes;

    public Date timestamp;

    public Expense(double amount, String notes, Date timestamp) {
        this.amount = amount;
        this.notes = notes;
        this.timestamp = timestamp;
    }
}
