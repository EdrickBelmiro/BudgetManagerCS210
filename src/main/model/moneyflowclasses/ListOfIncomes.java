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

public class ListOfIncomes implements Writable {
    List<Income> listOfIncomes;

    // EFFECTS: instantiate an empty list of incomes
    public ListOfIncomes() {
        listOfIncomes = new ArrayList<>();
    }

    // MODIFIES: this
    // EFFECTS: add an income to the list, returns true if succeeded
    public boolean add(Income income) {
        EventLog.getInstance().logEvent(new Event("Income of " + income.getMoneyAmount()
                + " from " + income.getName() +  " is added to the list."));
        return listOfIncomes.add(income);
    }

    // EFFECTS: return the income at a certain list index
    public Income get(int index) {
        return listOfIncomes.get(index);
    }

    // EFFECTS: return the size of the list
    public int size() {
        return listOfIncomes.size();
    }

    // EFFECTS: remove an income at a certain list index and return it
    public Income remove(int index) {
        EventLog.getInstance().logEvent(new Event("Income of " + listOfIncomes.get(index).getMoneyAmount()
                + " from " + listOfIncomes.get(index).getName() +  " is removed from the list."));
        return listOfIncomes.remove(index);
    }

    // EFFECTS: return the sum of income amount in the list from a certain date until now
    public double incomeSumFrom(Date latestBoundary) {
        double sum = 0;
        for (Income i : listOfIncomes) {
            if (i.getDate().compareTo(latestBoundary) >= 0) {
                sum += i.getMoneyAmount();
            }
        }
        return sum;
    }

    // EFFECTS: return the sum of income amount in the list
    public double incomeSum() {
        double sum = 0;
        for (Income i : listOfIncomes) {
            sum += i.getMoneyAmount();
        }
        return sum;
    }

    public List<Income> getListOfIncomes() {
        return listOfIncomes;
    }

    // EFFECTS: sort the incomes in the list based on certain sortType, either:
    //          - from the most recent income
    //          - from the highest income amount
    //          - from the lowest income amount
    public void sortIncomes(String sortType) {
        Comparator<MoneyFlow> lowestAmountComparator;
        switch (sortType) {
            case "most recent":
                Comparator<MoneyFlow> leastRecentComparator = Comparator.comparing(MoneyFlow::getDate);
                listOfIncomes.sort(leastRecentComparator);
                reverseIncomes();
                break;
            case "highest":
                lowestAmountComparator = Comparator.comparing(MoneyFlow::getMoneyAmount);
                listOfIncomes.sort(lowestAmountComparator);
                reverseIncomes();
                break;
            case "lowest":
                lowestAmountComparator = Comparator.comparing(MoneyFlow::getMoneyAmount);
                listOfIncomes.sort(lowestAmountComparator);
                break;
            default:
                break;
        }
    }

    // MODIFIES: this
    // EFFECTS: reverse the list
    private void reverseIncomes() {
        for (int i = 0; i < listOfIncomes.size() / 2; i++) {
            Income atI = listOfIncomes.get(i);
            int j = listOfIncomes.size() - i - 1;
            listOfIncomes.set(i, listOfIncomes.get(j));
            listOfIncomes.set(j, atI);
        }
    }

    // EFFECTS: return this instance as a JSON Object with the list inside it as a JSON Array
    @Override
    public JSONObject toJson() {
        JSONObject joListOfIncomesParent = new JSONObject();
        JSONArray jaListOfIncomesSubs = new JSONArray();

        for (Income inc : listOfIncomes) {
            jaListOfIncomesSubs.put(inc.toJson());
        }

        joListOfIncomesParent.put("listOfIncomes", jaListOfIncomesSubs);
        return joListOfIncomesParent;
    }
}

