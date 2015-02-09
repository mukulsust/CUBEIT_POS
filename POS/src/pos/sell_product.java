package pos;

import com.sun.rowset.CachedRowSetImpl;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.rowset.CachedRowSet;
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

public class sell_product extends javax.swing.JFrame {

    StringSearchable searchable_cat;
    StringSearchable searchable_cat_sub;

    List<String> category = new ArrayList<String>();
    List<String> sub_category = new ArrayList<String>();
    int warranty_id = 0, warranty_period, warranty_status, product_quantity = 0;
    int cat3, ci;
    String q = null;
    int old_q = 0;
    boolean flag = false, click = false, click_date = false;
    Connection conn;
    Statement stat, stat47, statical;
    ResultSet res, racial;
    String cat = null, cat2 = null, sub_cat = null;
    String product_id = null, product_name = null, company = null, s_selling_price = null;
    Integer selling_price = 0;
    connectDB cnnt = new connectDB();
    String[] seta = new String[500];
    String[] setb = new String[500];
    String[] setc = new String[500];
    String initial_table_query = "SELECT `trans_id`, `product_id`, `product_name`, `product_vendor`, `warranty_period`, `selling_price`, `category_main`, `category_sub` from temp";
    String sql_cat, date1 = null;
    Boolean flagger = false;
    Map parametersMap = new HashMap();

    Integer trans_id, trans_id2, buying_price;
    String id;
    String name;
    String vendor;
    int wp;
    int sell_pr;
    String cat_m;
    String cat_s;
    Integer total_sell = 0;
    List<String> pro_name = new ArrayList<String>();

    /*ItemListener il = new ItemListener() {

     @Override
     public void itemStateChanged(ItemEvent e) {
     //To change body of generated methods, choose Tools | Templates.
     Statement s = cnnt.getStatement();
     ResultSet r = cnnt.getResultset();
     cat = jComboBox1.getSelectedItem().toString();
     System.out.println("category pore: "+cat);
     sub_cat=jComboBox2.getSelectedItem().toString();
            
     System.out.println("scategory pore: "+sub_cat);
     System.out.println("Combo box :" + cat);
     int k = 0;
     try {
     r = s.executeQuery("select product_name from product where cat_id like (select cat_id from category where category_main like '"+cat+"' and category_sub like '"+sub_cat+"')");

     while (r.next()) {
     setc[k] = r.getString("product_name");
     //System.out.println("hi" +k);
     k++;
     }
     //System.out.println("i = " +(i-1));

     } catch (SQLException ex) {
     Logger.getLogger(Category.class.getName()).log(Level.SEVERE, null, ex);
     }

     //System.out.println("new " + set[0]);
     //System.out.println(set[1]);
     jComboBox3.removeAllItems();
     for (int j = 0; j < k; j++) {
     jComboBox3.addItem(setc[j]);
     }

     AutoCompleteDecorator.decorate(jComboBox3);
     jComboBox3.setEditable(true);
     }
     };*/
    Calendar currentDate = Calendar.getInstance(); //Get the current date
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); //format it as per your requirement
    String dateNow = formatter.format(currentDate.getTime());
    Date today = null;
    DateFormat f = new SimpleDateFormat("yyyy-MM-dd");

    public sell_product() {
        cnnt.myConnect();
        Statement s1 = cnnt.getStatement();
        Statement s2 = cnnt.getStatement();
        Statement s3 = cnnt.getStatement();
        Statement s4 = cnnt.getStatement();

        ResultSet r1 = cnnt.getResultset();
        ResultSet r2 = cnnt.getResultset();
        ResultSet r3 = cnnt.getResultset();
        ResultSet r4 = cnnt.getResultset();

        initComponents();
        setAlwaysOnTop(true);
        Truncate();
        //searchable_cat = null;
        jTextField11.setText("0");

        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int width = d.width;
        int height = d.height;
        setSize(width, height);
        //System.out.println(height+" "+width);
        jTextField1.requestFocus(true);
        Table(initial_table_query);

        jTextField3.setEditable(false);
        jTextField4.setEditable(false);
        jTextField6.setEditable(false);

        try {
            today = (Date) f.parse(dateNow);
        } catch (Exception ex) {
            Logger.getLogger(sell_product.class.getName()).log(Level.SEVERE, null, ex);
        }
        jDateChooser2.setDate(today);

        //for main-category combobox itemlist
        int i = 0;
        try {
            r1 = s1.executeQuery("select distinct category_main from category");

            while (r1.next()) {
                seta[i] = r1.getString("category_main");
                //System.out.println(seta[i]);
                i++;
            }
            //System.out.println("i = " + (i - 1));

        } catch (SQLException ex) {
            Logger.getLogger(Category.class.getName()).log(Level.SEVERE, null, ex);
        }

        //System.out.println("new " + set[0]);
        //System.out.println(set[1]);
        for (int j = 0; j < i; j++) {
            jComboBox1.addItem(seta[j]);
        }
        AutoCompleteDecorator.decorate(jComboBox1);
        jComboBox1.setEditable(true);
        seta = null;

        //for sub-category combobox itemlist
        cat = jComboBox1.getSelectedItem().toString();
        jComboBox3.removeAllItems();
        jComboBox3.revalidate();
        String c_name = null;

        try {
            jComboBox2.removeAllItems();
            r1 = s1.executeQuery("select distinct customer_name from customer");

            while (r1.next()) {
                c_name = r1.getString("customer_name");
                jComboBox2.addItem(c_name);
            }
            AutoCompleteDecorator.decorate(jComboBox2);
            jComboBox2.revalidate();

        } catch (SQLException ex) {
            Logger.getLogger(Category.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox();
        jLabel13 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel14 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jTextField11 = new javax.swing.JTextField();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        jButton8 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jComboBox2 = new javax.swing.JComboBox();
        jLabel16 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jTextField10 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jButton1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(204, 0, 0));
        jButton1.setText("DELETE");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No.", "Product ID", "Product Name", "Company", "Warranty", "Selling Price", "Category", "Sub-Category"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jTable1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTable1KeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Sell Product"));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel1.setText("Product ID");

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel6.setText("T O T A L");

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel5.setText("Discount (if Applicable)");

        jTextField6.setEditable(false);
        jTextField6.setDisabledTextColor(new java.awt.Color(255, 0, 0));
        jTextField6.setSelectionColor(new java.awt.Color(255, 0, 0));

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel4.setText("Product Price");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel3.setText("Company");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel2.setText("Product Name");

        jTextField5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField5ActionPerformed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel12.setText("D A T E");

        jComboBox3.setEditable(true);
        jComboBox3.setAutoscrolls(true);
        jComboBox3.setFocusCycleRoot(true);
        jComboBox3.setInheritsPopupMenu(true);
        jComboBox3.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox3ItemStateChanged(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel13.setText("Category");

        jComboBox1.setEditable(true);
        jComboBox1.setAutoscrolls(true);
        jComboBox1.setInheritsPopupMenu(true);
        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel14.setText("Advance/PAID");

        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel15.setText("DUE");

        jTextField11.setEditable(false);

        jDateChooser2.setDateFormatString("yyyy-MM-dd");
        jDateChooser2.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser2PropertyChange(evt);
            }
        });

        jButton8.setText("+");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(56, 56, 56)
                        .addComponent(jLabel1)
                        .addGap(107, 107, 107)
                        .addComponent(jLabel13)
                        .addGap(99, 99, 99)
                        .addComponent(jLabel2))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addGap(38, 38, 38)
                                .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton8)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel14)
                                    .addComponent(jLabel15))
                                .addGap(69, 69, 69))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                            .addGap(73, 73, 73)
                                            .addComponent(jLabel3)))
                                    .addComponent(jLabel5))
                                .addGap(18, 18, 18)))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jTextField5, javax.swing.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE)
                                .addComponent(jTextField6))
                            .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(22, 22, 22))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(86, 86, 86))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jLabel13)
                            .addComponent(jLabel2))
                        .addGap(13, 13, 13)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(44, 44, 44))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTextField3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(17, 17, 17)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(8, 8, 8)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel14))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel12)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addGap(28, 28, 28))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Customer's Info."));
        jPanel1.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel7.setText("Customer's Name");

        jButton6.setText("+ Add Customer");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setText("View List");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jComboBox2.setEditable(true);
        jComboBox2.setAutoscrolls(true);
        jComboBox2.setFocusCycleRoot(true);
        jComboBox2.setInheritsPopupMenu(true);

        jLabel16.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel16.setText("Bearer");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 191, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton7, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel16))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBox2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextField7))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Payment Info."));

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel11.setText("Credit Card No./Bkash ID");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(77, 77, 77)
                .addComponent(jLabel11)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jTextField10)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jButton2.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jButton2.setForeground(new java.awt.Color(0, 153, 0));
        jButton2.setText("S E L L");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Tahoma", 0, 22)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 102, 0));
        jButton3.setText("PRINT");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 51, 51));
        jButton4.setText("EXIT");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("  REFRESH");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE)
                            .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 361, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(55, 55, 55))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed

        stat = cnnt.getStatement();
        res = cnnt.getResultset();

        jTextField1.requestFocus(true);
        String s = jTextField1.getText();
        String price = null;
        int quan = 0;
        //System.out.println(s);
        q = "SELECT `product_id`, `product_name`, `product_quantity`, `product_vendor`, `warranty_period`, `selling_price`, `category_main`, `category_sub` FROM record where product_id='" + s + "'";    //(record JOIN buy_rec using(product_id)) join category using(cat_id)

        try {
            res = stat.executeQuery(q);
            //System.out.println("data fetched");
            while (res.next()) {
                id = res.getString("product_id");
                name = res.getString("product_name");
                vendor = res.getString("product_vendor");
                wp = res.getInt("warranty_period");
                sell_pr = res.getInt("selling_price");
                cat_m = res.getString("category_main");
                cat_s = res.getString("category_sub");
                quan = res.getInt("product_quantity");
            }
            //System.out.println("data var entered");
        } catch (SQLException ex) {
            Logger.getLogger(sell_product.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (quan <= 0) {

            JOptionPane.showMessageDialog(rootPane, "Insufficient storage!!");

        } else if (quan > 0) {
            jTextField3.setText(vendor);
            jTextField4.setText(sell_pr + "");
            jComboBox1.setSelectedItem(cat_m);
            jComboBox3.setSelectedItem(name);

            total_sell += sell_pr;
            jTextField6.setText(total_sell + "");
            jTextField11.setText(total_sell + "");

            q = "INSERT INTO temp (`product_id`, `product_name`, `product_vendor`, `warranty_period`, `selling_price`, `category_main`, `category_sub`) VALUES ('" + id + "', '" + name + "', '" + vendor + "', " + wp + ", " + sell_pr + ", '" + cat_m + "', '" + cat_s + "')";

            try {
                stat.executeUpdate(q);
                //System.out.println("data temp entered");
            } catch (SQLException ex) {
                Logger.getLogger(sell_product.class.getName()).log(Level.SEVERE, null, ex);
            }

            flagger = true;
            jTextField1.setText(null);
            Table(initial_table_query);
        }
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        Truncate();
        dispose();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jTextField5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField5ActionPerformed
        // TODO add your handling code here:
        Integer i;
        String s;
        Integer t;
        stat = cnnt.getStatement();
        res = cnnt.getResultset();
        s = "SELECT trans_id FROM temp WHERE product_id='" + id + "'";

        try {
            res = stat.executeQuery(s);

            while (res.next()) {

                trans_id = res.getInt("trans_id");

            }
        } catch (SQLException ex) {
            Logger.getLogger(sell_product.class.getName()).log(Level.SEVERE, null, ex);
        }
        s = "SELECT buying_price FROM buy_rec WHERE product_id='" + id + "'";
        try {
            res = stat.executeQuery(s);

            while (res.next()) {

                buying_price = res.getInt("buying_price");

            }
        } catch (SQLException ex) {
            Logger.getLogger(sell_product.class.getName()).log(Level.SEVERE, null, ex);
        }

        i = Integer.parseInt(jTextField4.getText()) - Integer.parseInt(jTextField5.getText());

        if (i < buying_price) {
            JOptionPane.showMessageDialog(rootPane, "Discount is more than buying price!!");
        } else {
            s = "UPDATE temp SET selling_price=" + i + " where trans_id=" + trans_id + "";
            if (click == true) {
                s = "UPDATE temp SET selling_price=" + i + " where trans_id=" + trans_id2 + "";
            }
            try {
                stat.executeUpdate(s);
                //System.out.println("Updated temp!!");
            } catch (SQLException ex) {
                Logger.getLogger(sell_product.class.getName()).log(Level.SEVERE, null, ex);
            }

            Table(initial_table_query);
            click = false;
            Integer discount = Integer.parseInt(jTextField5.getText());
            total_sell = total_sell - discount;
            String tt;
            tt = total_sell.toString().trim();
            //System.out.println("total sell=" + total_sell + ", after trim =" + tt);
            jTextField6.setText(tt);
            //jTextField5.setText("");
            jTextField1.requestFocus(true);

        }
    }//GEN-LAST:event_jTextField5ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:        
        String s = null, c_no = null;
        int limit = 0, c_id = 0;

        stat = cnnt.getStatement();
        res = cnnt.getResultset();

        if (click_date == false || jDateChooser2.getDate() == null) {
            JOptionPane.showMessageDialog(null, "Please Select DATE!!");
        } else if (click_date == true) {

            try {
                res = stat.executeQuery("SELECT * FROM customer WHERE customer_name = '" + jComboBox2.getSelectedItem().toString() + "'");

                while (res.next()) {
                    c_id = res.getInt("customer_id");
                    limit = res.getInt("limit");

                    System.out.println("id " + id);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            for (int i = 0; i < jTable1.getRowCount(); i++) {

                //Inserting into warranty
                warranty_period = Integer.parseInt(jTable1.getValueAt(i, 4).toString());
                if (warranty_period > 0) {
                    warranty_status = 1;
                } else {
                    warranty_status = 0;
                }

                s = "INSERT INTO `warranty`(`warranty_status`, `warranty_period`, `customer_id`) VALUES(" + warranty_status + ", " + warranty_period + ", " + c_id + " )";

                try {
                    stat.executeUpdate(s);
                    //System.out.println("Inserted into warranty");
                } catch (SQLException ex) {
                    Logger.getLogger(sell_product.class.getName()).log(Level.SEVERE, null, ex);
                }

                //getting warranty_id
                s = "SELECT warranty_id FROM warranty WHERE warranty_status=" + warranty_status + " and warranty_period=" + warranty_period + " and customer_id = " + c_id; //and customer_id =" + id

                try {
                    res = stat.executeQuery(s);
                    //System.out.println("Getting warranty_id");
                    while (res.next()) {
                        warranty_id = res.getInt("warranty_id");

                    }
                    System.out.println("warranty id " + warranty_id);
                } catch (SQLException ex) {
                    Logger.getLogger(sell_product.class.getName()).log(Level.SEVERE, null, ex);
                }

                int p_due = 0;
                s = "SELECT due FROM customer WHERE customer_id='" + c_id + "'";

                try {
                    res = stat.executeQuery(s);
                    System.out.println("Getting product_quantity");
                    while (res.next()) {
                        p_due = res.getInt("due");
                    }
                    System.out.println("product_quantity " + product_quantity);
                } catch (SQLException ex) {
                    Logger.getLogger(sell_product.class.getName()).log(Level.SEVERE, null, ex);
                }
                limit -= p_due;

                s = null;
                String bearer = jTextField7.getText();
                Integer due = Integer.parseInt(jTextField11.getText());
                System.out.println("pro id " + jTable1.getValueAt(i, 1));
                String pro_id = jTable1.getValueAt(i, 1).toString();
                Integer sell_price = Integer.parseInt(jTable1.getValueAt(i, 5).toString());

                if (due > limit) {
                    JOptionPane.showMessageDialog(rootPane, "Sorry!! Mr. " + jComboBox2.getSelectedItem().toString() + ". You can't purchase so much amount Due.");
                } else if (limit >= due) {

                    //Inserting into sell-record
                    s = "INSERT INTO `sell_rec`(`product_id`, `selling_price`, `selling_quantity`, `selling_date`, `customer_id`, `credit_card`) "
                            + " VALUES('" + pro_id + "', " + sell_price + ", 1, '" + date1 + "', " + c_id + ", '" + jTextField10.getText().toString() + "')";

                    try {
                        stat.executeUpdate(s);
                        System.out.println("Inserted into sell-rec");

                    } catch (SQLException ex) {
                        Logger.getLogger(sell_product.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    //get product_quantity
                    s = "SELECT product_quantity FROM product WHERE product_id='" + pro_id + "'";

                    try {
                        res = stat.executeQuery(s);
                        System.out.println("Getting product_quantity");
                        while (res.next()) {
                            product_quantity = res.getInt("product_quantity");
                        }
                        System.out.println("product_quantity " + product_quantity);
                    } catch (SQLException ex) {
                        Logger.getLogger(sell_product.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    if (product_quantity <= 0) {

                        JOptionPane.showMessageDialog(rootPane, "Unsufficient storage!!");

                    } else if (product_quantity > 0) {

                        //UPDATE product_quantity
                        s = "UPDATE product SET product_quantity=" + (product_quantity - 1) + " WHERE product_id='" + pro_id + "'";

                        try {
                            stat.executeUpdate(s);
                            System.out.println("Product list UPDATED");
                        } catch (SQLException ex) {
                            Logger.getLogger(sell_product.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        //UPDATE record table
                        s = "UPDATE record SET product_quantity=" + (product_quantity - 1) + " WHERE product_id='" + pro_id + "'";

                        try {
                            stat.executeUpdate(s);
                            System.out.println("Product list UPDATED");
                        } catch (SQLException ex) {
                            Logger.getLogger(sell_product.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                    jTextField1.requestFocus(true);

                    String s_total_purchase = jTextField6.getText().toString(), s_total_paid = jTextField2.getText().toString(), s_total_due = jTextField11.getText();

                    int total_purchase = Integer.parseInt(s_total_purchase), total_paid = 0, total_due = 0;
                    if (!s_total_paid.isEmpty()) {
                        total_paid = Integer.parseInt(s_total_paid);
                    } else {
                        total_paid = 0;
                    }

                    if (!s_total_due.isEmpty()) {
                        total_due = Integer.parseInt(s_total_due);
                        total_due = total_purchase - total_paid;
                    } else {
                        total_due = 0;
                    }

                    s = "INSERT INTO `customer_due` (`customer_id`, `purchase_date`, `bearer`, `total_purchase`, `total_paid`, `total_due`) VALUES (" + c_id + ", '" + date1 + "', '"+jTextField7.getText().toString()+"', '" + total_purchase + "', '" + total_paid + "', '" + total_due + "')";
                    ResultSet r_cid = cnnt.getResultset();
                    try {
                        stat.executeUpdate(s);

                    } catch (SQLException ex) {
                        Logger.getLogger(sell_product.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    //Update total due into customer table
                    total_due += p_due;
                    s = "UPDATE customer SET due=" + total_due + " WHERE customer_id='" + c_id + "'";

                    try {
                        stat.executeUpdate(s);
                        System.out.println("Product list UPDATED");
                    } catch (SQLException ex) {
                        Logger.getLogger(sell_product.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    JOptionPane.showMessageDialog(rootPane, "SOLD!!!");
                    jButton2.setEnabled(false);
                }
                

                //Updating customer
               /* s = "UPDATE customer SET warranty_id = '"+warranty_id+"', "
                 + "bearer = '"+bearer+"' AND "
                 + " limit = "+limit+" WHERE customer_name = '"+jComboBox2.getSelectedItem().toString()+"'";
                 //                s = "INSERT INTO `customer(`customer_name`, `warranty_id`, `contact_no`, ` address`, `email`, `bearer`, limit)"
                 //                        + " VALUES('" + jComboBox2.getSelectedItem().toString() + "', " + warranty_id + ", '" + c_no + "',"
                 //                        + " '" + address + "', '" + email + "', '"+bearer+"', "+limit+")";

                 System.out.println("query = "+s);
                 try {
                 stat.executeUpdate(s);
                 System.out.println("Inserted into customer"); //have commented it
                 } catch (SQLException ex) {
                 Logger.getLogger(sell_product.class.getName()).log(Level.SEVERE, null, ex);
                 }*/
                /*
                 //getting customer_id
                 s = "SELECT customer_id FROM customer WHERE customer_name='" + jComboBox3.getSelectedItem().toString() + "' and "
                 + "warranty_id=" + warranty_id;

                 try {
                 res = stat.executeQuery(s);
                 System.out.println("Getting customer_id");
                 while (res.next()) {
                 customer_id = res.getInt("customer_id");
                 }
                 System.out.println("customer_id " + customer_id);
                 } catch (SQLException ex) {
                 Logger.getLogger(sell_product.class.getName()).log(Level.SEVERE, null, ex);
                 }

                
                 //Inserting into sell-record
                 s = "INSERT INTO `sell_rec`(`product_id`, `selling_price`, `selling_quantity`, `selling_date`, `customer_id`, `credit_card`) "
                 + " VALUES('" + jTable1.getValueAt(i, 1) + "', '" + jTable1.getValueAt(i, 5) + "', 1, '" + date1 + "', " + customer_id + ", '" + jTextField10.getText() + "')";

                 try {
                 stat.executeUpdate(s);
                 System.out.println("Inserted into sell-rec");

                 } catch (SQLException ex) {
                 Logger.getLogger(sell_product.class.getName()).log(Level.SEVERE, null, ex);
                 }

                 //Updating product
                 System.out.println("UPDATING!!");
                 //get product_quantity

                 s = "SELECT product_quantity FROM product WHERE product_id='" + jTable1.getValueAt(i, 1) + "'";

                 try {
                 res = stat.executeQuery(s);
                 System.out.println("Getting product_quantity");
                 while (res.next()) {
                 product_quantity = res.getInt("product_quantity");
                 }
                 System.out.println("product_quantity " + product_quantity);
                 } catch (SQLException ex) {
                 Logger.getLogger(sell_product.class.getName()).log(Level.SEVERE, null, ex);
                 }

                 //Check for null quantity //copied
                 if (product_quantity <= 0) {

                 JOptionPane.showMessageDialog(rootPane, "Unsufficient storage!!");

                 } else if (product_quantity > 0) {

                 //UPDATE product_quantity
                 s = "UPDATE product SET product_quantity=" + (product_quantity - 1) + " WHERE product_id='" + jTable1.getValueAt(i, 1) + "'";

                 try {
                 stat.executeUpdate(s);
                 System.out.println("Product list UPDATED");
                 } catch (SQLException ex) {
                 Logger.getLogger(sell_product.class.getName()).log(Level.SEVERE, null, ex);
                 }

                 }

                 jTextField1.requestFocus(true);
                 }
                 Date e=jDateChooser2.getDate();
                 String d=e.toString();
                 s="INSERT INTO `cubeit_pos`.`customer_due` (`customer_id`, `purchase_date`, `total_purchase`, `total_paid`, `total_due`) VALUES ('"+customer_id+"', '"+d+"', '"+Integer.parseInt(jTextField6.getText())+"', '"+Integer.parseInt(jTextField2.getText())+"', '"+Integer.parseInt(jTextField11.getText())+"')";
                 ResultSet r_cid=cnnt.getResultset();
                 try {
                 stat.executeUpdate(s);
               
                 } catch (SQLException ex) {
                 Logger.getLogger(sell_product.class.getName()).log(Level.SEVERE, null, ex);
                 }
                 //customer due
                 int customer_due=Integer.parseInt(jTextField11.getText());
                 s="UPDATE customer SET due='"+customer_due+"' WHERE customer_id='"+customer_id+"'";
                 try {
                 stat.executeUpdate(s);
               
                 } catch (SQLException ex) {
                 Logger.getLogger(sell_product.class.getName()).log(Level.SEVERE, null, ex);
                 }
                 */            }
            // s="INSERT INTO `cubeit_pos`.`customer_due` (`customer_id`, `purchase_date`, `total_purchase`, `total_paid`, `total_due`) VALUES ('"++"', '', '12', '10', '2')";
            //INSERT INTO `cubeit_pos`.`customer_due` (`customer_id`, `purchase_date`, `total_purchase`, `total_paid`, `total_due`) VALUES ('3', '', '12', '10', '2');

        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
        click = true;
        int row = jTable1.getSelectedRow();
        int col = jTable1.getSelectedColumn();

        for (int i = 0; i < jTable1.getColumnCount(); i++) {
            String s = jTable1.getValueAt(row, i).toString();
            //System.out.println(s);
        }

        trans_id2 = Integer.parseInt(jTable1.getValueAt(row, 0).toString());
        //System.out.println("Trans id : " + trans_id2);
        product_id = jTable1.getValueAt(row, 1).toString();
        product_name = jTable1.getValueAt(row, 2).toString();
        company = jTable1.getValueAt(row, 3).toString();
        s_selling_price = jTable1.getValueAt(row, 5).toString();
        selling_price = Integer.parseInt(s_selling_price);

        //jTextField2.setText(product_name);
        jTextField3.setText(company);
        jTextField4.setText(s_selling_price);

        //System.out.println(row+" "+col);
        //System.out.println(product_id);

    }//GEN-LAST:event_jTable1MouseClicked

    private void jDateChooser2changeDate(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser2changeDate
        // TODO add your handling code here:
        click_date = true;
        if ("date".equals(evt.getPropertyName())) {
            date1 = ((JTextField) jDateChooser2.getDateEditor().getUiComponent()).getText();
            //System.out.println("Date : " + date1);
        }
    }//GEN-LAST:event_jDateChooser2changeDate

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed

        connectDB cdb = new connectDB();
        cdb.myConnect();

        conn = cdb.getConnection();
        stat = cdb.getStatement();
        res = cdb.getResultset();
        parametersMap.put("total", total_sell);
        parametersMap.put("Customer name", jComboBox3.getSelectedItem().toString());
        //parametersMap.put("Contact No.", jTextField8.getText());
        //parametersMap.put("Address", jTextArea1.getText());
        //parametersMap.put("email", jTextField9.getText());
        if (flagger == false) {
            JOptionPane.showMessageDialog(null, "Please Add a Product first!!");
        } else if (flagger == true) {

            String query = "SELECT `product_id`, `product_name`, `product_vendor`, `warranty_period`, `selling_price` FROM temp;";

            try {
                String location = "C:\\report_invoice.jrxml";

                try {
                    InputStream fs = new FileInputStream(new File(location));

                } catch (FileNotFoundException ex) {
                    Logger.getLogger(record_buy_add.class
                            .getName()).log(Level.SEVERE, null, ex);
                }

                InputStream is = this.getClass().getResourceAsStream(location);

                JasperDesign jd = JRXmlLoader.load(location);

                JRDesignQuery qur = new JRDesignQuery();
                qur.setText(query);
                jd.setQuery(qur);
                JasperReport jasperReport = JasperCompileManager.compileReport(jd);
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametersMap, conn);

                JasperViewer.viewReport(jasperPrint, false);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        //System.out.println(e);
        int reply = JOptionPane.showConfirmDialog(null, "Do you really want to DELETE the PRODUCT?", "DELETE Product!!", JOptionPane.YES_NO_OPTION);

        if (reply == JOptionPane.YES_OPTION) {

            ResultSet r1 = cnnt.getResultset();
            Statement s1 = cnnt.getStatement();

            String query_trans_id = "DELETE FROM temp WHERE trans_id= " + trans_id2 + "; ";

            try {
                s1.executeUpdate(query_trans_id);
                System.out.println("Deleted: Trans ID: " + trans_id2);
                Table(initial_table_query);
                JOptionPane.showMessageDialog(rootPane, "Deleted: " + product_name);

            } catch (SQLException ex) {
                //System.out.println();
                //JOptionPane.showMessageDialog(rootPane, "Product alredy deleted!!");
                Logger.getLogger(Update_product.class
                        .getName()).log(Level.SEVERE, null, ex);
            }

            //Table(initial_table_query);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTable1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable1KeyReleased
        int e = evt.getKeyCode();
        if (e == 127) {

            //System.out.println(e);
            int reply = JOptionPane.showConfirmDialog(null, "Do you really want to DELETE the PRODUCT?", "DELETE Product!!", JOptionPane.YES_NO_OPTION);

            if (reply == JOptionPane.YES_OPTION) {

                ResultSet r1 = cnnt.getResultset();
                Statement s1 = cnnt.getStatement();

                String query_trans_id = "DELETE FROM temp WHERE trans_id= " + trans_id2 + "; ";

                try {
                    s1.executeUpdate(query_trans_id);
                    //System.out.println("Deleted: Trans ID: " + trans_id2);
                    Table(initial_table_query);
                    JOptionPane.showMessageDialog(rootPane, "Deleted: " + product_name);

                } catch (SQLException ex) {
                    //System.out.println();
                    //JOptionPane.showMessageDialog(rootPane, "Product alredy deleted!!");
                    Logger.getLogger(Update_product.class
                            .getName()).log(Level.SEVERE, null, ex);
                }

                //Table(initial_table_query);
            }
        }
    }//GEN-LAST:event_jTable1KeyReleased

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        Truncate();
        Table(initial_table_query);
        jTextField1.requestFocus(true);
        jTextField2.setText("");
        jTextField11.setText("");
        jComboBox1.setSelectedIndex(-1);
        jComboBox2.setSelectedIndex(-1);
        jComboBox3.setSelectedIndex(-1);
        jTextField3.setText("");
        jTextField4.setText("");
        jTextField6.setText("");
        jTextField5.setText("");
        jButton2.setEnabled(true);
        
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged

        
        statical = cnnt.getStatement();
        racial = cnnt.getResultset();
        cat = null;
        cat = jComboBox1.getSelectedItem().toString();
        //setc=null;
        System.out.println("cat1 "+cat);
        
        int k = 0;
        try {
            racial = statical.executeQuery("Select product.product_name from product natural join category WHERE category.category_main = '" + cat + "'");
            CachedRowSet row_set= new CachedRowSetImpl();
            row_set.populate(racial);
            if(jComboBox3.getItemCount()!=0){
                jComboBox3.removeAllItems();
            }
            while (row_set.next()) {
                setc[k] = row_set.getString("product_name");
                jComboBox3.addItem(setc[k]);
                k++;
            }
            AutoCompleteDecorator.decorate(jComboBox3);
            racial.close();
        } catch (SQLException ex) {
            Logger.getLogger(Category.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        cat = null;
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void jComboBox3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox3ItemStateChanged
//        stat = cnnt.getStatement();
//        res = cnnt.getResultset();
//        
//        //jTextField1.requestFocus(true);
//        String srr = jComboBox3.getSelectedItem().toString();
//        String price = null;
//        int quan = 0;
//        System.out.println(srr);
//        q = "SELECT `product_id`, `product_name`, `product_quantity`, `product_vendor`, `warranty_period`, `selling_price`, `category_main`, `category_sub` FROM record where product_name='" + srr + "'";    //(record JOIN buy_rec using(product_id)) join category using(cat_id)
//
//        try {
//            res = stat.executeQuery(q);
//            System.out.println("data fetched");
//            while (res.next()) {
//                id = res.getString("product_id");
//                name = res.getString("product_name");
//                vendor = res.getString("product_vendor");
//                wp = res.getInt("warranty_period");
//                sell_pr = res.getInt("selling_price");
//                cat_m = res.getString("category_main");
//                cat_s = res.getString("category_sub");
//                quan = res.getInt("product_quantity");
//            }
//            res.close();
//            System.out.println("data var entered");
//        } catch (SQLException ex) {
//            Logger.getLogger(sell_product.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        if (quan <= 0) {
//
//            JOptionPane.showMessageDialog(rootPane, "Insufficient storage!!");
//
//        } else if (quan > 0) {
//            jTextField3.setText(vendor);
//            jTextField4.setText(sell_pr + "");
//            jComboBox1.setSelectedItem(cat_m);
//            jComboBox3.setSelectedItem(name);
//
//            total_sell += sell_pr;
//            jTextField6.setText(total_sell + "");
//            jTextField11.setText(total_sell + "");
//
//            q = "INSERT INTO temp (`product_id`, `product_name`, `product_vendor`, `warranty_period`, `selling_price`, `category_main`, `category_sub`) VALUES ('" + id + "', '" + name + "', '" + vendor + "', " + wp + ", " + sell_pr + ", '" + cat_m + "', '" + cat_s + "')";
//
//            stat47=cnnt.getStatement();
//            try {
//                
//                stat47.execute(q);
//                System.out.println("data temp entered");
//            } catch (SQLException ex) {
//                Logger.getLogger(sell_product.class.getName()).log(Level.SEVERE, null, ex);
//            }
//
//            flagger = true;
//            jTextField1.setText(null);
//            
//            Table(initial_table_query);
//            System.out.println("hoise");
//        }
    }//GEN-LAST:event_jComboBox3ItemStateChanged

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        customer_profile cp = new customer_profile();
        cp.show();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
        String s_total = jTextField6.getText();
        Integer t = Integer.parseInt(s_total);

        String s_advance = jTextField2.getText();
        Integer a = Integer.parseInt(s_advance);

        Integer grand_total = t - a;
        jTextField11.setText(grand_total.toString());
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jDateChooser2PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser2PropertyChange
        // TODO add your handling code here:
        if ("date".equals(evt.getPropertyName())) {
            date1 = ((JTextField) jDateChooser2.getDateEditor().getUiComponent()).getText();
            System.out.println("Date : " + date1);
        }
        click_date = true;
    }//GEN-LAST:event_jDateChooser2PropertyChange

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
        stat = cnnt.getStatement();
        res = cnnt.getResultset();

        //jTextField1.requestFocus(true);
        String srr = jComboBox3.getSelectedItem().toString();
        String price = null;
        int quan = 0;
        System.out.println(srr);
        q = "SELECT `product_id`, `product_name`, `product_quantity`, `product_vendor`, `warranty_period`, `selling_price`, `category_main`, `category_sub` FROM record where product_name='" + srr + "'";    //(record JOIN buy_rec using(product_id)) join category using(cat_id)

        try {
            res = stat.executeQuery(q);
            System.out.println("data fetched");
            while (res.next()) {
                id = res.getString("product_id");
                name = res.getString("product_name");
                vendor = res.getString("product_vendor");
                wp = res.getInt("warranty_period");
                sell_pr = res.getInt("selling_price");
                cat_m = res.getString("category_main");
                cat_s = res.getString("category_sub");
                quan = res.getInt("product_quantity");
            }
            res.close();
            System.out.println("data var entered");
        } catch (SQLException ex) {
            Logger.getLogger(sell_product.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (quan <= 0) {

            JOptionPane.showMessageDialog(rootPane, "Insufficient storage!!");

        } else if (quan > 0) {
            jTextField3.setText(vendor);
            jTextField4.setText(sell_pr + "");
            jComboBox1.setSelectedItem(cat_m);
            jComboBox3.setSelectedItem(name);

            total_sell += sell_pr;
            jTextField6.setText(total_sell + "");
            jTextField11.setText(total_sell + "");

            q = "INSERT INTO temp (`product_id`, `product_name`, `product_vendor`, `warranty_period`, `selling_price`, `category_main`, `category_sub`) VALUES ('" + id + "', '" + name + "', '" + vendor + "', " + wp + ", " + sell_pr + ", '" + cat_m + "', '" + cat_s + "')";

            stat47 = cnnt.getStatement();
            try {

                stat47.execute(q);
                System.out.println("data temp entered");
            } catch (SQLException ex) {
                Logger.getLogger(sell_product.class.getName()).log(Level.SEVERE, null, ex);
            }

            flagger = true;
            jTextField1.setText(null);

            Table(initial_table_query);
            System.out.println("hoise");
        }
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        view_customer vc = new view_customer();
        vc.show();
    }//GEN-LAST:event_jButton7ActionPerformed

    /**
     * @param args the command line arguments
     */
    public void Truncate() {
        total_sell = 0;
        stat = cnnt.getStatement();
        //res= cnnt.getResultset();
        String s = "TRUNCATE TABLE temp";
        try {
            stat.execute(s);

        } catch (SQLException ex) {
            Logger.getLogger(sell_product.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        //System.out.println("TRUNCATED temp!!");
        Table(initial_table_query);
        jTextField1.requestFocus(true);

    }

    public void Table(String query) {
        try {
            //cnnt.myConnect();

            stat = cnnt.getStatement();
            res = cnnt.getResultset();

            res = stat.executeQuery("Select * from temp");

            DefaultTableModel dtm = new DefaultTableModel();

            dtm.setColumnIdentifiers(new String[]{"No.", "Product ID", "Product Name", "Company", "Warranty", "Selling Price", "Category", "Sub-Category"});

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

                dtm.setValueAt(r1.getInt(1), i, 0);
                dtm.setValueAt(r1.getString(2), i, 1);
                dtm.setValueAt(r1.getString(3), i, 2);
                dtm.setValueAt(r1.getString(4), i, 3);
                dtm.setValueAt(r1.getInt(5), i, 4);
                dtm.setValueAt(r1.getInt(6), i, 5);
                dtm.setValueAt(r1.getString(7), i, 6);
                dtm.setValueAt(r1.getString(8), i, 7);
                i++;

            }
            jTable1.setModel(dtm);

        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }

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
            java.util.logging.Logger.getLogger(Category.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Category.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Category.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Category.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new sell_product().setVisible(true);
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
    private javax.swing.JButton jButton8;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JComboBox jComboBox3;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    // End of variables declaration//GEN-END:variables
}
