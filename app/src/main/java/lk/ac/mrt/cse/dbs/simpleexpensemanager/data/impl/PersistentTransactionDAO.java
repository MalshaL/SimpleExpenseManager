package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.DBHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * Created by MalshaL on 12/6/2015.
 */
public class PersistentTransactionDAO implements TransactionDAO {
    private DBHelper dbHelper;
    private String[] TRANSACTION_TABLE_COLUMNS = { DBHelper.ACCOUNT_NO,DBHelper.TR_DATE,DBHelper.EXPENSE_TYPE,DBHelper.AMOUNT};
    private SQLiteDatabase db;

    public PersistentTransactionDAO(DBHelper helper) {
        dbHelper = helper;
    }

    public void open() throws SQLException {
        db = dbHelper.getReadableDatabase();
    }

    public  void insert() throws SQLException {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        this.open();
        ContentValues values = new ContentValues();
        values.put(DBHelper.ACCOUNT_NO, accountNo);
        values.put(DBHelper.TR_DATE,  date.getTime());
        values.put(DBHelper.EXPENSE_TYPE,expenseType.name());
        values.put(DBHelper.AMOUNT,amount);
        db.insert(DBHelper.TRANSACTION_DB, null, values);
        this.close();
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        Date d = null;
        ExpenseType p = null;
        this.open();
        List<Transaction> transactionList = new ArrayList<Transaction>();
        Cursor cursor = db.query(DBHelper.TRANSACTION_DB, TRANSACTION_TABLE_COLUMNS, null, null, null, null, null);
        while (cursor.moveToNext()){
            d = new Date(cursor.getLong(1));
            p = ExpenseType.valueOf(cursor.getString(2).trim());
            Transaction tr = new Transaction(d,cursor.getString(0),p,cursor.getDouble(3));
            transactionList.add(tr);
        }
        return transactionList;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        Date r = null;
        ExpenseType p = null;
        this.open();
        List<Transaction> transactionList = new ArrayList<Transaction>();
        Cursor cursor = db.query(DBHelper.TRANSACTION_DB, TRANSACTION_TABLE_COLUMNS , null ,null ,null ,null , null);
        while (cursor.moveToNext()) {
            r = new Date(cursor.getLong(1));
            p = ExpenseType.valueOf(cursor.getString(2));
            Transaction tr = new Transaction(r,cursor.getString(0),p,cursor.getDouble(3));
            int size = transactionList.size();
            if (size <= limit) {
                transactionList.add(tr);
            }
        }
        return transactionList;
    }
}
