package model.moneyflowclasses;

import java.text.ParseException;
import java.util.Date;

// Represent an income with name, amount, and date
public class Income extends MoneyFlow {
    // EFFECTS: instantiate an income with name name, amount |amount|, and date based on a string format
    public Income(String name, double amount, String dateInString) throws ParseException {
        this.name = name;
        this.moneyAmount = Math.abs(amount);
        setDateFromString(dateInString);
    }

    // EFFECTS: instantiate an income with name name, amount |amount|, and date date
    public Income(String name, double amount, Date date) {
        this.name = name;
        this.moneyAmount = Math.abs(amount);
        setDateFromDate(date);
    }
}
