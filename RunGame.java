import java.util.Scanner;

/**
 *	JAVA实现MCTS算法，并可以完成三子棋游戏
 *
 *	1.RunGame: 
 *	main方法 - 游戏的执行
 * */

public class RunGame {

	//初始化部分: 创建棋盘数组和棋盘对象、创建一棵MCTS树
	//创建一个字符数组，用来表示棋盘
	static int[][] state = new int[3][3];
	
	//实例化一个棋盘状态对象
	static TicTacToeGameState initialBoardState = new TicTacToeGameState(state,1);
		
	//实例化一个根节点 + 由根节点root建立起MCTS树
	static Nodes root = new Nodes(initialBoardState,null);
	static Search mcts = new Search(root);
		
	 //打印出棋盘的状态信息
	private static void ShowState(int[][] board)
	{
		for(int i = 0; i < 3; i++)
		{
			System.out.print(i+"  |  ");
			for(int j = 0; j < 3; j++)
			{
				if(board[i][j] == 0)
				{
					System.out.print("_");
				}
				else if(board[i][j] == 1)
				{
					System.out.print("X");
				}
				else if(board[i][j] == -1)
				{
					System.out.print("O");
				}
				System.out.print("      ");
			}
			System.out.println("");
		}
		System.out.println("______________________________");
	}
	
	//输入玩家的移动 并 判断位置是不是合法的
	private static TicTacToeMove GetAction(TicTacToeGameState state) {
		
		//输入玩家要移动的位置
		System.out.print("Your move: ");
		String input = new Scanner(System.in).next();
		String[] buff = input.split(",");
		
		//将位置获取并实例化一个move对象
		int x = Integer.parseInt(buff[0]);
		int y = Integer.valueOf(buff[1]).intValue();
		TicTacToeMove move = new TicTacToeMove(x,y,-1);
		
		if(!state.IsMoveLegal(move))
		{
			System.out.println("invalid move");
			move = GetAction(state);
		}
		return move;
	}
	
	//判断比赛结果
	private static int JudgeGameResult(TicTacToeGameState state)
	{
		if(state.IsGameOver())
		{
			if(state.GameResult() == 1)
			{
				System.out.println("You lose!");
			}
			else if(state.GameResult() == 0)
			{
				System.out.println("Tie!");
			}
			else if(state.GameResult() == -1)
			{
				System.out.println("You win!");
			}
			return 1;
		}
		else
		{
			return -1;
		}
	}
	
	public static void main(String[] args) {
				
		/**
		 * 	bug1: best_action -> tree_policy -> best_child -> 
		 * 
		 * 	第一次调用的时候，children(存放Nodes的LinkedList)里面没有值，所以出现了越界异常。
		 * 	但是python的实现中又是有东西的，那到底它那个choices_weights里面放的是个啥，
		 * 	1.children肯定也是一个list<Nodes>，
		 * 	2.np.argmax(choices_weights)这个肯定也就是找出choices_weights最大值的下标
		 * 	3.根据choices_weights的计算过程，很明显就是计算children里面树节点的UCT函数值。
		 * 	
		 * 	但是python的那个语法还是不知道怎么搞的。
		 * */
		
		//模拟最优落子1000次
		Nodes best_node = mcts.best_action(10);
		TicTacToeGameState c_state = best_node.state;
		int c_board[][] = c_state.board;
		
		//主函数(运行游戏的主体)
		try 
		{
			//电脑下了第一个子之后的状态
			ShowState(c_board);
			
			//玩家和电脑开始循环下棋
			while(true)
			{
				//玩家下棋，直接输入要下的位置，然后输出下之后的状态
				TicTacToeMove move1 = GetAction(c_state);
				c_state = c_state.Move(move1);
				c_board = c_state.board;
				ShowState(c_board);
				
				//电脑下棋，跟第一次下棋一样，模拟1000次找出最优落子
				TicTacToeGameState board_state = new TicTacToeGameState(c_board,1);
				root = new Nodes(board_state,null);
				mcts = new Search(root);
				
				best_node = mcts.best_action(1000);
				c_state = best_node.state;
				c_board = c_state.board;
				ShowState(c_board);
				
				//每次下完一局棋都需要判断游戏是不是可以结束了
				if(JudgeGameResult(c_state) == 1)
				{
					break;
				}
				else
				{
					continue;
				}
			}
			
		} 
		catch (Exception e)
		{
			System.out.println(e);
		}
		
	}
}
