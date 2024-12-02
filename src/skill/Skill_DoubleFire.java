package skill;

import entity.Projectile;
import main.GamePanel;

public class Skill_DoubleFire extends Projectile{
	GamePanel gp;
	
	public Skill_DoubleFire(GamePanel gp) {
		super(gp);
		this.gp = gp;
		
		name = "더블 파이어";
		speed = 10;
		maxLife = 60;
		life = maxLife;
		attack = 2;
		useCost = 3;
		skillLevel = 1;
		description = "[" +name+ "] \n전방에 파이어볼을 2개 발사하여\n각각 "+attack * skillLevel + "+ 공격력 만큼의 데미지를 입힌다.\n소비 마나: "+useCost;
		alive = false;
		elements = "불";
		getImage();
	}
	public void getImage() {
		icon = setup("/projectile/doublefire_icon",gp.tileSize,gp.tileSize);
		up1 = setup("/projectile/fireball_up",gp.tileSize,gp.tileSize);
		up2 = setup("/projectile/fireball_up",gp.tileSize,gp.tileSize);
		down1 = setup("/projectile/fireball_down",gp.tileSize,gp.tileSize);
		down2 = setup("/projectile/fireball_down",gp.tileSize,gp.tileSize);
		left1 = setup("/projectile/fireball_left",gp.tileSize,gp.tileSize);
		left2 = setup("/projectile/fireball_left",gp.tileSize,gp.tileSize);
		right1 = setup("/projectile/fireball_right",gp.tileSize,gp.tileSize);
		right2 = setup("/projectile/fireball_right",gp.tileSize,gp.tileSize);

	}
}
