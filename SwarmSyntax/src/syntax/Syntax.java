package syntax;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import syntax.ExampleFileFilter;

public class Syntax 
{
	
	void go()
	{
		frame = new JFrame("Swarm's Syntax Analyser"); 
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 600);
		frame.setVisible(true);
		frame.setLocation(200, 80);
		
		Container contentPane=frame.getContentPane();		
	    contentPane.setLayout(new BorderLayout());
	    
	    myal = new MyAL();
	    
	    openFile = new JButton("Open File");
	    openFile.addActionListener(myal);
	    openFile.setToolTipText("You can open a grammar file instead of typing by yourself!");
	    analyse = new JButton("Analyse");
	    analyse.addActionListener(myal);
	    analyse.setToolTipText("When you've input the grammar and the sentence,click here to analyse!");
	    clearAll = new JButton("Clear All");
	    clearAll.addActionListener(myal);
	    clearAll.setToolTipText("It means reset all,go back to the beginning when nothing happens!");
	    about = new JButton("About");
	    about.setToolTipText("About me!");
	    about.addActionListener(myal);
	    
	    blankLabel = new JLabel("	                      ");
	    resultLabel = new JLabel("Result: ");
	    resultLabel.setToolTipText("Show the result here");
	    resultField = new JTextField("N/A");
	    resultField.setEditable(false);
	   resultField.setColumns(6);
	    resultField.setToolTipText("Show the result here");
	    northPanel = new JPanel();
	    
	    northPanel.add(openFile);
	    northPanel.add(analyse);
	    northPanel.add(clearAll);
	    northPanel.add(about);
	    northPanel.add(blankLabel);
	    northPanel.add(resultLabel);
	    northPanel.add(resultField);
	    
	    contentPane.add(northPanel,BorderLayout.NORTH);
	    
	    centerPanel = new JPanel();
	    centerPanel.setLayout(new  GridLayout(2,1));
	    
	    middlePanel = new JPanel();
	    downPanel = new JPanel();
	    centerPanel.add(middlePanel);
	    centerPanel.add(downPanel);
	    
	    leftPanel = new JPanel();
	    rightPanel = new JPanel();
	    middlePanel.setLayout(new GridLayout(1,2));
	    middlePanel.add(leftPanel);
	    middlePanel.add(rightPanel);
	    
	    inputGrammar = new JLabel("Please input the Grammar in the textarea below!");
	    grammarArea = new JTextArea();
	    inputSentence = new JLabel("Please input the sentence to be analysed: ");
	    sentenceField = new JTextField();
	    sentenceField.setColumns(18);
	    sentencePanel = new JPanel();
	    sentencePanel.add(inputSentence);
	    sentencePanel.add(sentenceField);
	    grammarScrollPane = new JScrollPane(grammarArea);
	    leftPanel.setLayout(new BorderLayout());
	    leftPanel.add(inputGrammar,BorderLayout.NORTH);
	    leftPanel.add(grammarScrollPane,BorderLayout.CENTER);
	    leftPanel.add(sentencePanel,BorderLayout.SOUTH);
	    leftPanel.setBorder(new TitledBorder("Input Area"));
	    
	    rightPanel.setLayout(new BorderLayout());
	    rightPanel.setBorder(new TitledBorder("Output the standard family of itemsets"));
	    familyArea = new JTextArea();
	    familyArea.setEditable(false);
	    rightScrollPane = new JScrollPane(familyArea);
	    rightPanel.add(rightScrollPane,BorderLayout.CENTER);
	    
	    downPanel.setBorder(new TitledBorder("Output the procedure of the analysing:"));
	    downPanel.setLayout(new BorderLayout());
	    tableModel = new DefaultTableModel();
	    tableModel.setRowCount(13);
	    for(int i = 0; i < tableName.length; i++)
	    {
	    	vectorName.add(tableName[i]);
	    }
	    tableModel.setDataVector(dataVector, vectorName);
	    procedureTable = new JTable(tableModel);
	    
	    downScrollPane = new JScrollPane(procedureTable);
	    downPanel.add(downScrollPane,BorderLayout.CENTER);
	    
	    contentPane.add(centerPanel,BorderLayout.CENTER);
	    frame.setVisible(true);
	    frame.repaint();
	}
	
	void openFile() 
	{
	    grammarArea.setText("");
		BufferedReader in = null;
		JFrame dialogFrame = new JFrame();
		StringBuffer sbtemp = new StringBuffer("");
		String temp = null;
		originalGrammar tempGrammar = new originalGrammar();
		filechooser = new JFileChooser();
		ExampleFileFilter filter = new ExampleFileFilter();
		filter.addExtension("txt");
		filter.setDescription("grammar txt");
		filechooser.setFileFilter(filter);
		int v = filechooser.showOpenDialog(dialogFrame);
		if (v == JFileChooser.APPROVE_OPTION) {
			filePath = filechooser.getSelectedFile().getPath();
			try {
				in = new BufferedReader(new FileReader(filePath));
			} catch (FileNotFoundException e1) {
				JOptionPane.showMessageDialog(frame, "Can not open the file!", "Open File Error", JOptionPane.ERROR_MESSAGE);
			}
			try {
				while ((temp = in.readLine()) != null) {
					tempGrammar = new originalGrammar();
					String tempSource = "";
					tempSource =temp+"\n";
					sbtemp.append(tempSource);
					tempGrammar.left = String.valueOf(temp.charAt(0));
					tempGrammar.right = temp.substring(3);
					oriGrammar.add(tempGrammar);
				  }
			} catch (IOException e2) {
				e2.printStackTrace();
			}
			try {
				in.close();
			} catch (IOException e3) {
				e3.printStackTrace();
			}
			grammarArea.append(sbtemp.toString());		
		}
	}
	
	boolean isElementOf(Vector<String> temp,String test)
	{
		int i = 0;
		boolean flag = false;
		for(i = 0; i < temp.size(); i++)
		{
			if(test.equals(temp.get(i)))
			{
				flag = true;
				break;
			}
		}
		return flag;
	}
	
	//分析出终结符和非终结符
	void analyseTerminalsAndNonterminals()
	{
		String temp;
		char tempChar;
		terminal.add("S'");
		for(int i = 0; i < oriGrammar.size(); i++)
		{
			temp = oriGrammar.get(i).left;
			if(isElementOf(terminal, temp) != true)
			{
				terminal.add(temp);
			}
			for(int j = 0; j < oriGrammar.get(i).right.length(); j++)
			{
				tempChar= oriGrammar.get(i).right.charAt(j);
				temp = String.valueOf(tempChar);
				if((tempChar >= 'A' && tempChar <= 'Z') && (isElementOf(terminal, temp) == false))
				{
					terminal.add(temp);
				}
				else if(isElementOf(terminal, temp) == false)
				{
					if(isElementOf(nonterminal, temp) == false)
					{
						nonterminal.add(temp);
					}
				}
			}
		}
	}
	
	//为文法添加一个圆点构成项目
	void addDotToGrammar()	
	{
		dotGrammar tempDotGrammar = new dotGrammar();
		//先变成拓广文法
		tempDotGrammar.left = "S'";
		tempDotGrammar.right = ".S";	
		dGrammar.add(tempDotGrammar);	
		tempDotGrammar = new dotGrammar();
		tempDotGrammar.left = "S'";
		tempDotGrammar.right = "S.";	
		dGrammar.add(tempDotGrammar);		
		
		for(int i = 0; i < oriGrammar.size(); i++)
		{
			//.在最左边
			tempDotGrammar = new dotGrammar();
			String dotString;
			dotString = "." + oriGrammar.get(i).right;
			tempDotGrammar.left = oriGrammar.get(i).left;
			tempDotGrammar.right = dotString;
			dGrammar.add(tempDotGrammar);
			
			for(int j = 0; j < oriGrammar.get(i).right.length()-1; j++)
			{	
				tempDotGrammar = new dotGrammar();
				tempDotGrammar.left = oriGrammar.get(i).left;
				dotString = oriGrammar.get(i).right.substring(0, j+1)+"."
				+oriGrammar.get(i).right.substring(j+1,oriGrammar.get(i).right.length());
				tempDotGrammar.right = dotString;
				dGrammar.add(tempDotGrammar);
			}
			
			//.在最后
			tempDotGrammar = new dotGrammar();
			tempDotGrammar.left = oriGrammar.get(i).left;
			dotString = oriGrammar.get(i).right + ".";
			tempDotGrammar.right = dotString;
			dGrammar.add(tempDotGrammar);
		}		
	}
	
	HashSet<String> getFirst(String preString)
	{
		char preChar;
		preChar = preString.charAt(0);
		if(preChar >= 'A' && preChar <= 'Z')
		{
			for(int i = 0; i < oriGrammar.size(); i++)
			{
				if(oriGrammar.get(i).left.equals(String.valueOf(preChar)))
				{
					HashSet<String> tFirst = new HashSet<String>();
					tFirst = getFirst(oriGrammar.get(i).right);
					Iterator<String> ir = tFirst.iterator();
					while(ir.hasNext())
					{
						tempFirst.add(ir.next());
					}
				}
			}
		}
		else if(preChar == '$')
		{
			firstIndex++;
			preString= preString.substring(firstIndex);
			HashSet<String> tFirstTwo = new HashSet<String>();
			tFirstTwo = getFirst(preString);
			Iterator<String> ir = tFirstTwo.iterator();
			while(ir.hasNext())
			{
				tempFirst.add(ir.next());
			}
		}
		else
		{
			tempFirst.add(String.valueOf(preChar));
		}

		return tempFirst;
	}
	
	/*  仅用来测试first集是否求解正确用！！！！
	void testFirst()
	{
		firstSet = getFirst("Sag");
		Iterator<String> ir = firstSet.iterator();
		int i = 1;
		while(ir.hasNext())
		{
			System.out.println("i: "+i);
			System.out.println(ir.next());
			i++;
		}
		System.out.println("done");
	}
	*/
	
	void closure(familyGrammar testGrammar,int index)
	{
		familyGrammar tempGrammar = new familyGrammar();
		tempGrammar.index = index;
		tempGrammar.left = testGrammar.left;
		tempGrammar.right = testGrammar.right;
		tempGrammar.end = testGrammar.end;
		countNumber++;
		fGrammar.add(tempGrammar);
		
		char tempChar;	
		int i = 0;
		
		familyGrammar extraGrammar;
		int count = 1;
		int flag = 1;
		while(true)
		{
			for(i = 0;i < tempGrammar.right.length(); i++)
			{
				tempChar = tempGrammar.right.charAt(i);
				if(tempChar == '.')		//先找到'.'在哪
				{
					i++;
					break;
				}
			}
			if(tempGrammar.right.length() > i)
			{
				tempChar = tempGrammar.right.charAt(i);		//找到'.'后面一个字符
				if(tempChar >= 'A' & tempChar <= 'Z')
				{
					String testString;
					if(tempGrammar.right.length() > i+1)
					{
						testString = tempGrammar.right.substring(i+1) + tempGrammar.end;
					}
					else
					{
						testString = tempGrammar.end;
					}
					firstSet.clear();
					firstSet = getFirst(testString);
					for(int j = 0; j < dGrammar.size(); j++)
					{
						if(dGrammar.get(j).left.equals(String.valueOf(tempChar)))
						{
							if(dGrammar.get(j).right.charAt(0) == '.')
							{
								Iterator<String> ir = firstSet.iterator();
								while(ir.hasNext())
								{
									extraGrammar = new familyGrammar();
									extraGrammar.left = String.valueOf(tempChar);
									extraGrammar.index = index;
									extraGrammar.right = dGrammar.get(j).right;
									extraGrammar.end = ir.next();
									
									countNumber++;
									tempGrammar = extraGrammar;
									fGrammar.add(extraGrammar);
									count++;
								}
							}
						}
					}
				}
			}
			
			flag++;
			if(flag - count == 1)
			{
				break;
			}
		}
	}
	
	HashSet<String> getX(int index)			//取得'.'后面的字符
	{
		HashSet<String> tHashSet = new HashSet<String>();
		String xString = "";
		int i = 0;
		int j = 0;
		for(i = 0; i < fGrammar.size(); i++)
		{
			if(fGrammar.get(i).index == index)
			{
				for(j = 0; j < fGrammar.get(i).right.length(); j++)
				{
					if(fGrammar.get(i).right.charAt(j) == '.')
					{
						j++;
						break;
					}
				}
				if(j != fGrammar.get(i).right.length())
				{
					xString = String.valueOf(fGrammar.get(i).right.charAt(j));
					tHashSet.add(xString);
				}
				else
				{
					xString = "";
				}
			}
		}
		return tHashSet;
	}
	
	boolean partOf(String sdot,String base)		//判断base里是否含有sdot
	{
		boolean flag = false;
		int i = 0;
		for(i = 0; i < base.length()-1; i++)
		{
			if(base.substring(i, i+2).equals(sdot))
			{
				flag =true;
			}
		}
		return flag;
	}
	
	String dotMove(String stringdot)
	{
		String test;
		test = stringdot;
		int i = 0;
		for(i = 0; i < test.length(); i++)
		{
			if('.' == test.charAt(i))
			{
				break;
			}
		}
		String tempS ="";
		tempS = test.substring(0, i)+test.charAt(i+1)+'.'+test.substring(i+2);
		return tempS;
	}
	
	void setGoIndex(int indexIn,int newIndexIn)
	{
		int i = 0;
		for(i = 0;i < goTable.size(); i++)
		{
			if(goTable.get(i).index == indexIn) 
			{
				goTable.get(i).index = newIndexIn;
			}
		}
	}
	
	
	void setGoNext(int nextIn,int newNextIn)
	{
		int i = 0;
		for(i = 0;i < goTable.size(); i++)
		{
			if(goTable.get(i).nextIndex == nextIn) 
			{
				goTable.get(i).nextIndex = newNextIn;
			}
		}
	}
	
	
	int getGoNext(int indexIn,String XIn)			//转换函数，得到下一个族的号码
	{
		int next = 0;
		int i = 0;
		for(i = 0; i < goTable.size(); i++)
		{
			if((goTable.get(i).index == indexIn) && (goTable.get(i).X.equals(XIn)) )
			{
				next = goTable.get(i).nextIndex;
				break;
			}
		}
		return next;
	}
	
	
	
	void goNext(String X,int index,int nextIndex)   		//从I(index)中出发，经过一个X，到达I(nextIndex)
	{
		ArrayList<familyGrammar> goGrammar = new ArrayList<familyGrammar>();
		goGrammar = fGrammar;
		String dots = '.'+X;
		for(int i = 0; i < goGrammar.size(); i++)
		{
			if((goGrammar.get(i).index == index) && partOf(dots,goGrammar.get(i).right))
			{
				familyGrammar tempGrammar;
				tempGrammar = new familyGrammar();
				tempGrammar.left = goGrammar.get(i).left;
				tempGrammar.right = dotMove(goGrammar.get(i).right);
				tempGrammar.end = goGrammar.get(i).end;
				tempGrammar.index = nextIndex;
				
				//给goTable填表
				goList tempGo = new goList();
				tempGo.index = index;
				tempGo.X = X;
				tempGo.nextIndex = nextIndex;
				goTable.add(tempGo);
				closure(tempGrammar, nextIndex);
			}
		}
	}
	
	boolean isTheSame(familyGrammar one[],familyGrammar two[])  //判断两个族是否一样
	{
		boolean flag = false;
		boolean tflag = true;
		if(one.length == two.length)
		{
			for(int i = 0; i < one.length; i++)
			{
				flag = false;
				for(int j = 0; j < two.length; j++)
				{
					if((one[i].end.equals(two[j].end))
							&&(one[i].left.equals(two[j].left))
							&&(one[i].right.equals(two[j].right)))
					{
						flag = true;
						break;
					}
				}
				tflag = tflag && flag;
				if(tflag == false)
				{
					break;
				}
			}
		}		
		else
		{
			tflag = false;
		}
		return tflag;
	}
	
	familyGrammar[] getMaxIndexGrammar(familyGrammar one[],familyGrammar two[])
	{
		if(one[0].index > two[0].index)
		{
			return one;
		}
		else
		{
			return two;
		}
	}
	
	void processGoTable()		//把Go表简洁，清楚冗余
	{
		int i = 0;
		int j = 0;
		int toR = 0;
		ArrayList<Integer> toRemove = new ArrayList<Integer>();
		for(i = 0; i < goTable.size()-1;i++)
		{
			for(j = i+1; j < goTable.size();j++)
			{
				if((goTable.get(i).index == goTable.get(j).index)
						&& (goTable.get(i).X.equals(goTable.get(j).X)
								&& (goTable.get(i).nextIndex == goTable.get(j).nextIndex)) )
				{
					toRemove.add(j);
				}
			}
		}
		for(int m = 0; m < toRemove.size(); m++)
		{
			toR = toRemove.get(m)-m;
			goTable.remove(toR);
		}
		/*
		for(int k = 0; k < goTable.size(); k++)
		{
			System.out.println(k+" "+goTable.get(k).index+
					" "+goTable.get(k).X+" "
					+ goTable.get(k).nextIndex);
		}
		*/
	}
	
	void processTedious()		//处理冗余重复的族
	{
		finalNumber = itemNumbers.size();
		int finalIndex = 0;
		boolean isTedious[] = new boolean[finalNumber];
		int sameIndex[] = new int[finalNumber];
		for(int i = 0; i < isTedious.length; i++)
		{
			isTedious[i] = false;
			sameIndex[i] = -1;
		}
		
		for(int i = 0; i < itemNumbers.size()-1; i++)
		{
			for(int j = i+1; j < itemNumbers.size(); j++)
			{
				if(isTheSame(IGrammar[i], IGrammar[j]))
				{
					isTedious[j] = true;
					sameIndex[j] = i;
					finalNumber--;
				}
			}
		}
		finalyGrammar = new familyGrammar[finalNumber][];
		for(int i = 0; i < itemNumbers.size(); i++)
		{
			familyGrammar temp[] = new familyGrammar[itemNumbers.get(i)];
			if(isTedious[i] == false)			//该族不是冗余的，需要加入
			{
				temp = IGrammar[i];
				finalyGrammar[finalIndex] = temp;
				finalIndex++;
			}
			else
			{
				//此族为冗余的族，不应加入
				setGoNext(IGrammar[i][0].index,sameIndex[i]);	//把指向这个族的族的Next指向正确的族
			}
		}
		
		for(int j = 0; j < finalNumber; j++)
		{
			if(finalyGrammar[j][0].index != 0)
			{
				setGoNext(finalyGrammar[j][0].index, j);		//把指向这个族的族的nextIndex都修改一下
				setGoIndex(finalyGrammar[j][0].index, j);		//把这个族的Index修改
				for(int k = 0; k < finalyGrammar[j].length; k++)
				{
					finalyGrammar[j][k].index = j;
				}
			}  
		}
		
		processGoTable();		//清楚Go表中的冗余！！
	}
	

	
	void showFamily()		//把LR(1)项目集规范族显示出来
	{
		int i = 0;
		String familyShow="";
		for(i = 0; i < finalNumber; i++)
		{
			familyShow += "I"+i+":\n";
			for(int j = 0; j < finalyGrammar[i].length; j++)
			{
				familyShow += finalyGrammar[i][j].left+"->"
				+finalyGrammar[i][j].right+","+finalyGrammar[i][j].end+"\n";
			}
			familyShow += "\n";
		}
		familyArea.setText(familyShow);
	}
	
	
	void calFamily()
	{
		int index = 0;
		familyGrammar calGrammar = new familyGrammar();
		calGrammar.left = "S'";
		calGrammar.right = ".S";
		calGrammar.end = "#";
		calGrammar.index = index;
		closure(calGrammar, index);
		itemNumbers.add(countNumber);
		int nextIndex = 1;
		int count = 0;

		while(true)
		{
			HashSet<String> hs = new HashSet<String>();
			hs = getX(index);
			if(hs.size() > 0)
			{
				String X;
				Iterator<String> ir = hs.iterator();
				while(ir.hasNext())
				{
					X = ir.next();
					countNumber = 0;
					goNext(X, index, nextIndex);
					nextIndex++;
					itemNumbers.add(countNumber);
				}
				index++;
			}
			else
			{
				index++;
				count++;
				if(count >= 5)
				{
					break;
				}
			}
		}
		
		int i = 0;
		int newIndex = 0;
		int tempN[] = new int[itemNumbers.size()];
		IGrammar = new familyGrammar[itemNumbers.size()][];
		//先把存储项目集规范组的二维数组初始化
		for(newIndex = 0; newIndex < itemNumbers.size(); newIndex++)
		{
			familyGrammar tG[] = new familyGrammar[itemNumbers.get(newIndex)];
			IGrammar[newIndex]  = tG;	
			tempN[newIndex] = 0;
		}
		//给二维数组填内容
		for(i = 0; i < fGrammar.size();i++)
		{
			IGrammar[fGrammar.get(i).index][tempN[fGrammar.get(i).index]++] = fGrammar.get(i);
		}
	}
	
	int getSentenceNumber(String test)			//返回字符串所在文法的编号
	{
		int i = 0;
		int num = 0;
		int stringLength = 0;
		String temp;
		stringLength  = test.length();
		temp = test.substring(0,stringLength-1);
		for(i = 0; i < oriGrammar.size(); i++)
		{
			if(temp.equals(oriGrammar.get(i).right))
			{
				num = i+1;
				break;
			}
		}
		return num;
	}
	
	void generateAnalyseTable()	//生成分析表
	{
		analyseList = new ArrayList<HashMap>();
		int i = 0;
		boolean isShift = false;
		String key = "";
		String value = "";
		for(i = 0; i < finalNumber; i++)		//这里 i 即为状态号
		{
			isShift = false;
			int count = 0;
			analyseMap = new HashMap();
			for(int j = 0; j < goTable.size(); j++)
			{
				if(i == goTable.get(j).index)
				{
					isShift = true;
					key = goTable.get(j).X;
					count++;
					if(nonterminal.contains(key))
					{
						value = "S"+ goTable.get(j).nextIndex;
					}
					else
					{
						value = String.valueOf(goTable.get(j).nextIndex);
					}
					analyseMap.put(key, value);					
				}
			}
			if(isShift == false)		//若为归约的情况
			{
				for(int index = 0; index < finalyGrammar[i].length; index++)
				{
					key = finalyGrammar[i][index].end;
					if(finalyGrammar[i][index].left.equals("S'"))
					{
						value = "acc";
					}
					else
					{
						value = "r" + String.valueOf(getSentenceNumber(finalyGrammar[i][index].right));
					}
					analyseMap.put(key, value);		
				}
			}
			else if(count != finalyGrammar[i].length)		//部分为归约
			{
				for(int index = 0; index < finalyGrammar[i].length; index++)
				{
					//如果right的最右边为.，则需要归约
					if(finalyGrammar[i][index].right.substring(finalyGrammar[i][index].right.length()-1).equals("."))
					{
						key = finalyGrammar[i][index].end;
						if(finalyGrammar[i][index].left.equals("S'"))
						{
							value = "acc";
						}
						else
						{
							value = "r" + String.valueOf(getSentenceNumber(finalyGrammar[i][index].right));
						}
						analyseMap.put(key, value);		
					}
				}
			}
			analyseList.add(analyseMap);
		}
		
		//测试分析表是否正确
		/*
		int x = 0;
		Iterator iKey;
		Iterator iValue;
		for(x = 0; x < analyseList.size();x++)
		{
			System.out.print("编号："+x+" ");
			iKey = analyseList.get(x).keySet().iterator();
			iValue = analyseList.get(x).values().iterator();
			while(iKey.hasNext())
			{
				System.out.print(iKey.next()+"  ");
				System.out.print(iValue.next()+"  ");
			}
			System.out.println("\n");
		}		
		*/
	}
		
	String processSymbolStatus(String symbol,String left,String right)
	{
		int i  = symbol.length();
		int j = right.length();
		int index = statusArray.size()-1;
		i = i - j;
		symbol = symbol.substring(0, i);
		symbol += left;
		for(int step = j; step >0; step--)
		{
			statusArray.remove(index);
			index--;
		}
		return symbol;
	}
	
	int getStep(String symbol,String right)
	{
		//int i  = symbol.length();
		int j = right.length();
		//i = i - j;
		return j;
	}
	
	
	void finalAnalyse()			//终于写到最终BOSS了，哈哈哈哈哈，对句子进行分析！！！
	{
		sentence = sentenceField.getText()+"#";
		int status = 0;
		int lastStatus = 0;
		int senNum = 0;
		String theOne = "";
		statusArray = new ArrayList<Integer>();
		statusArray.clear();
		statusArray.add(Integer.valueOf(status));

		inputString = sentence;
		while(true)
		{
			row = new Vector<String>();
			row.add(String.valueOf(step));
			row.add(statusStack);
			row.add(symbolStack);
			row.add(inputString);
			theOne = inputString.substring(0, 1);
			actionString = "";
			actionString = (String) analyseList.get(status).get(theOne);
			if(actionString == null)
			{
				actionString = "@.@Error";
			}
			row.add(actionString);
			if(actionString.substring(0,1).equals("S"))	//移进的情况
			{
				gotoString = "";
				row.add(gotoString);
				inputString = inputString.substring(1);
				symbolStack += theOne;
				status = Integer.parseInt(actionString.substring(1));
				statusArray.add(Integer.valueOf(status));
				statusStack = "";
				for(int i = 0; i < statusArray.size(); i++)
				{
					statusStack += " "+statusArray.get(i);
				}
			}
			else if(actionString.substring(0,1).equals("r"))		//归约的情况
			{
				senNum = 0;
				senNum = Integer.parseInt(actionString.substring(1)) - 1;	
				int step = getStep(symbolStack,oriGrammar.get(senNum).right);
				Integer a = statusArray.get(statusArray.size()-step-1);
				lastStatus = a.intValue();
				symbolStack = processSymbolStatus(symbolStack, oriGrammar.get(senNum).left, oriGrammar.get(senNum).right);
				gotoString = (String) analyseList.get(lastStatus).get(oriGrammar.get(senNum).left);
				row.add(gotoString);
				status = Integer.parseInt(gotoString);
				statusStack = "";
				statusArray.add(Integer.valueOf(status));
				for(int i = 0; i < statusArray.size(); i++)
				{
					statusStack += " "+statusArray.get(i);
				}
				
			}
			else if(actionString.substring(0,1).equals("a"))		//成功的情况
			{
				row.add(gotoString);
				resultField.setText("Yes!!!!");
				dataVector.add(row);
				break;
			}
			else					//失败的情况
			{
				row.add(gotoString);
				resultField.setText("Not!!!");
				dataVector.add(row);
				break;
			}
			dataVector.add(row);
			step++;
		}
	    tableModel.setDataVector(dataVector, vectorName);
	    procedureTable = new JTable(tableModel);
	}
	
	void clearAll()
	{
		grammarArea.setText(null);
		resultField.setText("N/A");
		familyArea.setText(null);
		sentenceField.setText(null);
		dataVector.clear();
	    tableModel.setDataVector(dataVector, vectorName);
	    procedureTable = new JTable(tableModel);
	    fGrammar = new ArrayList<familyGrammar>();
	    analyseMap = new HashMap();
	    itemNumbers = new ArrayList<Integer>();	
	    countNumber = 0;	
	    goTable = new ArrayList<goList>();
	    tempFirst = new HashSet<String>();
	    dGrammar = new ArrayList<dotGrammar>();		
	    firstSet = new HashSet<String>();
	    oriGrammar = new ArrayList<originalGrammar>();
		terminal = new Vector<String>();
		nonterminal = new Vector<String>();
	}
	
	class MyAL implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			if(event.getSource() == openFile)
			{
				clearAll();
				openFile();
			}
			else if(event.getSource() == analyse)
			{
				if(sentenceField.getText().isEmpty())
				{
					JOptionPane.showMessageDialog(frame, "Please input sentence ","Please input sentence", JOptionPane.INFORMATION_MESSAGE);					
				}
				else
				{						
					analyseTerminalsAndNonterminals();
					addDotToGrammar();
					//testFirst();    //测试first集是否求解正确
					calFamily();
					processTedious();
					showFamily();
					generateAnalyseTable();
					finalAnalyse();
				}
			}
			else if(event.getSource() == clearAll)
			{
				clearAll();
			}
			else	if(event.getSource() == about)
			{
				String message = "This simple Syntax Analyser is made by Swarm!\n" +
				"       Version: 0.9 \n" +
				"       Student No: 0706550128\n"  +
				"       Student Name: 王恒 \n" +
				"CopyRight (c) Swarm and NUST.All rights reserved!";
		JOptionPane.showMessageDialog(frame, message, "About this", JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}

	
	public static void main(String[] args)
	{
		try
		{
		     UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		Syntax demo = new Syntax();
		demo.go();
	}
	
	JFrame frame;
	
	JButton openFile;
	JButton analyse;
	JButton clearAll;
	JButton about;
	
 	JFileChooser filechooser;
 	String filePath;				//打开文件的file path
	
	JLabel blankLabel;
	JLabel resultLabel;
	JTextField resultField;
	
	JPanel northPanel;			//最上面的JPanel
	JPanel centerPanel;			//中间的JPanel
	JPanel middlePanel;	
	JPanel downPanel;
	JPanel leftPanel;
	JPanel sentencePanel;
	JPanel rightPanel;
	
	JLabel inputGrammar;
	JTextArea grammarArea;
	JLabel inputSentence;
	JTextField sentenceField;
	JTable procedureTable;
	DefaultTableModel tableModel;
	String[] tableName = {"Steps","Status Stack","Symbol Stack","Input String","ACTION","GOTO"};
	Vector<String> row;
	Vector<Vector<String>> dataVector = new Vector<Vector<String>>();
	Vector<String> vectorName = new Vector<String>();
	int rowNumber;
	
	JTextArea familyArea;
	
	JScrollPane grammarScrollPane;
	JScrollPane rightScrollPane;
	JScrollPane downScrollPane;
	
	String sentence;			//用户输入用来判断的句子
	
	MyAL myal;
	
	class originalGrammar
	{
		String left;
		String right;
	}
	
	ArrayList<originalGrammar> oriGrammar = new ArrayList<originalGrammar>();
	
	Vector<String> terminal = new Vector<String>();
	Vector<String> nonterminal = new Vector<String>();
	
	class dotGrammar
	{
		String left;
		String right;
	}
	ArrayList<dotGrammar> dGrammar = new ArrayList<dotGrammar>();
	
	HashSet< String> firstSet = new HashSet<String>();
	int firstIndex = 0;   //仅仅在求first集时用到啊！！！！！！
	HashSet<String> tempFirst = new HashSet<String>();
	
	class familyGrammar
	{
		String left;		
		String right;
		String end;		//项目集规范族的,后边的符号
		int index;			//项目集规范族的编号		
	}
	ArrayList<familyGrammar> fGrammar = new ArrayList<familyGrammar>();  //保存项目集规范族
	
	ArrayList<Integer> itemNumbers = new ArrayList<Integer>();		//存储每个族里有几个元素
	
	class goList
	{
		int index;
		String X;
		int nextIndex;
	}
	
	ArrayList<goList> goTable = new ArrayList<goList>();
	

	int countNumber = 0;			//仅仅在计算族里有几个元素时候用到
	int finalNumber ;				//最后规范族的个数
	
	familyGrammar IGrammar[][];
	familyGrammar finalyGrammar[][];
	
	HashMap analyseMap = new HashMap();
	ArrayList<HashMap> analyseList;
	
	int step = 1;
	String statusStack = " 0";
	ArrayList<Integer> statusArray /*= new ArrayList<Integer>()*/;	
	String symbolStack = "#";
	String inputString = "";
	String actionString = "";
	String gotoString = "";
}