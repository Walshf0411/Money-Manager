package in.slwsolutions.moneymanager.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Transaction {
    @PrimaryKey
    public int id;

    public String contactLookupKey;

    public double amount;

    public boolean debit;

    public long timestamp;

    public Transaction(String contactLookupKey, double amount, boolean debit) {
        this.contactLookupKey = contactLookupKey;
        this.amount = amount;
        this.debit = debit;
    }
}
