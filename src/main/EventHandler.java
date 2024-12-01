package main;

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
			if(hit(0,13,12,"any") == true) { teleport(1,34,12); }//house01->house02
			if(hit(0,14,12,"any") == true) { teleport(1,34,12); }//house01->house02
			else if(hit(1,35,12,"any") == true) { teleport(0,15,12); }//house02->house01
			else if(hit(1,36,12,"any") == true) { teleport(0,15,12); }//house02->house01
			
			else if(hit(1,23,37,"any") == true) { teleport(2,23,28); }//house02->world01
			else if(hit(2,23,27,"any") == true) { teleport(1,23,36); }//world01->house02
			
			else if(hit(2,5,21,"any") == true) { teleport(3,43,21); }//world01->field01
			else if(hit(3,44,21,"any") == true) { gp.timeState += 1;
													teleport(2,6,21); }//field01->world01
			
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
	public void healingPool(int col, int row, int gameState) {
		if(gp.keyH.enterPressed == true) {
			gp.gameState = gameState;
			gp.player.life = gp.player.maxLife;
			gp.player.mana = gp.player.maxMana;
//			eventRect[map][col][row].eventDone = true; // 이벤트가 한번 발생 하면 끝
//			canTouchEvent = false; // 이벤트 박스에 한번 닿으면 밖으로 나올 때까지 닿을 수 없음
		}
	}
	public void teleport(int map, int col, int row) {
		
		gp.gameState = gp.transitionState;
		tempMap = map;
		tempCol = col;
		tempRow = row;
		
		
		canTouchEvent = false;
	}
	
}
