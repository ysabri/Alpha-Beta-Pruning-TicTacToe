

public class TicTacToe {
	//Table will hold initial game state, solution has best next move
	static int row = 3;
	static int col = 4;
	static String[][] table = new String[row][col];
	static String solution = null;
	static boolean print=false;
	//handle user args and first call to recursive methods
	public static void main(String[] args) {
		//get arguments
		int argCount = 0;
		for(int i=0; i<row; i++){
			for(int j=0; j<col; j++){
				table[i][j] = args[argCount];
				argCount++;
			}
		}
		//check the Y flag
		if(args[args.length-1].equals("Y"))
			print = true;
		//count the number of prev plays to decide who starts next
		String[] currTable = tableToString().split("");
		int prevPlays = 0;
		for(String curr: currTable)
			if(curr.equals("X")||curr.equals("O"))
				prevPlays++;
		String state = tableToString();
		int alpha = -2;
		int beta = 2;
		//Human(max) player starts 
		if(prevPlays%2==0){
			//board is full
			if(terminalCheck(state)){
				solution = state;
			}
			else{
				//check first level successors and maximize their alpha score
				//and take the maximum state as solution
				int tempAlpha;
				String[] succ = succ(state,"O");
				for(String curr: succ){
					tempAlpha = max(alpha, minVal(curr,alpha,beta));
					if(tempAlpha>alpha){
						alpha = tempAlpha;
						solution = curr;
					}
				}
			}
			if(print){
				print(stringToTable(state.split(" ")[0]));
				System.out.println("Alpha: "+alpha+" Beta: "+ beta);}
			System.out.println("SOLUTION");
			print(stringToTable(solution));
		}
		else {
			//board is full
			if(terminalCheck(state)){
				solution = state;
			}
			//check first level successors and minimize their beta score
			//and take the minimum state as solution
			else{
				int tempBeta;
				String[] succ = succ(state,"X");
				for(String curr: succ){
					tempBeta = min(beta, maxVal(curr,alpha,beta));
					if(tempBeta<beta){
						beta = tempBeta;
						solution = curr;
					}
				}
			}
			if(print){
				print(stringToTable(state.split(" ")[0]));
				System.out.println("Alpha: "+alpha+" Beta: "+ beta);}
			System.out.println("SOLUTION");
			print(stringToTable(solution));
		}

	}
	//finds max alpha between current value and recursively returned value
	public static int maxVal (String state, int alpha, int beta){
		//check if state is goal
		if(stateScore(state)==-1||stateScore(state)==1){
			if(print){
				print(stringToTable(state.split(" ")[0]));
				System.out.println("Alpha: "+alpha+" Beta: "+ beta);}
			return stateScore(state);
		}
		//check is board is full
		else if(terminalCheck(state)){
			if(print){
				print(stringToTable(state.split(" ")[0]));
				System.out.println("Alpha: "+alpha+" Beta: "+ beta);}
			return stateScore(state);
		}
		//prune if necessary otherwise call minVal
		else{
			String[] succ = succ(state,"O");
			for(String curr: succ){
				alpha = max(alpha, minVal(curr,alpha,beta));
				if(alpha>=beta){
					if(print){
						print(stringToTable(state.split(" ")[0]));
						System.out.println("Alpha: "+alpha+" Beta: "+ beta);}
					return beta;
				}
			}
		}
		if(print){
			print(stringToTable(state.split(" ")[0]));
			System.out.println("Alpha: "+alpha+" Beta: "+ beta);}
		return alpha;
	}
	//finds min beta between current value and recursively returned value
	public static int minVal (String state, int alpha, int beta){
		//If board is full
		if(terminalCheck(state)){
			if(print){
				print(stringToTable(state.split(" ")[0]));
				System.out.println("Alpha: "+alpha+" Beta: "+ beta);}
			return stateScore(state);
		}
		//check if goal
		else if(stateScore(state)==-1||stateScore(state)==1){
			if(print){
				print(stringToTable(state.split(" ")[0]));
				System.out.println("Alpha: "+alpha+" Beta: "+ beta);}
			return stateScore(state);
		}
		//prune if necessary otherwise call maxVal
		else{
			String[] succ = succ(state,"X");
			for(String curr:succ){
				beta = min(beta,maxVal(curr, alpha,beta));
				if(alpha>=beta){
					if(print){
						print(stringToTable(state.split(" ")[0]));
						System.out.println("Alpha: "+alpha+" Beta: "+ beta);}
					return alpha;
				}
			}
		}
		if(print){
			print(stringToTable(state.split(" ")[0]));
			System.out.println("Alpha: "+alpha+" Beta: "+ beta);}
		return beta;
	}
	//return max of two elements
	public static int max(int a, int b){
		if(a>b)
			return a;
		else
			return b;
	}
	//return min of two elements
	public static int min(int a, int b){
		if(a<b)
			return a;
		else
			return b;
	}
	//check if no more moves are possible
	public static boolean terminalCheck(String state){
		String[] temp = state.split("");
		boolean returned = true;
		for(String curr:temp)
			if(curr.equals("_"))
				returned = false;
		return returned;
	}
	//check score of an index given a state and that index at the end
	public static int stateScore(String succState){
		String[] splitted = succState.split(" ");
		if(splitted.length<2)
			return 0;
		int index = Integer.parseInt(splitted[splitted.length-1]);
		String[][] currState = stringToTable(splitted[0]);
		String currEl = null;
		switch (index){
		case 0:
			currEl = currState[0][0];
			if((currEl.equals(currState[0][1])&&currEl.equals(currState[0][2]))
					|| (currEl.equals(currState[1][1])&&currEl.equals(currState[2][2])))
				return currEl.equals("O") ? 1 : -1;
			else
				return 0;
		case 1: 
			currEl = currState[0][1];
			if(currEl.equals(currState[0][2])&&currEl.equals(currState[0][3])&&!currEl.equals(currState[0][0])
					|| currEl.equals(currState[0][0])&&currEl.equals(currState[0][2])&&!currEl.equals(currState[0][3])
					|| currEl.equals(currState[1][2])&&currEl.equals(currState[2][3])
					|| currEl.equals(currState[1][1])&&currEl.equals(currState[2][1]))
				return currEl.equals("O") ? 1 : -1;
			else 
				return 0;
		case 2:
			currEl = currState[0][2];
			if(currEl.equals(currState[0][1])&&currEl.equals(currState[0][0])&&!currEl.equals(currState[0][3])
					|| currEl.equals(currState[0][1])&&currEl.equals(currState[0][3])&&!currEl.equals(currState[0][0])
					|| currEl.equals(currState[1][2])&&currEl.equals(currState[2][2])
					|| currEl.equals(currState[1][1])&&currEl.equals(currState[2][0]))
				return currEl.equals("O") ? 1 : -1;
			else 
				return 0;
		case 3: 
			currEl = currState[0][3];
			if(currEl.equals(currState[0][2])&&currEl.equals(currState[0][1])&&!currEl.equals(currState[0][0])
					|| currEl.equals(currState[1][3])&&currEl.equals(currState[2][3])
					|| currEl.equals(currState[1][2])&&currEl.equals(currState[2][1]))
				return currEl.equals("O") ? 1 : -1;
			else
				return 0;
		case 5:
			currEl = currState[1][1];
			if(currEl.equals(currState[0][1])&&currEl.equals(currState[2][1])
					|| currEl.equals(currState[0][0])&&currEl.equals(currState[2][2])
					|| currEl.equals(currState[1][2])&&currEl.equals(currState[1][3]))
				return currEl.equals("O") ? 1 : -1;
			else
				return 0;
		case 6:
			currEl = currState[1][2];
			if(currEl.equals(currState[0][2])&&currEl.equals(currState[2][2])
					|| currEl.equals(currState[1][1])&&currEl.equals(currState[1][3])
					|| currEl.equals(currState[2][1])&&currEl.equals(currState[0][3])
					|| currEl.equals(currState[0][1])&&currEl.equals(currState[2][3]))
				return currEl.equals("O") ? 1 : -1;
			else
				return 0;
		case 7:
			currEl = currState[1][3];
			if(currEl.equals(currState[1][2])&&currEl.equals(currState[1][1])
					|| currEl.equals(currState[0][3])&&currEl.equals(currState[2][3]))
				return currEl.equals("O") ? 1 : -1;
			else
				return 0;
		case 8:
			currEl = currState[2][0];
			if(currEl.equals(currState[2][1])&&currEl.equals(currState[2][2])&&!currEl.equals(currState[2][3])
					|| currEl.equals(currState[1][1])&& currEl.equals(currState[0][2]))
				return currEl.equals("O") ? 1 : -1;
			else
				return 0;
		case 9:
			currEl = currState[2][1];
			if(currEl.equals(currState[2][0])&&currEl.equals(currState[2][2])&&!currEl.equals(currState[2][3])
					|| currEl.equals(currState[2][2])&&currEl.equals(currState[2][3])&&!currEl.equals(currState[2][0])
					|| currEl.equals(currState[1][1])&&currEl.equals(currState[0][1])
					|| currEl.equals(currState[1][2])&&currEl.equals(currState[0][3]))
				return currEl.equals("O") ? 1 : -1;
			else
				return 0;
		case 10:
			currEl = currState[2][2];
			if(currEl.equals(currState[2][1])&&currEl.equals(currState[2][3])&&!currEl.equals(currState[2][0])
					|| currEl.equals(currState[2][1])&&currEl.equals(currState[2][0])&&!currEl.equals(currState[2][3])
					|| currEl.equals(currState[1][2])&&currEl.equals(currState[0][2])
					|| currEl.equals(currState[1][1])&&currEl.equals(currState[0][0]))
				return currEl.equals("O") ? 1 : -1;
			else
				return 0;
		case 11:
			currEl = currState[2][3];
			if((currEl.equals(currState[1][3])&&currEl.equals(currState[0][3]))
					|| (currEl.equals(currState[2][2])&&currEl.equals(currState[2][1]))
					|| (currEl.equals(currState[1][2])&&currEl.equals(currState[0][1])))
				return currEl.equals("O") ? 1 : -1;
			else 
				return 0;


		}
		return 0;
	}
	//generate successors with index of where element is added appended at the end
	public static String[] succ(String state,String player){
		String[] stateArray = state.split("");
		int succCount = 0;
		for(String curr: stateArray)
			if(curr.equals("_"))
				succCount++;
		String[] succ = new String[succCount];
		int currIndex=0;
		for(int i=0; i<succ.length;i++)
			for(int j=currIndex; j<stateArray.length;j++)
				if(stateArray[j].equals("_")){
					stateArray[j]=player;
					String temp = "";
					for(String curr:stateArray)
						temp = temp + curr;
					succ[i]=temp + " " + j;
					stateArray[j]="_";
					currIndex=j+1;
					break;
				}
		return succ;
	}
	//convert given state into a 2D table
	public static String[][] stringToTable(String state){
		String[][] returned = new String[row][col];
		String[] splitted = state.split("");
		int count = 0;
		for(int i=0; i<row;i++)
			for(int j=0; j<col; j++){
				returned[i][j]= splitted[count];
				count++;
			}
		return returned;
	}
	//convert global table into String
	public static String tableToString(){
		String returned="";
		for(int i=0; i<row; i++)
			for(int j=0;j<col; j++)
				returned = returned + table[i][j];
		return returned;
	}
	//convert given table into String
	public static String tableToString(String[][] table){
		String returned="";
		for(int i=0; i<row; i++)
			for(int j=0;j<col; j++)
				returned = returned + table[i][j];
		return returned;
	}
	//print global table
	public static void print() {
		for(int i=0; i<row; i++)
			for(int j=0; j<col; j++){
				if(j==col-1)
					System.out.println(table[i][j]);
				else
					System.out.print(table[i][j] + " ");
			}
	}
	//print given table
	public static void print(String[][] table) {
		for(int i=0; i<row; i++)
			for(int j=0; j<col; j++){
				if(j==col-1)
					System.out.println(table[i][j]);
				else
					System.out.print(table[i][j] + " ");
			}
	}

}
