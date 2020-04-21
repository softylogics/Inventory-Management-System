
import static java.awt.event.KeyEvent.VK_ENTER;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Muhammad Samraiz
 */
public class sale extends javax.swing.JFrame {
Connection conn = null;
ResultSet rs = null;
PreparedStatement pst = null;
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Creates new form sale
     */
    public sale() {
        initComponents();
        conn =sqlconnect.ConnectDB();
    }
private void deletedata(){
    try{       
        
    String selectqty = "SELECT item.item_id, item.item_qty, model.model_id, model.qty, sale.qty_sale "
            + "FROM item "
            + "INNER JOIN model ON item.item_id = model.item_id "
            + "INNER JOIN sale ON model.model_id = sale.model_id "
            + "WHERE sale.voucher_no = '"+s_txt_voucher.getText()+"' ";
    pst = conn.prepareStatement(selectqty);
    rs = pst.executeQuery();
    
    if(rs.next()){
    String model_name = rs.getString("model.model_id");
    String item_name = rs.getString("item.item_id");
    int apqty = Integer.parseInt(rs.getString("model.qty"));
    int amqty = Integer.parseInt(rs.getString("sale.qty_sale"));
    int imqty = Integer.parseInt(rs.getString("item.item_qty"));

    int cal1 = apqty+amqty;
    int cal2 = imqty+amqty;
    String model_qty = Integer.toString(cal1);
    String item_qty = Integer.toString(cal2);

    String update_item ="UPDATE item set item_qty = '"+item_qty+"'"
                        + " where item_id = '"+item_name+"' ";
    pst = conn.prepareStatement(update_item);
    pst.executeUpdate();

    String update_model ="UPDATE model set qty = '"+model_qty+"' "
                        + " where model_id = '"+model_name+"' ";
    pst = conn.prepareStatement(update_model);
    pst.executeUpdate();
    
    String insertdelete = "INSERT into del (voucher_no,date_deleted,qty,price,purchase_sale,note,model_id,item_id) VALUES (?,?,?,?,?,?,?,?)" ;
          pst = conn.prepareStatement(insertdelete);
          pst.setString(1, s_txt_voucher.getText());
          pst.setString(2, s_date.getText());
          pst.setString(3, s_txt_qtysale.getText());
          pst.setString(4, s_txt_price.getText());
          pst.setString(5, "Sale");
          pst.setString(6, s_txt_note.getText());
          pst.setString(7, model_name); 
          pst.setString(8, item_name); 
    pst.execute();
    
    String update_purchase ="Delete from sale where voucher_no = '"+s_txt_voucher.getText()+"' ";
    pst = conn.prepareStatement(update_purchase);
    pst.executeUpdate();
    
    JOptionPane.showMessageDialog(null, "Data Deleted");
    clearfield();
    updatetable();
    s_txt_note.setEditable(false);
    s_txt_note.setText("");
    }

    else{
        JOptionPane.showMessageDialog(null, "Error in insert");
    }
    
    }
    catch(SQLException ex){
        JOptionPane.showMessageDialog(null, ex);  
            }
}   

private void updatedata(){
    try{       

    String selectqty = "SELECT item.item_qty, model.qty, sale.item_id, sale.model_id, sale.customer_name, sale.voucher_no, sale.qty_sale "
            + "FROM item  INNER JOIN model ON item.item_id = model.item_id  INNER JOIN sale ON model.model_id = sale.model_id"
            + " where sale.voucher_no = '"+s_txt_voucher.getText()+"'";
    pst = conn.prepareStatement(selectqty);
    rs = pst.executeQuery();
    if(rs.next()){
    String model_id = rs.getString("sale.model_id");
    String item_id = rs.getString("sale.item_id");
    int apqty = Integer.parseInt(rs.getString("model.qty"));
    int amqty = Integer.parseInt(rs.getString("sale.qty_sale"));
    int imqty = Integer.parseInt(rs.getString("item.item_qty"));
    int cal1 = apqty+amqty;
    int cal2 = imqty+amqty;
    int nqty = Integer.parseInt(s_txt_qtysale.getText());
    int newvalue1 = cal1-nqty;
    int newvalue2 = cal2-nqty;
    String model_value = Integer.toString(newvalue1);
    String item_value = Integer.toString(newvalue2);
    
    String update_item ="UPDATE item set item_qty = '"+item_value+"'"
                        + " where item_id = '"+item_id+"' ";
    pst = conn.prepareStatement(update_item);
    pst.executeUpdate();
    
    String update_model ="UPDATE model set qty = '"+model_value+"' "
                        + " where model_id = '"+model_id+"' ";
    pst = conn.prepareStatement(update_model);
    pst.executeUpdate();
    
    String update_purchase ="UPDATE sale set customer_name = '"+ s_txt_customername.getText()+"', qty_sale = '"+s_txt_qtysale.getText()+"', "
                        + " date_sale = '"+s_txt_datesale.getText()+"', total_bill = '"+s_txt_totalbill.getText()+"', "
                        + " discount = '"+s_txt_discount.getText()+"' "
                        + " where voucher_no = '"+s_txt_voucher.getText()+"' ";
    pst = conn.prepareStatement(update_purchase);
    pst.executeUpdate();
    
    JOptionPane.showMessageDialog(null, "Data Updated");
    clearfield();
    windowopen();
    s_cb_category.setEnabled(true);
    s_cb_item.setEnabled(true);
    s_cb_model.setEnabled(true);

    }
    else{
        JOptionPane.showMessageDialog(null, "Error Connection With the Database");
    }
    }
    catch(SQLException ex){
        JOptionPane.showMessageDialog(null, ex);
    }
}

private void remove(){
    s_cb_category.removeAllItems();
    s_cb_item.removeAllItems();
    s_cb_model.removeAllItems();
        
    }
private void clearfield(){
    
    s_txt_qtysale.setText("");
    s_txt_price.setText("");
    s_txt_qtyavaiable.setText("");
    s_txt_discount.setText("");
    s_txt_totalbill.setText("");
    s_txt_customername.setText("");
    s_txt_voucher.setEditable(false);
    s_txt_qtyavaiable.setEditable(false);
    sale_updatebt.setEnabled(false);
    sale_deletebt.setEnabled(false);
    sale_savebt.setEnabled(true);
    sale_addbt.setEnabled(true);
    s_cb_category.setEnabled(true);
    s_cb_item.setEnabled(true);
    s_cb_model.setEnabled(true);
    s_txt_note.setEditable(false);
    windowopen();
    lb1.setText("");
    lb2.setText("");
    lb3.setText("");
    lbprice.setText("");
    lbcustomer.setText("");
    lbdiscount.setText("");
    lbtotal.setText("");
    s_txt_totalbill.setEditable(false);
    s_txt_discount.setText("0");
    s_txt_price.setEditable(false);
    lbqty.setText("");
}
private void discount(){
    String a = "";
    if(s_txt_discount.getText().equals(a)){
        s_txt_discount.setText("0");        
    }
}

 private void calculation(){
     String value = Integer.toString(Integer.parseInt(s_txt_price.getText())*(Integer.parseInt(s_txt_qtysale.getText()))-(Integer.parseInt(s_txt_discount.getText())));
     s_txt_totalbill.setText(value);
     lbqty.setText(s_txt_qtysale.getText());
     lbtotal.setText(value+".00");
     
 }
   private void windowopen(){
            try{
       
       String sql = "select max(voucher_no) from sale";
       pst = conn.prepareStatement(sql);
       rs=pst.executeQuery();
       
       /*Taking maximum voucher number from DB, converting it to integer type, 
       adding one, converting it back to string type and then pasting in
       voucher number text field*/
       if(rs.next()){
       String newv = rs.getString("max(voucher_no)");
       s_txt_voucher.setText(Integer.toString(Integer.parseInt(newv)+1));
       }
       else{
           s_txt_voucher.setText("1");
       }
      
      }
       catch(SQLException ex){
       JOptionPane.showMessageDialog(null,ex);
      
       }
       catch(Exception ex){
       JOptionPane.showMessageDialog(null,ex);    
       }  
    }
 
 

private void categorycb(){
    try{
    String selectcategory = "SELECT category_name from category";
    pst = conn.prepareStatement(selectcategory);
    rs = pst.executeQuery();
    while(rs.next()){
        String category = rs.getString("category_name");
        s_cb_category.addItem(category);
    }
    }
    catch(Exception e){
        JOptionPane.showMessageDialog(null, e);
    }
    
}

private void itemcb(){
    
    String sql1 = "select category_id from category where category_name = '"+s_cb_category.getSelectedItem()+"' ";
    try{
        pst = conn.prepareStatement(sql1);        
        rs = pst.executeQuery();
        rs.next();
        String sql2 = "select item_name from item where category_id = '"+rs.getString("category_id")+"' ";
        pst = conn.prepareStatement(sql2);        
        rs = pst.executeQuery();
        while(rs.next()){
            String item = rs.getString("item_name");
            s_cb_item.addItem(item);            
        }
        lb1.setText((String) s_cb_category.getSelectedItem());
    }
    catch(Exception e){
        JOptionPane.showMessageDialog(null, e);
    }
}
 private void modelbox(){
      
        String sql1 = "select item_id from item where item_name = '"+s_cb_item.getSelectedItem()+"' ";
    try{
        pst = conn.prepareStatement(sql1);        
        rs = pst.executeQuery();
        rs.next();
        String sql2 = "select model_no from model where item_id = '"+rs.getString("item_id")+"' ";
        pst = conn.prepareStatement(sql2);        
        rs = pst.executeQuery();
        while(rs.next()){
            String item = rs.getString("model_no");
            s_cb_model.addItem(item);            
        }  
        lb2.setText((String) s_cb_item.getSelectedItem());
    }
    catch(Exception e){
        JOptionPane.showMessageDialog(null, "Please First Select Category");        
    }
 }
 private void setvaluesoncb(){
     try{
          String selectmodel = "SELECT * FROM model where model_no = '"+s_cb_model.getSelectedItem()+"' ";
          pst = conn.prepareStatement(selectmodel);
          rs = pst.executeQuery();
          rs.next();
          s_txt_price.setText(rs.getString("sale_price"));
          s_txt_qtyavaiable.setText(rs.getString("qty"));
          s_txt_discount.setText(rs.getString("discount"));
          s_txt_price.setEditable(false);
          s_txt_qtyavaiable.setEditable(false);
          
          lb3.setText((String) s_cb_model.getSelectedItem());
          lbprice.setText(rs.getString("sale_price"));
     }
     catch(Exception e){
         JOptionPane.showMessageDialog(null, "Please First Select model");
     }
 }
  
       private void save() {
    try{
            int aqty = Integer.parseInt(s_txt_qtyavaiable.getText());
            int sqty = Integer.parseInt(s_txt_qtysale.getText());
            
            if(aqty > sqty){
                
                int tqty = aqty-sqty;
             try{   
            String updateitem = "UPDATE item set item_qty = '"+tqty+"' where item_name = '"+s_cb_item.getSelectedItem()+"' ";
            pst = conn.prepareStatement(updateitem);
            pst.execute();
             }
             catch(Exception e){
                 JOptionPane.showMessageDialog(null, "Item Update Error" +e);
             }
            try{
            String updatemodel = "UPDATE model set qty = '"+tqty+"' where model_no = '"+s_cb_model.getSelectedItem()+"' ";
            pst = conn.prepareStatement(updatemodel);
            pst.execute();
            }
            catch(Exception e){
                JOptionPane.showMessageDialog(null, "Model Update Error" +e);
            }
            try{
            String selectitemid = "SELECT item_id from item where item_name = '"+s_cb_item.getSelectedItem()+"' ";
            pst = conn.prepareStatement(selectitemid);
            rs = pst.executeQuery();
            rs.next();
            String itemid = rs.getString("item_id");
            
            String selectmodelid = "SELECT model_id from model where model_no = '"+s_cb_model.getSelectedItem()+"' ";
            pst = conn.prepareStatement(selectmodelid);
            rs = pst.executeQuery();
            rs.next();
            String modelid = rs.getString("model_id");
            
            String sql1 = "INSERT into sale (item_id,customer_name,qty_sale,date_sale,total_bill,voucher_no,model_id,discount) "
                                                + "values (?,?,?,?,?,?,?,?)";
            
            pst = conn.prepareStatement(sql1);
            pst.setString(1, itemid);
            pst.setString(2, s_txt_customername .getText());
            pst.setString(3, s_txt_qtysale.getText());
            pst.setString(4, s_txt_datesale.getText());
            pst.setString(5, s_txt_totalbill.getText());
            pst.setString(6, s_txt_voucher.getText());
            pst.setString(7, modelid);
            pst.setString(8, s_txt_discount.getText());            
            pst.execute();
            
            JOptionPane.showMessageDialog(null, "data saved");
            }
            catch(Exception e){
                JOptionPane.showMessageDialog(null, "Select erroe" +e);
            }
            }
            }
        catch(Exception e)
                {
                    JOptionPane.showMessageDialog(null,"Error in data saved method" + e);
                            
            
        }
       }
    private void updatetable(){
        
        try{
        String sql = "SELECT sale.date_sale, sale.voucher_no, sale.customer_name, category.category_name,"
                + " item.item_name, model.model_no, model.sale_price, sale.qty_sale, sale.discount, sale.total_bill"
                + " FROM category "
                + " INNER JOIN item ON category.category_id = item.category_id "
                + " INNER JOIN model ON item.item_id = model.item_id "
                + " INNER JOIN sale ON model.model_id = sale.model_id "
                + " where sale.date_sale = ? ";
        pst = conn.prepareStatement(sql);
        pst.setString(1, s_date.getText());
        rs = pst.executeQuery();
        s_table.setModel(DbUtils.resultSetToTableModel(rs));
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, e);
        }
    }
    private void searchrecord(){
        
        try{
        String sql = "SELECT sale.date_sale, sale.voucher_no, sale.customer_name, category.category_name,"
                + " item.item_name, model.model_no, model.sale_price, sale.qty_sale, sale.discount, sale.total_bill"
                + " FROM category "
                + " INNER JOIN item ON category.category_id = item.category_id "
                + " INNER JOIN model ON item.item_id = model.item_id "
                + " INNER JOIN sale ON model.model_id = sale.model_id "
                + " where sale.voucher_no = ? ";
        pst = conn.prepareStatement(sql);
        pst.setString(1, s_txt_searchvoucher.getText());
        rs = pst.executeQuery();
        s_table.setModel(DbUtils.resultSetToTableModel(rs));
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, e);
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

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        s_cb_category = new javax.swing.JComboBox();
        s_cb_item = new javax.swing.JComboBox();
        s_cb_model = new javax.swing.JComboBox();
        s_txt_customername = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        s_txt_datesale = new datechooser.beans.DateChooserCombo();
        jPanel3 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        s_txt_qtyavaiable = new javax.swing.JTextField();
        s_txt_price = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        s_txt_discount = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        s_txt_qtysale = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        s_txt_totalbill = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        s_table = new javax.swing.JTable()
        {public boolean isCellEditable(int rowIndex, int colIndex) {
            return false;
        }};
        jPanel5 = new javax.swing.JPanel();
        sale_exitbt = new javax.swing.JButton();
        sale_savebt = new javax.swing.JButton();
        sale_newbt = new javax.swing.JButton();
        sale_addbt = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lb1 = new javax.swing.JLabel();
        lb2 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        lbqty = new javax.swing.JLabel();
        lbprice = new javax.swing.JLabel();
        lb3 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        lbtotal = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        lbdiscount = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        lbcustomer = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        s_txt_voucher = new javax.swing.JTextField();
        s_date = new datechooser.beans.DateChooserCombo();
        jPanel8 = new javax.swing.JPanel();
        s_txt_searchvoucher = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        sale_searchbt = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        sale_deletebt = new javax.swing.JButton();
        sale_updatebt = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        s_txt_note = new javax.swing.JTextArea();
        jLabel16 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Sales Form");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(0, 204, 204));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Sale", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 16), new java.awt.Color(204, 0, 0))); // NOI18N

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Select Data", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14), new java.awt.Color(0, 153, 153))); // NOI18N

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setText("Date of Sale");

        s_cb_category.setToolTipText("Select Category");
        s_cb_category.setPreferredSize(new java.awt.Dimension(6, 30));
        s_cb_category.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                s_cb_categoryPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });
        s_cb_category.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                s_cb_categoryActionPerformed(evt);
            }
        });

        s_cb_item.setToolTipText("Select Category");
        s_cb_item.setPreferredSize(new java.awt.Dimension(6, 30));
        s_cb_item.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                s_cb_itemFocusGained(evt);
            }
        });
        s_cb_item.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                s_cb_itemPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });
        s_cb_item.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                s_cb_itemActionPerformed(evt);
            }
        });

        s_cb_model.setToolTipText("Select Category");
        s_cb_model.setPreferredSize(new java.awt.Dimension(6, 30));
        s_cb_model.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                s_cb_modelPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        s_txt_customername.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        s_txt_customername.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        s_txt_customername.setToolTipText("Click to change date");
        s_txt_datesale.setText(sdf.format(date));
        s_txt_customername.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                s_txt_customernameFocusLost(evt);
            }
        });
        s_txt_customername.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                s_txt_customernameActionPerformed(evt);
            }
        });

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel20.setText("Select Category");

        jLabel21.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel21.setText("Select item");

        jLabel22.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel22.setText("Select Model");

        jLabel23.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel23.setText("Customer name");

        s_txt_datesale.setCurrentView(new datechooser.view.appearance.AppearancesList("Light",
            new datechooser.view.appearance.ViewAppearance("custom",
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                    new java.awt.Color(0, 0, 0),
                    new java.awt.Color(0, 0, 255),
                    false,
                    true,
                    new datechooser.view.appearance.swing.ButtonPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                    new java.awt.Color(0, 0, 0),
                    new java.awt.Color(0, 0, 255),
                    true,
                    true,
                    new datechooser.view.appearance.swing.ButtonPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                    new java.awt.Color(0, 0, 255),
                    new java.awt.Color(0, 0, 255),
                    false,
                    true,
                    new datechooser.view.appearance.swing.ButtonPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                    new java.awt.Color(128, 128, 128),
                    new java.awt.Color(0, 0, 255),
                    false,
                    true,
                    new datechooser.view.appearance.swing.LabelPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                    new java.awt.Color(0, 0, 0),
                    new java.awt.Color(0, 0, 255),
                    false,
                    true,
                    new datechooser.view.appearance.swing.LabelPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                    new java.awt.Color(0, 0, 0),
                    new java.awt.Color(255, 0, 0),
                    false,
                    false,
                    new datechooser.view.appearance.swing.ButtonPainter()),
                (datechooser.view.BackRenderer)null,
                false,
                true)));
    s_txt_datesale.setFormat(2);

    javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
    jPanel2.setLayout(jPanel2Layout);
    jPanel2Layout.setHorizontalGroup(
        jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel2Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel22)
                        .addComponent(jLabel23))
                    .addGap(18, 18, 18)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(s_cb_model, 0, 138, Short.MAX_VALUE)
                        .addComponent(s_txt_customername)))
                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(jLabel7)
                        .addGap(18, 18, 18)
                        .addComponent(s_txt_datesale, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(jLabel21)
                            .addGap(18, 18, 18)
                            .addComponent(s_cb_item, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(jLabel20)
                            .addGap(18, 18, 18)
                            .addComponent(s_cb_category, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)))))
            .addContainerGap(20, Short.MAX_VALUE))
    );
    jPanel2Layout.setVerticalGroup(
        jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel2Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addComponent(s_txt_datesale, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel7))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(s_cb_category, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel20))
            .addGap(18, 18, 18)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(s_cb_item, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel21))
            .addGap(18, 18, 18)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(s_cb_model, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel22))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(s_txt_customername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel23))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Prices & Qty", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14), new java.awt.Color(0, 153, 153))); // NOI18N

    jLabel11.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    jLabel11.setText("Quantity Avaiable");

    s_txt_qtyavaiable.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
    s_txt_qtyavaiable.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    s_txt_qtyavaiable.setToolTipText("Quantity Avaiable for sale");

    s_txt_price.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
    s_txt_price.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    s_txt_price.setToolTipText("Price of single unit");
    s_txt_price.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            s_txt_priceActionPerformed(evt);
        }
    });

    jLabel12.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    jLabel12.setText("Price");

    s_txt_discount.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
    s_txt_discount.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    s_txt_discount.setToolTipText("Discount");
    s_txt_discount.addFocusListener(new java.awt.event.FocusAdapter() {
        public void focusLost(java.awt.event.FocusEvent evt) {
            s_txt_discountFocusLost(evt);
        }
    });
    s_txt_discount.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            s_txt_discountActionPerformed(evt);
        }
    });

    jLabel19.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    jLabel19.setText("Discount");

    s_txt_qtysale.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
    s_txt_qtysale.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    s_txt_qtysale.setToolTipText("Quntity Sale");
    s_txt_qtysale.addFocusListener(new java.awt.event.FocusAdapter() {
        public void focusLost(java.awt.event.FocusEvent evt) {
            s_txt_qtysaleFocusLost(evt);
        }
    });
    s_txt_qtysale.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            s_txt_qtysaleActionPerformed(evt);
        }
    });
    s_txt_qtysale.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyPressed(java.awt.event.KeyEvent evt) {
            s_txt_qtysaleKeyPressed(evt);
        }
    });

    jLabel14.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    jLabel14.setText("Quantity Sale");

    s_txt_totalbill.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
    s_txt_totalbill.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    s_txt_totalbill.setToolTipText("Quntity Sale");

    jLabel15.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    jLabel15.setText("Total Bill");

    javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
    jPanel3.setLayout(jPanel3Layout);
    jPanel3Layout.setHorizontalGroup(
        jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel3Layout.createSequentialGroup()
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(jLabel19, javax.swing.GroupLayout.Alignment.TRAILING)
                .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING)
                .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.TRAILING)
                .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.TRAILING))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                .addComponent(s_txt_qtysale, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                .addComponent(s_txt_discount, javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(s_txt_price, javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(s_txt_totalbill)
                .addComponent(s_txt_qtyavaiable, javax.swing.GroupLayout.Alignment.LEADING))
            .addContainerGap(38, Short.MAX_VALUE))
    );
    jPanel3Layout.setVerticalGroup(
        jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel3Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel11)
                .addComponent(s_txt_qtyavaiable, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(18, 18, 18)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel12)
                .addComponent(s_txt_price, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(18, 18, 18)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel19)
                .addComponent(s_txt_discount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(18, 18, 18)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel14)
                .addComponent(s_txt_qtysale, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(18, 18, 18)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(s_txt_totalbill, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel15))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Record", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14), new java.awt.Color(0, 153, 153))); // NOI18N

    s_table.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
            {null, null, null, null},
            {null, null, null, null},
            {null, null, null, null},
            {null, null, null, null}
        },
        new String [] {
            "Title 1", "Title 2", "Title 3", "Title 4"
        }
    ));
    s_table.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            s_tableMouseClicked(evt);
        }
    });
    jScrollPane2.setViewportView(s_table);

    javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
    jPanel4.setLayout(jPanel4Layout);
    jPanel4Layout.setHorizontalGroup(
        jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel4Layout.createSequentialGroup()
            .addComponent(jScrollPane2)
            .addContainerGap())
    );
    jPanel4Layout.setVerticalGroup(
        jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(329, 329, 329))
    );

    jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Buttons", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14))); // NOI18N

    sale_exitbt.setBackground(new java.awt.Color(255, 0, 0));
    sale_exitbt.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    sale_exitbt.setText("Exit");
    sale_exitbt.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            sale_exitbtActionPerformed(evt);
        }
    });

    sale_savebt.setBackground(new java.awt.Color(153, 255, 51));
    sale_savebt.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    sale_savebt.setText("Save");
    sale_savebt.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            sale_savebtActionPerformed(evt);
        }
    });
    sale_savebt.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyPressed(java.awt.event.KeyEvent evt) {
            sale_savebtKeyPressed(evt);
        }
    });

    sale_newbt.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    sale_newbt.setText("New");
    sale_newbt.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            sale_newbtActionPerformed(evt);
        }
    });
    sale_newbt.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyPressed(java.awt.event.KeyEvent evt) {
            sale_newbtKeyPressed(evt);
        }
    });

    sale_addbt.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    sale_addbt.setText("Add Another");
    sale_addbt.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            sale_addbtActionPerformed(evt);
        }
    });
    sale_addbt.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyPressed(java.awt.event.KeyEvent evt) {
            sale_addbtKeyPressed(evt);
        }
    });

    javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
    jPanel5.setLayout(jPanel5Layout);
    jPanel5Layout.setHorizontalGroup(
        jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
            .addGap(67, 67, 67)
            .addComponent(sale_addbt)
            .addGap(18, 18, 18)
            .addComponent(sale_newbt, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(sale_savebt, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(sale_exitbt, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap())
    );
    jPanel5Layout.setVerticalGroup(
        jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel5Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(sale_newbt, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(sale_savebt, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(sale_exitbt, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(sale_addbt, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addContainerGap(28, Short.MAX_VALUE))
    );

    jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Bill Summary", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 16))); // NOI18N

    jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
    jLabel1.setText("Computer Shop");

    jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    jLabel2.setText("Items");

    lb1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
    lb1.setPreferredSize(new java.awt.Dimension(43, 26));

    lb2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

    jLabel13.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    jLabel13.setText("Price");

    jLabel17.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    jLabel17.setText("Quantity Sale");

    jLabel6.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
    jLabel6.setText("Total");

    lbqty.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

    lbprice.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

    lb3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

    jLabel25.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
    jLabel25.setText("Khawaja Center Multan");

    lbtotal.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N

    jLabel24.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    jLabel24.setText("Discount");

    lbdiscount.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

    jLabel18.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    jLabel18.setText("Customer");

    lbcustomer.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

    javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
    jPanel6.setLayout(jPanel6Layout);
    jPanel6Layout.setHorizontalGroup(
        jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel6Layout.createSequentialGroup()
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                    .addGap(23, 23, 23)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lb1, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lb2, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lb3, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(18, 18, 18)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lbcustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel18))))
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel6Layout.createSequentialGroup()
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel25)
                    .addContainerGap())
                .addGroup(jPanel6Layout.createSequentialGroup()
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel6Layout.createSequentialGroup()
                            .addGap(17, 17, 17)
                            .addComponent(jLabel17))
                        .addGroup(jPanel6Layout.createSequentialGroup()
                            .addGap(18, 18, 18)
                            .addComponent(lbqty, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGap(17, 17, 17)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel6Layout.createSequentialGroup()
                            .addGap(1, 1, 1)
                            .addComponent(lbdiscount, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jLabel24))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel6Layout.createSequentialGroup()
                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(0, 50, Short.MAX_VALUE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                            .addComponent(lbprice, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addContainerGap())))))
        .addGroup(jPanel6Layout.createSequentialGroup()
            .addGap(74, 74, 74)
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jSeparator1)
                .addGroup(jPanel6Layout.createSequentialGroup()
                    .addComponent(jLabel6)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbtotal, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap())))
    );
    jPanel6Layout.setVerticalGroup(
        jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel1)
                .addComponent(jLabel25))
            .addGap(18, 18, 18)
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel2)
                .addComponent(jLabel13)
                .addComponent(jLabel17)
                .addComponent(jLabel24)
                .addComponent(jLabel18))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(lbqty, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lbprice, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lbdiscount, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lb1, javax.swing.GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE)
                .addComponent(lbcustomer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(lb2, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(lb3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(27, 27, 27)
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(lbtotal, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(33, 33, 33))
    );

    jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

    jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    jLabel4.setText("Voucher No :");

    jLabel3.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
    jLabel3.setText("Date :");

    s_txt_voucher.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N

    s_date.setCurrentView(new datechooser.view.appearance.AppearancesList("Light",
        new datechooser.view.appearance.ViewAppearance("custom",
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.ButtonPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(0, 0, 255),
                true,
                true,
                new datechooser.view.appearance.swing.ButtonPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                new java.awt.Color(0, 0, 255),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.ButtonPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                new java.awt.Color(128, 128, 128),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.LabelPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.LabelPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(255, 0, 0),
                false,
                false,
                new datechooser.view.appearance.swing.ButtonPainter()),
            (datechooser.view.BackRenderer)null,
            false,
            true)));
s_date.setNothingAllowed(false);
s_date.setFormat(2);
s_date.setLocked(true);

javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
jPanel7.setLayout(jPanel7Layout);
jPanel7Layout.setHorizontalGroup(
    jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
        .addGap(65, 65, 65)
        .addComponent(jLabel3)
        .addGap(18, 18, 18)
        .addComponent(s_date, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addComponent(jLabel4)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(s_txt_voucher, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGap(43, 43, 43))
    );
    jPanel7Layout.setVerticalGroup(
        jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel7Layout.createSequentialGroup()
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel7Layout.createSequentialGroup()
                    .addGap(14, 14, 14)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4)
                        .addComponent(s_txt_voucher, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(jPanel7Layout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(s_date, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addContainerGap())
    );

    jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Search sale record", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14))); // NOI18N

    s_txt_searchvoucher.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
    s_txt_searchvoucher.addFocusListener(new java.awt.event.FocusAdapter() {
        public void focusGained(java.awt.event.FocusEvent evt) {
            s_txt_searchvoucherFocusGained(evt);
        }
    });
    s_txt_searchvoucher.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyPressed(java.awt.event.KeyEvent evt) {
            s_txt_searchvoucherKeyPressed(evt);
        }
    });

    jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    jLabel5.setText("Voucher No :");

    sale_searchbt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/button_plus_pink.png"))); // NOI18N
    sale_searchbt.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            sale_searchbtActionPerformed(evt);
        }
    });
    sale_searchbt.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyPressed(java.awt.event.KeyEvent evt) {
            sale_searchbtKeyPressed(evt);
        }
    });

    javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
    jPanel8.setLayout(jPanel8Layout);
    jPanel8Layout.setHorizontalGroup(
        jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel8Layout.createSequentialGroup()
            .addGap(66, 66, 66)
            .addComponent(jLabel5)
            .addGap(39, 39, 39)
            .addComponent(s_txt_searchvoucher, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(43, 43, 43)
            .addComponent(sale_searchbt, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(37, Short.MAX_VALUE))
    );
    jPanel8Layout.setVerticalGroup(
        jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel8Layout.createSequentialGroup()
            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(s_txt_searchvoucher, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel5)
                .addComponent(sale_searchbt, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(0, 0, Short.MAX_VALUE))
    );

    jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Buttons", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14))); // NOI18N

    sale_deletebt.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    sale_deletebt.setText("Delete");
    sale_deletebt.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            sale_deletebtActionPerformed(evt);
        }
    });
    sale_deletebt.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyPressed(java.awt.event.KeyEvent evt) {
            sale_deletebtKeyPressed(evt);
        }
    });

    sale_updatebt.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    sale_updatebt.setText("Edit");
    sale_updatebt.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            sale_updatebtActionPerformed(evt);
        }
    });
    sale_updatebt.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyPressed(java.awt.event.KeyEvent evt) {
            sale_updatebtKeyPressed(evt);
        }
    });

    javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
    jPanel9.setLayout(jPanel9Layout);
    jPanel9Layout.setHorizontalGroup(
        jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel9Layout.createSequentialGroup()
            .addGap(31, 31, 31)
            .addComponent(sale_deletebt, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 72, Short.MAX_VALUE)
            .addComponent(sale_updatebt, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(29, 29, 29))
    );
    jPanel9Layout.setVerticalGroup(
        jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(sale_deletebt, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(sale_updatebt, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addContainerGap())
    );

    jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Note For delete", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14))); // NOI18N

    s_txt_note.setColumns(20);
    s_txt_note.setRows(5);
    jScrollPane1.setViewportView(s_txt_note);

    jLabel16.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    jLabel16.setText("Note");

    javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
    jPanel10.setLayout(jPanel10Layout);
    jPanel10Layout.setHorizontalGroup(
        jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
            .addGap(18, 18, 18)
            .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
            .addContainerGap())
    );
    jPanel10Layout.setVerticalGroup(
        jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel10Layout.createSequentialGroup()
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(0, 0, Short.MAX_VALUE))
        .addGroup(jPanel10Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel1Layout.createSequentialGroup()
            .addGap(18, 18, 18)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                    .addGap(4, 4, 4)
                    .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap())
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGap(10, 10, 10))
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap())))
    );
    jPanel1Layout.setVerticalGroup(
        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel1Layout.createSequentialGroup()
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGap(9, 9, 9)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
    );

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    pack();
    setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
    categorycb();      
    windowopen();
    updatetable();
    clearfield();
    s_cb_category.requestFocusInWindow();
    }//GEN-LAST:event_formWindowOpened

    private void sale_exitbtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sale_exitbtActionPerformed
        int p = JOptionPane.showConfirmDialog(null, "Confirm For Exit","Exit",JOptionPane.YES_NO_CANCEL_OPTION);
        if(p == 0){
            this.dispose();
        }  
    }//GEN-LAST:event_sale_exitbtActionPerformed

    private void sale_savebtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sale_savebtActionPerformed
        save();  
        clearfield();
        windowopen();
        updatetable();
        s_cb_category.requestFocusInWindow();
    }//GEN-LAST:event_sale_savebtActionPerformed

    private void s_txt_qtysaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_s_txt_qtysaleActionPerformed
        sale_savebt.requestFocusInWindow();
    }//GEN-LAST:event_s_txt_qtysaleActionPerformed

    private void s_txt_priceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_s_txt_priceActionPerformed
        s_txt_discount.requestFocusInWindow();
    }//GEN-LAST:event_s_txt_priceActionPerformed

    private void s_txt_discountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_s_txt_discountActionPerformed
        sale_savebt.requestFocusInWindow();
    }//GEN-LAST:event_s_txt_discountActionPerformed

    private void sale_savebtKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_sale_savebtKeyPressed
        int key = evt.getKeyCode();
        if(key == VK_ENTER){
        save();  
        clearfield();
        windowopen();
        updatetable();
        s_cb_category.requestFocusInWindow();
        }
    }//GEN-LAST:event_sale_savebtKeyPressed

    private void s_txt_qtysaleFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_s_txt_qtysaleFocusLost
lbdiscount.setText(s_txt_discount.getText());
        int aqty = Integer.parseInt(s_txt_qtyavaiable.getText());
            int sqty = Integer.parseInt(s_txt_qtysale.getText());
            int dis = Integer.parseInt(s_txt_discount.getText());
            int price = Integer.parseInt(s_txt_price.getText());
            if(aqty < sqty ){
            JOptionPane.showMessageDialog(null, "Please enter valid Quantity");
            s_txt_qtysale.setText("");
            }
            if (dis > price){
            JOptionPane.showMessageDialog(null, "Please enter valid Discount");
            s_txt_discount.setText("");
            }
            else{
                calculation();
            }
    }//GEN-LAST:event_s_txt_qtysaleFocusLost

    private void s_txt_discountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_s_txt_discountFocusLost
        int dis = Integer.parseInt(s_txt_discount.getText());
            int price = Integer.parseInt(s_txt_price.getText());
            if (dis > price){
            JOptionPane.showMessageDialog(null, "Please enter valid Discount");
            s_txt_discount.setText("");
            
            }
            discount();
            lbdiscount.setText(s_txt_discount.getText());
    }//GEN-LAST:event_s_txt_discountFocusLost

    private void sale_updatebtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sale_updatebtActionPerformed
        updatedata();
    }//GEN-LAST:event_sale_updatebtActionPerformed

    private void sale_updatebtKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_sale_updatebtKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_sale_updatebtKeyPressed

    private void sale_newbtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sale_newbtActionPerformed
        clearfield();
    }//GEN-LAST:event_sale_newbtActionPerformed

    private void sale_newbtKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_sale_newbtKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_sale_newbtKeyPressed

    private void sale_deletebtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sale_deletebtActionPerformed
        deletedata();
    }//GEN-LAST:event_sale_deletebtActionPerformed

    private void sale_deletebtKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_sale_deletebtKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_sale_deletebtKeyPressed

    private void sale_addbtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sale_addbtActionPerformed
        save();  
        clearfield();
        clearfield();
        s_cb_category.requestFocusInWindow();
    }//GEN-LAST:event_sale_addbtActionPerformed

    private void sale_addbtKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_sale_addbtKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_sale_addbtKeyPressed

    private void s_txt_customernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_s_txt_customernameActionPerformed
        s_txt_qtysale.requestFocusInWindow();
    }//GEN-LAST:event_s_txt_customernameActionPerformed

    private void s_tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_s_tableMouseClicked
         if (evt.getClickCount() == 2) {
             int p = JOptionPane.showConfirmDialog(null, "Confirm for update","Update",JOptionPane.YES_NO_OPTION);
        if(p == 0){
   
        int row = s_table.getSelectedRow();
        String date         = (s_table.getModel().getValueAt(row, 0).toString());
        String voucher      = (s_table.getModel().getValueAt(row, 1).toString());
        String customer    = (s_table.getModel().getValueAt(row, 2).toString());
        String category     = (s_table.getModel().getValueAt(row, 3).toString());
        String item         = (s_table.getModel().getValueAt(row, 4).toString());
        String model        = (s_table.getModel().getValueAt(row, 5).toString());
        String sale_price   = (s_table.getModel().getValueAt(row, 6).toString());
        String qty          = (s_table.getModel().getValueAt(row, 7).toString());
        String discount     = (s_table.getModel().getValueAt(row, 8).toString());
        String total        = (s_table.getModel().getValueAt(row, 9).toString());
        
        s_txt_datesale.setText(date);
        s_txt_voucher.setText(voucher);
        s_txt_customername.setText(customer);
        s_txt_price.setText(sale_price);
        s_txt_qtysale.setText(qty);
        s_txt_totalbill.setText(total);
        s_txt_discount.setText(discount);
        sale_savebt.setEnabled(false);
        sale_updatebt.setEnabled(true);
        sale_deletebt.setEnabled(true);

        
        sale_addbt.setEnabled(false);
        s_cb_category.setEnabled(false);
        s_cb_item.setEnabled(false);
        s_cb_model.setEnabled(false);
        s_txt_note.setEditable(true);
        s_txt_note.setText("");
        }
   }
    }//GEN-LAST:event_s_tableMouseClicked

    private void s_cb_categoryPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_s_cb_categoryPopupMenuWillBecomeInvisible
        itemcb();
    }//GEN-LAST:event_s_cb_categoryPopupMenuWillBecomeInvisible

    private void s_cb_itemPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_s_cb_itemPopupMenuWillBecomeInvisible
        modelbox();
    }//GEN-LAST:event_s_cb_itemPopupMenuWillBecomeInvisible

    private void s_cb_modelPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_s_cb_modelPopupMenuWillBecomeInvisible
        setvaluesoncb();
        s_txt_customername.requestFocusInWindow();
    }//GEN-LAST:event_s_cb_modelPopupMenuWillBecomeInvisible

    private void s_txt_customernameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_s_txt_customernameFocusLost
        lbcustomer.setText(s_txt_customername.getText());        
    }//GEN-LAST:event_s_txt_customernameFocusLost

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        int p = JOptionPane.showConfirmDialog(null, "Confirm For Exit","Exit",JOptionPane.YES_NO_CANCEL_OPTION);
        if(p == 0){
            this.dispose();
        }  
    }//GEN-LAST:event_formWindowClosing

    private void s_cb_categoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_s_cb_categoryActionPerformed
                s_cb_item.removeAllItems();
    }//GEN-LAST:event_s_cb_categoryActionPerformed

    private void s_cb_itemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_s_cb_itemActionPerformed
                s_cb_model.removeAllItems();
    }//GEN-LAST:event_s_cb_itemActionPerformed

    private void s_cb_itemFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_s_cb_itemFocusGained
        s_cb_model.removeAllItems();
    }//GEN-LAST:event_s_cb_itemFocusGained

    private void sale_searchbtKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_sale_searchbtKeyPressed
        int key = evt.getKeyCode();
        if(key == VK_ENTER){
            searchrecord();
        }
    }//GEN-LAST:event_sale_searchbtKeyPressed

    private void sale_searchbtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sale_searchbtActionPerformed
        searchrecord();
    }//GEN-LAST:event_sale_searchbtActionPerformed

    private void s_txt_searchvoucherKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_s_txt_searchvoucherKeyPressed
        int key = evt.getKeyCode();
        if(key == VK_ENTER){
            searchrecord();
        }
    }//GEN-LAST:event_s_txt_searchvoucherKeyPressed

    private void s_txt_searchvoucherFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_s_txt_searchvoucherFocusGained
        s_txt_searchvoucher.setText("");
    }//GEN-LAST:event_s_txt_searchvoucherFocusGained

    private void s_txt_qtysaleKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_s_txt_qtysaleKeyPressed
        int key = evt.getKeyCode();
        if(key == VK_ENTER){
        
        lbdiscount.setText(s_txt_discount.getText());
        int aqty = Integer.parseInt(s_txt_qtyavaiable.getText());
            int sqty = Integer.parseInt(s_txt_qtysale.getText());
            int dis = Integer.parseInt(s_txt_discount.getText());
            int price = Integer.parseInt(s_txt_price.getText());
            if(aqty < sqty ){
            JOptionPane.showMessageDialog(null, "Please enter valid Quantity");
            s_txt_qtysale.setText("");
            }
            if (dis > price){
            JOptionPane.showMessageDialog(null, "Please enter valid Discount");
            s_txt_discount.setText("");
            }
            else{
                calculation();
            }
        }
    }//GEN-LAST:event_s_txt_qtysaleKeyPressed

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lb1;
    private javax.swing.JLabel lb2;
    private javax.swing.JLabel lb3;
    private javax.swing.JLabel lbcustomer;
    private javax.swing.JLabel lbdiscount;
    private javax.swing.JLabel lbprice;
    private javax.swing.JLabel lbqty;
    private javax.swing.JLabel lbtotal;
    private javax.swing.JComboBox s_cb_category;
    private javax.swing.JComboBox s_cb_item;
    private javax.swing.JComboBox s_cb_model;
    private datechooser.beans.DateChooserCombo s_date;
    private javax.swing.JTable s_table;
    private javax.swing.JTextField s_txt_customername;
    private datechooser.beans.DateChooserCombo s_txt_datesale;
    private javax.swing.JTextField s_txt_discount;
    private javax.swing.JTextArea s_txt_note;
    private javax.swing.JTextField s_txt_price;
    private javax.swing.JTextField s_txt_qtyavaiable;
    private javax.swing.JTextField s_txt_qtysale;
    private javax.swing.JTextField s_txt_searchvoucher;
    private javax.swing.JTextField s_txt_totalbill;
    private javax.swing.JTextField s_txt_voucher;
    private javax.swing.JButton sale_addbt;
    private javax.swing.JButton sale_deletebt;
    private javax.swing.JButton sale_exitbt;
    private javax.swing.JButton sale_newbt;
    private javax.swing.JButton sale_savebt;
    private javax.swing.JButton sale_searchbt;
    private javax.swing.JButton sale_updatebt;
    // End of variables declaration//GEN-END:variables
}
