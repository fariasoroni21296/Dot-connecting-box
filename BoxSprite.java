package DotsAndBox;

import java.awt.Color;

public class BoxSprite extends Sprite {

	

	ConnectionSprite[] horizontalConnections;	
	ConnectionSprite[] verticalConnections;

	int player;	

	public BoxSprite() {
		super();

		color=Color.WHITE;	

     
        horizontalConnections=new ConnectionSprite[2];
		verticalConnections=new ConnectionSprite[2];

		width=Dots.DOT_GAP;
		height=Dots.DOT_GAP;

		shape.addPoint(-width/2, -height/2);
        shape.addPoint(-width/2, height/2);
        shape.addPoint(width/2, height/2);
        shape.addPoint(width/2, -height/2);
	}

	public boolean isBoxed() {
		boolean boxed=true;

		for(int i=0; i<2; i++) {
			if(!horizontalConnections[i].connectionMade || !verticalConnections[i].connectionMade) {
				boxed=false;
			}
		}

		return boxed;
	}

	public static BoxSprite createBox(int x, int y, ConnectionSprite[] horizontalConnections, ConnectionSprite[] verticalConnections) {
		BoxSprite box=new BoxSprite();
		box.player=0;
		box.x=x;
		box.y=y;

		box.horizontalConnections=horizontalConnections;
		box.verticalConnections=verticalConnections;
		return box;
	}
}