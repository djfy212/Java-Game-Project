package data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import entity.Entity;
import main.GamePanel;
import object.OBJ_HpPotion;
import object.OBJ_MpPotion;
import object.OBJ_Shield_Normal;
import object.OBJ_Weapon_Normal;

public class SaveLoad {

	GamePanel gp;
	
	public SaveLoad(GamePanel gp) {
		this.gp = gp;
	}
	public Entity getObject(String itemName) {
		
		Entity obj = null;		
		
		switch(itemName) {
		case"HP Potion" : obj = new OBJ_HpPotion(gp); break;
		case"MP Potion" : obj = new OBJ_MpPotion(gp); break;
		case"평범한 옷" : obj = new OBJ_Shield_Normal(gp); break;
		case"평범한 지팡이" : obj = new OBJ_Weapon_Normal(gp); break;
		}
		
		return obj;
	}
	
	public void save() {
		
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("save.dat")));
			
			DataStorage ds = new DataStorage();

			
			ds.level = gp.player.level;
			ds.maxLife = gp.player.maxLife;
			ds.life = gp.player.life;
			ds.maxMana = gp.player.maxMana;
			ds.mana = gp.player.mana;
			ds.strength = gp.player.strength;
			ds.exp = gp.player.exp;
			ds.nextLevelExp = gp.player.nextLevelExp;
			ds.coin = gp.player.coin;
			
			ds.currentMap = gp.currentMap;
			ds.timeCount = gp.timeCount;	
			ds.timeState = gp.timeState;
			ds.dayCount = gp.dayCount;
			
			// 플레이어 인벤토리
			for (int i = 0; i < gp.player.inventory.size(); i++) {
				ds.itemNames.add(gp.player.inventory.get(i).name);
				ds.itemAmounts.add(gp.player.inventory.get(i).amount);	
			}

			// 플레이어 장비
			ds.currentWeaponSlot = gp.player.getCurrentWeaponSlot();
			ds.currentShieldSlot = gp.player.getCurrentShieldSlot();
			
			//OBJECT ON MAP
			ds.mapObjectNames = new String[gp.maxMap][gp.obj[1].length];
			ds.mapObjectWorldX = new int[gp.maxMap][gp.obj[1].length];
			ds.mapObjectWorldY = new int[gp.maxMap][gp.obj[1].length];
			ds.mapObjectLootNames = new String[gp.maxMap][gp.obj[1].length];
			ds.mapObjectOpend = new boolean[gp.maxMap][gp.obj[1].length];
			
//			for(int mapNum = 0; mapNum <gp.maxMap; mapNum++) {
//				for(int i=0;i<gp.obj[1].length;i++) {
//					if(gp.obj[mapNum][i] != null) {
//						ds.mapObjectNames[mapNum][i] = "NA";
//					}
//					else {
////						ds.mapObjectNames[mapNum][i] = gp.obj[mapNum][i].name;
//						ds.mapObjectWorldX[mapNum][i] = gp.obj[mapNum][i].worldX;
//						ds.mapObjectWorldY[mapNum][i] = gp.obj[mapNum][i].worldY;
////						if(gp.obj[mapNum][i].loot != null) {
////							ds.mapObjectLootNames[mapNum][i] = gp.obj[mapNum][i].loot.name;
////						}
////						ds.mapObjectOpened[mapNum][i] = gp.obj[mapNum][i].opened;
//					}
//				}
//			}
			
			// Write the DataStorage object
			oos.writeObject(ds);
			System.out.println("save 완료");
		}
		catch(Exception e) {
			System.out.println("Save 오류!!!");
			//System.out.println(e);
			e.printStackTrace();
		}
	}
	
	public void load() {
	
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File("save.dat")));

			// Read the DataStorage object
			DataStorage ds = (DataStorage)ois.readObject();
	        
			
			gp.player.level = ds.level;
			gp.player.maxLife = ds.maxLife;
			gp.player.life = ds.life;
			gp.player.maxMana = ds.maxMana;
			gp.player.mana = ds.mana;
			gp.player.strength = ds.strength;
			gp.player.exp = ds.exp;
			gp.player.nextLevelExp = ds.nextLevelExp;
			gp.player.coin = ds.coin;
			 
			gp.player.inventory.clear();
			
			for (int i = 0; i < ds.itemNames.size(); i++) {
				gp.player.inventory.add(getObject(ds.itemNames.get(i)));
				gp.player.inventory.get(i).amount = ds.itemAmounts.get(i);
			}
			gp.player.currentWeapon = gp.player.inventory.get(ds.currentWeaponSlot);
			gp.player.currentShield = gp.player.inventory.get(ds.currentShieldSlot);
			gp.player.getAttack();
			gp.player.getDefence();
			
//			for(int mapNum = 0; mapNum < gp.maxMap; mapNum++) {
//				for(int i=0;i<gp.obj[1].length;i++) {
//					if(ds.mapObjectNames[mapNum][i].equals("NA")) {
//						gp.obj[mapNum][i] = null;
//					}
//					else {
//						gp.obj[mapNum][i] = getObject(ds.mapObjectNames[mapNum][i]);
//						gp.obj[mapNum][i].worldX = ds.mapObjectWorldX[mapNum][i];
//						gp.obj[mapNum][i].worldY = ds.mapObjectWorldY[mapNum][i]  ;
////						if(ds.mapObjectLootNames[mapNum][i] != null) {
////							 gp.obj[mapNum][i].loot = getObject(ds.mapObjectLootNames[mapNum][i]);
////						}
////						gp.obj[mapNum][i].opened = ds.mapObjectOpened[mapNum][i];
////						if(gp.obj[mapNum][i].opened == true) {
////							gp.obj[mapNum][i].down1 = gp.obj[mapNum][i].image2;
////						}
//					}
//				}
//			}
			
			gp.currentMap = ds.currentMap;
			gp.timeCount = ds.timeCount;	
			gp.timeState = ds.timeState;
			gp.dayCount = ds.dayCount;
		}			
		catch (Exception e) {
			System.out.println("Load 오류!!!");
		}
	}
}
