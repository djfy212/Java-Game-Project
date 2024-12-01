package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Shield_Normal extends Entity{

	public OBJ_Shield_Normal(GamePanel gp) {
		super(gp);
		
		name = "평범한 옷";
		defenceValue = 1;
	}

}
