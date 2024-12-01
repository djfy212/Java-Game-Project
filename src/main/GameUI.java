package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import entity.Entity;

public class GameUI {

	GamePanel gp;
	GamePanel gp2;
	Graphics2D g2;
	//Graphics2D g2d = gp.paintComponent();
	Font fontB;
	Font fontR;
	public boolean messageOn = false;
//	public String message = "";
//	int messageCounter = 0;
	ArrayList<String> message = new ArrayList<>();
	ArrayList<Integer> messageCounter = new ArrayList<>();
	public String currentDialogue = "";
	public int commandNum = 0;
	int subState = 0;
	int counter = 0;
	public Entity npc;
	
	// 점수 관련 변수
    private int score = 0;
	
	public GameUI(GamePanel gp) {
		this.gp = gp;
		// 폰트 파일을 통해 폰트 생성
		try {
			InputStream is = getClass().getResourceAsStream("/font/CookieRun Regular.ttf");
			fontR = Font.createFont(Font.TRUETYPE_FONT, is);
			is = getClass().getResourceAsStream("/font/CookieRun Bold.ttf");
			fontB = Font.createFont(Font.TRUETYPE_FONT, is);
		} catch (FontFormatException e) {
			System.out.println(e);
			// e.printStackTrace();
		} catch (IOException e) {
			System.out.println(e);
			// e.printStackTrace();
		}
	}

	public void addMessage(String text) {
		
		message.add(text);
		messageCounter.add(0);
	}
	
	 // 점수를 설정하는 메서드
    public void setScore(int score) {
        this.score = score;
    }

    // 점수 표시 메서드
    public void drawScore() {
        int x = gp.tileSize * 3; // 화면의 왼쪽에 위치
        int y = gp.tileSize; // 화면 상단에 위치

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 20F));
        g2.setColor(Color.WHITE);
        g2.drawString("Score: " + score, x, y);
    }

	public void draw(Graphics2D g2) {

		this.g2 = g2;
		g2.setFont(fontR);
		g2.setColor(Color.WHITE);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// PLAY STATE
		if (gp.gameState == gp.playState) {
			drawMainUI();
			drawPlayLife();
			drawDay();
			drawMessage();
			drawQuest();
		}
		// DIALOGUE STATE
		if (gp.gameState == gp.dialogueState) {
			drawDialogueScreen();
		}
		// TRANSITION STATE
		if (gp.gameState == gp.transitionState) {
			drawTransition();
		}
		// TRADE STATE
		if (gp.gameState == gp.tradeState) {
			drawTradeScreen();
		}
	}
	public void drawMainUI() {
		// WINDOW ///대화창 크기, 위치 설정
		int x = 1;
		int y = 1;
		int width = gp.tileSize * 5;
		int height = gp.tileSize * 2;
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 15F));
		
		drawSubWindow2(x,y,width,height);//왼쪽 위

		width = gp.tileSize * 7;
		height = gp.tileSize - gp.tileSize/3;
		
		x = gp.screenWidth - width-2;
		drawSubWindow2(x,y,width,height);//오른쪽 위

		x = gp.tileSize * 15;
		y = gp.tileSize * 9;
		drawSubWindow3(x,y,gp.tileSize,gp.tileSize,"U");

		x += gp.tileSize*1.5;
		drawSubWindow3(x,y,gp.tileSize,gp.tileSize,"I");		
		
		x += gp.tileSize*1.5;
		drawSubWindow3(x,y,gp.tileSize,gp.tileSize,"O");
		
		x = gp.tileSize * 15;
		y += gp.tileSize*1.5;
		drawSubWindow3(x,y,gp.tileSize,gp.tileSize,"J");
		
		x += gp.tileSize*1.5;
		drawSubWindow3(x,y,gp.tileSize,gp.tileSize,"K");
		
		x += gp.tileSize*1.5;
		drawSubWindow3(x,y,gp.tileSize,gp.tileSize,"L");
		
	}

	public void drawPlayLife() {

		int x = gp.tileSize / 2-10;
		int y = gp.tileSize / 2;
		int textX =  gp.tileSize * 3 + gp.tileSize/2;
		
		double oneHpScale = (double) gp.tileSize * 3 / gp.player.maxLife;
		double hpBarValue = oneHpScale * gp.player.life;
		double oneManaScale = (double) gp.tileSize * 3 / gp.player.maxMana;
		double mpBarValue = oneManaScale * gp.player.mana;
		double oneExpScale = (double) gp.tileSize * 3 / gp.player.nextLevelExp;
		double expBarValue = oneExpScale * gp.player.exp;
		
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 16F));
		
		// HP 바 출력
		g2.setColor(new Color(60,60,60));
		g2.fillRoundRect(x - 2, y - 2, gp.tileSize * 3 + 4, 14 ,3, 3);

		g2.setColor(new Color(255, 0, 30));
		g2.fillRoundRect(x, y, (int) hpBarValue, 10 ,3, 3);
		g2.setColor(Color.white);	
		g2.drawString(gp.player.life + "/" + gp.player.maxLife, textX , y+10);	// HP 수치	
		y+=gp.tileSize/2;
		
		// MP 바 출력
		g2.setColor(new Color(60,60,60));
		g2.fillRect(x - 2, y - 2, gp.tileSize * 3 + 4, 14);

		g2.setColor(new Color(30, 0, 255));
		g2.fillRect(x, y, (int) mpBarValue, 10);
		g2.setColor(Color.white);	
		g2.drawString(gp.player.mana + "/" + gp.player.maxMana, textX , y+10);	// MP 수치
		
		y+=gp.tileSize/2;
		
		// Exp 바 출력
		g2.setColor(new Color(60,60,60));
		g2.fillRect(x - 2, y - 2, gp.tileSize * 3 + 4, 14);

		g2.setColor(new Color(30, 255, 30));
		g2.fillRect(x, y, (int) expBarValue, 10);

		g2.setColor(Color.white);	
		g2.drawString(gp.player.exp + "/" + gp.player.nextLevelExp, textX , y+10);// Exp 수치

	}
	public void drawDay() {
		
		int x = gp.tileSize * 13 + gp.tileSize/2;
		int y = gp.tileSize / 2;
		String time = "";
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 16F));
		g2.setColor(Color.white);
		g2.drawString("Lv. " + gp.player.level, gp.tileSize * 8, y);
		g2.drawString("day: " + gp.dayCount, x, y);
		x += gp.tileSize*2;
		switch(gp.timeState) {
		case 0: time = "아침";break;
		case 1: time = "점심";break;
		case 2: time = "저녁";break;
		case 3: time = "밤";break;
		}
		g2.drawString("time: " + time,x, y);
		x += gp.tileSize*3d;
		int sec = gp.timeCount % 60;
		int min = gp.timeCount / 60;
		g2.drawString(min + ":" + sec, x, y);

	}
	public void drawQuest() {
		int quessX = gp.tileSize * 12;
		int quessY = gp.tileSize * 2;
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 16F));
		g2.setColor(Color.white);
		for(int i=0;i<gp.questList.size();i++) {
			if(gp.questList.get(i) != null) {
				g2.drawString(gp.questList.get(i).name + "을 쓰러뜨려라 (" + 
						gp.questList.get(i).count + "/" + gp.questList.get(i).maxCount +")",quessX ,quessY );
				if(gp.questList.get(i).finish == true) {
					g2.drawString("완료",quessX+gp.tileSize*5 ,quessY );
				}
				quessY += 20;
			}
		}
		
	}
	public void drawMessage() {
		int messageX = gp.tileSize;
		int messageY = gp.tileSize * 9;
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 16F));
		g2.setColor(Color.white);
		for(int i=0;i<message.size();i++) {
			if(message.get(i) != null) {
				g2.drawString(message.get(i), messageX, messageY);
				int counter = messageCounter.get(i) + 1;
				messageCounter.set(i, counter);
				messageY += 20;
				if(messageCounter.get(i) > 180) {
					message.remove(i);
					messageCounter.remove(i);
				}
			}
		}
	}
	public void drawDialogueScreen() {
		
		// WINDOW ///대화창 크기, 위치 설정
		int width = gp.screenWidth - gp.tileSize * 2;
		int height = gp.tileSize * 4;		
		int x = gp.tileSize;
		int y = gp.screenHeight - height-1;
		int standWidth = gp.tileSize * 9;
		int standHeight = gp.tileSize * 18;

		if(gp.npc[gp.currentMap][gp.player.speakNum].type == 9) {
			g2.drawImage(gp.player.standing, x-standWidth/2 + gp.tileSize*2, y-height*2 + gp.tileSize,
							standWidth, standHeight, null);
			g2.drawImage(gp.npc[gp.currentMap][gp.player.speakNum].standing, x+standWidth+ gp.tileSize*2, y-height*2 + gp.tileSize*2 + gp.tileSize/2, 
							standWidth-20, standHeight-20, null);			
		}
		
		/// 대화창 출력
		drawSubWindow(x, y, width, height);

		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 30f));
		x += gp.tileSize;
		y += gp.tileSize;
		for (String line : currentDialogue.split("\n")) {
			g2.drawString(line, x, y);
			y += 40;
		}	
		gp.keyH.enterPressed = false;
	}
	
	public void drawTransition() {
		counter++;
		g2.setColor(new Color(0,0,0,counter*5));
		g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
		if(counter == 50) {
			counter = 0;
			gp.gameState = gp.playState;
			gp.currentMap = gp.eHandler.tempMap;
			gp.player.worldX = gp.tileSize * gp.eHandler.tempCol;
			gp.player.worldY = gp.tileSize * gp.eHandler.tempRow;
			gp.eHandler.previousEventX = gp.player.worldX;
			gp.eHandler.previousEventY = gp.player.worldY;
		}
	}
	
	public void drawTradeScreen() {
		switch(subState) {
		case 0: trade_select();break;
		case 1: trade_buy();break;
		case 2: trade_sell();break;
		}
		gp.keyH.enterPressed = false;
	}
	public void trade_select() {
		drawDialogueScreen();
		
		int x = gp.tileSize * 15;
		int y = gp.tileSize * 4;
		int width = gp.tileSize*3;
		int height = (int)(gp.tileSize*3.5);
		drawSubWindow(x,y,width,height);
		
		x += gp.tileSize;
		y += gp.tileSize;
		g2.drawString("buy", x, y);
		
		y += gp.tileSize;
		g2.drawString("sell", x, y);
		
		y += gp.tileSize*2;
		g2.drawString("leave", x, y);
	}
	public void trade_buy() {
		
	}
	public void trade_sell() {
		
	}
	public void drawSubWindow(int x, int y, int width, int height) {

		Color c = new Color(0, 0, 0, 210);
		g2.setColor(c);
		g2.fillRoundRect(x, y, width, height, 35, 35);

		c = new Color(255, 255, 255);
		g2.setColor(c);
		g2.setStroke(new BasicStroke(5));
		g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);
	}
	public void drawSubWindow2(int x, int y, int width, int height) {

		Color c = new Color(0, 0, 0, 210);
		g2.setColor(c);
		g2.fillRect(x, y, width, height);

		c = new Color(55, 55, 55);
		g2.setColor(c);
		g2.setStroke(new BasicStroke(5));
		g2.drawRect(x, y, width, height );
	}
	public void drawSubWindow3(int x, int y, int width, int height, String text) {

		Color c = new Color(0, 0, 0, 210);
		g2.setColor(c);
		g2.fillRect(x, y, width, height);

		c = new Color(55, 55, 55);
		g2.setColor(c);
		g2.setStroke(new BasicStroke(5));
		g2.drawRect(x, y, width, height );
		
		g2.setColor(Color.WHITE);
		g2.drawString(text,x+1,y+gp.tileSize-2);
	}

	public int getXforCenteredText(String text) {
		int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		int x = gp.screenWidth / 2 - length / 2;
		return x;
	}
}
