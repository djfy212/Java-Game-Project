package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener{
	

	GamePanel gp;
	TitlePanel tp;

	public boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed, shotKeyPressed;
	public boolean attackPressed, skill1Pressed, skill2Pressed, skill3Pressed, slot1Pressed, slot2Pressed;

	public KeyHandler(TitlePanel tp){
		this.tp = tp;
	}
	public KeyHandler(GamePanel gp){
		this.gp = gp;
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
				else if(gp.gameState == gp.tradeState) {
					tradeState(code);
				}
				else if(gp.gameState == gp.menuState) {
					menuState(code);
				}
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
//			shotKeyPressed = false;
			//attackPressed = false;
			skill1Pressed = false;
		}
		if (code == KeyEvent.VK_K) {
			skill2Pressed = false;
		}
		if (code == KeyEvent.VK_L) {
			skill3Pressed = false;
		}
		if (code == KeyEvent.VK_U) {
			slot1Pressed = false;
		}
		if (code == KeyEvent.VK_I) {
			slot2Pressed = false;
		}
//		if (code == KeyEvent.VK_O) {
//			slot3Pressed = false;
//		}
	}
	public void tradeState(int code) {
		if(code == KeyEvent.VK_ENTER) {
			enterPressed = true;
		}
		if(gp.ui.subState == 0) {
			if(code == KeyEvent.VK_W) {
				gp.ui.commandNum--;
				if(gp.ui.commandNum < 0) {
					gp.ui.commandNum = 2;
				}
			}		
			if(code == KeyEvent.VK_S) {
				gp.ui.commandNum++;
				if(gp.ui.commandNum > 2) {
					gp.ui.commandNum = 0;
				}
			}
		}
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
//		if (code == KeyEvent.VK_J) { shotKeyPressed = true; }
		if (code == KeyEvent.VK_J) { skill1Pressed = true; }//attackPressed = true; }
		if (code == KeyEvent.VK_K) { skill2Pressed = true; }
		if (code == KeyEvent.VK_L) { skill3Pressed = true; }
		if (code == KeyEvent.VK_U) { slot1Pressed = true; }
		if (code == KeyEvent.VK_I) { slot2Pressed = true; }
//		if (code == KeyEvent.VK_O) { Pressed = true; }
//		if (code == KeyEvent.VK_X) {
//			gp.gameState = gp.menuState;
//		}
		
		// 메뉴 창 표시
		if (code == KeyEvent.VK_ESCAPE) {
 
			gp.gameState = gp.menuState;
			gp.ui.menuCommand = 0;
			gp.ui.menuCount = 0;
			gp.ui.openMenu();
		}
	}
	public void menuState(int code){

		if(gp.ui.menuCommand != -1) {
			if (code == KeyEvent.VK_ESCAPE) {
				gp.gameState = gp.playState;
			}
			if (code == KeyEvent.VK_ENTER) {
				enterPressed = true;
			}
			if(code == KeyEvent.VK_W) {
				gp.ui.menuCommand--;
				if(gp.ui.menuCommand < 0) {
					gp.ui.menuCommand = 5;
				}
			}		
			if(code == KeyEvent.VK_S) {
				gp.ui.menuCommand++;
				if(gp.ui.menuCommand > 5) {
					gp.ui.menuCommand = 0;
				}
			}
		}
		else if (gp.ui.menuCommand == -1) {
			if (gp.ui.menuCount == 2) {
				if (code == KeyEvent.VK_W) {

					gp.ui.skillCount--;
					System.out.println(gp.ui.skillCount);
					if (gp.ui.skillCount < 0) {
						gp.ui.skillCount = 2;
					}
				}
				if (code == KeyEvent.VK_S) {
					gp.ui.skillCount++;
					System.out.println(gp.ui.skillCount);
					if (gp.ui.skillCount > 2) {
						gp.ui.skillCount = 0;
					}
				}
				if(code == KeyEvent.VK_ESCAPE) {
					gp.ui.menuCommand = gp.ui.menuCount-1;
				}
				
			}
			if (gp.ui.menuCount == 4) {
				if (code == KeyEvent.VK_W) {
					gp.ui.itemCount--;
					if (gp.ui.itemCount < 0) {
						gp.ui.itemCount = 3;
					}
				}
				if (code == KeyEvent.VK_S) {
					gp.ui.itemCount++;
					if (gp.ui.itemCount > 3) {
						gp.ui.itemCount = 0;
					}
				}
				if(code == KeyEvent.VK_ESCAPE) {
					gp.ui.menuCommand = gp.ui.menuCount-1;
				}
			}
		}
		
	}
	public void dialogueState(int code) {
		if(code == KeyEvent.VK_ENTER) {
			enterPressed = true;

		}
	}

}
