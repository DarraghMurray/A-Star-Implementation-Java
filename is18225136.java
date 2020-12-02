import java.util.Scanner;
import java.util.ArrayList;
import java.util.TreeMap;

import javafx.scene.shape.Rectangle;

import javafx.application.Application;
import javafx.scene.paint.Color;

import javafx.geometry.Pos;

import javafx.scene.Scene;
import javafx.scene.layout.GridPane;

import javafx.stage.Stage;
/*
 * Darragh Murray - 18225136
 */
class node {
	
	public int x;
	public int y;
	
	public int pX;
	public int pY;
	
	public int g;
	public int h;
	public int f;
	
	public node(int x, int y, int pX, int pY) {
		
	}
}

public class is18225136 extends Application {
	
	private int startRow;
	private int startCol;
	
	private int endRow;
	private int endCol;
	
	private node currentNode;
	
	private int Grid[][] = {  { 0, 1, 2, 3, 4, 5, 6, 7 }, 
							  { 10, 11, 12, 13, 14, 15, 16, 17 }, 
						      { 20, 21, 22, 23, 24, 25, 26, 27 }, 
						      { 30, 31, 32, 33, 34, 35, 36, 37 }, 
						      { 40, 41, 42, 43, 44, 45, 46, 47}, 
						      { 50, 51, 52, 53, 54, 55, 56, 57 }, 
						      { 60, 61, 62, 63, 64, 65, 66, 67 }, 
						      { 70, 71, 72, 73, 74, 75, 76, 77 } 
						   };
	
	private ArrayList<node> open = new ArrayList<node>();
	private ArrayList<node> closed = new ArrayList<node>();
	
	 public void start(Stage primaryStage){
		 	run();
		 	
		    GridPane pane = new GridPane();
		    pane.setGridLinesVisible(true);
		    pane.setAlignment(Pos.CENTER);
	        
	        for (int i = 0; i < 8; i++) {
	            for (int j = 0; j < 8; j++) {
	            	
	            	Rectangle temp = new Rectangle(80,80);
	            	temp.setStroke(Color.BLACK);
	            	
	            	if(Grid[i][j]  == -1) {  
	            		temp.setFill(Color.BLACK);
	            	}
	            	else if(closed.contains(Grid[i][j])) {
	            		temp.setFill(Color.RED);
	            	}
	            	else {
	            		temp.setFill(Color.WHITE);
	            	}
            		pane.add(temp, j, i);
	        	}
	        }
	        
	        Scene scene = new Scene(pane, 640, 640);
	        primaryStage.setResizable(false);
	        primaryStage.setTitle("Grid"); // Set the stage title
	        primaryStage.setScene(scene); // Place the scene in the stage
	        primaryStage.show();
		}
	
	public void createObstacle() {
		int choice = (int)(Math.random()*3);
		int row = 0;
		int col = 0;
		switch(choice) {
		//T
		case 0: 
			row = (int)(Math.random()*6);
			col = (int)((Math.random()*6) + 1);
			
			Grid[row][col - 1] = -1;
			Grid[row][col] = -1;
			Grid[row][col + 1] = -1;
			Grid[row + 1][col] = -1;
			Grid[row + 2][col] = -1;
			break;
		//L
		case 1:
			row = (int)(Math.random()*4);
			col = (int)((Math.random()*7));
			
			Grid[row][col] = -1;
			Grid[row + 1][col] = -1;
			Grid[row + 2][col] = -1;
			Grid[row + 3][col] = -1;
			Grid[row + 3][col + 1] = -1;
			break;
		//I
		case 2:
			row = (int)(Math.random()*3);
			col = (int)(Math.random()*8);
			
			Grid[row][col] = -1;
			Grid[row + 1][col] = -1;
			Grid[row + 2][col] = -1;
			Grid[row + 3][col] = -1;
			Grid[row + 4][col] = -1;
			break;
		}
	}
	
	public int getIntInput() {
		int n = 0;
		Scanner in = new Scanner(System.in);
		try {
			n = in.nextInt() - 1;
			if(n < 0 || n > 7) {
				System.out.println("number must be >= 1 and <= 8 re-enter:");
				n = getIntInput() - 1;
			}
		}catch(Exception e){
			System.out.println("not a valid integer");
			in.nextLine();
			n = getIntInput() - 1;
		}
		return n;
	}
	
	public void getStart() {
		
		System.out.println("Enter Start Row : ");
		startRow = getIntInput();
		
		System.out.println("Enter Start Column : ");
		startCol = getIntInput();
		
		if(Grid[startRow][startCol] == -1) {
			System.out.println("position blocked");
			getStart();
		}
	
	}
	
	public void getEnd() {
		System.out.println("Enter End Row : ");
		endRow = getIntInput();
		
		System.out.println("Enter End Column : ");
		endCol = getIntInput();
		
		if(Grid[endRow][endCol] == -1) {
			System.out.println("position blocked");
			getEnd();
		}
	}
	
	public void findOpenNodes(node pNode) {
		if( pNode.x-1 >=0 && Grid[pNode.x-1][pNode.y] != -1 ) {
			open.add(new node(pNode.x-1,pNode.y,pNode.x,pNode.y));
		}
		
		if(pNode.x+1 <= 7 && Grid[pNode.x+1][pNode.y] != -1  ) {
			open.add(new node(pNode.x+1,pNode.y,pNode.x,pNode.y));
		}
		
		if(pNode.y-1 >= 0 && Grid[pNode.x][pNode.y-1] != -1 ) {
			open.add(new node(pNode.x,pNode.y-1,pNode.x,pNode.y));
		}
		
		if(pNode.y+1 <= 7 && Grid[pNode.x][pNode.y+1] != -1 ) {
			open.add(new node(pNode.x,pNode.y+1,pNode.x,pNode.y));
		}
	}
	
	public double heurestic(int row1, int row2, int col1, int col2) {
	
		double dx = Math.abs(row2 - row1);
		double dy = Math.abs(col2 - col1);
		return (dx + dy);
	}
	
	public double calculateNodeValue(int pathCost, int row, int col) {
		return  pathCost + heurestic(row, endRow, col , endCol);
	}
	
	public void run() {
		
		currentNode = new node();

		createObstacle();
		getStart();
		getEnd();
		
		while(open.size() > 0) {
			
			//currentNode = lowest f node; priority queue open list?
			//remove from open
			//add to closed;
			
			if(currentNode.x == endRow && currentNode.y == endCol ) {
				fin = true;
			}
			
			findOpenNodes(currentNode);
			
			//loop through children
			//if in closed end loop
			for(int i = 0; i < open.size(); i++) {
				double n = calculateNodeValue(pathCost, open.get(i)/10,open.get(i)%10);
				if( n < min) {
					min =  n;
					position = i;
				} 
				
			}
			//create f,g,h values
			//if in open already
			  //if g higher go to loop start
			
			//add child to open
			
		}
	}
	
	
	public static void main(String[] args) {
		launch(args);
  }

}
