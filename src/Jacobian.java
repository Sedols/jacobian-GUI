import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.net.*;
public class Jacobian extends JFrame implements ActionListener{
	
	
	// field related
	private static final  JPanel p1 = new JPanel(), p2 = new JPanel(), p3 = new JPanel(), space = new JPanel(), spaceLow = new JPanel();
	private static final JPanel score = new JPanel(new GridLayout(2,1));
	private static final UsedCards lowRight = new UsedCards();
	private static PlayerContainer[] cardCont= new PlayerContainer[4];
	private static JPanel cen = new JPanel();
	private static Box box = Box.createVerticalBox(), box2 = Box.createVerticalBox(), box3 =  Box.createVerticalBox() ;
	private static Container contents;

	//protocol related 
	static String protocol, mode, ID = "";
	
	// game related
	private static boolean started = false;
	public static int scoreN = 0, scoreW = 0;
	static int level;
	public static String turnStart, trumph, curTurn, lastTurn = null, lastTurnDum = null, seat,dumHand, cont, vul, playerSeat,hand="";
	static JLabel zone;
	static boolean isDoubled,isRedoubled,notrumph = false;
	
	//constructor
	public Jacobian() throws IOException  {
		setTitle("jacobian");
		contents = getContentPane();
		setLayouts();
		setColors();
		setSize(Helper.wStart,Helper.hStart);
		contents.setSize(Helper.wStart,Helper.hStart);
		space.setPreferredSize(new Dimension(130,100));
		spaceLow.setPreferredSize(new Dimension(200,100));
		addStuff();
		setSizeCards((int)(contents.getWidth() * Helper.wMult),(int)(contents.getHeight() * Helper.hMult/3));
		setSizePanel(contents.getWidth(), contents.getHeight()/3);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setDefaultLookAndFeelDecorated(false);
		setResizable( false );
	} 
	
	//resize all panels
	private void setSizePanel(int w, int h)
	{
	  p1.setSize(new Dimension(w,h));
	  p2.setSize(new Dimension(w,h));
	  p3.setSize(new Dimension(w,h));
	  cen.setPreferredSize(new Dimension((int)(Helper.wC*w),h));
	}
	
	//resize all cards
	private static void setSizeCards(int w, int h)
	{
		for(int i = 0; i < cardCont.length; i++)
			cardCont[i].setPreferredSize(new Dimension(w,h));
		lowRight.setPreferredSize(new Dimension(w - 80,h));
	}
	
	//put default colors
	private void setColors()
	{
		contents.setBackground(Helper.bgColor);
		p1.setBackground(Helper.bgColor);
		p2.setBackground(Helper.bgColor);
		p3.setBackground(Helper.bgColor);
		cen.setBackground(Helper.bgColor);
		space.setBackground(Helper.bgColor);
		spaceLow.setBackground(Helper.bgColor);
	}
	
	// set layouts
	private void setLayouts()
	{
		contents.setLayout(new GridLayout(3,1));
		p1.setLayout(new FlowLayout(FlowLayout.LEFT));
		p2.setLayout(new FlowLayout(FlowLayout.LEFT));
		p3.setLayout(new FlowLayout(FlowLayout.LEFT));
	}
	
	// adds everything on the field
	private void addStuff() throws IOException
	{
		contents.add(p1);
		contents.add(p2);
		contents.add(p3);
		// constructs players
		for(int i = 0; i < cardCont.length; i++)
			cardCont[i] = new PlayerContainer(new GUIPlayer(this));
		score.setBackground(new Color(51,0,153));
		JLabel scoreNS = new JLabel(" NS:  " + scoreN + "   "), scoreEW = new JLabel(" EW: " + scoreW + "    ");
		scoreNS.setForeground(Color.white);
		scoreEW.setForeground(Color.white);
		score.add(scoreNS);
		score.add(scoreEW);
		URL url = Jacobian.class.getResource("images/None.png"); 
	        zone = new JLabel(new ImageIcon(url));
	    	zone.setBorder(BorderFactory.createLineBorder(Color.black));
		p1.add(zone);
		p1.add(space);
		p1.add(cardCont[0]);
		p2.add(cardCont[3]);
		p2.add(cen);
		p2.add(cardCont[1]);
		box.add(Box.createVerticalStrut((int)(contents.getHeight()*(1-Helper.hMult)/3 - 20)));
		box2.add(Box.createVerticalStrut((int)(contents.getHeight()*(1-Helper.hMult)/3 + 100)));
		box3.add(Box.createVerticalStrut((int)(contents.getHeight()*(1-Helper.hMult)/3 - 20)));
		box.add(cardCont[2]);
		box2.add(score);
		box3.add(lowRight);
		p3.add(box2);
		int w = getWidth();
		spaceLow.setPreferredSize(new Dimension((w - (int)(w * Helper.wMult))/2 - 60,100));
	    p3.add(spaceLow);
		p3.add(box);
		p3.add(box3);
		//addComponentListener(this);
	}
	private static boolean first = false;
	private static void makeMove(String seat, String card)
	{
		JPanel newPanel = null;
		for(int i = 0; i < 4; i++)
		{
			if(cardCont[i].getSeat().equals(seat))
			{
				if(cardCont[i].isDummy() || i == 2)
				{
					newPanel = cardCont[i].doItUser(card, cen.getWidth(), cen.getHeight());
					if(newPanel != null)
					{
						if(i == 2)
						{
							lastTurn = card;
							if(isWaiting)
							{
								isWaiting = false;
								System.out.println("play " + lastTurn);
								lastTurn = null;
							}
						}
						else
						{
							if(cardCont[0].isDummy())
							{
								lastTurnDum = card;
								if(isWaitingDum)
								{
									isWaitingDum = false;
									System.out.println("play " + lastTurnDum);
									lastTurnDum = null;
								}
							}
						}
					}
				}
				else
					newPanel = cardCont[i].doIt(card, cen.getWidth(), cen.getHeight());
			}
		}
		if (newPanel != null)
		{
			/*
			if(!first)
			for(int i = 0; i < 4; i++)
				if(cardCont[i].isDummy())
				{
					cardCont[i].setHand(dumHand);
					cardCont[i].reveal();
					first = true;
				}*/	
			started = true;
			p2.remove(cen);
			cen = newPanel;
			cen.updateUI();
			p2.remove(cardCont[3]);
			p2.remove(cardCont[1]);
			if(cardCont[3].isDummy())
				cardCont[3].addAgain();
			p2.add(cardCont[3]);
			p2.add(cen);
			if(cardCont[1].isDummy())
				cardCont[1].addAgain();
			p2.add(cardCont[1]);
			p2.updateUI();
			cen.updateUI();
			if(Helper.history.size() > 3)
			{			
				Helper.eval();
				for(int i = 0; i < cardCont.length; i++)
					if(cardCont[i].getSeat().equals(turnStart))
					{
						cardCont[i].setTurn();
						break;
					}
				lowRight.setPreferredSize(new Dimension(cardCont[2].getWidth()- 80,cardCont[2].getHeight()));
				lowRight.setContract(cont);
			}
			else 
			{
				int k = 0;
				while(!Helper.seats[k].equals(turnStart)) k++;
				for(int i = 0; i < cardCont.length; i++)
					if(cardCont[i].getSeat().equals(Helper.seats[(k + Helper.history.size()) % 4]))
						cardCont[i].setTurn();
					else
						cardCont[i].removeTurn();
			}
			cardCont[2].addAgain();
			for(int i = 0; i < 4; i++)
				cardCont[i].updateUI();
		}
	}
	// if a button is clicked
	public void actionPerformed(ActionEvent e) {
		GUICard card = (GUICard) e.getSource();

		if(cardCont[2].has(card) && cardCont[2].isTurn())
		{
			makeMove(cardCont[2].getSeat(), card.getName());
		}
		else
		{
			if(cardCont[0].has(card) && cardCont[0].isTurn() && cardCont[0].isDummy())
			{
				makeMove(cardCont[0].getSeat(), card.getName());
			}
		}
	} 
	
	private static int number(String s)
	{
		if(s.startsWith("seat ")) return 0;
		if(s.startsWith("vul ")) return 1;
		if(s.startsWith("hand ")) return 2;
		if(s.startsWith("contract ")) return 3;
		if(s.equals("go")) return 4;
		if(s.startsWith("dummy ")) return 5;
		if(s.startsWith("show ")) return 6;
		if(s.startsWith("go")) return 7;
		return -1;
	}
	
	private static String next(String seat, int k)
	{
		int i = 0;
		while(!Helper.seats[i].equals(seat)) i++;
		return Helper.seats[(i + k)%4];
	}
	private static boolean isWaiting = false,isWaitingDum = false;
	private static void process(String command)
	{
	
		if(protocol == null)
		{
			protocol = command;
			System.out.println();
			return;
		}
		if(command.startsWith("id"))
		{
			System.out.println("id " + ID);
			return;
		}
		if(protocol.equals("jacobian"))
		{
			if(command.equals("new") || command.equals("newplay") || command.equals("newbidding"))
			{
				mode = command;
				System.out.println();
				return;
			}
			if(mode.equals("newplay"))
			{
				int i;
				switch(number(command))
				{
					
					case 0:
						playerSeat = command.substring(5, 6);
						cardCont[2].setSeat(playerSeat );
						Helper.map.put("S", playerSeat );
						cardCont[3].setSeat(next(playerSeat ,1));
						Helper.map.put("W",next(playerSeat ,1));
						cardCont[0].setSeat(next(playerSeat ,2));
						Helper.map.put("N",next(playerSeat ,2));
						cardCont[1].setSeat(next(playerSeat,3));
						Helper.map.put("E",next(playerSeat,3));
						System.out.println();
						break;
					case 1:
						vul = command.substring(4, command.length());
						URL url;
						if(vul.equals("None") || vul.equals("All") || (vul.equals("NS") && vul.contains(playerSeat)))
							url = Jacobian.class.getResource("images/" + vul + ".png"); 
						else
						{
							if(vul.equals("NS")) url =Jacobian.class.getResource("images/EW.png"); 
							else  url =Jacobian.class.getResource("images/NS.png");
						}
						zone.setIcon(new ImageIcon(url));
						System.out.println();
						break;
					case 2:
						hand = command.substring(5, command.length());
						System.out.println();
						break;
					case 3:
						cont = command.substring(9, command.length());
						trumph = cont.charAt(1) + "";
						if(trumph.equals("N"))
						{
							notrumph = true;
							trumph = "S";
						}
						cardCont[2].setContract(cont);
						lowRight.setContract(cont);
						if(cont.length() == 3)
							isDoubled = false;
							isRedoubled = false;
						if(cont.length() == 4)
							isDoubled = true;
							isRedoubled = false;
						if(cont.length() == 5)
							isDoubled = false;
							isRedoubled = true;
						level = cont.charAt(0) - '0';
						seat = cont.substring(cont.length()-1);
						playerSeat = cardCont[2].getSeat();
						cardCont[2].setName(ID);
						cardCont[2].setHand(hand);
						cardCont[2].updateUI();
						cardCont[2].reveal();
						i = 0; 
						while(!Helper.seats[i].equals(seat)) i++;
						turnStart = Helper.seats[(i + 1)%4];
						for(int j = 0;j < 4; j++)
						{
						  if(cardCont[j].getSeat().equals(turnStart)) cardCont[j].setTurn();
						  else cardCont[j].removeTurn();
						}
						if(!cardCont[2].isDummy()) cardCont[2].enableButtons();
						cardCont[2].updateUI();
						i = 0; 
						while(!cardCont[i].getSeat().equals(seat)) i++;
						cardCont[(i + 2)%4].setDummy();
						if(cardCont[2].isDummy()) dumHand = hand;
						System.out.println();
						break;
					case 4:
						if(lastTurn != null)
						{
							System.out.println("play " + lastTurn);
							lastTurn = null;
							isWaiting = false;
						}
						else
						{
							isWaiting = true;
						}
						break;
					case 5:
						dumHand = command.substring(6, command.length());
						i = 0; 
						while(!Helper.seats[i].equals(cardCont[2].getSeat())) i++;
						for(int j = 0; j < 4; j++)
						{
							if(cardCont[j].getSeat().equals(Helper.opposite[i]) && cardCont[j].isDummy()) cardCont[j].enableButtons();
							cardCont[j].updateUI();
						}
						for(int k = 0; k < 4; k++)
						{
							if(cardCont[k].isDummy())
							{
								cardCont[k].setHand(dumHand);
								cardCont[k].reveal();
								break;
							}
						}
						System.out.println();
						break;
					case 6:
						String show = command.substring(5, command.length());	
						String startSeat = show.charAt(0) + "";
						if(startSeat.equals(turnStart)) 
						{
							curTurn = show.charAt(2) + "";
							if(notrumph)
								trumph = curTurn;
						}
						for(int k = 0; k < 4; k++)
						{
							if(cardCont[k].getSeat().equals(startSeat))
							{
								cardCont[k].setTurn();
							}
							else
								cardCont[k].removeTurn();
						}
						makeMove(startSeat, show.substring(2));
						System.out.println();
						break;
					case 7:
						if(lastTurnDum != null)
						{
							System.out.println("play " + lastTurnDum);
							lastTurnDum = null;
							isWaitingDum = false;
						}
						else
						{
							isWaitingDum = true;
						}
						break;
					default: break;
				}
			}
		}
	}
	static Jacobian jacobian;
	public static void main(String [] args) throws IOException  {
	  jacobian = new Jacobian();
	  jacobian.setVisible(true);
	  String response = JOptionPane.showInputDialog(null,
			  "What is your name?",
			  "Enter your name",
			  JOptionPane.QUESTION_MESSAGE);
	  ID = response;
	  cardCont[2].setName(ID);
	  BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	  String str = "";
	  while (str != null) {
	       str = in.readLine();
	       try {
	    	   process(str);
	       }
	       catch(Exception e)
	       {
	    	System.out.println("Sorry mate, wrong protocol: " + e);   
	       }
	  }
	}
} // class Jacobian
