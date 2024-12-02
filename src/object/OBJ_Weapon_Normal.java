package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Weapon_Normal extends Entity{
	public OBJ_Weapon_Normal(GamePanel gp) {
		super(gp);

		name = "평범한 지팡이";
		attackValue = 2;
		description = "공격력 + " + attackValue;
	}
}
