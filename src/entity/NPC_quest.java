package entity;

import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import main.GamePanel;
import quest.Q_Kill_Slime;

public class NPC_quest extends Entity{
	
	Q_Kill_Slime quest = new Q_Kill_Slime(gp);
	
	public NPC_quest(GamePanel gp) {
		super(gp);
		
		direction = "down";
		speed = 0;
		type = type_quest_npc;
		questMarkOn = true;
		request = false;
		
		
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
		
		if(request == false) {
			dialogues[0] = "슬라임을 잡아주세요."; 
		}
		else{
			dialogues[0] = "감사합니다."; 
		}
		
		
	}
	public void speak() {
		
		setDialogue();
		super.speak();
		if(gp.player.speaking == false) {
			
			if(request == false) {			
				gp.questList.add(new Q_Kill_Slime(gp));
				questMarkOn = false;
				request = true;
			}
			else {
				gp.player.rewardQuest(quest.name);
				questMarkOn = true;
				request = false;
			}
		}
	}
}
