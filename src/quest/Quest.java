package quest;

import main.GamePanel;

public class Quest {
	GamePanel gp;
	
	public String name;
	public String text = "";
	public int count;
	public int maxCount;
	
	public Quest(GamePanel gp) {
		this.gp = gp;
	}
	
}
