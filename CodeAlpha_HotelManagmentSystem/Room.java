import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.sql.*;
import java.util.ArrayList;

// ===================== DATABASE CONNECTION =====================
class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/hotel_db";
    private static final String USER = "root";
    private static final String PASSWORD = "1234"; // <-- Change this

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

// ===================== ROOM CLASS =====================
class Room {
    private String roomId, category;
    private double price;
    private boolean availability;

    public Room(String roomId, String category, double price, boolean availability) {
        this.roomId = roomId;
        this.category = category;
        this.price = price; 
        this.availability = availability;
    }
    public String getRoomId(){ 
        return roomId; 
    }
    public String getCategory(){ 
        return category; 
    }
    public double getPrice(){
     return price;
      }
    public boolean getAvailability(){ 
        return availability; 
    }
}

// ===================== CUSTOMER CLASS =====================
class Customer {
    private String custId, name, phone, email;

    public Customer(String custId, String name, String phone, String email) {
        this.custId = custId; this.name = name;
        this.phone = phone; this.email = email;
    }
    public String getId() { 
        return custId; 
    }
    public String getName(){
     return name;
      }
    public String getPhone(){ 
        return phone; 
    }
    public String getEmail() {
     return email; 
 }
}

// ===================== RESERVATION CLASS =====================
class Reservation {
    private String resId, checkIn, checkOut;
    private String customerId, roomId, customerName;
    private boolean paymentStatus;

    public Reservation(String resId, String customerId, String customerName,
                       String roomId, String checkIn, String checkOut, boolean paymentStatus) {
        this.resId = resId;
        this.customerId = customerId;
        this.customerName = customerName; 
        this.roomId = roomId;
        this.checkIn = checkIn; 
        this.checkOut = checkOut;
        this.paymentStatus = paymentStatus;
    }
    public String getResId() { 
        return resId;
         }
    public String getCustomerId() { 
        return customerId; 
    }
    public String getCustomerName() { 
        return customerName; 
    }
    public String getRoomId(){ 
        return roomId;
    }
    public String getCheckIn() {
     return checkIn; 
    }
    public String getCheckOut(){ 
        return checkOut; 
    }
    public boolean getPaymentStatus(){
     return paymentStatus; 
 }
}

// ===================== HOTEL DAO =====================
class HotelDAO {
    public ArrayList<Room> getAllRooms() {
        ArrayList<Room> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM rooms")) {
            while (rs.next())
                list.add(new Room(rs.getString("room_id"), rs.getString("category"),
                        rs.getDouble("price"), rs.getBoolean("availability")));
        } catch (SQLException e) { showError(e); }
        return list;
    }

    public ArrayList<Customer> getAllCustomers() {
        ArrayList<Customer> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM customers")) {
            while (rs.next())
                list.add(new Customer(rs.getString("customer_id"), rs.getString("name"),
                        rs.getString("phone"), rs.getString("email")));
        } catch (SQLException e) { showError(e); }
        return list;
    }

    public ArrayList<Reservation> getAllReservations() {
        ArrayList<Reservation> list = new ArrayList<>();
        String sql = "SELECT r.*, c.name FROM reservations r JOIN customers c ON r.customer_id = c.customer_id";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next())
                list.add(new Reservation(rs.getString("reservation_id"),
                        rs.getString("customer_id"), rs.getString("name"),
                        rs.getString("room_id"), rs.getString("check_in_date"),
                        rs.getString("check_out_date"), rs.getBoolean("payment_status")));
        } catch (SQLException e) { showError(e); }
        return list;
    }

    public String bookRoom(String roomId, String custId, String in, String out,
                           String resId, boolean payment) {
        try (Connection con = DBConnection.getConnection()) {
            // Check availability
            PreparedStatement check = con.prepareStatement(
                    "SELECT availability FROM rooms WHERE room_id=?");
            check.setString(1, roomId);
            ResultSet rs = check.executeQuery();
            if (!rs.next()) return "Room not found!";
            if (!rs.getBoolean("availability")) return "Room already booked!";

            // Check customer
            PreparedStatement cc = con.prepareStatement(
                    "SELECT customer_id FROM customers WHERE customer_id=?");
            cc.setString(1, custId);
            ResultSet rs2 = cc.executeQuery();
            if (!rs2.next()) return "Customer not found!";

            // Insert reservation
            PreparedStatement ins = con.prepareStatement(
                    "INSERT INTO reservations VALUES (?,?,?,?,?,?)");
            ins.setString(1, resId); ins.setString(2, custId);
            ins.setString(3, roomId); ins.setString(4, in);
            ins.setString(5, out); ins.setBoolean(6, payment);
            ins.executeUpdate();

            // Update room
            PreparedStatement upd = con.prepareStatement(
                    "UPDATE rooms SET availability=false WHERE room_id=?");
            upd.setString(1, roomId);
            upd.executeUpdate();
            return "SUCCESS";
        } catch (SQLException e) { return "Error: " + e.getMessage(); }
    }

    public String cancelBooking(String roomId) {
        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement check = con.prepareStatement(
                    "SELECT availability FROM rooms WHERE room_id=?");
            check.setString(1, roomId);
            ResultSet rs = check.executeQuery();
            if (!rs.next()) return "Room not found!";
            if (rs.getBoolean("availability")) return "Room is not booked!";

            PreparedStatement upd = con.prepareStatement(
                    "UPDATE rooms SET availability=true WHERE room_id=?");
            upd.setString(1, roomId);
            upd.executeUpdate();

            PreparedStatement del = con.prepareStatement(
                    "DELETE FROM reservations WHERE room_id=?");
            del.setString(1, roomId);
            del.executeUpdate();
            return "SUCCESS";
        } catch (SQLException e) { return "Error: " + e.getMessage(); }
    }

    public ArrayList<Room> searchByCategory(String category) {
        ArrayList<Room> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT * FROM rooms WHERE category=?")) {
            ps.setString(1, category);
            ResultSet rs = ps.executeQuery();
            while (rs.next())
                list.add(new Room(rs.getString("room_id"), rs.getString("category"),
                        rs.getDouble("price"), rs.getBoolean("availability")));
        } catch (SQLException e) { showError(e); }
        return list;
    }

    public String addCustomer(String custId, String name, String phone, String email) {
        try (Connection con = DBConnection.getConnection()) {
            // Check duplicate
            PreparedStatement chk = con.prepareStatement(
                    "SELECT customer_id FROM customers WHERE customer_id=?");
            chk.setString(1, custId);
            ResultSet rs = chk.executeQuery();
            if (rs.next()) return "Customer ID already exists!";

            PreparedStatement ins = con.prepareStatement(
                    "INSERT INTO customers VALUES (?,?,?,?)");
            ins.setString(1, custId); ins.setString(2, name);
            ins.setString(3, phone);  ins.setString(4, email);
            ins.executeUpdate();
            return "SUCCESS";
        } catch (SQLException e) { return "Error: " + e.getMessage(); }
    }

    private void showError(SQLException e) {
        JOptionPane.showMessageDialog(null, "DB Error: " + e.getMessage(),
                "Database Error", JOptionPane.ERROR_MESSAGE);
    }
}

// ===================== MAIN GUI =====================
 class HotelManagementSystem extends JFrame {

    // Color palette — deep navy luxury
    private static final Color BG_DARK      = new Color(10, 14, 26);
    private static final Color BG_CARD      = new Color(18, 24, 42);
    private static final Color BG_SIDEBAR   = new Color(13, 18, 35);
    private static final Color GOLD         = new Color(212, 175, 55);
    private static final Color GOLD_LIGHT   = new Color(240, 210, 100);
    private static final Color TEXT_WHITE   = new Color(240, 240, 255);
    private static final Color TEXT_MUTED   = new Color(140, 150, 180);
    private static final Color ACCENT_GREEN = new Color(52, 199, 89);
    private static final Color ACCENT_RED   = new Color(255, 69, 58);
    private static final Color BORDER_COLOR = new Color(40, 50, 80);

    private HotelDAO dao = new HotelDAO();
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private JLabel statusBar;

    public HotelManagementSystem() {
        setTitle("Grand Horizon Hotel — Management System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1200, 750);
        setMinimumSize(new Dimension(1000, 650));
        setLocationRelativeTo(null);
        setBackground(BG_DARK);

        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {}

        setLayout(new BorderLayout());
        add(buildSidebar(), BorderLayout.WEST);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(BG_DARK);
        contentPanel.add(buildDashboard(), "dashboard");
        contentPanel.add(buildRoomsPanel(), "rooms");
        contentPanel.add(buildCustomersPanel(), "customers");
        contentPanel.add(buildBookingPanel(), "booking");
        contentPanel.add(buildReservationsPanel(), "reservations");
        contentPanel.add(buildCancelPanel(), "cancel");
        contentPanel.add(buildAddCustomerPanel(), "addcustomer");
        add(contentPanel, BorderLayout.CENTER);

        statusBar = new JLabel("  ✦  Connected to Grand Horizon Hotel System");
        statusBar.setForeground(TEXT_MUTED);
        statusBar.setFont(new Font("Serif", Font.ITALIC, 12));
        statusBar.setBackground(new Color(8, 12, 22));
        statusBar.setOpaque(true);
        statusBar.setBorder(new EmptyBorder(6, 16, 6, 16));
        add(statusBar, BorderLayout.SOUTH);

        cardLayout.show(contentPanel, "dashboard");
    }

    // ---- SIDEBAR ----
    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, BG_SIDEBAR,
                        0, getHeight(), new Color(8, 12, 22));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                // Gold left border
                g2.setColor(GOLD);
                g2.fillRect(getWidth()-1, 0, 1, getHeight());
            }
        };
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(new EmptyBorder(0, 0, 0, 0));

        // Logo area
        JPanel logoPanel = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(20, 28, 50));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(GOLD);
                g2.setStroke(new BasicStroke(1f));
                g2.drawLine(20, getHeight()-1, getWidth()-20, getHeight()-1);
            }
        };
        logoPanel.setPreferredSize(new Dimension(220, 110));
        logoPanel.setMaximumSize(new Dimension(220, 110));
        logoPanel.setLayout(new BoxLayout(logoPanel, BoxLayout.Y_AXIS));
        logoPanel.setBorder(new EmptyBorder(20, 20, 16, 20));

        JLabel hotelIcon = new JLabel("⌂", SwingConstants.CENTER);
        hotelIcon.setFont(new Font("Serif", Font.PLAIN, 32));
        hotelIcon.setForeground(GOLD);
        hotelIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel hotelName = new JLabel("GRAND HORIZON");
        hotelName.setFont(new Font("Serif", Font.BOLD, 14));
        hotelName.setForeground(TEXT_WHITE);
        hotelName.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel hotelSub = new JLabel("Hotel Management");
        hotelSub.setFont(new Font("Serif", Font.ITALIC, 11));
        hotelSub.setForeground(GOLD);
        hotelSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        logoPanel.add(hotelIcon);
        logoPanel.add(Box.createVerticalStrut(4));
        logoPanel.add(hotelName);
        logoPanel.add(Box.createVerticalStrut(2));
        logoPanel.add(hotelSub);
        sidebar.add(logoPanel);
        sidebar.add(Box.createVerticalStrut(10));

        String[][] menuItems = {
            {"🏠", "Dashboard", "dashboard"},
            {"🛏", "All Rooms", "rooms"},
            {"👤", "Customers", "customers"},
            {"➕", "Add Customer", "addcustomer"},
            {"📋", "Book Room", "booking"},
            {"📑", "Reservations", "reservations"},
            {"❌", "Cancel Booking", "cancel"}
        };

        ButtonGroup bg = new ButtonGroup();
        for (String[] item : menuItems) {
            JToggleButton btn = createNavButton(item[0], item[1], item[2]);
            bg.add(btn);
            sidebar.add(btn);
            sidebar.add(Box.createVerticalStrut(2));
        }

        sidebar.add(Box.createVerticalGlue());

        JLabel version = new JLabel("v2.0  •  JDBC Edition");
        version.setFont(new Font("Monospaced", Font.PLAIN, 10));
        version.setForeground(new Color(80, 90, 120));
        version.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(version);
        sidebar.add(Box.createVerticalStrut(16));

        return sidebar;
    }

    private JToggleButton createNavButton(String icon, String label, String card) {
        JToggleButton btn = new JToggleButton(icon + "  " + label) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (isSelected()) {
                    g2.setColor(new Color(30, 40, 70));
                    g2.fillRoundRect(8, 2, getWidth()-16, getHeight()-4, 8, 8);
                    g2.setColor(GOLD);
                    g2.fillRoundRect(8, 2, 3, getHeight()-4, 3, 3);
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(25, 33, 58));
                    g2.fillRoundRect(8, 2, getWidth()-16, getHeight()-4, 8, 8);
                }
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Serif", Font.PLAIN, 13));
        btn.setForeground(TEXT_MUTED);
        btn.setBackground(new Color(0,0,0,0));
        btn.setOpaque(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(220, 44));
        btn.setPreferredSize(new Dimension(220, 44));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(new EmptyBorder(0, 20, 0, 0));

        btn.addActionListener(e -> {
            btn.setForeground(GOLD_LIGHT);
            cardLayout.show(contentPanel, card);
            if (card.equals("rooms")) refreshRoomsTable();
            if (card.equals("customers")) refreshCustomersTable();
            if (card.equals("reservations")) refreshReservationsTable();
            if (card.equals("addcustomer")) refreshCustomersTable();
            setStatus("Viewing: " + label);
        });

        btn.addChangeListener(e -> {
            if (!btn.isSelected()) btn.setForeground(TEXT_MUTED);
        });
        return btn;
    }

    // ---- DASHBOARD ----
    private JPanel buildDashboard() {
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(BG_DARK);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel title = new JLabel("Welcome Back  ✦");
        title.setFont(new Font("Serif", Font.BOLD, 28));
        title.setForeground(TEXT_WHITE);
        JLabel subtitle = new JLabel("Grand Horizon Hotel — Management Overview");
        subtitle.setFont(new Font("Serif", Font.ITALIC, 14));
        subtitle.setForeground(GOLD);
        header.add(title, BorderLayout.NORTH);
        header.add(subtitle, BorderLayout.CENTER);
        panel.add(header, BorderLayout.NORTH);

        JPanel cardsPanel = new JPanel(new GridLayout(2, 3, 16, 16));
        cardsPanel.setOpaque(false);
        cardsPanel.setBorder(new EmptyBorder(24, 0, 0, 0));

        String[][] cards = {
            {"🛏", "Total Rooms", "5", GOLD.toString()},
            {"✅", "Available", "—", ACCENT_GREEN.toString()},
            {"🔴", "Booked", "—", ACCENT_RED.toString()},
            {"👤", "Customers", "5", "#8888ff"},
            {"📋", "Reservations", "—", "#ff9955"},
            {"💰", "Revenue", "—", GOLD.toString()}
        };

        for (String[] c : cards) {
            cardsPanel.add(createStatCard(c[0], c[1], c[2]));
        }

        panel.add(cardsPanel, BorderLayout.CENTER);
        refreshDashboardCards(cardsPanel);
        return panel;
    }

    private void refreshDashboardCards(JPanel cardsPanel) {
        ArrayList<Room> rooms = dao.getAllRooms();
        ArrayList<Reservation> reservations = dao.getAllReservations();
        long available = rooms.stream().filter(Room::getAvailability).count();
        long booked = rooms.size() - available;
        double revenue = reservations.stream()
            .mapToDouble(r -> {
                for (Room rm : rooms) if (rm.getRoomId().equals(r.getRoomId())) return rm.getPrice();
                return 0;
            }).sum();

        Component[] comps = cardsPanel.getComponents();
        updateStatCard((JPanel) comps[1], "✅", "Available", String.valueOf(available));
        updateStatCard((JPanel) comps[2], "🔴", "Booked", String.valueOf(booked));
        updateStatCard((JPanel) comps[4], "📋", "Reservations", String.valueOf(reservations.size()));
        updateStatCard((JPanel) comps[5], "💰", "Revenue", "PKR " + (int)revenue);
    }

    private JPanel createStatCard(String icon, String label, String value) {
        JPanel card = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BG_CARD);
                g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 14, 14);
                g2.setColor(BORDER_COLOR);
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 14, 14);
                // Gold top accent
                g2.setColor(GOLD);
                g2.setStroke(new BasicStroke(2f));
                g2.drawLine(20, 1, getWidth()-20, 1);
            }
        };
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        iconLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel valLabel = new JLabel(value);
        valLabel.setFont(new Font("Serif", Font.BOLD, 26));
        valLabel.setForeground(TEXT_WHITE);
        valLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        valLabel.setName("value");

        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(new Font("Serif", Font.PLAIN, 12));
        lblLabel.setForeground(TEXT_MUTED);
        lblLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(iconLabel);
        card.add(Box.createVerticalStrut(8));
        card.add(valLabel);
        card.add(Box.createVerticalStrut(2));
        card.add(lblLabel);
        return card;
    }

    private void updateStatCard(JPanel card, String icon, String label, String value) {
        for (Component c : card.getComponents()) {
            if (c instanceof JLabel && ((JLabel)c).getName() != null
                    && ((JLabel)c).getName().equals("value")) {
                ((JLabel)c).setText(value);
            }
        }
    }

    // ---- ROOMS TABLE ----
    private DefaultTableModel roomsModel;
    private JPanel buildRoomsPanel() {
        JPanel panel = createContentPanel("🛏  Room Inventory", "All rooms and their current status");
        String[] cols = {"Room ID", "Category", "Price (PKR)", "Status"};
        roomsModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = createStyledTable(roomsModel);
        table.getColumnModel().getColumn(3).setCellRenderer(new StatusCellRenderer());
        panel.add(new JScrollPane(table) {{
            setOpaque(false); getViewport().setBackground(BG_CARD);
            setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        }}, BorderLayout.CENTER);

        // Search bar
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        searchPanel.setOpaque(false);
        JLabel lbl = styledLabel("Filter by Category:");
        String[] cats = {"All", "Suite", "Deluxe", "Standard", "Economy"};
        JComboBox<String> combo = styledCombo(cats);
        JButton filterBtn = goldButton("Filter");
        filterBtn.addActionListener(e -> {
            String sel = (String) combo.getSelectedItem();
            refreshRoomsTable("All".equals(sel) ? null : sel);
        });
        searchPanel.add(lbl); searchPanel.add(combo); searchPanel.add(filterBtn);
        panel.add(searchPanel, BorderLayout.SOUTH);
        return panel;
    }

    private void refreshRoomsTable() { refreshRoomsTable(null); }
    private void refreshRoomsTable(String category) {
        roomsModel.setRowCount(0);
        ArrayList<Room> rooms = category == null ? dao.getAllRooms() : dao.searchByCategory(category);
        for (Room r : rooms)
            roomsModel.addRow(new Object[]{r.getRoomId(), r.getCategory(),
                    "PKR " + (int)r.getPrice(), r.getAvailability() ? "Available" : "Booked"});
    }

    // ---- CUSTOMERS TABLE ----
    private DefaultTableModel customersModel;
    private JPanel buildCustomersPanel() {
        JPanel panel = createContentPanel("👤  Guest Registry", "All registered hotel guests");
        String[] cols = {"Customer ID", "Name", "Phone", "Email"};
        customersModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = createStyledTable(customersModel);
        panel.add(new JScrollPane(table) {{
            setOpaque(false); getViewport().setBackground(BG_CARD);
            setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        }}, BorderLayout.CENTER);
        return panel;
    }

    private void refreshCustomersTable() {
        customersModel.setRowCount(0);
        for (Customer c : dao.getAllCustomers())
            customersModel.addRow(new Object[]{c.getId(), c.getName(), c.getPhone(), c.getEmail()});
    }

    // ---- BOOKING PANEL ----
    private JPanel buildBookingPanel() {
        JPanel panel = createContentPanel("📋  Book a Room", "Create a new room reservation");

        JPanel formCard = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BG_CARD);
                g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 16, 16);
                g2.setColor(BORDER_COLOR);
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 16, 16);
            }
        };
        formCard.setOpaque(false);
        formCard.setLayout(new GridBagLayout());
        formCard.setBorder(new EmptyBorder(30, 40, 30, 40));

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(8, 8, 8, 8);
        gc.fill = GridBagConstraints.HORIZONTAL;

        String[] labels = {"Reservation ID:", "Room ID:", "Customer ID:",
                "Check-In Date:", "Check-Out Date:", "Payment Status:"};
        JTextField[] fields = new JTextField[5];
        JCheckBox payCheck = new JCheckBox("Paid");
        payCheck.setForeground(TEXT_WHITE);
        payCheck.setOpaque(false);
        payCheck.setFont(new Font("Serif", Font.PLAIN, 13));

        for (int i = 0; i < labels.length; i++) {
            gc.gridx = 0; gc.gridy = i; gc.weightx = 0.3;
            JLabel lbl = styledLabel(labels[i]);
            formCard.add(lbl, gc);
            gc.gridx = 1; gc.weightx = 0.7;
            if (i < 5) {
                fields[i] = styledTextField(i == 3 ? "e.g. 2026-06-01" : i == 4 ? "e.g. 2026-06-05" : "");
                formCard.add(fields[i], gc);
            } else {
                formCard.add(payCheck, gc);
            }
        }

        gc.gridx = 0; gc.gridy = 6; gc.gridwidth = 2; gc.weightx = 1;
        JButton bookBtn = goldButton("✦  Confirm Booking");
        bookBtn.addActionListener(e -> {
            String result = dao.bookRoom(
                    fields[1].getText().trim(), fields[2].getText().trim(),
                    fields[3].getText().trim(), fields[4].getText().trim(),
                    fields[0].getText().trim(), payCheck.isSelected());
            if ("SUCCESS".equals(result)) {
                showSuccess("Room booked successfully!");
                for (JTextField f : fields) f.setText("");
                payCheck.setSelected(false);
                setStatus("✦ Room booked successfully");
            } else {
                showError(result);
            }
        });
        formCard.add(bookBtn, gc);

        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        wrapper.setOpaque(false);
        wrapper.add(formCard);
        panel.add(wrapper, BorderLayout.CENTER);
        return panel;
    }

    // ---- RESERVATIONS TABLE ----
    private DefaultTableModel reservationsModel;
    private JPanel buildReservationsPanel() {
        JPanel panel = createContentPanel("📑  Reservations", "All active and past reservations");
        String[] cols = {"Res. ID", "Customer ID", "Customer Name", "Room ID", "Check-In", "Check-Out", "Payment"};
        reservationsModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = createStyledTable(reservationsModel);
        table.getColumnModel().getColumn(6).setCellRenderer(new StatusCellRenderer());
        panel.add(new JScrollPane(table) {{
            setOpaque(false); getViewport().setBackground(BG_CARD);
            setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        }}, BorderLayout.CENTER);
        return panel;
    }

    private void refreshReservationsTable() {
        reservationsModel.setRowCount(0);
        for (Reservation r : dao.getAllReservations())
            reservationsModel.addRow(new Object[]{r.getResId(), r.getCustomerId(),
                    r.getCustomerName(), r.getRoomId(), r.getCheckIn(),
                    r.getCheckOut(), r.getPaymentStatus() ? "Paid" : "Unpaid"});
    }

    // ---- CANCEL PANEL ----
    private JPanel buildCancelPanel() {
        JPanel panel = createContentPanel("❌  Cancel Booking", "Release a room by its Room ID");
        JPanel formCard = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BG_CARD);
                g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 16, 16);
                g2.setColor(BORDER_COLOR);
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 16, 16);
            }
        };
        formCard.setOpaque(false);
        formCard.setLayout(new GridBagLayout());
        formCard.setBorder(new EmptyBorder(40, 50, 40, 50));
        formCard.setPreferredSize(new Dimension(600, 260));

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(14, 10, 14, 10);
        gc.fill = GridBagConstraints.HORIZONTAL;

        gc.gridx = 0; gc.gridy = 0; gc.weightx = 0.15;
        formCard.add(styledLabel("Room ID to Cancel:"), gc);
        gc.gridx = 1; gc.weightx = 0.85;
        JTextField roomField = new JTextField(30) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(14, 20, 38));
                g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 8, 8);
                super.paintComponent(g);
                if (getText().isEmpty() && !isFocusOwner()) {
                    g2.setColor(new Color(100, 110, 140));
                    g2.setFont(getFont().deriveFont(Font.ITALIC));
                    g2.drawString("e.g. R1", 14, getHeight()/2 + 7);
                }
            }
        };
        roomField.setBackground(new Color(14, 20, 38));
        roomField.setForeground(new Color(240, 240, 255));
        roomField.setCaretColor(new Color(212, 175, 55));
        roomField.setFont(new Font("Serif", Font.BOLD, 20));
        roomField.setOpaque(false);
        roomField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(212, 175, 55), 1),
                new EmptyBorder(10, 14, 10, 14)));
        roomField.setMinimumSize(new Dimension(420, 54));
        roomField.setPreferredSize(new Dimension(420, 54));
        formCard.add(roomField, gc);

        gc.gridx = 0; gc.gridy = 1; gc.gridwidth = 2;
        JButton cancelBtn = new JButton("✦  Cancel Booking") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? ACCENT_RED.darker() : ACCENT_RED);
                g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 8, 8);
                super.paintComponent(g);
            }
        };
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setFont(new Font("Serif", Font.BOLD, 14));
        cancelBtn.setPreferredSize(new Dimension(200, 40));
        cancelBtn.setOpaque(false);
        cancelBtn.setContentAreaFilled(false);
        cancelBtn.setBorderPainted(false);
        cancelBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        cancelBtn.addActionListener(e -> {
            String roomId = roomField.getText().trim();
            if (roomId.isEmpty()) { showError("Please enter a Room ID"); return; }
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Cancel booking for Room " + roomId + "?",
                    "Confirm Cancellation", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                String result = dao.cancelBooking(roomId);
                if ("SUCCESS".equals(result)) {
                    showSuccess("Booking cancelled for Room " + roomId);
                    roomField.setText("");
                    setStatus("✦ Booking cancelled: Room " + roomId);
                } else showError(result);
            }
        });
        formCard.add(cancelBtn, gc);

        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        wrapper.setOpaque(false);
        wrapper.add(formCard);
        panel.add(wrapper, BorderLayout.CENTER);
        return panel;
    }

    // ---- ADD CUSTOMER PANEL ----
    private JPanel buildAddCustomerPanel() {
        JPanel panel = createContentPanel("➕  Add New Customer", "Register a new guest in the system");

        JPanel formCard = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BG_CARD);
                g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 16, 16);
                g2.setColor(BORDER_COLOR);
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 16, 16);
            }
        };
        formCard.setOpaque(false);
        formCard.setLayout(new GridBagLayout());
        formCard.setBorder(new EmptyBorder(35, 50, 35, 50));

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(10, 10, 10, 10);
        gc.fill = GridBagConstraints.HORIZONTAL;

        String[] labels = {"Customer ID:", "Full Name:", "Phone Number:", "Email Address:"};
        String[] hints  = {"e.g. C6", "e.g. Yasir Ali", "e.g. 03001234567", "e.g. yasir@gmail.com"};
        JTextField[] fields = new JTextField[4];

        for (int i = 0; i < labels.length; i++) {
            gc.gridx = 0; gc.gridy = i; gc.weightx = 0.3;
            formCard.add(styledLabel(labels[i]), gc);
            gc.gridx = 1; gc.weightx = 0.7;
            fields[i] = styledTextField(hints[i]);
            fields[i].setPreferredSize(new Dimension(340, 42));
            fields[i].setFont(new Font("Serif", Font.PLAIN, 15));
            formCard.add(fields[i], gc);
        }

        gc.gridx = 0; gc.gridy = 4; gc.gridwidth = 2; gc.weightx = 1;
        gc.insets = new Insets(20, 10, 5, 10);
        JButton addBtn = goldButton("➕  Add Customer");
        addBtn.addActionListener(e -> {
            String id    = fields[0].getText().trim();
            String name  = fields[1].getText().trim();
            String phone = fields[2].getText().trim();
            String email = fields[3].getText().trim();

            if (id.isEmpty() || name.isEmpty() || phone.isEmpty() || email.isEmpty()) {
                showError("Please fill all fields!");
                return;
            }
            String result = dao.addCustomer(id, name, phone, email);
            if ("SUCCESS".equals(result)) {
                showSuccess("Customer \"" + name + "\" added successfully!");
                for (JTextField f : fields) f.setText("");
                setStatus("✦ New customer added: " + name);
            } else {
                showError(result);
            }
        });
        formCard.add(addBtn, gc);

        // Live customer count label
        gc.gridy = 5; gc.insets = new Insets(4, 10, 5, 10);
        JLabel hint = new JLabel("Tip: After adding, go to Customers tab to verify.");
        hint.setFont(new Font("Serif", Font.ITALIC, 11));
        hint.setForeground(TEXT_MUTED);
        hint.setHorizontalAlignment(SwingConstants.CENTER);
        formCard.add(hint, gc);

        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        wrapper.setOpaque(false);
        wrapper.add(formCard);
        panel.add(wrapper, BorderLayout.CENTER);
        return panel;
    }

    // ---- HELPERS ----
    private JPanel createContentPanel(String title, String subtitle) {
        JPanel panel = new JPanel(new BorderLayout(0, 16)) {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(BG_DARK); g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setBorder(new EmptyBorder(28, 28, 28, 28));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new MatteBorder(0, 0, 1, 0, BORDER_COLOR));
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(0, 0, 1, 0, BORDER_COLOR), new EmptyBorder(0, 0, 12, 0)));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 22));
        titleLabel.setForeground(TEXT_WHITE);
        JLabel subLabel = new JLabel(subtitle);
        subLabel.setFont(new Font("Serif", Font.ITALIC, 12));
        subLabel.setForeground(GOLD);
        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(subLabel, BorderLayout.CENTER);
        panel.add(headerPanel, BorderLayout.NORTH);
        return panel;
    }

    private JTable createStyledTable(DefaultTableModel model) {
        JTable table = new JTable(model) {
            @Override public Component prepareRenderer(TableCellRenderer r, int row, int col) {
                Component c = super.prepareRenderer(r, row, col);
                c.setBackground(row % 2 == 0 ? BG_CARD : new Color(22, 30, 52));
                c.setForeground(TEXT_WHITE);
                if (isRowSelected(row)) c.setBackground(new Color(40, 55, 90));
                return c;
            }
        };
        table.setBackground(BG_CARD);
        table.setForeground(TEXT_WHITE);
        table.setFont(new Font("Serif", Font.PLAIN, 13));
        table.setRowHeight(36);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setSelectionBackground(new Color(40, 55, 90));
        table.setSelectionForeground(GOLD_LIGHT);
        table.getTableHeader().setBackground(new Color(20, 28, 50));
        table.getTableHeader().setForeground(GOLD);
        table.getTableHeader().setFont(new Font("Serif", Font.BOLD, 13));
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, GOLD));
        return table;
    }

    private JLabel styledLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Serif", Font.PLAIN, 13));
        lbl.setForeground(TEXT_MUTED);
        return lbl;
    }

    private JTextField styledTextField(String placeholder) {
        JTextField field = new JTextField(18) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(14, 20, 38));
                g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 8, 8);
                super.paintComponent(g);
                if (getText().isEmpty() && !isFocusOwner()) {
                    g2.setColor(new Color(80, 90, 120));
                    g2.setFont(getFont().deriveFont(Font.ITALIC));
                    g2.drawString(placeholder, 8, getHeight()/2 + 5);
                }
            }
        };
        field.setBackground(new Color(14, 20, 38));
        field.setForeground(TEXT_WHITE);
        field.setCaretColor(GOLD);
        field.setFont(new Font("Serif", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                new EmptyBorder(6, 10, 6, 10)));
        field.setOpaque(false);
        field.setPreferredSize(new Dimension(220, 36));
        return field;
    }

    private JComboBox<String> styledCombo(String[] items) {
        JComboBox<String> combo = new JComboBox<>(items);
        combo.setBackground(new Color(14, 20, 38));
        combo.setForeground(TEXT_WHITE);
        combo.setFont(new Font("Serif", Font.PLAIN, 13));
        combo.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        return combo;
    }

    private JButton goldButton(String text) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0,
                        getModel().isRollover() ? GOLD_LIGHT : GOLD,
                        0, getHeight(), new Color(160, 120, 20));
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 8, 8);
                super.paintComponent(g);
            }
        };
        btn.setForeground(new Color(20, 14, 2));
        btn.setFont(new Font("Serif", Font.BOLD, 14));
        btn.setPreferredSize(new Dimension(220, 42));
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void showSuccess(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void setStatus(String msg) {
        statusBar.setText("  ✦  " + msg);
    }

    // ---- STATUS CELL RENDERER ----
    class StatusCellRenderer extends DefaultTableCellRenderer {
        @Override public Component getTableCellRendererComponent(JTable t, Object v,
                boolean sel, boolean foc, int row, int col) {
            super.getTableCellRendererComponent(t, v, sel, foc, row, col);
            String val = v == null ? "" : v.toString();
            if ("Available".equals(val) || "Paid".equals(val)) {
                setForeground(ACCENT_GREEN);
                setText("● " + val);
            } else if ("Booked".equals(val) || "Unpaid".equals(val)) {
                setForeground(ACCENT_RED);
                setText("● " + val);
            } else {
                setForeground(TEXT_WHITE);
            }
            setBackground(row % 2 == 0 ? BG_CARD : new Color(22, 30, 52));
            setBorder(new EmptyBorder(0, 12, 0, 0));
            return this;
        }
    }

    // ---- MAIN ----
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            HotelManagementSystem frame = new HotelManagementSystem();
            frame.setVisible(true);
        });
    }
}