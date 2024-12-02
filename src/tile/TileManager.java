package tile;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.UtilityTool;

public class TileManager {
	
	GamePanel gp;
	public Tile[] tile;
	public int mapTileNum [][][];
		
	public TileManager(GamePanel gp) {
		this.gp = gp;
		
		tile = new Tile[200];
		mapTileNum = new int[gp.maxMap][gp.maxWorldCol][gp.maxWorldRow];
		
		getTileImage();
		loadMap("/maps/house01.txt",0);
		loadMap("/maps/house02.txt",1);
		loadMap("/maps/world01.txt",2);
		loadMap("/maps/map1.txt",3);
	}
	//타일 이미지 불러오기
	public void getTileImage() {
			
		setup(0, "grass", false);
		setup(1, "wall", true);
		setup(2, "water", true);
		setup(3, "black", false);
		setup(4, "black", true);
		setup(110, "home_10", true);
		setup(112, "home_12", false);
		setup(113, "home_13", false);
		setup(114, "home_14", true);
		setup(115, "home_15", true);
		setup(116, "home_16", true);

		setup(120, "home_20", true);
		setup(121, "home_21", true);
		setup(122, "home_22", true);
		setup(123, "home_23", true);
		setup(124, "home_24", true);
		setup(125, "home_25", true);
		setup(130, "home_30", true);
		setup(131, "home_31", true);		
		setup(132, "home_32", true);
		setup(133, "home_33", true);
		setup(134, "home_34", true);
		setup(135, "home_35", true);
		setup(140, "home_40", false);
		setup(141, "home_41", true);
		setup(142, "home_42", true);
		setup(145, "home_45", true);
		setup(146, "home_46", true);
		setup(147, "home_47", true);
		setup(148, "home_48", false);
		setup(150, "home_50", false);
		setup(151, "home_51", false);
		
		setup(22, "forest_0", true);
		setup(23, "forest_1", true);
		setup(24, "forest_2", true);
		setup(25, "forest_3", true);
		setup(26, "forest_4", true);
		setup(27, "forest_5", true);
		setup(28, "forest_6", true);
		setup(29, "forest_7", true);
//		setup(30, "forest_8", true);
		setup(31, "grass", false);
		setup(81, "foresti_1", true);
		setup(82, "foresti_2", true);
		setup(83, "foresti_3", true);
		setup(84, "foresti_4", true);
		setup(85, "foresti_5", true);

	}
	public void setup(int index, String imageName, boolean collision) {
		
		UtilityTool uTool = new UtilityTool();
		try {
			tile[index] = new Tile();
			tile[index].image = ImageIO.read(getClass().getResourceAsStream("/tiles/" + imageName + ".png"));
			tile[index].image = uTool.scaleImage(tile[index].image, gp.tileSize, gp.tileSize);
			tile[index].collision = collision;
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	//맵 배열 생성
	public void loadMap(String filePath, int map) {
		try {
			InputStream is = getClass().getResourceAsStream(filePath);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			int col = 0;
			int row = 0;
			
			while(col < gp.maxWorldCol && row<gp.maxWorldRow) {
				
				String line = br.readLine();
				
				while(col < gp.maxWorldCol) {
					
					String numbers[] = line.split(" ");
					
					int num = Integer.parseInt(numbers[col]);
					
					mapTileNum[map][col][row] = num;
					col++;
				}
				if(col == gp.maxWorldCol) {
					col = 0;
					row++;
				}			
			}
			br.close();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void draw(Graphics2D g2) {
				
		int worldCol = 0;
		int worldRow = 0;
		
		while (worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow){
			
			int tileNum = mapTileNum[gp.currentMap][worldCol][worldRow];
			
			int worldX = worldCol * gp.tileSize;
			int worldY = worldRow * gp.tileSize;
			int screenX = worldX - gp.player.worldX + gp.player.screenX;
			int screenY = worldY - gp.player.worldY + gp.player.screenY;
			
			if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
			   worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
			   worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
			   worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
				
				if(tile[tileNum] != null) {
					g2.drawImage(tile[tileNum].image, screenX, screenY, null);
				}
				
				
			}
			worldCol++;
			
			if(worldCol == gp.maxWorldCol) {
				worldCol = 0;
				worldRow++;
			}
		}
	}
}
