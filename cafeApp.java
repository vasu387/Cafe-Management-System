import java.sql.*;
import java.util.*;

class CafeApp {

    static final String URL = "jdbc:mysql://localhost:3306/cafe_db";
    static final String USER = "root";
    static final String PASS = "vasu123";

    public static void main(String[] args) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
        }

        Scanner sc = new Scanner(System.in);

        while (true) {

            System.out.println("\n====== CAFE MANAGEMENT SYSTEM ======");
            System.out.println("1. Add Item");
            System.out.println("2. View Items");
            System.out.println("3. Update Item Price");
            System.out.println("4. Delete Item");
            System.out.println("5. Billing System");
            System.out.println("6. Exit");
            System.out.print("Enter choice: ");

            int ch = sc.nextInt();
            sc.nextLine();

            switch (ch) {
                case 1: addItem(sc); break;
                case 2: viewItems(); break;
                case 3: updateItem(sc); break;
                case 4: deleteItem(sc); break;
                case 5: billingSystem(sc); break;
                case 6:
                    System.out.println("Goodbye Bee 🐝💛!");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    // ----------------------------------------------------------
    // ADD ITEM (WITH CATEGORY)
    // ----------------------------------------------------------
    static void addItem(Scanner sc) {
        try (Connection con = DriverManager.getConnection(URL, USER, PASS)) {

            System.out.print("Enter Item Name: ");
            String name = sc.nextLine();

            System.out.println("\nSelect Category:");
            System.out.println("1. Food");
            System.out.println("2. Beverages");
            System.out.println("3. Dessert");
            System.out.println("4. Savouries");
            System.out.print("Enter choice: ");
            int ch = sc.nextInt();
            sc.nextLine();

            String category;
            switch (ch) {
                case 1: category = "Food"; break;
                case 2: category = "Beverages"; break;
                case 3: category = "Dessert"; break;
                case 4: category = "Savouries"; break;
                default:
                    System.out.println("Invalid! Category set to Food");
                    category = "Food";
            }

            System.out.print("Enter Price: ");
            double price = sc.nextDouble();

            String q = "INSERT INTO cafe_items(name, price, category) VALUES(?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(q);
            ps.setString(1, name);
            ps.setDouble(2, price);
            ps.setString(3, category);

            ps.executeUpdate();
            System.out.println("Item Added Successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ----------------------------------------------------------
    // VIEW ITEMS (CATEGORY-WISE)
    // ----------------------------------------------------------
    static void viewItems() {
        try (Connection con = DriverManager.getConnection(URL, USER, PASS)) {

            String[] categories = {"Food", "Beverages", "Dessert", "Savouries"};

            for (String cat : categories) {

                System.out.println("\n--- " + cat.toUpperCase() + " ---");

                String q = "SELECT * FROM cafe_items WHERE category=?";
                PreparedStatement ps = con.prepareStatement(q);
                ps.setString(1, cat);
                ResultSet rs = ps.executeQuery();

                boolean found = false;
                while (rs.next()) {
                    found = true;
                    System.out.println(
                        rs.getInt("id") + " | " +
                        rs.getString("name") + " | ₹" +
                        rs.getDouble("price")
                    );
                }

                if (!found) {
                    System.out.println("No items available.");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ----------------------------------------------------------
    // UPDATE ITEM PRICE
    // ----------------------------------------------------------
    static void updateItem(Scanner sc) {
        try (Connection con = DriverManager.getConnection(URL, USER, PASS)) {

            System.out.print("Enter Item ID to Update: ");
            int id = sc.nextInt();

            System.out.print("Enter New Price: ");
            double price = sc.nextDouble();

            String q = "UPDATE cafe_items SET price=? WHERE id=?";
            PreparedStatement ps = con.prepareStatement(q);
            ps.setDouble(1, price);
            ps.setInt(2, id);

            int rows = ps.executeUpdate();
            if (rows > 0)
                System.out.println("Price Updated!");
            else
                System.out.println("Item Not Found!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ----------------------------------------------------------
    // DELETE ITEM
    // ----------------------------------------------------------
    static void deleteItem(Scanner sc) {
        try (Connection con = DriverManager.getConnection(URL, USER, PASS)) {

            System.out.print("Enter Item ID to Delete: ");
            int id = sc.nextInt();

            String q = "DELETE FROM cafe_items WHERE id=?";
            PreparedStatement ps = con.prepareStatement(q);
            ps.setInt(1, id);

            int rows = ps.executeUpdate();
            if (rows > 0)
                System.out.println("Item Deleted!");
            else
                System.out.println("Item Not Found!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ----------------------------------------------------------
    // BILLING SYSTEM (UNCHANGED)
    // ----------------------------------------------------------
    static void billingSystem(Scanner sc) {

        try (Connection con = DriverManager.getConnection(URL, USER, PASS)) {

            Map<String, Double> selectedItems = new LinkedHashMap<>();
            Map<String, Integer> qtyMap = new LinkedHashMap<>();

            while (true) {
                System.out.println("\n--- MENU ITEMS ---");
                String q = "SELECT * FROM cafe_items";
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(q);

                System.out.println("ID | ITEM | PRICE");
                while (rs.next()) {
                    System.out.println(
                        rs.getInt("id") + " | " +
                        rs.getString("name") + " | ₹" + rs.getDouble("price")
                    );
                }

                System.out.print("\nEnter Item ID to Add to Bill (0 TO FINISH): ");
                int id = sc.nextInt();
                if (id == 0) break;

                String get = "SELECT name, price FROM cafe_items WHERE id=?";
                PreparedStatement ps = con.prepareStatement(get);
                ps.setInt(1, id);
                ResultSet item = ps.executeQuery();

                if (item.next()) {
                    String name = item.getString("name");
                    double price = item.getDouble("price");

                    System.out.print("Enter Quantity: ");
                    int qty = sc.nextInt();

                    selectedItems.put(name, price);
                    qtyMap.put(name, qty);

                    System.out.println(name + " added to bill!");
                } else {
                    System.out.println("Invalid Item ID!");
                }
            }

            double subtotal = 0;

            System.out.println("\n======== FINAL BILL ========");
            System.out.printf("%-20s %-10s %-10s %-10s\n",
                    "Item", "Price", "Qty", "Total");

            for (String name : selectedItems.keySet()) {
                double price = selectedItems.get(name);
                int qty = qtyMap.get(name);
                double total = price * qty;

                System.out.printf("%-20s %-10.2f %-10d %-10.2f\n",
                        name, price, qty, total);

                subtotal += total;
            }

            double gst = subtotal * 0.05;
            double grandTotal = subtotal + gst;

            System.out.println("\nSubtotal : ₹" + subtotal);
            System.out.println("GST (5%): ₹" + gst);
            System.out.println("Grand Total: ₹" + grandTotal);
            System.out.println("=============================");

            String billInsert = "INSERT INTO bills(subtotal, gst, grand_total) VALUES(?, ?, ?)";
            PreparedStatement pb = con.prepareStatement(billInsert, Statement.RETURN_GENERATED_KEYS);
            pb.setDouble(1, subtotal);
            pb.setDouble(2, gst);
            pb.setDouble(3, grandTotal);
            pb.executeUpdate();

            ResultSet keys = pb.getGeneratedKeys();
            int billId = 0;
            if (keys.next()) billId = keys.getInt(1);

            String itemInsert = "INSERT INTO bill_items(bill_id, product_name, price, quantity, total) VALUES(?,?,?,?,?)";
            PreparedStatement pi = con.prepareStatement(itemInsert);

            for (String name : selectedItems.keySet()) {
                double price = selectedItems.get(name);
                int qty = qtyMap.get(name);
                double total = price * qty;

                pi.setInt(1, billId);
                pi.setString(2, name);
                pi.setDouble(3, price);
                pi.setInt(4, qty);
                pi.setDouble(5, total);

                pi.executeUpdate();
            }

            System.out.println("\nBill stored in database with Bill ID: " + billId);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
