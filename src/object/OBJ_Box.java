package object;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class OBJ_Box extends SuperObject{
	public OBJ_Box() {
		name = "Box";
		try {
			image = ImageIO.read(getClass().getResourceAsStream("/object/Box.png"));
		}catch(IOException e) {
			e.printStackTrace();
		}
		collision = true;
		
	}
}
