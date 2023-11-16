package model.persistence;

import model.accountclasses.Account;
import model.moneyflowclasses.ListOfExpenses;
import model.moneyflowclasses.ListOfIncomes;
import model.moneyflowclasses.MoneyFlow;
import org.junit.jupiter.api.Test;
import persistence.JsonReader;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class JsonReaderTest {

    @Test
    public void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            Account account = reader.readAccount();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    public void testReaderEmptyExpense() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyExpense.json");
        try {
            Account account = reader.readAccount();
            checkAccount(account , "Tommy Hu", "$100.00");
            ListOfExpenses listOfExpenses = reader.readExpenses();
            assertTrue(listOfExpenses.getListOfExpenses().isEmpty());
            assertEquals(0, listOfExpenses.size());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    public void testReaderEmptyIncome() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyIncome.json");
        try {
            Account account = reader.readAccount();
            checkAccount(account , "Edrick", "$1000000000.00");
            ListOfIncomes listOfIncomes = reader.readIncomes();
            assertTrue(listOfIncomes.getListOfIncomes().isEmpty());
            assertEquals(0, listOfIncomes.size());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    public void testReaderGeneralSet() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralSet.json");
        try {
            Account account = reader.readAccount();
            checkAccount(account , "Ethan Jiu", "$11980.00");
            ListOfExpenses listOfExpenses = reader.readExpenses();
            assertEquals(2, listOfExpenses.size());
            listOfExpenses.sortExpenses("highest");
            checkMoneyFlow(listOfExpenses.get(0), "Tuition fee", 10000, "01/01/2023");
            checkMoneyFlow(listOfExpenses.get(1), "Dinner", 20, "02/01/2023");
            ListOfIncomes listOfIncomes = reader.readIncomes();
            listOfIncomes.sortIncomes("highest");
            checkMoneyFlow(listOfIncomes.get(0), "RA salary", 1900, "01/02/2023");
            checkMoneyFlow(listOfIncomes.get(1), "Raffle prize", 100, "02/02/2023");
        } catch (IOException e) {
            fail("Couldn't read from file");
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