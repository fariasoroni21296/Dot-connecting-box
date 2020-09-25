package DotsAndBox;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Dots extends JFrame implements MouseMotionListener, MouseListener {

    
	private static final long serialVersionUID = 1L;
	public  int DOT_NUMBER=Integer.parseInt(JOptionPane.showInputDialog(null,"Enter the number of dots in Rows and Colums: "));	
    public static final int DOT_GAP=60;		
    public static final int DOT_SIZE=10;		

    public static final int PLAYER_ONE=1;
    public static final int PLAYER_TWO=2;

    public static final Color PLAYER_ONE_COLOR=Color.BLUE;	
    public static final Color PLAYER_TWO_COLOR=Color.RED;		
	public static final int DOT_GAP1 = 0;



  private ConnectionSprite[] horizontalConnections;	
    private ConnectionSprite[] verticalConnections;	
    private BoxSprite[] boxes;	
    private Sprite[] dots;		

    private Dimension dim;		

    private int clickx;		
    private int clicky;		

    private int mousex;		
    private int mousey; 	

    private int centerx;	
    private int centery; 	

    private int side;	
    private int space;	

    private int activePlayer;	

    public Dots() {
        super("Connect the Dots");

        setSize(700, 700);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addMouseListener(this);
        addMouseMotionListener(this);

        loadProperties();
        loadDots();

        startNewGame();

        setVisible(true);
    }

    private void loadProperties() {
    	

        clickx=0;
        clicky=0;
        mousex=0;
        mousey=0;

        dim=getSize();
        centerx=dim.width/2;
        centery=(dim.height - 100) /2;

        side=DOT_NUMBER * DOT_SIZE + (DOT_NUMBER - 1) * DOT_GAP;	
    	space=DOT_SIZE + DOT_GAP;
    }

    private void loadConnections() {

        horizontalConnections=new ConnectionSprite[(DOT_NUMBER-1) * DOT_NUMBER];
        verticalConnections=new ConnectionSprite[(DOT_NUMBER-1) * DOT_NUMBER];

        
        for(int i=0; i<horizontalConnections.length; i++) {
        	int colsx=i % (DOT_NUMBER-1);
        	int rowsx=i / (DOT_NUMBER-1);
        	int horx=centerx - side / 2 + DOT_SIZE + colsx * space;
        	int hory=centery - side / 2 + rowsx * space;
        	horizontalConnections[i]=ConnectionSprite.createConnection(ConnectionSprite.HORZ_CONN, horx, hory);

        	int colsy=i % DOT_NUMBER;
        	int rowsy=i / DOT_NUMBER;
        	int vertx=centerx - side / 2 + colsy * space;
        	int verty=centery - side / 2 + DOT_SIZE + rowsy * space;
        	verticalConnections[i]=ConnectionSprite.createConnection(ConnectionSprite.VERT_CONN, vertx, verty);
        }
    }

    private void loadBoxes() {

    	

    	boxes=new BoxSprite[(DOT_NUMBER-1) * (DOT_NUMBER-1)];

    	for(int i=0; i<boxes.length; i++) {
    		int cols=i % (DOT_NUMBER-1);
    		int rows=i / (DOT_NUMBER-1);

    		int boxx=centerx - side / 2 + DOT_SIZE + cols * space;
    		int boxy=centery - side / 2 + DOT_SIZE + rows * space;

    		ConnectionSprite[] horConn=new ConnectionSprite[2];
    		horConn[0]=horizontalConnections[i];
    		horConn[1]=horizontalConnections[i + (DOT_NUMBER - 1)];

    		ConnectionSprite[] verConn=new ConnectionSprite[2];		
    		verConn[0]=verticalConnections[i + rows];
    		verConn[1]=verticalConnections[i + rows + 1];

    		boxes[i]=BoxSprite.createBox(boxx, boxy, horConn, verConn);
    	}
    }

    private void loadDots() {

		

        dots=new Sprite[DOT_NUMBER * DOT_NUMBER];
        for(int rows=0; rows<DOT_NUMBER; rows++) {
            for(int cols=0; cols<DOT_NUMBER; cols++) {
                Sprite dot=new Sprite();
                dot.width=DOT_SIZE;
                dot.height=DOT_SIZE;
                dot.x=centerx - side/2 + cols * space;
                dot.y=centery - side/2 + rows * space;
                dot.shape.addPoint(-DOT_SIZE/2, -DOT_SIZE/2);
                dot.shape.addPoint(-DOT_SIZE/2, DOT_SIZE/2);
                dot.shape.addPoint(DOT_SIZE/2, DOT_SIZE/2);
                dot.shape.addPoint(DOT_SIZE/2, -DOT_SIZE/2);
                int index=rows * DOT_NUMBER + cols;
                dots[index]=dot;
            }
        }
    }

    private void startNewGame() {
    	activePlayer=PLAYER_ONE;
    	loadConnections();
        loadBoxes();
    }

    private ConnectionSprite getConnection(int x, int y) {

    	

    	for(int i=0; i<horizontalConnections.length; i++) {
    		if(horizontalConnections[i].containsPoint(x, y)) {
    			return horizontalConnections[i];
    		}
    	}

    	for(int i=0; i<verticalConnections.length; i++) {
    		if(verticalConnections[i].containsPoint(x, y)) {
    			return verticalConnections[i];
    		}
    	}

    	return null;
    }

    private boolean[] getBoxStatus() {
    	boolean[] status=new boolean[boxes.length];

    	for(int i=0; i<status.length; i++) {
    		status[i]=boxes[i].isBoxed();
    	}

    	return status;
    }

    private int[] calculateScores() {
    	int[] scores={0, 0};

    	for(int i=0; i<boxes.length; i++) {
    		if(boxes[i].isBoxed() && boxes[i].player!=0) {
    			scores[boxes[i].player - 1]++;
    		}
    	}

    	return scores;
    }

    private boolean makeConnection(ConnectionSprite connection) {
    	boolean newBox=false;

    	boolean[] boxStatusBeforeConnection=getBoxStatus();	

    	connection.connectionMade=true;

    	boolean[] boxStatusAfterConnection=getBoxStatus();

    	for(int i=0; i<boxes.length; i++) {
    		if(boxStatusAfterConnection[i]!=boxStatusBeforeConnection[i]) {
    			newBox=true;
    			boxes[i].player=activePlayer;
    		}
    	}

    	if(!newBox) {	
    		if(activePlayer==PLAYER_ONE)
    			activePlayer=PLAYER_TWO;
    		else
    			activePlayer=PLAYER_ONE;
    	}

    	checkForGameOver();

    	return newBox;
    }

    private void checkForGameOver() {
    	int[] scores=calculateScores();
    	if((scores[0] + scores[1])==((DOT_NUMBER - 1) * (DOT_NUMBER - 1))) {
    		JOptionPane.showMessageDialog(this, "Player1: " + scores[0] + "\nPlayer2: " + scores[1], "Game Over", JOptionPane.PLAIN_MESSAGE);
    		startNewGame();
    		repaint();
    	}
    }

    private void handleClick() {
    	ConnectionSprite connection=getConnection(clickx, clicky);
    	if(connection==null)
    		return;

    	if(!connection.connectionMade) {
    		makeConnection(connection);

    	}

    	repaint();
    }

    public void mouseMoved(MouseEvent event) {
    	mousex=event.getX();
    	mousey=event.getY();
    	repaint();
    }

    public void mouseDragged(MouseEvent event) {
    	mouseMoved(event);
    }

    public void mouseClicked(MouseEvent event) {
    	clickx=event.getX();
    	clicky=event.getY();

    	handleClick();
    }

    public void mouseEntered(MouseEvent event) {
    }

    public void mouseExited(MouseEvent event) {
    }

    public void mousePressed(MouseEvent event) {
    }

    public void mouseReleased(MouseEvent event) {
    }

    private void paintBackground(Graphics g) {
    	g.setColor(Color.PINK);
    	g.fillRect(0, 0, dim.width, dim.height);
    }

    private void paintDots(Graphics g) {
    	for(int i=0; i<dots.length; i++) {
    		dots[i].render(g);
    	}
    }

    private void paintConnections(Graphics g) {
    	for(int i=0; i<horizontalConnections.length; i++) {

    		if(!horizontalConnections[i].connectionMade && activePlayer==PLAYER_ONE) {
    			if(horizontalConnections[i].containsPoint(mousex, mousey)) {
    				horizontalConnections[i].color=Color.BLUE;
                                
                                
    			} else {
    				horizontalConnections[i].color=Color.PINK;
    			}
                        
    		} else if(!horizontalConnections[i].connectionMade && activePlayer==PLAYER_TWO) {
    			if(horizontalConnections[i].containsPoint(mousex, mousey)) {
    				horizontalConnections[i].color=Color.RED;
    			} else {
    				horizontalConnections[i].color=Color.PINK;
    			}
                }


    		horizontalConnections[i].render(g);
    	}

    	for(int i=0; i<verticalConnections.length; i++) {

    		if(!verticalConnections[i].connectionMade && activePlayer==PLAYER_ONE) {
    			if(verticalConnections[i].containsPoint(mousex, mousey)) {
                            if(activePlayer==PLAYER_ONE){
    				verticalConnections[i].color=Color.BLUE;
                            }
                            
    			} else {
    				verticalConnections[i].color=Color.PINK;
    			}
    		} else if(!verticalConnections[i].connectionMade && activePlayer==PLAYER_TWO) {
    			if(verticalConnections[i].containsPoint(mousex, mousey)) {
    				verticalConnections[i].color=Color.RED;
    			} else {
    				verticalConnections[i].color=Color.PINK;
    			}
                }

    		verticalConnections[i].render(g);
        
        }}

    public void paintBoxes(Graphics g) {
    	for(int i=0; i<boxes.length; i++) {
    		if(boxes[i].isBoxed()) {
    			if(boxes[i].player==PLAYER_ONE) {
    				boxes[i].color=PLAYER_ONE_COLOR;
    			} else if(boxes[i].player==PLAYER_TWO) {
    				boxes[i].color=PLAYER_TWO_COLOR;
    			}
    		} else {
    			boxes[i].color=Color.WHITE;
    		}

    		boxes[i].render(g);
    	}
    }

    public void paintStatus(Graphics g) {
    	int[] scores=calculateScores();
    	String status="It is player" + activePlayer + "'s turn";
    	String status2="Player 1: " + scores[0];
    	String status3="Player 2: " + scores[1];

    	
    	g.setColor(Color.GREEN);
    	g.drawString(status, 10, dim.height-50);

    	g.setColor(PLAYER_ONE_COLOR);
    	g.drawString(status2, 10, dim.height-35);

    	g.setColor(PLAYER_TWO_COLOR);
    	g.drawString(status3, 10, dim.height-20);
    }

    public void update(Graphics g) {
    	paint(g);
    }

    public void paint(Graphics g) {

    	Image bufferImage=createImage(dim.width, dim.height);
    	Graphics bufferGraphics=bufferImage.getGraphics();

    	paintBackground(bufferGraphics);
    	paintDots(bufferGraphics);
    	paintConnections(bufferGraphics);
    	paintBoxes(bufferGraphics);
    	paintStatus(bufferGraphics);

    	g.drawImage(bufferImage, 0, 0, null);
    }

    public static void main(String[] args) {


    	new Dots();
    }
}