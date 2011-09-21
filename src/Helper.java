import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;
import java.net.*;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.util.*;

public class Helper {
	public static final Color bgColor = new Color(0,102,0);
	public static final double wMult = 0.35, hMult = 0.6, wC = 0.270, ratio = 56/88.;
	public static final int wStart = 1024, hStart = 740, wMax = 1650, hMax = 1040;
	public static final String[] opposite = { "S", "W", "N", "E"}, suits = { "C", "D", "H", "S"}, seats = {"N", "E", "S", "W"};
	public static String[] suitsDis = new String[4];
	public static final String[] order = { "2", "3", "4", "5","6" ,"7" , "8", "9","T" , "J", "Q","K","A"};
	public static int number = -1;
	public static ArrayList<GUICard> history = new ArrayList<GUICard>();
	public static HashMap<String, String> map = new HashMap<String,String>();
	public static Dimension dim;
	static BufferedImage resize(BufferedImage image, int width, int height) {
		int type = image.getType() == 0? BufferedImage.TYPE_INT_ARGB : image.getType();
		BufferedImage resizedImage = new BufferedImage(width, height, type);
		Graphics2D g = resizedImage.createGraphics();
		g.setComposite(AlphaComposite.Src);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
		RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.setRenderingHint(RenderingHints.KEY_RENDERING,
		RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		RenderingHints.VALUE_ANTIALIAS_ON);
		g.drawImage(image, 0, 0, width, height, null);
		g.dispose();
		return resizedImage;
    }
	public static void setNextTurn(int i)
	{
		int k = 0;
		while(!seats[k].equals(Jacobian.turnStart)) k++;
		k += i;
		k %= 4;
		Jacobian.turnStart = seats[k];
		if(seats[k].equals("W") || seats[k].equals("E"))
			Jacobian.scoreW++;
		else 
			Jacobian.scoreN++;
		if(number == 12)
		{
			String overs = "";
			int sc = 0;
			if(Jacobian.seat.equals("S") || Jacobian.seat.equals("N"))
				sc = Jacobian.scoreN - Jacobian.level;
			else sc = Jacobian.scoreW - Jacobian.level;
			sc -= 6;
			if(sc == 0) overs = "=";
			if(sc < 0) overs = "" + sc;
			if(sc > 0) overs = "+" + sc;
			int finalScore = calcScore(Jacobian.cont.charAt(2) + "", Jacobian.level, Jacobian.cont.substring(Jacobian.cont.length()-1), Jacobian.vul, sc);
			int j = 0;
			while(!seats[j].equals(Jacobian.seat)) j++;
			if(!(Jacobian.playerSeat.equals(seats[j]) || Jacobian.playerSeat.equals(opposite[j])))
				finalScore *= -1;
			JOptionPane.showMessageDialog(Jacobian.jacobian, "Game Over! " + Jacobian.cont.substring(0,Jacobian.cont.length()-1) + overs + " You scored: " + finalScore);
		}
	}
	private static int calcScore(String trumph, int level, String seat, String vul, int overs)
	{
		int score = 0;
		if(overs >= 0)
		{
			if(trumph.equals("N"))
			{
				if(Jacobian.isRedoubled)
				{
					if(vul.equals("All") || vul.contains(seat))
					{
						score += 4*(10 + level*30);
						score += overs*400;
						if(level >= 3 || 4*(10 + level*30) >= 100 ) score += 500;
						else score += 50;
						if(level >= 6) score += 750;
						if(level >= 7) score += 1500;
					}
					else
					{
						score += 4*(10 + level*30);
						score += overs*200;
						if(level >= 3 || 4*(10 + level*30) >= 100) score += 300;
						else score += 50;
						if(level >= 6) score += 500;
						if(level >= 7) score += 1000;
					}
				}
				else
				{
					if(Jacobian.isDoubled)
					{
						if(vul.equals("All") || vul.contains(seat))
						{
							score += 2*(10 + level*30);
							score += overs*200;
							if(level >= 3 || 2*(10 + level*30) >= 100) score += 500;
							else score += 50;
							if(level >= 6) score += 750;
							if(level >= 7) score += 1500;
						}
						else
						{
							score += 2*(10 + level*30);
							score += overs*100;
							if(level >= 3 || 2*(10 + level*30)>= 100) score += 300;
							else score += 50;
							if(level >= 6) score += 500;
							if(level >= 7) score += 1000;
						}
					}
					else
					{
						if(vul.equals("All") || vul.contains(seat))
						{
							score += 10 + (level + overs)*30;
							if(level >= 3 || 10 + (level)*30 >= 100 )score += 500;
							else score += 50;
							if(level >= 6) score += 750;
							if(level >= 7) score += 1500;
						}
						else
						{
							score += 10 + (level + overs)*30 ;
							if(level >= 3 ||  10 + (level)*30 >= 100) score += 300;
							else score += 50;
							if(level >= 6) score += 500;
							if(level >= 7) score += 1000;
						}
					}
				}
			}
			else
			{
				if(trumph.equals("S") || trumph.equals("H"))
				{
					if(Jacobian.isRedoubled)
					{
						if(vul.equals("All") || vul.contains(seat))
						{
							score += 4*(level*30);
							score += overs*400;
							if(level >= 4 || 4*(level*30) >= 100)score += 500;
							else score += 50;
							if(level >= 6) score += 750;
							if(level >= 7) score += 1500;
						}
						else
						{
							score += 4*(level*30);
							score += overs*200;
							if(level >= 4 || 4*(level*30) >= 100) score += 300;
							else score += 50;
							if(level >= 6) score += 500;
							if(level >= 7) score += 1000;
						}
					}
					else
					{
						if(Jacobian.isDoubled)
						{
							if(vul.equals("All") || vul.contains(seat))
							{
								score += 2*(level*30);
								score += overs*200;
								if(level >= 4 || 2*(level*30) >= 100) score += 500;
								else score += 50;
								if(level >= 6) score += 750;
								if(level >= 7) score += 1500;
							}
							else
							{
								score += 2*(level*30) ;
								score += overs*100;
								if(level >= 4 || 2*(level*30) >= 100) score += 300;
								else score += 50;
								if(level >= 6) score += 500;
								if(level >= 7) score += 1000;
							}
						}
						else
						{
							if(vul.equals("All") || vul.contains(seat))
							{
								score += (level + overs)*30;
								if(level >= 4 || level*30 >= 100) score += 500;
								else score += 50;
								if(level >= 6) score += 750;
								if(level >= 7) score += 1500;
							}
							else
							{
								score += (level + overs)*30;
								if(level >= 4 || level*30 >= 100) score += 300;
								else score += 50;
								if(level >= 6) score += 500;
								if(level >= 7) score += 1000;
							}
						}
					}
				}
				else
				{
					if(Jacobian.isRedoubled)
					{
						if(vul.equals("All") || vul.contains(seat))
						{
							score += 4*(level*20);
							score += overs*400;
							if(level >= 5 || 4*(level*20) >= 100) score += 500;
							else score += 50;
							if(level >= 6) score += 750;
							if(level >= 7) score += 1500;
						}
						else
						{
							score += 4*(level*20);
							score += overs*200;
							if(level >= 5 || 4*(level*20) >= 100) score += 300;
							else score += 50;
							if(level >= 6) score += 500;
							if(level >= 7) score += 1000;
						}
					}
					else
					{
						if(Jacobian.isDoubled)
						{
							if(vul.equals("All") || vul.contains(seat))
							{
								score += 2*(level*20);
								score += overs*200;
								if(level >= 5 || 2*(level*20) >= 100) score += 500;
								else score += 50;
								if(level >= 6) score += 750;
								if(level >= 7) score += 1500;
							}
							else
							{
								score += 2*(level*20);
								score += overs*100;
								if(level >= 5 || 2*(level*20) >= 100) score += 300;
								else score += 50;
								if(level >= 6) score += 500;
								if(level >= 7) score += 1000;
							}
						}
						else
						{
							if(vul.equals("All") || vul.contains(seat))
							{
								score += (level + overs)*20;
								if(level >= 5 || (level)*20 >= 100) score += 500;
								else score += 50;
								if(level >= 6) score += 750;
								if(level >= 7) score += 1500;
							}
							else
							{
								score += (level + overs)*20;
								if(level >= 5 || (level)*20 >= 100) score += 300;
								else score += 50;
								if(level >= 6) score += 500;
								if(level >= 7) score += 1000;
							}
						}
					}
					
					
				}
			}
			if(Jacobian.isDoubled) score += 50;
			if(Jacobian.isRedoubled) score += 100;
			return score;
		}
		else 
		{
			if(vul.equals("All") || vul.contains(seat))
			{
				if(Jacobian.isRedoubled)
				{
					if (overs <= -1) score -= 400;
					score -= 600 *( -overs - 1);
				}
				else
				{
					if(Jacobian.isDoubled)
					{
						if (overs <= -1) score -= 200;
						score -= 300 *( -overs - 1);
					}
					else 
					{
						score -= 100*(-overs);
					}	
				}
			}
			else
			{
				if(Jacobian.isRedoubled)
				{
					if(overs <= -1) score -= 200;
					if(overs <= -2) score -= 400;
					if(overs <= -3) score -= 400;
					if(overs <= -4)
						score -= 600 *( -overs - 3);
				}
				else
				{
					if(Jacobian.isDoubled)
					{
						if(overs <= -1) score -= 100;
						if(overs <= -2) score -= 200;
						if(overs <= -3) score -= 200;
						if(overs <= -4)
							score -= 300 *( -overs - 3);
					}
					else 
					{
						score -= 50*(-overs);
					}	
				}
			}
			return score;
		}
	}
	private static int compare(GUICard o, GUICard t)
	{
		if(o.getSuit().equals(Jacobian.trumph) && !t.getSuit().equals(Jacobian.trumph))
			return 1;
		if(t.getSuit().equals(Jacobian.trumph) && !o.getSuit().equals(Jacobian.trumph))
			return -1;
		int ot = 0, th = 0;
		if(t.getSuit().equals(o.getSuit()))
		{
			for(int i = 0; i < 13; i++)
			{
				if(t.getCard().equals(Helper.order[i]))
					th = i;
				if(o.getCard().equals(Helper.order[i]))
					ot = i;
			}
			return ot - th;
		}	
		if(o.getSuit().equals(Jacobian.curTurn)) return 1;
		return -1;
	}

	public static void eval()
	{
		int maxi = 0;
		for(int i = 1; i < 4; i++ )
			if(compare(history.get(maxi), history.get(i)) < 0)
				maxi = i;
		history.clear();
		number++;
		setNextTurn(maxi);
	}
	public static int[] permutateX(int x[])
	{
		if(Jacobian.turnStart == null) Jacobian.turnStart = "N";
		if(Jacobian.turnStart.equals(map.get("N"))) return x;
		int[] n = new int[4];
		if(Jacobian.turnStart.equals(map.get("E"))){
			n[0] = x[1];
			n[1] = x[2];
			n[2] = x[3];
			n[3] = x[0];
			return n;
		}
		if(Jacobian.turnStart.equals(map.get("S"))){
			n[0] = x[2];
			n[1] = x[3];
			n[2] = x[0];
			n[3] = x[1];
			return n;
		}
		if(Jacobian.turnStart.equals(map.get("W"))){
			n[0] = x[3];
			n[1] = x[0];
			n[2] = x[1];
			n[3] = x[2];
			return n;
		}
		return x;
	}
    public static int[] permutateY(int y[])
    {
    	if(Jacobian.turnStart == null) Jacobian.turnStart = "N";
		if(Jacobian.turnStart.equals(map.get("N"))) return y;
		int[] n = new int[4];
		if(Jacobian.turnStart.equals(map.get("E"))){
			n[0] = y[1];
			n[1] = y[2];
			n[2] = y[3];
			n[3] = y[0];
			return n;
		}
		if(Jacobian.turnStart.equals(map.get("S"))){
			n[0] = y[2];
			n[1] = y[3];
			n[2] = y[0];
			n[3] = y[1];
			return n;
		}
		if(Jacobian.turnStart.equals(map.get("W"))){
			n[0] = y[3];
			n[1] = y[0];
			n[2] = y[1];
			n[3] = y[2];
			return n;
		}
		return y;
    }
	public static JPanel drawPanel(int h, int panelW, int panelH)
	{
		BufferedImage[] images;
		if(history.size() != 0)
			images= new BufferedImage[Helper.history.size()];
		else 
			images = new BufferedImage[4];
			
		try
		{

			for(int j = 0; j < images.length; j++) {
				URL url = Helper.class.getResource("images/" + Helper.history.get(j).getName()  + ".jpg"); 
				images[j] = Helper.resize(javax.imageio.ImageIO.read(url), (int) (ratio * h),h);
			}
		}
		catch(Exception e){System.err.print(e);}
    	JPanel panel = new JPanel(null);
		Dimension d = new Dimension(panelW, panelH);
	    panel.setPreferredSize(d);
	    int iw = images[0].getWidth();
	    int ih = images[0].getHeight();
	    int overlap = iw/4;
        int x0 = (panelW - iw)/2;
        JLabel[] labels = new JLabel[4];
        for(int j = 0; j < images.length; j++)
        {
        	labels[j] = new JLabel(new ImageIcon(images[j]));
        	labels[j].setBorder(BorderFactory.createLineBorder(Color.black));
        }
        int[] xn = {x0,x0 + 2*overlap,x0,x0 - 2*overlap};
        int[] yn = {10,(panelH - ih)/3 + 10,(panelH - ih)/3 * 2 + 10,(panelH - ih)/3 + 10};
        xn = permutateX(xn);
        yn = permutateY(yn);
	    for(int j = 0 ; j < images.length; j++)
	    {
	    	panel.add(labels[j]);
            labels[j].setBounds(xn[j], yn[j], iw, ih);
	    	panel.setComponentZOrder(labels[j], 0);
	    }
	    panel.setBackground(Helper.bgColor);
		return panel;
	}
}
