import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.*;

public class GUIPlayer {
	private String seat;
	private boolean isTurn = false;
	private boolean isDummy = false;
	private final GUICard[] hand = new GUICard[13];
	public GUIPlayer(Jacobian jc)
	{
		for(int i = 0; i < 13; i++)
			hand[i] = new GUICard(jc);
	}
	public void checkDummy(String seat)
	{
		int i = 0; 
		while(!Helper.seats[i].equals(seat)) i++;
		isDummy = this.seat.equals(Helper.seats[(i + 2)%4]);
	}
	public void checkTurn(String seat)
	{
		int i = 0; 
		while(!Helper.seats[i].equals(seat)) i++;
		isTurn= this.seat.equals(Helper.seats[(i + 1)%4]);	
	}
	public void reveal()
	{
		for(int i = 0; i < hand.length; i++)
			if(hand[i] != null) hand[i].showCard();
	}
	public void setHand(String hand)
	{
		int i = 0;
		int k = 0;
		for(int j = 3 ; j >= 0; j--)
		{
			while(i < hand.length() && hand.charAt(i) != '.')
			{
		
					this.hand[k].setCard(Helper.suits[j] + hand.charAt(i));
					i++;
					k++;
			}
			i++;
		}
		Arrays.sort(this.hand);
		for(int k2 = 0; k2 < this.hand.length; k2++)
			if(this.hand[k2] != null ) 
			{
				if(!this.hand[k2].getName().equals("X"))
				this.hand[k2].updateUI();
			}
	}
	GUICard[] getHand() { return hand; }
	public void setSize(Dimension d)
	{
		if(hand != null)
		for(int i = 0; i < hand.length; i++)
		{
			if(hand[i] != null)
			{
		
				if(i < 12)
					hand[i].setSize(d);
				else
					hand[i].setSizeBig(d);
			}
		}
	}
	public JPanel makeMove(int i, int h, int panelW, int panelH) 
	{
		return Helper.drawPanel(h - 10, panelW, panelH);	
	}
	public JPanel makeMove(String card, int h, int panelW, int panelH) 
	{
		Helper.history.add(new GUICard(card));
		if(seat.equals(Jacobian.turnStart))
			Jacobian.curTurn = "" + card.charAt(0);
		return Helper.drawPanel(h-10, panelW, panelH);	
	}
	public void setTurn()	{isTurn=true;}
	public void removeTurn()	{isTurn=false;}
	public boolean isTurn() { return isTurn; }
	public boolean isDummy() { return isDummy; }
	public String getSeat() { return seat; }
	public void setSeat(String s) {
		this.seat = s;
	}
	public void setDummy() {
		isDummy = true;
	}
	public void enableButtons() {
		for(int i = 0; i < hand.length; i++)
			hand[i].setEnabled(true);
	}
	public void removeCard(int i) {
		Helper.history.add(new GUICard(hand[i].getName()));
		if(seat.equals(Jacobian.turnStart))
			Jacobian.curTurn = hand[i].getSuit();
		for(int j = i; j < 12; j++)
		{
			hand[j].become(hand[j+1]);
		}
		hand[11 - Helper.number].setBackground(Helper.bgColor);
		hand[11 - Helper.number].setForeground(Helper.bgColor);
		hand[11 - Helper.number].setSelected(false);
		hand[11 - Helper.number].setFocusable(false);
		hand[11 - Helper.number].setName("X");
		hand[11 - Helper.number].setText("");
		hand[11 - Helper.number].setBorder(BorderFactory.createEmptyBorder());
		hand[11 - Helper.number].setEnabled(false);
	}
	public void removeCard(String card, int i) {
		if (i > 0)
		{
			for(int j = i - 1; j < i; j++)
			{
				hand[j].become(hand[j+1]);
			}
		}
		hand[11 - Helper.number].setSize(Helper.dim);
		hand[11 - Helper.number].setIcon(null);
		hand[11 - Helper.number].setBackground(Helper.bgColor);
		hand[11 - Helper.number].setForeground(Helper.bgColor);
		hand[11 - Helper.number].setSelected(false);
		hand[11 - Helper.number].setFocusable(false);
		hand[11 - Helper.number].setName("X");
		hand[11 - Helper.number].setText("");
		hand[11 - Helper.number].setBorder(BorderFactory.createEmptyBorder());
		hand[11 - Helper.number].setEnabled(false);
	}
}
