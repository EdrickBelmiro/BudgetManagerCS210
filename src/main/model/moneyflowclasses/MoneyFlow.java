package model.moneyflowclasses;

import org.json.JSONObject;
import persistence.Writable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

// Represent an abstract class for Income and Expense
public abstract class MoneyFlow implements Writable {
    protected String name;
    protected double moneyAmount;
    protected String dateInString;

    protected static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    public String getName() {
        return name;
    }

    public double getMoneyAmount() {
        return moneyAmount;
    }

    // EFFECTS: returns a dollar form of money amount spent or received
    public String getMoneyAmountInDollar() {
        String balanceStr = String.format("%.2f", moneyAmount);
        return "$" + balanceStr;
    }

    public Date getDate() {
        try {
            return DATE_FORMAT.parse(dateInString);
        } catch (ParseException e) {
            return null;
        }
    }

    // EFFECTS: returns date of income or expense in string format
    public String getDateInString() {
        return dateInString;
    }

    // MODIFIES: this
    // EFFECTS: rename the income or expense
    public void setName(String name) {
        this.name = name;
    }

    // MODIFIES: this
    // EFFECTS: set the money amount spent or received
    public void setMoneyAmount(double moneyAmount) {
        this.moneyAmount = moneyAmount;
    }

    // MODIFIES: this
    // EFFECTS: set the date based on string format dd/MM/yyyy
    public void setDateFromString(String ddMMyyyy) throws ParseException {
        this.dateInString = DATE_FORMAT.format(DATE_FORMAT.parse(ddMMyyyy));
    }

    // MODIFIES: this
    // EFFECTS: set the date from date
    public void setDateFromDate(Date date) {
        this.dateInString = DATE_FORMAT.format(date);
    }

    // EFFECTS: return this instance of income or expense as a JSON Object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("moneyAmount", moneyAmount);
        json.put("dateInString", dateInString);
        return json;
    }
}
