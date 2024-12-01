package entity;

import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import main.GamePanel;
import quest.Q_Kill_Slime;

public class NPC_test extends Entity{
	public NPC_test(GamePanel gp) {
		super(gp);
		
		direction = "down";
		speed = 1;
		type = type_major_npc;
		
		solidArea.x = 8;
		solidArea.y = 16;
		solidArea.width = 32;
		solidArea.height = 32;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		
		getImage();
		setDialogue();
		
	}
	public void getImage() {
		try {
			standing = ImageIO.read(getClass().getResourceAsStream("/characterImg/Char_Stand_2.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		up1 = setup("/player/testUp",gp.tileSize,gp.tileSize);
		up2 = setup("/player/testUp",gp.tileSize,gp.tileSize);
		down1 = setup("/player/testDown",gp.tileSize,gp.tileSize);
		down2 = setup("/player/testDown",gp.tileSize,gp.tileSize);
		left1 = setup("/player/testLeft",gp.tileSize,gp.tileSize);
		left2 = setup("/player/testLeft",gp.tileSize,gp.tileSize);
		right1 = setup("/player/testRight",gp.tileSize,gp.tileSize);
		right2 = setup("/player/testRight",gp.tileSize,gp.tileSize);

	}
	public void setDialogue() {
		
//		dialogues[0] = "Hello.";
//		dialogues[1] = "Nice to meet you.";
//		dialogues[2] = "Have you ever seen my item? \nIt looks like key.";
//		dialogues[3] = "Good. Have a nice day.";
		dialogues[0] = "안녕?";
		dialogues[1] = "만나서 반가워."; 

		
	}
	public void setAction() {
		
		actionLockCounter++;
		
		if(actionLockCounter == 120) {
			
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
	public void speak() {
		
		super.speak();

	}
	
}
