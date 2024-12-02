package entity;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.UtilityTool;

public class Entity {
	
	GamePanel gp;
	public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
	public BufferedImage att_up1, att_up2, att_down1, att_down2, att_left1, att_left2, att_right1, att_right2;
	public BufferedImage image, questImage;	
	public BufferedImage standing, icon;
	public Rectangle solidArea = new Rectangle(0,0,48,48);
	public Rectangle skillArea = new Rectangle(0,0,0,0);
	public Rectangle attackArea = new Rectangle(0,0,0,0);
	public int solidAreaDefaultX, solidAreaDefaultY;
	public boolean collision = false;
	public boolean targetOn = false;
	public String dialogues[][] = new String[20][20];
	
	//STATE
	public int worldX, worldY;	
	public String direction = "down";
	public int spriteNum = 1;
	public int dialogueSet = 0;
	public int dialogueIndex = 0;
	public boolean collisionOn = false;
	public boolean invincible = false;
	boolean attacking = false;
	public boolean alive = true;
	public boolean dying = false;
	boolean hpBarOn = false;
	boolean questMarkOn = false;
	boolean request = false;
	boolean drinking = false;
	public String job;
	
	//COUNTER
	public int spriteCounter = 0;
	public int actionLockCounter = 0;
	public int invincibleCounter = 0;
	public int shotAvailableCounter1 = 0;
	public int shotAvailableCounter2 = 0;
	public int shotAvailableCounter3 = 0;
	public int potionCounter = 0;
	int dyingCounter = 0;
	int hpBarCounter = 0;
	
	
	//CHARACTER ATTRIBUTES 캐릭터 요소
	public String name;		//이름
	public int speed;		//속도
	public int maxLife;		//최대 체력
	public int life;		//체력
	public int maxMana;		//최대 마나
	public int mana;		//마나
	public int exp;			//경험치
	public int nextLevelExp;//다음 경험치까지
	public int level;		//레벨
	public int strength;	//힘
	public int attack;		//공격력
	public int defence;		//방어력
	public int coin;		//재화 골드
	public Entity currentWeapon;//현재 무기
	public Entity currentShield;//현재 방어구
	public Projectile projectile;//스킬
	public Projectile skill1; //스킬 1
	public Projectile skill2; //스킬 2
	public Projectile skill3; //스킬 3
	public int skillLevel;	//스킬 레벨
	public String elements; //속성
	
	public int statePoint = 2;
	public int skillPoint = 2;
	
	public Projectile[] currentSkill = new Projectile[3];
	//ITEM ATTRIBUTES 아이템 요소
	public ArrayList<Entity> inventory = new ArrayList<>();
	public final int maxInventorySize = 20;
	public int value;
	public int attackValue;	//공격력 값(무기)
	public int defenceValue;//방어력 값(방어구)
	public String description = "";// 설명
	public int useCost;
	public int price;
	public boolean stackable = false;
	public int amount = 1;
	public String nameKr;
	
	//TYPE
	public int type; // 0 = player, 1 = npc, 2 = monster...
	public final int type_player = 0;
	public final int type_npc = 1;
	public final int type_monster = 2;
	public final int type_sword = 3;
	public final int type_shield = 4;
	public final int type_consumable = 5;
	public final int type_matter = 6;
	public final int type_quest_npc = 7;
	public final int type_major_npc = 9;
	public final int type_pickupOnly = 10;
	public final int type_obstacle = 11;
	
	public Entity(GamePanel gp) {
		this.gp = gp;
		try {
			questImage = ImageIO.read(getClass().getResourceAsStream("/object/questMark.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setAction() {}
	public void damageReaction() {}
	public void speak() {}
	//npc 대화 출력
	public void facePlayer() {

		//대화 시 방향 전환
		switch(gp.player.direction) {
		case "up": direction = "down";break;
		case "down": direction = "up";break;
		case "left": direction = "right";break;
		case "right": direction = "left";break;
		}
	}
	public void startDialogue(Entity entity, int setNum) {
		
		gp.gameState = gp.dialogueState;
		gp.ui.npc = entity;
		dialogueSet = setNum;
		
	}
	//업데이트
	public void update() {
		
		setAction();
		
		collisionOn = false;
		targetOn = false;
		gp.cChecker.checkTile(this);
		gp.cChecker.checkObject(this, false);
		gp.cChecker.checkEntity(this, gp.npc);
		gp.cChecker.checkEntity(this, gp.monster);
		gp.cChecker.checkMissileTarget(this, gp.monster);
		boolean contactPlayer = gp.cChecker.checkPlayer(this);
		
		if(this.type == 2 && contactPlayer == true) {
			if(gp.player.invincible == false) {
				//we can give damage
				gp.player.life -= 1;
				gp.player.invincible = true;
			}
		}
		
		//IF COLLISION IS FALSE, PLAYER CAN MOVE
		if(collisionOn == false) {
			switch(direction) {
			case "up": worldY -= speed; break;
			case "down": worldY += speed; break;
			case "left": worldX -= speed; break;
			case "right": worldX += speed; break;
			}
		}
		
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
		
		if(invincible == true) {
			invincibleCounter++;
			if(invincibleCounter > 20) {
				invincible = false;
				invincibleCounter = 0;
			}
		}
		
	}
	public void draw(Graphics2D g2) {
		
		BufferedImage image = null;
		int screenX = worldX - gp.player.worldX + gp.player.screenX;
		int screenY = worldY - gp.player.worldY + gp.player.screenY;
		
		if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
		   worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
		   worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
		   worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
						
			switch(direction) {
			case "up":
				if(spriteNum == 1) {image = up1;}
				if(spriteNum == 2) {image = up2;}
				if(spriteNum == 3) {image = up1;}
				break;
			case "down":
				if(spriteNum == 1) {image = down1;}
				if(spriteNum == 2) {image = down2;}	
				if(spriteNum == 3) {image = down1;}
				break;
			case "left":
				if(spriteNum == 1) {image = left1;}
				if(spriteNum == 2) {image = left2;}
				if(spriteNum == 3) {image = left1;}
				break;
			case "right":
				if(spriteNum == 1) {image = right1;}
				if(spriteNum == 2) {image = right2;}
				if(spriteNum == 3) {image = right1;}
				break;
			}
			
			//Monster HP Bar
			if(type == 2 && hpBarOn == true) {
				
				double oneScale = (double)gp.tileSize/maxLife;
				double hpBarValue = oneScale * life;
				
				//검은색 배경
				g2.setColor(new Color(35,35,35));
				g2.fillRect(screenX-1, screenY - 16, gp.tileSize+2, 12);
				//빨간색 hp바
				g2.setColor(new Color(255,0,30));
				g2.fillRect(screenX, screenY - 15, (int)hpBarValue, 10);
				
				hpBarCounter++;
				
				if(hpBarCounter > 600) {
					hpBarCounter = 0;
					hpBarOn = false;
				}
			}		
			
			if(type == type_quest_npc && questMarkOn == true) {
				g2.drawImage(questImage, screenX, screenY -  gp.tileSize - 10, gp.tileSize,gp.tileSize,null);
			}
			
			if(invincible == true) {
				hpBarOn = true;
				hpBarCounter = 0;
				changeAlpha(g2,0.4F);
			}			
			if(dying == true) {
				dyingAnimation(g2);
			}
			
			g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
			
			changeAlpha(g2,1F);
		}
	}
	public void dyingAnimation(Graphics2D g2) {
		
		dyingCounter++;
		
		if(dyingCounter <=5) {changeAlpha(g2,0f);}
		if(dyingCounter > 5 && dyingCounter <= 10) {changeAlpha(g2,1f);}
		if(dyingCounter > 10 && dyingCounter <= 15) {changeAlpha(g2,0f);}
		if(dyingCounter > 15 && dyingCounter <= 20) {changeAlpha(g2,1f);}
		if(dyingCounter > 20 && dyingCounter <= 25) {changeAlpha(g2,0f);}
		if(dyingCounter > 25 && dyingCounter <= 30) {changeAlpha(g2,1f);}
		if(dyingCounter > 30 && dyingCounter <= 35) {changeAlpha(g2,0f);}
		if(dyingCounter > 35 && dyingCounter <= 40) {changeAlpha(g2,1f);}
		if(dyingCounter > 40) {
			dying = false;
			alive = false;
		}
		
	}
	public void changeAlpha(Graphics2D g2, float alphaValue) {
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaValue));
	}

	public BufferedImage setup(String imagePath, int width, int height) {
		UtilityTool uTool = new UtilityTool();
		BufferedImage image = null;
		try {
			image = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
			image = uTool.scaleImage(image, width, height);
		}catch(IOException e) {
			e.printStackTrace();
		}
		return image;
	}

}
