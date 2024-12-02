package main;

import entity.Entity;

public class EventHandler {

	GamePanel gp;
	EventRect eventRect[][][];
	
	int previousEventX, previousEventY;
	boolean canTouchEvent = true;
	int tempMap, tempCol, tempRow;
	
	public EventHandler(GamePanel gp) {
		this.gp = gp;
		
		eventRect = new EventRect[gp.maxMap][gp.maxWorldCol][gp.maxWorldRow];
		int map = 0;
		int col = 0;
		int row = 0;
		while(map < gp.maxMap && col < gp.maxWorldCol && row < gp.maxWorldRow) {
			eventRect[map][col][row] = new EventRect();
			eventRect[map][col][row].x = 0;
			eventRect[map][col][row].y = 0;
			eventRect[map][col][row].width = gp.tileSize;
			eventRect[map][col][row].height = gp.tileSize;
			eventRect[map][col][row].eventRectDefaultX = eventRect[map][col][row].x;
			eventRect[map][col][row].eventRectDefaultY = eventRect[map][col][row].y;
			
			col++;
			if(col == gp.maxWorldCol) {
				col = 0;
				row++;		
				
				if(row == gp.maxWorldRow) {
					row = 0;
					map++;
				}
			}
		}
	}
	
	public void checkEvent() {
		
		// Check if the player character is more than 1 tile away from the last event
		int xDistance = Math.abs(gp.player.worldX - previousEventX);
		int yDistance = Math.abs(gp.player.worldY - previousEventY);
		int distance = Math.max(xDistance, yDistance);
		if(distance > gp.tileSize) {
			canTouchEvent = true;
		}
		if(canTouchEvent == true) {
			if(hit(0,20,26,"any") == true) { teleport(1,34,12); }//house01->house02
			if(hit(0,21,26,"any") == true) { teleport(1,34,12); }//house01->house02
			else if(hit(1,35,12,"any") == true) { teleport(0,22,26); }//house02->house01
			else if(hit(1,36,12,"any") == true) { teleport(0,22,26); }//house02->house01
			
			else if(hit(1,23,37,"any") == true) { teleport(2,23,28); }//house02->world01
			else if(hit(1,24,37,"any") == true) { teleport(2,23,28); }//house02->world01
			
			else if(hit(2,23,27,"any") == true) { teleport(1,23,36); }//world01->house02
			
			else if(hit(2,8,26,"left") == true) { gp.player.respawnMon = true;
													teleport(3,40,26); }//world01->field01
			else if(hit(2,8,27,"left") == true) { gp.player.respawnMon = true;
													teleport(3,40,27); }//world01->field01
			else if(hit(2,8,28,"left") == true) { gp.player.respawnMon = true;
													teleport(3,40,28); }//world01->field01
			
			else if(hit(3,41,26,"any") == true) { gp.player.timeFlow = true;
													teleport(2,9,26); }//field01->world01
			else if(hit(3,41,27,"any") == true) { gp.player.timeFlow = true;
													teleport(2,9,27); }//field01->world01
			else if(hit(3,41,28,"any") == true) { gp.player.timeFlow = true;
													teleport(2,9,28); }//field01->world01
			
			else if(hit(2,15,16,"any") == true) {speak(gp.npc[2][0]);}
			else if(hit(0,23,20,"right") == true) { healing(); }//침대 회복
			else if(hit(0,23,21,"right") == true) { healing(); }//침대 회복
		}
		
	}
	public boolean hit(int map, int col, int row, String reqDirection) {
		
		boolean hit = false;
		
		if(map == gp.currentMap) {
			gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
			gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;
			eventRect[map][col][row].x = col * gp.tileSize + eventRect[map][col][row].x;
			eventRect[map][col][row].y = row * gp.tileSize + eventRect[map][col][row].y;

			if (gp.player.solidArea.intersects(eventRect[map][col][row])
					&& eventRect[map][col][row].eventDone == false) {
				if (gp.player.direction.contentEquals(reqDirection) || reqDirection.contentEquals("any")) {
					hit = true;

					previousEventX = gp.player.worldX;
					previousEventY = gp.player.worldY;
				}
			}
			gp.player.solidArea.x = gp.player.solidAreaDefaultX;
			gp.player.solidArea.y = gp.player.solidAreaDefaultY;
			eventRect[map][col][row].x = eventRect[map][col][row].eventRectDefaultX;
			eventRect[map][col][row].y = eventRect[map][col][row].eventRectDefaultY;
		}
				
		
		return hit;
	}
	public void healing() {
		if(gp.keyH.enterPressed == true) {
			gp.player.timeFlow = true;
			gp.player.life = gp.player.maxLife;
			gp.player.mana = gp.player.maxMana;
			gp.ui.addMessage("휴식");
		}
	}
	public void teleport(int map, int col, int row) {
		
		gp.gameState = gp.transitionState;
		tempMap = map;
		tempCol = col;
		tempRow = row;
		
		
		canTouchEvent = false;
	}
	
	public void speak(Entity entity) {
		if(gp.keyH.enterPressed == true) {
			gp.gameState = gp.tradeState;
			gp.player.attackCanceled = true;
			entity.speak();
		}
	}
}
