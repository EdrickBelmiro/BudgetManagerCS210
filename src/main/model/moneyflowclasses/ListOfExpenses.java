package model.moneyflowclasses;

import model.Event;
import model.EventLog;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

// Represent a list of expenses
public class ListOfExpenses implements Writable {
    List<Expense> listOfExpenses;

    // EFFECTS: instantiate an empty list of expenses
    public ListOfExpenses() {
        listOfExpenses = new ArrayList<>();
    }

    // MODIFIES: this
    // EFFECTS: add an expense to the list, returns true if succeeded
    public boolean add(Expense expense) {
        EventLog.getInstance().logEvent(new Event("Expense of " + expense.getMoneyAmount()
                + " for " + expense.getName() +  " is added to the list."));
        return listOfExpenses.add(expense);
    }

    // EFFECTS: return the expense at a certain list index
    public Expense get(int index) {
        return listOfExpenses.get(index);
    }

    // EFFECTS: return the size of the list
    public int size() {
        return listOfExpenses.size();
    }

    // MODIFIES: this
    // EFFECTS: remove an expense at a certain list index and return it
    public Expense remove(int index) {
        EventLog.getInstance().logEvent(new Event("Expense of " + listOfExpenses.get(index).getMoneyAmount()
                + " for " + listOfExpenses.get(index).getName() +  " is removed from the list."));
        return listOfExpenses.remove(index);
    }

    // EFFECTS: return the sum of expense amount in the list from a certain date until now
    public double expenseSumFrom(Date latestBoundary) {
        double sum = 0;
        for (Expense e : listOfExpenses) {
            if (e.getDate().compareTo(latestBoundary) >= 0) {
                sum += e.getMoneyAmount();
            }
        }
        return sum;
    }

    // EFFECTS: return the sum of expense amount in the list
    public double expenseSum() {
        double sum = 0;
        for (Expense e : listOfExpenses) {
            sum += e.getMoneyAmount();
        }
        return sum;
    }

    public List<Expense> getListOfExpenses() {
        return listOfExpenses;
    }

    // EFFECTS: sort the expenses in the list based on certain sortType, either:
    //          - from the most recent expense
    //          - from the highest expense amount
    //          - from the lowest expense amount
    public void sortExpenses(String sortType) {
        Comparator<MoneyFlow> lowestAmountComparator;
        switch (sortType) {
            case "most recent":
                Comparator<MoneyFlow> leastRecentComparator = Comparator.comparing(MoneyFlow::getDate);
                listOfExpenses.sort(leastRecentComparator);
                reverseExpenses();
                break;
            case "highest":
                lowestAmountComparator = Comparator.comparing(MoneyFlow::getMoneyAmount);
                listOfExpenses.sort(lowestAmountComparator);
                reverseExpenses();
                break;
            case "lowest":
                lowestAmountComparator = Comparator.comparing(MoneyFlow::getMoneyAmount);
                listOfExpenses.sort(lowestAmountComparator);
                break;
            default:
                break;
        }
    }

    // MODIFIES: this
    // EFFECTS: reverse the list
    private void reverseExpenses() {
        for (int i = 0; i < listOfExpenses.size() / 2; i++) {
            Expense atI = listOfExpenses.get(i);
            int j = listOfExpenses.size() - i - 1;
            listOfExpenses.set(i, listOfExpenses.get(j));
            listOfExpenses.set(j, atI);
        }
    }

    // EFFECTS: return this instance as a JSON Object with the list inside it as a JSON Array
    @Override
    public JSONObject toJson() {
        JSONObject joListOfExpensesParent = new JSONObject();
        JSONArray jaListOfExpensesSubs = new JSONArray();

        for (Expense e : listOfExpenses) {
            jaListOfExpensesSubs.put(e.toJson());
        }

        joListOfExpensesParent.put("listOfExpenses", jaListOfExpensesSubs);
        return joListOfExpensesParent;
    }
}

