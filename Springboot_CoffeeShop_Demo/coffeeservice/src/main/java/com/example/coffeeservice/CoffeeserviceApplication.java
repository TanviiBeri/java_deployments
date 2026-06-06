package com.example.coffeeservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Map;

@SpringBootApplication
@RestController
public class CoffeeserviceApplication {

    private final JdbcTemplate jdbcTemplate;

    public CoffeeserviceApplication(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public static void main(String[] args) {
        SpringApplication.run(CoffeeserviceApplication.class, args);
        System.out.println("\n=============================================");
        System.out.println("☕ THE VELVET ROAST INTERACTIVE ENGINE LIVE  ☕");
        System.out.println("=============================================\n");
    }

    @GetMapping("/coffeeservice/test")
    public String renderInteractiveStorefront() {
        // 1. Fetch products out of the database
        List<Map<String, Object>> products = jdbcTemplate.queryForList(
            "SELECT p.product_id, p.name, p.description, p.base_price, c.name AS category " +
            "FROM products p LEFT JOIN categories c ON p.category_id = c.category_id"
        );

        // 2. Fetch barista profiles out of the database
        List<Map<String, Object>> baristas = jdbcTemplate.queryForList(
            "SELECT first_name, role FROM employees WHERE role != 'Owner'"
        );

        StringBuilder html = new StringBuilder();
        html.append("""
               <!DOCTYPE html>
               <html>
               <head>
                   <meta charset='UTF-8'>
                   <title>The Velvet Roast - Terminal Console</title>
                   <style>
                       :root { --accent: #ddb892; --bg: #120c0a; --card: rgba(255,255,255,0.02); --border: rgba(230,204,178,0.15); }
                       body { margin: 0; padding: 20px; background: var(--bg); font-family: 'Segoe UI', sans-serif; color: #f5ebe6; display: flex; justify-content: center; }
                       .console-container { width: 100%; max-width: 1200px; display: grid; grid-template-columns: 2fr 1fr; gap: 20px; }
                       header { grid-column: span 2; text-align: center; padding: 20px; background: var(--card); border: 1px solid var(--border); border-radius: 16px; margin-bottom: 10px; }
                       h1 { margin: 0; color: var(--accent); font-weight: 400; letter-spacing: 1px; }
                       
                       /* Interactive Navigation Tabs */
                       .tabs { display: flex; gap: 10px; margin-bottom: 20px; grid-column: span 2; }
                       .tab-btn { background: var(--card); border: 1px solid var(--border); color: #b7a497; padding: 10px 20px; border-radius: 8px; cursor: pointer; font-size: 14px; transition: 0.3s; }
                       .tab-btn.active { background: #7f5539; color: #fff; border-color: var(--accent); }
                       .view-panel { display: none; }
                       .view-panel.active { display: block; }

                       /* Catalog Layout */
                       .catalog-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(280px, 1fr)); gap: 20px; }
                       .coffee-card { background: var(--card); border: 1px solid var(--border); padding: 20px; border-radius: 12px; transition: 0.3s; position: relative; }
                       .coffee-card:hover { transform: translateY(-3px); border-color: var(--accent); }
                       .badge { background: #7f5539; color: #ede0d4; padding: 3px 8px; font-size: 10px; border-radius: 12px; font-weight: bold; }
                       .price { font-size: 18px; color: var(--accent); font-weight: bold; margin: 15px 0; }
                       
                       /* Console Buttons */
                       .action-btn { background: transparent; border: 1px solid var(--accent); color: var(--accent); padding: 8px 14px; border-radius: 6px; cursor: pointer; font-weight: 600; width: 100%; transition: 0.3s; }
                       .action-btn:hover { background: var(--accent); color: var(--bg); }
                       
                       /* Shopping Cart Drawer */
                       .cart-panel { background: var(--card); border: 1px solid var(--border); padding: 20px; border-radius: 16px; height: fit-content; position: sticky; top: 20px; }
                       .cart-title { border-bottom: 1px solid var(--border); padding-bottom: 10px; margin-top: 0; display: flex; justify-content: space-between; }
                       .cart-item { display: flex; justify-content: space-between; font-size: 14px; margin: 12px 0; padding-bottom: 8px; border-bottom: 1px dashed rgba(255,255,255,0.05); }
                       .checkout-form { margin-top: 20px; display: flex; flex-direction: column; gap: 10px; }
                       .checkout-form input, .checkout-form select { background: #231a16; border: 1px solid var(--border); padding: 10px; border-radius: 6px; color: #fff; }
                       
                       /* Barista Profiles */
                       .barista-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 20px; }
                       .barista-card { background: var(--card); border: 1px solid var(--border); border-radius: 12px; padding: 20px; text-align: center; }
                       .barista-avatar { font-size: 40px; margin-bottom: 10px; }
                   </style>
               </head>
               <body>
                   <div class='console-container'>
                       <header>
                           <h1>THE VELVET ROAST OPERATING OS</h1>
                           <p style='color: #b79a83; margin: 5px 0 0 0;'>Interactive Full-Stack Client Node</p>
                       </header>
                       
                       <div class='tabs'>
                           <button class='tab-btn active' onclick="switchTab('menu')">☕ Shop Catalog</button>
                           <button class='tab-btn' onclick="switchTab('team')">🧑‍🍳 Meet The Baristas</button>
                       </div>
                       
                       <div class='left-column'>
                           <div id='menu-panel' class='view-panel active'>
                               <div class='catalog-grid'>
               """);

        // Loop through live items to construct shop items
        for (Map<String, Object> row : products) {
            html.append("<div class='coffee-card'>");
            html.append("<span class='badge'>").append(row.get("category")).append("</span>");
            html.append("<h3 style='margin: 10px 0 5px 0;'>").append(row.get("name")).append("</h3>");
            html.append("<p style='font-size:12px; color:#b7a497; min-height:36px;'>").append(row.get("description")).append("</p>");
            html.append("<div class='price'>₹").append(row.get("base_price")).append("</div>");
            html.append("<button class='action-btn' onclick=\"addToCart('")
                .append(row.get("name")).append("', ")
                .append(row.get("base_price")).append(", ")
                .append(row.get("product_id")).append(")\">Add To Order</button>");
            html.append("</div>");
        }

        html.append("""
                               </div>
                           </div>
                           
                           <div id='team-panel' class='view-panel'>
                               <div class='barista-grid'>
               """);

        // Loop through active employees inside the database dynamically
        for (Map<String, Object> staff : baristas) {
            html.append("<div class='barista-card'>");
            html.append("<div class='barista-avatar'>☕</div>");
            html.append("<h4 style='margin:0; color:var(--accent);'>").append(staff.get("first_name")).append("</h4>");
            html.append("<p style='font-size:12px; color:#b7a497; margin:5px 0 0 0;'>").append(staff.get("role")).append("</p>");
            html.append("</div>");
        }

        html.append("""
                               </div>
                           </div>
                       </div>
                       
                       <div class='cart-panel'>
                           <h3 class='cart-title'><span>🛒 Active Order</span> <span id='cart-count' style='font-size:14px; color:var(--accent);'>0 items</span></h3>
                           <div id='cart-items-container' style='min-height: 100px;'>
                               <p id='empty-cart-msg' style='color:#b7a497; font-size:13px; text-align:center; padding-top:30px;'>No items in cart.</p>
                           </div>
                           <div style='border-top:1px solid var(--border); padding-top:15px; margin-top:15px;'>
                               <div style='display:flex; justify-content:space-between; font-weight:bold;'>
                                   <span>Total Bill:</span>
                                   <span style='color:var(--accent);' id='cart-total'>₹0.00</span>
                               </div>
                           </div>
                           
                           <form class='checkout-form' action='/coffeeservice/checkout' method='POST'>
                               <input type='hidden' id='hidden-product-id' name='productId' value=''>
                               <input type='hidden' id='hidden-quantity' name='quantity' value='1'>
                               
                               <label style='font-size:12px; color:#b7a497;'>Customer Account Profile:</label>
                               <select name='customerId'>
                                   <option value='1'>Kabir Mehta (Silver Tier)</option>
                                   <option value='2'>Riya Sen (Gold Tier)</option>
                                   <option value='3'>Rohan Das (Bronze Tier)</option>
                               </select>
                               
                               <label style='font-size:12px; color:#b7a497;'>Payment Mode Node:</label>
                               <select name='paymentMethod'>
                                   <option value='UPI'>UPI Transaction</option>
                                   <option value='Credit Card'>Credit Card Terminal</option>
                                   <option value='Loyalty Points'>Burn Loyalty Points</option>
                               </select>
                               
                               <button type='submit' class='action-btn' style='background:var(--accent); color:var(--bg); margin-top:10px;'>Transmit Checkout Request</button>
                           </form>
                       </div>
                   </div>

                   <script>
                       let cart = null;

                       function switchTab(tabId) {
                           document.querySelectorAll('.tab-btn').forEach(btn => btn.classList.remove('active'));
                           document.querySelectorAll('.view-panel').forEach(panel => panel.classList.remove('active'));
                           event.target.classList.add('active');
                           document.getElementById(tabId + '-panel').classList.add('active');
                       }

                       function addToCart(name, price, id) {
                           // Set up simple single item tracking simulation for form submission integration
                           cart = { name: name, price: price, id: id };
                           
                           // Update View Layout Screen
                           const container = document.getElementById('cart-items-container');
                           container.innerHTML = `
                               <div class='cart-item'>
                                   <div><strong>${name}</strong><br><span style="font-size:11px; color:#b7a497;">Qty: 1</span></div>
                                   <div style="color:var(--accent);">₹${price.toFixed(2)}</div>
                               </div>
                           `;
                           
                           document.getElementById('cart-count').innerText = '1 item';
                           document.getElementById('cart-total').innerText = '₹' + price.toFixed(2);
                           
                           // Bind values to the hidden transactional inputs
                           document.getElementById('hidden-product-id').value = id;
                       }
                   </script>
               </body>
               </html>
               """);

        return html.toString();
    }

    /**
     * TRANSACTION ENGINE: Handles form checkpoint targets submitted from the interactive app UI
     * Logs entries straight inside the transaction tables using Spring Boot JDBC!
     */
    @PostMapping("/coffeeservice/checkout")
    public String executeCheckoutTransaction(
            @RequestParam("productId") int productId,
            @RequestParam("quantity") int quantity,
            @RequestParam("customerId") int customerId,
            @RequestParam("paymentMethod") String paymentMethod) {
        
        if (productId == 0) {
            return "<body style='background:#120c0a; color:#ff6b6b; font-family:sans-serif; text-align:center; padding-top:100px;'>" +
                   "<h2>Transaction Rejected</h2><p>Your shopping cart payload was empty.</p><a href='/coffeeservice/test' style='color:#fff;'>Return to Terminal</a></body>";
        }

        // 1. Resolve price from master products matrix
        double unitPrice = jdbcTemplate.queryForObject(
            "SELECT base_price FROM products WHERE product_id = ?", Double.class, productId);
        double totalBill = unitPrice * quantity;

        // 2. Insert Transaction Record into standard Orders ledger table
        jdbcTemplate.update(
            "INSERT INTO orders (customer_id, employee_id, total_amount, order_status, payment_method) VALUES (?, 2, ?, 'Completed', ?)",
            customerId, totalBill, paymentMethod
        );

        // 3. Extract the generated order token identifier
        int orderId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);

        // 4. Inject structural details into line items table
        jdbcTemplate.update(
            "INSERT INTO order_details (order_id, product_id, quantity, unit_price) VALUES (?, ?, ?, ?)",
            orderId, productId, quantity, unitPrice
        );

        // 5. Reward customer profile with points automatically
        jdbcTemplate.update(
            "UPDATE customers SET loyalty_points = loyalty_points + ? WHERE customer_id = ?",
            (int)(totalBill * 0.1), customerId
        );

        return """
               <body style='background:#120c0a; color:#ddb892; font-family:sans-serif; text-align:center; padding-top:100px;'>
                   <div style='border: 1px solid #7f5539; max-width:500px; margin:0 auto; padding:40px; border-radius:12px; background:rgba(255,255,255,0.01);'>
                       <div style='font-size:50px;'></div>
                       <h2 style='color:#e6ccb2;'>Checkout Stream Complete</h2>
                       <p style='color:#b7a497;'>Order Token Logged Successfully into MySQL Ledger Instance.</p>
                       <hr style='border:0; border-top:1px dashed #7f5539; margin:20px 0;'>
                       <p style='font-size:14px; text-align:left;'>• <b>Order Reference ID:</b> #00""" + orderId + """
                       <br>• <b>Total Transmitted Bill:</b> ₹""" + totalBill + """
                       <br>• <b>Loyalty Points Appended:</b> +""" + (int)(totalBill * 0.1) + """
                       </p>
                       <a href='/coffeeservice/test' style='display:inline-block; margin-top:20px; padding:10px 20px; background:#7f5539; color:#fff; text-decoration:none; border-radius:6px;'>Return to OS Console</a>
                   </div>
               </body>
               """;
    }
}