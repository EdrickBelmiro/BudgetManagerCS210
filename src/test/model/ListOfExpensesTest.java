package model;

import model.moneyflowclasses.Expense;
import model.moneyflowclasses.ListOfExpenses;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class ListOfExpensesTest {
    ListOfExpenses listOfExpenses1;
    // ListOfExpenses listOfExpenses2;

    @BeforeEach
    public void runBefore() {
        listOfExpenses1 = new ListOfExpenses();
        //listOfExpenses2 = new ListOfExpenses();
    }

    @Test
    public void testConstructor() {
        assertTrue(listOfExpenses1.getListOfExpenses().isEmpty());
        //assertTrue(listOfExpenses2.getListOfExpenses().isEmpty());
    }

    @Test
    public void testAddExpense() {
        try {
            assertTrue(listOfExpenses1.add(new Expense("1", 20, "20/20/2020")));
        } catch (ParseException e) {
            fail("ParseException shouldn't be thrown here");
        }
        assertEquals(1, listOfExpenses1.size());
        assertEquals("1", listOfExpenses1.get(0).getName());
    }

    @Test
    public void testAddInvalidExpense() {
        try {
            assertTrue(listOfExpenses1.add(new Expense("1", 20, "20/20//2020")));
            fail("ParseException should be thrown here");
        } catch (ParseException e) {
            // Pass
        }
        assertEquals(0, listOfExpenses1.size());
    }

    @Test
    public void testRemoveExpense() {
        try {
            assertTrue(listOfExpenses1.add(new Expense("1", 20, "20/20/2020")));
            assertTrue(listOfExpenses1.add(new Expense("2", 30, "21/21/2021")));
        } catch (ParseException e) {
            fail("ParseException shouldn't be thrown here");
        }
        assertEquals(20, listOfExpenses1.remove(0).getMoneyAmount());
        assertEquals(1, listOfExpenses1.size());
        assertEquals(30, listOfExpenses1.get(0).getMoneyAmount());
        assertEquals(30, listOfExpenses1.remove(0).getMoneyAmount());
        assertEquals(0, listOfExpenses1.size());
    }

    @Test
    public void testExpenseSum() {
        try {
            assertTrue(listOfExpenses1.add(new Expense("1", 20, "20/20/2020")));
            assertTrue(listOfExpenses1.add(new Expense("2", 30, "21/21/2021")));
            assertTrue(listOfExpenses1.add(new Expense("3", 10, "21/21/2019")));
        } catch (ParseException e) {
            fail("ParseException shouldn't be thrown here");
        }
        assertEquals(60, listOfExpenses1.expenseSum());
    }

    @Test
    public void testExpenseSumFrom() {
        try {
            assertTrue(listOfExpenses1.add(new Expense("1", 20, "20/20/2020")));
            assertTrue(listOfExpenses1.add(new Expense("2", 30, "21/21/2021")));
            assertTrue(listOfExpenses1.add(new Expense("3", 10, "21/21/2019")));
        } catch (ParseException e) {
            fail("ParseException shouldn't be thrown here");
        }
        assertEquals(60, listOfExpenses1.expenseSumFrom(parseDate("20/21/2019")));
        assertEquals(50, listOfExpenses1.expenseSumFrom(parseDate("19/20/2020")));
    }

    @Test
    public void testSortExpenses() {
        try {
            assertTrue(listOfExpenses1.add(new Expense("1", 30, "20/20/2020")));
            assertTrue(listOfExpenses1.add(new Expense("2", 20, "21/21/2021")));
            assertTrue(listOfExpenses1.add(new Expense("3", 10, "21/21/2019")));
        } catch (ParseException e) {
            fail("ParseException shouldn't be thrown here");
        }
        listOfExpenses1.sortExpenses("highest");
        assertEquals("1", listOfExpenses1.get(0).getName());
        assertEquals("2", listOfExpenses1.get(1).getName());
        assertEquals("3", listOfExpenses1.get(2).getName());

        listOfExpenses1.sortExpenses("most recent");
        assertEquals("2", listOfExpenses1.get(0).getName());
        assertEquals("1", listOfExpenses1.get(1).getName());
        assertEquals("3", listOfExpenses1.get(2).getName());

        listOfExpenses1.sortExpenses("lowest");
        assertEquals("3", listOfExpenses1.get(0).getName());
        assertEquals("2", listOfExpenses1.get(1).getName());
        assertEquals("1", listOfExpenses1.get(2).getName());

        listOfExpenses1.sortExpenses("invalid sort type");
        assertEquals("3", listOfExpenses1.get(0).getName());
        assertEquals("2", listOfExpenses1.get(1).getName());
        assertEquals("1", listOfExpenses1.get(2).getName());
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
