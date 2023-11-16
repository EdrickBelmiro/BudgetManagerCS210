package ui;

import model.accountclasses.Account;
import model.moneyflowclasses.*;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

// Budget Manager application
public class BudgetManager {
    private Account account;
    private ListOfExpenses listOfExpenses;
    private ListOfIncomes listOfIncomes;
    private List<String> listOfPath;
    private String currentPath;
    private boolean isSaved;

    private Scanner input;

    private static final String JSON_DATA = "./data/accountdata.json";
    private static final String JSON_DATA_TEST_GENERAL_SET = "./data/testReaderGeneralSet.json";
    private static final String JSON_DATA_TEST_EMPTY_INCOME = "./data/testReaderEmptyIncome.json";
    private static final String JSON_DATA_TEST_EMPTY_EXPENSE = "./data/testReaderEmptyExpense.json";
    private static final String JSON_DATA_EMPTY = "./data/emptydata.json";

    // EFFECTS: Run the BudgetManager application
    public BudgetManager() {
        runApp();
    }

    // MODIFIES: this
    // EFFECTS: process user input, is the home or main menu of the app
    private void runApp() {
        boolean active = true;
        String command;

        init();

        while (active) {
            displayMenu();
            command = input.nextLine();

            if (command.equalsIgnoreCase("q")) {
                active = saveNotification();
            } else {
                processCommand(command);
            }
        }

        System.out.println("Goodbye!");
    }

    // EFFECTS: return false if file is saved, otherwise notify user and request user input to save file
    private boolean saveNotification() {
        if (isSaved) {
            return false;
        } else {
            System.out.println("You haven't saved the file!");
            System.out.println("Are you sure you want to quit?");
            System.out.println("[Y] Yes\t \t [N] No");
            return !input.nextLine().equalsIgnoreCase("y");
        }
    }

    // MODIFIES: this
    // EFFECTS: process user input, initialize account, list of expenses, list of incomes,
    //          scanner, list of available save path, and current directory
    private void init() {
        System.out.println("\nWelcome to Budget Manager!"
                + "\nBudget Manager is an app helping to keep track of your budget!");

        listOfPath = new ArrayList<>();
        listOfPath.add(JSON_DATA);
        listOfPath.add(JSON_DATA_TEST_EMPTY_EXPENSE);
        listOfPath.add(JSON_DATA_TEST_EMPTY_INCOME);
        listOfPath.add(JSON_DATA_TEST_GENERAL_SET);
        listOfPath.add(JSON_DATA_EMPTY);

        input = new Scanner(System.in);
        loadOrNew();
        input = new Scanner(System.in);
        while (account == null) {
            makeNewAccount();
        }
    }

    // MODIFIES: this
    // EFFECTS: process user input, display initial options:
    //          - l to load account
    //          - n to start a new account
    //          - q to quit the application
    private void loadOrNew() {
        while (true) {
            System.out.println("Select:");
            System.out.println("[L] Load an available account");
            System.out.println("[N] Make a new account");
            System.out.println("[Q] Quit application");
            String selection = input.nextLine();
            if (selection.equalsIgnoreCase("l")) {
                selectWhichAccountToLoad();
                break;
            } else if (selection.equalsIgnoreCase("n")) {
                makeNewAccount();
                break;
            } else if (selection.equalsIgnoreCase("q")) {
                System.out.println("Goodbye!");
                System.exit(0);
            } else {
                System.out.println("Please enter a valid option:");
            }
        }
    }

    // EFFECTS: display all available paths, later on prompts user to choose which to load
    private void selectWhichAccountToLoad() {
        System.out.println("Select which account to load:");
        displayListOfPathExplicit();
        System.out.println("Upgrade to Budget Manager Premium for more slots!");
        processCommandSelectAccount(listOfPath);
    }

    // EFFECTS: display list of path available for loading or saving
    private void displayListOfPathExplicit() {
        int count = 1;
        for (String p : listOfPath) {
            System.out.println("[" + count + "] " + showAccountName(p) + " in " + p);
            count++;
        }
    }

    // MODIFIES: this
    // EFFECTS: process user input, load data
    private void processCommandSelectAccount(List<String> lop) {
        while (true) {
            int selection = readIntButBetter();
            try {
                load(lop.get(selection - 1));
                break;
            } catch (Exception e) {
                System.out.println("Please enter a valid index:");
            }
        }
    }

    // EFFECTS: return account name contained in file directory p
    private String showAccountName(String p) {
        JsonReader reader = new JsonReader(p);
        Account accountRead;
        try {
            accountRead = reader.readAccount();
        } catch (Exception e) {
            return "<Empty>";
        }
        return accountRead.getName();
    }

    // MODIFIES: this
    // EFFECTS: process user input, prompts user to make a new account with empty expenses and incomes
    private void makeNewAccount() {
        String name = null;

        while (name == null) {
            System.out.println("\nTo start, please enter your name:");
            name = input.nextLine();
        }

        double startingBalance = 0;

        while (startingBalance == 0) {
            System.out.println("Please enter your starting balance:");
            System.out.println("[e.g., 2.35 for $2.35, 2.00 for $2.00]");
            startingBalance = readDoubleButBetter();
        }

        account = new Account(name, startingBalance);
        listOfExpenses = new ListOfExpenses();
        listOfIncomes = new ListOfIncomes();
    }

    // MODIFIES: this
    // EFFECTS: process user input in the main menu
    private void processCommand(String command) {
        switch (command.toLowerCase()) {
            case "a":
                editAccount();
                break;
            case "e":
                displayListOfExpenses();
                break;
            case "i":
                displayListOfIncomes();
                break;
            case "h":
                displayHistory();
                break;
            case "t":
                displayStatistics();
                break;
            case "s":
                selectWhichPathToSave();
                break;
            default:
                System.out.println("Please enter a valid option");
                break;
        }
    }

    // MODIFIES: this
    // EFFECTS: process user input to edit account name and budget
    private void editAccount() {
        while (true) {
            System.out.println("Actions:");
            System.out.println("[N] Edit account name");
            System.out.println("[A] Edit account budget");
            System.out.println("[Q] Back");
            String selection = input.nextLine();
            switch (selection.toLowerCase()) {
                case ("n"):
                    editAccountName();
                    return;
                case ("a"):
                    editAccountBudget();
                    return;
                case ("q"):
                    return;
                default:
                    System.out.println("Please enter a valid selection:");
                    break;
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: process user input to edit account name
    private void editAccountName() {
        System.out.println("Enter your new account name:");
        String newName = input.nextLine();
        account.setName(newName);
        System.out.println("Your account name is set to " + newName);
    }

    // MODIFIES: this
    // EFFECTS: process user input to edit account budget
    private void editAccountBudget() {
        System.out.println("Enter your new account budget:");
        double newAmount = readDoubleButBetter();
        account.setBudget(newAmount);
        System.out.println("Your budget balance is set to " + newAmount);
    }

    // EFFECTS: display all available paths, later on prompts user to choose which to load
    private void selectWhichPathToSave() {
        System.out.println("Please select save destination");
        displayListOfPathExplicit();
        System.out.println("Current Path: " + currentPathInProgress());
        System.out.println("[0] Back");
        processCommandSelectAccountToSave(listOfPath);
    }

    // EFFECTS: return current path that is under the modification, else return a empty notice
    private String currentPathInProgress() {
        if (currentPath != null) {
            return "[" + (listOfPath.indexOf(currentPath) + 1) + "] " + showAccountName(currentPath)
                    + " in " + currentPath;
        } else {
            return "<Empty>";
        }
    }

    // MODIFIES: this
    // EFFECTS: process user input, save and overwrite data inside the chosen path
    private void processCommandSelectAccountToSave(List<String> lop) {
        int selection;
        while (true) {
            selection = readIntButBetter();
            if (selection == 0) {
                return;
            }
            try {
                lop.get(selection - 1);
                break;

            } catch (Exception e) {
                System.out.println("Please enter a valid index:");
            }
        }
        System.out.println("Are you sure you want to overwrite [" + selection + "] ?");
        System.out.println("[Y] Yes\t \t [N] No");
        String selectionSure = input.nextLine();
        if (selectionSure.equalsIgnoreCase("y")) {
            isSaved = true;
            save(lop.get(selection - 1));
        } else {
            selectWhichPathToSave();
        }
    }

    // EFFECTS: display main menu of the app
    private void displayMenu() {
        System.out.println("\nWelcome to Budget Manager, " + account.getName() + "!");
        System.out.println("\nYou have " + account.getBudgetInDollar());
        if (account.getBudgetInDollar().charAt(0) == '-') {
            System.out.println("< Warning: Low Balance >");
        }
        System.out.println("\nPress:");
        System.out.println("\t[A] Edit Account");
        System.out.println("\t[E] Your Expenses");
        System.out.println("\t[I] Your Incomes");
        System.out.println("Summary:");
        System.out.println("\t[H] History");
        System.out.println("\t[T] Statistics");
        System.out.println("Other option:");
        System.out.println("\t[S] Save");
        System.out.println("\t[Q] Quit");
    }

    // MODIFIES: this
    // EFFECTS: sort listOfExpenses based on preferences, process user input,
    //          display subMenu1 of the app with additional options
    private void displayListOfExpenses() {
        isSaved = false;
        String selection;
        String sortType = "most recent";
        while (true) {
            listOfExpenses.sortExpenses(sortType);
            displayListOfExpensesText(sortType);
            selection = input.nextLine();
            sortType = processCommandExpenses(selection, sortType);
            if (sortType == null) {
                return;
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: process user input:
    //          - add Expense
    //          - edit Expense
    //          - sort from highest
    //          - sort from lowest
    //          - sort from most recent
    //          - quit and go back to main menu
    private String processCommandExpenses(String selection, String sortType) {
        switch (selection.toLowerCase()) {
            case "a":
                addOneExpense();
                break;
            case "e":
                selectOneExpense();
                break;
            case "h":
                sortType = "highest";
                break;
            case "l":
                sortType = "lowest";
                break;
            case "r":
                sortType = "most recent";
                break;
            case "q":
                return null;
            default:
                System.out.println("Please enter a valid option");
                break;
        }
        return sortType;
    }

    // EFFECTS: display subMenu1 of the app, showing all list of expenses and options
    private void displayListOfExpensesText(String sortType) {
        System.out.println("\nHere are your list of expenses, sorted from " + sortType);
        displayListOfExpensesExplicit();
        System.out.println("\t Sort from:");
        System.out.println("\t [H] Highest");
        System.out.println("\t [L] Lowest");
        System.out.println("\t [R] Most recent");
        System.out.println("[A] Add expenses");
        System.out.println("[E] Edit expenses");
        System.out.println("\n[Q] Back");
    }

    // EFFECTS: display a message if the listOfExpenses is empty, otherwise display all the expenses, numbered
    private void displayListOfExpensesExplicit() {
        if (listOfExpenses.size() == 0) {
            System.out.println("[ You have no expenses ]");
        } else {
            int count = 1;
            for (Expense expense : listOfExpenses.getListOfExpenses()) {
                System.out.println("[" + count + "] Spends " + expense.getMoneyAmountInDollar()
                        + " on " + expense.getName() + " on " + expense.getDateInString());
                count += 1;
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: process user input, add one expense to listOfExpenses
    private void addOneExpense() {
        System.out.println("\nAdd Expense:");
        System.out.println("Please enter the expense name:");
        String expenseName = input.nextLine();
        System.out.println("Please enter the expense amount:");

        double expenseAmount = readDoubleButBetter();
        Expense newExpense = new Expense(expenseName, expenseAmount, new Date());
        listOfExpenses.add(newExpense);
        updateCurrentBudget(newExpense);
        System.out.println("Expense added!");
    }

    // MODIFIES: this
    // EFFECTS: display edit options for user, process user input to:
    //          - edit name
    //          - edit amount
    //          - edit date
    //          - remove the expense
    //          - go back to the subMenu1
    private void selectOneExpense() {
        System.out.println("Edit Expense:");
        System.out.println("^ Please select expense number to be edited ^");
        int expenseIndex = readIntButBetter();
        Expense selectedExpense = listOfExpenses.get(expenseIndex - 1);
        while (true) {
            System.out.println("[" + expenseIndex + "] Spends " + selectedExpense.getMoneyAmountInDollar() + " on "
                    + selectedExpense.getName() + " on " + selectedExpense.getDateInString());
            System.out.println("Actions:");
            System.out.println("\t [N] edit name");
            System.out.println("\t [A] edit amount");
            System.out.println("\t [D] edit date");
            System.out.println("\t [X] remove from expenses");
            System.out.println("\t [Q] go back");
            String selectOneExpenseSelection = input.nextLine();

            if (selectOneExpenseSelection.equalsIgnoreCase("q")) {
                break;
            } else {
                processSelectOneExpenseSelection(expenseIndex, selectedExpense, selectOneExpenseSelection);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: process user input, edit the name, the date, or the amount of the expense
    //          go back to the subMenu1
    private void processSelectOneExpenseSelection(int expenseIndex, Expense selectedExpense,
                                                  String selectOneExpenseSelection) {
        switch (selectOneExpenseSelection.toLowerCase()) {
            case "n":
                processSelectOneExpenseSelectionN(selectedExpense);
                break;
            case "a":
                processSelectOneExpenseSelectionA(selectedExpense);
                break;
            case "d":
                processSelectOneExpenseSelectionD(selectedExpense);
                break;
            case "x":
                listOfExpenses.remove(expenseIndex - 1);
                System.out.println("\nThis expense is removed! It will be gone in the list!");
                break;
            default:
                System.out.println("Please enter a valid option");
                break;
        }
    }

    // MODIFIES: this
    // EFFECTS: process user input, set the name of an expense
    private void processSelectOneExpenseSelectionN(Expense selectedExpense) {
        System.out.println("\nPlease enter new name:");
        String newName = input.nextLine();
        selectedExpense.setName(newName);
        System.out.println("Name changed successfully to " + newName + "!");
    }

    // MODIFIES: this
    // EFFECTS: process user input, set the amount of an expense
    private void processSelectOneExpenseSelectionA(Expense selectedExpense) {
        System.out.println("\nPlease enter new amount:");
        double newAmount = readDoubleButBetter();

        Expense newExpense = new Expense(selectedExpense.getName(), newAmount, selectedExpense.getDate());
        updateCurrentBudget(newExpense, selectedExpense);
        selectedExpense.setMoneyAmount(newAmount);

        System.out.println("Expense amount changed successfully to " + newAmount + "!");
    }

    // MODIFIES: this
    // EFFECTS: process user input, set the date of an expense
    private void processSelectOneExpenseSelectionD(Expense selectedExpense) {
        System.out.println("\nPlease enter new date: [dd/mm/yyyy]");
        String newDate = input.nextLine();
        try {
            selectedExpense.setDateFromString(newDate);
        } catch (ParseException e) {
            System.out.println("Please enter a valid date format");
        }
        System.out.println("Date changed successfully to " + newDate + "!");
    }

    // MODIFIES: this
    // EFFECTS: sort listOfIncomes based on preferences, process user input,
    //          display subMenu1 of the app with additional options
    private void displayListOfIncomes() {
        isSaved = false;
        String selection;
        String sortType = "most recent";
        while (true) {
            listOfIncomes.sortIncomes(sortType);
            displayListOfIncomesText(sortType);
            selection = input.nextLine();
            sortType = processCommandIncomes(selection, sortType);
            if (sortType == null) {
                return;
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: process user input:
    //          - add Income
    //          - edit Income
    //          - sort from highest
    //          - sort from lowest
    //          - sort from most recent
    //          - quit and go back to main menu
    private String processCommandIncomes(String selection, String sortType) {
        switch (selection.toLowerCase()) {
            case "a":
                addOneIncome();
                break;
            case "e":
                selectOneIncome();
                break;
            case "h":
                sortType = "highest";
                break;
            case "l":
                sortType = "lowest";
                break;
            case "r":
                sortType = "most recent";
                break;
            case "q":
                return null;
            default:
                System.out.println("Please enter a valid option");
                break;
        }
        return sortType;
    }

    // EFFECTS: display subMenu1 of the app, showing all list of incomes and options
    private void displayListOfIncomesText(String sortType) {
        System.out.println("\nHere are your list of Incomes, sorted from " + sortType);
        displayListOfIncomesExplicit();
        System.out.println("\t Sort from:");
        System.out.println("\t [H] Highest");
        System.out.println("\t [L] Lowest");
        System.out.println("\t [R] Most recent");
        System.out.println("\t [O] Oldest");
        System.out.println("[A] Add Incomes");
        System.out.println("[E] Edit Incomes");
        System.out.println("\n[Q] Back");
    }

    // EFFECTS: display a message if the listOfIncomes is empty, otherwise display all the incomes, numbered
    private void displayListOfIncomesExplicit() {
        if (listOfIncomes.size() == 0) {
            System.out.println("[ You have no incomes ]");
        } else {
            int count = 1;
            for (Income income : listOfIncomes.getListOfIncomes()) {
                System.out.println("[" + count + "] Received " + income.getMoneyAmountInDollar() + " from "
                        + income.getName() + " on " + income.getDateInString());
                count += 1;
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: process user input, add one income to listOfIncomes
    private void addOneIncome() {
        System.out.println("\nAdd Income:");
        System.out.println("Please enter the Income name:");
        String incomeName = input.nextLine();
        System.out.println("Please enter the Income amount:");

        double incomeAmount = readDoubleButBetter();
        Income newIncome = new Income(incomeName, incomeAmount, new Date());
        listOfIncomes.add(newIncome);
        updateCurrentBudget(newIncome);

        System.out.println("Income added!");
    }

    // MODIFIES: this
    // EFFECTS: display edit options for user, process user input to:
    //          - edit name
    //          - edit amount
    //          - edit date
    //          - remove the income
    //          - go back to the subMenu1
    private void selectOneIncome() {
        System.out.println("Edit Income:");
        System.out.println("^ Please select Income number to be edited ^");
        int incomeIndex = readIntButBetter();
        Income selectedIncome = listOfIncomes.get(incomeIndex - 1);
        while (true) {
            System.out.println("[" + incomeIndex + "] Received " + selectedIncome.getMoneyAmountInDollar() + " from "
                    + selectedIncome.getName() + " on " + selectedIncome.getDateInString());
            System.out.println("Actions:");
            System.out.println("\t [N] edit name");
            System.out.println("\t [A] edit amount");
            System.out.println("\t [D] edit date");
            System.out.println("\t [X] remove from incomes");
            System.out.println("\t [Q] go back");
            String selectOneIncomeSelection = input.nextLine();

            if (selectOneIncomeSelection.equalsIgnoreCase("q")) {
                break;
            } else {
                processSelectOneIncomeSelection(incomeIndex, selectedIncome, selectOneIncomeSelection);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: process user input, edit the name, the date, or the amount of the income
    //          go back to the subMenu1
    private void processSelectOneIncomeSelection(int incomeIndex, Income selectedIncome,
                                                 String selectOneIncomeSelection) {
        switch (selectOneIncomeSelection.toLowerCase()) {
            case "n":
                processSelectOneIncomeSelectionN(selectedIncome);
                break;
            case "a":
                processSelectOneIncomeSelectionA(selectedIncome);
                break;
            case "d":
                processSelectOneIncomeSelectionD(selectedIncome);
                break;
            case "x":
                listOfIncomes.remove(incomeIndex - 1);
                System.out.println("\nThis income is removed! It will be gone in the list!");
                break;
            default:
                System.out.println("Please enter a valid option");
                break;
        }
    }

    // MODIFIES: this
    // EFFECTS: process user input, set the date of an income
    private void processSelectOneIncomeSelectionD(Income selectedIncome) {
        System.out.println("\nPlease enter new date: [dd/mm/yyyy]");
        String newDate = input.nextLine();
        try {
            selectedIncome.setDateFromString(newDate);
        } catch (ParseException e) {
            System.out.println("Please enter a valid date format");
        }
        System.out.println("Date changed successfully to " + newDate + "!");
    }

    // MODIFIES: this
    // EFFECTS: process user input, set the amount of an income
    private void processSelectOneIncomeSelectionA(Income selectedIncome) {
        System.out.println("\nPlease enter new amount:");
        double newAmount = readDoubleButBetter();

        Income newIncome = new Income(selectedIncome.getName(), newAmount, selectedIncome.getDate());
        updateCurrentBudget(newIncome, selectedIncome);
        selectedIncome.setMoneyAmount(newAmount);

        System.out.println("Income amount changed successfully to " + newAmount + "!");
    }

    // MODIFIES: this
    // EFFECTS: process user input, set the name of an income
    private void processSelectOneIncomeSelectionN(Income selectedIncome) {
        System.out.println("\nPlease enter new name:");
        String newName = input.nextLine();
        selectedIncome.setName(newName);
        System.out.println("Name changed successfully to " + newName + "!");
    }

    // MODIFIES: this
    // EFFECTS: update the current budget balance by either increasing if an income is added,
    //          or decreasing if an expense is added
    private void updateCurrentBudget(MoneyFlow m) {
        if (m instanceof Income) {
            this.account.addBudget(m.getMoneyAmount());
        } else if (m instanceof Expense) {
            this.account.subtractBudget(m.getMoneyAmount());
        }
    }

    // MODIFIES: this
    // EFFECTS: update the current budget balance by calculating the new difference between new and old
    //          income or expense
    private void updateCurrentBudget(MoneyFlow after, MoneyFlow before) {
        double diff = after.getMoneyAmount() - before.getMoneyAmount();
        if (after instanceof Income) {
            this.account.addBudget(diff);
        } else if (after instanceof Expense) {
            this.account.subtractBudget(diff);
        }
    }

    // EFFECTS: display history of spending or listOfExpenses and earnings or listOfIncomes
    //          process user input
    private void displayHistory() {
        while (true) {
            System.out.println("\nHere are your history of spending and earnings");
            displayHistoryExplicit();
            System.out.println("\n[Q] Back");
            String selection = input.nextLine();
            if (selection.equalsIgnoreCase("q")) {
                return;
            }
        }
    }

    // EFFECTS: the list displayed is combination between listOfExpenses and listOfIncomes,
    //          sorted by the most recent
    private void displayHistoryExplicit() {
        List<MoneyFlow> listOfAll = new ArrayList<>();
        listOfAll.addAll(listOfExpenses.getListOfExpenses());
        listOfAll.addAll(listOfIncomes.getListOfIncomes());
        if (listOfAll.size() == 0) {
            System.out.println("[ You have no history of spending nor earning ]");
        } else {
            Comparator<MoneyFlow> leastRecentComparator = Comparator.comparing(MoneyFlow::getDate);
            listOfAll.sort(leastRecentComparator);
            Collections.reverse(listOfAll);
            int count = 1;
            for (MoneyFlow m : listOfAll) {
                if (m instanceof Income) {
                    System.out.println("[" + count + "]\t[" + m.getName() + "]\t\t["
                            + m.getMoneyAmountInDollar() + "]\t\t[" + m.getDateInString() + "]");
                    count += 1;
                } else if (m instanceof Expense) {
                    System.out.println("[" + count + "]\t[" + m.getName() + "]\t\t[-"
                            + m.getMoneyAmountInDollar() + "]\t\t[" + m.getDateInString() + "]");
                    count += 1;
                }
            }
        }
    }

    // EFFECTS: display the summary of expenses and incomes, including total, last month, and last year
    //          process user input
    private void displayStatistics() {
        while (true) {
            System.out.println("Statistics");
            System.out.println("\t[Expenses]");
            System.out.println("\tTotal money spent: " + displayTotal("expenses"));
            System.out.println("\tMoney spent last month: " + displayLastMonth("expenses"));
            System.out.println("\tMoney spent last year: " + displayLastYear("expenses"));
            System.out.println("\t[Incomes]");
            System.out.println("\tTotal money received: " + displayTotal("incomes"));
            System.out.println("\tMoney received last month: " + displayLastMonth("incomes"));
            System.out.println("\tMoney received last year: " + displayLastYear("incomes"));
            System.out.println("\n[Q] Back");
            String selection = input.nextLine();
            if (selection.equalsIgnoreCase("q")) {
                return;
            }
        }
    }

    // EFFECTS: returns the total incomes or expenses in dollar format
    private String displayTotal(String type) {
        double total = 0;
        if (type.equals("expenses")) {
            total = listOfExpenses.expenseSum();
        } else if (type.equals("incomes")) {
            total = listOfIncomes.incomeSum();
        }
        return "$" + String.format("%.2f", total);
    }

    // EFFECTS: returns the total incomes or expenses for the last month in dollar format
    private String displayLastMonth(String type) {
        double total = 0;
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        Date oneMonthAgo = cal.getTime();
        if (type.equals("expenses")) {
            total = listOfIncomes.incomeSumFrom(oneMonthAgo);
        } else if (type.equals("incomes")) {
            total = listOfExpenses.expenseSumFrom(oneMonthAgo);
        }
        return "$" + String.format("%.2f", total);
    }

    // EFFECTS: returns the total incomes or expenses for the last year in dollar format
    private String displayLastYear(String type) {
        double total = 0;
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -1);
        Date oneYearAgo = cal.getTime();
        if (type.equals("expenses")) {
            total = listOfIncomes.incomeSumFrom(oneYearAgo);
        } else if (type.equals("incomes")) {
            total = listOfExpenses.expenseSumFrom(oneYearAgo);
        }
        return "$" + String.format("%.2f", total);
    }

    // EFFECTS: prevent a bug when the scanner is reading a double
    private double readDoubleButBetter() {
        while (true) {
            try {
                return Double.parseDouble(input.nextLine());
            } catch (Exception e) {
                System.out.println("Please enter a valid value: ");
                System.out.println("[e.g., 2.35 for $2.35, 2.00 for $2.00]");
            }
        }
    }

    // EFFECTS: prevent a bug when the scanner is reading an integer
    private int readIntButBetter() {
        while (true) {
            try {
                return Integer.parseInt(input.nextLine());
            } catch (Exception e) {
                System.out.println("Please enter a valid index: ");
            }
        }
    }

    // EFFECTS: saves the Account, ListOfExpenses, and ListOfIncomes to a file
    private void save(String source) {
        JsonWriter jsonWriter = new JsonWriter(source);
        try {
            jsonWriter.open();
            jsonWriter.write(account, listOfExpenses, listOfIncomes);
            jsonWriter.close();
            System.out.println("Saved account with named " + account.getName() + " with budget "
                    + account.getBudgetInDollar() + " to " + source);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + source);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads Account, ListOfExpenses, and ListOfIncomes from file
    private void load(String source) {
        JsonReader jsonReader = new JsonReader(source);
        currentPath = source;
        try {
            account = jsonReader.readAccount();
            listOfExpenses = jsonReader.readExpenses();
            listOfIncomes = jsonReader.readIncomes();
            System.out.println("Loaded account with named " + account.getName() + " with budget "
                    + account.getBudgetInDollar() + " from " + source);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + source);
        }
    }
}
