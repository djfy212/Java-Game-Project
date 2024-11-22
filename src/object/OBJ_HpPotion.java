package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_HpPotion extends Entity{
	
	public OBJ_HpPotion(GamePanel gp) {

		super(gp);

		name = "HP Potion";
		//down1 = setup("/object/hp_potion", gp.tileSize, gp.tileSize);
		value = 2;
		price = 25;
		stackable = true;
	}
}
