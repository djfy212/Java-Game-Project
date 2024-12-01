package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_MpPotion extends Entity {

	public OBJ_MpPotion(GamePanel gp) {

		super(gp);

		name = "MP Potion";
		//down1 = setup("/object/mp_potion", gp.tileSize, gp.tileSize);
		value = 3;
		price = 25;
		stackable = true;
		//amount = 0;
	}

}
