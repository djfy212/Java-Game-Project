package monster;

import java.util.Random;

import entity.Entity;
import main.GamePanel;

public class Mon_Butterfly extends Entity{
	GamePanel gp;
	
	public Mon_Butterfly(GamePanel gp) {
		super(gp);
		
		this.gp = gp;
		
		type = type_monster;
		name = "나비";
		speed = 2;
		maxLife = 5;
		life = maxLife;
		attack = 1;
		defence = 1;
		exp = 2;
		elements = "바람";
		
		solidArea.x = 3;
		solidArea.y = 18;
		solidArea.width = 42;
		solidArea.height = 30;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		
		getImage(gp);
	}
	public void getImage(GamePanel gp) {
		up1 = setup("/monster/butterfly_down1",gp.tileSize,gp.tileSize);
		up2 = setup("/monster/butterfly_down2",gp.tileSize,gp.tileSize);
		down1 = setup("/monster/butterfly_down1",gp.tileSize,gp.tileSize);
		down2 = setup("/monster/butterfly_down2",gp.tileSize,gp.tileSize);
		left1 = setup("/monster/butterfly_down1",gp.tileSize,gp.tileSize);
		left2 = setup("/monster/butterfly_down2",gp.tileSize,gp.tileSize);
		right1 = setup("/monster/butterfly_down1",gp.tileSize,gp.tileSize);
		right2 = setup("/monster/butterfly_down2",gp.tileSize,gp.tileSize);
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
	public void damageReaction() {
		
		actionLockCounter = 0;
		direction = gp.player.direction;
	}
}
