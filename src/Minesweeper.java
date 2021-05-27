import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Minesweeper implements ActionListener {
	JFrame frame = new JFrame("Minesweeper");
	JButton reset = new JButton("Reset");
	final int BOARD_SIZE = 20;
	final int MINE_COUNT = 30;
	JButton[][] buttons = new JButton[BOARD_SIZE][BOARD_SIZE];
	int[][] counts = new int[BOARD_SIZE][BOARD_SIZE];
	
	Container grid = new Container();

	final int MINE = 10;
	BufferedImage img1;
	BufferedImage img2;
	BufferedImage img3;
	BufferedImage img4;
	BufferedImage img5;
	BufferedImage img6;
	BufferedImage img7;
	BufferedImage img8;
	BufferedImage mine;
	public Minesweeper() {
		frame.setSize(400, 500);
		frame.setLayout(new BorderLayout());
		frame.add(reset, BorderLayout.NORTH);
		reset.addActionListener(this);
		//Button grid
		grid.setLayout(new GridLayout(BOARD_SIZE,BOARD_SIZE));
		for (int a = 0; a < buttons.length; a++) {
			for (int b = 0; b < buttons[0].length; b++) {
				buttons[a][b] = new JButton();
				buttons[a][b].addActionListener(this);
				grid.add(buttons[a][b]);
			}
		}

		frame.add(grid, BorderLayout.CENTER);
		
		// 
		createRandomMines();
		
		// close
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		// load all images
		loadAllImages();
//		for (int i = 0; i < buttons.length; i++) {
//			for (int j = 0; j < buttons[0].length; j++) {
//					buttons[i][j].setText(counts[i][j] + "");
//			}
//		}
	}
	private void loadAllImages() {
		// TODO Auto-generated method stub
		try {
			img1 = ImageIO.read(getClass().getResource("img/1.png"));
			img2 = ImageIO.read(getClass().getResource("img/2.png"));
			img3 = ImageIO.read(getClass().getResource("img/3.png"));
			img4 = ImageIO.read(getClass().getResource("img/4.png"));
			img5 = ImageIO.read(getClass().getResource("img/5.png"));
			img6 = ImageIO.read(getClass().getResource("img/6.png"));
			img7 = ImageIO.read(getClass().getResource("img/7.png"));
			mine = ImageIO.read(getClass().getResource("img/bomb.png"));
		} catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	public static void main(String[] args) {

		new Minesweeper();
	}

	public void createRandomMines() {
		// initialize list of random pairs
		ArrayList<Integer> list = new ArrayList<>(); 
		for (int x = 0; x < counts.length; x++) {
			for (int y = 0; y < counts[0].length; y++) {
				list.add(x*100+y);
			}
		}
		
		// reset counts, pick out 30 mines
		counts = new int[BOARD_SIZE][BOARD_SIZE];
		for (int i = 0; i < MINE_COUNT; i++) {
			int choice = (int)(Math.random() * list.size());
			counts[list.get(choice)/100][list.get(choice) % 100] = MINE;
			list.remove(choice);
		}
		
		// figure out the neighbor counts
		for (int x = 0; x < counts.length; x++) {
			for (int y = 0; y < counts[0].length; y++) {
				if (counts[x][y] == MINE)
					continue;
				
				int neighborcount = 0;
				if (x > 0 && y > 0 && counts[x-1][y-1] == MINE) { // up left
					neighborcount++;
				}
				if (x > 0 && counts[x-1][y] == MINE) {
					neighborcount++;
				}
				if (x > 0 && y < counts[0].length -1 && counts[x -1][y+1] == MINE) { // lower left
					neighborcount++;
				}
				if (y > 0 && counts[x][y-1] == MINE) { // 
					neighborcount++;
				}
				if (y < counts[0].length - 1 && counts[x][y+1] == MINE) { // lower
					neighborcount++;
				}
				if (x < counts.length - 1 && y > 0 && counts[x+1][y-1] == MINE) {
					neighborcount++;
				}
				if (x < counts.length - 1 && counts[x+1][y] == MINE) {
					neighborcount++;
				}
				if ( x < counts.length-1 && y < counts[0].length-1 && counts[x+1][y+1] == MINE) { // lower right
					neighborcount++;
				}
				
				counts[x][y] = neighborcount;
			}
		}
		
//		for (int i = 0; i < buttons.length; i++) {
//			for (int j = 0; j < buttons[0].length; j++) {
//					buttons[i][j].setText(counts[i][j] + "");
//			}
//		}
	}
	
	public void lostGame() {
		for (int i = 0; i < buttons.length; i++) {
			for (int j = 0; j < buttons[0].length; j++) {
				if (buttons[i][j].isEnabled()) {
						showButton(buttons[i][j], counts[i][j]);
						buttons[i][j].setEnabled(false);
					}
				}
			}
	}
	
	public void resetGame() {
		for (int i = 0; i < buttons.length; i++) {
			for (int j = 0; j < buttons[0].length; j++) {
				buttons[i][j].setEnabled(true);
				buttons[i][j].setIcon(null);
				buttons[i][j].setText("");
			}
		}
		createRandomMines();
	}
	
	public void clearZeros(ArrayList<Integer> toClear) {
		if (toClear.size() == 0) return; 
		
		int x = toClear.get(0) / 100;
		int y = toClear.get(0) % 100;
		
		toClear.remove(0);
		
			if (x > 0 && y > 0 && buttons[x-1][y-1].isEnabled() && counts[x-1][y-1] != MINE) {
				showButton(buttons[x-1][y-1], counts[x-1][y-1]);
				buttons[x-1][y-1].setEnabled(false);
				if (counts[x-1][y-1] == 0)
					toClear.add((x-1)*100+(y-1));
			}
			if (y>0 && buttons[x][y-1].isEnabled()  && counts[x][y-1] != MINE ) {
				showButton(buttons[x][y-1],counts[x][y-1]);
				buttons[x][y-1].setEnabled(false);
				if (counts[x][y-1] == 0)
					toClear.add((x)*100+(y-1));
			}
			if (x < counts.length - 1 && y > 0 && buttons[x+1][y-1].isEnabled()  && counts[x+1][y-1] != MINE) {
				showButton(buttons[x+1][y-1],counts[x+1][y-1]);
				buttons[x+1][y-1].setEnabled(false);
				if (counts[x+1][y-1] == 0)
					toClear.add((x+1)*100+(y-1));
			}
			if (x > 0 && buttons[x-1][y].isEnabled()  && counts[x-1][y] != MINE )  {
				showButton(buttons[x-1][y],counts[x-1][y]);
				buttons[x-1][y].setEnabled(false);
				if (counts[x-1][y] == 0)
					toClear.add((x-1)*100+(y));
			}
			if (x < counts.length - 1 && buttons[x+1][y].isEnabled()  && counts[x+1][y] != MINE) {
				showButton(buttons[x+1][y], counts[x+1][y]);
				buttons[x+1][y].setEnabled(false);
				if (counts[x+1][y] == 0)
					toClear.add((x+1)*100+(y));
			}
			
			if (x > 0 && y < counts[0].length - 1 && buttons[x-1][y+1].isEnabled()   && counts[x-1][y+1] != MINE) {
				showButton(buttons[x-1][y+1], counts[x-1][y+1]);
				buttons[x-1][y+1].setEnabled(false);
				if (counts[x-1][y+1] == 0)
					toClear.add((x-1)*100+(y+1));
			}
			if (y < counts[0].length - 1 && buttons[x][y+1].isEnabled()  && counts[x][y+1] != MINE ) {
				showButton(buttons[x][y+1],counts[x][y+1]);
				buttons[x][y+1].setEnabled(false);
				if (counts[x][y+1] == 0)
					toClear.add((x)*100+(y+1));
			}
			if (x < counts.length - 1 && y < counts[0].length - 1 && buttons[x+1][y+1].isEnabled()  && counts[x+1][y+1] != MINE ) {
				showButton(buttons[x+1][y+1], counts[x+1][y+1]);
				buttons[x+1][y+1].setEnabled(false);
				if (counts[x+1][y+1] == 0)
					toClear.add((x+1)*100+(y+1));
			}
		
	    
		clearZeros(toClear);
	}

	private void showButton(JButton jButton, int i) {
		// TODO Auto-generated method stub
		
		try {
		    // get the size of Jbutton
			BufferedImage img = getCorrectImg(i);
		    //Dimension size = jButton.getPreferredSize();
		    //System.out.println("size is: " + size.toString());
		    BufferedImage img2 = resizeImage(img, jButton.getWidth(), jButton.getHeight());
		    ImageIcon imgIcon = new ImageIcon(img2);
		    jButton.setIcon(imgIcon);
		    jButton.setDisabledIcon(imgIcon);
		  } catch (Exception ex) {
		    System.out.println(ex);
		  }
	}
	private BufferedImage getCorrectImg(int i) {
		// TODO Auto-generated method stub
		switch (i) {
		case 1: return img1;
		case 2: return img2;
		case 3: return img3;
		case 4: return img4;
		case 5: return img5;
		case 6: return img6;
		case 7: return img7;
		
		case MINE: return mine;
		default: return null;
		}
	}
	public void checkWin() {
		boolean won = true;

		for (int i = 0; i < counts.length; i++) {
			for (int j = 0; j< counts[0].length; j++) {
				if (counts[i][j] != MINE && buttons[i][j].isEnabled() == true) {
					won = false;
				}
			}
		}
		
		if (won) {
			JOptionPane.showMessageDialog(frame, "You won!");
		}
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource().equals(reset)) {
			// reset the board
			resetGame();
		}
		else {
			for (int i = 0; i < buttons.length; i++) {
				for (int j = 0; j < buttons[0].length; j++) {
					if (e.getSource().equals(buttons[i][j])) {
						if (counts[i][j] == MINE) {
							// it's a mine
							showButton(buttons[i][j], counts[i][j]);
							lostGame();
						} else if (counts[i][j] == 0) {
							showButton(buttons[i][j], counts[i][j]);
							buttons[i][j].setEnabled(false);
							ArrayList<Integer> toClear = new ArrayList<Integer>();
							toClear.add(i*100+j);
							clearZeros(toClear);
							checkWin();
						}
						else {
							showButton(buttons[i][j], counts[i][j]);
							buttons[i][j].setEnabled(false);// disable it
							checkWin();
						}
					}
				}
			}
		}
	}

	public static BufferedImage resizeImage(BufferedImage originalImage, int width, int height) throws IOException {
		Image resultingImage = originalImage.getScaledInstance(width, height, Image.SCALE_DEFAULT);
		BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
		outputImage.getGraphics().drawImage(resultingImage,0, 0, null);
		return outputImage;
	}
}
