package persistence;

import model.accountclasses.Account;
import model.moneyflowclasses.ListOfExpenses;
import model.moneyflowclasses.ListOfIncomes;
import org.json.JSONObject;


import java.io.*;

// Represents a writer that writes JSON representation of workroom to file
public class JsonWriter {
    private static final int TAB = 4;
    private PrintWriter writer;
    private String destination;

    // EFFECTS: constructs writer to write to destination file
    public JsonWriter(String destination) {
        this.destination = destination;
    }

    // MODIFIES: this
    // EFFECTS: opens writer; throws FileNotFoundException if destination file cannot
    // be opened for writing
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(destination);
    }

    // MODIFIES: this
    // EFFECTS: writes and combines JSON representation of account, list of expenses,
    //          and list of incomes to file
    public void write(Account a, ListOfExpenses loe, ListOfIncomes loi) {
        JSONObject joSet = new JSONObject();
        JSONObject json = a.toJson();
        joSet.put("account", json);
        JSONObject joListOfExpensesParent = loe.toJson();
        JSONObject joListOfIncomesParent = loi.toJson();
        joSet.put("listOfExpenses", joListOfExpensesParent.getJSONArray("listOfExpenses"));
        joSet.put("listOfIncomes", joListOfIncomesParent.getJSONArray("listOfIncomes"));
        saveToFile(joSet.toString(TAB));
    }

    // MODIFIES: this
    // EFFECTS: closes writer
    public void close() {
        writer.close();
    }

    // MODIFIES: this
    // EFFECTS: writes string to file
    private void saveToFile(String json) {
        writer.print(json);
    }
}
