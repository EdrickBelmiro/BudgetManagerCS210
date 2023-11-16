package model.accountclasses;

import org.json.JSONObject;
import persistence.Writable;

// Represent an account having a name and a "budget" balance
public class Account implements Writable {
    protected String name;
    protected double budget;

    // EFFECTS: instantiate an account with name accountName and balance initialBudget
    public Account(String accountName, double initialBudget) {
        this.name = accountName;
        this.budget = initialBudget;
    }

    // REQUIRES: amount > 0
    // MODIFIES: this
    // EFFECTS: increase budget by amount
    public void addBudget(double amount) {
        budget += amount;
    }

    // REQUIRES: amount > 0
    // MODIFIES: this
    // EFFECTS: decrease budget by amount
    public void subtractBudget(double amount) {
        budget -= amount;
    }

    public String getName() {
        return name;
    }

    // EFFECTS: returns a dollar form of budget balance if budget >= 0, otherwise return a negative dollar form
    public String getBudgetInDollar() {
        if (budget >= 0) {
            String balanceStr = String.format("%.2f", budget);
            return "$" + balanceStr;
        } else {
            String balanceStr = String.format("%.2f", -budget);
            return "-$" + balanceStr;
        }
    }

    // MODIFIES: this
    // EFFECTS: set name to the new name
    public void setName(String newName) {
        this.name = newName;
    }

    // MODIFIES: this
    // EFFECTS: set budget to the new budget
    public void setBudget(double newBudget) {
        this.budget = newBudget;
    }

    // EFFECTS: return this account as a JSON Object
    @Override
    public JSONObject toJson() {
        JSONObject joAccount = new JSONObject();
        joAccount.put("name", name);
        joAccount.put("budget", budget);
        return joAccount;
    }
}
