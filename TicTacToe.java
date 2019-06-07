import java.util.ArrayList;
import java.util.LinkedList;

/**
 *	TicTacToeMove: 井字棋的移动方法 
 *	TicTacToeGameState: 井字棋的游戏状态
 */

class TicTacToeMove {
	
	//移动的三个性质: x坐标 、y坐标、value - 表示这是谁下的棋
	public int x_coordinate;
	public int y_coordinate;
	public int value;
	
	public TicTacToeMove(int x_coordinate, int y_coordinate, int value) {
		super();
		this.x_coordinate = x_coordinate;
		this.y_coordinate = y_coordinate;
		this.value = value;
	}
}

class TicTacToeGameState {
	
	//两种不同的棋子，x -> 1,o -> -1
	private static final int x = 1;//电脑
	private static final int o = -1;//玩家
	
	//棋盘的状态信息
	public int [][] board;
	public int next_to_move = 1;
	public int board_size;
	
	//构造函数: 传入棋盘参数state + next_to_move
	public TicTacToeGameState(int[][] state, int next_to_move) {
		super();
		this.board = state;
		this.next_to_move = next_to_move;
		this.board_size = state.length;
	}

	//判断游戏结果
	public Integer GameResult() {
		int Diag1Sum = 0;//计算对角线元素之和
		int Diag2Sum = 0;
		
		for(int i = 0; i < board.length; i++)
		{
			Diag1Sum += board[i][i];
		}
		for(int i = 0; i < board.length; i++)
		{
			Diag2Sum += board[board.length-1-i][i];
		}
		if(Diag1Sum == 3 || Diag2Sum == 3)
		{
			return 1;
		}
		if(Diag1Sum == -3 || Diag2Sum == -3)
		{
			return -1;
		}
		
		int RowSum = 0;//计算行之和
		for(int i = 0; i < board.length; i++)
		{
			RowSum = 0;
			for(int j = 0; j < board.length; j++)
			{
				RowSum += board[i][j];
			}
			if(RowSum == 3)
			{
				return 1;
			}
			else if(RowSum == -3)
			{
				return -1;
			}
			
		}
		int ColSum = 0;
		for(int j = 0; j < board.length; j++)
		{
			ColSum = 0;
			for(int i = 0; i < board.length; i++)
			{
				ColSum += board[j][i];
			}
			if(ColSum == 3)
			{
				return 1;
			}
			else if(ColSum == -3)
			{
				return -1;
			}
		}
		
		//平局的出现(未考虑平局出现)
//		int BoardSum = 0;//计算数组中全部的和
//		for(int i = 0; i < board.length; i++)
//		{
//			for(int j = 0; j < board.length; j++)
//			{
//				BoardSum += board[i][j];
//			}
//		}
//		if(BoardSum == 0)
//		{
//			return 0;
//		}
		
		//如果没有找到行\列\对角线有3\-3的，就说明游戏还没有结束，继续进行游戏
		return null;
	}
	
	//判断游戏是否结束
	public boolean IsGameOver() {

		if(this.GameResult() == null)
		{
			return false;
		}
		else 
		{
			return true;
		}
	}

	//判断移动是否合法
	public boolean IsMoveLegal(TicTacToeMove move) {
		
		//判断玩家移动是不是正确的下一步
		if(move.value != this.next_to_move)
		{
			return false;
		}
		
		//判断是不是走在棋盘内
		if(!(move.x_coordinate >= 0 && move.x_coordinate < 3))
		{
			return false;
		}
		if(!(move.y_coordinate >= 0 && move.y_coordinate < 3))
		{
			return false;
		}
		
		//判断走的位置是不是已经走过了
		if(this.board[move.x_coordinate][move.y_coordinate] == 0)
		{
			return true;
		}
		else
		{
			return false;
		}
		
	}
	
	//移动棋子
	public TicTacToeGameState Move(TicTacToeMove move) throws MyException
	{
		if(this.IsMoveLegal(move) == false)
		{
			throw new MyException("该次移动不合法");
		}
		//移动合法，返回一个新的状态，得到下一步子的状态(x->o | o->x)
		int[][] new_board;
		new_board = board;
		new_board[move.x_coordinate][move.y_coordinate] = move.value;
		if(next_to_move == x)
		{
			next_to_move = o;
		}
		else
		{
			next_to_move = x;
		}
		return new TicTacToeGameState(new_board,next_to_move);
			
	}
	
	//得到合法动作的集合
	public LinkedList<TicTacToeMove> get_legal_actions() {
		
		LinkedList<TicTacToeMove> indices = new <TicTacToeMove>LinkedList();//Move对象数组
		int index = 0;
		for(int i = 0; i < board.length; i++)
		{
			for(int j = 0; j < board.length; j++)
			{
				if(board[i][j] == 0)
				{
					indices.add(new TicTacToeMove(i,j,this.next_to_move));
				}
			}
		}
		return indices;
	}

	
}





















