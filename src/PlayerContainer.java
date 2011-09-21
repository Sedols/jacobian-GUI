import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;

import javax.swing.*;
import java.awt.*;

public class PlayerContainer extends JPanel{
	private static final Color bgColor = new Color(0,102,0);
	private GUIPlayer player;
	JLabel nameLabel; 
	String name;
	public PlayerContainer(GUIPlayer p)
	{	
		player = p;
		setBackground(bgColor);
		setLayout(new GridBagLayout());
		addCards(p.getHand());
		name = "Gib";
		nameLabel = new JLabel();
		nameLabel.setForeground(Color.white);
		JPanel  p1 = new JPanel();
		p1.setBackground(new Color(51,0,153));
		p1.add(nameLabel);
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 13;
		c.gridy = 1;
		c.gridx = 0;
		add(p1,c);
	}
	public void setSeat(String s)
	{
		setLabel(s);
		player.setSeat(s);
	}
	public void setHand(String hand)
	{
		player.setHand(hand);
		removeAll();
		addCards(player.getHand());
		JPanel  p1 = new JPanel();
		p1.setBackground(new Color(51,0,153));
		p1.add(nameLabel);
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 13;
		c.gridy = 1;
		c.gridx = 0;
		add(p1,c);
		add(p1,c);
		updateUI();
	}
	public void addAgain() {
		removeAll();
		addCards(player.getHand());
		
		JPanel  p1 = new JPanel();
		p1.setBackground(new Color(51,0,153));
		p1.add(nameLabel);
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 13;
		c.gridy = 1;
		c.gridx = 0;
		add(p1,c);
		add(p1,c);
		updateUI();
	}
	private void addCards(GUICard[] cards)
	{
		GridBagConstraints c = new GridBagConstraints();
	    c.fill = GridBagConstraints.HORIZONTAL;
	    c.gridy = 0;
	    int k = 0;
		for(int i = 0; i < cards.length; i++)
		{
			if(cards[i] != null)
			{
				c.gridx = k;
				add(cards[i],c);
				k++;
			}
		}
	}
	public void setPreferredSize(Dimension d)
	{
		super.setPreferredSize(d);
		player.setSize(d);
	}
	private boolean noSuit(String cur)
	{
		GUICard[] c = player.getHand();
		for(int i = 0; i < 11 - Helper.number; i++)
			if(c[i] != null)
				if(!c[i].getName().equals("X"))
				if(c[i].getSuit().equals(cur))
					return false;
		return true;
	}
	public boolean has(GUICard c)
	{
		GUICard[] cards = player.getHand();
		for(int i = 0; i < 13; i++)
		{
			if(cards[i] != null)
			if(c.getName().equals(cards[i].getName()) ) return true;
		}
		return false;
	}
	public JPanel doItUser(String card, int w, int h)
	{
		GUICard[] cards = player.getHand();
		String thisSuit = card.charAt(0) + "";
		for(int i = 0; i < 13; i++)
		{
			if(cards[i] != null)
			if(!cards[i].getName().equals("X"))
			if(card.equals(cards[i].getName()))
			{
				if(player.getSeat().equals(Jacobian.turnStart))
				{
					Jacobian.curTurn = cards[i].getSuit();
					if(Jacobian.notrumph)
						Jacobian.trumph = Jacobian.curTurn;
					player.removeTurn();
					player.removeCard(i);
					updateUI();
					return player.makeMove(i, getHeight(), w, h);
				}
				else
				{
					if((thisSuit).equals(Jacobian.curTurn) || noSuit(Jacobian.curTurn))
					{
						player.removeTurn();
						player.removeCard(i);
						updateUI();
						return player.makeMove(i, getHeight(), w, h);
					}
					
				}
			}
		}
		return null;
	}
	public JPanel doIt(String card, int w, int h)
	{
		GUICard[] cards = player.getHand();
		if(player.getSeat().equals(Jacobian.turnStart))
		{
			Jacobian.curTurn = "" + card.charAt(0);
			if(Jacobian.notrumph)
				Jacobian.trumph = Jacobian.curTurn;
		}
		player.removeCard(card, 11 - Helper.number);
		updateUI();
		player.removeTurn();
		return player.makeMove(card , getHeight(), w, h);

	}
	public GUICard[] getHand()
	{
		return player.getHand();
	}
	public void reveal()
	{
		player.reveal();
	}
	public String getSeat()
	{
		return player.getSeat();
	}
	public void setTurn()
	{
		player.setTurn();
	}
	public void removeTurn()
	{
		player.removeTurn();
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public void setLabel(String seat)
	{
		nameLabel.setText(seat + "    " + name);
		nameLabel.updateUI();
	}
	public void setContract(String cont)
	{
		player.checkDummy(cont.substring(cont.length() - 1));
		player.checkTurn(cont.substring(cont.length() - 1));
		if(Jacobian.trumph.equals("S"))
		{
			Helper.suitsDis[0] = "D";
			Helper.suitsDis[1] = "C";
			Helper.suitsDis[2] = "H";
			Helper.suitsDis[3] = "S";
		}
		if(Jacobian.trumph.equals("H"))
		{
			Helper.suitsDis[0] = "C";
			Helper.suitsDis[1] = "D";
			Helper.suitsDis[2] = "S";
			Helper.suitsDis[3] = "H";
		}
		if(Jacobian.trumph.equals("D"))
		{
			Helper.suitsDis[0] = "C";
			Helper.suitsDis[1] = "H";
			Helper.suitsDis[2] = "S";
			Helper.suitsDis[3] = "D";
		}
		if(Jacobian.trumph.equals("C"))
		{
			Helper.suitsDis[0] = "D";
			Helper.suitsDis[1] = "S";
			Helper.suitsDis[2] = "H";
			Helper.suitsDis[3] = "C";
		}
	}
	public boolean isDummy() {
		return player.isDummy();
	}
	public boolean isTurn() {
		return player.isTurn();
	}
	public void setDummy() {
		player.setDummy();
	}
	public void enableButtons() {
		player.enableButtons();
	}
}
