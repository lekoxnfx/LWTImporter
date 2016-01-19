package com.esa.lwimporter.ui;

import com.esa.config.N4Config;
import com.esa.lwimporter.worker.DoJobRunnable;
import com.esa.lwimporter.worker.LoginWorker;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainFrame extends JFrame {

    private JPanel contentPane;
    private Font fnt = new Font("微软雅黑", Font.PLAIN, 12);
    private JPanel northPanel;
    private JLabel lblStep3;
    private JPanel centerPanel;
    private JPanel panel1;
    private JTextField tF_name;
    private JPasswordField passwordField;
    private JPanel panel_2;
    private JLabel label_4;
    private JTextField tF_FilePath;
    private JComboBox comboBox;
    private JButton button_2;
    private JPanel panel_3;
    private JPanel panel;
    private JLabel lblNewLabel_2;
    private JPanel panel_1;
    private JButton btnNewButton;
    private JButton button_3;
    private JLabel lblStep1;
    private JLabel lblStep2;
    private JPanel southPanel;
    private JTextPane tRResult;
    private JScrollPane scrollPane_1;
    private JButton button_4;
    private JTextArea textArea_1;
    private JTextField tF_N4IP;
    private JTextField tF_N4Port;
    private JTextField tF_N4DBIP;
    private JTextField tF_N4DBPort;
    private JTextField tF_N4DBSID;
    private JTextField tF_N4DBUsername;
    private JPasswordField pF_N4DBPassword;
    private JPanel panel_a;
    private JTextField tF_OperatorId;
    private JTextField tF_ComplexId;
    private JTextField tF_FacilityId;
    private JTextField tF_YardId;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MainFrame frame = new MainFrame();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public MainFrame() {
        setTitle("龙湾导入器");
        //golbal font settings
        initGlobalFontSetting(fnt);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 600);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));

        northPanel = new JPanel();
        northPanel.setBorder(new LineBorder(Color.LIGHT_GRAY, 5, true));
        contentPane.add(northPanel, BorderLayout.NORTH);

        lblStep1 = new JLabel(" 1 ");
        lblStep1.setForeground(Color.CYAN);
        lblStep1.setBackground(Color.BLUE);
        lblStep1.setOpaque(true);
        lblStep1.setFont(new Font("宋体", Font.BOLD, 40));
        northPanel.add(lblStep1);

        JLabel label = new JLabel("   ");
        label.setEnabled(false);
        label.setOpaque(true);
        label.setFont(new Font("方正舒体", Font.BOLD, 40));
        northPanel.add(label);

        lblStep2 = new JLabel(" 2 ");
        lblStep2.setOpaque(true);
        lblStep2.setFont(new Font("宋体", Font.BOLD, 40));
        northPanel.add(lblStep2);

        JLabel label_1 = new JLabel("   ");
        label_1.setEnabled(false);
        label_1.setOpaque(true);
        label_1.setFont(new Font("方正舒体", Font.BOLD, 40));
        northPanel.add(label_1);

        lblStep3 = new JLabel(" 3 ");
        lblStep3.setOpaque(true);
        lblStep3.setFont(new Font("宋体", Font.BOLD, 40));
        northPanel.add(lblStep3);

        centerPanel = new JPanel();
        centerPanel.setBorder(new LineBorder(new Color(192, 192, 192), 1, true));
        contentPane.add(centerPanel, BorderLayout.CENTER);
        centerPanel.setLayout(new CardLayout(10, 10));

        panel1 = new JPanel();
        centerPanel.add(panel1, "panel1");
        panel1.setLayout(null);

        JLabel lblNewLabel = new JLabel("请输入您的N4账号，并点击下一步");
        lblNewLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        lblNewLabel.setBounds(10, 40, 732, 49);
        panel1.add(lblNewLabel);

        JLabel lblNewLabel_1 = new JLabel("用户名");
        lblNewLabel_1.setHorizontalAlignment(SwingConstants.LEFT);
        lblNewLabel_1.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        lblNewLabel_1.setBounds(198, 154, 173, 21);
        panel1.add(lblNewLabel_1);

        tF_name = new JTextField();
        tF_name.setBounds(381, 155, 160, 21);
        panel1.add(tF_name);
        tF_name.setColumns(10);

        JLabel label_3 = new JLabel("密码");
        label_3.setHorizontalAlignment(SwingConstants.LEFT);
        label_3.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        label_3.setBounds(198, 212, 173, 21);
        panel1.add(label_3);

        passwordField = new JPasswordField();
        passwordField.setBounds(381, 213, 160, 21);
        panel1.add(passwordField);

        JButton button = new JButton("下一步");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                if (tF_name.getText() != null && passwordField.getPassword() != null) {
                    String username = tF_name.getText();
                    String password = new String(passwordField.getPassword());
                    System.out.println(username + "\t" + password);
                    LoginWorker lw = new LoginWorker();

                    N4Config.N4_IP = tF_N4IP.getText();
                    N4Config.N4_PORT = tF_N4Port.getText();
                    N4Config.N4_DB_IP = tF_N4DBIP.getText();
                    N4Config.N4_DB_PORT = tF_N4DBPort.getText();
                    N4Config.N4_DB_SID = tF_N4DBSID.getText();
                    N4Config.N4_DB_USERNAME = tF_N4DBUsername.getText();
                    N4Config.N4_DB_PASSWORD = new String(pF_N4DBPassword.getPassword());

                    N4Config.N4_OPERATOR_ID = tF_OperatorId.getText();
                    N4Config.N4_COMPLEX_ID = tF_ComplexId.getText();
                    N4Config.N4_FACILITY_ID = tF_FacilityId.getText();
                    N4Config.N4_YARD_ID = tF_YardId.getText();

                    N4Config.N4_USERNAME = username;
                    N4Config.N4_PASSOWRD = password;
                    boolean login_suc = false;
                    try {
                        lw.execute();
                        if ((boolean) lw.get()) {
                            login_suc = true;
                        }
                    } catch (InterruptedException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    } catch (ExecutionException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }

                    if (login_suc) {
                        N4Config.N4_USERNAME = username;
                        N4Config.N4_PASSOWRD = password;

                        CardLayout cl = (CardLayout) centerPanel.getLayout();
                        cl.next(centerPanel);

                        lblStep2.setBackground(lblStep1.getBackground());
                        lblStep2.setForeground(lblStep1.getForeground());
                        lblStep1.setBackground(lblStep3.getBackground());
                        lblStep1.setForeground(lblStep3.getForeground());

                        tRResult.setText("登陆成功！");

                    } else {
                        tRResult.setText("登陆失败！");
                    }
                }


            }
        });
        button.setBounds(448, 282, 93, 23);
        panel1.add(button);

        JButton btn_AS = new JButton("高级设置");
        btn_AS.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                panel_a.setVisible(!panel_a.isVisible());
            }
        });
        btn_AS.setBounds(198, 282, 93, 23);
        panel1.add(btn_AS);

        panel_a = new JPanel();
        panel_a.setBounds(10, 315, 732, 120);
        panel1.add(panel_a);
        panel_a.setLayout(null);
        panel_a.setVisible(false);

        tF_N4IP = new JTextField();
        tF_N4IP.setText(N4Config.N4_IP.toString());
        tF_N4IP.setBounds(10, 45, 120, 21);
        panel_a.add(tF_N4IP);
        tF_N4IP.setColumns(10);

        JLabel lblNewLabel_3 = new JLabel("N4IP");
        lblNewLabel_3.setBounds(10, 20, 120, 15);
        panel_a.add(lblNewLabel_3);

        tF_N4Port = new JTextField();
        tF_N4Port.setText(N4Config.N4_PORT.toString());
        tF_N4Port.setBounds(140, 45, 66, 21);
        panel_a.add(tF_N4Port);
        tF_N4Port.setColumns(10);

        JLabel lblNport = new JLabel("N4PORT");
        lblNport.setBounds(140, 20, 66, 15);
        panel_a.add(lblNport);

        tF_N4DBIP = new JTextField();
        tF_N4DBIP.setText(N4Config.N4_DB_IP.toString());
        tF_N4DBIP.setColumns(10);
        tF_N4DBIP.setBounds(247, 45, 120, 21);
        panel_a.add(tF_N4DBIP);

        tF_N4DBPort = new JTextField();
        tF_N4DBPort.setText(N4Config.N4_DB_PORT.toString());
        tF_N4DBPort.setColumns(10);
        tF_N4DBPort.setBounds(377, 45, 66, 21);
        panel_a.add(tF_N4DBPort);

        tF_N4DBSID = new JTextField();
        tF_N4DBSID.setText(N4Config.N4_DB_SID.toString());
        tF_N4DBSID.setColumns(10);
        tF_N4DBSID.setBounds(453, 45, 66, 21);
        panel_a.add(tF_N4DBSID);

        tF_N4DBUsername = new JTextField();
        tF_N4DBUsername.setText(N4Config.N4_DB_USERNAME.toString());
        tF_N4DBUsername.setColumns(10);
        tF_N4DBUsername.setBounds(540, 45, 81, 21);
        panel_a.add(tF_N4DBUsername);

        pF_N4DBPassword = new JPasswordField();
        pF_N4DBPassword.setBounds(641, 45, 81, 21);
        panel_a.add(pF_N4DBPassword);
        pF_N4DBPassword.setText(N4Config.N4_DB_PASSWORD.toString());

        JLabel lblNdbip = new JLabel("N4DBIP");
        lblNdbip.setBounds(247, 20, 120, 15);
        panel_a.add(lblNdbip);

        JLabel lblNdbport = new JLabel("N4DBPORT");
        lblNdbport.setBounds(377, 20, 66, 15);
        panel_a.add(lblNdbport);

        JLabel lblNdbsid = new JLabel("N4DBSID");
        lblNdbsid.setBounds(453, 20, 66, 15);
        panel_a.add(lblNdbsid);

        JLabel lblNdb = new JLabel("N4DB用户名");
        lblNdb.setBounds(540, 20, 81, 15);
        panel_a.add(lblNdb);

        JLabel lblNdb_1 = new JLabel("N4DB密码");
        lblNdb_1.setBounds(641, 20, 81, 15);
        panel_a.add(lblNdb_1);

        JLabel label_2 = new JLabel("登录到");
        label_2.setBounds(10, 95, 54, 15);
        panel_a.add(label_2);

        tF_OperatorId = new JTextField();
        tF_OperatorId.setText(N4Config.N4_OPERATOR_ID.toString());
        tF_OperatorId.setColumns(10);
        tF_OperatorId.setBounds(74, 92, 66, 21);
        panel_a.add(tF_OperatorId);

        tF_ComplexId = new JTextField();
        tF_ComplexId.setText(N4Config.N4_COMPLEX_ID.toString());
        tF_ComplexId.setColumns(10);
        tF_ComplexId.setBounds(150, 92, 66, 21);
        panel_a.add(tF_ComplexId);

        tF_FacilityId = new JTextField();
        tF_FacilityId.setText(N4Config.N4_FACILITY_ID.toString());
        tF_FacilityId.setColumns(10);
        tF_FacilityId.setBounds(226, 92, 66, 21);
        panel_a.add(tF_FacilityId);

        tF_YardId = new JTextField();
        tF_YardId.setText(N4Config.N4_YARD_ID.toString());
        tF_YardId.setColumns(10);
        tF_YardId.setBounds(302, 92, 66, 21);
        panel_a.add(tF_YardId);

        panel_2 = new JPanel();
        centerPanel.add(panel_2, "panel2");
        panel_2.setLayout(null);

        label_4 = new JLabel("选择文件，并选择导入内容");
        label_4.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        label_4.setBounds(10, 40, 732, 49);
        panel_2.add(label_4);

        MyJTextArea textArea = new MyJTextArea() {

            @Override
            public void drop(DropTargetDropEvent dtde) {
                // TODO Auto-generated method stub
                super.drop(dtde);
                try {
                    Transferable tr = dtde.getTransferable();

                    if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                        dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                        System.out.println("file droped");
                        List list = (List) (dtde.getTransferable()
                                .getTransferData(DataFlavor.javaFileListFlavor));
                        if (list.size() > 1) {
                            tRResult.setText("只能一个文件!");
                        } else {
                            File f = (File) (list.get(0));
                            if (f.getName().endsWith(".xls")) {
                                StaticData.READ_FILE = f;
                                tF_FilePath.setText(f.getAbsolutePath());
                                tRResult.setText("已选择一个文件!");
                            } else {
                                tRResult.setText("必须是.xls文件!");
                            }
                        }

                        dtde.dropComplete(true);
                        this.updateUI();
                    } else {
                        dtde.rejectDrop();
                    }
                } catch (Exception ioe) {
                    ioe.printStackTrace();
                }
            }

        };
        textArea.setFont(new Font("微软雅黑", Font.ITALIC, 15));
        textArea.setEditable(false);
        textArea.setBorder(new LineBorder(new Color(192, 192, 192), 3, true));
        textArea.setBackground(Color.LIGHT_GRAY);
        textArea.setText("把文件\r\n拖到这里\r\n文件名需和船名航次一致");
        textArea.setBounds(91, 103, 199, 119);
        panel_2.add(textArea);

        JLabel label_5 = new JLabel("或者");
        label_5.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        label_5.setBounds(340, 151, 36, 15);
        panel_2.add(label_5);

        tF_FilePath = new JTextField();
        tF_FilePath.setEditable(false);
        tF_FilePath.setBounds(394, 107, 314, 21);
        panel_2.add(tF_FilePath);
        tF_FilePath.setColumns(10);

        JButton button_1 = new JButton("浏览");
        button_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser dlg = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel 2003文件", "xls");
                dlg.setFileFilter(filter);
                dlg.setDialogTitle("打开导入(xls)文件");
                int result = dlg.showOpenDialog(MainFrame.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File file = dlg.getSelectedFile();
                    StaticData.READ_FILE = file;
                    tF_FilePath.setText(file.getAbsolutePath());
                }
            }
        });
        button_1.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        button_1.setBounds(615, 206, 93, 23);
        panel_2.add(button_1);

        comboBox = new JComboBox();
        comboBox.setModel(new DefaultComboBoxModel(new String[]{"进口舱单", "出口提单"}));
        comboBox.setBounds(91, 299, 199, 25);
        comboBox.setSelectedIndex(-1);
        panel_2.add(comboBox);

        button_2 = new JButton("下一步");
        button_2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (tF_FilePath.getText().isEmpty()) {
                    tRResult.setText("请选择文件");
                } else if (comboBox.getSelectedIndex() == -1) {
                    tRResult.setText("请选择导入类型");
                } else {
                    lblStep3.setBackground(lblStep2.getBackground());
                    lblStep3.setForeground(lblStep2.getForeground());
                    lblStep2.setBackground(lblStep1.getBackground());
                    lblStep2.setForeground(lblStep1.getForeground());
                    tRResult.setText("欢迎使用N4导入助手！");

                    StaticData.TYPE = comboBox.getSelectedItem().toString();
                    CardLayout cl = (CardLayout) centerPanel.getLayout();
                    cl.show(centerPanel, "panel3");

                }
            }
        });
        button_2.setBounds(615, 396, 93, 23);
        panel_2.add(button_2);

        JButton btn_logout = new JButton("重新登录");
        btn_logout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                N4Config.N4_USERNAME = "";
                N4Config.N4_PASSOWRD = "";

                CardLayout cl = (CardLayout) centerPanel.getLayout();
                cl.show(centerPanel, "panel1");

                lblStep1.setBackground(lblStep2.getBackground());
                lblStep1.setForeground(lblStep2.getForeground());
                lblStep2.setBackground(lblStep3.getBackground());
                lblStep2.setForeground(lblStep3.getForeground());

                tRResult.setText("欢迎使用N4导入助手！");

            }
        });
        btn_logout.setBounds(512, 396, 93, 23);
        panel_2.add(btn_logout);

        panel_3 = new JPanel();
        centerPanel.add(panel_3, "panel3");
        panel_3.setLayout(new BorderLayout(0, 0));

        JScrollPane scrollPane = new JScrollPane();
        panel_3.add(scrollPane, BorderLayout.CENTER);

        textArea_1 = new JTextArea();
        textArea_1.setBackground(Color.LIGHT_GRAY);
        textArea_1.setEditable(false);
        scrollPane.setViewportView(textArea_1);

        panel = new JPanel();
        FlowLayout flowLayout_1 = (FlowLayout) panel.getLayout();
        flowLayout_1.setAlignment(FlowLayout.LEFT);
        panel_3.add(panel, BorderLayout.NORTH);

        lblNewLabel_2 = new JLabel("点击开始，开始导入");
        lblNewLabel_2.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        panel.add(lblNewLabel_2);

        button_3 = new JButton("开始导入");
        button_3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DoJobRunnable dj = new DoJobRunnable();
                dj.addOutListener(new OutListener() {

                    @Override
                    public void out(String str) {
                        // TODO Auto-generated method stub
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
                        String date = sdf.format(new Date());
                        textArea_1.append("[" + date + "]" + str + "\n");
                        textArea_1.setCaretPosition(textArea_1.getText().length());
                    }

                });
                Thread t = new Thread(dj);
                t.start();
            }
        });
        panel.add(button_3);

        panel_1 = new JPanel();
        FlowLayout flowLayout = (FlowLayout) panel_1.getLayout();
        flowLayout.setAlignment(FlowLayout.RIGHT);
        panel_3.add(panel_1, BorderLayout.SOUTH);

        button_4 = new JButton("上一步");
        button_4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout) centerPanel.getLayout();
                cl.show(centerPanel, "panel2");

                lblStep2.setBackground(lblStep3.getBackground());
                lblStep2.setForeground(lblStep3.getForeground());
                lblStep3.setBackground(lblStep1.getBackground());
                lblStep3.setForeground(lblStep1.getForeground());
            }
        });
        panel_1.add(button_4);

        btnNewButton = new JButton("清空输出");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textArea_1.setText("");
            }
        });
        panel_1.add(btnNewButton);

        southPanel = new JPanel();
        contentPane.add(southPanel, BorderLayout.SOUTH);
        southPanel.setLayout(new BorderLayout(0, 0));

        scrollPane_1 = new JScrollPane();
        southPanel.add(scrollPane_1);

        tRResult = new JTextPane();
        tRResult.setBackground(Color.LIGHT_GRAY);
        tRResult.setEditable(false);
        scrollPane_1.setViewportView(tRResult);
        tRResult.setText("欢迎使用N4导入助手！");
        tRResult.setFont(new Font("微软雅黑", Font.PLAIN, 14));
    }

    //init global font settings
    public static void initGlobalFontSetting(Font fnt) {
        FontUIResource fontRes = new FontUIResource(fnt);
        for (Enumeration keys = UIManager.getDefaults().keys(); keys.hasMoreElements(); )

        {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource)
                UIManager.put(key, fontRes);
        }

    }
}
