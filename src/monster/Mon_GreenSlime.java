package monster;

import java.util.Random;

import entity.Entity;
import main.GamePanel;

public class Mon_GreenSlime extends Entity{
	GamePanel gp;
	
	public Mon_GreenSlime(GamePanel gp) {
		super(gp);
		
		this.gp = gp;
		
		type = type_monster;
		name = "그린 슬라임";
		speed = 1;
		maxLife = 10;
		life = maxLife;
		attack = 2;
		defence = 1;
		exp = 2;
		elements = "흙";
		
		solidArea.x = 3;
		solidArea.y = 18;
		solidArea.width = 42;
		solidArea.height = 30;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		
		getImage(gp);
	}
	public void getImage(GamePanel gp) {
		up1 = setup("/monster/greenslime_down1",gp.tileSize,gp.tileSize);
		up2 = setup("/monster/greenslime_down2",gp.tileSize,gp.tileSize);
		down1 = setup("/monster/greenslime_down1",gp.tileSize,gp.tileSize);
		down2 = setup("/monster/greenslime_down2",gp.tileSize,gp.tileSize);
		left1 = setup("/monster/greenslime_down1",gp.tileSize,gp.tileSize);
		left2 = setup("/monster/greenslime_down2",gp.tileSize,gp.tileSize);
		right1 = setup("/monster/greenslime_down1",gp.tileSize,gp.tileSize);
		right2 = setup("/monster/greenslime_down2",gp.tileSize,gp.tileSize);
	}
	public void setAction() {
		
		actionLockCounter++;
		if(actionLockCounter == 100) {
			
			Random random = new Random();
			int i = random.nextInt(100) + 1;
			
			if (i <= 25) {
				direction = "up";
			}
			if (i > 25 && i <= 50) {
				direction = "down";
			}
			if (i > 50 && i <= 75) {
				direction = "left";
			}
			if (i > 75 && i <= 100) {
				direction = "right";
			}
			
			actionLockCounter = 0;
		}
		
	}
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
				spriteNum = 3;
			}
			else if(spriteNum == 3) {
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
	public void damageReaction() {
		
		actionLockCounter = 0;
		direction = gp.player.direction;
	}
}
