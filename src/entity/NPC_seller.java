package entity;

import java.util.Random;

import main.GamePanel;
import object.OBJ_HpPotion;
import object.OBJ_MpPotion;

public class NPC_seller extends Entity{
	public NPC_seller(GamePanel gp) {
		super(gp);
		
		direction = "down";
		speed = 0;
		job = "shop";
		
		solidArea.x = 8;
		solidArea.y = 16;
		solidArea.width = 32;
		solidArea.height = 32;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		
		getImage();
		setDialogue();
		setItems();
	}
	public void getImage() {
		
//		up1 = setup("/player/testUp",gp.tileSize,gp.tileSize);
//		up2 = setup("/player/testUp",gp.tileSize,gp.tileSize);
//		down1 = setup("/player/testDown",gp.tileSize,gp.tileSize);
//		down2 = setup("/player/testDown",gp.tileSize,gp.tileSize);
//		left1 = setup("/player/testLeft",gp.tileSize,gp.tileSize);
//		left2 = setup("/player/testLeft",gp.tileSize,gp.tileSize);
//		right1 = setup("/player/testRight",gp.tileSize,gp.tileSize);
//		right2 = setup("/player/testRight",gp.tileSize,gp.tileSize);
		down1 = setup("/player/testRight",gp.tileSize,gp.tileSize);
		down2 = setup("/player/testRight",gp.tileSize,gp.tileSize);

	}
	public void setDialogue() {
		dialogues[0] = "안녕";
		
	}
	public void setItems() {
		inventory.add(new OBJ_HpPotion(gp));
		inventory.add(new OBJ_MpPotion(gp));
	}
	public void speak() {
		
		super.speak();
		gp.gameState = gp.tradeState;
		gp.ui.npc = this;
	}
	
}
