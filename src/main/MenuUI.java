package main;



import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import entity.Entity;


public class MenuUI {

	MenuPanel mp;
	Graphics2D g2;
	
	Font font;
	BufferedImage image;
	
	int subState = 0;
	public int commandNum = 0;
	
	public MenuUI(MenuPanel mp) {
		this.mp = mp;
		try {
			InputStream is = getClass().getResourceAsStream("/font/CookieRun Bold.ttf");
			font = Font.createFont(Font.TRUETYPE_FONT, is);
		}catch(FontFormatException e) {
			System.out.println(e);
			//e.printStackTrace();
		}catch(IOException e) {
			System.out.println(e);
			//e.printStackTrace();
		}
	}
	
	public void draw(Graphics2D g2) {
		
		this.g2 = g2;
		g2.setFont(font);
		g2.setColor(Color.WHITE);
		if(mp.menuState == mp.charState) {
			drawCharInfo();
		}
		else if(mp.menuState == mp.equipState) {
			drawEquipInfo();
		}
		else if(mp.menuState == mp.invenState) {
			drawInventoryInfo(mp.gp.player);
		}
		else if(mp.menuState == mp.optionState) {
			drawOptionInfo();
		}
	}
	public void drawCharInfo() {
		
		g2.setColor(Color.white);
		g2.setFont(g2.getFont().deriveFont(32F));
		int textX = 20;
		int textY = 100;
		// Names
		g2.drawString("Level", textX, textY);
		textY += 32;
		g2.drawString("HP", textX, textY);
		textY += 32;
		g2.drawString("MP", textX, textY);
		textY += 32;
		g2.drawString("Attack", textX, textY);
		textY += 32;
		g2.drawString("Defense", textX, textY);
		textY += 32;
		g2.drawString("Exp", textX, textY);
		textY += 32;
		g2.drawString("Next Exp", textX, textY);
		textY += 32;
		g2.drawString("Weapon", textX, textY);
		textY += 32;
		g2.drawString("Shield", textX, textY);
//		textY += 32;
		
	}
	public void drawEquipInfo() {
		
	}
	public void drawInventoryInfo(Entity entity) {
		g2.setColor(Color.white);
		g2.setFont(g2.getFont().deriveFont(32F));
		int x = 100;
		int y = 100;
		for(int i=0;i<mp.gp.player.inventory.size();i++) {
//			g2.drawImage(entity.inventory.get(i).down1, x, y,null);
			g2.drawString(entity.inventory.get(i).name, x, y);
			if(entity.inventory.get(i).amount >= 1) {
				String s = ""+entity.inventory.get(i).amount;
				g2.drawString(s, x+200, y);
			}
			y += 30;
		}
	}
	public void drawOptionInfo() {
		g2.setColor(Color.white);
		g2.setFont(g2.getFont().deriveFont(32F));
		
		switch(subState) {
		case 0: option_top(); break;
		case 1: break;
		case 2: break;
		}
	}
	public void option_top() {
		int textX;
		int textY;
		
		String text = "설정";
		textX = 500;
		textY = 48;
		g2.drawString(text, textX, textY);
		textX = 100;
		textY = 48*2;
		g2.drawString("전체화면", textX, textY);
		
		textY = 48*3;
		g2.drawString("게임 종료", textX, textY);
		
		textY = 48*4;
		g2.drawString("돌아가기", textX, textY);
		
	}

}
