package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener{
	

	GamePanel gp;
	TitlePanel tp;
	MenuPanel mp;

	public boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed, shotKeyPressed;
	public boolean skill1Pressed, skill2Pressed, slot1Pressed, slot2Pressed;

	public KeyHandler(TitlePanel tp){
		this.tp = tp;
	}
	public KeyHandler(GamePanel gp){
		this.gp = gp;
	}
	public KeyHandler(MenuPanel mp){
		this.mp = mp;
	}

	@Override
	public void keyTyped(KeyEvent e) {}
	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		
		if(gp != null) {
			if(gp.main.panelState == gp.main.game) {
				// GAME STATE
				if(gp.gameState == gp.playState) {
					gameState(code);				
				}
				// DIALOGUE STATE
				else if(gp.gameState == gp.dialogueState) {
					dialogueState(code);
				}
			}			
		}
		if(mp != null) {
			if(mp.main.panelState == mp.main.menu) {
				menuState(code);
			}			
		}

	}
	@Override
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		
		if(code == KeyEvent.VK_W)	
			upPressed = false;
		if(code == KeyEvent.VK_S)
			downPressed = false;
		if(code == KeyEvent.VK_A)
			leftPressed = false;
		if(code == KeyEvent.VK_D)
			rightPressed = false;
		if(code == KeyEvent.VK_X)
			rightPressed = false;
		if (code == KeyEvent.VK_J) {
			shotKeyPressed = false;
		}
		if (code == KeyEvent.VK_K) {
			skill1Pressed = false;
		}
		if (code == KeyEvent.VK_L) {
			skill2Pressed = false;
		}
		if (code == KeyEvent.VK_I) {
			slot1Pressed = false;
		}
		if (code == KeyEvent.VK_O) {
			slot2Pressed = false;
		}
//		if (code == KeyEvent.VK_P) {
//			slot3Pressed = false;
//		}
	}
	
	public void titleState(int code){}
	public void gameState(int code) {
		/// 플레이어 이동 키
		if (code == KeyEvent.VK_W) {upPressed = true;}			
		if (code == KeyEvent.VK_S) {downPressed = true;}		
		if (code == KeyEvent.VK_A) {leftPressed = true;}		
		if (code == KeyEvent.VK_D) {rightPressed = true;}
		// 엔터키 입력
		if (code == KeyEvent.VK_ENTER) {
			enterPressed = true;
		}
		if (code == KeyEvent.VK_J) { shotKeyPressed = true; }
		if (code == KeyEvent.VK_K) { skill1Pressed = true; }
		if (code == KeyEvent.VK_L) { skill2Pressed = true; }
		if (code == KeyEvent.VK_I) { System.out.println("I"); slot1Pressed = true; }
		if (code == KeyEvent.VK_O) { System.out.println("O"); slot2Pressed = true; }
//		if (code == KeyEvent.VK_P) { Pressed = true; }
//		if (code == KeyEvent.VK_X) {
//			gp.gameState = gp.menuState;
//		}
		
		// 메뉴 창 표시
		if (code == KeyEvent.VK_ESCAPE) {
			gp.main.panelState = gp.main.menu;	
        	Main.layout.show(Main.mainPanel,"mp");
        	Main.mainPanel.getComponent(2).setFocusable(true);
        	Main.mainPanel.getComponent(2).requestFocusInWindow();       	    	
		}
	}
	public void menuState(int code){
		if (code == KeyEvent.VK_ESCAPE) {
			mp.main.panelState = mp.main.game;	
        	Main.layout.show(Main.mainPanel,"gp");
        	Main.mainPanel.getComponent(1).setFocusable(true);
        	Main.mainPanel.getComponent(1).requestFocusInWindow();
		}
	}
	public void dialogueState(int code) {
		if(code == KeyEvent.VK_ENTER) {
			
			if(gp.player.speaking == false)
				gp.gameState = gp.playState;	//게임 복귀	
			else {
				gp.npc[gp.currentMap][gp.player.speakNum].speak();
			}
		}
	}

}
