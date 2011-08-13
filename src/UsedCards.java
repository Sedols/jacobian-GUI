import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class UsedCards extends JPanel{
	
	JButton[] cards = new JButton[13];
	JLabel bottom;
	public UsedCards()
	{
		bottom = new JLabel();
		setBackground(Helper.bgColor);
		setBorder( BorderFactory.createEmptyBorder( 0, 20, 0, 0));
		setLayout(new GridBagLayout());
		bottom.setForeground(Color.white);
		GridBagConstraints c = new GridBagConstraints();
	    c.fill = GridBagConstraints.HORIZONTAL;
	    c.gridy = 0;
		for(int i = 0; i < cards.length; i++)
		{
			cards[i] = new JButton();
			cards[i].setBackground(Helper.bgColor);
			cards[i].setForeground(Helper.bgColor);
			cards[i].setBorder(BorderFactory.createEmptyBorder());
			cards[i].setFocusable(false);
			c.gridx = i;
			add(cards[i],c);
		}
		JPanel  p1 = new JPanel();
		p1.setBackground(new Color(51,0,153));
		p1.add(bottom);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 13;
		c.gridy = 1;
		c.gridx = 0;
		add(p1,c);
	}
	private String suit(char c)
	{
		if(c == 'S') return "\u2660";
		if(c == 'H') return "\u2665";
		if(c == 'D') return "\u2666";
		if(c == 'C') return "\u2663";
		return "NT";
	}
	public void setContract(String contract)
	{
		bottom.setText(contract.charAt(0) + suit(contract.charAt(1)) + contract.substring(2,contract.length()-1) + " " + contract.charAt(contract.length()-1) + "    NS: " + Jacobian.scoreN + "    EW: " + Jacobian.scoreW);
	}
	public void setPreferredSize(Dimension d)
	{
		super.setPreferredSize(d);
		for(int i = 0; i < cards.length; i++)
		{
			if(i > Helper.number)
			{
				cards[i].setPreferredSize(new Dimension((d.width)/19, d.height - 30));
			}
			if (i == Helper.number)
			{
				cards[i].setPreferredSize(new Dimension((d.width)/19*6, d.height - 30));
				try {
					URL url = Helper.class.getResource("images/backBig.png");
					cards[i].setIcon(new ImageIcon(Helper.resize(javax.imageio.ImageIO.read(url), (d.width)/19*6, d.height - 30)));
					cards[i].setBorder(BorderFactory.createLineBorder(Color.black));
				} 
				catch (IOException e) {e.printStackTrace();}
				
			}
			if(i < Helper.number)
			{
				cards[i].setPreferredSize(new Dimension((d.width)/19, d.height - 30));
				try {
					URL url = Helper.class.getResource("images/backS.png");
					cards[i].setIcon(new ImageIcon(Helper.resize(javax.imageio.ImageIO.read(url), (d.width)/19, d.height - 30)));
				} 
				catch (IOException e) {e.printStackTrace();}
				
			}
		}
	}
}
