
import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.Scanner;
import java.sql.*;

public class Main {
    public static void main(String[] args)  {
        Scanner sc = new Scanner(System.in);

        try {
            Connection con = DBConnection.getConnection();

        //====== customer name=====
            String cname=" ";
            while (true) {
                System.out.print("customer name :");
                cname = sc.nextLine();
                if (cname.trim().isEmpty()) {
                    throw new Exception("name cannot be empty");
                }
                break;
            }
        // ====== mobile number =====
            String mobile;
            while (true) {
                try{
                System.out.print("mobile number :");
                mobile = sc.nextLine();
                if (!mobile.matches("\\d{10}"))
                    throw new Exception(" mobile mush be 10 digit");

                PreparedStatement ps = con.prepareStatement(
                        "select id from customer where mobile=?");
                ps.setString(1, mobile);
                ResultSet rs = ps.executeQuery();

                if (rs.next())
                    throw new Exception("mobile already exits");
                break;

            } catch(Exception e){
                System.out.println("Error : " + e.getMessage());
            }
           }
            PreparedStatement cps = con.prepareStatement(
                    "insert into customer(name,mobile) value(?,?)",
                    Statement.RETURN_GENERATED_KEYS);

            cps.setString(1, cname);
            cps.setString(2, mobile);
            cps.executeUpdate();

            ResultSet crs = cps.getGeneratedKeys();
            crs.next();
            int customerId = crs.getInt(1);

          //============MENU==============
            while (true) {
                try {
                    System.out.println("\n1 add item");
                    System.out.println("2 delete item");
                    System.out.println("3 print bill");
                    System.out.println("4 pay exit");
                    System.out.print("choice :");
                    int ch = sc.nextInt();

                    if (ch == 1) {
                        System.out.print("item name : ");
                        String name = sc.next();
                        System.out.print("qty : ");
                        int qty = sc.nextInt();
                        System.out.print("price : ");
                        double price = sc.nextDouble();

                        BillingService.addItem(name, qty, price);
                    } else if (ch == 2) {
                        System.out.print("item name to delete : ");
                        BillingService.deleteItem(sc.next());
                        System.out.println("---------------------------");
                        System.out.println("Delete successfully! " + cname);
                        System.out.println("---------------------------");

                    } else if (ch == 3) {
                      BillingService.printItems();

                    } else if (ch == 4) {
                        double subtotal = BillingService.getSubtotal();
                        if (subtotal == 0)
                        throw new Exception("no item in bill");

                        double discount = subtotal * 0.10;
                        double cgst = (subtotal - discount) * 0.09;
                        double sgst = (subtotal - discount) * 0.09;
                        double total = subtotal - discount + cgst + sgst;

                         //=====payment mode==========

                        System.out.print("payment mode(cash/upi/card) : ");
                        String pay = sc.next();
                        if(!pay.equalsIgnoreCase("Cash")
                                && !pay.equalsIgnoreCase("Upi")
                                && !pay.equalsIgnoreCase("card"))
                            throw new Exception("invalid payment mode");

                        PreparedStatement bps = con.prepareStatement(
                                "insert into bill values(null,?,?,?,?,?,?,?)");
                        bps.setInt(1, customerId);
                        bps.setDouble(2, subtotal);
                        bps.setDouble(3, discount);
                        bps.setDouble(4, cgst);
                        bps.setDouble(5, sgst);
                        bps.setDouble(6, total);
                        bps.setString(7, pay);
                        bps.executeUpdate();

                        System.out.println("\n===========final bill============");
                        BillingService.printItems();

                        DecimalFormat df=new DecimalFormat("0.00");

                        System.out.println("-----------------------------");
                        System.out.println("subtotal :  " +df.format(subtotal));
                        System.out.println("discount :  " +df.format(discount));
                        System.out.println("cgst     :  " +df.format(cgst));
                        System.out.println("sgst     :  " +df.format(sgst));
                        System.out.println("=============================");
                        System.out.println(" total   :  "  +df.format(total));
                        System.out.println("=============================");

                        BillingService.clearBill();
                        System.out.println("Payment Done. thank you!!");
                        break;
                    } else {
                        throw new Exception("invalid menu choice ");
                    }
                } catch(Exception e){
                    System.out.println("Error : " + e.getMessage());
                    sc.nextLine();
                }
            }
        } catch (Exception e) {
            System.out.println("Error : " + e.getMessage());

        }
    }
}








