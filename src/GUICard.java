import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.*;

public class GUICard extends JButton implements Comparable<GUICard> {
	String name;
	Icon ic;
	public GUICard(Jacobian jc)
	{
		setFocusable(false);
		setBackground(Color.white);
		addActionListener(jc);
		setBorder(BorderFactory.createLineBorder(Color.black));
		setEnabled(false);
	}
	public GUICard(String name)
	{
		setFocusable(false);
		setBackground(Color.white);
		this.name = name;
	}
	public void setCard(String card)
	{
		name = card;
	}
	private String suit(char c)
	{
		if(c == 'S') return "\u2660";
		if(c == 'H') return "\u2665";
		if(c == 'D') return "\u2666";
		if(c == 'C') return "\u2663";
		return "";
	}
	private String first(char c)
	{
		if(c == 'T') return "10";
		if (c == 'A' || c == 'K' || c == 'Q')
			return ""+name.charAt(1);
		else
			return  "&nbsp;" + name.charAt(1);

	}
	public void showCard() {
		setSize(Helper.dim);
		this.setIcon(null);
		this.setDisabledIcon(null);
		this.setText("<html>" + "<br>" +  "<p>" + first(name.charAt(1))  + "</p>"  + "<p>" +  suit(name.charAt(0)) + "</p></html>");
		if(name.charAt(0) == 'S' || name.charAt(0) == 'C')
		this.setForeground(Color.black);
		else 
			this.setForeground(new Color(171,7,5));
		this.setHorizontalAlignment(SwingConstants.CENTER);
		this.setVerticalAlignment(SwingConstants.TOP);
		this.setVerticalTextPosition(SwingConstants.TOP);
		this.setHorizontalTextPosition(SwingConstants.CENTER);
		this.setFont(new Font("Serif", Font.BOLD, 16));
		updateUI();
	}
	public void setSize(Dimension d)
	{
		Helper.dim = d;
		setPreferredSize(new Dimension((int)d.getWidth()/16,(int)d.getHeight() - 30));
		try {
			URL url = Helper.class.getResource("images/backS.png"); 
			setIcon(new ImageIcon(Helper.resize(javax.imageio.ImageIO.read(url), (int)d.getWidth()/16, (int)d.getHeight() - 30)));
			setDisabledIcon(new ImageIcon(Helper.resize(javax.imageio.ImageIO.read(url), (int)d.getWidth()/16, (int)d.getHeight() - 30)));
		} 
		catch (IOException e) {System.out.println(e);}
	}
	public void setSizeBig(Dimension d) {
		setPreferredSize(new Dimension((int)(d.getWidth()/16 * 4),(int)d.getHeight() - 30));
		try {
			URL url = Helper.class.getResource("images/backBig.png");
			setIcon(new ImageIcon(Helper.resize(javax.imageio.ImageIO.read(url), (int)(d.getWidth()/16 * 4), (int)d.getHeight() - 30)));
			setDisabledIcon(new ImageIcon(Helper.resize(javax.imageio.ImageIO.read(url), (int)(d.getWidth()/16 * 4), (int)d.getHeight() - 30)));
		} 
		catch (IOException e) {System.out.println(e);}
	}
 
	public String getSuit()
	{
		return "" +  (char)name.charAt(0);
	}
	public String getCard()
	{
		return "" +  (char)name.charAt(1);
	}
	public String getName() { return name; }
	public int compareTo(GUICard o)
	{
		if(o.name.equals(name)) return 0;
		if(o.name.equals("X")) return -1;
		if(name.equals("X")) return 1;
		if(Jacobian.trumph != null)
		{
		if(o.getSuit().equals(Jacobian.trumph) && !this.getSuit().equals(Jacobian.trumph))
			return 1;
		if(this.getSuit().equals(Jacobian.trumph) && !o.getSuit().equals(Jacobian.trumph))
			return -1;
		int ot = 0, th = 0;
		if(this.getSuit().equals(o.getSuit()))
		{
			for(int i = 0; i < 13; i++)
			{
				if(this.getCard().equals(Helper.order[i]))
					th = i;
				if(o.getCard().equals(Helper.order[i]))
					ot = i;
			}
			return (ot - th);
		}	
		return compare(this.getSuit(), o.getSuit());
		}
		return 0;
	}
	private static int compare(String a, String b)
	{
		int t = 0, o = 0;
		for(int i = 0; i < 4; i++)
		{
			if(a.equals(Helper.suitsDis[i]))
				t = i;
			if(b.equals(Helper.suitsDis[i]))
				o = i;
		}
		return o - t;
	}
	public void become(GUICard o) {
		name = o.getName();
		setText(o.getText());
		ic = o.ic;
		this.setPreferredSize(new Dimension(o.getWidth(), o.getHeight()));
		setIcon(o.getIcon());
		setDisabledIcon(o.getDisabledIcon());
		this.setForeground(o.getForeground());
	}
}
