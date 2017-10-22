import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class qaq {
  private static final int bufSize = 1024;
  private static int numOfwords = 1024;
  private static String buf = "";
  private static String path = "./";
  private static String libPath = "./bin/";
  private static int[][] graphOfWords = new int[bufSize][bufSize];
  private static int INF = 1000000000;
  private static Map m = new HashMap();
  private static int[][] minRouteMatrix = new int[bufSize][bufSize];
  private static int[][] pathMatrix = new int[bufSize][bufSize];
  private static void floyd() {
    //初始化最短路径数组
    for (int i = 0; i < numOfwords; i++) {
      for (int j = 0; j < numOfwords; j++) {
        if (graphOfWords[i][j] > 0) {
          minRouteMatrix[i][j] = graphOfWords[i][j];
        } else {
          minRouteMatrix[i][j] = INF;
        }
      }
    }
    //开始计算最短路径并且记录路径
    for (int k = 0; k < numOfwords; k++) {
      for (int i = 0; i < numOfwords; i++) {
        for (int j = 0; j < numOfwords; j++) {
          if (minRouteMatrix[i][k] + minRouteMatrix[k][j] < minRouteMatrix[i][j]) {
            minRouteMatrix[i][j] = minRouteMatrix[i][k] + minRouteMatrix[k][j];
            pathMatrix[i][j] = k;
          }
        }
      }
    }
  }
  private static String ReadFile(String fileName) {
    Reader reader = null;
    try {
      // 一次读多个字符
      int tempchars;
      reader = new InputStreamReader(new FileInputStream(fileName));
      String tempBuf = "";
      // 读入多个字符到字符数组中，charread为一次读取字符数
      int flag = 0;
      while ((tempchars = reader.read()) != -1)
      {
        if ((tempchars >= 97 && tempchars <= 122) || tempchars >= 65 && tempchars <= 90) 
        {
          flag = 0;
          tempBuf += (char) tempchars;
        }
         else {
          if (flag == 0)
          {
            buf += tempBuf + " ";
            tempBuf = "";
          }
          flag = 1;
        }
      }
      reader.close();
      return buf;
    } catch (Exception e1) {
      System.out.println(e1);
      return "";
    } finally {
      if (reader != null) {
        try {
          reader.close();
          return buf;
        } catch (IOException e1) {
          System.out.println(e1);
          return "";
        }
      }
    }
  }
  
  /**。
  * @param word1。
  * @param word2。
  * @param opt。
  * @return。
  */
  public static String queryBridgeWords(String word1, String word2, int opt) {
    //查询桥接词,opt为0返回不存在信息，opt为1如不存在则返回null
    String bridgeWords = null;
    if (!(m.containsKey(word1) || m.containsKey(word2))) {
      if (opt == 0) {
        bridgeWords = "No " + word1 + " and " + word2 + " in the graph!";
      }
      return bridgeWords;
    }
    if (!(m.containsKey(word1))) {
      if (opt == 0) {
        bridgeWords = "No " + word1 + " in the graph!";
      }
      return bridgeWords;
    }
    if (!(m.containsKey(word2))) {
      if (opt == 0) {
        bridgeWords = "No " + word2 + " in the graph!";
      }
      return bridgeWords;
    }
    String[] tempBridge = new String[numOfwords];
    int src = (int)m.get(word1);
    int dest = (int)m.get(word2);
    int index = 0;
    for (int j = 0;j < numOfwords;j++) {
      if (graphOfWords[src][j] * graphOfWords[j][dest] > 0) {
        for (Object s:m.keySet()) {
          if ((int) m.get(s) == j) {
            tempBridge[index++]  = (String)s;
          }
        }
      }
    }
    if (index == 0) {
      if (opt == 0) {
        bridgeWords = "No bridge words from " + word1 + " to " + word2 + "!";
      }
      return bridgeWords;
    }
    bridgeWords = "";
    int record = 0;
    for (int i = 0;i < tempBridge.length;i++) {
      if (tempBridge[i] ==  null) {
        break;
      } else {
        record++;
      }
    }
    for (int i = 0;i < record - 1;i++) {
      bridgeWords += tempBridge[i] + ",";
    }
    bridgeWords += tempBridge[record - 1];
    if (opt == 0) {
      bridgeWords = "The bridge words from \"" + word1 + "\" to \"" + word2 + "\" are: " 
        + bridgeWords + ".";
    }
    return bridgeWords;
  }
  
  /**
 * @param proc。
 * @return。
 */
  public static int showDirectedGraph(String[] proc) { //展示有向图
    Writer output = null;
    try {
      output = new OutputStreamWriter(new FileOutputStream(path + "out.dot"));
      String dotout = "digraph dirGraph{\n";
      for (int i = 0; i < proc.length; i++) {
        dotout += "\t" + proc[i] + ";\n";
      }

      for (Object s : m.keySet()) { //写入dot文件
        for (Object s1 : m.keySet()) {
          if (graphOfWords[(int) m.get(s)][(int) m.get(s1)] != 0) {
            dotout += "\t" + s + "->" + s1 + "[label=\"" 
              + String.valueOf(graphOfWords[(int) m.get(s)][(int) m.get(s1)]) + "\"]" + ";\n";
          }
        }
      }
      dotout += "}";
      output.write(dotout);
      output.close();
    } catch (Exception e) {
      System.out.println(e);
      return 0;
    }
    try {
    } catch (Exception e) {
      System.out.println(e);
      return 0;
    }
    return 1;
  }
  
  /**。
 * @param inputText。
 * @return。
 */
  public static String generateNewText(String inputText) { //根据bridge word生成新文本
    String temp = "";
    String result = "";
    int flag = 0;
    for (int i = 0 ;i < inputText.length();i++) {
      int letter = (int)inputText.charAt(i);
      if ((letter >= 97 && letter <= 122) || (letter >= 65 && letter <= 90)) {
        flag = 0;
        temp += (char) letter;
      } else {
        if (flag == 0) {
          temp += " ";
        }
        flag = 1;
      }
    }
    String[] tempArray = temp.split(" ");
    java.util.Random random = new java.util.Random();
    for (int i = 0;i < tempArray.length - 1;i++) {
      String temp0 = queryBridgeWords(tempArray[i],tempArray[i + 1],1);
      if (temp0 == null) {
        result += tempArray[i] + " ";
      } else { 
        String[] temp1 = temp0.split(",");
        int count = 0;
        for (int m = 0;m < temp1.length;m++) {
          if (temp1[m] == null) {
            break;
          }
          count++;
        }
        int rand = random.nextInt(count);
        result += tempArray[i] + " " + temp1[rand] + " ";
      }
    }
    result += tempArray[tempArray.length - 1] + ".";
    return result;
  } 
  
  /**
 * @param route。
 * @param color。
 */
  public static void clearMarked(String route,String color) { 
    Reader reader = null;
    int tempchars;
    String tempBuf = "";
    String[] array = route.split("->");
    try {
      // 一次读多个字符
      reader = new InputStreamReader(new FileInputStream("out.dot"));
      while ((tempchars = reader.read()) != -1) {
        tempBuf += (char) tempchars;
      }
    } catch (Exception e1) {
      System.out.println(e1);
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (IOException e1) {
          System.out.println(e1);
        }
      }
    }
    for (int i = 0;i < array.length - 1;i++) {
      String pattern = array[i] + "->" + array[i + 1] + "\\[color=" + color + ",";
      System.out.println(pattern);
      Pattern p = Pattern.compile(pattern);
      Matcher m = p.matcher(tempBuf);
      System.out.println(m);
      tempBuf = m.replaceFirst(array[i] + "->" + array[i + 1] + "\\[");
    }
    Writer output = null;
    try {
      output = new OutputStreamWriter(new FileOutputStream(path + "out.dot"));
      output.write(tempBuf);
      output.close();
    } catch (Exception e) {
      System.out.println(e);
    }
  }
  
  /**
 * @param word1。
 * @param word2。
 * @param color。
 * @return。
 */
  
  public static String calcShortestPath(String word1, String word2,String color) {
    String res = ShortestPath(word1,word2,color);
    try {
      Thread.currentThread();
	Thread.sleep(100);
    } catch (Exception e) {
      System.out.println(e);
    }
    clearMarked(res,color);
    return res;
  }
  /**。
 * @param word。
 * @param opt。
 * @return。
 */
  
  public static String[] calcShortestPath(String word,int opt) { 
    String[] result = new String[numOfwords];
    int index = 0;
    Random random = new java.util.Random();
    String[] stringChoice = {"red","yellow","green","blue"};
    String[] color = new String[numOfwords];
    try {
      for (Object s:m.keySet()) {
        if (!((String)s).equals(word)) {
          int rand = random.nextInt(stringChoice.length);
          String c = stringChoice[rand];
          result[index] = ShortestPath(word,(String)s,c);
          color[index++]  = c;
        }
      }
      if (opt == 0) {
        String[] cmd = {"cmd.exe", "/C", "start /b" + libPath + "dot " 
            + path + "out.dot -T png -o " + path + "out.png"};
        Runtime.getRuntime().exec(cmd);
        //Thread.currentThread().sleep(100);
      }

    } catch (Exception e) {
      System.out.println(e);
    }
    for (int i = 0;i < result.length;i++) {
      clearMarked(result[i],color[i]);
    }
    return result;
  }
  /**
 * @param word1。
 * @param word2。
 * @param color。
 * @return。
 */
  private static String ShortestPath(String word1, String word2,String color) { //计算两个单词之间的最短路径
    int[] formerPath = new int[numOfwords];
    int []latterPath = new int[numOfwords];
    int indexSrc = (int)m.get(word1);
    int indexDest = (int)m.get(word2);
    int formerIndex = pathMatrix[indexSrc][indexDest];
    int latterIndex = formerIndex;
    int t1 = 0;
    int t2 = 0;
    if (formerIndex == -1) { //there is no path from word1 to word2
      return "No path from " + word1 + " to " + word2;
    }
    while (true) { //获取从 word1->中间点 经历的所有点到数组formerPath
      if (graphOfWords[indexSrc][formerIndex] != 0) { //可一步到达
        formerPath[t1++] = formerIndex;
        break;
      } else { //不可一步到达，寻找上一节点
        formerPath[t1++] = formerIndex;
        formerIndex = pathMatrix[indexSrc][formerIndex];
      }
    }
    while (true) { //获取从 中间点->word2 经历的所有点到数组latterPath
      if (graphOfWords[latterIndex][indexDest] != 0) {
        latterPath[t2] = latterIndex;
        break;
      } else { 
        latterPath[t2++] = latterIndex;
        latterIndex = pathMatrix[latterIndex][indexDest];
      }
    }
    int[] routeArray = new int[t1 + t2];
    for (int i = 0; i < routeArray.length;i++) {
      if (i < t1) {
        routeArray[i] = formerPath[t1 - 1 - i];
      } else { 
        routeArray[i] = latterPath[i - t1 + 1];
      }
    }
    String result = word1 + "->";
    for (int i = 0; i < routeArray.length;i++) {
      for (Object s: m.keySet()) {
        if ((int)m.get(s) == routeArray[i]) {
          result += (String)s + "->";
        }
      }
    }
    result += word2;
    System.out.println(result);
    Reader reader = null;
    int tempchars;
    String tempBuf = "";
    try {
      // 一次读多个字符
      reader = new InputStreamReader(new FileInputStream("out.dot"));
      while ((tempchars = reader.read()) != -1) {
        tempBuf += (char) tempchars;
      }
    } catch (Exception e1) {
      System.out.println(e1);
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (IOException e1) {
          System.out.println(e1);
        }
      }
    }
    String[] tempArry = result.split("->");
    for (int i = 0;i < tempArry.length - 1;i++) {
      String pattern = tempArry[i] + "->" + tempArry[i + 1] + "\\[";
      System.out.println(pattern);
      Pattern p = Pattern.compile(pattern);
      Matcher m = p.matcher(tempBuf);
      System.out.println(m);
      tempBuf = m.replaceFirst(tempArry[i] + "->" + tempArry[i + 1] + "[color=" + color + ",");
    }
    Writer output = null;
    try {
      output = new OutputStreamWriter(new FileOutputStream(path + "out.dot"));
      output.write(tempBuf);
      output.close();
    } catch (Exception e) {
      System.out.println(e);
    }
    System.out.println("the shortest length from " + word1 + " to " + word2  + " is :" 
        + minRouteMatrix[indexSrc][indexDest]);
    
    return result;
  }

  /**
 * @return。
 */
  
  public static String randomWalk() { //随机游走
    int[] resultIndex = new int[bufSize];
    String result = new String();
    java.util.Random random = new java.util.Random();
    int rand = random.nextInt(numOfwords);
    int viaIndex = 0;
    int[][] tempMatrix = new int[numOfwords][numOfwords];
    resultIndex[viaIndex++] = rand;
    while (true) {
      int[] temp = new int[bufSize];//记录一个点的所有出边指向的点，以便随机选取
      int index = 0;
      for (int i = 0; i < numOfwords;i++) { //扫描当前节点的出边
        if (graphOfWords[rand][i] != 0) {
          temp[index++] = i;
        }
      }
      if (index == 0) { //若出度为0，终止随机游动
        break;
      }
      rand = temp[random.nextInt(index)];
      resultIndex[viaIndex++] = rand;//随机选取一个出边
      if (tempMatrix[resultIndex[viaIndex - 2]][resultIndex[viaIndex - 1]] == 1) { //边在之前已经出现过，也停止游走
        break;
      }
      tempMatrix[resultIndex[viaIndex - 2]][resultIndex[viaIndex - 1]] = 1;
    }
    for (int i = 0 ;i < viaIndex;i++) { //将索引转化为字符串
      for (Object s : m.keySet()) {
        if ((int)m.get(s) == resultIndex[i]) {
          result += (String)s;
        }
      }
      if (i != viaIndex - 1) {
        result += "->";
      }
    }
    return result;
  }

  /**
 * @param s。
 */
  public static void init(String[] s) {
    int seq = 0;
    for (int i = 0; i < s.length; i++) { //每个字符串号
      if (!m.containsKey(s[i])) {
        m.put(s[i], seq++);
      }
    }
    for (int i = 0; i < s.length - 1; i++) { //统计权值
      graphOfWords[(int) m.get(s[i])][(int) m.get(s[i + 1])] += 1;
    }
    numOfwords = seq;
    for (int i = 0; i < s.length;i++) {
      for (int j = 0;j < s.length;j++) {
        pathMatrix[i][j] = -1;
      }
    }
    floyd();//使用Floyd算法在程序读入文本后计算出最短路径，以便之后的功能使用
  }
  
  /**
 * @param args。
 */
  public static void main(String[] args) {
    JFrame frame = new JFrame("lab1"); //GUI的主界面
    frame.setSize(750, 500);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    JPanel panel = new JPanel();
    frame.add(panel);
    placeComponents(panel);//调用组件布局函数
    frame.setVisible(true);
    //queryBridgeWords("natio","any",0);
    //System.out.println(generateNewText("final place those who gave"));
    //System.out.println(calcShortestPath("lives"));
    //System.out.println(randomWalk());
  }

  public static void placeComponents(JPanel panel)
  {
  	panel.setLayout(null);									//使用绝对布局
  	JButton openFileButton = new JButton("Open File");
  	openFileButton.setBounds(320, 200, 120, 25);			//首先设置打开文件按钮
  	panel.add(openFileButton);
  	
  	openFileButton.addActionListener(new ActionListener() 	//打开文件按钮事件监听器
  	{
  		public void actionPerformed(ActionEvent e)
  		{
  			JFileChooser fileChoose = new JFileChooser();	//选择文件
  			fileChoose.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
  			fileChoose.showDialog(new JLabel(), "选择");
  			File file = fileChoose.getSelectedFile();
  			if (file.isDirectory())
  			{
  				System.out.println("文件夹" + file.getAbsolutePath());
  			}
  			else if (file.isFile())
  			{
  				System.out.println("文件:"+file.getAbsolutePath());
  			}
  			System.out.println(fileChoose.getSelectedFile().getName());

  			String buf = ReadFile(fileChoose.getSelectedFile().getName());	//将文件名传入函数
  			
  	    String[] procBuf = buf.split(" ");
  	    init(procBuf);
  	    showDirectedGraph(procBuf);	
  	    
  	    Choose_FileFunction();							//打开文件后，对文件选择功能操作界面
  			
  		}
  	});
  }

  public static void Choose_FileFunction() 
  {
  	JFrame new_frame = new JFrame("文件操作功能选择");
  	new_frame.setSize(650, 400);
  	new_frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
  	new_frame.setVisible(true);
  	
  	new_frame.addWindowListener(new WindowAdapter() 		//关闭分界面后，不会关闭程序
  	{
  		public void windowClosing(WindowEvent e)
  		{
  			new_frame.dispose();
  		}
  	});
  	
  	JPanel choose_function_panel = new JPanel();
  	new_frame.add(choose_function_panel);
  	
  	new_placeComponents(choose_function_panel);
  	
  	new_frame.setVisible(true);
  }

  public static void new_placeComponents(JPanel choose_function_panel)
  {
  	choose_function_panel.setLayout(null);
  	
  	JButton showGraph_Button = new JButton("展示有向图");	//展示有向图功能按钮
  	showGraph_Button.setBounds(220, 35, 200, 35);
  	choose_function_panel.add(showGraph_Button);
  	
  	showGraph_Button.addActionListener(new ActionListener() 
  	{
  		public void actionPerformed(ActionEvent e)
  		{			
				JFrame picture_frame = new JFrame("图片浏览");
				picture_frame.setSize(2000,1000);
				picture_frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				picture_frame.setVisible(true);
				
				JPanel picturePane = new JPanel();
				
				JLabel label = new JLabel();
				label.setSize(500,500);
				
				picturePane.add(label);
				label.setIcon(new ImageIcon(".\\out.png"));
				
				JScrollPane scrollPane = new JScrollPane(picturePane);
				scrollPane.validate();
				scrollPane.setPreferredSize(new Dimension(900,650));
				
				picture_frame.add(scrollPane);
				
				picture_frame.addWindowListener(new WindowAdapter() 
				{
					public void windowClosing(WindowEvent e)
					{
						picture_frame.dispose();
					}
				});	
  		}
  	});
  	
  	JButton queryBridgeWords_Button = new JButton("查询桥接词");
  	queryBridgeWords_Button.setBounds(220, 90, 200, 35);
  	choose_function_panel.add(queryBridgeWords_Button);
  	
  	queryBridgeWords_Button.addActionListener(new ActionListener() 
  	{
  		public void actionPerformed(ActionEvent e)
  		{
  			JFrame queryBridgeWords_frame = new JFrame("查询桥接词");
  			queryBridgeWords_frame.setSize(600,400);
  			queryBridgeWords_frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
  			queryBridgeWords_frame.setVisible(true);
  			
  			JPanel queryBridgeWords_Panel = new JPanel();
  			queryBridgeWords_frame.add(queryBridgeWords_Panel);
  			
  			queryBridgeWords_Panel.setLayout(null);
  			
  			JLabel FirstWord = new JLabel("第一个英文单词：");
  			FirstWord.setBounds(150, 20, 120, 40);
  			queryBridgeWords_Panel.add(FirstWord);
  			
  			JTextField FirstText = new JTextField();
  			FirstText.setBounds(260, 28, 165, 25);
  			queryBridgeWords_Panel.add(FirstText);
  			
  			JLabel SecondWord = new JLabel("第二个英文单词：");
  			SecondWord.setBounds(150, 70, 120, 40);
  			queryBridgeWords_Panel.add(SecondWord);
  			
  			JTextField SecondText = new JTextField();
  			SecondText.setBounds(260, 78, 165, 25);
  			queryBridgeWords_Panel.add(SecondText);
  			
  			JButton query_Button = new JButton("查询");
  			query_Button.setBounds(250, 130, 80, 25);
  			queryBridgeWords_Panel.add(query_Button);
  			
  			JLabel ResultWord = new JLabel("查询结果：");
  			ResultWord.setBounds(100, 160, 120, 40);
  			queryBridgeWords_Panel.add(ResultWord);
  			
  			JTextArea result = new JTextArea();
  			result.setBounds(180, 165, 300, 50);
  			result.setLineWrap(true);
  			queryBridgeWords_Panel.add(result);
  			
  			query_Button.addActionListener(new ActionListener() 
  			{
					public void actionPerformed(ActionEvent e)
					{
						String query_result = queryBridgeWords(FirstText.getText(),SecondText.getText(),0);
						System.out.println(queryBridgeWords(FirstText.getText(),SecondText.getText(),0));
						result.setText(query_result);
					}
  			});
  			
  			queryBridgeWords_frame.addWindowListener(new WindowAdapter() 
				{
					public void windowClosing(WindowEvent e)
					{
						queryBridgeWords_frame.dispose();
					}
				});	
  		}
  	});
  	
  	JButton generateNewText_Button = new JButton("生成新文本");
  	generateNewText_Button.setBounds(220, 145, 200, 35);
  	choose_function_panel.add(generateNewText_Button);
  	
  	generateNewText_Button.addActionListener(new ActionListener() 
  	{
  		public void actionPerformed(ActionEvent e)
  		{
  			JFrame generateNewText_frame = new JFrame("生成新文本");
  			generateNewText_frame.setSize(600,400);
  			generateNewText_frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
  			generateNewText_frame.setVisible(true);
  			
  			JPanel generateNewText_Panel = new JPanel();
  			generateNewText_frame.add(generateNewText_Panel);
  			
  			generateNewText_Panel.setLayout(null);
  			
  			JLabel OldText = new JLabel("请输入文本：");
  			OldText.setBounds(100, 53, 120, 40);
  			generateNewText_Panel.add(OldText);
  			
  			JTextArea Old_Text = new JTextArea(5,15);
  			Old_Text.setBounds(200, 48, 300, 60);
  			Old_Text.setLineWrap(true);
  			generateNewText_Panel.add(Old_Text);
  			
  			JButton generate_Button = new JButton("生成");
  			generate_Button.setBounds(260, 140, 80, 25);
  			generateNewText_Panel.add(generate_Button);
  			
  			JLabel NewText = new JLabel("生成的新文本：");
  			NewText.setBounds(100, 200, 120, 40);
  			generateNewText_Panel.add(NewText);
  			
  			JTextArea New_Text = new JTextArea(5,15);
  			New_Text.setBounds(200, 188, 300, 60);
  			New_Text.setLineWrap(true);
  			generateNewText_Panel.add(New_Text);
  			
  			generate_Button.addActionListener(new ActionListener() {
  				public void actionPerformed(ActionEvent e)
  				{
  					New_Text.setText(generateNewText(Old_Text.getText()));
  				}
  			});
  			
  			generateNewText_frame.addWindowListener(new WindowAdapter() 
				{
					public void windowClosing(WindowEvent e)
					{
						generateNewText_frame.dispose();
					}
				});	
  		}
  	});
  	
  	JButton calcShortestPath_Button = new JButton("计算最短路径");
  	calcShortestPath_Button.setBounds(220, 200, 200, 35);
  	choose_function_panel.add(calcShortestPath_Button);
  	
  	calcShortestPath_Button.addActionListener(new ActionListener() 
  	{
			public void actionPerformed(ActionEvent e)
			{
				JFrame calcShortestPath_frame = new JFrame("计算最短路径");
				calcShortestPath_frame.setSize(600,400);
				calcShortestPath_frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				calcShortestPath_frame.setVisible(true);
				
				JTabbedPane tabbedpane = new  JTabbedPane();
				
				JPanel first_panel = new JPanel();
				tabbedpane.addTab("计算两个单词最短路径", first_panel);
				
				first_panel.setLayout(null);
				JLabel FirstWord = new JLabel("第一个单词：");
  			FirstWord.setBounds(150, 20, 120, 40);
  			first_panel.add(FirstWord);
  			
  			JTextField FirstText = new JTextField();
  			FirstText.setBounds(260, 28, 165, 25);
  			first_panel.add(FirstText);
  			
  			JLabel SecondWord = new JLabel("第二个单词：");
  			SecondWord.setBounds(150, 70, 120, 40);
  			first_panel.add(SecondWord);
  			
  			JTextField SecondText = new JTextField();
  			SecondText.setBounds(260, 78, 165, 25);
  			first_panel.add(SecondText);
  			
  			JLabel tag1 = new JLabel("选择路径颜色");
  			tag1.setBounds(60, 125, 120, 40);
  			first_panel.add(tag1);
  			
  			JRadioButton colorButton1 = new JRadioButton("red");
  			JRadioButton colorButton2 = new JRadioButton("yellow");
  			JRadioButton colorButton3 = new JRadioButton("green");
  			JRadioButton colorButton4 = new JRadioButton("blue");
  			first_panel.add(colorButton1);
  			first_panel.add(colorButton2);
  			first_panel.add(colorButton3);
  			first_panel.add(colorButton4);
  			ButtonGroup group = new ButtonGroup();
  			group.add(colorButton1);
  			group.add(colorButton2);
  			group.add(colorButton3);
  			group.add(colorButton4);
  			colorButton1.setBounds(150, 120, 70, 50);
  			colorButton2.setBounds(230, 120, 70, 50);
  			colorButton3.setBounds(310, 120, 70, 50);
  			colorButton4.setBounds(390, 120, 70, 50);
  			
  			JButton calcShortestPath_Button = new JButton("计算");
  			calcShortestPath_Button.setBounds(250, 180, 100, 25);
  			first_panel.add(calcShortestPath_Button);
  			
  			JLabel result_tag = new JLabel("计算结果：");
  			result_tag.setBounds(80, 240, 120, 40);
  			first_panel.add(result_tag);
  			
  			JTextArea result_Text = new JTextArea(5,15);
  			result_Text.setLineWrap(true);
  			
  			JScrollPane scrollPane = new JScrollPane(result_Text);
				scrollPane.validate();
				scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
				scrollPane.setBounds(160, 230, 300, 60);
				first_panel.add(scrollPane);
				
				colorButton1.addItemListener(new ItemListener() 
				{
					public void itemStateChanged(ItemEvent e)
					{
						if (colorButton1.isSelected())
						{
							calcShortestPath_Button.addActionListener(new ActionListener() 
			  			{
								public void actionPerformed(ActionEvent e)
			  				{
										result_Text.setText(calcShortestPath(FirstText.getText(),SecondText.getText(),"red"));
			  				}
			  			});
						}
					}
				});
  			
  			colorButton2.addItemListener(new ItemListener() 
				{
					public void itemStateChanged(ItemEvent e)
					{
						if (colorButton2.isSelected())
						{
							calcShortestPath_Button.addActionListener(new ActionListener() 
			  			{
								public void actionPerformed(ActionEvent e)
			  				{
									result_Text.setText(calcShortestPath(FirstText.getText(),SecondText.getText(),"yellow"));
			  				}
			  			});
						}
					}
				});
  			
  			colorButton3.addItemListener(new ItemListener() 
				{
					public void itemStateChanged(ItemEvent e)
					{
						if (colorButton3.isSelected())
						{
							calcShortestPath_Button.addActionListener(new ActionListener() 
			  			{
								public void actionPerformed(ActionEvent e)
			  				{
									result_Text.setText(calcShortestPath(FirstText.getText(),SecondText.getText(),"green"));
			  				}
			  			});
						}
					}
				});
  			
  			colorButton4.addItemListener(new ItemListener() 
				{
					public void itemStateChanged(ItemEvent e)
					{
						if (colorButton4.isSelected())
						{
							calcShortestPath_Button.addActionListener(new ActionListener() 
			  			{
								public void actionPerformed(ActionEvent e)
			  				{
									result_Text.setText(calcShortestPath(FirstText.getText(),SecondText.getText(),"blue"));
			  				}
			  			});
						}
					}
				});

				
				JPanel second_panel = new JPanel();
				tabbedpane.addTab("计算一个单词最短路径", second_panel);
				
				second_panel.setLayout(null);
				
				JLabel word = new JLabel("请输入单词：");
  			word.setBounds(160, 60, 120, 40);
  			second_panel.add(word);
  			
  			JTextField text = new JTextField();
  			text.setBounds(260, 68, 165, 25);
  			second_panel.add(text);
  			
  			JButton calc_Button = new JButton("计算");
  			calc_Button.setBounds(250, 120, 100, 25);
  			second_panel.add(calc_Button);
  			
  			JTextArea result_text = new JTextArea(5,15);
  			result_text.setLineWrap(true);
  			
  			JScrollPane new_scrollPane = new JScrollPane(result_text);
  			new_scrollPane.validate();
  			new_scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
  			new_scrollPane.setBounds(150, 180, 300, 60);
  			second_panel.add(new_scrollPane);
  			
  			calc_Button.addActionListener(new ActionListener() 
  			{
					public void actionPerformed(ActionEvent e)
  				{
						String[] new_result = calcShortestPath(text.getText(),1);
						for (int i = 0 ; i < new_result.length ; i++)
						{
							System.out.println(new_result[i]);
							result_text.append(new_result[i]);
							result_text.append("\n");
						}
  				}
  			});
				
				calcShortestPath_frame.setContentPane(tabbedpane);
				
				calcShortestPath_frame.addWindowListener(new WindowAdapter() 
				{
					public void windowClosing(WindowEvent e)
					{
						calcShortestPath_frame.dispose();
					}
				});	
			}
		});
  	
  	JButton randomWalk_Button = new JButton("随机游走");
  	randomWalk_Button.setBounds(220, 255, 200, 35);
  	choose_function_panel.add(randomWalk_Button);
  	
  	randomWalk_Button.addActionListener(new ActionListener() 
  	{
			public void actionPerformed(ActionEvent e)
			{
				JFrame randomWalk_frame = new JFrame("随机游走");
				randomWalk_frame.setSize(600,400);
				randomWalk_frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				randomWalk_frame.setVisible(true);
  			
  			JPanel randomWalk_Panel = new JPanel();
  			randomWalk_frame.add(randomWalk_Panel);
  			
  			randomWalk_Panel.setLayout(null);
  			
  			JLabel randomWalk_tag = new JLabel("随机游走结果：");
  			randomWalk_tag.setBounds(60, 40, 120, 40);
  			randomWalk_Panel.add(randomWalk_tag);
  			
  			JTextArea result_Text = new JTextArea(5,15);
  			result_Text.setBounds(160, 40, 400, 200);
  			result_Text.setLineWrap(true);
  			randomWalk_Panel.add(result_Text);
  			
  			result_Text.setText(randomWalk());
  			
  			randomWalk_frame.addWindowListener(new WindowAdapter() 
				{
					public void windowClosing(WindowEvent e)
					{
						randomWalk_frame.dispose();
					}
				});	
			}
		});
  }
}

