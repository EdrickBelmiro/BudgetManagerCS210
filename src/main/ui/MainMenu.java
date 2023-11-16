package ui;

import model.EventLog;
import model.Event;
import model.accountclasses.Account;
import model.moneyflowclasses.*;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static javax.swing.SwingConstants.CENTER;

// Budget Manager JSwing Application
public class MainMenu {

    // Fields
    private Account account;
    private ListOfExpenses listOfExpenses;
    private ListOfIncomes listOfIncomes;
    private List<String> listOfPath;
    private String currentPath;
    private boolean isSaved;
    private int currentSelectedExpenseIndex;
    private int currentSelectedIncomeIndex;

    // Components
    private JFrame splashScreenFrame;
    private JProgressBar loadingBar;
    private JFrame mainFrame;
    private JPanel root;
    private JPanel loadPanel;
    private JPanel savePanel;
    private JButton loadPanelLoadButton;
    private JButton loadPanelNewButton;
    private JList listOfLoadPathGUI;
    private JPanel mainMenuPanel;
    private JButton editAccountButton;
    private JLabel currentPathLabel;
    private JLabel accountDisplay;
    private JButton yourExpensesButton;
    private JButton yourIncomesButton;
    private JButton historyButton;
    private JButton statisticsButton;
    private JButton mainMenuPanelSaveButton;
    private JPanel expensesPanel;
    private JButton expensesPanelHighestButton;
    private JButton expensesPanelLowestButton;
    private JButton expensesPanelMostRecentButton;
    private JButton expensesPanelAddButton;
    private JButton expensesPanelRemoveButton;
    private JButton expensesPanelBackButton;
    private JList listOfExpensesGUI;
    private JLabel expensesPanelSortTypeLabel;
    private JList listOfSavePathGUI;
    private JButton savePanelSaveButton;
    private JLabel currentPathInSaveLabel;
    private JTextField expenseNameTextField;
    private JTextField expenseDateTextField;
    private JTextField expenseAmountTextField;
    private JButton expenseConfirmChangesButton;
    private JPanel incomesPanel;
    private JList listOfIncomesGUI;
    private JButton incomesPanelHighestButton;
    private JButton incomesPanelMostRecentButton;
    private JButton incomesPanelLowestButton;
    private JButton incomesPanelAddButton;
    private JButton incomesPanelBackButton;
    private JButton incomesPanelRemoveButton;
    private JTextField incomeNameTextField;
    private JTextField incomeDateTextField;
    private JTextField incomeAmountTextField;
    private JButton incomeConfirmChangesButton;
    private JLabel incomesPanelSortTypeLabel;

    // List of Path
    private static final String JSON_DATA = "./data/accountdata.json";
    private static final String JSON_DATA_TEST_GENERAL_SET = "./data/testReaderGeneralSet.json";
    private static final String JSON_DATA_TEST_EMPTY_INCOME = "./data/testReaderEmptyIncome.json";
    private static final String JSON_DATA_TEST_EMPTY_EXPENSE = "./data/testReaderEmptyExpense.json";
    private static final String JSON_DATA_EMPTY = "./data/emptydata.json";

    // EFFECTS: Run the Budget Manager application
    public MainMenu() {
        startSplashScreen();
        init();
    }

    // MODIFIES: this
    // EFFECTS: Starts Splash Screen of Budget Manager application
    private void startSplashScreen() {
        splashScreenFrame = new JFrame();
        setBudgetManagerIcon();
        setLoadingBar();
        splashScreenFrame.add(loadingBar);
        splashScreenFrame.setSize(800, 500);
        splashScreenFrame.setLayout(null);
        splashScreenFrame.setTitle("Budget Manager");
        splashScreenFrame.setDefaultCloseOperation(splashScreenFrame.EXIT_ON_CLOSE);
        splashScreenFrame.setResizable(false);
        splashScreenFrame.setVisible(true);
        loadLoadingBar();
    }

    // MODIFIES: this
    // EFFECTS: set the Budget Manager icon
    private void setBudgetManagerIcon() {
        ImageIcon budgetManagerIcon = new ImageIcon("images/budgetManagerLogo.png");
        JLabel budgetManagerIconLabel = new JLabel();
        budgetManagerIconLabel.setIcon(budgetManagerIcon);
        budgetManagerIconLabel.setBounds(0, 0, 800, 420);
        budgetManagerIconLabel.setHorizontalAlignment(CENTER);
        budgetManagerIconLabel.setVerticalAlignment(CENTER);
        splashScreenFrame.add(budgetManagerIconLabel);
    }

    // MODIFIES: this
    // EFFECTS: set the loading bar of the program
    private void setLoadingBar() {
        loadingBar = new JProgressBar(0, 100);
        loadingBar.setValue(0);
        loadingBar.setBounds(0, 420, 800, 40);
        loadingBar.setStringPainted(true);
        loadingBar.setFont(new Font("Consolas", Font.BOLD, 20));
    }

    // MODIFIES: this
    // EFFECTS: fill the loading bar
    private void loadLoadingBar() {
        int progress = 0;
        while (progress <= 100) {
            loadingBar.setValue(progress);
            try {
                Thread.sleep((int) (80f - 80f * Math.min((progress + 5) / 100f, 1f)));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (progress >= 70) {
                loadingBar.setString("Initiating Budget Manager...");
            } else {
                String stuff = UUID.randomUUID().toString();
                loadingBar.setString("Loading: " + progress + "%"
                        + " (" + stuff + ")");
            }
            progress += 1;
        }
        splashScreenFrame.dispose();
    }

    // MODIFIES: this
    // EFFECTS: listing path slots, assigning panel and buttons, initializing some fields
    private void init() {
        listOfPath = new ArrayList<>();
        listOfPath.add(JSON_DATA);
        listOfPath.add(JSON_DATA_TEST_EMPTY_EXPENSE);
        listOfPath.add(JSON_DATA_TEST_EMPTY_INCOME);
        listOfPath.add(JSON_DATA_TEST_GENERAL_SET);
        listOfPath.add(JSON_DATA_EMPTY);

        setMainFrame();

        for (Component c : root.getComponents()) {
            JPanel p = ((JPanel) c);
            ((CardLayout) root.getLayout()).addLayoutComponent(p, p.getName());
        }
        isSaved = true;
        setLoadPanel();
    }

    // MODIFIES: this
    // EFFECTS: set main window used for the app
    private void setMainFrame() {
        mainFrame = new JFrame();
        mainFrame.setSize(800, 400);
        mainFrame.setTitle("Budget Manager");
        mainFrame.setContentPane(root);
        mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        overrideWindowListener();
        overrideListOfIncomesGUI();
        overrideListOfExpensesGUI();
        overrideAllButtons();
        mainFrame.setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: process user input, remind user to save file before exit
    private void overrideWindowListener() {
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                if (isSaved) {
                    printLog(EventLog.getInstance());
                    System.exit(0);
                } else {
                    String[] objButtons = {"Exit Anyway", "No"};
                    int promptResult = JOptionPane.showOptionDialog(null,
                            "Are you sure you want to exit?", "File is not saved",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                            null, objButtons, objButtons[1]);
                    if (promptResult == JOptionPane.YES_OPTION) {
                        printLog(EventLog.getInstance());
                        System.exit(0);
                    }
                }
            }
        });
    }

    // EFFECTS: assigning functionality to all buttons
    private void overrideAllButtons() {
        overrideMainMenuPanelSaveButton();
        overrideSavePanelSaveButton();
        overrideLoadPanelLoadButton();
        overrideYourExpensesButton();
        overrideYourIncomesButton();
        overrideLoadPanelNewButton();
        overrideExpensesPanelAddButton();
        overrideExpensesPanelRemoveButton();
        overrideExpenseConfirmChangesButton();
        overrideExpensesBackButton();
        overrideIncomesPanelAddButton();
        overrideIncomesPanelRemoveButton();
        overrideIncomeConfirmChangesButton();
        overrideIncomesBackButton();
    }

    // MODIFIES: this
    // EFFECTS: set the load panel with the correct list content
    private void setLoadPanel() {
        ((CardLayout) root.getLayout()).show(root, "loadPanel");
        displayPathToBeSelectedIn(listOfLoadPathGUI);
    }

    // MODIFIES: this
    // EFFECTS: assigning function to the new button to make a new account
    private void overrideLoadPanelNewButton() {
        loadPanelNewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                makeNewAccount();
                setMainMenuPanel();
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: assigning function to the load button to load file from selected path
    private void overrideLoadPanelLoadButton() {
        loadPanelLoadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selection = listOfLoadPathGUI.getSelectedIndex();
                if (selection == -1) {
                    showMessageToUser("Please select an account to load");
                } else {
                    String path = listOfPath.get(selection);
                    load(path);
                    setMainMenuPanel();
                }
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: set the main menu panel with the correct label content
    private void setMainMenuPanel() {
        ((CardLayout) root.getLayout()).show(root, "mainMenuPanel");
        currentPathLabel.setText("Current Directory: " + currentPath);
        setAccountDisplay();
    }

    // MODIFIES: this
    // EFFECTS: assigning function to the JList of expense to show properties of the selected
    //          expense on list
    private void overrideListOfExpensesGUI() {
        listOfExpensesGUI.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (listOfExpensesGUI.getSelectedIndex() != -1) {
                    currentSelectedExpenseIndex = listOfExpensesGUI.getSelectedIndex();
                }
                setExpensesTextFields();
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: assigning function to the JList of income to show properties of the selected
    //          income on list
    private void overrideListOfIncomesGUI() {
        listOfIncomesGUI.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (listOfIncomesGUI.getSelectedIndex() != -1) {
                    currentSelectedIncomeIndex = listOfIncomesGUI.getSelectedIndex();
                }
                setIncomesTextFields();
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: assigning function to the yourExpensesButton to change panel to the expensesPanel
    private void overrideYourExpensesButton() {
        yourExpensesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setExpensesPanel();
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: assigning function to the yourIncomesButton to change panel to the incomesPanel
    private void overrideYourIncomesButton() {
        yourIncomesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setIncomesPanel();
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: set the expensesPanel with the correct labels and list
    private void setExpensesPanel() {
        listOfExpensesGUI.setSelectedIndex(-1);
        ((CardLayout) root.getLayout()).show(root, "expensesPanel");
        DefaultListModel<String> lm = new DefaultListModel<>();
        listOfExpensesGUI.setModel(lm);
        if (listOfExpenses.size() == 0) {
            lm.add(-1, "[ You have no expenses ]");
        } else {
            int count = 1;
            for (Expense expense : listOfExpenses.getListOfExpenses()) {
                lm.add((count - 1), "[" + count + "] Spends " + expense.getMoneyAmountInDollar()
                        + " on " + expense.getName() + " on " + expense.getDateInString());
                count++;
            }

        }
        expensesPanelSortTypeLabel.setText("Your Expenses");
    }

    // MODIFIES: this
    // EFFECTS: set the incomesPanel with the correct labels and list
    private void setIncomesPanel() {
        listOfIncomesGUI.setSelectedIndex(-1);
        ((CardLayout) root.getLayout()).show(root, "incomesPanel");
        DefaultListModel<String> lm = new DefaultListModel<>();
        listOfIncomesGUI.setModel(lm);
        if (listOfIncomes.size() == 0) {
            lm.add(-1, "[ You have no incomes ]");
        } else {
            int count = 1;
            for (Income income : listOfIncomes.getListOfIncomes()) {
                lm.add((count - 1), "[" + count + "] Spends " + income.getMoneyAmountInDollar()
                        + " on " + income.getName() + " on " + income.getDateInString());
                count++;
            }
        }
        incomesPanelSortTypeLabel.setText("Your Incomes");
    }

    // MODIFIES: this
    // EFFECTS: set the expensesPanel text fields' value for a selected item
    private void setExpensesTextFields() {
        int selectedIndex = listOfExpensesGUI.getSelectedIndex();
        if (selectedIndex != -1) {
            Expense selectedExpense = listOfExpenses.get(selectedIndex);
            expenseNameTextField.setText(selectedExpense.getName());
            expenseAmountTextField.setText(Double.toString(selectedExpense.getMoneyAmount()));
            expenseDateTextField.setText(selectedExpense.getDateInString());
        }
    }

    // MODIFIES: this
    // EFFECTS: set the incomesPanel text fields' value for a selected item
    private void setIncomesTextFields() {
        int selectedIndex = listOfIncomesGUI.getSelectedIndex();
        if (selectedIndex != -1) {
            Income selectedIncome = listOfIncomes.get(selectedIndex);
            incomeNameTextField.setText(selectedIncome.getName());
            incomeAmountTextField.setText(Double.toString(selectedIncome.getMoneyAmount()));
            incomeDateTextField.setText(selectedIncome.getDateInString());
        }
    }

    // MODIFIES: this
    // EFFECTS: assigning function to the confirm change button to change the property of a selected expense
    private void overrideExpenseConfirmChangesButton() {
        expenseConfirmChangesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Expense selectedExpense = listOfExpenses.get(currentSelectedExpenseIndex);
                selectedExpense.setName(expenseNameTextField.getText());
                showMessageToUser("Expense name changed!");
                tryModifyExpenseAmount(selectedExpense);
                try {
                    selectedExpense.setDateFromString(expenseDateTextField.getText());
                    showMessageToUser("Expense date changed!");
                } catch (Exception ex) {
                    showMessageToUser("Please enter a valid date");
                }
                isSaved = false;
                setExpensesPanel();
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: assigning function to the confirm change button to change the property of a selected income
    private void overrideIncomeConfirmChangesButton() {
        incomeConfirmChangesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Income selectedIncome = listOfIncomes.get(currentSelectedIncomeIndex);
                selectedIncome.setName(incomeNameTextField.getText());
                showMessageToUser("Income name changed!");
                tryModifyIncomeAmount(selectedIncome);
                try {
                    selectedIncome.setDateFromString(incomeDateTextField.getText());
                    showMessageToUser("Income date changed!");
                } catch (Exception ex) {
                    showMessageToUser("Please enter a valid date");
                }
                isSaved = false;
                setIncomesPanel();
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: a helper function made to change amount property of an expense selected,
    // made because of line limit
    private void tryModifyExpenseAmount(Expense selectedExpense) {
        try {
            Expense oldExpense = new Expense(selectedExpense.getName(), selectedExpense.getMoneyAmount(),
                    selectedExpense.getDate());
            double newAmount = Math.abs(Double.parseDouble(expenseAmountTextField.getText()));
            selectedExpense.setMoneyAmount(newAmount);
            updateCurrentBudget(selectedExpense, oldExpense);
            showMessageToUser("Expense amount changed!");
        } catch (Exception ex) {
            showMessageToUser("Please enter a valid amount");
        }
    }

    // MODIFIES: this
    // EFFECTS: a helper function made to change amount property of an income selected,
    // made because of line limit
    private void tryModifyIncomeAmount(Income selectedIncome) {
        try {
            Income oldIncome = new Income(selectedIncome.getName(), selectedIncome.getMoneyAmount(),
                    selectedIncome.getDate());
            double newAmount = Math.abs(Double.parseDouble(incomeAmountTextField.getText()));
            selectedIncome.setMoneyAmount(newAmount);
            updateCurrentBudget(selectedIncome, oldIncome);
            showMessageToUser("Income amount changed!");
        } catch (Exception ex) {
            showMessageToUser("Please enter a valid amount");
        }
    }

    // MODIFIES: this
    // EFFECTS: assigning function to the back button in the expense panel
    private void overrideExpensesBackButton() {
        expensesPanelBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setMainMenuPanel();
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: assigning function to the back button in the income panel
    private void overrideIncomesBackButton() {
        incomesPanelBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setMainMenuPanel();
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: assigning function to the add expense button to prompts user input to add expense
    private void overrideExpensesPanelAddButton() {
        expensesPanelAddButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String expenseName = JOptionPane.showInputDialog("Please enter the expense name:");
                Double amount = readDoubleButBetter("Please enter the expense amount:",
                        "Please enter a valid value \n [e.g., 2.35 for $2.35, 2.00 for $2.00]");
                Expense newExpense = new Expense(expenseName, amount, new Date());
                listOfExpenses.add(newExpense);
                DefaultListModel<String> lm = (DefaultListModel<String>) listOfExpensesGUI.getModel();
                lm.add(lm.size(), "[" + (lm.size() + 1) + "] Spends " + newExpense.getMoneyAmountInDollar()
                        + " on " + newExpense.getName() + " on " + newExpense.getDateInString());
                updateCurrentBudget(newExpense);
                showMessageToUser("Expense added!");
                isSaved = false;
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: assigning function to the add income button to prompts user input to add income
    private void overrideIncomesPanelAddButton() {
        incomesPanelAddButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String incomeName = JOptionPane.showInputDialog("Please enter the income name:");
                Double amount = readDoubleButBetter("Please enter the income amount:",
                        "Please enter a valid value \n [e.g., 2.35 for $2.35, 2.00 for $2.00]");
                Income newIncome = new Income(incomeName, amount, new Date());
                listOfIncomes.add(newIncome);
                DefaultListModel<String> lm = (DefaultListModel<String>) listOfIncomesGUI.getModel();
                lm.add(lm.size(), "[" + (lm.size() + 1) + "] Spends " + newIncome.getMoneyAmountInDollar()
                        + " on " + newIncome.getName() + " on " + newIncome.getDateInString());
                updateCurrentBudget(newIncome);
                showMessageToUser("Income added!");
                isSaved = false;
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: assigning function to the remove expense button to remove the expense from the list and
    //          update the panel content
    private void overrideExpensesPanelRemoveButton() {
        expensesPanelRemoveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selection = listOfExpensesGUI.getSelectedIndex();
                if (selection == -1) {
                    showMessageToUser("Please select an expense");
                } else {
                    account.addBudget(listOfExpenses.get(selection).getMoneyAmount());
                    listOfExpenses.remove(selection);
                    showMessageToUser("Expense removed!");
                    isSaved = false;
                    setExpensesPanel();
                }
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: assigning function to the remove income button to remove the income from the list and
    //          update the panel content
    private void overrideIncomesPanelRemoveButton() {
        incomesPanelRemoveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selection = listOfIncomesGUI.getSelectedIndex();
                if (selection == -1) {
                    showMessageToUser("Please select an income");
                } else {
                    account.subtractBudget(listOfIncomes.get(selection).getMoneyAmount());
                    listOfIncomes.remove(selection);
                    showMessageToUser("Income removed!");
                    isSaved = false;
                    setIncomesPanel();
                }
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: assigning function to the save button to save the file
    private void overrideMainMenuPanelSaveButton() {
        mainMenuPanelSaveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayPathToBeSelectedIn(listOfSavePathGUI);
                setSavePanel();
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: set the savePanel to the correct label and list content
    private void setSavePanel() {
        ((CardLayout) root.getLayout()).show(root, "savePanel");
        currentPathInSaveLabel.setText("Current path: " + currentPath);
    }

    // MODIFIES: this
    // EFFECTS: assigning function to save to a seelected path
    private void overrideSavePanelSaveButton() {
        savePanelSaveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selection = listOfSavePathGUI.getSelectedIndex();
                if (selection == -1) {
                    showMessageToUser("Please select a path to save to");
                } else {
                    int sure = askConfirmationToUser("Are you sure you want to overwrite ["
                            + (selection + 1) + "] ?");
                    if (sure == 0) {
                        isSaved = true;
                        String path = listOfPath.get(selection);
                        save(path);
                        currentPath = path;
                        setMainMenuPanel();
                    }
                    return;
                }
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: set the greetings label in the main menu
    private void setAccountDisplay() {
        String warning;
        if (account.getBudgetInDollar().charAt(0) == '-') {
            warning = "< Warning: Low Balance >";
        } else {
            warning = "";
        }
        accountDisplay.setText("<html>Welcome to Budget Manager, " + account.getName() + "!"
                + "<br><br>You have " + account.getBudgetInDollar() + "<br><br>" + warning + "</html>");
    }

    // MODIFIES: this
    // EFFECTS: prompts user input to make a new account
    private void makeNewAccount() {
        try {
            String userName = "";
            while (userName.equals("")) {
                userName = JOptionPane.showInputDialog("To start, please enter your name:");
            }
            Double budget = Math.abs(readDoubleButBetter("Please enter your starting balance:",
                    "Please enter a valid value \n [e.g., 2.35 for $2.35, 2.00 for $2.00]"));
            account = new Account(userName, budget);
            listOfExpenses = new ListOfExpenses();
            listOfIncomes = new ListOfIncomes();
            if (account.getName() == null) {
                throw new Exception();
            }
        } catch (Exception e) {
            setLoadPanel();
        }
    }

    // MODIFIES: this, jl
    // EFFECTS: add the paths to the jl
    private void displayPathToBeSelectedIn(JList jl) {
        DefaultListModel<String> lm = new DefaultListModel<>();
        int count = 1;
        for (String p : listOfPath) {
            lm.add((count - 1), "[" + count + "] " + showAccountName(p) + " in " + p);
            count++;
        }
        jl.setModel(lm);
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
    // EFFECTS: loads Account, ListOfExpenses, and ListOfIncomes from file
    private void load(String source) {
        try {
            JsonReader jsonReader = new JsonReader(source);
            currentPath = source;
            account = jsonReader.readAccount();
            listOfExpenses = jsonReader.readExpenses();
            listOfIncomes = jsonReader.readIncomes();
            showMessageToUser("Loaded account with name " + account.getName() + " with budget "
                    + account.getBudgetInDollar() + " from " + source);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + source);
        }
    }

    // EFFECTS: saves the Account, ListOfExpenses, and ListOfIncomes to a file
    private void save(String source) {
        JsonWriter jsonWriter = new JsonWriter(source);
        try {
            jsonWriter.open();
            jsonWriter.write(account, listOfExpenses, listOfIncomes);
            jsonWriter.close();
            showMessageToUser("Saved account with named " + account.getName() + " with budget "
                    + account.getBudgetInDollar() + " to " + source);
        } catch (FileNotFoundException e) {
            showMessageToUser("Unable to write to file: " + source);
        }
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

    // EFFECTS: a runnable class, instantiate the application
    public static void main(String[] args) {
        new MainMenu();
    }

    // EFFECTS: prompts user to input a valid double
    private double readDoubleButBetter(String prompt, String instruction) {
        while (true) {
            try {
                return Double.parseDouble(JOptionPane.showInputDialog(prompt));
            } catch (Exception e) {
                showMessageToUser(instruction);
            }
        }
    }

    // EFFECTS: prompts user to input a valid date
    private String readDateButBetter(String prompt, String instruction) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        while (true) {
            try {
                return dateFormat.format(dateFormat.parse(JOptionPane.showInputDialog(prompt)));
            } catch (Exception e) {
                showMessageToUser(instruction);
            }
        }
    }

    // EFFECTS: for buttons to show message in this class
    private void showMessageToUser(String message) {
        JOptionPane.showMessageDialog(this.mainFrame, message);
    }

    // EFFECTS: ask confirmation to user
    private int askConfirmationToUser(String question) {
        return JOptionPane.showConfirmDialog(this.mainFrame, question,
                "Notice", JOptionPane.YES_NO_OPTION);
    }

    private void printLog(EventLog el) {
        for (Event next: el) {
            System.out.println(next.toString() + "\n\n");
        }
    }
}
