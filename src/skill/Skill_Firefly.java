package skill;

import data.GuidedMissileSimulation;
import entity.Entity;
import entity.Projectile;
import main.GamePanel;

public class Skill_Firefly extends Projectile{
	GamePanel gp;
	public GuidedMissileSimulation missile;

	public Skill_Firefly(GamePanel gp) {
		super(gp);
		this.gp = gp;
		
		name = "반딧불이";
		speed = 10;
		maxLife = 100;
		life = maxLife;
		attack = 1;
		useCost = 3;
		skillLevel = 1;
		description = "[" +name+ "] \n 주변에 불꽃을 3개 생산하여\n각각"+attack * skillLevel+ "+공격력 만큼의데미지를 입힌다.\n소비 마나: "+useCost;
		alive = false;
		elements = "불";
		getImage();
	}
	public void getImage() {
		icon = setup("/projectile/firefly_icon",gp.tileSize,gp.tileSize);
		up1 = setup("/projectile/firefly01",gp.tileSize,gp.tileSize);
		up2 = setup("/projectile/firefly01",gp.tileSize,gp.tileSize);
		down1 = setup("/projectile/firefly01",gp.tileSize,gp.tileSize);
		down2 = setup("/projectile/firefly01",gp.tileSize,gp.tileSize);
		left1 = setup("/projectile/firefly01",gp.tileSize,gp.tileSize);
		left2 = setup("/projectile/firefly01",gp.tileSize,gp.tileSize);
		right1 = setup("/projectile/firefly01",gp.tileSize,gp.tileSize);
		right2 = setup("/projectile/firefly01",gp.tileSize,gp.tileSize);
	}
	
	@Override
	public void update() {
		if(user == gp.player) {
			int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
			if(monsterIndex != 999) {
				gp.player.damageElementMonster(monsterIndex, attack + gp.player.attack,this);
				alive = false;
			}
		}
		//CHECK TARGET IN MY SKILL AREA
		int targetIndex = gp.cChecker.checkMissileTarget(gp.player, gp.monster);
		
		if(targetIndex != 999) {
			
			missile = new GuidedMissileSimulation();				
			this.worldX = (int)missile.getX(gp.monster[gp.currentMap][targetIndex].worldX,this.worldX);
			this.worldY = (int)missile.getY(gp.monster[gp.currentMap][targetIndex].worldY,this.worldY);			
						
		}
		if(targetIndex == 999) {
			if (gp.keyH.upPressed == true || gp.keyH.downPressed == true || gp.keyH.leftPressed == true
					|| gp.keyH.rightPressed == true) {
				switch (gp.player.direction) {
				case "up":
					worldY -= gp.player.speed;
					break;
				case "down":
					worldY += gp.player.speed;
					break;
				case "left":
					worldX -= gp.player.speed;
					break;
				case "right":
					worldX += gp.player.speed;
					break;
				}
			}
		}				

		life--;
		if(life <= 0) {
			alive = false;
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
				
	}
}
