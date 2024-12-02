package main;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JButton;

import data.RoundedButton;


public class TitleUI {
	
	TitlePanel tp;		
	Graphics2D g2;
	public RoundedButton startBtn = new RoundedButton();
	public RoundedButton continueBtn = new RoundedButton();
	public RoundedButton exitBtn = new RoundedButton();
	File file = new File("save.dat");
	
	public TitleUI(TitlePanel tp) {
		this.tp = tp;
		tp.setLayout(null);	
	}


//	class MyMouseListener extends MouseAdapter {

//		public void mouseEntered(MouseEvent e) {
//			JButton btn = (JButton) e.getSource();
//			btn.setBackground(Color.DARK_GRAY);
//		}
//		public void mouseExited(MouseEvent e) {
//			Component c = (Component) e.getSource();
//			c.setBackground(Color.LIGHT_GRAY);
//		}
//		public void mouseClicked(MouseEvent e) {
//			Component c = (Component) e.getSource();
//			c.setBackground(Color.LIGHT_GRAY);
//		}
//	}
	

}

