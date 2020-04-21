
import java.awt.*;
import static java.awt.event.KeyEvent.VK_ENTER;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
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
public class purchase extends javax.swing.JFrame {
Connection conn = null;
ResultSet rs = null;
PreparedStatement pst = null;
        
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

    /**
     * Creates new form purchase
     */
    public purchase() {
        initComponents();
        conn =sqlconnect.ConnectDB();
    }
    
    private void categorycombobox(){
        String sql1 = "select category_name from category ";
    try {
        pst = conn.prepareStatement(sql1);        
        rs = pst.executeQuery();
        while(rs.next()){
            String item = rs.getString("category_name");
            p_cb_category.addItem(item);
        }
     }
    catch(Exception e){
        JOptionPane.showMessageDialog(null, e);
    }
    
    }
private void deletedata(){
    try{       
        
    String selectqty = "SELECT item.item_id, item.item_qty, model.model_id, model.qty, purchase.qty_purchase "
            + "FROM item "
            + "INNER JOIN model ON item.item_id = model.item_id "
            + "INNER JOIN purchase ON model.model_id = purchase.model_id "
            + "WHERE purchase.voucher_no = '"+p_txt_voucher.getText()+"' ";
    pst = conn.prepareStatement(selectqty);
    rs = pst.executeQuery();
    
    if(rs.next()){
    String model_name = rs.getString("model.model_id");
    String item_name = rs.getString("item.item_id");
    int apqty = Integer.parseInt(rs.getString("model.qty"));
    int amqty = Integer.parseInt(rs.getString("purchase.qty_purchase"));
    int imqty = Integer.parseInt(rs.getString("item.item_qty"));

    int cal1 = apqty-amqty;
    int cal2 = imqty-amqty;
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
          pst.setString(1, p_txt_voucher.getText());
          pst.setString(2, p_date.getText());
          pst.setString(3, p_txt_qtypurchase.getText());
          pst.setString(4, p_txt_pprice.getText());
          pst.setString(5, "Purchase");
          pst.setString(6, p_txt_note.getText());
          pst.setString(7, model_name); 
          pst.setString(8, item_name); 
    pst.execute();
    
    String update_purchase ="Delete from purchase where voucher_no = '"+p_txt_voucher.getText()+"' ";
    pst = conn.prepareStatement(update_purchase);
    pst.executeUpdate();
    
    JOptionPane.showMessageDialog(null, "Data Deleted");
    textfield();
    updatetable();
    p_txt_note.setEditable(false);
    p_txt_note.setText("");
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
        
    String selectqty = "SELECT category.category_name, item.item_name, item.item_qty, model.qty, model.model_no, purchase.qty_purchase "
            + " FROM item INNER JOIN category ON category.category_id = item.category_id INNER JOIN model ON item.item_id = model.item_id "
            + " INNER JOIN purchase ON model.model_id = purchase.model_id"
            + " WHERE purchase.voucher_no = '"+p_txt_voucher.getText()+"' ";
    pst = conn.prepareStatement(selectqty);
    rs = pst.executeQuery();
    
    if(rs.next()){
    String model_name = rs.getString("model.model_no");
    String category_name = rs.getString("category.category_name");
    String item_name = rs.getString("item.item_name");
    int apqty = Integer.parseInt(rs.getString("model.qty"));
    int amqty = Integer.parseInt(rs.getString("purchase.qty_purchase"));
    int imqty = Integer.parseInt(rs.getString("item.item_qty"));
    int cal1 = apqty-amqty;
    int cal2 = imqty-amqty;
    int nqty = Integer.parseInt(p_txt_qtypurchase.getText());
    int newvalue1 = cal1+nqty;
    int newvalue2 = cal2+nqty;
    String model_value = Integer.toString(newvalue1);
    String item_value = Integer.toString(newvalue2);
    String update_category ="UPDATE category set category_name = '"+p_txt_ncategory.getText()+"' "
                             + "where category_name = '"+category_name+"' ";
    pst = conn.prepareStatement(update_category);
    pst.executeUpdate();
    
    String update_item ="UPDATE item set item_name = '"+p_txt_nitemname.getText()+"', item_qty = '"+item_value+"'"
                        + " where item_name = '"+item_name+"' ";
    pst = conn.prepareStatement(update_item);
    pst.executeUpdate();
    
    String update_model ="UPDATE model set model_no = '"+ p_txt_nmodel.getText()+"', qty = '"+model_value+"', "
                        + " sale_price = '"+p_txt_saleprice.getText()+"', discount = '"+p_txt_discount.getText()+"' "
                        + " where model_no = '"+model_name+"' ";
    pst = conn.prepareStatement(update_model);
    pst.executeUpdate();
    
    String update_purchase ="UPDATE purchase set qty_purchase = '"+ p_txt_qtypurchase.getText()+"', date_purchase = '"+p_txt_datepurchase.getText()+"', "
                        + " supplier_name = '"+p_txt_sname.getText()+"', total_bill = '"+p_txt_totalbill.getText()+"', "
                        + "item_purchase_price = '"+p_txt_pprice.getText()+"', discount = '"+p_txt_discount.getText()+"' "
                        + " where voucher_no = '"+p_txt_voucher.getText()+"' ";
    pst = conn.prepareStatement(update_purchase);
    pst.executeUpdate();
    
    JOptionPane.showMessageDialog(null, "Data Updated");
    textfield();
    windowopen();
    p_txt_note.setEditable(false);
    }
    else{
        JOptionPane.showMessageDialog(null, "Error Connection With the Database");
    }
    }
    catch(SQLException ex){
        JOptionPane.showMessageDialog(null, ex);
    }
}
private void discount(){
    String a = "";
    if(p_txt_discount.getText().equals(a)){
        p_txt_discount.setText("0");        
    }
}
 private void calculation(){
     String value = Integer.toString(Integer.parseInt(p_txt_pprice.getText())*(Integer.parseInt(p_txt_qtypurchase.getText()))-(Integer.parseInt(p_txt_discount.getText())));
     p_txt_totalbill.setText(value);
 }
  private void checkbox3(){
        if(p_chb_model.isSelected()){        
           
           p_cb_model.setEnabled(false);
           p_chb_model.setSelected(true);
           p_txt_nmodel.setEditable(true);
           p_txt_nmodel.setText("");
           p_txt_saleprice.setEditable(true);
        }
        else{
            
             p_chb_model.setSelected(false);
           p_cb_model.setEnabled(true);
           p_txt_nmodel.setEditable(false);
           p_txt_nmodel.setText("");
           p_txt_saleprice.setEditable(false);
        }
    }
    private void checkbox2(){
        if(p_chb_item.isSelected()){
           p_cb_item.setEnabled(false);
           p_txt_nitemname.setEditable(true);
           p_txt_nitemname.setText("");
           
           p_chb_model.setEnabled(false);
           p_cb_model.setEnabled(false);
           p_chb_model.setSelected(true);
           p_txt_nmodel.setEditable(true);
           p_txt_nmodel.setText("");
        }
        else{
            p_cb_item.setEnabled(true);
            p_txt_nitemname.setEditable(false);
            p_txt_nitemname.setText("");
            
            p_chb_model.setEnabled(true);
             p_chb_model.setSelected(false);
           p_cb_model.setEnabled(true);
           p_txt_nmodel.setEditable(false);
           p_txt_nmodel.setText("");
        }
    }
   private void checkbox1(){
        if(p_chb_category.isSelected()){
            p_cb_category.setEnabled(false);
            p_txt_ncategory.setEditable(true);
            p_txt_ncategory.setText("");
            
            p_chb_item.setEnabled(false);
            p_cb_item.setEnabled(false);
           p_chb_item.setSelected(true);           
           p_txt_nitemname.setEditable(true);
           p_txt_nitemname.setText("");
           
           p_chb_model.setEnabled(false);
           p_cb_model.setEnabled(false);
           p_chb_model.setSelected(true);
           p_txt_nmodel.setEditable(true);
           p_txt_nmodel.setText("");
           
        }
        else{
            p_cb_category.setEnabled(true);
            p_txt_ncategory.setEditable(false);
            p_txt_ncategory.setText("");
            
            p_chb_item.setEnabled(true);
            p_chb_item.setSelected(false);
            p_cb_item.setEnabled(true);
            p_txt_nitemname.setEditable(false);
            p_txt_nitemname.setText("");            
            
            p_chb_model.setEnabled(true);
            p_chb_model.setSelected(false);
           p_cb_model.setEnabled(true);
           p_txt_nmodel.setEditable(false);
           p_txt_nmodel.setText("");
        }
    }
   
    private void itemcombox(){
      
        
       
    try{

        String sql1 = "select category_id from category where category_name = '"+p_cb_category.getSelectedItem()+"' ";
        pst = conn.prepareStatement(sql1);        
        rs = pst.executeQuery();
        rs.next();
       int ab = Integer.parseInt(rs.getString("category_id"));
  
        String sql2 = "select item_name from item where category_id = '"+ab+"' ";
        pst = conn.prepareStatement(sql2);        
        rs = pst.executeQuery();
        while(rs.next()){
            String item = rs.getString("item_name");
            p_cb_item.addItem(item);    
    }
    }
    
    catch(SQLException e){
        JOptionPane.showMessageDialog(null, e + " Item Name");        
    }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, e + " Normal Exception");
        }
        
 }
    private void modelbox(){
      
        String sql1 = "select item_id from item where item_name = '"+p_cb_item.getSelectedItem()+"' ";
    try{
        pst = conn.prepareStatement(sql1);        
        rs = pst.executeQuery();
        rs.next();
        String sql2 = "select model_no from model where item_id = '"+rs.getString("item_id")+"' ";
        pst = conn.prepareStatement(sql2);        
        rs = pst.executeQuery();
        while(rs.next()){
            String item = rs.getString("model_no");
            p_cb_model.addItem(item);            
        }        
    }
    catch(Exception e){
        JOptionPane.showMessageDialog(null, "Please First Select Category");        
    }
 }
 private void setvaluesoncb(){
     try{
          String selectmodel = "SELECT * FROM model where model_no = '"+p_cb_model.getSelectedItem()+"' ";
          pst = conn.prepareStatement(selectmodel);
          rs = pst.executeQuery();
          rs.next();
          p_txt_saleprice.setText(rs.getString("sale_price"));
          p_txt_qtystock.setText(rs.getString("qty"));
          p_txt_qtystock.setEditable(false);
          p_txt_saleprice.setEditable(false);
     }
     catch(Exception e){
         JOptionPane.showMessageDialog(null, "Please First Select Item");
     }
 }    
private void modelboxwinopen(){     
        
    try{
        String sql2 = "select category_name from category ";
        pst = conn.prepareStatement(sql2);        
        rs = pst.executeQuery();
        while(rs.next()){
            String item = rs.getString("category_name");
            p_cb_category1.addItem(item);            
        }        
    }
    catch(Exception e){
        JOptionPane.showMessageDialog(null, e);        
    }
 }
private void modelboxaction(){     
        
    try{
        String sql = "select * from model where model_no = '"+p_cb_cmodel.getSelectedItem()+"' ";
        pst = conn.prepareStatement(sql);        
        rs = pst.executeQuery();
        rs.next();
        p_txt_nsellingprice.setText(rs.getString("sale_price"));
    }
    catch(SQLException e){
        JOptionPane.showMessageDialog(null, e);        
    }
 }

private void purchase_up(){
    try{
        String sql = "UPDATE model set sale_price = '"+p_txt_nsellingprice.getText()+"' "
                + "   WHERE model_no = '"+p_cb_cmodel.getSelectedItem()+"' ";
        pst = conn.prepareStatement(sql);
        pst.executeUpdate();
        JOptionPane.showMessageDialog(null, "Data Updateted");       
    }
    catch(SQLException e){
        JOptionPane.showMessageDialog(null, e);
    }
}
   
    private void textfield(){
        p_txt_ncategory.setText("");
        p_txt_nitemname.setText("");
        p_txt_nmodel.setText("");
            p_txt_pprice.setText("");
            p_txt_totalbill.setText("");
            p_txt_discount.setText("");
            p_txt_saleprice.setText("");
            p_txt_qtypurchase.setText("");
            p_txt_sname.setText("");
            p_txt_qtystock.setText("");
            
            
        p_txt_saleprice.setEditable(true);
        p_txt_ncategory.setEditable(false);
        p_txt_nitemname.setEditable(false);
        p_txt_nmodel.setEditable(false);
        p_txt_voucher.setEditable(false);
        p_txt_totalbill.setEditable(false);
        p_txt_qtystock.setEditable(false);
        p_txt_discount.setText("0");
        
        purchase_updatebt.setEnabled(false);
        purchase_deletebt.setEnabled(false);
        p_txt_note.setEditable(false);
        
        purchase_savebt.setEnabled(true);
        p_chb_category.setSelected(false);
       p_chb_item.setSelected(false);
       p_chb_model.setSelected(false);
       p_chb_category.setEnabled(true);
        p_chb_item.setEnabled(true);
        p_chb_model.setEnabled(true);
        p_cb_category.setEnabled(true);
        p_cb_item.setEnabled(true);
        p_cb_model.setEnabled(true);
        purchase_updatep.setEnabled(true);
        p_txt_note.setText("");
            
    }
private void textfield1(){
        p_txt_ncategory.setText("");
        p_txt_nitemname.setText("");
        p_txt_nmodel.setText("");
            p_txt_pprice.setText("");
            p_txt_totalbill.setText("");
            p_txt_discount.setText("");
            p_txt_saleprice.setText("");
            p_txt_qtypurchase.setText("");
            p_txt_sname.setText("");
            p_txt_qtystock.setText("");
            
            
        p_txt_saleprice.setEditable(true);
        p_txt_ncategory.setEditable(false);
        p_txt_nitemname.setEditable(false);
        p_txt_nmodel.setEditable(false);
        p_txt_voucher.setEditable(false);
        p_txt_totalbill.setEditable(false);
        p_txt_qtystock.setEditable(false);
        p_txt_discount.setText("0");
        
        purchase_updatebt.setEnabled(false);
        purchase_deletebt.setEnabled(false);
        p_txt_note.setEditable(false);
        
        purchase_savebt.setEnabled(true);
        purchase_updatep.setEnabled(true);
        p_txt_note.setText("");
            
    }
    
    /*
   This method is used to check if the new category is inserted or the previous one is selected
   and than insert into item and check the new item is inserted or not...
   */
 private void categoryinsert(){
     try{
         /*
         if the main category is seleted than new category inserted into the category table
         than the generated id is inserted into the item table and the new entry made in item table also
         after that a new entry in made in purchase table.
         */
         if(p_chb_category.isSelected()){
             
         String insertcategory = "INSERT into category (category_name) VALUES (?)";
          pst = conn.prepareStatement(insertcategory, Statement.RETURN_GENERATED_KEYS); 
          pst.setString(1, p_txt_ncategory.getText());
          pst.executeUpdate();  
          rs = pst.getGeneratedKeys();    
          rs.next();  
          int key1 = rs.getInt(1);
          
          
          String insertitem = "INSERT into item (item_name,item_qty,category_id) VALUES (?,?,?)";
          pst = conn.prepareStatement(insertitem, Statement.RETURN_GENERATED_KEYS);
          
          pst.setString(1, p_txt_nitemname.getText());
          pst.setString(2, p_txt_qtypurchase.getText());
          pst.setInt(3, key1);
          pst.executeUpdate();
          rs = pst.getGeneratedKeys();
          rs.next();
          int key2 = rs.getInt(1);
          
          String insertmodel = "INSERT into model (item_id,model_no,qty,sale_price,discount) VALUES (?,?,?,?,?)";
          pst = conn.prepareStatement(insertmodel, Statement.RETURN_GENERATED_KEYS);
          
          pst.setInt(1, key2);
          pst.setString(2, p_txt_nmodel.getText());
          pst.setString(3, p_txt_qtypurchase.getText());
          pst.setString(4, p_txt_saleprice.getText());
          pst.setString(5, p_txt_discount.getText());
          pst.executeUpdate();
          rs = pst.getGeneratedKeys();
          rs.next();
          int key3 = rs.getInt(1);
          
            String insertpurchase = "INSERT into purchase (item_id,qty_purchase,date_purchase,supplier_name,total_bill,item_purchase_price,model_id,voucher_no,discount) VALUES (?,?,?,?,?,?,?,?,?)";
            pst = conn.prepareStatement(insertpurchase);
            pst.setInt(1, key2);
            pst.setString(2, p_txt_qtypurchase.getText());
            pst.setString(3, p_txt_datepurchase.getText());
            pst.setString(4, p_txt_sname.getText());
            pst.setString(5, p_txt_totalbill.getText());
            pst.setString(6, p_txt_pprice.getText());
            pst.setInt(7, key3);
            pst.setString(8, p_txt_voucher.getText());
            pst.setString(9, p_txt_discount.getText());           
            pst.execute();
            
            JOptionPane.showMessageDialog(null, "data enter");
         }
         /*
         if the 
         */
         else{
                
                if(p_chb_item.isSelected()){
           
          String select = "SELECT * FROM category where category_name = '"+p_cb_category.getSelectedItem()+"' ";
          pst = conn.prepareStatement(select);
          rs = pst.executeQuery();
          rs.next();
          String categoryid = rs.getString("category_id");
          
          String insertitem = "INSERT into item (item_name,item_qty,category_id) VALUES (?,?,?)";
          pst = conn.prepareStatement(insertitem, Statement.RETURN_GENERATED_KEYS);
          
          pst.setString(1, p_txt_nitemname.getText());
          pst.setString(2, p_txt_qtypurchase.getText());
          pst.setString(3, categoryid);
          pst.executeUpdate();
          rs = pst.getGeneratedKeys();
          rs.next();
          int key2 = rs.getInt(1);
          
          String insertmodel = "INSERT into model (item_id,model_no,qty,sale_price,discount) VALUES (?,?,?,?,?)";
          pst = conn.prepareStatement(insertmodel, Statement.RETURN_GENERATED_KEYS);
          
          pst.setInt(1, key2);
          pst.setString(2, p_txt_nmodel.getText());
          pst.setString(3, p_txt_qtypurchase.getText());
          pst.setString(4, p_txt_saleprice.getText());
          pst.setString(5, p_txt_discount.getText());
          pst.executeUpdate();
          rs = pst.getGeneratedKeys();
          rs.next();
          int key3 = rs.getInt(1);
          
            String insertpurchase = "INSERT into purchase (item_id,qty_purchase,date_purchase,supplier_name,total_bill,item_purchase_price,model_id,voucher_no,discount) VALUES (?,?,?,?,?,?,?,?,?)";
            pst = conn.prepareStatement(insertpurchase);
            pst.setInt(1, key2);
            pst.setString(2, p_txt_qtypurchase.getText());
            pst.setString(3, p_txt_datepurchase.getText());
            pst.setString(4, p_txt_sname.getText());
            pst.setString(5, p_txt_totalbill.getText());
            pst.setString(6, p_txt_pprice.getText());
            pst.setInt(7, key3);
            pst.setString(8, p_txt_voucher.getText());
            pst.setString(9, p_txt_discount.getText());           
            pst.execute();
            
            JOptionPane.showMessageDialog(null, "Data Saved");
             
                }
                else{
                    
          String selectitem = "SELECT * FROM item where item_name = '"+p_cb_item.getSelectedItem()+"' ";
          pst = conn.prepareStatement(selectitem);
          rs = pst.executeQuery();
          rs.next();
          
          String itemid = rs.getString("item_id");
          String pqty = rs.getString("item_qty");
          String nqty = p_txt_qtypurchase.getText();
          String tqty = Integer.toString(Integer.parseInt(pqty) + Integer.parseInt(nqty));
          
          String updateitem = "UPDATE item set item_qty = '"+tqty+"' where item_id = ? ";
          pst = conn.prepareStatement(updateitem);
          pst.setString(1, itemid);
          pst.executeUpdate();
          
          if(p_chb_model.isSelected()){
              
          String insertmodel = "INSERT into model (item_id,model_no,qty,sale_price,discount) VALUES (?,?,?,?,?)";
          pst = conn.prepareStatement(insertmodel, Statement.RETURN_GENERATED_KEYS);
          
          pst.setString(1, itemid);
          pst.setString(2, p_txt_nmodel.getText());
          pst.setString(3, p_txt_qtypurchase.getText());
          pst.setString(4, p_txt_saleprice.getText());
          pst.setString(5, p_txt_discount.getText());
          pst.executeUpdate();
          rs = pst.getGeneratedKeys();
          rs.next();
          int key3 = rs.getInt(1);
          
          String insertpurchase = "INSERT into purchase (item_id,qty_purchase,date_purchase,supplier_name,total_bill,item_purchase_price,model_id,voucher_no,discount) VALUES (?,?,?,?,?,?,?,?,?)";
            pst = conn.prepareStatement(insertpurchase);
            pst.setString(1, itemid);
            pst.setString(2, p_txt_qtypurchase.getText());
            pst.setString(3, p_txt_datepurchase.getText());
            pst.setString(4, p_txt_sname.getText());
            pst.setString(5, p_txt_totalbill.getText());
            pst.setString(6, p_txt_pprice.getText());
            pst.setInt(7, key3);
            pst.setString(8, p_txt_voucher.getText());
            pst.setString(9, p_txt_discount.getText());           
            pst.execute();
            
            JOptionPane.showMessageDialog(null, "Data Enter");
          }
          else{
          String selectmodel = "SELECT * FROM model where model_no = '"+p_cb_model.getSelectedItem()+"' ";
          pst = conn.prepareStatement(selectmodel);
          rs = pst.executeQuery();
          rs.next();
          
          String modelid = rs.getString("model_id");
          String mpqty = rs.getString("qty");
          String mnqty = p_txt_qtypurchase.getText();
          String mtqty = Integer.toString(Integer.parseInt(mpqty) + Integer.parseInt(mnqty));
          
          String updatemodel = "UPDATE model set qty = '"+mtqty+"' where model_id = ? ";
          pst = conn.prepareStatement(updatemodel);
          pst.setString(1, modelid);
          pst.executeUpdate();
          
          String insertpurchase = "INSERT into purchase (item_id,qty_purchase,date_purchase,supplier_name,total_bill,item_purchase_price,model_id,voucher_no,discount) VALUES (?,?,?,?,?,?,?,?,?)";
            pst = conn.prepareStatement(insertpurchase);
            pst.setString(1, itemid);
            pst.setString(2, p_txt_qtypurchase.getText());
            pst.setString(3, p_txt_datepurchase.getText());
            pst.setString(4, p_txt_sname.getText());
            pst.setString(5, p_txt_totalbill.getText());
            pst.setString(6, p_txt_pprice.getText());
            pst.setString(7, modelid);
            pst.setString(8, p_txt_voucher.getText());
            pst.setString(9, p_txt_discount.getText());           
            pst.execute();
            
            JOptionPane.showMessageDialog(null, "Data Enter");
          }
          
         
         
                }
         }
     }
     
         catch(Exception e){
                 JOptionPane.showMessageDialog(null, e);
                 }
 }
    private void updatetable(){
        
        try{
            String sql = "SELECT purchase.date_purchase as 'Date',purchase.voucher_no as 'Voucher',"
                    + " category.category_name as 'Category', item.item_name as 'Item', model.model_no as 'Model', "
                    + " purchase.supplier_name as 'Supplier name', purchase.item_purchase_price as 'Purchase Price',"
                    + " model.sale_price as 'Sale Price', purchase.qty_purchase as 'Qty', purchase.discount as 'Discount', "
                    + " purchase.total_bill as 'Total'"
                    + " FROM category "
                    + " INNER JOIN item ON category.category_id = item.category_id "
                    + " INNER JOIN model ON item.item_id = model.item_id "
                    + " INNER JOIN purchase ON model.model_id = purchase.model_id "
                    + " where purchase.date_purchase = ? ";

        
        pst = conn.prepareStatement(sql);
        pst.setString(1, p_date.getText());
        rs = pst.executeQuery();
        p_table.setModel(DbUtils.resultSetToTableModel(rs));
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, e);
        }
    }
    private void search(){
        
        try{
            String sql = "SELECT purchase.date_purchase as 'Date',purchase.voucher_no as 'Voucher',"
                    + " category.category_name as 'Category', item.item_name as 'Item', model.model_no as 'Model', "
                    + " purchase.supplier_name as 'Supplier name', purchase.item_purchase_price as 'Purchase Price',"
                    + " model.sale_price as 'Sale Price', purchase.qty_purchase as 'Qty', purchase.discount as 'Discount', "
                    + " purchase.total_bill as 'Total'"
                    + " FROM category "
                    + " INNER JOIN item ON category.category_id = item.category_id "
                    + " INNER JOIN model ON item.item_id = model.item_id "
                    + " INNER JOIN purchase ON model.model_id = purchase.model_id "
                    + " where purchase.voucher_no = ? ";

        
        pst = conn.prepareStatement(sql);
       
        pst.setString(1, p_txt_vouchersearch.getText());        
        rs = pst.executeQuery();
        
                p_table.setModel(DbUtils.resultSetToTableModel(rs));
        
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, e);
        }
    }    
    
    // set max voucher no on window oprn
    
    private void windowopen(){
            

           try{
       String sql = "select max(voucher_no) from purchase";
       pst = conn.prepareStatement(sql);
       rs=pst.executeQuery();
       
       /*Taking maximum voucher number from DB, converting it to integer type, 
       adding one, converting it back to string type and then pasting in
       voucher number text field*/
       if(rs.next()){
       String newv = rs.getString("max(voucher_no)");
       p_txt_voucher.setText(Integer.toString(Integer.parseInt(newv)+1));
       }
       
      
      }
       catch(SQLException ex){
       JOptionPane.showMessageDialog(null,ex);
      
       }
       catch(Exception ex){
       JOptionPane.showMessageDialog(null,ex);     
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        buttonGroup4 = new javax.swing.ButtonGroup();
        buttonGroup5 = new javax.swing.ButtonGroup();
        buttonGroup6 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        p_cb_item = new javax.swing.JComboBox();
        jLabel16 = new javax.swing.JLabel();
        p_cb_category = new javax.swing.JComboBox();
        p_chb_category = new javax.swing.JCheckBox();
        p_txt_nitemname = new javax.swing.JTextField();
        p_chb_item = new javax.swing.JCheckBox();
        p_txt_ncategory = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        p_cb_model = new javax.swing.JComboBox();
        jLabel22 = new javax.swing.JLabel();
        p_chb_model = new javax.swing.JCheckBox();
        jLabel11 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        p_txt_nmodel = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        p_txt_sname = new javax.swing.JTextField();
        p_txt_datepurchase = new datechooser.beans.DateChooserCombo();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        p_table = new javax.swing.JTable()
        {public boolean isCellEditable(int rowIndex, int colIndex) {
            return false;
        }};
        jPanel5 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        p_txt_voucher = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        p_date = new datechooser.beans.DateChooserCombo();
        jPanel3 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        p_txt_qtypurchase = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        p_txt_saleprice = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        p_txt_discount = new javax.swing.JTextField();
        p_txt_totalbill = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        p_txt_pprice = new javax.swing.JTextField();
        p_txt_qtystock = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        p_txt_nsellingprice = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        purchase_updatep = new javax.swing.JButton();
        p_cb_cmodel = new javax.swing.JComboBox();
        jLabel25 = new javax.swing.JLabel();
        p_cb_item1 = new javax.swing.JComboBox();
        jLabel26 = new javax.swing.JLabel();
        p_cb_category1 = new javax.swing.JComboBox();
        jLabel23 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        p_txt_vouchersearch = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        purchase_savebt = new javax.swing.JButton();
        purchase_newbt = new javax.swing.JButton();
        purchase_addbt = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        purchase_deletebt = new javax.swing.JButton();
        purchase_updatebt = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        p_txt_note = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Purchase Form");
        setBackground(new java.awt.Color(255, 255, 153));
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(102, 204, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Purchase", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 18), new java.awt.Color(204, 0, 102))); // NOI18N

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Enter Data", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14), new java.awt.Color(0, 153, 153))); // NOI18N
        jPanel2.setPreferredSize(new java.awt.Dimension(705, 145));

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel15.setText("Select Category");

        p_cb_item.setBackground(new java.awt.Color(159, 130, 130));
        p_cb_item.setForeground(new java.awt.Color(153, 255, 153));
        p_cb_item.setToolTipText("Select Item");
        p_cb_item.setPreferredSize(new java.awt.Dimension(6, 26));
        p_cb_item.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                p_cb_itemPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
                p_cb_itemPopupMenuWillBecomeVisible(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel16.setText("Select Item");

        p_cb_category.setBackground(new java.awt.Color(159, 130, 130));
        p_cb_category.setForeground(new java.awt.Color(153, 255, 153));
        p_cb_category.setToolTipText("Select Category");
        p_cb_category.setPreferredSize(new java.awt.Dimension(6, 26));
        p_cb_category.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                p_cb_categoryPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
                p_cb_categoryPopupMenuWillBecomeVisible(evt);
            }
        });

        p_chb_category.setText("Add New Category");
        p_chb_category.setToolTipText("Select to add new category");
        p_chb_category.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                p_chb_categoryActionPerformed(evt);
            }
        });

        p_txt_nitemname.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        p_txt_nitemname.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        p_txt_nitemname.setToolTipText("Add new item");
        p_txt_nitemname.setPreferredSize(new java.awt.Dimension(6, 30));
        p_txt_nitemname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                p_txt_nitemnameActionPerformed(evt);
            }
        });

        p_chb_item.setText("Add New Item");
        p_chb_item.setToolTipText("Select to add new item");
        p_chb_item.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                p_chb_itemActionPerformed(evt);
            }
        });

        p_txt_ncategory.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        p_txt_ncategory.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        p_txt_ncategory.setToolTipText("Add new category");
        p_txt_ncategory.setPreferredSize(new java.awt.Dimension(6, 30));
        p_txt_ncategory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                p_txt_ncategoryActionPerformed(evt);
            }
        });
        p_txt_ncategory.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                p_txt_ncategoryKeyPressed(evt);
            }
        });

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel19.setText(" New Category");

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel20.setText("Enter New Item");

        p_cb_model.setBackground(new java.awt.Color(159, 130, 130));
        p_cb_model.setForeground(new java.awt.Color(153, 255, 153));
        p_cb_model.setToolTipText("Select Item");
        p_cb_model.setPreferredSize(new java.awt.Dimension(6, 26));
        p_cb_model.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                p_cb_modelPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        jLabel22.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel22.setText("Select Model");

        p_chb_model.setText("Add New Model");
        p_chb_model.setToolTipText("Select to add new item");
        p_chb_model.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                p_chb_modelActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel11.setText("New Model No");

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setText("Date Purchase");

        p_txt_nmodel.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        p_txt_nmodel.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        p_txt_nmodel.setToolTipText("Enter supplier number");
        p_txt_nmodel.setPreferredSize(new java.awt.Dimension(6, 30));
        p_txt_nmodel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                p_txt_nmodelActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel9.setText("Supplier Name");

        p_txt_sname.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        p_txt_sname.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        p_txt_sname.setToolTipText("Enter Supplier Name");
        p_txt_sname.setPreferredSize(new java.awt.Dimension(6, 30));
        p_txt_sname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                p_txt_snameActionPerformed(evt);
            }
        });

        p_txt_datepurchase.setCurrentView(new datechooser.view.appearance.AppearancesList("Light",
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
    p_txt_datepurchase.setFormat(2);

    javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
    jPanel2.setLayout(jPanel2Layout);
    jPanel2Layout.setHorizontalGroup(
        jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
        .addGroup(jPanel2Layout.createSequentialGroup()
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(16, 16, 16)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel19)
                        .addComponent(jLabel22)
                        .addComponent(jLabel11)))
                .addComponent(jLabel15))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addComponent(p_chb_category)
                    .addGap(156, 156, 156))
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                            .addComponent(p_cb_category, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel16))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(p_cb_model, 0, 148, Short.MAX_VALUE)
                                    .addComponent(p_txt_ncategory, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addComponent(p_txt_nmodel, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel20, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                    .addComponent(jLabel9)
                                    .addGap(6, 6, 6))))
                        .addComponent(p_chb_model))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addComponent(p_txt_datepurchase, javax.swing.GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE)
                .addComponent(p_cb_item, 0, 148, Short.MAX_VALUE)
                .addComponent(p_chb_item)
                .addComponent(p_txt_nitemname, javax.swing.GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE)
                .addComponent(p_txt_sname, javax.swing.GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE))
            .addContainerGap())
    );
    jPanel2Layout.setVerticalGroup(
        jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel2Layout.createSequentialGroup()
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel16)
                        .addComponent(p_cb_item, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(p_chb_item)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(p_txt_nitemname, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel20)
                        .addComponent(p_txt_ncategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel19)))
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(p_cb_category, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel15))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(p_chb_category)))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(p_cb_model, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22)
                    .addComponent(jLabel7))
                .addComponent(p_txt_datepurchase, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(p_chb_model)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(p_txt_nmodel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addComponent(jLabel9)
                .addComponent(p_txt_sname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addContainerGap())
    );

    jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Record Details", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14), new java.awt.Color(0, 153, 153))); // NOI18N

    p_table.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {

        },
        new String [] {

        }
    ));
    p_table.setRowHeight(20);
    p_table.getTableHeader().setReorderingAllowed(false);
    p_table.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            p_tableMouseClicked(evt);
        }
    });
    jScrollPane1.setViewportView(p_table);

    javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
    jPanel4.setLayout(jPanel4Layout);
    jPanel4Layout.setHorizontalGroup(
        jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel4Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jScrollPane1)
            .addContainerGap())
    );
    jPanel4Layout.setVerticalGroup(
        jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(21, 21, 21))
    );

    jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

    jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    jLabel4.setText("Voucher No :");

    p_txt_voucher.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
    p_txt_voucher.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            p_txt_voucherActionPerformed(evt);
        }
    });

    jLabel3.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
    jLabel3.setText("Date :");

    p_date.setCurrentView(new datechooser.view.appearance.AppearancesList("Light",
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
p_date.setNothingAllowed(false);
p_date.setFormat(2);
p_date.setWeekStyle(datechooser.view.WeekDaysStyle.SHORT);
p_date.setLocked(true);
p_date.setCurrentNavigateIndex(0);
p_date.setBehavior(datechooser.model.multiple.MultyModelBehavior.SELECT_PERIOD);

javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
jPanel5.setLayout(jPanel5Layout);
jPanel5Layout.setHorizontalGroup(
    jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
    .addGroup(jPanel5Layout.createSequentialGroup()
        .addGap(50, 50, 50)
        .addComponent(jLabel3)
        .addGap(39, 39, 39)
        .addComponent(p_date, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGap(18, 18, 18)
        .addComponent(jLabel4)
        .addGap(18, 18, 18)
        .addComponent(p_txt_voucher, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap(49, Short.MAX_VALUE))
    );
    jPanel5Layout.setVerticalGroup(
        jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel5Layout.createSequentialGroup()
            .addGap(16, 16, 16)
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addComponent(jLabel3)
                .addComponent(p_date, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(p_txt_voucher, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Enter Prices", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14), new java.awt.Color(0, 153, 153))); // NOI18N
    jPanel3.setForeground(new java.awt.Color(51, 153, 255));

    jLabel18.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    jLabel18.setText("Quantity Purchase");

    p_txt_qtypurchase.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
    p_txt_qtypurchase.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    p_txt_qtypurchase.setToolTipText("Enter Qty purchase");
    p_txt_qtypurchase.setPreferredSize(new java.awt.Dimension(6, 30));
    p_txt_qtypurchase.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            p_txt_qtypurchaseActionPerformed(evt);
        }
    });

    jLabel14.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    jLabel14.setText("Discount");

    jLabel13.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    jLabel13.setText("Sale Price");

    p_txt_saleprice.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
    p_txt_saleprice.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    p_txt_saleprice.setToolTipText("Enter selling price");
    p_txt_saleprice.setPreferredSize(new java.awt.Dimension(6, 30));
    p_txt_saleprice.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            p_txt_salepriceActionPerformed(evt);
        }
    });

    jLabel10.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    jLabel10.setText("Purchase price");

    p_txt_discount.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
    p_txt_discount.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    p_txt_discount.setToolTipText("Enter discount");
    p_txt_discount.setPreferredSize(new java.awt.Dimension(6, 30));
    p_txt_discount.addFocusListener(new java.awt.event.FocusAdapter() {
        public void focusLost(java.awt.event.FocusEvent evt) {
            p_txt_discountFocusLost(evt);
        }
    });
    p_txt_discount.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            p_txt_discountActionPerformed(evt);
        }
    });

    p_txt_totalbill.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
    p_txt_totalbill.setHorizontalAlignment(javax.swing.JTextField.CENTER);

    jLabel8.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    jLabel8.setText("Total Bill");

    p_txt_pprice.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
    p_txt_pprice.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    p_txt_pprice.setToolTipText("Enter purchase price");
    p_txt_pprice.setPreferredSize(new java.awt.Dimension(6, 30));
    p_txt_pprice.addFocusListener(new java.awt.event.FocusAdapter() {
        public void focusLost(java.awt.event.FocusEvent evt) {
            p_txt_ppriceFocusLost(evt);
        }
    });
    p_txt_pprice.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            p_txt_ppriceActionPerformed(evt);
        }
    });

    p_txt_qtystock.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
    p_txt_qtystock.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    p_txt_qtystock.setToolTipText("Enter Qty purchase");
    p_txt_qtystock.setPreferredSize(new java.awt.Dimension(6, 30));

    jLabel24.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    jLabel24.setText("Quantity in Stock");

    javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
    jPanel3.setLayout(jPanel3Layout);
    jPanel3Layout.setHorizontalGroup(
        jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel3Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addComponent(jLabel13)
                .addComponent(jLabel14)
                .addComponent(jLabel18)
                .addComponent(jLabel10)
                .addComponent(jLabel8)
                .addComponent(jLabel24))
            .addGap(4, 4, 4)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(p_txt_totalbill, javax.swing.GroupLayout.Alignment.TRAILING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(p_txt_discount, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE)
                        .addComponent(p_txt_qtypurchase, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(p_txt_saleprice, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(p_txt_qtystock, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGap(0, 0, Short.MAX_VALUE))
                .addComponent(p_txt_pprice, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
    );
    jPanel3Layout.setVerticalGroup(
        jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel3Layout.createSequentialGroup()
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(p_txt_qtystock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel24))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(p_txt_qtypurchase, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel18))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(p_txt_pprice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel10))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(p_txt_discount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel14))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(p_txt_saleprice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel13))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(p_txt_totalbill, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel8))
            .addContainerGap())
    );

    jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Change Prices", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14), new java.awt.Color(0, 153, 153))); // NOI18N

    p_txt_nsellingprice.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
    p_txt_nsellingprice.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    p_txt_nsellingprice.setToolTipText("Enter supplier address");
    p_txt_nsellingprice.setPreferredSize(new java.awt.Dimension(6, 30));
    p_txt_nsellingprice.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            p_txt_nsellingpriceActionPerformed(evt);
        }
    });

    jLabel21.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    jLabel21.setText("Selling Price");

    purchase_updatep.setBackground(new java.awt.Color(153, 255, 153));
    purchase_updatep.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    purchase_updatep.setText("Update");
    purchase_updatep.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            purchase_updatepActionPerformed(evt);
        }
    });
    purchase_updatep.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyPressed(java.awt.event.KeyEvent evt) {
            purchase_updatepKeyPressed(evt);
        }
    });

    p_cb_cmodel.setBackground(new java.awt.Color(159, 130, 130));
    p_cb_cmodel.setForeground(new java.awt.Color(153, 255, 153));
    p_cb_cmodel.setToolTipText("Select Item");
    p_cb_cmodel.setPreferredSize(new java.awt.Dimension(6, 26));
    p_cb_cmodel.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
        public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
        }
        public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
            p_cb_cmodelPopupMenuWillBecomeInvisible(evt);
        }
        public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
        }
    });

    jLabel25.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    jLabel25.setText("Select Model");

    p_cb_item1.setBackground(new java.awt.Color(159, 130, 130));
    p_cb_item1.setForeground(new java.awt.Color(153, 255, 153));
    p_cb_item1.setToolTipText("Select Item");
    p_cb_item1.setPreferredSize(new java.awt.Dimension(6, 26));
    p_cb_item1.addFocusListener(new java.awt.event.FocusAdapter() {
        public void focusGained(java.awt.event.FocusEvent evt) {
            p_cb_item1FocusGained(evt);
        }
    });
    p_cb_item1.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
        public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
        }
        public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
            p_cb_item1PopupMenuWillBecomeInvisible(evt);
        }
        public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
        }
    });

    jLabel26.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    jLabel26.setText("Select Item");

    p_cb_category1.setBackground(new java.awt.Color(159, 130, 130));
    p_cb_category1.setToolTipText("Select Category");
    p_cb_category1.setPreferredSize(new java.awt.Dimension(6, 26));
    p_cb_category1.addFocusListener(new java.awt.event.FocusAdapter() {
        public void focusGained(java.awt.event.FocusEvent evt) {
            p_cb_category1FocusGained(evt);
        }
    });
    p_cb_category1.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
        public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
        }
        public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
            p_cb_category1PopupMenuWillBecomeInvisible(evt);
        }
        public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
        }
    });

    jLabel23.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    jLabel23.setText("Category");

    javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
    jPanel6.setLayout(jPanel6Layout);
    jPanel6Layout.setHorizontalGroup(
        jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addGroup(jPanel6Layout.createSequentialGroup()
                    .addComponent(jLabel25)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(p_cb_cmodel, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(purchase_updatep)
                .addGroup(jPanel6Layout.createSequentialGroup()
                    .addComponent(jLabel21)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(p_txt_nsellingprice, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel6Layout.createSequentialGroup()
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel26)
                        .addComponent(jLabel23))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(p_cb_category1, javax.swing.GroupLayout.Alignment.TRAILING, 0, 112, Short.MAX_VALUE)
                        .addComponent(p_cb_item1, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGap(52, 52, 52))
    );
    jPanel6Layout.setVerticalGroup(
        jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel6Layout.createSequentialGroup()
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(p_cb_category1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel23))
            .addGap(18, 18, 18)
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(p_cb_item1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel26))
            .addGap(18, 18, 18)
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(p_cb_cmodel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel25))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(p_txt_nsellingprice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel21))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(purchase_updatep, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
    );

    jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Search for record", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14), new java.awt.Color(0, 153, 153))); // NOI18N

    jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    jLabel5.setText("Voucher No :");

    p_txt_vouchersearch.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
    p_txt_vouchersearch.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    p_txt_vouchersearch.setToolTipText("Enter Qty purchase");
    p_txt_vouchersearch.setPreferredSize(new java.awt.Dimension(6, 30));
    p_txt_vouchersearch.addFocusListener(new java.awt.event.FocusAdapter() {
        public void focusGained(java.awt.event.FocusEvent evt) {
            p_txt_vouchersearchFocusGained(evt);
        }
        public void focusLost(java.awt.event.FocusEvent evt) {
            p_txt_vouchersearchFocusLost(evt);
        }
    });
    p_txt_vouchersearch.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyPressed(java.awt.event.KeyEvent evt) {
            p_txt_vouchersearchKeyPressed(evt);
        }
    });

    javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
    jPanel7.setLayout(jPanel7Layout);
    jPanel7Layout.setHorizontalGroup(
        jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel7Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jLabel5)
            .addGap(41, 41, 41)
            .addComponent(p_txt_vouchersearch, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(109, Short.MAX_VALUE))
    );
    jPanel7Layout.setVerticalGroup(
        jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel7Layout.createSequentialGroup()
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(p_txt_vouchersearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel5))
            .addContainerGap(12, Short.MAX_VALUE))
    );

    jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Buttons", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14), new java.awt.Color(0, 153, 153))); // NOI18N
    jPanel8.setPreferredSize(new java.awt.Dimension(527, 62));

    jButton3.setBackground(new java.awt.Color(255, 102, 102));
    jButton3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    jButton3.setText("Exit");
    jButton3.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton3ActionPerformed(evt);
        }
    });

    purchase_savebt.setBackground(new java.awt.Color(51, 255, 51));
    purchase_savebt.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    purchase_savebt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/button_ok.png"))); // NOI18N
    purchase_savebt.setText("Save");
    purchase_savebt.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            purchase_savebtActionPerformed(evt);
        }
    });
    purchase_savebt.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyPressed(java.awt.event.KeyEvent evt) {
            purchase_savebtKeyPressed(evt);
        }
    });

    purchase_newbt.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    purchase_newbt.setText("New");
    purchase_newbt.setPreferredSize(new java.awt.Dimension(94, 23));
    purchase_newbt.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            purchase_newbtActionPerformed(evt);
        }
    });

    purchase_addbt.setBackground(new java.awt.Color(255, 255, 153));
    purchase_addbt.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    purchase_addbt.setText("Add another");
    purchase_addbt.setPreferredSize(new java.awt.Dimension(94, 23));
    purchase_addbt.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            purchase_addbtActionPerformed(evt);
        }
    });

    javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
    jPanel8.setLayout(jPanel8Layout);
    jPanel8Layout.setHorizontalGroup(
        jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
            .addGap(22, 22, 22)
            .addComponent(purchase_addbt, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(18, 18, 18)
            .addComponent(purchase_newbt, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(18, 18, 18)
            .addComponent(purchase_savebt)
            .addGap(18, 18, 18)
            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(24, Short.MAX_VALUE))
    );
    jPanel8Layout.setVerticalGroup(
        jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel8Layout.createSequentialGroup()
            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(purchase_savebt, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(purchase_newbt, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(purchase_addbt, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(0, 15, Short.MAX_VALUE))
    );

    jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Buttons", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14), new java.awt.Color(0, 153, 153))); // NOI18N

    purchase_deletebt.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    purchase_deletebt.setText("Delete");
    purchase_deletebt.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            purchase_deletebtActionPerformed(evt);
        }
    });

    purchase_updatebt.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    purchase_updatebt.setText("Edit");
    purchase_updatebt.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            purchase_updatebtActionPerformed(evt);
        }
    });

    javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
    jPanel9.setLayout(jPanel9Layout);
    jPanel9Layout.setHorizontalGroup(
        jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(purchase_deletebt, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(purchase_updatebt, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap())
    );
    jPanel9Layout.setVerticalGroup(
        jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel9Layout.createSequentialGroup()
            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(purchase_updatebt, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(purchase_deletebt, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(0, 0, Short.MAX_VALUE))
    );

    jLabel17.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    jLabel17.setText("Note For Deletion");

    p_txt_note.setColumns(20);
    p_txt_note.setRows(5);
    jScrollPane2.setViewportView(p_txt_note);

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel1Layout.createSequentialGroup()
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 582, Short.MAX_VALUE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGap(0, 0, Short.MAX_VALUE))
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addComponent(jLabel17)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, 484, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap())))
    );
    jPanel1Layout.setVerticalGroup(
        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel1Layout.createSequentialGroup()
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGap(11, 11, 11)
            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, 77, Short.MAX_VALUE)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(33, 33, 33)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGap(733, 733, 733))
    );

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addGap(22, 22, 22)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addContainerGap())
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 696, Short.MAX_VALUE))
    );

    pack();
    setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents


    private void purchase_savebtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_purchase_savebtActionPerformed
categoryinsert();
windowopen();
textfield();
updatetable();
p_cb_category.removeAllItems();
categorycombobox();
p_cb_item.removeAllItems();
p_cb_model.removeAllItems();


    }//GEN-LAST:event_purchase_savebtActionPerformed

    private void purchase_savebtKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_purchase_savebtKeyPressed
          int key = evt.getKeyCode();
        if(key == VK_ENTER){
            categoryinsert();
            windowopen();
            textfield();
            updatetable();
            p_cb_category.setSelectedIndex(0);
            p_cb_item.removeAllItems();
            p_cb_model.removeAllItems();
        }
       
    }//GEN-LAST:event_purchase_savebtKeyPressed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        int p = JOptionPane.showConfirmDialog(null, "Confirm For Exit","Exit",JOptionPane.YES_NO_CANCEL_OPTION);
        if(p == 0){
            this.dispose();
        }  
    }//GEN-LAST:event_jButton3ActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        categorycombobox();
        windowopen();
        updatetable();
        textfield();
        modelboxwinopen();
    }//GEN-LAST:event_formWindowOpened

    private void p_chb_modelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_p_chb_modelActionPerformed
        checkbox3();
        textfield1();
       
    }//GEN-LAST:event_p_chb_modelActionPerformed

    private void p_txt_ncategoryKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_p_txt_ncategoryKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_p_txt_ncategoryKeyPressed

    private void p_chb_itemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_p_chb_itemActionPerformed
        checkbox2();
        textfield1();
        
    }//GEN-LAST:event_p_chb_itemActionPerformed

    private void p_chb_categoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_p_chb_categoryActionPerformed
        checkbox1();
        textfield1();

        
    }//GEN-LAST:event_p_chb_categoryActionPerformed

    private void p_txt_ppriceFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_p_txt_ppriceFocusLost
        calculation();
    }//GEN-LAST:event_p_txt_ppriceFocusLost

    private void p_txt_discountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_p_txt_discountFocusLost
        discount();
        calculation();
    }//GEN-LAST:event_p_txt_discountFocusLost

    private void purchase_addbtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_purchase_addbtActionPerformed
        categoryinsert();
        textfield();
    }//GEN-LAST:event_purchase_addbtActionPerformed

    private void purchase_updatepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_purchase_updatepActionPerformed
        int p = JOptionPane.showConfirmDialog(null, "Confirm this action","Selling Price Update",JOptionPane.YES_NO_CANCEL_OPTION);
        if(p == 0){
        purchase_up();
        p_txt_nsellingprice.setText("");
        p_cb_item1.removeAllItems();
        p_cb_cmodel.removeAllItems();
        
        }
    }//GEN-LAST:event_purchase_updatepActionPerformed

    private void purchase_newbtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_purchase_newbtActionPerformed
        int p = JOptionPane.showConfirmDialog(null, "Confirm this action","New Form",JOptionPane.YES_NO_OPTION);
        if(p == 0){
        textfield();
        windowopen();
        }
    }//GEN-LAST:event_purchase_newbtActionPerformed

    private void p_txt_ncategoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_p_txt_ncategoryActionPerformed
        p_txt_nitemname.requestFocusInWindow();
    }//GEN-LAST:event_p_txt_ncategoryActionPerformed

    private void p_txt_qtypurchaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_p_txt_qtypurchaseActionPerformed
        p_txt_pprice.requestFocusInWindow();
    }//GEN-LAST:event_p_txt_qtypurchaseActionPerformed

    private void p_txt_voucherActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_p_txt_voucherActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_p_txt_voucherActionPerformed

    private void p_txt_nitemnameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_p_txt_nitemnameActionPerformed
        p_txt_nmodel.requestFocusInWindow();
    }//GEN-LAST:event_p_txt_nitemnameActionPerformed

    private void p_txt_nmodelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_p_txt_nmodelActionPerformed
        p_txt_sname.requestFocusInWindow();
    }//GEN-LAST:event_p_txt_nmodelActionPerformed

    private void p_txt_snameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_p_txt_snameActionPerformed
        p_txt_qtypurchase.requestFocusInWindow();
    }//GEN-LAST:event_p_txt_snameActionPerformed

    private void p_txt_ppriceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_p_txt_ppriceActionPerformed
        p_txt_discount.requestFocusInWindow();
    }//GEN-LAST:event_p_txt_ppriceActionPerformed

    private void p_txt_discountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_p_txt_discountActionPerformed
        p_txt_saleprice.requestFocusInWindow();
    }//GEN-LAST:event_p_txt_discountActionPerformed

    private void p_txt_salepriceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_p_txt_salepriceActionPerformed
        purchase_savebt.requestFocusInWindow();
    }//GEN-LAST:event_p_txt_salepriceActionPerformed

    private void p_txt_nsellingpriceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_p_txt_nsellingpriceActionPerformed
        purchase_updatep.requestFocusInWindow();
    }//GEN-LAST:event_p_txt_nsellingpriceActionPerformed

    private void p_txt_vouchersearchFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_p_txt_vouchersearchFocusGained
        p_txt_vouchersearch.setText("");
    }//GEN-LAST:event_p_txt_vouchersearchFocusGained

    private void p_tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_p_tableMouseClicked
   if (evt.getClickCount() == 2) {
    int p = JOptionPane.showConfirmDialog(null, "Confirm for update","Update",JOptionPane.YES_NO_OPTION);
        if(p == 0){
   
        int row = p_table.getSelectedRow();
        String date         = (p_table.getModel().getValueAt(row, 0).toString());
        String voucher      = (p_table.getModel().getValueAt(row, 1).toString());
        String category     = (p_table.getModel().getValueAt(row, 2).toString());
        String item         = (p_table.getModel().getValueAt(row, 3).toString());
        String model        = (p_table.getModel().getValueAt(row, 4).toString());
        String supplier_name = (p_table.getModel().getValueAt(row, 5).toString());
        String purchase_price = (p_table.getModel().getValueAt(row, 6).toString());
        String sale_price   = (p_table.getModel().getValueAt(row, 7).toString());
        String qty          = (p_table.getModel().getValueAt(row, 8).toString());
        String discount     = (p_table.getModel().getValueAt(row, 9).toString());
        String total        = (p_table.getModel().getValueAt(row, 10).toString());
        
        p_txt_datepurchase.setText(date);
        p_txt_voucher.setText(voucher);
        p_txt_ncategory.setText(category);
        p_txt_nitemname.setText(item);
        p_txt_nmodel.setText(model);
        p_txt_sname.setText(supplier_name);
        p_txt_pprice.setText(purchase_price);
        p_txt_saleprice.setText(sale_price);
        p_txt_qtypurchase.setText(qty);
        p_txt_discount.setText(discount);
        p_txt_totalbill.setText(total);
        
        purchase_savebt.setEnabled(false);
        purchase_updatebt.setEnabled(true);
        purchase_deletebt.setEnabled(true);
        p_txt_ncategory.setEditable(true);
        p_txt_nitemname.setEditable(true);
        p_txt_nmodel.setEditable(true);
        p_chb_category.setEnabled(false);
        p_chb_item.setEnabled(false);
        p_chb_model.setEnabled(false);
        p_cb_category.setEnabled(false);
        p_cb_item.setEnabled(false);
        p_cb_model.setEnabled(false);
        purchase_updatep.setEnabled(false);
        purchase_addbt.setEnabled(false);
        p_txt_note.setEditable(true);
        }
   }
    }//GEN-LAST:event_p_tableMouseClicked

    private void purchase_updatebtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_purchase_updatebtActionPerformed
       int p = JOptionPane.showConfirmDialog(null, "Do you really want to Update the record","Update",JOptionPane.YES_NO_CANCEL_OPTION);
        if(p == 0){
        updatedata();
        }
    }//GEN-LAST:event_purchase_updatebtActionPerformed

    private void purchase_deletebtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_purchase_deletebtActionPerformed
        int p = JOptionPane.showConfirmDialog(null, "Do you really want to delete the data","Delete",JOptionPane.YES_NO_CANCEL_OPTION);
        if(p == 0){
        deletedata();
        }
    }//GEN-LAST:event_purchase_deletebtActionPerformed

    private void p_cb_categoryPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_p_cb_categoryPopupMenuWillBecomeInvisible
       itemcombox();
    }//GEN-LAST:event_p_cb_categoryPopupMenuWillBecomeInvisible

    private void p_cb_itemPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_p_cb_itemPopupMenuWillBecomeInvisible
        modelbox();
    }//GEN-LAST:event_p_cb_itemPopupMenuWillBecomeInvisible

    private void p_cb_modelPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_p_cb_modelPopupMenuWillBecomeInvisible
        setvaluesoncb();
    }//GEN-LAST:event_p_cb_modelPopupMenuWillBecomeInvisible

    private void p_cb_item1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_p_cb_item1FocusGained
    p_cb_cmodel.removeAllItems();
    }//GEN-LAST:event_p_cb_item1FocusGained

    private void p_cb_item1PopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_p_cb_item1PopupMenuWillBecomeInvisible

      
        String sql1 = "select item_id from item where item_name = '"+p_cb_item1.getSelectedItem()+"' ";
    try{
        pst = conn.prepareStatement(sql1);        
        rs = pst.executeQuery();
        rs.next();
        String sql2 = "select model_no from model where item_id = '"+rs.getString("item_id")+"' ";
        pst = conn.prepareStatement(sql2);        
        rs = pst.executeQuery();
        while(rs.next()){
            String item = rs.getString("model_no");
            p_cb_cmodel.addItem(item);            
        }        
    }
    catch(Exception e){
        JOptionPane.showMessageDialog(null, "Please First Select Category");        
    }
        
    }//GEN-LAST:event_p_cb_item1PopupMenuWillBecomeInvisible

    private void p_cb_category1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_p_cb_category1FocusGained
        p_cb_item1.removeAllItems();
    }//GEN-LAST:event_p_cb_category1FocusGained

    private void p_cb_category1PopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_p_cb_category1PopupMenuWillBecomeInvisible
        try{

        String sql1 = "select category_id from category where category_name = '"+p_cb_category1.getSelectedItem()+"' ";
        pst = conn.prepareStatement(sql1);        
        rs = pst.executeQuery();
        rs.next();
       int ab = Integer.parseInt(rs.getString("category_id"));
  
        String sql2 = "select item_name from item where category_id = '"+ab+"' ";
        pst = conn.prepareStatement(sql2);        
        rs = pst.executeQuery();
        while(rs.next()){
            String item = rs.getString("item_name");
            p_cb_item1.addItem(item);    
    }
    }
    
    catch(SQLException e){
        JOptionPane.showMessageDialog(null, e + " Item Name");        
    }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, e + " Normal Exception");
        }
  
    }//GEN-LAST:event_p_cb_category1PopupMenuWillBecomeInvisible

    private void p_cb_cmodelPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_p_cb_cmodelPopupMenuWillBecomeInvisible
                modelboxaction();
        p_txt_nsellingprice.requestFocusInWindow();
    }//GEN-LAST:event_p_cb_cmodelPopupMenuWillBecomeInvisible

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
   int p = JOptionPane.showConfirmDialog(null, "Confirm For Exit","Exit",JOptionPane.YES_NO_CANCEL_OPTION);
        if(p == 0){
            this.dispose();
        }       // TODO add your handling code here:
    }//GEN-LAST:event_formWindowClosing

    private void purchase_updatepKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_purchase_updatepKeyPressed
        int key = evt.getKeyCode();
        if(key == VK_ENTER){
        int p = JOptionPane.showConfirmDialog(null, "Confirm this action","Selling Price Update",JOptionPane.YES_NO_CANCEL_OPTION);
        if(p == 0){
        purchase_up();
        p_txt_nsellingprice.setText("");
        p_cb_item1.removeAllItems();
        p_cb_cmodel.removeAllItems();
        }
        }
    }//GEN-LAST:event_purchase_updatepKeyPressed

    private void p_txt_vouchersearchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_p_txt_vouchersearchKeyPressed
        int key = evt.getKeyCode();
        if(key == VK_ENTER){
            search();
        }
    }//GEN-LAST:event_p_txt_vouchersearchKeyPressed

    private void p_txt_vouchersearchFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_p_txt_vouchersearchFocusLost
        p_txt_vouchersearch.setText("");
    }//GEN-LAST:event_p_txt_vouchersearchFocusLost

    private void p_cb_categoryPopupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_p_cb_categoryPopupMenuWillBecomeVisible
        p_cb_item.removeAllItems();
    }//GEN-LAST:event_p_cb_categoryPopupMenuWillBecomeVisible

    private void p_cb_itemPopupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_p_cb_itemPopupMenuWillBecomeVisible
        p_cb_model.removeAllItems();
    }//GEN-LAST:event_p_cb_itemPopupMenuWillBecomeVisible

    /**
     * @param args the command line arguments
     */


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.ButtonGroup buttonGroup4;
    private javax.swing.ButtonGroup buttonGroup5;
    private javax.swing.ButtonGroup buttonGroup6;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
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
    private javax.swing.JComboBox p_cb_category;
    private javax.swing.JComboBox p_cb_category1;
    private javax.swing.JComboBox p_cb_cmodel;
    private javax.swing.JComboBox p_cb_item;
    private javax.swing.JComboBox p_cb_item1;
    private javax.swing.JComboBox p_cb_model;
    private javax.swing.JCheckBox p_chb_category;
    private javax.swing.JCheckBox p_chb_item;
    private javax.swing.JCheckBox p_chb_model;
    private datechooser.beans.DateChooserCombo p_date;
    private javax.swing.JTable p_table;
    private datechooser.beans.DateChooserCombo p_txt_datepurchase;
    private javax.swing.JTextField p_txt_discount;
    private javax.swing.JTextField p_txt_ncategory;
    private javax.swing.JTextField p_txt_nitemname;
    private javax.swing.JTextField p_txt_nmodel;
    private javax.swing.JTextArea p_txt_note;
    private javax.swing.JTextField p_txt_nsellingprice;
    private javax.swing.JTextField p_txt_pprice;
    private javax.swing.JTextField p_txt_qtypurchase;
    private javax.swing.JTextField p_txt_qtystock;
    private javax.swing.JTextField p_txt_saleprice;
    private javax.swing.JTextField p_txt_sname;
    private javax.swing.JTextField p_txt_totalbill;
    private javax.swing.JTextField p_txt_voucher;
    private javax.swing.JTextField p_txt_vouchersearch;
    private javax.swing.JButton purchase_addbt;
    private javax.swing.JButton purchase_deletebt;
    private javax.swing.JButton purchase_newbt;
    private javax.swing.JButton purchase_savebt;
    private javax.swing.JButton purchase_updatebt;
    private javax.swing.JButton purchase_updatep;
    // End of variables declaration//GEN-END:variables
}
