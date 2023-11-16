package model.persistence;


import model.accountclasses.Account;
import model.moneyflowclasses.*;
import org.junit.jupiter.api.Test;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.IOException;
import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.*;

class JsonWriterTest {
    //NOTE TO CPSC 210 STUDENTS: the strategy in designing tests for the JsonWriter is to
    //write data to a file and then use the reader to read it back in and check that we
    //read in a copy of what was written out.

    @Test
    public void testWriterInvalidFile() {
        try {
            Account account = new Account("Ethan Jiu", 20000);
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    public void testWriterEmptyExpenses() {
        try {
            Account account = new Account("Ethan Jiu", 20000);

            ListOfExpenses listOfExpenses = new ListOfExpenses();

            ListOfIncomes listOfIncomes = new ListOfIncomes();
            try {
                assertTrue(listOfIncomes.add(new Income("RA salary", 1900, "01/02/2023")));
                assertTrue(listOfIncomes.add(new Income("Raffle prize", 100, "02/02/2023")));
            } catch (ParseException e) {
                fail("Are you sure you put the dates in the right syntax? dd/MM/yyyy");
            }

            JsonWriter writer = new JsonWriter("./data/testWriterEmptyExpense.json");
            writer.open();
            writer.write(account, listOfExpenses, listOfIncomes);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyExpense.json");
            Account accountRead = reader.readAccount();
            ListOfExpenses listOfExpensesRead = reader.readExpenses();
            checkAccount(accountRead, "Ethan Jiu", "$20000.00");
            assertEquals(0, listOfExpensesRead.size());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    public void testWriterEmptyIncomes() {
        try {
            Account account = new Account("Ethan Jiu", 20000);

            ListOfExpenses listOfExpenses = new ListOfExpenses();
            try {
                assertTrue(listOfExpenses.add(new Expense("Tuition fee", 10000, "01/01/2023")));
                assertTrue(listOfExpenses.add(new Expense("Dinner", 20, "02/01/2023")));
            } catch (ParseException e) {
                fail("Are you sure you put the dates in the right syntax? dd/MM/yyyy");
            }

            ListOfIncomes listOfIncomes = new ListOfIncomes();

            JsonWriter writer = new JsonWriter("./data/testWriterEmptyIncome.json");
            writer.open();
            writer.write(account, listOfExpenses, listOfIncomes);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyIncome.json");
            Account accountRead = reader.readAccount();
            ListOfIncomes listOfIncomesRead = reader.readIncomes();
            checkAccount(accountRead, "Ethan Jiu", "$20000.00");
            assertEquals(0, listOfIncomesRead.size());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    public void testWriterGeneralSet() {
        try {
            Account account = new Account("Ethan Jiu", 20000);

            ListOfExpenses listOfExpenses = new ListOfExpenses();
            try {
                assertTrue(listOfExpenses.add(new Expense("Tuition fee", 10000, "01/01/2023")));
                assertTrue(listOfExpenses.add(new Expense("Dinner", 20, "02/01/2023")));
            } catch (ParseException e) {
                fail("Are you sure you put the dates in the right syntax? dd/MM/yyyy");
            }

            ListOfIncomes listOfIncomes = new ListOfIncomes();
            try {
                assertTrue(listOfIncomes.add(new Income("RA salary", 1900, "01/02/2023")));
                assertTrue(listOfIncomes.add(new Income("Raffle prize", 100, "02/02/2023")));
            } catch (ParseException e) {
                fail("Are you sure you put the dates in the right syntax? dd/MM/yyyy");
            }

            JsonWriter writer = new JsonWriter("./data/testWriterGeneralSet.json");
            writer.open();
            writer.write(account, listOfExpenses, listOfIncomes);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralSet.json");
            Account accountRead = reader.readAccount();
            ListOfExpenses listOfExpensesRead = reader.readExpenses();
            ListOfIncomes listOfIncomesRead = reader.readIncomes();

            checkAccount(accountRead, "Ethan Jiu", "$20000.00");
            assertEquals(2, listOfExpensesRead.size());
            checkMoneyFlow(listOfExpensesRead.get(0), "Tuition fee", 10000, "01/01/2023");
            checkMoneyFlow(listOfExpensesRead.get(1), "Dinner", 20, "02/01/2023");

            assertEquals(2, listOfIncomesRead.size());
            checkMoneyFlow(listOfIncomesRead.get(0), "RA salary", 1900, "01/02/2023");
            checkMoneyFlow(listOfIncomesRead.get(1), "Raffle prize", 100, "02/02/2023");
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    private void checkAccount(Account account, String name, String budgetInDollar) {
        assertEquals(name, account.getName());
        assertEquals(budgetInDollar, account.getBudgetInDollar());
    }

    private void checkMoneyFlow(MoneyFlow moneyFlow, String name, double amount, String dateInString) {
        assertEquals(name, moneyFlow.getName());
        assertEquals(amount, moneyFlow.getMoneyAmount());
        assertEquals(dateInString, moneyFlow.getDateInString());
    }
}
