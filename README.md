# 🧾 Java + MySQL Billing Management System 

This project is a console-based billing system built using Java + MySQL with GST calculation.

# ✅ Features
Add item to bill
Update quantity automatically
Delete item
Print current bill
GST calculation (CGST 9% + SGST 9%)
Store final bill in MySQL
Clear bill after payment

📦 Project Structure
BillingProject
│
├── DBConnection.java
├── BillingService.java
└── Main.java


▶️ Run the Program
Open Main.java
Right-click → Run
Console window will open
🧾 How to Use the Program

Add Item
Choose: 1
Enter item name, quantity, price

Delete Item
Choose: 2

Enter item name
Print Bill
Choose: 3

Pay and Exit
Choose: 4
Program calculates:
Subtotal
CGST (9%)
SGST (9%)
Grand Total


🖥 Sample Output
1 Add Item
2 Delete Item
3 Print Bill
4 Pay & Exit
Choose: 1
Item Name: Rice
Quantity: 2
Price: 50

Choose: 1
Item Name: Oil
Quantity: 1
Price: 150

Choose: 4

------ FINAL BILL ------
Item    Qty   Price   Total
Rice     2   50.00   100.00
Oil      1  150.00   150.00

Subtotal : 250.0
CGST 9%  : 22.5
SGST 9%  : 22.5
TOTAL    : 295.0
Payment Done. Thank You!
