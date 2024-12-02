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
import skill.Skill_DoubleFire;
import skill.Skill_Fireball;
import skill.Skill_Firefly;

public class Player extends Entity {

	KeyHandler keyH;
	public int screenX;
	public int screenY;
	int standCounter = 0;
	public boolean attackCanceled = false;
	public boolean timeFlow = false;
	public boolean respawnMon = false;

	public Player(GamePanel gp, KeyHandler keyH) {

		super(gp);
		this.keyH = keyH;

		screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
		screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

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
		setSkills();
		setDefaultPosition();
	}
	// 플레이어 관련 메소드

	public void setDefaultValues() {

		speed = 4;
		direction = "down";

		// PLAYER STATUS
		maxLife = 10;
		life = maxLife;
		maxMana = 20;
		mana = maxMana;
		level = 1;
		exp = 0;
		nextLevelExp = 5;
		strength = 1;
		coin = 0;

		elements = "불";
		currentWeapon = new OBJ_Weapon_Normal(gp);
		currentShield = new OBJ_Shield_Normal(gp);

		attack = getAttack();
		defence = getDefence();

	}

	public void setDefaultPosition() {
		worldX = gp.tileSize * 23;
		worldY = gp.tileSize * 22;
	}

	public void setDialogue() {

	}

	public void setItems() {
		inventory.add(currentWeapon);
		inventory.add(currentShield);
		inventory.add(new OBJ_HpPotion(gp));
		inventory.add(new OBJ_MpPotion(gp));
	}

	public void setSkills() {
		currentSkill[0] = new Skill_Fireball(gp);
		currentSkill[1] = new Skill_Firefly(gp);
		currentSkill[2] = new Skill_DoubleFire(gp);
		skill1 = currentSkill[0];
		skill2 = currentSkill[1];
		skill3 = currentSkill[2];
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
		for (int i = 0; i < inventory.size(); i++) {
			if (inventory.get(i) == currentWeapon) {
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
		try {
			standing = ImageIO.read(getClass().getResourceAsStream("/characterImg/Char_Stand_1.png"));
			// standing =
			// ImageIO.read(getClass().getResourceAsStream("/characterImg/test1.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		up1 = setup("/player/u1", gp.tileSize, gp.tileSize);
		up2 = setup("/player/u2", gp.tileSize, gp.tileSize);
		down1 = setup("/player/d1", gp.tileSize, gp.tileSize);
		down2 = setup("/player/d2", gp.tileSize, gp.tileSize);
		left1 = setup("/player/left1", gp.tileSize, gp.tileSize);
		left2 = setup("/player/left2", gp.tileSize, gp.tileSize);
		right1 = setup("/player/right1", gp.tileSize, gp.tileSize);
		right2 = setup("/player/right2", gp.tileSize, gp.tileSize);

	}

	public void getPlayerAttackImage() {

		att_up1 = setup("/player/u1", gp.tileSize, gp.tileSize * 2);
		att_up2 = setup("/player/u2", gp.tileSize, gp.tileSize * 2);
		att_down1 = setup("/player/d1", gp.tileSize, gp.tileSize * 2);
		att_down2 = setup("/player/d2", gp.tileSize, gp.tileSize * 2);
		att_left1 = setup("/player/u1", gp.tileSize * 2, gp.tileSize);
		att_left2 = setup("/player/u2", gp.tileSize * 2, gp.tileSize);
		att_right1 = setup("/player/d1", gp.tileSize * 2, gp.tileSize);
		att_right2 = setup("/player/d2", gp.tileSize * 2, gp.tileSize);

	}

	public void update() {
		checkDay();
		if (attacking == true) {
			attacking();
		}

		else if (keyH.upPressed == true || keyH.downPressed == true || keyH.leftPressed == true
				|| keyH.rightPressed == true || keyH.enterPressed == true || keyH.attackPressed == true) {
			if (keyH.upPressed == true) {
				direction = "up";
			} else if (keyH.downPressed == true) {
				direction = "down";
			} else if (keyH.leftPressed == true) {
				direction = "left";
			} else if (keyH.rightPressed == true) {
				direction = "right";
			}

			// CHECK TILE COLLISION
			collisionOn = false;
			gp.cChecker.checkTile(this);

			// CHECK OBJECT COLLISION
			int objIndex = gp.cChecker.checkObject(this, true);

			// CHECK NPC COLLISION
			int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
			interactNPC(npcIndex);

			// CHECK MONSTER COLLISION
			int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
			contactMonster(monsterIndex);

			gp.eHandler.checkEvent();

			// IF COLLISION IS FALSE, PLAYER CAN MOVE
			if (collisionOn == false && keyH.enterPressed == false) {
				switch (direction) {
				case "up":
					worldY -= speed;
					break;
				case "down":
					worldY += speed;
					break;
				case "left":
					worldX -= speed;
					break;
				case "right":
					worldX += speed;
					break;
				}
			}

			if (keyH.attackPressed == true && attackCanceled == false) {
				attacking = true;
				spriteCounter = 0;
			}

			attackCanceled = false;
			gp.keyH.enterPressed = false;

			spriteCounter++;
			if (spriteCounter > 12) {
				if (spriteNum == 1) {
					spriteNum = 2;
				} else if (spriteNum == 2) {
					spriteNum = 1;
				}
				spriteCounter = 0;
			}
		} else {
			standCounter++;
			if (standCounter == 20) {
				spriteNum = 1;
				standCounter = 0;
			}
		}

		if (gp.keyH.skill1Pressed == true && skill1.alive == false && shotAvailableCounter1 == 30) {

			if (mana - skill1.useCost < 0) {
				gp.ui.addMessage("마나 부족");
				shotAvailableCounter1 = 0;
			} else {
				mana -= skill1.useCost;
				skill1.set(worldX, worldY, direction, true, this);
				gp.projectileList.add(skill1);
				shotAvailableCounter1 = 0;
			}
		}

		else if (gp.keyH.skill2Pressed == true && skill2.alive == false && shotAvailableCounter2 == 30) {

			if (mana - skill2.useCost < 0) {
				gp.ui.addMessage("마나 부족");
				shotAvailableCounter2 = 0;
			} else {
				mana -= skill2.useCost;
				skill2.set(worldX + gp.tileSize, worldY + gp.tileSize, direction, true, this);
				gp.projectileList.add(skill2);
				skill2 = new Skill_Firefly(gp);
				skill2.set(worldX - gp.tileSize, worldY + gp.tileSize, direction, true, this);
				gp.projectileList.add(skill2);
				skill2 = new Skill_Firefly(gp);
				skill2.set(worldX, worldY - gp.tileSize, direction, true, this);
				gp.projectileList.add(skill2);

				shotAvailableCounter2 = 0;
			}

		}

		else if (gp.keyH.skill3Pressed == true && skill3.alive == false && shotAvailableCounter3 == 30) {
			if (mana - skill3.useCost < 0) {
				gp.ui.addMessage("마나 부족");
				// mana = 0;
				shotAvailableCounter3 = 0;
			} else {
				mana -= skill3.useCost;
				if (direction.equals("up")) {
					skill3.set(worldX, worldY - gp.tileSize / 2, direction, true, this);
					gp.projectileList.add(skill3);
				}
				if (direction.equals("down")) {
					skill3.set(worldX, worldY + gp.tileSize / 2, direction, true, this);
					gp.projectileList.add(skill3);
				}
				if (direction.equals("left")) {
					skill3.set(worldX - gp.tileSize / 2, worldY, direction, true, this);
					gp.projectileList.add(skill3);
				}
				if (direction.equals("right")) {
					skill3.set(worldX + gp.tileSize / 2, worldY, direction, true, this);
					gp.projectileList.add(skill3);
				}
				skill3 = new Skill_DoubleFire(gp);
				skill3.set(worldX, worldY, direction, true, this);

				gp.projectileList.add(skill3);

				shotAvailableCounter3 = 0;
			}
		}
		if (drinking == false && gp.keyH.slot1Pressed == true) {
			drinking = true;

			healing("HP Potion");
		}
		if (drinking == true) {
			potionCounter++;
			System.out.println("쿨타임:" + potionCounter);

		}
		if (gp.keyH.slot2Pressed == true) {
			potionCounter++;
			if (drinking == false) {
				drinking = true;
				healing("MP Potion");
			}
		}
		if (potionCounter > 20) {
			drinking = false;
			potionCounter = 0;
		}

		if (invincible == true) {
			invincibleCounter++;
			if (invincibleCounter > 60) {
				invincible = false;
				invincibleCounter = 0;
			}
		}
		if (shotAvailableCounter1 < 30) {
			shotAvailableCounter1++;
		}
		if (shotAvailableCounter2 < 30) {
			shotAvailableCounter2++;
		}
		if (shotAvailableCounter3 < 30) {
			shotAvailableCounter3++;
		}

		if (gp.player.respawnMon == true) {
			gp.aSetter.setMonster();
			gp.player.respawnMon = false;
		}
	}

	public void healing(String text) {
		if (searchItemInInventory(text) != 999) {

			if (text.equals("HP Potion")) {
				life += inventory.get(searchItemInInventory(text)).value;
				if (life > maxLife) {
					life = maxLife;
				}
			} else if (text.equals("MP Potion")) {
				mana += inventory.get(searchItemInInventory(text)).value;
				if (mana > maxMana) {
					mana = maxMana;
				}
			}
			System.out.println(inventory.get(searchItemInInventory(text)).amount);
			inventory.get(searchItemInInventory(text)).amount -= 1;
			gp.ui.addMessage(text + "사용");
			if (inventory.get(searchItemInInventory(text)).amount == 0) {
				inventory.remove(searchItemInInventory(text));
			}

		} else {
			gp.ui.addMessage(text + "없음");
		}
	}

	public void attacking() {

		spriteCounter++;

		if (spriteCounter <= 5) {
			spriteNum = 1;
		}
		if (spriteCounter > 5 && spriteCounter <= 25) {
			spriteNum = 2;

			// Save the current worldX, worldY, solidArea
			int currentWorldX = worldX;
			int currentWorldY = worldY;
			int solidAreaWidth = solidArea.width;
			int solidAreaHeight = solidArea.height;

			// Adjust player's worldX/Y for the attackAea
			switch (direction) {
			case "up":
				worldY -= attackArea.height;
				break;
			case "down":
				worldY += attackArea.height;
				break;
			case "left":
				worldX -= attackArea.width;
				break;
			case "right":
				worldX += attackArea.width;
				break;
			}
			// attackArea becomes solidArea
			solidArea.width = attackArea.width;
			solidArea.height = attackArea.height;
			// Check monster collision with the updated worldX, worldY, solidArea
			int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
			damageMonster(monsterIndex, attack);
			worldX = currentWorldX;
			worldY = currentWorldY;
			solidArea.width = solidAreaWidth;
			solidArea.height = solidAreaHeight;

		}
		if (spriteCounter > 25) {
			spriteNum = 1;
			spriteCounter = 0;
			attacking = false;

		}
	}

	public void pickUpObject(int i) {

		if (i != 999) {
			if (gp.obj[gp.currentMap][i].type == type_pickupOnly) {
				// gp.obj[gp.currentMap][i].use(this);
			} else if (gp.obj[gp.currentMap][i].type == type_obstacle) {
				if (keyH.enterPressed == true) {
					attackCanceled = true;
					// gp.obj[gp.currentMap][i].interact();
				}
			} else {
				String text;

				if (canObtainItem(gp.obj[gp.currentMap][i]) == true) {
					text = gp.obj[gp.currentMap][i].name + " 획득";
				} else {
					text = "인벤토리가 꽉 찼음";
				}
				gp.ui.addMessage(text);
				gp.obj[gp.currentMap][i] = null;
			}
		}
	}

	public void interactNPC(int i) {

		if (i != 999) {
			if (gp.keyH.enterPressed == true) {

				attackCanceled = true;
				gp.gameState = gp.dialogueState;
				gp.npc[gp.currentMap][i].speak();
			}
		}
	}

	public void contactMonster(int i) {

		if (i != 999) {
			if (invincible == false && gp.monster[gp.currentMap][i].dying == false) {
				int damage = gp.monster[gp.currentMap][i].attack - defence;
				if (damage < 0) {
					damage = 1; // 최소 데미지 1
				}

				life -= damage;
				invincible = true;
			}
		}
	}

	public void damageMonster(int i, int attack) {

		if (i != 999) {

			if (gp.monster[gp.currentMap][i].invincible == false) {

				int damage = attack - gp.monster[gp.currentMap][i].defence;
				if (damage <= 0) {
					damage = 1;
				}
				gp.monster[gp.currentMap][i].life -= damage;
				gp.ui.addMessage(damage + "의 데미지를 입힘.");

				gp.monster[gp.currentMap][i].invincible = true;
				gp.monster[gp.currentMap][i].damageReaction();

				if (gp.monster[gp.currentMap][i].life <= 0) {

					gp.monster[gp.currentMap][i].dying = true;
					exp += gp.monster[gp.currentMap][i].exp;
					gp.ui.addMessage(gp.monster[gp.currentMap][i].name + "을 쓰러트림.");
					checkQuest(i);
					checkLevelUp();
				}
			}
		}
	}

	public void damageElementMonster(int i, int attack, Entity entity) {
		if (i != 999) {

			//if (gp.monster[gp.currentMap][i].invincible == false) {

				int damage = attack - gp.monster[gp.currentMap][i].defence;
				if (damage <= 0) {
					damage = 1;
				}
				String eElement = gp.monster[gp.currentMap][i].elements;
				String damageE = entity.elements;
				if (checkElements(eElement, damageE) == true) {
					gp.monster[gp.currentMap][i].life -= damage * 2;
					gp.ui.addMessage("상성! " + damage * 2 + "의 데미지를 입힘.");
				} else if (checkElements(eElement, damageE) == false) {
					gp.monster[gp.currentMap][i].life -= damage;
					gp.ui.addMessage(damage + "의 데미지를 입힘.");
				}

				gp.monster[gp.currentMap][i].invincible = true;
				gp.monster[gp.currentMap][i].damageReaction();

				if (gp.monster[gp.currentMap][i].life <= 0) {

					gp.monster[gp.currentMap][i].dying = true;
					exp += gp.monster[gp.currentMap][i].exp;
					gp.ui.addMessage(gp.monster[gp.currentMap][i].name + "을 쓰러트림.");
					checkQuest(i);
					checkLevelUp();
				}
			//}
		}
	}

	public boolean checkElements(String enemy, String damage) {
		if (damage.equals("불")) {
			if (enemy.equals("바람")) {
				return true;
			}
		} else if (damage == "물") {
			if (enemy.equals("불")) {
				return true;
			}
		} else if (damage == "바람") {
			if (enemy.equals("흙")) {
				return true;
			}
		} else if (damage == "흙") {
			if (enemy.equals("물")) {
				return true;
			}
		}
		return false;
	}

	public void checkQuest(int i) {
		for (int j = 0; j < gp.questList.size(); j++) {
			if (gp.questList.get(j).name.equals(gp.monster[gp.currentMap][i].name)) {
				gp.questList.get(j).count += 1;
				if (gp.questList.get(j).count >= gp.questList.get(j).maxCount) {
					gp.questList.get(j).finish = true;
				}
			}
		}
	}

	public void rewardQuest(String name) {
		for (int i = 0; i < gp.questList.size(); i++) {
			if (gp.questList.get(i) != null) {
				if (gp.questList.get(i).finish == true && gp.questList.get(i).name.equals(name)) {

					exp += gp.questList.get(i).exp;
					coin += gp.questList.get(i).coin;
					checkLevelUp();
					gp.questList.remove(i);
				}
			}
		}
	}

	public void checkLevelUp() {
		if (exp >= nextLevelExp) {
			level++;
			exp = exp - nextLevelExp;
			nextLevelExp = nextLevelExp * 2;
			maxLife += 2;
			maxMana += 2;
			statePoint += 2;
			skillPoint += 2;
			gp.ui.addMessage("레벨업!");
		}
	}

	public int searchItemInInventory(String itemName) {

		int itemIndex = 999;
		for (int i = 0; i < inventory.size(); i++) {
			if (inventory.get(i).name.equals(itemName)) {
				itemIndex = i;
				break;
			}
		}
		return itemIndex;
	}

	public boolean canObtainItem(Entity item) {

		boolean canObtain = false;
		// CHECK IF STACKABLE
		if (item.stackable == true) {
			int index = searchItemInInventory(item.name);

			if (index != 999) {
				inventory.get(index).amount++;
				canObtain = true;
			} else {
				if (inventory.size() != maxInventorySize) {
					inventory.add(item);
					canObtain = true;
				}
			}
		} else {
			if (inventory.size() != maxInventorySize) {
				inventory.add(item);
				canObtain = true;
			}
		}
		return canObtain;
	}

	public void checkDay() {
		if (timeFlow == true) {
			gp.timeState += 1;
			if (gp.timeState == 3) {
				gp.eHandler.teleport(0, 23, 22);
			}
			if (gp.timeState > 3) {
				gp.dayCount += 1;
				gp.timeState = 0;
			}
			timeFlow = false;
		}

	}

	public void draw(Graphics2D g2) {

		BufferedImage image = null;
		int tempScreenX = screenX;
		int tempScreenY = screenY;

		switch (direction) {
		case "up":
			if (attacking == false) {
				if (spriteNum == 1) {
					image = up1;
				}
				if (spriteNum == 2) {
					image = up2;
				}
			}
			if (attacking == true) {
				tempScreenY = screenY - gp.tileSize;
				if (spriteNum == 1) {
					image = att_up1;
				}
				if (spriteNum == 2) {
					image = att_up2;
				}
			}
			break;
		case "down":
			if (attacking == false) {
				if (spriteNum == 1) {
					image = down1;
				}
				if (spriteNum == 2) {
					image = down2;
				}
			}
			if (attacking == true) {
				if (spriteNum == 1) {
					image = att_down1;
				}
				if (spriteNum == 2) {
					image = att_down2;
				}
			}
			break;
		case "left":
			if (attacking == false) {
				if (spriteNum == 1) {
					image = left1;
				}
				if (spriteNum == 2) {
					image = left2;
				}
			}
			if (attacking == true) {
				tempScreenX = screenX - gp.tileSize;
				if (spriteNum == 1) {
					image = att_left1;
				}
				if (spriteNum == 2) {
					image = att_left2;
				}
			}
			break;
		case "right":
			if (attacking == false) {
				if (spriteNum == 1) {
					image = right1;
				}
				if (spriteNum == 2) {
					image = right2;
				}
			}
			if (attacking == true) {
				if (spriteNum == 1) {
					image = att_right1;
				}
				if (spriteNum == 2) {
					image = att_right2;
				}
			}
			break;
		}

		// 데이지 입었을 때 투명화
		if (invincible == true) {
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
		}

		g2.drawImage(image, tempScreenX, tempScreenY, null);

		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

	}
}
