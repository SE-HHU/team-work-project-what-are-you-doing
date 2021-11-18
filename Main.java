package ui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import storages.Record;
import tools.CalculateOperation;
import tools.SpawnOperation;


public class Main extends JFrame{
    /**
     * 建立一个显示窗口
     */
    public static void run(final JFrame f,int width,int height){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                f.setTitle("四则运算");//显示框架的标题
                f.setLocation(0, 0);//显示框架左上点的坐标
                f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//点击×后退出
                f.setSize(width,height);//显示框架的宽和高
                f.getContentPane().setBackground(new Color(213,202,189));//设置背景颜色
                f.setVisible(true);//是否显示框架
            }
        });
    }
    private double rate=0.0;
    private int width;//屏幕的宽
    private int height;//屏幕的高
    private JLabel l;
    private JTextField tf=new JTextField("     韩师傅计算");
    private JButton b1=new JButton("记录");
    private JButton b2=new JButton("错题");
    private JButton b3=new JButton("做题");
    private JButton b4=new JButton("批改");
    private JButton b6=new JButton("帮助");
    //一些必要的按钮，具体用处见按钮名
    private JPanel p=new JPanel(null);
    //屏幕右侧的框架，null指定排列方式为坐标式
    private JFreeChart fc1;
    //建立近五次正确率的折线图
    private ScrollPane sl;
    //建立一个滑动列表，便于浏览错题
    private ImageIcon i0=new ImageIcon("./Images/hsfjs.png");
    private ImageIcon i1=new ImageIcon("./Images/record.png");
    //关于记录的图片，放置在”记录“按钮中
    private ImageIcon i2=new ImageIcon("./Images/error.png");
    //关于错题的图片，放置在”错题“按钮中
    private ImageIcon i3=new ImageIcon("./Images/do.png");
    //关于做题的图片，放置在”做题“按钮中
    private ImageIcon i4=new ImageIcon("./Images/correct.png");
    //关于批改的图片，放置在”批改“按钮中
    private ImageIcon i6=new ImageIcon("./Images/help.png");
    //关于帮助的图片，放置在”帮助“按钮中
    private JButton bt4;

    Color c1=new Color(75,68,83);
    Color c2=new Color(251,234,255);
    //定义一些必要的颜色，用RGB表示
    Font f1=new Font("楷体",Font.BOLD,40);
    Font f2=new Font("楷体",Font.BOLD,50);
    //定义一些必要的字体、字体样式、字体大小


    //初始化各个组件的颜色
    public void InitColor(){
        tf.setBackground(null);
        b1.setBackground(c1);
        b2.setBackground(c1);
        b3.setBackground(c1);
        b4.setBackground(c1);
        b6.setBackground(c1);
        p.setBackground(c2);
    }
    //初始化各个组件中字的字体
    public void InitFont(){
        tf.setFont(new Font("华文彩云",Font.BOLD,40));
        b1.setFont(f2);
        b2.setFont(f2);
        b3.setFont(f2);
        b4.setFont(f2);
        b6.setFont(f2);
    }
    //初始化各个组件在窗口中的位置
    public void InitBounds(){
        int h=height/8,w=width/3;
        tf.setBounds(0,0,w,h);
        b1.setBounds(0,h,w,h);
        b2.setBounds(0,2*h,w,h);
        b3.setBounds(0,3*h,w,h);
        b4.setBounds(0,4*h,w,h);
        b6.setBounds(0,5*h,w,h);
    }
    //为每个按钮添加监听事件
    public void InitAction(){
        b1.addActionListener(al1);
        b2.addActionListener(al2);
        b3.addActionListener(al3);
        b4.addActionListener(al4);
        b6.addActionListener(al6);
    }
    //初始化各个组件的字的颜色
    public void InitForeGround(){

        tf.setForeground(c1);
        b1.setForeground(Color.white);
        b2.setForeground(Color.white);
        b3.setForeground(Color.white);
        b4.setForeground(Color.white);
        b6.setForeground(Color.white);
    }
    //为每个按钮添加图标
    public void InitIcon(){
        i1.setImage(i1.getImage().getScaledInstance(40,40, 0));
        i2.setImage(i2.getImage().getScaledInstance(40,40, 0));
        i3.setImage(i3.getImage().getScaledInstance(40,40, 0));
        i4.setImage(i4.getImage().getScaledInstance(40,40, 0));
        i6.setImage(i6.getImage().getScaledInstance(40,40, 0));
        b1.setIcon(i1);
        b2.setIcon(i2);
        b3.setIcon(i3);
        b4.setIcon(i4);
        b6.setIcon(i6);
    }

    //将其他按钮的样式还原，改变此按钮的样式，以此达到突出显示的效果
    public void InitActionPerformed(JButton b){
        InitColor();
        InitForeGround();
        b.setBackground(c2);
        b.setForeground(Color.black);
        p.removeAll();
    }
    //刷新组件
    public void RefreshPanel(){
        p.revalidate();
        p.repaint();
    }

    /**
     * 记录
     */
    ActionListener al1=new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            InitActionPerformed(b1);//突出显示b1按钮
            LinkedList<Double> q=Record.accuracyRate;
            fc1=new chart("rate",q).getFc();
            ChartPanel cp1=new ChartPanel(fc1,true);
            cp1.setBounds(50,100,2*width/3-100,height-200);
            p.add(cp1);
            RefreshPanel();
        }
    };
    /**
     * 错题
     */
    ActionListener al2=new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            InitActionPerformed(b2);
            JButton bt1=new JButton("");
            JButton bt2=new JButton("");
            bt1.setBounds(0,0,width/3,height/8);
            bt2.setBounds(width/3,0,width/3,height/8);
            bt1.setBackground(c1);
            bt2.setBackground(c1);
            bt1.setFont(f2);
            bt2.setFont(f2);
            bt1.setForeground(Color.white);
            bt2.setForeground(Color.white);
            bt1.setBorder(null);
            bt2.setBorder(null);
            p.add(bt1);
            p.add(bt2);
            sl=new ScrollPane(new File("./Record.txt"));
            JScrollPane s= sl.getSl();
            s.setBounds(0,height/8,2*width/3,7*height/8);
            p.add(s);
            RefreshPanel();
        }
    };
    /**
     * 做题
     */
    ActionListener al3=new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            InitActionPerformed(b3);
            JTextField tf1=new JTextField("年级：");
            tf1.setBounds(width/6,height/9,width/6,4*height/27);
            tf1.setFont(f2);
            tf1.setBackground(c2);
            tf1.setBorder(null);
            JTextField tf2=new JTextField("上下册：");
            tf2.setBounds(width/6,height/3,width/6,4*height/27);
            tf2.setFont(f1);
            tf2.setBackground(c2);
            tf2.setBorder(null);
            String[] s1=new String[]{"一年级","二年级","三年级","四年级","五年级","六年级"};
            String[] s2=new String[]{"上册","下册"};
            JComboBox cb1=new JComboBox(s1);
            cb1.setSelectedIndex(3);
            cb1.setBounds(width/3,height/9+15,width/6+30,4*height/27-30);
            cb1.setFont(f2);
            JComboBox cb2=new JComboBox(s2);
            cb2.setSelectedIndex(0);
            cb2.setBounds(width/3,height/3+15,width/6+30,4*height/27-30);
            cb2.setFont(f2);
            JButton bt2=new JButton("立即做题");
            bt2.setBounds(width/3,2*height/3,width/3-20,height/3-30);
            bt2.setBackground(c1);
            bt2.setFont(f1);
            bt2.setForeground(Color.white);
            bt2.addActionListener(new ActionListener() {
                /**
                 * 在这里添加题目打印
                 * @param e
                 */
                @Override
                public void actionPerformed(ActionEvent e) {
                    int grade=cb1.getSelectedIndex();
                    int uad=cb2.getSelectedIndex();
                    switch (grade){
                        case 0:
                            if(uad==0){
                                SpawnOperation.spawnArithmetic(10,10,1,
                                        false,false,true,
                                        false, false,false);
                            }else {
                                SpawnOperation.spawnArithmetic(15,10,1,
                                        false,false,true,
                                        true, false,false);
                            }
                            ;break;
                        case 1:
                            if(uad==0){
                                SpawnOperation.spawnArithmetic(20,20,2,
                                        false,false,true,
                                        true, false,false);
                            }else {
                                SpawnOperation.spawnArithmetic(20,20,2,
                                        false,false,true,
                                        true, false,false);
                            }
                            break;
                        case 2:
                            if(uad==0){
                                SpawnOperation.spawnArithmetic(20,100,3,
                                        false,false,true,
                                        true, true,false);
                            }else {
                                SpawnOperation.spawnArithmetic(20,100,3,
                                        false,false,true,
                                        true, true,true);
                            }
                            break;
                        case 3:
                            if(uad==0){
                                SpawnOperation.spawnArithmetic(20,100,3,
                                        true,false,true,
                                        true, true,true);
                            }else {
                                SpawnOperation.spawnArithmetic(20,100,3,
                                        true,false,true,
                                        true, true,true);
                            }
                            break;
                        case 4:
                            if(uad==0){
                                SpawnOperation.spawnArithmetic(20,100,3,
                                        true,false,true,
                                        true,true,true);
                            }else {
                                SpawnOperation.spawnArithmetic(20,100,3,
                                        true,false,true,
                                        true,true,true);
                            }
                            break;
                        case 5:
                            if(uad==0){
                                SpawnOperation.spawnArithmetic(20,100,3,
                                        true,true,true,
                                        true,true,true);
                            }else {
                                SpawnOperation.spawnArithmetic(20,100,3,
                                        true,true,true,
                                        true,true,true);
                            }
                            break;
                        default:System.out.println("wrong");
                    }
                    SpawnOperation.print();
                    try {
                        Runtime.getRuntime().exec("notepad.exe ./Exercises.txt");
                        Runtime.getRuntime().exec("notepad.exe ./Answers.txt");
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    bt2.setBackground(c2);
                    p.remove(bt2);
                    bt4=new JButton("查看答案");
                    bt4.setFont(f1);
                    bt4.setBackground(c1);
                    bt4.setForeground(Color.white);
                    bt4.setBounds(width/3,2*height/3,width/3-20,height/3-30);
                    bt4.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try {
                                Runtime.getRuntime().exec("notepad.exe ./Exercises.txt");
                                Runtime.getRuntime().exec("notepad.exe ./Results.txt");
                            } catch (IOException ioException) {
                                ioException.printStackTrace();
                            }
                        }
                    });
                    p.add(bt4);
                    RefreshPanel();
                    //打开文件
                }
            });
            p.add(tf1);
            p.add(tf2);
            p.add(cb1);
            p.add(cb2);
            p.add(bt2);
            RefreshPanel();
        }
    };
    /**
     * 批改
     */
    ActionListener al4=new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            InitActionPerformed(b4);
            JButton  bt0=new JButton("开始批改");
            bt0.setBackground(c2);
            bt0.setFont(f1);
            bt0.setForeground(Color.black);
            bt0.setBounds(0,0,2*width/3,height);
            bt0.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    InitActionPerformed(b4);
                    CalculateOperation.check();
                    JTextField tf=new JTextField("正确率："+Record.accuracyRate.getLast());
                    tf.setBounds(0,0,2*width/3,height/4);
                    tf.setFont(f1);
                    tf.setBackground(c2);
                    tf.setForeground(Color.black);
                    ScrollPane sp=new ScrollPane(Record.wrongArithmetics);
                    JScrollPane s= sp.getSl();
                    s.setBounds(0,height/4,2*width/3,3*height/4);
                    p.add(s);
                    p.add(tf);
                    RefreshPanel();
                }
            });
            p.add(bt0);
            RefreshPanel();
        }
    };
    /**
     * 帮助
     */
    ActionListener al6=new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            InitActionPerformed(b6);
            String s="                          帮助文档\n--记录\n  这里有你的最近五次的正确率变化折线图\n--错题\n" +
                    "  这里有你历次做题所产生的错题，包括这些错题的产生时间" +
                    "\n--做题\n  在这里你可以即时练习四则运算，通过选择年级和上下册有针对地练习，选择好年级后点击’立即做题‘，即可在弹出的记事本中做题：" +
                    "在Exercises.txt中查看题目，在Answers.txt中填写你的答案，填写完成后保存（快捷键ctrl+s）。现在你就可以点击查看答案" +
                    "查看题目的答案，并在批改中查看正确率以及错题\n" +
                    "--批改\n  点击立即批改即可对刚刚做的题目进行批改得到正确率和此次做题产生的错题";
            JTextArea ta=new JTextArea();
            ta.setLineWrap(true);
            ta.setText(s);
            ta.setBounds(0,0,2*width/3,height);
            ta.setFont(new Font("楷体",Font.BOLD,20));
            p.add(ta);
            RefreshPanel();
        }
    };

    public Main(int w,int h)
    {
        try{
            this.rate= Record.accuracyRate.getLast();
        }catch (Exception e){};
        this.width=w;
        this.height=h;
        InitAction();
        setLayout(null);
        add(tf);
        add(b1);
        add(b2);
        add(b3);
        add(b4);
        add(b6);
        InitBounds();
        InitColor();
        InitFont();
        InitForeGround();
        InitIcon();
        add(p);
        p.setBounds(width/3,0,2*width/3,height);
    }

    public static void main(String[] args) {
        Main m=new Main(1080,650);
        run(m,1080,650);
    }

}

class chart{
    /**
     * 关于制作表格的类
     */
    private Queue<Double> rates;
    private DefaultCategoryDataset dcd=new DefaultCategoryDataset();//制作表格需要的数据
    private JFreeChart fc=ChartFactory.createLineChart("","","",dcd,
            PlotOrientation.VERTICAL,false,false,false);
    //建立一个表格同时设置表格的一些属性，如标题、数据等
    public chart(String s,Queue<Double> ra){
        StandardChartTheme standardChartTheme = new StandardChartTheme("CN");
        standardChartTheme.setExtraLargeFont(new Font("楷体", Font.BOLD, 20));
        standardChartTheme.setRegularFont(new Font("楷体", Font.PLAIN, 15));
        standardChartTheme.setLargeFont(new Font("楷体", Font.PLAIN, 15));
        ChartFactory.setChartTheme(standardChartTheme);
        int i=1;
        fc.setBorderPaint(new Color(251,234,255));
        //制作正确率的表格
        fc.setBorderPaint(new Color(0,0,0,0));
        for (double r:ra){
                dcd.addValue(r,"",""+i);
                i++;
            }
        //继续设置一些表格的属性

        CategoryPlot plot = (CategoryPlot)fc.getPlot();
        plot.setBackgroundAlpha(0.5f);
        plot.setForegroundAlpha(1.0f);
        LineAndShapeRenderer renderer = (LineAndShapeRenderer)plot.getRenderer();
        renderer.setBaseShapesVisible(true);
        renderer.setBaseLinesVisible(true);
        renderer.setUseSeriesOffset(true);
        renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        renderer.setBaseItemLabelsVisible(true);
    }

    /**
     * 返回表格
     * @return
     */
    public JFreeChart getFc(){
        return this.fc;
    }
}
class ScrollPane{
    /**
     * 自定义滑动列表
     */
    private JList<String> l;//列表所需的数据
    private JScrollPane sl;//列表本体
    private File file;
    public ScrollPane(File f){
        this.file=f;
        ArrayList<String> errors=new ArrayList<>();
        try {
            BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(f)));
            String s;
            while ((s=br.readLine())!=null){
                errors.add(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        l=new JList<String>(errors.toArray(new String[errors.size()]));
        l.setVisibleRowCount(10);
        l.setFont(new Font("楷体",Font.BOLD,30));
        l.setBackground(new Color(251,234,255));
        sl=new JScrollPane(l);
    }
    public ScrollPane(ArrayList<String> al){
        l=new JList<String>(al.toArray(new String[al.size()]));
        l.setVisibleRowCount(10);
        l.setFont(new Font("楷体",Font.BOLD,30));
        l.setBackground(new Color(251,234,255));
        sl=new JScrollPane(l);
    }
    public JScrollPane getSl(){
        return this.sl;
    }
}