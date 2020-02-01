package in.slwsolutions.moneymanager.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Transaction {
    @PrimaryKey
    public int id;

    public String contactLookupKey;

    public float amount;

    public boolean debit;

    public long timestamp;

}
