package DotsAndBox;

import java.awt.Color;

public class ConnectionSprite extends Sprite {

	public static final int HORZ_CONN=1;
    public static final int VERT_CONN=2;

    boolean connectionMade;	

    public ConnectionSprite() {
    	
        super();

        connectionMade=false;
        color=Color.PINK;
    }

    public static ConnectionSprite createConnection(int type, int x, int y) {
    	ConnectionSprite conn=new ConnectionSprite();

        if(type==ConnectionSprite.HORZ_CONN) {
        	conn.width=Dots.DOT_GAP;
        	conn.height=Dots.DOT_SIZE;
        } else if(type==ConnectionSprite.VERT_CONN) {
        	conn.width=Dots.DOT_SIZE;
        	conn.height=Dots.DOT_GAP;
        } else {
        	return null;
        }

        conn.x=x;
        conn.y=y;

        conn.shape.addPoint(-conn.width/2, -conn.height/2);
        conn.shape.addPoint(-conn.width/2, conn.height/2);
        conn.shape.addPoint(conn.width/2, conn.height/2);
        conn.shape.addPoint(conn.width/2, -conn.height/2);

        return conn;
    }
}
