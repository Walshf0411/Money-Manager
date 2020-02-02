package in.slwsolutions.moneymanager.database;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

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
    public long contactImageURI;

    // amount that is being borrowed or lent
    public double amount;

    // flag to indicate whether money is borrowed or lent
    public boolean lent;

    /*
    * if the user chooses not to enter a date the date will be automatically
    * applied to the current date
    * */
    public long timestamp;

    public Transaction(String contactLookupKey, String contactName, String contactNumber, long contactImageURI, double amount, boolean lent) {
        this.contactLookupKey = contactLookupKey;
        this.contactName = contactName;
        this.contactNumber = contactNumber;
        this.contactImageURI = contactImageURI;
        this.amount = amount;
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
                '}';
    }
}
