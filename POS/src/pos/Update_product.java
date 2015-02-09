package pos;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.sql.Date;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

public class Update_product extends javax.swing.JFrame {

    int cat3, ci;
    String q = null;
    int old_q = 0;
    boolean flag = true, flag1 = false, flag2 = false, flag3 = false;
    Connection conn;
    Statement stat;
    ResultSet res;
    String cat = null, cat2 = null, product_id = null, sql_report = null;

    connectDB cnnt = new connectDB();
    String[] seta = new String[50];
    String[] setb = new String[50];
    String[] setc = new String[50];
    String initial_table_query = "Select * from record"; //(product join buy_rec using(product_id)) join category using(cat_id)
    String sql_cat, date1 = null;
    Icon bc = new ImageIcon("barcode/" + product_id + ".png");

    public Update_product() {
        initComponents();
        setAlwaysOnTop(true);
        jTextField3.setEnabled(false);
        jComboBox1.setSelectedIndex(-1);

        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int width = d.width;
        int height = d.height;
        setSize(width, height);
//        System.out.println("Width = " + width + " height = " + height);

        cnnt.myConnect();
        Statement s1 = cnnt.getStatement();
        Statement s2 = cnnt.getStatement();
        Statement s3 = cnnt.getStatement();
        Statement s4 = cnnt.getStatement();

        ResultSet r1 = cnnt.getResultset();
        ResultSet r2 = cnnt.getResultset();
        ResultSet r3 = cnnt.getResultset();
        ResultSet r4 = cnnt.getResultset();

//        sql_cat = "Select `product_id`, `product_name`, `product_quantity`, `product_vendor`,"
//                + " `warranty_period`, `buying_price`, `selling_price`, `buying_date`,`category_main`, "
//                + "`category_sub` from (product join buy_rec using(product_id)) join category using(cat_id) "
//                + "WHERE category_sub like '"+ combo2 +"'";
        //Table();
        //for main-category combobox itemlist
        int i = 0;
        try {
            r1 = s1.executeQuery("select distinct category_main from category");

            while (r1.next()) {
                seta[i] = r1.getString("category_main");
//                System.out.println(seta[i]);
                i++;
            }
//            System.out.println("i = " + (i - 1));

        } catch (SQLException ex) {
            Logger.getLogger(Category.class.getName()).log(Level.SEVERE, null, ex);
        }

        //System.out.println("new " + set[0]);
        //System.out.println(set[1]);
        for (int j = 0; j < i; j++) {
            jComboBox1.addItem(seta[j]);
            jComboBox3.addItem(seta[j]);
        }

        seta = null;

        //for sub-category combobox itemlist
        cat = jComboBox1.getSelectedItem().toString();
        cat2 = jComboBox3.getSelectedItem().toString();
        i = 0;
        try {
            r2 = s2.executeQuery("select distinct category_sub from category where category_main like '" + cat + "'");

            while (r2.next()) {
                setb[i] = r2.getString("category_sub");
//                System.out.println(setb[i]);
                i++;
            }
//            System.out.println("i = " + (i - 1));
            cat = null;

        } catch (SQLException ex) {
            Logger.getLogger(Category.class.getName()).log(Level.SEVERE, null, ex);
        }
        jComboBox2.removeAllItems();
        jComboBox4.removeAllItems();
        //System.out.println("new " + set[0]);
        //System.out.println(set[1]);
        for (int j = 0; j < i; j++) {
            jComboBox2.addItem(setb[j]);
            jComboBox4.addItem(setb[j]);
        }
        jComboBox2.revalidate();
        jComboBox4.revalidate();

        setb = null;
        jComboBox5.removeAllItems();
        try {
            String com = "";
            r3 = s3.executeQuery("SELECT distinct product_vendor FROM record");

            // System.out.println("Test01 xjsryyyyyyyyyyyf");
            while (r3.next()) {

                com = r3.getString("product_vendor");
//                System.out.println("Test01 xjsryyyyyyyyyyyf"+com);
                jComboBox5.addItem(com);

            }
        } catch (SQLException ex) {
            //  Logger.getLogger(Update_product.class.getName()).log(Level.SEVERE, null, ex);
        }
        AutoCompleteDecorator.decorate(jComboBox5);
        jComboBox5.revalidate();

        try {
            String com;
            r4 = s4.executeQuery("SELECT distinct product_name FROM record");
//            System.out.println("Product Name = ");
            while (r4.next()) {
                com = r4.getString("product_name");
                jComboBox6.addItem(com);
//                System.out.println(com);
            }
            AutoCompleteDecorator.decorate(jComboBox6);
            jComboBox6.revalidate();
        } catch (SQLException ex) {
            Logger.getLogger(Update_product.class.getName()).log(Level.SEVERE, null, ex);
        }

        /*Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
         int width = d.width;
         int height = d.height;
         setSize(width, height);*/
        Table(initial_table_query);

    }

    public void Table(String query) {
        try {
            cnnt.myConnect();

            stat = cnnt.getStatement();
            res = cnnt.getResultset();

            res = stat.executeQuery(initial_table_query);

            DefaultTableModel dtm = new DefaultTableModel();

            dtm.setColumnIdentifiers(new String[]{"Product ID", "Product Name", "Quantity", "Company", "Invoice No", "Warranty", "Buying Price", "Selling Price", "Date", "Category", "Sub-Category"});

            int size = 0;
            while (res.next()) {
                size++;
            }

            dtm.setRowCount(size);

            ResultSet r1 = cnnt.getResultset();
            Statement s1 = cnnt.getStatement();

            r1 = s1.executeQuery(query);

            int i = 0;
            while (r1.next()) {

                dtm.setValueAt(r1.getString(2), i, 0);
                dtm.setValueAt(r1.getString(3), i, 1);
                dtm.setValueAt(r1.getInt(4), i, 2);
                dtm.setValueAt(r1.getString(5), i, 3);
                dtm.setValueAt(r1.getString(6), i, 4);
                dtm.setValueAt(r1.getInt(7), i, 5);
                dtm.setValueAt(r1.getInt(8), i, 6);
                dtm.setValueAt(r1.getInt(9), i, 7);
                dtm.setValueAt(r1.getDate(10), i, 8);
                dtm.setValueAt(r1.getString(11), i, 9);
                dtm.setValueAt(r1.getString(12), i, 10);
                i++;

            }
            jTable1.setModel(dtm);

        } catch (Exception e) {
//            System.out.println(e.toString());
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jTextField6 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jLabel9 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox();
        jLabel12 = new javax.swing.JLabel();
        jComboBox4 = new javax.swing.JComboBox();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        jTextField10 = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jComboBox6 = new javax.swing.JComboBox();
        jPanel3 = new javax.swing.JPanel();
        jComboBox2 = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jButton5 = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        jTextField9 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        jComboBox5 = new javax.swing.JComboBox();
        jLabel15 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Product ID", "Product Name", "Quantity", "Company", "Invoice No", "Warranty", "Buying Price", "Selling Price", "Date", "Category", "Sub-Category"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1DemoChange(evt);
            }
        });
        jTable1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTable1KeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Edit Record"));

        jLabel4.setText("Buying Price");

        jLabel5.setText("Quantity");

        jLabel1.setText("Product ID");

        jButton3.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jButton3.setText("UPDATE");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel6.setText("Date");

        jTextField3.setEditable(false);
        jTextField3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField3MouseClicked(evt);
            }
        });
        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });

        jLabel7.setText("Product Name");

        jLabel8.setText("Selling Price");

        jDateChooser1.setDateFormatString("yyyy-MM-dd");
        jDateChooser1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser1changeDate(evt);
            }
        });

        jLabel9.setText("Company");

        jLabel10.setText("Warranty Time");

        jLabel11.setText("Category");

        jComboBox3.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox3ItemStateChanged(evt);
            }
        });

        jLabel12.setText("Sub- category");

        jComboBox4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox4ActionPerformed(evt);
            }
        });

        jButton6.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jButton6.setText("PRINT");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jButton7.setText("DELETE");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jLabel17.setText("Invoice No.");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap(70, Short.MAX_VALUE)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel4))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTextField6)
                                    .addComponent(jTextField2, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)
                                    .addComponent(jTextField4, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)
                                    .addComponent(jTextField5, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel10)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel11)
                                    .addComponent(jLabel12)
                                    .addComponent(jLabel9)
                                    .addComponent(jLabel17))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTextField10)
                                    .addComponent(jDateChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jTextField7)
                                    .addComponent(jTextField8, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)
                                    .addComponent(jTextField3, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)
                                    .addComponent(jComboBox3, 0, 165, Short.MAX_VALUE)
                                    .addComponent(jComboBox4, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
                .addGap(30, 30, 30))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(24, 24, 24)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel7))
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 25, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel5))
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel4))
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel8))
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel9))
                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel17)
                    .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel10))
                    .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel11))
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel12))
                    .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel6))
                    .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jButton6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Search"));

        jButton1.setText("Search By");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jLabel16.setText("Product Name");

        jComboBox6.setEditable(true);
        jComboBox6.setAutoscrolls(true);
        jComboBox6.setFocusCycleRoot(true);
        jComboBox6.setInheritsPopupMenu(true);
        jComboBox6.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox6ItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(31, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16))
                .addContainerGap())
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Filter"));

        jComboBox2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox2ItemStateChanged(evt);
            }
        });

        jLabel2.setText("Category");

        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });

        jLabel3.setText("Sub-Category");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2))
                .addGap(39, 39, 39)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jButton4.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jButton4.setText("EXIT");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Add Quantity"));

        jButton5.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jButton5.setText("ADD");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel14.setText("Quantity");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel14)
                .addGap(18, 18, 18)
                .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jTextField9)
                .addComponent(jLabel14)
                .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE))
        );

        jButton2.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jButton2.setText("REFRESH Table");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel13.setText(" ");
        jLabel13.setMaximumSize(new java.awt.Dimension(3, 6));

        jComboBox5.setAutoscrolls(true);
        jComboBox5.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox5ItemStateChanged(evt);
            }
        });

        jLabel15.setText("Company");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(286, 286, 286))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(10, 10, 10)
                                        .addComponent(jLabel15)
                                        .addGap(67, 67, 67)
                                        .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 896, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)))))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(22, 22, 22))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jButton2)
                        .addGap(10, 10, 10)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(99, 99, 99))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(46, 46, 46))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel15)))
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18))))
        );

        jPanel2.getAccessibleContext().setAccessibleName("Search by Product ID");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTable1DemoChange(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1DemoChange
        int row = jTable1.getSelectedRow();
        int col = jTable1.getSelectedColumn();

//        for (int i = 0; i < jTable1.getColumnCount(); i++) {
//            String s = jTable1.getValueAt(row, i).toString();
////            System.out.println(s);
//        }

        product_id = jTable1.getValueAt(row, 0).toString();

        //System.out.println(row+" "+col);
//        System.out.println(product_id);
        //jLabel_stat.setText(product_id);
        String sql_upd = "Select `product_id`, `product_name`, `product_quantity`, `product_vendor`, `invoice_no`, "
                + " `warranty_period`, `buying_price`, `selling_price`, `buying_date`,`category_main`, "
                + "`category_sub` from (product join buy_rec using(product_id)) join category using(cat_id) "
                + "WHERE product_id= '" + product_id + "' ";

        Statement stat = cnnt.getStatement();
        ResultSet res = cnnt.getResultset();
        String hajari = "barcode/" + product_id + ".png";
        Icon bc = new ImageIcon(hajari);
        try {
            res = stat.executeQuery(sql_upd);

            while (res.next()) {
                //System.out.println(res.getString("product_name"));
                jTextField3.setText(res.getString("product_id"));
                jTextField4.setText(String.valueOf(res.getInt("product_quantity")));
                jTextField2.setText(res.getString("product_name"));
                jTextField6.setText(String.valueOf(res.getInt("buying_price")));
                jTextField5.setText(String.valueOf(res.getInt("selling_price")));
                jTextField7.setText(res.getString("product_vendor"));
                jTextField10.setText(res.getString("invoice_no"));
                jTextField8.setText(String.valueOf(res.getInt("warranty_period")));
                jComboBox3.setSelectedItem(res.getString("category_main"));
                jComboBox4.setSelectedItem(res.getString("category_sub"));
                jDateChooser1.setDate(res.getDate("buying_date"));
                jLabel13.setIcon(bc);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Update_product.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jTable1DemoChange

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            String id = jTextField1.getText();
            String sql;

            sql = "Select `product_id`, `product_name`, `product_quantity`, `product_vendor`, `invoice_no`, "
                    + " `warranty_period`, `buying_price`, `selling_price`, `buying_date`,`category_main`, "
                    + "`category_sub` from record " //(product join buy_rec using(product_id)) join category using(cat_id)
                    + "WHERE product_id = '" + id + "'";
            Table(sql);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }//GEN-LAST:event_jButton1ActionPerformed

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        // TODO add your handling code here:
        connectDB cn = new connectDB();
        cn.myConnect();

        cat = null;
        //setc=null;
        Statement s3 = cn.getStatement();
        ResultSet r3 = cn.getResultset();
        ResultSet r6 = cn.getResultset();
        cat = jComboBox1.getSelectedItem().toString();
//        System.out.println("Combo box sel :" + cat);
        int k = 0;
        try {
            r3 = s3.executeQuery("select distinct category_sub from category where category_main like '" + cat + "'");
            r6 = r3;
            while (r6.next()) {
                setc[k] = r6.getString("category_sub");
                //System.out.println("hi" +k);
                k++;
            }
            //System.out.println("i = " +(i-1));

        } catch (SQLException ex) {
            Logger.getLogger(Category.class.getName()).log(Level.SEVERE, null, ex);
        }

        //System.out.println("new " + set[0]);
        //System.out.println(set[1]);
        jComboBox2.removeAllItems();
        for (int j = 0; j < k; j++) {
            jComboBox2.addItem(setc[j]);
        }
        //jComboBox2.revalidate();
        //setc=null;
        //cat=null;*/

        String sql_cat_main = "Select * from record WHERE category_main = '" + jComboBox1.getSelectedItem().toString() + "'";
        Table(sql_cat_main);

        flag1 = true;
        flag2 = flag3 = false;
        sql_report = sql_cat_main;
    }//GEN-LAST:event_jComboBox1ItemStateChanged


    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        bc = null;
        jLabel13.setIcon(bc);
        Table(initial_table_query);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jComboBox4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox4ActionPerformed

    private void jComboBox3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox3ItemStateChanged

        connectDB cn = new connectDB();
        cn.myConnect();

        cat = null;
        //setc=null;
        Statement s3 = cn.getStatement();
        ResultSet r3 = cn.getResultset();
        ResultSet r6 = cn.getResultset();
        cat = jComboBox3.getSelectedItem().toString();
//        System.out.println("Combo box sel :" + cat);
        int k = 0;
        try {
            r3 = s3.executeQuery("select distinct category_sub from category where category_main like '" + cat + "'");
            r6 = r3;
            while (r6.next()) {
                setc[k] = r6.getString("category_sub");
                //System.out.println("hi" +k);
                k++;
            }
            //System.out.println("i = " +(i-1));

        } catch (SQLException ex) {
            Logger.getLogger(Category.class.getName()).log(Level.SEVERE, null, ex);
        }

        //System.out.println("new " + set[0]);
        //System.out.println(set[1]);
        jComboBox4.removeAllItems();
        for (int j = 0; j < k; j++) {
            jComboBox4.addItem(setc[j]);
        }
        //jComboBox2.revalidate();
        //setc=null;
        //cat=null;*/
    }//GEN-LAST:event_jComboBox3ItemStateChanged

    private void jDateChooser1changeDate(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser1changeDate
        // TODO add your handling code here:
        if ("date".equals(evt.getPropertyName())) {
            date1 = ((JTextField) jDateChooser1.getDateEditor().getUiComponent()).getText();
        }
    }//GEN-LAST:event_jDateChooser1changeDate

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField3ActionPerformed

    private void jTextField3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField3MouseClicked

        //jTextField3.setText(null);
    }//GEN-LAST:event_jTextField3MouseClicked

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed

        ResultSet r1 = cnnt.getResultset();
        ResultSet r_p = cnnt.getResultset();
        ResultSet r_b = cnnt.getResultset();
        ResultSet r_q = cnnt.getResultset();
        Statement s_q = cnnt.getStatement();
        Statement s1, st_p, st_bsp, s_b;
        s_b = cnnt.getStatement();
        s1 = cnnt.getStatement();
        st_p = cnnt.getStatement();
        st_bsp = cnnt.getStatement();
        ResultSet r_bp = cnnt.getResultset();
        String sp_b_p, sp_q_p, sp_s_p;
        int p_b_p, n_b_p, p_q_p, n_q_p, p_s_p, n_s_p;
        p_b_p = 0;
        n_b_p = 0;
        p_q_p = 0;
        n_q_p = 0;
        p_s_p = 0;
        n_s_p = 0;
        int b_p, s_p;
        b_p = 0;
        s_p = 0;
        String s_pro = "Select product_id,product_quantity from product WHERE product_id='" + jTextField3.getText() + "'";

        String query_cat_id = "SELECT cat_id FROM category WHERE category_main like '" + jComboBox3.getSelectedItem().toString() + "' "
                + " AND category_sub like '" + jComboBox4.getSelectedItem().toString() + "'";
        String s_bsp = "Select buying_price,selling_price from buy_rec WHERE product_id='" + jTextField3.getText() + "'";
        try {
            r_p = st_p.executeQuery(s_pro);
            //  r_bp=st_bsp.executeQuery(s_bsp);
            while (r_p.next()) {
                sp_q_p = r_p.getString("product_quantity");
                p_q_p = Integer.parseInt(sp_q_p);
                n_b_p = Integer.parseInt(jTextField6.getText());
                n_q_p = Integer.parseInt(jTextField4.getText());
                n_s_p = Integer.parseInt(jTextField5.getText());
                /*  while(r_bp.next()){
                 p_b_p=r_bp.getInt("buying_price");
                 p_s_p=r_bp.getInt("buying_price");
                 b_p=(p_b_p*p_q_p+n_b_p*n_q_p)/(n_q_p+p_q_p);
                 s_p=(p_s_p*p_q_p+n_s_p*n_q_p)/(n_q_p+p_q_p);
                 System.out.println("kha kha"+s_p+b_p);
                 }*/
            }
        } catch (SQLException ex) {
            Logger.getLogger(Update_product.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            r_bp = st_bsp.executeQuery(s_bsp);
            while (r_bp.next()) {
                p_b_p = r_bp.getInt("buying_price");
                p_s_p = r_bp.getInt("selling_price");
                b_p = (p_b_p * p_q_p + n_b_p * n_q_p) / (n_q_p + p_q_p);
                s_p = (p_s_p * p_q_p + n_s_p * n_q_p) / (n_q_p + p_q_p);
                // System.out.println("p_buying price:"+p_b_p);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Update_product.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {

            r1 = s1.executeQuery(query_cat_id);
            //  r_p=st_p.executeQuery(s_pro);
            //  r_bp=st_bsp.executeQuery(s_bsp);
//            System.out.println("Dhukse");
          //  while(r_p.next()){
            //        sp_q_p=r_p.getString("product_quantity");
            //       p_q_p=Integer.parseInt(sp_q_p);
            //      n_b_p=Integer.parseInt(jTextField6.toString());
            //      n_q_p=Integer.parseInt(jTextField4.toString());
            //      n_s_p=Integer.parseInt(jTextField5.toString());
            //      while(r_bp.next()){
            //           p_b_p=r_bp.getInt("buying_price");
            //           p_s_p=r_bp.getInt("buying_price");
            //           b_p=(p_b_p*p_q_p+n_b_p*n_q_p)/(n_q_p+p_q_p);
            //         s_p=(p_s_p*p_q_p+n_s_p*n_q_p)/(n_q_p+p_q_p);
            //       System.out.println("kha kha"+s_p+b_p);
            //       }

            //  }
            while (r1.next()) {
                cat3 = r1.getInt("cat_id");
                //System.out.println("category upd "+cat3);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Update_product.class.getName()).log(Level.SEVERE, null, ex);
        }
        //cat3=4;

        String query_upd_prod = "UPDATE product SET product_id= '" + jTextField3.getText() + "', "
                + "product_quantity= " + Integer.parseInt(jTextField4.getText()) + ", "
                + "product_name= '" + jTextField2.getText() + "', "
                + "product_vendor= '" + jTextField7.getText() + "',"
                + "cat_id= " + cat3 + ","
                + "warranty_period= " + Integer.parseInt(jTextField8.getText()) + ""
                + " WHERE  product_id= '" + jTextField3.getText() + "'";

        try {
            s1.executeUpdate(query_upd_prod);
        } catch (SQLException ex) {
            Logger.getLogger(Update_product.class.getName()).log(Level.SEVERE, null, ex);
        }
        String update_buying_price = "Select ";
        String query_upd_buy = "UPDATE buy_rec SET product_id= '" + jTextField3.getText() + "', "
                + "buying_price= " + Integer.parseInt(jTextField6.getText()) + ", "
                + "selling_price= '" + Integer.parseInt(jTextField5.getText()) + "', "
                + "buying_date= '" + date1 + "'"
                + "WHERE  product_id= '" + jTextField3.getText() + "'";

        try {
            s1.executeUpdate(query_upd_buy);
        } catch (SQLException ex) {
            Logger.getLogger(Update_product.class.getName()).log(Level.SEVERE, null, ex);
        }

        String query_record = "Update record SET product_id= '" + jTextField3.getText() + "',"
                + " product_name= '" + jTextField2.getText() + "' , "
                + " product_quantity= " + Integer.parseInt(jTextField4.getText()) + ","
                + " product_vendor= '" + jTextField7.getText() + "',"
                + " warranty_period= " + Integer.parseInt(jTextField8.getText()) + ","
                + " buying_price= " + Integer.parseInt(jTextField6.getText()) + ","
                + " selling_price= " + Integer.parseInt(jTextField5.getText()) + ","
                + " buying_date= '" + date1 + "', "
                + " category_main= '" + jComboBox3.getSelectedItem().toString() + "',"
                + " category_sub= '" + jComboBox4.getSelectedItem().toString() + "'"
                + " WHERE product_id= '" + jTextField3.getText() + "'";

        try {
            s1.executeUpdate(query_record);
        } catch (SQLException ex) {
            Logger.getLogger(Update_product.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            String q = "Update buy_rec SET buying_price=" + b_p + ", selling_price=" + s_p + " WHERE product_id= '" + jTextField3.getText() + "'";
            s_b.executeUpdate(q);
        } catch (SQLException ex) {
            Logger.getLogger(Update_product.class.getName()).log(Level.SEVERE, null, ex);
        }

        int total_quantity = n_q_p + p_q_p;
        String q_q = "Update product SET product_quantity= " + total_quantity + " WHERE product_id= '" + jTextField3.getText() + "'";
        try {
            s_q.executeUpdate(q_q);

        } catch (SQLException ex) {
            Logger.getLogger(Update_product.class.getName()).log(Level.SEVERE, null, ex);
        }

        Table(initial_table_query);

    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        int reply = JOptionPane.showConfirmDialog(null, "Do you really want to ADD those PRODUCT?", "ADD Product!!", JOptionPane.YES_NO_OPTION);

        if (reply == JOptionPane.YES_OPTION) {

            q = jTextField9.getText();

            if (product_id == null) {
                JOptionPane.showMessageDialog(rootPane, "Select a product from the table");
            } else {
                ResultSet r1 = cnnt.getResultset();
                Statement s1 = cnnt.getStatement();

                String query_fetch = "SELECT product_quantity from product where product_id='" + product_id + "' ";

                try {
                    r1 = s1.executeQuery(query_fetch);

                    while (r1.next()) {
                        old_q = r1.getInt("product_quantity");
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(Update_product.class.getName()).log(Level.SEVERE, null, ex);
                }
//                System.out.println("New quantity " + (old_q + Integer.parseInt(q)));
                String query_upd = "UPDATE `product` SET `product_quantity`=" + (old_q + Integer.parseInt(q)) + " where product_id='" + product_id + "'";

                try {
                    s1.executeUpdate(query_upd);
                } catch (SQLException ex) {
                    Logger.getLogger(Update_product.class.getName()).log(Level.SEVERE, null, ex);
                }

                query_upd = "INSERT INTO record (`product_id`, `product_name`, `product_quantity`,"
                        + " `product_vendor`, `warranty_period`, `buying_price`, `selling_price`, `buying_date`, "
                        + "`category_main`, `category_sub`) VALUES('" + product_id + "', '" + jTextField2.getText() + "', " + q + ", "
                        + "'" + jTextField7.getText() + "', " + Integer.parseInt(jTextField8.getText()) + ","
                        + " " + Integer.parseInt(jTextField6.getText()) + ", " + Integer.parseInt(jTextField5.getText()) + ", '" + date1 + "'"
                        + ", '" + jComboBox3.getSelectedItem().toString() + "', '" + jComboBox4.getSelectedItem().toString() + "' )";
                
                String sql = "UPDATE `record` SET `product_quantity`= " + (old_q + Integer.parseInt(q)) + " WHERE `product_id` = '" + product_id + "'";
                
                try {
                    s1.executeUpdate(sql);
                } catch (SQLException ex) {
                    Logger.getLogger(Update_product.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        jTextField9.setText("");
        Table(initial_table_query);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        connectDB cdb = new connectDB();
        cdb.myConnect();

        conn = cdb.getConnection();
        stat = cdb.getStatement();
        res = cdb.getResultset();

        try {
            String location = "C:\\report_update.jrxml";

            try {
                InputStream fs = new FileInputStream(new File(location));
            } catch (FileNotFoundException ex) {
                Logger.getLogger(record_buy_add.class.getName()).log(Level.SEVERE, null, ex);
            }
            InputStream is = this.getClass().getResourceAsStream(location);

            JasperDesign jd = JRXmlLoader.load(location);
            String query = null;

            if (flag == true) {
                query = initial_table_query;
            } else if (flag1 == true) {
                query = sql_report;
            } else if (flag2 == true) {
                query = sql_report;
            } else if (flag3 == true) {
                query = sql_report;
            }

            JRDesignQuery qur = new JRDesignQuery();
            qur.setText(query);
            jd.setQuery(qur);
            JasperReport jasperReport = JasperCompileManager.compileReport(jd);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, conn);

            JasperViewer.viewReport(jasperPrint, false);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
        String sql = null;
        try {
            String id = jTextField1.getText();

            sql = "Select * from record " //(product join buy_rec using(product_id)) join category using(cat_id)
                    + "WHERE product_id = '" + id + "'";
            Table(sql);

            jTextField1.setText("");

            flag1 = false;
            flag2 = false;
            flag3 = true;
            sql_report = sql;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jComboBox2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox2ItemStateChanged
        // TODO add your handling code here:
        String combo2 = jComboBox2.getSelectedItem() + "";
        String combo1 = jComboBox1.getSelectedItem() + "";

        sql_cat = "Select * from record "
                + "WHERE category_sub like '" + combo2 + "' and category_main like '" + combo1 + "' ";
        Table(sql_cat);

        flag1 = false;
        flag2 = true;
        sql_report = sql_cat;
    }//GEN-LAST:event_jComboBox2ItemStateChanged

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        int reply = JOptionPane.showConfirmDialog(null, "Do you really want to DELETE the PRODUCT?", "DELETE Product!!", JOptionPane.YES_NO_OPTION);

        if (reply == JOptionPane.YES_OPTION) {

            ResultSet r1 = cnnt.getResultset();
            Statement s1 = cnnt.getStatement();

            String query_cat_id = "SELECT cat_id FROM category WHERE category_main like '" + jComboBox3.getSelectedItem().toString() + "'"
                    + " AND category_sub like '" + jComboBox4.getSelectedItem().toString() + "'";

            try {
                r1 = s1.executeQuery(query_cat_id);

                while (r1.next()) {
                    cat3 = r1.getInt("cat_id");
                }
            } catch (SQLException ex) {
                Logger.getLogger(Update_product.class.getName()).log(Level.SEVERE, null, ex);
            }

            String query_upd_prod = "DELETE FROM product WHERE product_id= '" + product_id + "'; ";

            try {
                s1.executeUpdate(query_upd_prod);
            } catch (SQLException ex) {
                Logger.getLogger(Update_product.class.getName()).log(Level.SEVERE, null, ex);
            }

            String query_upd_buy = "DELETE FROM buy_rec WHERE  product_id= '" + product_id + "';";

            try {
                s1.executeUpdate(query_upd_buy);
            } catch (SQLException ex) {
                Logger.getLogger(Update_product.class.getName()).log(Level.SEVERE, null, ex);
            }

            String query_record = "DELETE FROM record WHERE product_id= '" + product_id + "';";

            try {
                s1.executeUpdate(query_record);

                Table(initial_table_query);
                JOptionPane.showMessageDialog(rootPane, "Deleted: " + product_id);
            } catch (SQLException ex) {
                Logger.getLogger(Update_product.class.getName()).log(Level.SEVERE, null, ex);
            }

            //Table(initial_table_query);
        }
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jTable1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable1KeyReleased
        int p = evt.getKeyCode();
        if (p == 127) {
            int reply = JOptionPane.showConfirmDialog(null, "Do you really want to DELETE the PRODUCT?", "DELETE Product!!", JOptionPane.YES_NO_OPTION);

            if (reply == JOptionPane.YES_OPTION) {

                ResultSet r1 = cnnt.getResultset();
                Statement s1 = cnnt.getStatement();

                String query_cat_id = "SELECT cat_id FROM category WHERE category_main like '" + jComboBox3.getSelectedItem().toString() + "'"
                        + " AND category_sub like '" + jComboBox4.getSelectedItem().toString() + "'";

                try {
                    r1 = s1.executeQuery(query_cat_id);

                    while (r1.next()) {
                        cat3 = r1.getInt("cat_id");
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(Update_product.class.getName()).log(Level.SEVERE, null, ex);
                }

                String query_upd_prod = "DELETE FROM product WHERE product_id= '" + product_id + "'; ";

                try {
                    s1.executeUpdate(query_upd_prod);
                } catch (SQLException ex) {
                    Logger.getLogger(Update_product.class.getName()).log(Level.SEVERE, null, ex);
                }

                String query_upd_buy = "DELETE FROM buy_rec WHERE  product_id= '" + product_id + "';";

                try {
                    s1.executeUpdate(query_upd_buy);
                } catch (SQLException ex) {
                    Logger.getLogger(Update_product.class.getName()).log(Level.SEVERE, null, ex);
                }

                String query_record = "DELETE FROM record WHERE product_id= '" + product_id + "';";

                try {
                    s1.executeUpdate(query_record);

                    Table(initial_table_query);
                    JOptionPane.showMessageDialog(rootPane, "Deleted: " + product_id);
                } catch (SQLException ex) {
                    Logger.getLogger(Update_product.class.getName()).log(Level.SEVERE, null, ex);
                }

                //Table(initial_table_query);
            }
        }
    }//GEN-LAST:event_jTable1KeyReleased

    private void jComboBox5ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox5ItemStateChanged
        // TODO add your handling code here:
        String com_name = jComboBox5.getSelectedItem().toString();

        String sql = "Select * from record where product_vendor = '" + com_name + "'";

        Table(sql);
    }//GEN-LAST:event_jComboBox5ItemStateChanged

    private void jComboBox6ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox6ItemStateChanged
        // TODO add your handling code here:
        String com_name = jComboBox6.getSelectedItem().toString();

        String sql = "Select * from record where product_name = '" + com_name + "'";

        Table(sql);
    }//GEN-LAST:event_jComboBox6ItemStateChanged

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                /*if ("Windows".equals(info.getName())) {
                 javax.swing.UIManager.setLookAndFeel(info.getClassName());
                 break;
                 }*/
                UIManager.setLookAndFeel("com.jtattoo.plaf.acryl.AcrylLookAndFeel");
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Category.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Category.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Category.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Category.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Update_product().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JComboBox jComboBox3;
    private javax.swing.JComboBox jComboBox4;
    private javax.swing.JComboBox jComboBox5;
    private javax.swing.JComboBox jComboBox6;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    // End of variables declaration//GEN-END:variables
}
