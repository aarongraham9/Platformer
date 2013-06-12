import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Scanner;

public class Main {
	
	private static final String GAME_NAME = "The Platformer";
	private String mapName = "Map 1";
	private int width = -1;
	private int height = -1;
	
	private char[][] map = null;
	private int c_position_x = -1;
	private int c_position_y = -1;
	private int e_position_x = -1;
	private int e_position_y = -1;
	
	private static final String STR_MAP_PREFIX = "map";
	private static int mapNumber = 0;
	private static final String STR_MAP_SUFFIX = ".txt";
	
	private void init_map(String map_path) {
		URL url = null;
		BufferedReader br = null;
		StringBuilder sb = null;
		Scanner sc = null;
	    		
		try {
			url = (Main.class.getResource(map_path));
			if(url == null){
				//Either done the game or no maps can be found.
				System.out.println("Error!");
				System.exit(-1);
			}
			br = new BufferedReader(new InputStreamReader(url.openStream()));
			sc = new Scanner(new InputStreamReader(url.openStream()));
	        sb = new StringBuilder();
	        String line = br.readLine();
	        
	        width = sc.nextInt();
	    	height = sc.nextInt();
	    	map = new char[height][width];
	        
	        mapName = br.readLine().trim();
    
	        line = br.readLine();

	        while (line != null) {
	            sb.append(line);
	            sb.append("\n");
	            line = br.readLine();
	        }
		} catch (FileNotFoundException e) {
			e.printStackTrace();
	    } catch (IOException e) {
			e.printStackTrace();
		} finally {
	        try {
				br.close();
				sc.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }
	    
	    String everything = sb.toString();
	    //System.out.println(everything);
	    
	    int everything_count_start = 0;
	    int everything_count_end = width;
	    int str_count_start = 0;
	    
	    for(int i = 0; i < height;i++){
	    	
	    	if(everything.charAt(everything_count_start) == '\n'){
	    		everything_count_start++;
	    		everything_count_end++;
	    	}
	    	
	    	String str = everything.substring(everything_count_start, everything_count_end);
	    	
	    	for(int j = 0; j < width;j++){
	    		
	    		char c = str.charAt(str_count_start);
	    		
	    		if(c == ' '){
	    			c = 'w';
	    		}else if(c == '\n'){
	    			break;
	    		}else if(c == 'c'){
	    			c_position_x = j;
	    			c_position_y = i;
	    			map[i][j] = c;
	    		}else if(c == 'e'){
	    			e_position_x = j;
	    			e_position_y = i;
	    			map[i][j] = c;
	    		}else {
	    			map[i][j] = c;
	    		}

	    		everything_count_start++;
	    		str_count_start++;
	    	}
	    	everything_count_end = everything_count_end + width;
	    	str_count_start = 0;
	    }
	    //System.out.println(Arrays.deepToString(map));
	}
	
	
	private void engine() {
		
		if(mapNumber == 0) {
			System.out.println("Welcome to " + GAME_NAME + "!");
		}
		
		System.out.println("Loading map '" + mapName + "'");
		System.out.println("'c' is your character.");
		System.out.println("'e' is the exit (goal).");
		System.out.println("'-' are platforms.");
		System.out.println("Commands:");
		System.out.println("'l' moves you left 1 space.");
		System.out.println("'r' moves you right 1 space.");
		System.out.println("' ' (space bar) makes you jump up two spaces.");
		System.out.println("'q' to quit the game.");
		System.out.println("Enjoy!");
		
		char command = ' ';
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		boolean win = false;
		
		do {
			try {
				printMap();
				
				System.out.println("______________________");
				System.out.print("Please Enter your Command: ");
				String s = br.readLine();
				command = s.charAt(0);
				
				System.out.println("Your command is: '" + command + "'");
				
				if(!updateBoardWithCommand(command)) {
					System.out.println("Invalid command! Try another.");
				} else {
					win = checkWin();
				}
				
				System.out.println("______________________");
			
	    	} catch (IOException IOE) {
	    		//IOE.printStackTrace();
			} catch (StringIndexOutOfBoundsException SIOOBE) {
				//SIOOBE.printStackTrace();
			}
		} while((command != 'q') && !win);
		
		if(win) {
			try {
				printMap();
				System.out.println("______________________");
				System.out.println("You Win!!!!!!!!!!!");
				System.out.println("______________________");
					
				mapNumber++;
					
				String map_path = STR_MAP_PREFIX + mapNumber + STR_MAP_SUFFIX;
					
				URL url = (Main.class.getResource(map_path));
					
				if(url == null){
					//Either done the game or no maps can be found.
					System.out.println("Congrats! You have Completed the Game!");
					System.out.println("______________________");
				}else{
					
					System.out.println("Would you like to play again? ");
					System.out.println("'y' for Yes  or 'n' for No.");
					System.out.print("Please Enter your Command: ");
					String s = br.readLine();
					command = s.charAt(0);
					System.out.println("Your command is: '" + command + "'");						System.out.println("______________________");
					System.out.println("______________________");
					System.out.println("______________________");
									
					switch (command) {
						case 'y': //Play Again
							init_map(map_path);
							engine();
							break;
						case 'n': //Done Playing
							System.out.println("Quitting.....");
							break;
						default:
							System.out.println("Invalid command! Try another.");
						break;
					}
			
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}		
	}
	
	
	private boolean updateBoardWithCommand(char command) {
		
		boolean result = false;
		
		switch (command) {
			case ' ': result = updateBoardWithCommandHelper(0); //Jump
				break;
			//To-Do: Jump Down command
			case 'l': result = updateBoardWithCommandHelper(1); //Move Left
				break;
			case 'r': result = updateBoardWithCommandHelper(2); //Move Right
				break;
			case 'q': result = true; //Return True to quit the game
				System.out.println("Quitting.....");
				break;
			default:
				break;
		}
		
		return result;
	}
	
	
	private boolean updateBoardWithCommandHelper(int command) {
		boolean result = false;
		
		switch (command) {
		case 0: //Jump
			if(c_position_y > 1) {
				if(map[c_position_y - 1][c_position_x] == '-') {
					map[c_position_y][c_position_x] = 'w';
					map[c_position_y - 2][c_position_x] = 'c';
					
					c_position_y = c_position_y - 2;
					
					result = true;
				}
			}
			break;
		case 1: //Move Left
			if(c_position_x > 0) {
				if(map[c_position_y + 1][c_position_x - 1] == '-') {
					map[c_position_y][c_position_x] = 'w';
					map[c_position_y][c_position_x - 1] = 'c';
					
					c_position_x = c_position_x - 1;
					
					result = true;
				}else {//Fall Down...
					map[c_position_y][c_position_x] = 'w';
					map[c_position_y + 2][c_position_x - 1] = 'c';
					
					c_position_x = c_position_x - 1;
					c_position_y = c_position_y + 2;
					
					result = true;
				}
			}
			break;
		case 2: //Move Right
			if(c_position_x < width - 1) {
				if(map[c_position_y + 1][c_position_x + 1] == '-') {
					map[c_position_y][c_position_x] = 'w';
					map[c_position_y][c_position_x + 1] = 'c';
					
					c_position_x = c_position_x + 1;
					
					result = true;
				}else {//Fall Down...
					map[c_position_y][c_position_x] = 'w';
					map[c_position_y + 2][c_position_x + 1] = 'c';
					
					c_position_x = c_position_x + 1;
					c_position_y = c_position_y + 2;
					
					result = true;
				}
			}
			break;
		default:
			break;
		}		
		
		return result;
		
	}
	
	
	private boolean checkWin() {
		boolean result = false;
		
		if(map[e_position_y][e_position_x] == 'c') {
			result = true;
		}
		
		return result;
	}
	
	
	private void printMap() {
		System.out.println("");
		
		for(int i = 0; i < height;i++){
	    	for(int j = 0; j < width;j++){
	    		char c = map[i][j];
	    		
	    		if(c == 'w'){
	    			c = ' ';
	    		}
	    		
	    		System.out.print("" + c);
	    	}
	    	
	    	System.out.println("");
	    }
	}	
	
	
	public static void main(String[] args) {
		
		Main m = new Main();
		m.init_map(STR_MAP_PREFIX + mapNumber + STR_MAP_SUFFIX);
		m.engine();
		
	}
}