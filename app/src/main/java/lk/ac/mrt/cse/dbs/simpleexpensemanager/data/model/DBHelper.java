package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by MalshaL on 12/6/2015.
 */
public class DBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "130318K.db";
    public static final String ACCOUNT_DB = "account_details";
    public static final String TRANSACTION_DB = "transaction_details";
    public static final String ACCOUNT_NO = "account_no";
    public static final String ACCOUNT_HOLDER = "account_holder";
    public static final String BANK = "bank";
    public static final String BALANCE = "balance";
    public static final String TR_DATE = "tr_date";
    public static final String AMOUNT = "amount";
    public static final String EXPENSE_TYPE = "expense_type";

    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String CREATE_TABLE_ACCOUNT = "CREATE TABLE" + ACCOUNT_DB + "("
                + ACCOUNT_NO + "TEXT PRIMARY KEY,"
                + ACCOUNT_HOLDER + "TEXT NOT NULL,"
                + BANK + "TEXT NOT NULL,"
                + BALANCE + "DOUBLE NOT NULL);";
        String CREATE_TABLE_TRANSACTION = "CREATE TABLE" + TRANSACTION_DB + "("
                + "tr_id INTEGER PRIMARY KEY AUTO INCREMENT,"
                + ACCOUNT_NO + "TEXT,"
                + TR_DATE + "LONG NOT NULL,"
                + AMOUNT + "DOUBLE NOT NULL,"
                + EXPENSE_TYPE + "TEXT NOT NULL, FOREIGN KEY(ACCOUNT_NO) REFERENCES ACCOUNT_DB(ACCOUNT_NO) );";
        db.execSQL(CREATE_TABLE_ACCOUNT);
        db.execSQL(CREATE_TABLE_TRANSACTION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
