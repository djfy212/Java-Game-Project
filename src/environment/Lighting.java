package environment;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import main.GamePanel;

public class Lighting {
	GamePanel gp;
	BufferedImage darknessFilter;
	int dayCounter;
	float fillterAlpha = 0f;
	
	public Lighting(GamePanel gp, int circleSize) {
		
		darknessFilter = new BufferedImage(gp.screenWidth,gp.screenHeight,BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = (Graphics2D)darknessFilter.getGraphics();
		//스크린 크기의 사각형 만들기
		Area screenArea = new Area(new Rectangle2D.Double(0,0,gp.screenWidth,gp.screenHeight));
		//라이트 원의 중앙 x,y좌표
		int centerX = gp.player.screenX + (gp.tileSize/2);
		int centerY = gp.player.screenY + (gp.tileSize/2);
		//라이트 원의 왼쪽 위 x,y좌표
		double x = centerX - (circleSize/2);
		double y = centerY - (circleSize/2);
		//라이트 원 만들기
		Shape circleShape = new Ellipse2D.Double(x,y,circleSize,circleSize);
		//라이트 원 구역 만들기
		Area lightArea = new Area(circleShape);
		//사각형에서 원 빼기
		screenArea.subtract(lightArea);
		
		Color color[] = new Color[5];
		float fraction[] = new float[5];
		
		color[0] = new Color(0,0,0,0f);
		color[1] = new Color(0,0,0,0.25f);
		color[2] = new Color(0,0,0,0.50f);
		color[3] = new Color(0,0,0,0.70f);
		color[4] = new Color(0,0,0,0.80f);
		
		fraction[0] = 0f;
		fraction[1] = 0.25f;
		fraction[2] = 0.5f;
		fraction[3] = 0.75f;
		fraction[4] = 1f;
		
		RadialGradientPaint gPaint = new RadialGradientPaint(centerX, centerY, (circleSize/2), fraction, color);
		
		g2.setPaint(gPaint);
		
		g2.fill(lightArea);
		
		g2.fill(screenArea);
		g2.dispose();
	}
	public void update() {
		
	}
	public void draw(Graphics2D g2) {
		g2.drawImage(darknessFilter, 0, 0, null);
	}
}
