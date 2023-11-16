package model;

import model.moneyflowclasses.Expense;
import model.moneyflowclasses.Income;
import model.moneyflowclasses.ListOfIncomes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class ListOfIncomesTest {
    ListOfIncomes listOfIncomes1;
    // ListOfExpenses listOfExpenses2;

    @BeforeEach
    public void runBefore() {
        listOfIncomes1 = new ListOfIncomes();
        //listOfExpenses2 = new ListOfExpenses();
    }

    @Test
    public void testConstructor() {
        assertTrue(listOfIncomes1.getListOfIncomes().isEmpty());
        //assertTrue(listOfExpenses2.getListOfExpenses().isEmpty());
    }

    @Test
    public void testAddIncome() {
        try {
            assertTrue(listOfIncomes1.add(new Income("1", 20, "20/20/2020")));
        } catch (ParseException e) {
            fail("ParseException shouldn't be thrown here");
        }
        assertEquals(1, listOfIncomes1.size());
        assertEquals("1", listOfIncomes1.get(0).getName());
    }

    @Test
    public void testAddInvalidExpense() {
        try {
            assertTrue(listOfIncomes1.add(new Income("1", 20, "20/20//2020")));
            fail("ParseException should be thrown here");
        } catch (ParseException e) {
            // Pass
        }
        assertEquals(0, listOfIncomes1.size());
    }

    @Test
    public void testRemoveExpense() {
        try {
            assertTrue(listOfIncomes1.add(new Income("1", 20, "20/20/2020")));
            assertTrue(listOfIncomes1.add(new Income("2", 30, "21/21/2021")));
        } catch (ParseException e) {
            fail("ParseException shouldn't be thrown here");
        }
        assertEquals(20, listOfIncomes1.remove(0).getMoneyAmount());
        assertEquals(1, listOfIncomes1.size());
        assertEquals(30, listOfIncomes1.get(0).getMoneyAmount());
        assertEquals(30, listOfIncomes1.remove(0).getMoneyAmount());
        assertEquals(0, listOfIncomes1.size());
    }

    @Test
    public void testIncomeSum() {
        try {
            assertTrue(listOfIncomes1.add(new Income("1", 20, "20/20/2020")));
            assertTrue(listOfIncomes1.add(new Income("2", 30, "21/21/2021")));
            assertTrue(listOfIncomes1.add(new Income("3", 10, "21/21/2019")));
        } catch (ParseException e) {
            fail("ParseException shouldn't be thrown here");
        }
        assertEquals(60, listOfIncomes1.incomeSum());
    }

    @Test
    public void testIncomeSumFrom() {
        try {
            assertTrue(listOfIncomes1.add(new Income("1", 20, "20/20/2020")));
            assertTrue(listOfIncomes1.add(new Income("2", 30, "21/21/2021")));
            assertTrue(listOfIncomes1.add(new Income("3", 10, "21/21/2019")));
        } catch (ParseException e) {
            fail("ParseException shouldn't be thrown here");
        }
        assertEquals(60, listOfIncomes1.incomeSumFrom(parseDate("20/21/2019")));
        assertEquals(50, listOfIncomes1.incomeSumFrom(parseDate("19/20/2020")));
    }

    @Test
    public void testSortExpenses() {
        try {
            assertTrue(listOfIncomes1.add(new Income("1", 30, "20/20/2020")));
            assertTrue(listOfIncomes1.add(new Income("2", 20, "21/21/2021")));
            assertTrue(listOfIncomes1.add(new Income("3", 10, "21/21/2019")));
        } catch (ParseException e) {
            fail("ParseException shouldn't be thrown here");
        }
        listOfIncomes1.sortIncomes("highest");
        assertEquals("1", listOfIncomes1.get(0).getName());
        assertEquals("2", listOfIncomes1.get(1).getName());
        assertEquals("3", listOfIncomes1.get(2).getName());

        listOfIncomes1.sortIncomes("most recent");
        assertEquals("2", listOfIncomes1.get(0).getName());
        assertEquals("1", listOfIncomes1.get(1).getName());
        assertEquals("3", listOfIncomes1.get(2).getName());

        listOfIncomes1.sortIncomes("lowest");
        assertEquals("3", listOfIncomes1.get(0).getName());
        assertEquals("2", listOfIncomes1.get(1).getName());
        assertEquals("1", listOfIncomes1.get(2).getName());

        listOfIncomes1.sortIncomes("invalid sort type");
        assertEquals("3", listOfIncomes1.get(0).getName());
        assertEquals("2", listOfIncomes1.get(1).getName());
        assertEquals("1", listOfIncomes1.get(2).getName());
    }

    private static Date parseDate(String date) {
        try {
            return new SimpleDateFormat("dd/MM/yyyy").parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    private static String formatDate(Date date) {
        return new SimpleDateFormat("dd/MM/yyyy").format(date);
    }
}

