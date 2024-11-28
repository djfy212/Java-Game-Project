package skill;

import entity.Projectile;
import main.GamePanel;

public class Skill_Fireball extends Projectile{
	GamePanel gp;
	
	public Skill_Fireball(GamePanel gp) {
		super(gp);
		this.gp = gp;
		
		name = "파이어 볼";
		speed = 10;
		maxLife = 80;
		life = maxLife;
		attack = 2;
		useCost = 1;
		alive = false;
		getImage();
	}
	public void getImage() {
//		up1 = setup("/projectile/fireball_up_1",gp.tileSize,gp.tileSize);
//		up2 = setup("/projectile/fireball_up_2",gp.tileSize,gp.tileSize);
//		down1 = setup("/projectile/fireball_down_1",gp.tileSize,gp.tileSize);
//		down2 = setup("/projectile/fireball_down_2",gp.tileSize,gp.tileSize);
//		left1 = setup("/projectile/fireball_left_1",gp.tileSize,gp.tileSize);
//		left2 = setup("/projectile/fireball_left_2",gp.tileSize,gp.tileSize);
//		right1 = setup("/projectile/fireball_right_1",gp.tileSize,gp.tileSize);
//		right2 = setup("/projectile/fireball_right_2",gp.tileSize,gp.tileSize);
		up1 = setup("/tiles/black",gp.tileSize,gp.tileSize);
		up2 = setup("/tiles/black",gp.tileSize,gp.tileSize);
		down1 = setup("/tiles/black",gp.tileSize,gp.tileSize);
		down2 = setup("/tiles/black",gp.tileSize,gp.tileSize);
		left1 = setup("/tiles/black",gp.tileSize,gp.tileSize);
		left2 = setup("/tiles/black",gp.tileSize,gp.tileSize);
		right1 = setup("/tiles/black",gp.tileSize,gp.tileSize);
		right2 = setup("/tiles/black",gp.tileSize,gp.tileSize);
	}

}
