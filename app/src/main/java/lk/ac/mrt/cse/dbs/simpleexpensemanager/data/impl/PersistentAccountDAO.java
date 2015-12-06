package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.DBHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

/**
 * Created by MalshaL on 12/6/2015.
 */
public class PersistentAccountDAO implements AccountDAO{
    private DBHelper dbHelper;
    private SQLiteDatabase db;
    private String[] ACCOUNT_TABLE_COLUMNS = {DBHelper.ACCOUNT_NO, DBHelper.ACCOUNT_HOLDER, DBHelper.BANK, DBHelper.BALANCE};

    public PersistentAccountDAO(DBHelper dbHelper){
        this.dbHelper = dbHelper;
    }

    public void open() throws SQLException{
        db = dbHelper.getReadableDatabase();
    }

    public  void insert() throws SQLException{
        db = dbHelper.getWritableDatabase();
    }

    public void close(){
        db.close();
    }

    @Override
    public List<String> getAccountNumbersList(){
        try {
            this.open();
            List<String> accountNumList = new ArrayList<>();
            String selectQ = "SELECT * FROM" + DBHelper.ACCOUNT_DB;

            Cursor cursor = db.rawQuery(selectQ, null);
            while (cursor.moveToNext()) {
                accountNumList.add(cursor.getString(0));
                System.out.println(cursor.getString(0));
            }
            cursor.close();

            this.close();
            return accountNumList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Account> getAccountsList(){
        try {
            this.open();
            List<Account> accountList = new ArrayList<>();
            String selectQ = "SELECT * FROM" + DBHelper.ACCOUNT_DB;

            Cursor cursor = db.rawQuery(selectQ, null);
            while (cursor.moveToNext()) {
                Account account = new Account(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getDouble(3));
                accountList.add(account);
                System.out.println(cursor.getString(0));
            }
            cursor.close();

            this.close();
            return accountList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException{
        try {
            this.open();
            Cursor cursor = db.query(DBHelper.ACCOUNT_DB, ACCOUNT_TABLE_COLUMNS, DBHelper.ACCOUNT_NO + "=?",
                    new String[] { accountNo }, null, null, null, null);
            if (cursor != null) cursor.moveToFirst();
            Account account = new Account(cursor.getString(0), cursor.getString(1), cursor.getString(2),cursor.getDouble(3));
            this.close();
            return account;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void addAccount(Account account){
        try {
            this.insert();
        Cursor cursor = db.query(DBHelper.ACCOUNT_DB, new String[]{DBHelper.ACCOUNT_NO},DBHelper.ACCOUNT_NO+" = ?" ,
                new String[] { account.getAccountNo()}, null , null, null, null);
        if (!cursor.moveToNext()) {
            ContentValues values = new ContentValues();
            values.put(DBHelper.ACCOUNT_NO, account.getAccountNo());
            values.put(DBHelper.ACCOUNT_HOLDER, account.getAccountHolderName());
            values.put(DBHelper.BANK, account.getBankName());
            values.put(DBHelper.BALANCE, account.getBalance());
            db.insert(DBHelper.ACCOUNT_DB, null, values);
        }
        this.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException{
        try {
            this.insert();
            String id = accountNo;
            db.delete(DBHelper.ACCOUNT_DB, DBHelper.ACCOUNT_NO + " = ?", new String[]{id});
            this.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        Double amountValue=this.getAccount(accountNo).getBalance();
        ContentValues values = new ContentValues();
        switch (expenseType) {
            case EXPENSE:
                amount = amountValue-amount;
                break;
            case INCOME:
                amount = amountValue+amount;
                break;
        }
        values.put(DBHelper.BALANCE, amount);
        try {
            this.insert();
            db.update(DBHelper.ACCOUNT_DB, values, DBHelper.ACCOUNT_NO + " = ?",
                    new String[] { accountNo });
            this.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
