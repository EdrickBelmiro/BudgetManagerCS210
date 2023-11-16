# Personal Project CPSC 210 - Budget Manager <br>
Phase 0 - Proposal
<br><br>
- What will the application do?<br>
The application will keep account of one's spendings, earnings, and financial summary.
- Who will use it?<br>
People who want to manage their financial record to keep eye on their spending to plan
their budget carefully.
- Why is this project of interest to you?<br>
Because I find it difficult to manage my money and spendings, I want to make an application
where I can sum up and list all of my earnings and spendings.

## User Stories
As a user, I want to be able to:
- Add Incomes and Expenses to my list
- Edit my Income and Expense
- Remove my Income or Expense
- See the summary of my all my Incomes and Expenses
- See the total of my Incomes and Expenses overall, in the past month, or in the past year
- Have an option to save and be reminded to save my Incomes and Expenses data
- Have an option to load or make a new budget manager for new set of Incomes and Expenses

### Instructions for Grader
- You can locate my visual component by the start of the application.
- You can load the state of my application by choosing one of the path listed upon opening the file. Click the path you 
want to load and click the *Load* button. Otherwise, you can make a new file by clicking the *New* button, and you can
enter your name to make a new account and new non-negative balance.
- You can generate the first required action related to adding Expenses/Incomes to a ListOfExpenses/ListOfIncomes by
pressing either *My Expenses* button or *My Incomes* button. There, you can press *Add* button and adding the new
expense/income by entering the name and amount. The date will automatically set to the time upon adding. You can press 
*back* after then to go back to the main menu.
- You can generate the second required action related to adding Expenses/Incomes to a ListOfExpenses/ListOfIncomes by 
selecting one of the Expense/Income in the ListOfExpense/ListOfExpense shown in the list, and then either: entering the
value of name, amount, or date of the Expense/Income you selected, and then press the *Confirm Changes* to apply the 
changes; or to remove the selected Expense/Income from the list by pressing the *remove* button. You can press *back*
after then to go back to the main menu.
- Lastly, you can save the state of the application by choosing one of the path listed upon clicking the *save* button
in the main menu. Click the path you want to save and click the *Save* button. Keep in mind that the state of the
application will fully overwrite the path you have chosen, so choose the path wisely. Current path is shown.

### Phase 4: Task 2
Here are logs when an event is happening for the Xs and Y.
- A log when an expense is added to the list:
```
Sun Apr 09 19:00:46 PDT 2023
Expense of 11.0 for Tip is added to the list.
```
- A log when an income is added to the list:
```
Sun Apr 09 19:01:15 PDT 2023
Income of 20.0 from Friend Tax is added to the list.
```
- A log when an income is removed from the list:
```
Sun Apr 09 19:01:20 PDT 2023 
Income of 20.0 from Friend Tax is removed from the list.
```
- A log when an expense is removed from the list:
```
Sun Apr 09 19:01:24 PDT 2023
Expense of 11.0 for Tip is removed from the list.
```

### Phase 4: Task 3
One very good refactoring task I would have done had I have more time is 
to refactor the ListOfExpenses and ListOfIncomes classes. The reason the 
classes have to be refactored is because of them doing mostly the same thing. 
For example, I can add an Expense and Income to their own list, I can
remove them from their own list, I can sort the list by the amount of the date,
hence there is a lot of same functionality. By the principle of coupling,
this is not the best design since one golden rule is not appropriately applied 
single point of control. I realized I also find myself copying a lot of code
and applying changes to a bunch of codes because of the duplication.

Perhaps, I could refactor them just like I refactor their elements, Expense
and Income, by making ListOfMoneyFlow which have a field of MoneyFlow. Then, I
would abstract away all the code duplications, and have both of ListOfExpense and
ListOfIncome extends the ListOfMoneyFlow. All the operations that both can
perform should not be a problem since Expense and Income extends MoneyFlow, hence
the list methods should be able to add, remove, or sort the specified element 
from the list. For the methods that depends on whether the element is Expense or
Income, I can put them in their own respective class so that they have something
to be distinguished when used in the UI class.

Other than that, I could have refactor lots lots of methods in the UI classes. 
I could have make additional classes for different functionality, so each UI classes
(in this case, the swing and console based ui) can depend a class for displaying, 
another class for communicating with the user, and so on. This would apply the 
cohesion principle, because now the classes would focus on doing one thing at a time.
By doing this I also would not need to make a method for printing and displaying
if I were to make another type of UI in the future.
