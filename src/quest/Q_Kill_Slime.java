package quest;

import main.GamePanel;
import monster.Mon_GreenSlime;

public class Q_Kill_Slime extends Quest{
	GamePanel gp;
	Mon_GreenSlime greenSlime;
	
	public Q_Kill_Slime(GamePanel gp) {
		super(gp);
		this.gp = gp;
		greenSlime = new Mon_GreenSlime(gp);
		name = greenSlime.name;
		
		maxCount = 3;
		
	}

}
