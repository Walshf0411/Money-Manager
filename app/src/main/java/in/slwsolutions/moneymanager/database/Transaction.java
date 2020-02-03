package in.slwsolutions.moneymanager.database;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class Transaction {
    @PrimaryKey(autoGenerate = true)
    public int id;

    // unique key to lookup the contact in the database
    public String contactLookupKey;

    // 	ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
    public String contactName;

    // ContactsContract.CommonDataKinds.Phone
    public String contactNumber;

    // ContactsContract.CommonDataKinds.Photo
    public String contactImageURI;

    // amount that is being borrowed or lent
    public double amount;

    // flag to indicate whether money is borrowed or lent
    public boolean lent;

    @Nullable
    public String notes;

    /*
    * if the user chooses not to enter a date the date will be automatically
    * applied to the current date
    * */
    public Date timestamp;

    // extra column to be used by group by clause
    @ColumnInfo(name = "sum(amount)")
    public double amountAggregate;

    public void setNotes(@Nullable String notes) {
        this.notes = notes;
    }

    public Transaction(String contactLookupKey, String contactName, String contactNumber, String contactImageURI, double amount, boolean lent) {
        this.contactLookupKey = contactLookupKey;
        this.contactName = contactName;
        this.contactNumber = contactNumber;
        this.contactImageURI = contactImageURI;

        if (lent)
            this.amount = amount;
        else
            this.amount = -amount;

        this.lent = lent;
    }

    @Ignore
    public Transaction(String contactLookupKey, double amount, boolean lent) {
        this.contactLookupKey = contactLookupKey;
        this.amount = amount;
        this.lent = lent;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", contactLookupKey='" + contactLookupKey + '\'' +
                ", contactName='" + contactName + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                ", contactImageURI=" + contactImageURI +
                ", amount=" + amount +
                ", lent=" + lent +
                ", timestamp=" + timestamp +
                ", notes=" + notes +
                '}';
    }
}
