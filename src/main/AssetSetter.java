package main;

import entity.NPC_quest;
import entity.NPC_seller;
import entity.NPC_test;
import monster.Mon_Butterfly;
import monster.Mon_GreenSlime;
//import object.OBJ_Box;
//import object.OBJ_Door;

public class AssetSetter {
	
	GamePanel gp;
	public AssetSetter(GamePanel gp) {
		this.gp = gp;
	}
	//오브젝트 세팅
	public void setObject() {
		
//		int mapNum = 0;
//		int i=0;
//		gp.obj[mapNum][i] = new OBJ_Box(gp);
//		gp.obj[mapNum][i].worldX = 8 * gp.tileSize;
//		gp.obj[mapNum][i].worldY = 8 * gp.tileSize;
//		i++;
	}
	//NPC세팅
	public void setNPC() {
		
		int mapNum = 1;
		int i=0;
		gp.npc[mapNum][i] = new NPC_test(gp);
		gp.npc[mapNum][i].worldX = gp.tileSize*25;
		gp.npc[mapNum][i].worldY = gp.tileSize*15;
		i++;
		mapNum = 2;
		i=0;
		gp.npc[mapNum][i] = new NPC_seller(gp);
		gp.npc[mapNum][i].worldX = gp.tileSize*15;
		gp.npc[mapNum][i].worldY = gp.tileSize*15;
		i++;
		gp.npc[mapNum][i] = new NPC_quest(gp);
		gp.npc[mapNum][i].worldX = gp.tileSize*14;
		gp.npc[mapNum][i].worldY = gp.tileSize*27;
	}
	//몬스터 세팅
	public void setMonster() {
		
		int mapNum = 3;
		int i=0;
		gp.monster[mapNum][i] = new Mon_GreenSlime(gp);
		gp.monster[mapNum][i].worldX = gp.tileSize*7;
		gp.monster[mapNum][i].worldY = gp.tileSize*10;
		i++;
		gp.monster[mapNum][i] = new Mon_GreenSlime(gp);
		gp.monster[mapNum][i].worldX = gp.tileSize*15;
		gp.monster[mapNum][i].worldY = gp.tileSize*17;
		i++;
		gp.monster[mapNum][i] = new Mon_GreenSlime(gp);
		gp.monster[mapNum][i].worldX = gp.tileSize*20;
		gp.monster[mapNum][i].worldY = gp.tileSize*20;
		i++;
		gp.monster[mapNum][i] = new Mon_GreenSlime(gp);
		gp.monster[mapNum][i].worldX = gp.tileSize*28;
		gp.monster[mapNum][i].worldY = gp.tileSize*20;
		i++;
		gp.monster[mapNum][i] = new Mon_GreenSlime(gp);
		gp.monster[mapNum][i].worldX = gp.tileSize*40;
		gp.monster[mapNum][i].worldY = gp.tileSize*30;
		i++;
		gp.monster[mapNum][i] = new Mon_Butterfly(gp);
		gp.monster[mapNum][i].worldX = gp.tileSize*30;
		gp.monster[mapNum][i].worldY = gp.tileSize*17;
		i++;
		gp.monster[mapNum][i] = new Mon_Butterfly(gp);
		gp.monster[mapNum][i].worldX = gp.tileSize*19;
		gp.monster[mapNum][i].worldY = gp.tileSize*10;
		i++;
	}
	
}
