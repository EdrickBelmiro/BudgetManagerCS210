package model;

import model.moneyflowclasses.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoneyFlowTest {
    Income i1;
    Income i2;
    Income i3;
    Expense e1;
    Expense e2;
    Expense e3;

    @BeforeEach
    public void runBefore() throws ParseException {
        i1 = new Income("First Salary", 1900, "24/01/2022");
        i2 = new Income("Second Salary", 3800, "24/02/2022");
        i3 = new Income("Prize", 300000, parseDate("20/02/2022"));

        e1 = new Expense("Breakfast", 12, "23/02/2022");
        e2 = new Expense("Lunch", 15, "23/02/2022");
        e3 = new Expense("iMed", 75, parseDate("22/02/2022"));
    }

    @Test
    public void testConstructor() {
        assertEquals("First Salary", i1.getName());
        assertEquals("Second Salary", i2.getName());
        assertEquals("Breakfast", e1.getName());
        assertEquals("Lunch", e2.getName());

        assertEquals(1900, i1.getMoneyAmount());
        assertEquals(3800, i2.getMoneyAmount());
        assertEquals(12, e1.getMoneyAmount());
        assertEquals(15, e2.getMoneyAmount());

        assertEquals("24/01/2022", i1.getDateInString());
        assertEquals("24/02/2022", i2.getDateInString());
        assertEquals("23/02/2022", e1.getDateInString());
        assertEquals("23/02/2022", e2.getDateInString());
    }

    @Test
    public void testSetName() {
        i1.setName("January Salary");
        assertEquals("January Salary", i1.getName());

        i2.setName("February Salary");
        assertEquals("February Salary", i2.getName());

        e1.setName("Breakfast at Feast");
        assertEquals("Breakfast at Feast", e1.getName());

        e2.setName("Lunch at Grand Noodle Emporium");
        assertEquals("Lunch at Grand Noodle Emporium", e2.getName());
    }

    @Test
    public void testSetMoneyAmount() {
        i1.setMoneyAmount(1857.30);
        assertEquals("$1857.30", i1.getMoneyAmountInDollar());

        i2.setMoneyAmount(3714.60);
        assertEquals("$3714.60", i2.getMoneyAmountInDollar());

        e1.setMoneyAmount(12.60);
        assertEquals("$12.60", e1.getMoneyAmountInDollar());

        e2.setMoneyAmount(14.25);
        assertEquals("$14.25", e2.getMoneyAmountInDollar());
    }

    @Test
    public void testSetDateFromString() throws ParseException {
        i1.setDateFromString("10/01/2022");
        assertEquals("10/01/2022", i1.getDateInString());
        assertEquals("10/01/2022", formatDate(i1.getDate()));

        i2.setDateFromString("10/02/2022");
        assertEquals("10/02/2022", i2.getDateInString());
        assertEquals("10/02/2022", formatDate(i2.getDate()));

        e1.setDateFromString("20/02/2022");
        assertEquals("20/02/2022", e1.getDateInString());
        assertEquals("20/02/2022", formatDate(e1.getDate()));

        e2.setDateFromString("21/02/2022");
        assertEquals("21/02/2022", e2.getDateInString());
        assertEquals("21/02/2022", formatDate(e2.getDate()));
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
