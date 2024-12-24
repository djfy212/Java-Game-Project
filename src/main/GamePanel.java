package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import data.SaveLoad;
import entity.Entity;
import entity.Player;
import entity.Projectile;
import environment.EnvironmentManager;
import quest.Quest;
import tile.TileManager;

public class GamePanel extends JPanel implements Runnable {
	
	Thread timeThread = new Thread(new TimeThread());
	public int timeCount = 0;
	
	Main main;
	// SCREEN SETTING 스크린 사이즈를 설정합니다.
	final int originalTileSize = 16; // 타일 기본 크기 : 16X16 타일
	final int scale = 3; // 스케일 3배 확대
		
	// 맵 타일 크기 설정입니다.
	public final int tileSize = originalTileSize * scale; // 타일 크기 : 48X48 타일
	public final int maxScreenCol = 20;// 가로 타일 개수 : 20 칸
	public final int maxScreenRow = 12;// 세로 타일 개수 : 12 칸
	public final int screenWidth = tileSize * maxScreenCol; // 스크린 가로 픽셀 : 960 픽셀
	public final int screenHeight = tileSize * maxScreenRow; // 스크린 세로 픽셀 : 576 픽셀
//	public final int screenWidth = 1290; // 스크린 가로 픽셀 : 960 픽셀
//	public final int screenHeight = 720; // 스크린 세로 픽셀 : 576 픽셀

	// WORLD SETTINGS 맵 사이즈 세팅입니다.
	public final int maxWorldCol = 50; // 가로 50칸
	public final int maxWorldRow = 50; // 세로 50칸
	public final int maxMap = 10;
	public int currentMap = 0;
	
	// FOR FULL SCREEN
	int screenWidth2 = screenWidth;
	int screenHeight2 = screenHeight;
	BufferedImage tempScreen;
	Graphics2D g2;
	
	// FPS
	int FPS = 60;

	// SYSTEM
	TileManager tileM = new TileManager(this);
	public KeyHandler keyH = new KeyHandler(this);
	public CollisionChecker cChecker = new CollisionChecker(this);
	public AssetSetter aSetter = new AssetSetter(this);
	public GameUI ui = new GameUI(this);
	public EventHandler eHandler = new EventHandler(this);
	EnvironmentManager eManager = new EnvironmentManager(this);
	public SaveLoad saveLoad = new SaveLoad(this);
	Thread gameThread;

	// ENTITY AND OBJECT 엔티티와 오브젝트들을 담고 있습니다.
	public Player player = new Player(this, keyH);
	public Entity obj[][] = new Entity[maxMap][10];
	public Entity npc[][] = new Entity[maxMap][10];
	public Entity monster[][] = new Entity[maxMap][20];
//	public InteractiveTile iTile[][] = new InteractiveTile[maxMap][50];
	public ArrayList<Entity> projectileList = new ArrayList<Entity>();
	public ArrayList<Entity> entityList = new ArrayList<>();
	
	// QUEST 설정
	public ArrayList<Quest> questList = new ArrayList<>();
	
	// SKILL 설정 
	public Projectile skill = new Projectile(this);
	public ArrayList<Entity> skillList = new ArrayList<>();
	public ArrayList<Entity> skillAllList = new ArrayList<>();

	// 게임 내 상태 설정입니다.
	public int gameState; // 게임 스테이트 저장용
	public final int playState = 1;		//게임 플레이 상태
	public final int dialogueState = 2; // 태화창 표시 상태
	public final int transitionState = 3; // 
	public final int tradeState = 4;
	public final int menuState = 5;
	public final int gameOutState = 6;
	
	// 게임 내 시간 흐름도 설정들
	public int dayCount = 1;
	public int timeState;
	public final int morning = 0;
	public final int afternoon = 1;
	public final int evening = 2;
	public final int night = 3;
	
	
	public GamePanel(Main main) {
		this.main = main;
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		this.addKeyListener(keyH);
		//this.setFocusable(true);
	}

	//게임 셋업
	public void setupGame() {
		
		aSetter.setObject();	//오브젝트
		aSetter.setNPC();		//NPC
		aSetter.setMonster();	//몬스터
		eManager.setup();
		//playMusic(0);
		gameState = playState;	//게임 상태 : 플레이 상태
		
		dayCount = 1;			// 날짜 1
		timeState = morning;	// 시간 아침
		currentMap = 0;
		
		tempScreen = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
		g2 = (Graphics2D)tempScreen.getGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		skill.setSkill();
		//setFullScreen();		
	}
	public void setFullScreen() {
		
		// GET LOCAL SCREEN DEVICE
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		gd.setFullScreenWindow(Main.window);
		
		// GET FULL SCREEN WIDTH AND HEIGHT
		screenWidth2 = Main.window.getWidth();
		screenHeight2 = Main.window.getHeight();
	}
	//게임 스레드 생성 및 시작
	public void startThread() {

		gameThread = new Thread(this);
		gameThread.start();
		timeThread.start();
	}


	@Override
	public void run() {

		double drawInterval = 1000000000 / FPS;
		double delta = 0;
		long lastTime = System.nanoTime();
		long currentTime;
		long timer = 0;
		int drawCount = 0;

		while (gameThread != null) {

			currentTime = System.nanoTime();

			delta += (currentTime - lastTime) / drawInterval;
			timer += (currentTime - lastTime);
			lastTime = currentTime;

			if (delta >= 1) {
				if(main.panelState == main.game) {	
					update();
					drawToTempScreen(); // draw everything to the buffered image
					drawToScreen(); // draw the buffered image to the screen
					
				}
				delta--;
				drawCount++;		
			}
			if (timer >= 1000000000) {
				if(main.panelState == main.game) {
					System.out.println("gameFPS:" + drawCount);
				}
				drawCount = 0;
				timer = 0;
			}
		}
	}

	// 게임 정보 업데이트
	public void update() {

		if (gameState == playState) {
			// PLAYER
			player.update();
			// NPC
			for (int i = 0; i < npc[1].length; i++) {
				if (npc[currentMap][i] != null)
					npc[currentMap][i].update();
			}
			// MONSTER
			for (int i = 0; i < monster[1].length; i++) {
				if (monster[currentMap][i] != null) {
					if (monster[currentMap][i].alive == true && monster[currentMap][i].dying == false) {
						monster[currentMap][i].update();
					}
					if (monster[currentMap][i].alive == false) {
						monster[currentMap][i] = null;
					}
				}
			}
			for (int i = 0; i < projectileList.size(); i++) {
				if (projectileList.get(i) != null) {
					if (projectileList.get(i).alive == true) {
						projectileList.get(i).update();
					}
					if (projectileList.get(i).alive == false) {
						projectileList.remove(i);
					}
				}
			}
//			for(int i=0;i<iTile[1].length;i++) {
//				if(iTile[currentMap][i] != null) {
//					iTile[currentMap][i].update();
//				}
//			}

		}
	}

	public void drawToTempScreen() {

		// TILE 그리기
		tileM.draw(g2);

//		for(int i=0;i<iTile[1].length;i++) {
//			if (iTile[currentMap][i] != null) {
//				itile[currentMap][i].draw(g2);
//			}
//		}
		// ADD ENTITIES TO THE LIST
		entityList.add(player);

		for (int i = 0; i < npc[1].length; i++) {
			if (npc[currentMap][i] != null) {
				entityList.add(npc[currentMap][i]);
			}
		}

		for (int i = 0; i < obj[1].length; i++) {
			if (obj[currentMap][i] != null) {
				entityList.add(obj[currentMap][i]);
			}
		}

		// MONSTER 그리기
		for (int i = 0; i < monster[1].length; i++) {
			if (monster[currentMap][i] != null) {
				entityList.add(monster[currentMap][i]);
			}
		}
		
		for (int i = 0; i < projectileList.size(); i++) {
			if (projectileList.get(i) != null) {
				entityList.add(projectileList.get(i));
			}
		}

		// SORT
		Collections.sort(entityList, new Comparator<Entity>() {
			@Override
			public int compare(Entity e1, Entity e2) {
				int result = Integer.compare(e1.worldY, e2.worldY);
				return result;
			}

		});
		// DRAW ENTITIES
		for (int i = 0; i < entityList.size(); i++) {
			entityList.get(i).draw(g2);
		}
		// EMPTY ENTITIY LIST
		entityList.clear();
		
		// ENVIRONMENT
		eManager.draw(g2);
		
		// UI 그리기
		ui.draw(g2);	
		
	}

	public void drawToScreen() {
		Graphics g = getGraphics();
		g.drawImage(tempScreen, 0, 0, screenWidth2, screenHeight2, null);
		g.dispose();
		
	}
	class TimeThread implements Runnable{

		public void run() {
			while (true) {
				
				if (main.panelState == main.game) {
					timeCount++;
					System.out.println(timeCount);
					
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
