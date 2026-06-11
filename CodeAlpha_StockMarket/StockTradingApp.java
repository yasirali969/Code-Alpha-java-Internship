// import java.util.ArrayList;
// import java.util.List;

// public class Stock{
// 	private int  StockId;
// 	private String Symbol;
// 	private String CompanyName;
// 	private double CurrentPrice;
// 	private double AvailableShares;

// 		Stock(int StockId,String Symbol,String CompanyName,double CurrentPrice,double AvailableShares){
// 			this.StockId=StockId;
// 			this.Symbol=Symbol;
// 			this.CompanyName=CompanyName;
// 			this.CurrentPrice=CurrentPrice;
// 			this.AvailableShares=AvailableShares;
// 		}

// 		int getStockid(){
// 			return StockId;
// 		}

// 		String  getSymbol(){
// 			return Symbol;
// 		}
// 		String getCompanyName(){
// 			return CompanyName;
// 		}

// 		double  getCurrentPrice(){
// 			return CurrentPrice;
// 		}

// 		double getAvaialbleShares(){
// 			return AvailableShares;
// 		}



// 		void updatePrice(double price){
// 			if(price >0){
// 				this.CurrentPrice=price;
// 			}
// 		}

// 	    void decreaeStock(int quantity){
// 	    		this.AvailableShares=AvailableShares - quantity;
// 	    }

// 	    void IncreaseStock(int quantity){
// 	    		this.AvailableShares=AvailableShares + quantity;
// 	    }

		

// 		void displayInfo(){
// 			System.out.println("============================");
// 			System.out.println("StockId :"+getStockid());
// 			System.out.println("Symbol :"+getSymbol());
// 			System.out.println("CompanyName :"+getCompanyName());
// 			System.out.println("CurrentPrice :"+getCurrentPrice());
// 			System.out.println("AvailableShares :"+getAvaialbleShares());
// 			System.out.println("============================");

// 		}
// }

// class User{
// 	private int userid;
// 	private String UserName;
// 	private String PhoneNo;
// 	private double balance;

// 			User(int userid,String UserName,String PhoneNo,double balance){
// 				this.userid=userid;
// 				this.UserName=UserName;
// 				this.PhoneNo=PhoneNo;
// 				this.balance=balance;
// 			}

// 			int getuserid(){
// 				return userid;
// 			}

// 			String getuserName(){
// 				return UserName;
// 			}
// 			String getPhoneNo(){
// 				return PhoneNo;
// 			}
// 			double  getBalance(){
// 				return balance;
// 			}

// 		void buystock(Stock s2,int quantity){
// 				double price= s2.getCurrentPrice()*quantity;
// 				if(s2.getAvaialbleShares() < quantity){
// 					System.out.println("Insufficent Stocks!");
// 					return;
// 				}

// 				if(price > getBalance()){
// 					System.out.println("Insufficent Balance!");
// 					return;
// 				}
// 				this.balance=getBalance()-price;
// 				s2.decreaeStock(quantity);
// 					System.out.println("Stocks Buy Successfully!");
// 					System.out.println("Balance After Buying stocks ="+balance);

// 			}

// 		void sellStock(Stock s3,int quantity){
// 				double Price=s3.getCurrentPrice()*quantity;
// 				this.balance=Price+getBalance();
// 				s3.IncreaseStock(quantity);

// 					System.out.println("Stocks Sold Successfully!");
// 					System.out.println("Balance After Selling stocks ="+balance);

// 			}
// }


// class Holding{
// 	   private Stock stock;
// 	   private int quantity;
// 	   private double price;
// 	   	Holding(Stock stock,int quantity,double price){
// 	   			this.stock=stock;
// 	   			this.quantity=quantity;
// 	   			this.price=price;
// 	   	}

// 	   	Stock getStock(){
// 	   		return stock;
// 	   	}

// 	   	int getQuantity(){
// 	   		return quantity;
// 	   	}
// 	   	double getPrice(){
// 	   		return price;
// 	   	}

// 	   		void displayholding(){
// 	   		System.out.println("=========  Holding ==============");
// 	   		System.out.println(stock.getSymbol());
// 	   		System.out.println(stock.getCompanyName());
// 	   		System.out.println("Quantity ="+quantity);
// 	   		System.out.println("Price ="+price);
// 	   		System.out.println(stock.getCurrentPrice());
// 	   	    System.out.println("=================================");

// 	   	}
// }



// class Portfolio {

//     private List<Holding> holdings;

//     Portfolio() {
//         holdings = new ArrayList<>();
//     }

//     void addHolding(Holding h) {
//         holdings.add(h);
//     }

//     void displayPortfolio() {

//         if (holdings.isEmpty()) {
//             System.out.println("Portfolio is Empty!");
//             return;
//         }

//         System.out.println("====== PORTFOLIO ======");

//         for (Holding h : holdings) {
//             h.displayholding();
//         }

//         System.out.println("=======================");
//     }

//     int totalHoldings() {
//         return holdings.size();
//     }
// }
// class Main{
// 	public  static void main(String [] args){
// 		Stock s1 = new Stock(1, "AAPL", "Apple Inc.", 180.50, 1000);
//         Stock s2 = new Stock(2, "TSLA", "Tesla Inc.", 250.75, 800);
//         Stock s3 = new Stock(3, "GOOGL", "Google LLC", 140.30, 1200);
//         Stock s4 = new Stock(4, "MSFT", "Microsoft Corp.", 320.90, 900);

//         s1.displayInfo();
//         s2.displayInfo();
//         s3.displayInfo();
//         s4.displayInfo();

//         s3.updatePrice(145.30);
//         s3.displayInfo();

//         User u1=new User(1,"Yasir ALi","03283029322",45000);

//         u1.buystock(s2,10);
//         u1.sellStock(s3,5);

//         Holding h1=new Holding(s1,3,180);
//         Holding h2=new Holding(s2,10,150);
//         h1.displayholding();

//         Portfolio p1=new Portfolio();
//         p1.addHolding(h1);
//         p1.addHolding(h2);

//         p1.displayPortfolio();
// 	}
// }




import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Map;

class StockTradingApp extends JFrame {

    // Database Credentials (CHANGE THESE ACCORDING TO YOUR SETUP)
    private static final String DB_URL = "jdbc:mysql://localhost:3306/stocktrading";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "1234";

    // GUI Components
    private JTable marketTable;
    private DefaultTableModel marketTableModel;
    private JLabel balanceLabel;
    private GraphPanel graphPanel;
    private final int currentUserId = 1;

    // Theme Colors
    private final Color bgColor = new Color(30, 30, 30);
    private final Color panelColor = new Color(45, 45, 45);
    private final Color textColor = new Color(220, 220, 220);
    private final Color accentColor = new Color(0, 153, 255);

    public StockTradingApp() {
        setTitle("Pro Stock Trader - Live Market & Analytics");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(bgColor);

        // --- Header Panel ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(accentColor);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel("📊 Live Stock Market Analytics", JLabel.LEFT);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        
        balanceLabel = new JLabel("Loading Balance...", JLabel.RIGHT);
        balanceLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        balanceLabel.setForeground(Color.WHITE);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(balanceLabel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // --- Center Panel (Table + Graph) ---
        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        centerPanel.setBackground(bgColor);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 1. Market Table Styling
        String[] columns = {"Stock ID", "Symbol", "Company", "Current Price ($)", "Available Shares"};
        marketTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Prevent direct cell editing
            }
        };
        marketTable = new JTable(marketTableModel);
        styleTable(marketTable);
        JScrollPane scrollPane = new JScrollPane(marketTable);
        scrollPane.getViewport().setBackground(panelColor);
        scrollPane.setBorder(BorderFactory.createLineBorder(accentColor, 1));
        
        // 2. Graph Panel
        graphPanel = new GraphPanel();
        graphPanel.setBackground(panelColor);
        graphPanel.setBorder(BorderFactory.createLineBorder(accentColor, 1));

        centerPanel.add(scrollPane);
        centerPanel.add(graphPanel);
        add(centerPanel, BorderLayout.CENTER);

        // --- Bottom Buttons Panel ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(bgColor);

        JButton buyBtn = createStyledButton("🛒 Buy Selected Stock", new Color(40, 167, 69));
        JButton refreshBtn = createStyledButton("🔄 Refresh Market", accentColor);
        JButton simulateBtn = createStyledButton("📈 Simulate Market Trends", new Color(255, 193, 7));
        JButton viewPortfolioBtn = createStyledButton("💼 View Portfolio", new Color(111, 66, 193));

        buttonPanel.add(buyBtn);
        buttonPanel.add(refreshBtn);
        buttonPanel.add(simulateBtn);
        buttonPanel.add(viewPortfolioBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        // --- Action Listeners ---
        refreshBtn.addActionListener(e -> loadMarketData());
        buyBtn.addActionListener(e -> buySelectedStock());
        viewPortfolioBtn.addActionListener(e -> showPortfolio());
        simulateBtn.addActionListener(e -> simulateMarketTrends());

        // Initial Load
        loadMarketData();
        updateBalanceLabel();
    }

    // --- GUI STYLING METHODS ---
    private JButton createStyledButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void styleTable(JTable table) {
        table.setBackground(panelColor);
        table.setForeground(textColor);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(30);
        table.setSelectionBackground(accentColor);
        table.setSelectionForeground(Color.WHITE);
        table.setGridColor(new Color(60, 60, 60));

        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(20, 20, 20));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    // --- JDBC METHODS ---
    private void loadMarketData() {
        marketTableModel.setRowCount(0); 
        Map<String, Double> chartData = new LinkedHashMap<>();

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM stocks")) { // Table name lowercase matching schema

            while (rs.next()) {
                String symbol = rs.getString("symbol");
                double price = rs.getDouble("current_price");
                
                marketTableModel.addRow(new Object[]{
                        rs.getInt("stock_id"), 
                        symbol, 
                        rs.getString("company_name"),
                        price, 
                        rs.getDouble("available_shares") // SQL Column type is DOUBLE
                });
                
                chartData.put(symbol, price); // Collecting data for the graph
            }
            graphPanel.updateData(chartData); // Update graph visualization
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage());
        }
    }

    private void updateBalanceLabel() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement("SELECT balance FROM users WHERE user_id = ?")) {
            pstmt.setInt(1, currentUserId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                balanceLabel.setText("Balance: $" + String.format("%.2f", rs.getDouble("balance")));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Function to randomly fluctuate market prices up or down
    private void simulateMarketTrends() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement()) {
            // Changes price randomly between -5% and +5%
            stmt.executeUpdate("UPDATE stocks SET current_price = ROUND(current_price * (1 + (RAND() * 0.10 - 0.05)), 2)");
            loadMarketData();
            JOptionPane.showMessageDialog(this, "Market prices have fluctuated!", "Market Update", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void buySelectedStock() {
        int selectedRow = marketTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a stock from the table first!");
            return;
        }

        int stockId = (int) marketTableModel.getValueAt(selectedRow, 0);
        String symbol = (String) marketTableModel.getValueAt(selectedRow, 1);
        double price = (double) marketTableModel.getValueAt(selectedRow, 3);
        double available = (double) marketTableModel.getValueAt(selectedRow, 4);

        String input = JOptionPane.showInputDialog(this, "How many shares of " + symbol + " do you want to buy?");
        if (input == null || input.trim().isEmpty()) return;

        try {
            int qty = Integer.parseInt(input);
            if (qty <= 0) throw new NumberFormatException();
            if (qty > available) {
                JOptionPane.showMessageDialog(this, "Not enough shares available in the market!");
                return;
            }

            double totalCost = price * qty;

            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                PreparedStatement balCheck = conn.prepareStatement("SELECT balance FROM users WHERE user_id = ?");
                balCheck.setInt(1, currentUserId);
                ResultSet rs = balCheck.executeQuery();
                
                if (rs.next()) {
                    if (totalCost > rs.getDouble("balance")) {
                        JOptionPane.showMessageDialog(this, "Insufficient Balance! Total Cost: $" + String.format("%.2f", totalCost));
                        return;
                    }

                    conn.setAutoCommit(false); 
                    try {
                        // 1. Deduct User Balance
                        PreparedStatement updateBal = conn.prepareStatement("UPDATE users SET balance = balance - ? WHERE user_id = ?");
                        updateBal.setDouble(1, totalCost);
                        updateBal.setInt(2, currentUserId);
                        updateBal.executeUpdate();

                        // 2. Reduce Stock Inventory
                        PreparedStatement updateStock = conn.prepareStatement("UPDATE stocks SET available_shares = available_shares - ? WHERE stock_id = ?");
                        updateStock.setInt(1, qty);
                        updateStock.setInt(2, stockId);
                        updateStock.executeUpdate();

                        // 3. Update Portfolio Holdings (Column names aligned with SQL schema)
                        PreparedStatement addHolding = conn.prepareStatement(
                                "INSERT INTO holdings (user_id, stock_id, quantity, avg_price) VALUES (?, ?, ?, ?)");
                        addHolding.setInt(1, currentUserId);
                        addHolding.setInt(2, stockId);
                        addHolding.setInt(3, qty);
                        addHolding.setDouble(4, price);
                        addHolding.executeUpdate();

                        // 4. Record Transaction Log
                        PreparedStatement addTxn = conn.prepareStatement(
                                "INSERT INTO transactions (user_id, stock_id, type, quantity, price, total) VALUES (?, ?, 'BUY', ?, ?, ?)");
                        addTxn.setInt(1, currentUserId);
                        addTxn.setInt(2, stockId);
                        addTxn.setInt(3, qty);
                        addTxn.setDouble(4, price);
                        addTxn.setDouble(5, totalCost);
                        addTxn.executeUpdate();

                        conn.commit(); 
                        JOptionPane.showMessageDialog(this, "Congratulations! You have successfully purchased " + qty + " shares of " + symbol + ".");
                        
                        loadMarketData();
                        updateBalanceLabel();
                    } catch (SQLException innerEx) {
                        conn.rollback(); // Rollback in case of database transaction error
                        throw innerEx;
                    }
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid positive integer!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Transaction Failed: " + ex.getMessage());
        }
    }

    private void showPortfolio() {
        StringBuilder portfolioStr = new StringBuilder("=== YOUR PORTFOLIO ===\n\n");
        // Matches exact column constraints in MySQL schema (s.company_name, h.user_id, etc)
        String query = "SELECT s.symbol, s.company_name, h.quantity, h.avg_price " +
                       "FROM holdings h JOIN stocks s ON h.stock_id = s.stock_id WHERE h.user_id = ?";
                       
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, currentUserId);
            ResultSet rs = pstmt.executeQuery();
            boolean hasData = false;
            double totalInvested = 0;

            while (rs.next()) {
                hasData = true;
                double cost = rs.getDouble("avg_price") * rs.getInt("quantity");
                totalInvested += cost;
                portfolioStr.append("📈 ").append(rs.getString("symbol")).append(" (").append(rs.getString("company_name")).append(")\n")
                            .append("   Quantity: ").append(rs.getInt("quantity")).append("\n")
                            .append("   Avg Price: $").append(String.format("%.2f", rs.getDouble("avg_price"))).append("\n")
                            .append("----------------------------\n");
            }

            if (!hasData) {
                portfolioStr.append("Your portfolio is empty.");
            } else {
                portfolioStr.append("\n💰 Total Invested: $").append(String.format("%.2f", totalInvested));
            }
            
            JOptionPane.showMessageDialog(this, portfolioStr.toString(), "Portfolio Overview", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error fetching portfolio: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StockTradingApp().setVisible(true));
    }
}

// --- CUSTOM GRAPH PANEL CLASS ---
class GraphPanel extends JPanel {
    private Map<String, Double> data = new LinkedHashMap<>();

    public void updateData(Map<String, Double> newData) {
        this.data = newData;
        repaint(); // Forces chart panel to repaint when new data arrives
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (data == null || data.isEmpty()) return;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int padding = 40;
        int barWidth = 60;
        int spacing = (width - (2 * padding) - (data.size() * barWidth)) / (data.size() + 1);

        double maxPrice = 0;
        for (double price : data.values()) {
            if (price > maxPrice) maxPrice = price;
        }

        // Draw Axes
        g2d.setColor(Color.WHITE);
        g2d.drawLine(padding, height - padding, width - padding, height - padding); // X-Axis
        g2d.drawLine(padding, padding, padding, height - padding); // Y-Axis

        int x = padding + spacing;
        for (Map.Entry<String, Double> entry : data.entrySet()) {
            String symbol = entry.getKey();
            double price = entry.getValue();

            int barHeight = (int) ((price / maxPrice) * (height - 2 * padding - 20));
            int y = height - padding - barHeight;

            // Gradient Fill configuration
            GradientPaint gradient = new GradientPaint(x, y, new Color(0, 204, 102), x, height - padding, new Color(0, 102, 51));
            g2d.setPaint(gradient);
            g2d.fillRect(x, y, barWidth, barHeight);

            g2d.setColor(Color.GREEN);
            g2d.drawRect(x, y, barWidth, barHeight);

            // Labels
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Segoe UI", Font.BOLD, 12));
            g2d.drawString(symbol, x + 10, height - padding + 20);

            // Price tags
            g2d.setColor(new Color(255, 215, 0)); 
            g2d.drawString("$" + String.format("%.1f", price), x, y - 10);

            x += barWidth + spacing;
        }
        
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        g2d.drawString("Live Stock Prices Visualization", width / 2 - 100, 25);
    }
}