package quest;

import main.GamePanel;

public class Quest {
	GamePanel gp;
	
	public String name;
	public String text = "";
	public int count;
	public int maxCount;
	public int exp;
	public int coin;
	public boolean finish = false;
	public int checkNPC = 0;
	
	public Quest(GamePanel gp) {
		this.gp = gp;
	}
	
}
