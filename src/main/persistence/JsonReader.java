package persistence;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.stream.Stream;

import model.accountclasses.Account;
import model.moneyflowclasses.Expense;
import model.moneyflowclasses.Income;
import model.moneyflowclasses.ListOfExpenses;
import model.moneyflowclasses.ListOfIncomes;
import org.json.*;

// Represents a reader that reads workroom from JSON data stored in file
public class JsonReader {
    private final String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads account from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Account readAccount() throws IOException {
        String jsonData = readFile(source);
        JSONObject joSet = new JSONObject(jsonData);
        return parseAccount(joSet);
    }

    // EFFECTS: reads source file as string and returns it
    // throws IOException if an error occurs reading data from file
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(contentBuilder::append);
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses account from JSON object and returns it
    private Account parseAccount(JSONObject joSet) {
        JSONObject joAccount = joSet.getJSONObject("account");
        String name = joAccount.getString("name");
        double budget = joAccount.getDouble("budget");
        return new Account(name, budget);
    }

    // EFFECTS: reads list of expenses from file and returns it;
    // throws IOException if an error occurs reading data from file
    public ListOfExpenses readExpenses() throws IOException {
        String jsonData = readFile(source);
        JSONObject joSet = new JSONObject(jsonData);
        return parseExpenses(joSet);
    }

    // EFFECTS: parses list of expenses from JSON object and returns it
    private ListOfExpenses parseExpenses(JSONObject joSet) {
        ListOfExpenses listOfExpenses = new ListOfExpenses();
        JSONArray jaExpenses = joSet.getJSONArray("listOfExpenses");
        for (Object json : jaExpenses) {
            JSONObject jsonObject = (JSONObject) json;
            String name = jsonObject.getString("name");
            double amount = jsonObject.getDouble("moneyAmount");
            String dateInString = jsonObject.getString("dateInString");
            try {
                listOfExpenses.add(new Expense(name, amount, dateInString));
            } catch (ParseException e) {
                return listOfExpenses;
            }
        }
        return listOfExpenses;
    }

    // EFFECTS: reads list of incomes from file and returns it;
    // throws IOException if an error occurs reading data from file
    public ListOfIncomes readIncomes() throws IOException {
        String jsonData = readFile(source);
        JSONObject joSet = new JSONObject(jsonData);
        return parseIncomes(joSet);
    }

    // EFFECTS: parses list of incomes from JSON object and returns it
    private ListOfIncomes parseIncomes(JSONObject joSet) {
        ListOfIncomes listOfIncomes = new ListOfIncomes();
        JSONArray jaIncomes = joSet.getJSONArray("listOfIncomes");
        for (Object json : jaIncomes) {
            JSONObject jsonObject = (JSONObject) json;
            String name = jsonObject.getString("name");
            double amount = jsonObject.getDouble("moneyAmount");
            String dateInString = jsonObject.getString("dateInString");
            try {
                listOfIncomes.add(new Income(name, amount, dateInString));
            } catch (ParseException e) {
                return listOfIncomes;
            }
        }
        return listOfIncomes;
    }
}