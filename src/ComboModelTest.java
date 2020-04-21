    import java.awt.*;  
    import java.text.*;  
    import java.util.*;  
    import javax.swing.*;  
    import javax.swing.event.*;  
       
    public class ComboModelTest  
    {  
        public static void main(String[] args)  
        {  
            final ComboModel comboModel = new ComboModel(0, 2004);  
            JComboBox comboBox = new JComboBox(comboModel);  
            comboBox.setPreferredSize(new Dimension(120, comboBox.getPreferredSize().height));  
            final SpinnerDateModel model = new SpinnerDateModel(new Date(), null, null,  
                                                                Calendar.MONTH);  
            JSpinner spinner = new JSpinner(model);  
            spinner.setPreferredSize(new Dimension(75, spinner.getPreferredSize().height));  
            JSpinner.DateEditor editor = new JSpinner.DateEditor(spinner, "MMM yyyy");  
            editor.getTextField().setHorizontalAlignment(JTextField.CENTER);  
            spinner.setEditor(editor);  
            spinner.addChangeListener(new ChangeListener()  
            {  
                DateFormat df = new SimpleDateFormat("MM,yyyy");  
       
                public void stateChanged(ChangeEvent e)  
                {  
                    String[] s = df.format(model.getDate()).split(",");  
                    int m = Integer.parseInt(s[0]) - 1;  
                    int y = Integer.parseInt(s[1]);  
                    comboModel.reset(m, y);  
                }  
            });  
            JPanel north = new JPanel();  
            north.add(comboBox);  
            north.add(spinner);  
            JFrame f = new JFrame();  
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
            f.getContentPane().add(north, "North");  
            f.setSize(300,175);  
            f.setLocation(200,200);  
            f.setVisible(true);  
        }  
    }  
       
    class ComboModel extends DefaultComboBoxModel  
    {  
        DateFormat df;  
        Calendar calendar;  
        int size;  
       
        public ComboModel(int month, int year)  
        {  
            df = new SimpleDateFormat("dd MMM yyyy");  
            calendar = Calendar.getInstance();  
            init(month, year);  
        }  
       
        private void init(int month, int year)  
        {  
            calendar.set(year, month, 01);  
            String s;  
            size = 0;  
            while(calendar.get(Calendar.MONTH) == month)  
            {  
                s = df.format(calendar.getTime());  
                calendar.add(Calendar.DAY_OF_MONTH, 1);  
                //System.out.println(s);  
                addElement(s);  
                size++;  
            }  
        }  
       
        public int getSize()  
        {  
            return size;  
        }  
       
        public void reset(int month, int year)  
        {  
            removeAllElements();  
            init(month, year);  
            fireContentsChanged(this, 0, getSize() - 1);  
        }  
    }  