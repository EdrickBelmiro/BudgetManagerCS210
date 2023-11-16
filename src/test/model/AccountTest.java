package model;

import model.accountclasses.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AccountTest {
    private Account testAccount1;
    private Account testAccount2;

    @BeforeEach
    public void runBefore() {
        testAccount1 = new Account("Edrick", 200.50);
        testAccount2 = new Account("Philbertio", 3000.10);
    }

    @Test
    public void testConstructor() {
        assertEquals("Edrick", testAccount1.getName());
        assertEquals("$200.50", testAccount1.getBudgetInDollar());

        assertEquals("Philbertio", testAccount2.getName());
        assertEquals("$3000.10", testAccount2.getBudgetInDollar());
    }

    @Test
    public void testAddBudget() {
        testAccount1.addBudget(299.50);
        assertEquals("$500.00", testAccount1.getBudgetInDollar());

        testAccount2.addBudget(500);
        assertEquals("$3500.10", testAccount2.getBudgetInDollar());
    }

    @Test
    public void testSubtractBudget() {
        testAccount1.subtractBudget(50.20);
        assertEquals("$150.30", testAccount1.getBudgetInDollar());

        testAccount2.subtractBudget(1000.10);
        assertEquals("$2000.00", testAccount2.getBudgetInDollar());
    }

    @Test
    public void testSubtractBudgetWithNegativeAmount() {
        testAccount1.subtractBudget(250.50);
        assertEquals("-$50.00", testAccount1.getBudgetInDollar());

        testAccount2.subtractBudget(5000.20);
        assertEquals("-$2000.10", testAccount2.getBudgetInDollar());
    }

    @Test
    public void testSetName() {
        testAccount1.setName("Ethan");
        assertEquals("Ethan", testAccount1.getName());
        testAccount2.setName("Jiu");
        assertEquals("Jiu", testAccount2.getName());
    }

    @Test
    public void testSetBudget() {
        testAccount1.setBudget(300);
        assertEquals("$300.00", testAccount1.getBudgetInDollar());
        testAccount2.setBudget(10000);
        assertEquals("$10000.00", testAccount2.getBudgetInDollar());
    }
}