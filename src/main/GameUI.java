package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import entity.Entity;
import object.OBJ_HpPotion;
import object.OBJ_MpPotion;

public class GameUI {
	BufferedImage standImage;
	GamePanel gp;
	Graphics2D g2;
	Font fontB;
	Font fontR;
	public boolean messageOn = false;
	ArrayList<String> message = new ArrayList<>();
	ArrayList<Integer> messageCounter = new ArrayList<>();
	public String currentDialogue = "";
	public int commandNum = 0;
	public int menuCommand = 0;
	int subState = 0;
	int menuCount = 0;
	int counter = 0;
	String announceBuy = "";
	public int shopItemNum = 0;
	public int skillCount = 0; // 현재 선택한 스킬
	public int itemCount = 0;	// 현재 선택한 아이템
	public Entity npc;
	OBJ_HpPotion hpPotion;
	OBJ_MpPotion mpPotion;	

	
    private Rectangle mainMenuButton;
    private Rectangle loadButton;
    private BufferedImage gameOverImage;
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
		hpPotion = new OBJ_HpPotion(gp);
		mpPotion = new OBJ_MpPotion(gp);

	}
	public void addMessage(String text) {
		
		message.add(text);
		messageCounter.add(0);
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
		// MENU STATE
		if (gp.gameState == gp.menuState) {
			drawDay();
			openMenu();
			drawMenuScreen();
		}
		// GAME OUT STATE
		if(gp.gameState == gp.gameOutState) {
			//drawGameOutScreen();
			drawGameOverScreen();
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

		x = gp.tileSize * 15;
		y = gp.tileSize * 9;
		drawSubWindow3(x,y,gp.tileSize,gp.tileSize,"U");
		g2.drawImage(hpPotion.down1 , x, y, gp.tileSize,  gp.tileSize,null);
		
		x += gp.tileSize*1.5;
		drawSubWindow3(x,y,gp.tileSize,gp.tileSize,"I");		
		g2.drawImage(mpPotion.down1, x, y, gp.tileSize,  gp.tileSize,null);		
		
		x += gp.tileSize*1.5;
		drawSubWindow3(x,y,gp.tileSize,gp.tileSize,"O");

		x = gp.tileSize * 15;
		y += gp.tileSize*1.5;
		drawSubWindow3(x,y,gp.tileSize,gp.tileSize,"J");
		g2.drawImage(gp.player.currentSkill[0].icon, x, y, gp.tileSize,  gp.tileSize,null);
		
		x += gp.tileSize*1.5;
		drawSubWindow3(x,y,gp.tileSize,gp.tileSize,"K");
		g2.drawImage(gp.player.currentSkill[1].icon, x, y, gp.tileSize,  gp.tileSize,null);
		
		x += gp.tileSize*1.5;
		drawSubWindow3(x,y,gp.tileSize,gp.tileSize,"L");
		g2.drawImage(gp.player.currentSkill[2].icon, x, y, gp.tileSize,  gp.tileSize,null);
		
		
	}
	public void drawPlayLife() {

		int x = gp.tileSize / 2-10;
		int y = 10;
		int textX =  gp.tileSize * 3 + gp.tileSize/2;
		
		double oneHpScale = (double) gp.tileSize * 3 / gp.player.maxLife;
		double hpBarValue = oneHpScale * gp.player.life;
		double oneManaScale = (double) gp.tileSize * 3 / gp.player.maxMana;
		double mpBarValue = oneManaScale * gp.player.mana;
		double oneExpScale = (double) gp.tileSize * 3 / gp.player.nextLevelExp;
		double expBarValue = oneExpScale * gp.player.exp;
		
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 16F));
		g2.drawString("Lv. " + gp.player.level, x, y+gp.tileSize/4);
		y+=gp.tileSize/2;
		// HP 바 출력
		g2.setColor(new Color(60,60,60));
		g2.fillRoundRect(x - 2, y - 2, gp.tileSize * 3 + 2, 12 ,3, 3);

		g2.setColor(new Color(255, 0, 30));
		g2.fillRoundRect(x, y, (int) hpBarValue, 10 ,3, 3);
		g2.setColor(Color.white);	
		g2.drawString(gp.player.life + "/" + gp.player.maxLife, textX , y+10);	// HP 수치	
		y+=gp.tileSize/2;
		
		// MP 바 출력
		g2.setColor(new Color(60,60,60));
		g2.fillRoundRect(x - 2, y - 2, gp.tileSize * 3 + 2, 12 ,3, 3);

		g2.setColor(new Color(30, 0, 255));
		g2.fillRoundRect(x-1, y-1, (int) mpBarValue, 10 ,3, 3);
		g2.setColor(Color.white);	
		g2.drawString(gp.player.mana + "/" + gp.player.maxMana, textX , y+10);	// MP 수치
		
		y+=gp.tileSize/2;
		
		// Exp 바 출력
		g2.setColor(new Color(60,60,60));
		g2.fillRoundRect(x - 2, y - 2, gp.tileSize * 3 + 2, 12 ,3, 3);

		g2.setColor(new Color(30, 255, 30));
		g2.fillRoundRect(x-1, y-1, (int) expBarValue, 10 ,3, 3);

		g2.setColor(Color.white);	
		g2.drawString(gp.player.exp + "/" + gp.player.nextLevelExp, textX , y+10);// Exp 수치

	}
	public void drawDay() {
		
		int width = gp.tileSize * 7;
		int height = gp.tileSize - gp.tileSize/3;		
		int y = 1;
		int x = gp.screenWidth - width-2;
	
		drawSubWindow2(x,y,width,height);//오른쪽 위
		
		x = gp.tileSize * 13 + gp.tileSize/2;
		y = gp.tileSize / 2;
		String time = "";
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 16F));
		g2.setColor(Color.white);
		g2.drawString("day: " + gp.dayCount, x, y);
		x += gp.tileSize*2;
		switch(gp.timeState) {
		case 0: time = "아침";break;
		case 1: time = "점심";break;
		case 2: time = "저녁";break;
		case 3: time = "밤";break;
		}
		g2.drawString(time,x, y);
		x += gp.tileSize*3d;
		int sec = gp.timeCount % 60;
		int min = gp.timeCount / 60;
		g2.drawString(min + ":" + sec, x, y);

	}
	public void drawQuest() {
		int quessX = gp.tileSize * 15;
		int quessY = gp.tileSize +20;
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
		
		
		int npcIndex = gp.cChecker.checkEntity(gp.player, gp.npc);
		if(gp.npc[gp.currentMap][npcIndex].type == 9) {
			g2.drawImage(gp.player.standing, x-standWidth/2 + gp.tileSize*2, y-height*2 + gp.tileSize,
							standWidth, standHeight, null);
			g2.drawImage(gp.npc[gp.currentMap][npcIndex].standing, x+standWidth+ gp.tileSize*2, y-height*2 + gp.tileSize*2 + gp.tileSize/2, 
							standWidth-20, standHeight-20, null);			
		}
		
		/// 대화창 출력
		drawSubWindow(x, y, width, height);

		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 30f));
		x += gp.tileSize;
		y += gp.tileSize;
		
		if(npc.dialogues[npc.dialogueSet][npc.dialogueIndex] != null) {
			currentDialogue = npc.dialogues[npc.dialogueSet][npc.dialogueIndex];
			
			if(gp.keyH.enterPressed == true) {
				if(gp.gameState == gp.dialogueState) {
					npc.dialogueIndex++;
					gp.keyH.enterPressed = false;
				}
			}
		}
		else {
			npc.dialogueIndex = 0;
			
			if(gp.gameState == gp.dialogueState) {
				gp.gameState = gp.playState;
			}
		}
		
		for (String line : currentDialogue.split("\n")) {
			g2.drawString(line, x, y);
			y += 40;
		}	
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
		
		npc.dialogueSet = 0;
		drawDialogueScreen();
		
		int x = gp.tileSize * 16;
		int y = gp.tileSize * 4 + gp.tileSize/2;
		int width = gp.tileSize*3;
		int height = (int)(gp.tileSize*3.5);
		drawSubWindow(x,y,width,height);
		
		x += gp.tileSize;
		y += gp.tileSize;
		
		if(commandNum == 0) {
			g2.setColor(new Color(200,200,200,210));
			g2.fillRoundRect(x-gp.tileSize,y-gp.tileSize+15,width,gp.tileSize,20,20);
			if(gp.keyH.enterPressed == true) {
				subState = 1;
			}		
			g2.setColor(Color.WHITE);
		}
		g2.drawString("구매", x, y);
		
		y += gp.tileSize;
		if(commandNum == 1) {
			g2.setColor(new Color(200,200,200,210));
			g2.fillRoundRect(x-gp.tileSize,y-gp.tileSize+15,width,gp.tileSize,20,20);
			if(gp.keyH.enterPressed == true) {
				subState = 2;
			}		
			g2.setColor(Color.WHITE);
		}
		g2.drawString("판매", x, y);
		
		y += gp.tileSize;		
		if(commandNum == 2) {
			g2.setColor(new Color(200,200,200,210));
			g2.fillRoundRect(x-gp.tileSize,y-gp.tileSize+15,width,gp.tileSize,20,20);
			if(gp.keyH.enterPressed == true) {
				commandNum = 0;
				npc.startDialogue(npc, 1);
			}		
			g2.setColor(Color.WHITE);
		}
		g2.drawString("취소", x, y);
	}
	public void trade_buy() {
		int npcIndex = gp.cChecker.checkEntity(gp.player, gp.npc);
		g2.setFont(g2.getFont().deriveFont(20F));
		int x = gp.tileSize*4;
		int y = gp.tileSize;
		int width = gp.tileSize*6;
		int height = gp.tileSize*5;
		drawSubWindow(x,y,width,height);
		x = gp.tileSize*4+20;
		y = gp.tileSize+12;
		width = gp.tileSize * 5+5;
		height = gp.tileSize ;
		for(int i=0;i<gp.npc[gp.currentMap][npcIndex].inventory.size();i++) {
			if(gp.npc[gp.currentMap][npcIndex].inventory.get(0) != null) {
				drawSubWindow2(x,y,width,height);
//				g2.drawImage(entity.inventory.get(i).down1, x, y,null);
				g2.setColor(Color.white);
				g2.drawString(gp.npc[gp.currentMap][npcIndex].inventory.get(i).name, x+5, y+20);
				
				
				String s = "가격: "+gp.npc[gp.currentMap][npcIndex].inventory.get(i).price;
				g2.drawString(s, x+gp.tileSize*3,y+20);
										
			}
			y += height;
		}
		x = gp.tileSize * 4 + 20;
		y = gp.tileSize + 12;
		g2.setColor(new Color(200, 200, 200, 210));
		g2.fillRoundRect(x, y+(height*shopItemNum), width, height, 20, 20);
		int myCoin = gp.player.coin;
		int price = gp.npc[gp.currentMap][npcIndex].inventory.get(shopItemNum).price;
		if(gp.keyH.enterPressed == true) {
			if( myCoin - price  < 0) {
				//g2.drawString("구매 불가", x+gp.tileSize*8,y+20);
				announceBuy = gp.npc[gp.currentMap][npcIndex].inventory.get(shopItemNum).name + "구매 불가";
				System.out.println(myCoin+" " + price);
			}
			else {
				String itemname = gp.npc[gp.currentMap][npcIndex].inventory.get(shopItemNum).name;
				int index = gp.player.searchItemInInventory(itemname);
				gp.player.inventory.get(index).amount+= 1;
				//g2.drawString("구매 완료", x+gp.tileSize*8,y+20);
				announceBuy = gp.npc[gp.currentMap][npcIndex].inventory.get(shopItemNum).name + "구매 완료";
				gp.player.coin -= gp.npc[gp.currentMap][npcIndex].inventory.get(shopItemNum).price;
			}
		}	
		g2.drawString(announceBuy, x+gp.tileSize*8,y+20);
	}
	public void trade_sell() {
		
	}

	public void drawMenuScreen() {
		switch(menuCount) {
		case 1: stateScreen();break;
		case 2: skillScreen();break;
		case 3: equipmetScreen();break;
		case 4: inventoryScreen();break;
		}
		gp.keyH.enterPressed = false;
	}
	public void openMenu() {
		int x = gp.tileSize;
		int y = gp.tileSize*2;
		int width = gp.tileSize*3;
		int height = (int)(gp.tileSize*6.5);
		drawSubWindow(x,y,width,height);
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 20F));
		x += gp.tileSize;
		y += gp.tileSize;
		
		if(menuCommand == 0) {
			g2.setColor(new Color(200,200,200,210));
			g2.fillRoundRect(x-gp.tileSize,y-gp.tileSize+15,width,gp.tileSize,20,20);
			if(gp.keyH.enterPressed == true) {
				menuCount = 1;
			}		
			g2.setColor(Color.WHITE);
		}
		g2.drawString("캐릭터 정보", x-gp.tileSize/2, y);
		
		y += gp.tileSize;
		if(menuCommand == 1) {
			g2.setColor(new Color(200,200,200,210));
			g2.fillRoundRect(x-gp.tileSize,y-gp.tileSize+15,width,gp.tileSize,20,20);
			if(gp.keyH.enterPressed == true) {
				menuCommand = -1;
				menuCount = 2;
				skillCount = 0;
			}		
			g2.setColor(Color.WHITE);
		}
		g2.drawString("스킬 정보",  x-gp.tileSize/2, y);
		
		y += gp.tileSize;		
		if(menuCommand == 2) {
			g2.setColor(new Color(200,200,200,210));
			g2.fillRoundRect(x-gp.tileSize,y-gp.tileSize+15,width,gp.tileSize,20,20);
			if(gp.keyH.enterPressed == true) {
				menuCount = 3;
			}		
			g2.setColor(Color.WHITE);
		}
		g2.drawString("장비 정보",  x-gp.tileSize/2 , y);
		
		y += gp.tileSize;	
		if(menuCommand == 3) {
			g2.setColor(new Color(200,200,200,210));
			g2.fillRoundRect(x-gp.tileSize,y-gp.tileSize+15,width,gp.tileSize,20,20);
			if(gp.keyH.enterPressed == true) {
				menuCommand = -1;
				menuCount = 4;
				itemCount = 0;
			}		
			g2.setColor(Color.WHITE);
		}
		g2.drawString("인벤토리",  x-gp.tileSize/2 , y);
		
		y += gp.tileSize;	
		if(menuCommand == 4) {
			g2.setColor(new Color(200,200,200,210));
			g2.fillRoundRect(x-gp.tileSize,y-gp.tileSize+15,width,gp.tileSize,20,20);
			if(gp.keyH.enterPressed == true) {
				gp.saveLoad.save();
				gp.keyH.enterPressed = false;
			}		
			g2.setColor(Color.WHITE);
		}
		g2.drawString("세이브",  x-gp.tileSize/2 , y);
		
		y += gp.tileSize;	
		if(menuCommand == 5) {
			g2.setColor(new Color(200,200,200,210));
			g2.fillRoundRect(x-gp.tileSize,y-gp.tileSize+15,width,gp.tileSize,20,20);
			if(gp.keyH.enterPressed == true) {
				gp.saveLoad.save();
				System.exit(0);
			}		
			g2.setColor(Color.WHITE);
		}
		g2.drawString("종료",  x-gp.tileSize/2 , y);
	}
	public void stateScreen() {
		int x = gp.tileSize*4;
		int y = gp.tileSize;
		int width = gp.tileSize*6;
		int height = gp.tileSize*5;
		drawSubWindow(x,y,width,height);
		
		g2.setColor(Color.white);
		g2.setFont(g2.getFont().deriveFont(15F));
		int textX = x + 20;
		int textY = y*2;

		g2.drawString("Lv.", textX, textY);
		g2.drawString(Integer.toString(gp.player.level), textX+gp.tileSize, textY);
		textY += 32;
		g2.drawString("Exp", textX, textY);
		g2.drawString(gp.player.exp + "/" + gp.player.nextLevelExp, textX+gp.tileSize, textY);	
		textY += 32;		
		g2.drawString("HP", textX, textY);
		g2.drawString(gp.player.life + "/" + gp.player.maxLife, textX+gp.tileSize, textY);
		textY += 32;
		g2.drawString("MP", textX, textY);
		g2.drawString(gp.player.mana + "/" + gp.player.maxMana, textX+gp.tileSize, textY);
		textY += 32;
		g2.drawString("공격력", textX, textY);
		g2.drawString(Integer.toString(gp.player.attack), textX+gp.tileSize, textY);
		textY += 32;
		g2.drawString("방어력", textX, textY);
		g2.drawString(Integer.toString(gp.player.defence), textX+gp.tileSize, textY);
		textX += 150;
		g2.drawString("골드: ", textX, textY);
		g2.drawString(Integer.toString(gp.player.coin), textX+gp.tileSize, textY);
		
		//drawSubWindow(gp.tileSize*3,gp.tileSize*6,gp.tileSize*2,gp.tileSize);
		x = gp.tileSize*4;
		y = gp.tileSize*6;
		width = gp.tileSize*6;
		height = gp.tileSize*5;
		drawSubWindow(x,y,width,height);
		g2.setColor(Color.white);		
		x += gp.tileSize;
		y += gp.tileSize;
		g2.drawString("<진행중인 퀘스트>",x,y );
		y += gp.tileSize;

		for(int i=0;i<gp.questList.size();i++) {
			if(gp.questList.get(i) != null) {
				g2.drawString(gp.questList.get(i).name + "을 쓰러뜨려라 (" + 
						gp.questList.get(i).count + "/" + gp.questList.get(i).maxCount +")",x ,y );
				if(gp.questList.get(i).finish == true) {
					g2.drawString("완료",x+gp.tileSize*5 ,y );
				}
				y += 20;
			}
		}
	}
	public void skillScreen() {
		g2.setFont(g2.getFont().deriveFont(15F));
		int x = gp.tileSize*4;
		int y = gp.tileSize;
		int width = gp.tileSize*6;
		int height = gp.tileSize*5;
		drawSubWindow(x,y,width,height);
		x = gp.tileSize*4+20;
		y = gp.tileSize+12;
		width = gp.tileSize * 5+5;
		height = gp.tileSize ;		
		for(int i=0;i<gp.skillAllList.size();i++) {
			if(gp.skillAllList.get(0) != null) {
				drawSubWindow2(x,y,width,height);
				g2.drawImage(gp.skillAllList.get(i).icon, x, y, gp.tileSize, gp.tileSize,null);
				g2.setColor(Color.white);
				g2.drawString(gp.skillAllList.get(i).name,x+gp.tileSize+5,y+20);
				g2.drawString(Integer.toString(gp.skillAllList.get(i).skillLevel),x+gp.tileSize*3,y+40);
				y += height;
			}
		}
		//회색 바
		x = gp.tileSize * 4 + 20;
		y = gp.tileSize + 12;
		g2.setColor(new Color(200, 200, 200, 210));
		g2.fillRoundRect(x, y+(height*skillCount), width, height, 20, 20);
		
		//설명 창
		g2.setColor(Color.white);
		x = gp.tileSize*10;
		y = gp.tileSize;
		width = gp.tileSize*6;
		height = gp.tileSize*5;
		drawSubWindow(x,y,width,height);
		
		for (String line : gp.skillAllList.get(skillCount).description.split("\n")) {
			g2.drawString(line, x+20, y+gp.tileSize);
			y += 20;
		}
	}
	public void equipmetScreen() {
		int x = gp.tileSize*4;
		int y = gp.tileSize;
		int width = gp.tileSize*12;
		int height = gp.tileSize*10;
		drawSubWindow(x,y,width,height);
		
		g2.setColor(Color.white);
		g2.setFont(g2.getFont().deriveFont(15F));
		int textX = x + 20;
		int textY = y*2;
		
		g2.drawString("무기: ", textX, textY);
		g2.drawString(gp.player.currentWeapon.name, textX+gp.tileSize, textY);
		textY += 32;
		g2.drawString("방어구: ", textX, textY);
		g2.drawString(gp.player.currentShield.name, textX+gp.tileSize, textY);
		textY += 32;
	}
	public void inventoryScreen() {
		g2.setFont(g2.getFont().deriveFont(20F));
		
		int x = gp.tileSize*4;
		int y = gp.tileSize;
		int width = gp.tileSize*6;
		int height = gp.tileSize*5;
		drawSubWindow(x,y,width,height);
		x = gp.tileSize*4+20;
		y = gp.tileSize+12;
		width = gp.tileSize * 5+5;
		height = gp.tileSize ;
		for(int i=0;i<gp.player.inventory.size();i++) {
			if(gp.player.inventory.get(0) != null) {
				drawSubWindow2(x,y,width,height);
//				g2.drawImage(entity.inventory.get(i).down1, x, y,null);
				g2.setColor(Color.white);
				g2.drawString(gp.player.inventory.get(i).name, x+5, y+20);
				if(gp.player.inventory.get(i).amount >= 1) {
					String s = ""+gp.player.inventory.get(i).amount;
					g2.drawString(s, x+gp.tileSize*3,y+20);
				}			
				
			}
			y += height;
		}
		
		//회색 바
		//itemCount = 0;
		x = gp.tileSize * 4 + 20;
		y = gp.tileSize + 12;
		g2.setColor(new Color(200, 200, 200, 210));
		g2.fillRoundRect(x, y+(height*itemCount), width, height, 20, 20);
		
		//설명 창
		g2.setColor(Color.white);
		x = gp.tileSize*10;
		y = gp.tileSize;
		width = gp.tileSize*6;
		height = gp.tileSize*5;
		drawSubWindow(x,y,width,height);
		
		for (String line : gp.player.inventory.get(itemCount).description.split("\n")) {
			g2.drawString(line, x+20, y+gp.tileSize);
			y += 20;
		}
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
		g2.drawRect(x-1, y-1, width+2, height+2 );
		
		g2.setColor(Color.WHITE);
		g2.drawString(text,x-6,y+gp.tileSize);
	}
	public int getXforCenteredText(String text) {
		int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		int x = gp.screenWidth / 2 - length / 2;
		return x;
	}
	
	public void drawGameOutScreen() {
		g2.setColor(Color.BLACK);
		g2.fillRect(0,0,gp.screenWidth,gp.screenHeight);
		g2.setColor(Color.white);
		g2.setFont(g2.getFont().deriveFont(30F));
		g2.drawString("게임 아웃",gp.screenWidth/2,gp.screenHeight/2);
		if(gp.keyH.enterPressed == true) {
			
			gp.main.panelState = gp.main.title;
			Main.layout.show(Main.mainPanel,"tp");
        	Main.mainPanel.getComponent(0).setFocusable(true);
        	Main.mainPanel.getComponent(0).requestFocusInWindow();
        	gp.main.gamePanel = new GamePanel(gp.main);
        	gp.main.gamePanel.setupGame();
        	gp.main.gamePanel.startThread();
		}
	}
	
	public void drawGameOverScreen() {
//	    String text = "Game Over";
//	    g2.setFont(fontB.deriveFont(80F));
//	    g2.setColor(Color.RED);
//	    int x = getXforCenteredText(text);
//	    int y = gp.screenHeight / 2 - 100; // 텍스트 위치 조정
//	    g2.drawString(text, x, y);
		
		 // 이미지를 중앙에 그리기
	    if (gameOverImage != null) {
	        int imgX = (gp.screenWidth - gameOverImage.getWidth()) / 2;
	        int imgY = (gp.screenHeight - gameOverImage.getHeight()) / 2 - 50; // 위치 조정
	        g2.drawImage(gameOverImage, imgX, imgY, null);
	    }

	    // 버튼 크기 및 위치
	    int buttonWidth = 300;
	    int buttonHeight = 50;
	    int buttonX = (gp.screenWidth - buttonWidth) / 2;
	    int mainMenuButtonY = gp.screenHeight / 2 + 50;
	    int loadButtonY = gp.screenHeight / 2 + 120;

	    // "메인 화면으로" 버튼 그리기
	    drawButton(g2, buttonX, mainMenuButtonY, buttonWidth, buttonHeight, "메인 화면으로");

	    // "불러오기" 버튼 그리기
	    drawButton(g2, buttonX, loadButtonY, buttonWidth, buttonHeight, "불러오기");

	    // 마우스 클릭 이벤트 추가
	    MouseAdapter mouseAdapter = new MouseAdapter() {
	        @Override
	        public void mouseClicked(MouseEvent e) {
	            int mouseX = e.getX();
	            int mouseY = e.getY();

	            // 메인 메뉴 버튼 클릭 영역 확인
	            if (mouseX >= buttonX && mouseX <= buttonX + buttonWidth
	                    && mouseY >= mainMenuButtonY && mouseY <= mainMenuButtonY + buttonHeight&&gp.gameState == gp.gameOutState) {
	                System.out.println("메인 화면으로 버튼 클릭");
	                

	                // Main 클래스의 panelState를 title로 설정
	                gp.main.panelState = gp.main.title;

	                // CardLayout을 사용하여 titlePanel로 전환
	                Main.layout.show(Main.mainPanel, "tp"); 

	                // TitlePanel 화면 다시 그리기
	                gp.main.titlePanel.repaint();
	                gp.removeMouseListener(this); // 이벤트 중복 방지
					gp.player.resetGame = true;
	            }


	            // 불러오기 버튼 클릭 영역 확인
	            if (mouseX >= buttonX && mouseX <= buttonX + buttonWidth
	                    && mouseY >= loadButtonY && mouseY <= loadButtonY + buttonHeight &&gp.gameState == gp.gameOutState) {
	                System.out.println("불러오기 버튼 클릭");
	                
	                gp.gameState = gp.playState; // 플레이 상태로 전환
	                gp.removeMouseListener(this); // 이벤트 중복 방지
	                gp.saveLoad.load(); // 저장 데이터 로드

	            }
	        }
	    };	    

	    // 마우스 리스너 추가
	    gp.addMouseListener(mouseAdapter);
	}
	 private void drawButton(Graphics2D g2, int x, int y, int width, int height, String text) {
	        // 버튼 배경
	        g2.setColor(new Color(200, 200, 200)); // 연한 회색
	        g2.fillRoundRect(x, y, width, height, 15, 15);

	        // 버튼 테두리
	        g2.setColor(new Color(150, 150, 150)); // 진한 회색
	        g2.setStroke(new BasicStroke(3));
	        g2.drawRoundRect(x, y, width, height, 15, 15);

	        // 버튼 텍스트
	        g2.setFont(fontR.deriveFont(30F));
	        g2.setColor(Color.BLACK); // 텍스트 색상
	        int textX = x + (width - g2.getFontMetrics().stringWidth(text)) / 2;
	        int textY = y + (height + g2.getFontMetrics().getAscent()) / 2 - 5;
	        g2.drawString(text, textX, textY);
	    }
}
