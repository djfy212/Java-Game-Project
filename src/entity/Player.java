package entity;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.KeyHandler;
import main.UtilityTool;
import object.OBJ_HpPotion;
import object.OBJ_MpPotion;
import object.OBJ_Shield_Normal;
import object.OBJ_Weapon_Normal;

public class Player extends Entity{
	
	KeyHandler keyH;	
	public int screenX;
	public int screenY;	
	int standCounter = 0;
	public boolean attackCanceled = false;

	
	public Player(GamePanel gp, KeyHandler keyH) {
		
		super(gp);
		this.keyH = keyH;
		
		screenX = gp.screenWidth/2 - (gp.tileSize/2);
		screenY = gp.screenHeight/2 - (gp.tileSize/2);
		
		solidArea = new Rectangle();
		solidArea.x = 8;
		solidArea.y = 16;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		solidArea.width = 32;
		solidArea.height = 32;
		
		attackArea.width = 36;
		attackArea.height = 36;
				
		setDefaultValues();
		getPlayerImage();
		getPlayerAttackImage();
		setItems();
	}
	// 플레이어 관련 메소드
	
	public void setDefaultValues() {
		
		worldX = gp.tileSize * 20;
		worldY = gp.tileSize * 16;	
		
		speed = 4;
		direction = "down";
		
		//PLAYER STATUS
		maxLife = 10;
		life = maxLife;
		maxMana = 20;
		mana = maxMana;
		level = 1;
		exp = 0;
		nextLevelExp = 5;
		strength = 1;
		coin = 0;
		currentWeapon = new OBJ_Weapon_Normal(gp);
		currentShield = new OBJ_Shield_Normal(gp);
		attack = getAttack();
		defence = getDefence();
	}
	public void setItems() {
		inventory.add(currentWeapon);
		inventory.add(currentShield);
		inventory.add(new OBJ_HpPotion(gp));
		inventory.add(new OBJ_HpPotion(gp));
		inventory.add(new OBJ_HpPotion(gp));
		inventory.add(new OBJ_MpPotion(gp));
	}
	public int getAttack() {
		attack = strength + currentWeapon.attackValue;
		return attack;
	}
	public int getDefence() {
		defence = currentWeapon.defenceValue;
		return defence;
	}
	public int getCurrentWeaponSlot() {
		int currentWeaponSlot = 0;
		for(int i=0;i<inventory.size();i++) {
			if(inventory.get(i) == currentWeapon) {
				currentWeaponSlot = i;
			}
		}
		return currentWeaponSlot;
	}
	public int getCurrentShieldSlot() {
		int currentShieldSlot = 0;
		for (int i = 0; i < inventory.size(); i++) {
			if (inventory.get(i) == currentShield) {
				currentShieldSlot = i;
			}
		}
		return currentShieldSlot;
	}
	public void getPlayerImage() {
		
		up1 = setup("/player/u1",gp.tileSize,gp.tileSize);
		up2 = setup("/player/u2",gp.tileSize,gp.tileSize);
		down1 = setup("/player/d1",gp.tileSize,gp.tileSize);
		down2 = setup("/player/d2",gp.tileSize,gp.tileSize);
		left1 = setup("/player/u1",gp.tileSize,gp.tileSize);
		left2 = setup("/player/u2",gp.tileSize,gp.tileSize);
		right1 = setup("/player/d1",gp.tileSize,gp.tileSize);
		right2 = setup("/player/d2",gp.tileSize,gp.tileSize);
		
	}
	public void getPlayerAttackImage() {
		
		att_up1 = setup("/player/u1",gp.tileSize,gp.tileSize*2);
		att_up2 = setup("/player/u2",gp.tileSize,gp.tileSize*2);
		att_down1 = setup("/player/d1",gp.tileSize,gp.tileSize*2);
		att_down2 = setup("/player/d2",gp.tileSize,gp.tileSize*2);
		att_left1 = setup("/player/u1",gp.tileSize*2,gp.tileSize);
		att_left2 = setup("/player/u2",gp.tileSize*2,gp.tileSize);
		att_right1 = setup("/player/d1",gp.tileSize*2,gp.tileSize);
		att_right2 = setup("/player/d2",gp.tileSize*2,gp.tileSize);
		
	}	

	public void update() {
		
		if(attacking == true) {
			attacking();
		}
		
		else if(keyH.upPressed == true || keyH.downPressed == true || 
				keyH.leftPressed == true ||keyH.rightPressed == true || keyH.enterPressed == true) {
			
			if(keyH.upPressed == true) {
				direction = "up";			
			}
			else if(keyH.downPressed == true) {
				direction = "down";		
			}
			else if(keyH.leftPressed == true) {
				direction = "left";		
			}
			else if(keyH.rightPressed == true) {
				direction = "right";		
			}
			
			//CHECK TILE COLLISION
			collisionOn = false;
			gp.cChecker.checkTile(this);
			
			//CHECK OBJECT COLLISION
			int objIndex = gp.cChecker.checkObject(this, true);
			
			//CHECK NPC COLLISION
			int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
			interactNPC(npcIndex);
			
			//CHECK MONSTER COLLISION
			int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
			contactMonster(monsterIndex);
			//damageMonster(monsterIndex);
			
			//CHECK EVENT COLLISION
			gp.eHandler.checkEvent();
			
			//IF COLLISION IS FALSE, PLAYER CAN MOVE
			if(collisionOn == false && keyH.enterPressed == false) {
				switch(direction) {
				case "up": worldY -= speed; break;
				case "down": worldY += speed; break;
				case "left": worldX -= speed; break;
				case "right": worldX += speed; break;
				}
			}
			
			if(keyH.enterPressed == true && attackCanceled == false) {
				attacking = true;
				mana -= 1;
				spriteCounter = 0;
			}
			
			attackCanceled = false;
			gp.keyH.enterPressed = false;
			
			spriteCounter++;
			if(spriteCounter > 12) {
				if(spriteNum == 1) {
					spriteNum = 2;
				}
				else if(spriteNum == 2) {
					spriteNum = 1;
				}
				spriteCounter = 0;
			}			
		}
		else {
			standCounter++;
			if(standCounter == 20) {
				spriteNum = 1;
				standCounter = 0;
			}
		}
	
		if(invincible == true) {
			invincibleCounter++;
			if(invincibleCounter > 60) {
				invincible = false;
				invincibleCounter = 0;
			}
		}
	}

	public void attacking() {
		
		spriteCounter++;
		
		if(spriteCounter <= 5) {
			spriteNum = 1;
		}
		if(spriteCounter > 5 && spriteCounter <= 25) {
			spriteNum = 2;
			
			//Save the current worldX, worldY, solidArea
			int currentWorldX = worldX;
			int currentWorldY = worldY;
			int solidAreaWidth = solidArea.width;
			int solidAreaHeight = solidArea.height;
			
			//Adjust player's worldX/Y for the attackAea
			switch(direction) {
			case "up": worldY -= attackArea.height; break;
			case "down": worldY += attackArea.height; break;
			case "left": worldX -= attackArea.width; break;
			case "right": worldX += attackArea.width; break;
			}
			//attackArea becomes solidArea
			solidArea.width = attackArea.width;
			solidArea.height = attackArea.height;
			//Check monster collision with the updated worldX, worldY, solidArea
			int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
			damageMonster(monsterIndex);
			worldX = currentWorldX;
			worldY = currentWorldY;
			solidArea.width = solidAreaWidth;
			solidArea.height = solidAreaHeight;
		
		}
		if(spriteCounter > 25) {
			spriteNum = 1;
			spriteCounter = 0;
			attacking = false;
		}
	}	
	public void pickUpObject(int i) {
		
		if(i!= 999) {
			
			if(canObtainItem(gp.obj[gp.currentMap][i]) == true) {
				//
			}
			gp.obj[gp.currentMap][i] = null;
		}
	}
	public void interactNPC(int i) {
		
		if(gp.keyH.enterPressed == true) {
			if(i!= 999) {		
				gp.gameState = gp.dialogueState;
				gp.npc[gp.currentMap][i].speak();	
				attackCanceled = true;
			}
		}		
	}	
	public void contactMonster(int i) {
		
		if(i!= 999) {
			if(invincible == false && gp.monster[gp.currentMap][i].dying == false) {
				int damage = gp.monster[gp.currentMap][i].attack - defence;
				if(damage < 0) {
					damage = 1; // 최소 데미지 1
				}
				
				life -= damage;
				invincible = true;
			}
		}
	}
	public void damageMonster(int i) {
		
		if(i!= 999) {			

			if(gp.monster[gp.currentMap][i].invincible == false) {
				int damage = attack - gp.monster[gp.currentMap][i].defence;
				if(damage <=0 ) {
					damage = 1;
				}
				gp.monster[gp.currentMap][i].life -= damage;
				gp.ui.addMessage(damage + "의 데미지를 입힘.");
				
				gp.monster[gp.currentMap][i].invincible = true;
				gp.monster[gp.currentMap][i].damageReaction();
				
				if(gp.monster[gp.currentMap][i].life <= 0) {
					
					gp.monster[gp.currentMap][i].dying = true;
					exp += gp.monster[gp.currentMap][i].exp;
					gp.ui.addMessage(gp.monster[gp.currentMap][i].name + "을 쓰러트림.");
					checkQuest(i);
					checkLevelUp();
				}				
			}		
		}
	}
	public void checkQuest(int i) {
		for(int j=0;j<gp.questList.size();j++) {
			if(gp.questList.get(j).name.equals(gp.monster[gp.currentMap][i].name)) {
				gp.questList.get(j).count += 1;
			}
		}
	}
	public void checkLevelUp() {
		if(exp >= nextLevelExp) {
			level++;
			exp = exp - nextLevelExp;
			nextLevelExp *= level;
		}
	}
	public int searchItemInInventory(String itemName) {
		int itemIndex = 999;
		for(int i=0;i<inventory.size();i++) {
			if(inventory.get(i).name.equals(itemName)) {
				itemIndex = i;
				break;
			}		
		}
		return itemIndex;
	}
	public boolean canObtainItem(Entity item) {
		boolean canObtain = false;	
		// CHECK IF STACKABLE
		if(item.stackable == true) {
			int index = searchItemInInventory(item.name);
			
			if(index != 999) {
				inventory.get(index).amount++;
				canObtain = true;
			}
			else {
				if(inventory.size()!= maxInventorySize) {
					inventory.add(item);
					canObtain = true;
				}
			}
		}
		else {
			if(inventory.size()!= maxInventorySize) {
				inventory.add(item);
				canObtain = true;
			}
		}
		return canObtain;
	}

	public void draw(Graphics2D g2) {
		
		BufferedImage image = null;
		int tempScreenX = screenX;
		int tempScreenY = screenY;
		
		switch(direction) {
		case "up":
			if(attacking == false) {
				if(spriteNum == 1) {image = up1;}
				if(spriteNum == 2) {image = up2;}				
			}
			if(attacking == true) {
				tempScreenY = screenY - gp.tileSize;
				if(spriteNum == 1) {image = att_up1;}
				if(spriteNum == 2) {image = att_up2;}	
			}
			break;
		case "down":
			if(attacking == false) {
				if(spriteNum == 1) {image = down1;}
				if(spriteNum == 2) {image = down2;}				
			}
			if(attacking == true) {
				if(spriteNum == 1) {image = att_down1;}
				if(spriteNum == 2) {image = att_down2;}	
			}			
			break;
		case "left":
			if(attacking == false) {		
				if(spriteNum == 1) {image = left1;}
				if(spriteNum == 2) {image = left2;}			
			}
			if(attacking == true) {
				tempScreenX = screenX - gp.tileSize;
				if(spriteNum == 1) {image = att_left1;}
				if(spriteNum == 2) {image = att_left2;}					
			}
			break;
		case "right":
			if(attacking == false) {
				if(spriteNum == 1) {image = right1;}
				if(spriteNum == 2) {image = right2;}
			}
			if(attacking == true) {
				if(spriteNum == 1) {image = att_right1;}
				if(spriteNum == 2) {image = att_right2;}
			}		
			break;
		}
		
		//데이지 입었을 때 투명화
		if(invincible == true) {
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
		}
//		if(worldX <= gp.screenWidth/2 - gp.tileSize || worldY <= gp.screenHeight/2) {
//			//ascreenX = worldX;
//			g2.drawImage(image, worldX, tempScreenY, null);
//		}			
//		else
			g2.drawImage(image, tempScreenX, tempScreenY, null);

		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
		
	}
}
