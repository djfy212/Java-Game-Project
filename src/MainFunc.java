import javax.swing.*;
import java.awt.*;
import java.awt.Graphics;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;

class Enemy {
	String enemy_name;
	int enemy_health;
	int enemy_attack;
	int enemy_PosX;
	int enemy_PosY;
		
	Enemy(String name, int hp, int atk, int x, int y) {
		this.enemy_name = name;
		this.enemy_health = hp;
		this.enemy_attack = atk;
		this.enemy_PosX = x;
		this.enemy_PosY = y;
	}
	
	public int getHp() {
		return enemy_health;
	}
	public void setHp(int hp) {
		this.enemy_health = hp;
	}
}

class MoveFunc extends JFrame implements Runnable, KeyListener {

	boolean keyUp = false;
	boolean keyDown = false;
	boolean keyLeft = false;
	boolean keyRight = false; 
	boolean playerMove = false;

	Toolkit tk = Toolkit.getDefaultToolkit();
	Graphics gc;
	 
	Thread th;
	 
	int x, y; // 케릭터의 현재 좌표를 받을 변수
	int cnt; //무한 루프를 카운터 하기 위한 변수
	int moveStatus; //케릭터가 어디를 바라보는지 방향을 받을 변수
	
	// 이미지 버퍼
    Image buffImg;
    Graphics buffG;
    //플레이어 위치
	int playerX = 100;
	int playerY = 100;

	BufferedImage background; // 배경 이미지를 잠시 담아놓는 공간으로 활용
	BufferedImage background1; 
	BufferedImage background2; 

	BufferedImage player; // 케릭터 이미지를 잠시 담아 놓는 공간으로 활용
	BufferedImage playerRight; // 케릭터 왼쪽 이미지
	BufferedImage playerLeft; // 케릭터 오른쪽 이미지
	BufferedImage playerUp; // 케릭터 위쪽 이미지
	BufferedImage playerDown; // 케릭터 아래쪽 이미지

	
	public void start(){ 
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.addKeyListener(this);
		th = new Thread(this);
		th.start();
	}		
	public void run(){ 
		while(true){
			try {
				keyProcess();
				repaint();
				
				Thread.sleep(20);
				cnt++;
			} 
			catch (Exception e) {}
		}
	}
	
	public void keyProcess(){

		playerMove = false;		

		if (keyLeft) {
			playerMove = true;
			System.out.println("왼쪽");
			
			player = playerLeft; // 왼쪽이미지를 호출한다.
			
			playerX -= 10;
//			if (keyUp) { 
//	        	playerY -= 5;
//			} 
//			if (keyDown) { 
//				playerY += 5;
//			}
		} else if (keyRight) {
			playerMove = true;
			System.out.println("오른쪽");

			player = playerRight; // 오른쪽이미지를 호출한다.
			
			playerX += 10;
//			if (keyUp) { 
//	        	playerY -= 5;
//			} 
//			if (keyDown) { 
//				playerY += 5;
//			}
		} else if (keyUp) {
			playerMove = true;
			System.out.println("위");
			
			//player = playerUp; // 위쪽이미지를 호출한다.
			
			playerY -= 10;
//			if (keyLeft) { 
//				playerX -= 5;
//			} 
//			if (keyRight) { 
//				playerX += 5;
//			} 

		} else if (keyDown) {
			playerMove = true;
			System.out.println("아래");
			
			//player = playerDown; // 아래쪽이미지를 호출한다.
			
			playerY += 10;
			
//			if (keyLeft) { 
//				playerX -= 5;
//			} 
//			if (keyRight) { 
//				playerX += 5;
//			} 
		}
		repaint();

	}
		
	@Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()){
            case KeyEvent.VK_UP :
                this.keyUp = true;
                break;
            case KeyEvent.VK_DOWN :
                this.keyDown = true;
                break;
            case KeyEvent.VK_LEFT :
                this.keyLeft = true;
                break;
            case KeyEvent.VK_RIGHT :
                this.keyRight = true;
                break;
        }
    }
	
	@Override
	public void keyReleased(KeyEvent e) {
		switch(e.getKeyCode()){
		case KeyEvent.VK_LEFT :
		keyLeft = false;
		break;
		case KeyEvent.VK_RIGHT :
		keyRight = false;
		break;
		case KeyEvent.VK_UP :
		keyUp = false;
		break;
		case KeyEvent.VK_DOWN :
		keyDown = false;
		break;
		}
	}
	@Override
	public void keyTyped(KeyEvent e) {}

	public MoveFunc() {
		start();
		setFrame();
		initData();
		setInitLayout();
		addEventListener();
	}
	
	public void setFrame() {
		setTitle("Test");
		setSize(1920, 1080);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setExtendedState(MAXIMIZED_BOTH);
		setVisible(true);
	}
	private void initData() {
		
		try {
			playerRight = ImageIO.read(new File(("img/star_icon.png"))); // 오른쪽 케릭터의 이미지를 읽어옴
			playerLeft = ImageIO.read(new File(("img/pagetop.png"))); // 왼쪽 케릭터의 이미지를 읽어옴
		} catch (IOException e) {
			System.out.println("에러발생 경로, 파일명 에러");
			e.printStackTrace();
		}
		
		player = playerLeft; // 초기 케릭터의 이미지를 왼쪽을 보는 이미지를 임시 공간에 담아둔다.

	}

	private void setInitLayout() {
		setVisible(true); 		
		revalidate();
	}
	
	private void addEventListener() {
		this.addKeyListener(this); // keyListener인터페이스를 구현을 해줬기때문에 this 사용
	}
	
	@Override
    public void paint(Graphics g) {
        buffImg = createImage(getWidth(),getHeight()); // 버퍼링용 이미지 ( 도화지 )
        buffG = buffImg.getGraphics(); // 버퍼링용 이미지에 그래픽 객체를 얻어야 그릴 수 있다고 한다. ( 붓? )
        update(g);
    }

    @Override
    public void update(Graphics g) {
        buffG.clearRect(0, 0, 854, 480); // 백지화
        buffG.drawImage(player,playerX,playerY,50,50, this); // 유저 비행기 그리기.
        g.drawImage(buffImg,0,0,this); // 화면g애 버퍼(buffG)에 그려진 이미지(buffImg)옮김. ( 도화지에 이미지를 출력 )
        repaint();
    }
}

public class MainFunc extends JPanel {
	public static void main(String[] args) {
		MoveFunc f = new MoveFunc();
		Enemy e = new Enemy("aaa",100,10,100,100);
		//BufferedImage eimg = ImageIO.read(new File(("img/star_icon.png")));
		Graphics buffG;
		//buffG.drawImage(eimg,e.enemy_PosX,e.enemy_PosY,50,50,this); // 유저 비행기 그리기.
		
		
	}
}