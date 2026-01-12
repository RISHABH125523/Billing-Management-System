package bill_management_system;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.*;

public class BillingService {
    static final double CGST_Rate = 0.09;
    static final double SGST_Rate = 0.09;

    // ========Add and update item============
    public static void addItem(String name, int qty, double price)
            throws Exception {
        if (name.trim().isEmpty()) {
            throw new Exception("item name is Empty");
        }

        if (qty <= 0) {
            throw new Exception("Quantity must be > 0");
        }

        if (price <= 0) {
            throw new Exception("price mush be >= 0");
        }

        Connection con = DBConnection.getConnection();

        PreparedStatement check = con.prepareStatement(
                "select quantity from bill_item where product_name=?"
        );
        check.setString(1, name);
        ResultSet rs = check.executeQuery();

        if (rs.next()) {
            PreparedStatement update = con.prepareStatement(
                    "update bill_item set quantity=quantity+?, total=(quantity+?)*price where product_name=?"
            );
            update.setInt(1, qty);
            update.setInt(2, qty);
            update.setString(3, name);
            update.executeUpdate();

        } else {
            PreparedStatement insert = con.prepareStatement(
                    "insert into bill_item values(null,?,?,?,?)"
            );
            insert.setString(1, name);
            insert.setInt(2, qty);
            insert.setDouble(3, price);
            insert.setDouble(4, qty * price);
            insert.executeUpdate();
            System.out.println("==Item Added==");
        }
    }

    // ======== delete item method =============
    public static void deleteItem(String name) throws Exception {
        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(
                "delete from bill_item where product_name=?"
        );
        ps.setString(1, name);
        int row = ps.executeUpdate();

        if (row == 0)
            throw new Exception("Item not found");
        System.out.println("Item deleted");
    }

    // ======== subtotal method =============
    public static double getSubtotal() throws Exception {
        Connection con = DBConnection.getConnection();
        ResultSet rs = con.createStatement().executeQuery(
              "select sum(total)from bill_item"

        );
        return rs.next() ? rs.getDouble(1) : 0;
    }

    // ======== print method =============
      public static void printItems()throws Exception{
      Connection con= DBConnection.getConnection();
      ResultSet rs=con.createStatement().executeQuery(
              "select *from bill_item");
      System.out.println("------------------------------------");
      System.out.println("\nItem      qty     price      total");
      System.out.println("------------------------------------");
      while (rs.next()){
          System.out.printf("%-8s %4d %8.2f %11.2f\n",
                  rs.getString("product_name"),
                  rs.getInt("quantity"),
                  rs.getDouble("price"),
                  rs.getDouble("total")

                  );
    }
}
    // ======== clearbill method =============
  public static void clearBill()throws Exception{
        DBConnection.getConnection().createStatement()
                .execute("truncate table bill_item");

    }
}






