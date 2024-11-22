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
		}
		
		return obj;
	}
	
	public void save() {
		
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("Save.dat")));
			
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
			
			// 플레이어 인벤토리
			for (int i = 0; i < ds.itemNames.size(); i++) {
				ds.itemNames.add(gp.player.inventory.get(i).name);
				ds.itemAmounts.add(gp.player.inventory.get(i).amount);	
			}
			// 플레이어 장비
			ds.currentWeaponSlot = gp.player.getCurrentWeaponSlot();
			ds.currentShieldSlot = gp.player.getCurrentShieldSlot();
			
			// Write the DataStorage object
			oos.writeObject(ds);
		}
		catch(Exception e) {
			System.out.println("Save 오류!!!");
		}
		
	}
	
	public void load() {
	
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File("Save.dat")));
			
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
			 
//			 gp.player.inventory.clear();
//			 for(int i=0;i<ds.itemNames.size();i++) {
//				 gp.player.inventory.add(getObject(ds.itemNames.get(i)));
//				 gp.player.inventory.get(i).amount = ds.itemAmounts.get(i);
//			 }
//			gp.player.currentWeapon = ds.currentWeaponSlot;
//			gp.player.currentShield = ds.currentShieldSlot;
			gp.player.getAttack();
			gp.player.getDefence();
			
//			//OBJECT ON MAP
//			ds.mapObjectNames = new String[gp.maxMap][gp.obj[1].length];
//			ds.mapObjectWorldX = new int[gp.maxMap][gp.obj[1].length];
//			ds.mapObjectWorldY = new int[gp.maxMap][gp.obj[1].length];
//			ds.mapObjectLootNames = new String[gp.maxMap][gp.obj[1].length];
//			ds.mapObjectOpend = new boolean[gp.maxMap][gp.obj[1].length];
			

		}			
		catch (Exception e) {
			System.out.println("Load 오류!!!");
		}
	}
}
