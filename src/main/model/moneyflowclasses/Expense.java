package model.moneyflowclasses;

import java.text.ParseException;
import java.util.Date;

// Represent an expense with name, amount, and date
public class Expense extends MoneyFlow {
    // EFFECTS: instantiate an expense with name name, amount |amount|, and date based on a string format
    public Expense(String name, double amount, String dateInString) throws ParseException {
        this.name = name;
        this.moneyAmount = Math.abs(amount);
        setDateFromString(dateInString);
    }

    // EFFECTS: instantiate an expense with name name, amount |amount|, and date date
    public Expense(String name, double amount, Date date) {
        this.name = name;
        this.moneyAmount = Math.abs(amount);
        setDateFromDate(date);
    }
}
