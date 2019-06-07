import java.util.Scanner;

/**
 *	JAVAʵ��MCTS�㷨�������������������Ϸ
 *
 *	1.RunGame: 
 *	main���� - ��Ϸ��ִ��
 * */

public class RunGame {

	//��ʼ������: ����������������̶��󡢴���һ��MCTS��
	//����һ���ַ����飬������ʾ����
	static int[][] state = new int[3][3];
	
	//ʵ����һ������״̬����
	static TicTacToeGameState initialBoardState = new TicTacToeGameState(state,1);
		
	//ʵ����һ�����ڵ� + �ɸ��ڵ�root������MCTS��
	static Nodes root = new Nodes(initialBoardState,null);
	static Search mcts = new Search(root);
		
	 //��ӡ�����̵�״̬��Ϣ
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
	
	//������ҵ��ƶ� �� �ж�λ���ǲ��ǺϷ���
	private static TicTacToeMove GetAction(TicTacToeGameState state) {
		
		//�������Ҫ�ƶ���λ��
		System.out.print("Your move: ");
		String input = new Scanner(System.in).next();
		String[] buff = input.split(",");
		
		//��λ�û�ȡ��ʵ����һ��move����
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
	
	//�жϱ������
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
		 * 	��һ�ε��õ�ʱ��children(���Nodes��LinkedList)����û��ֵ�����Գ�����Խ���쳣��
		 * 	����python��ʵ���������ж����ģ��ǵ������Ǹ�choices_weights����ŵ��Ǹ�ɶ��
		 * 	1.children�϶�Ҳ��һ��list<Nodes>��
		 * 	2.np.argmax(choices_weights)����϶�Ҳ�����ҳ�choices_weights���ֵ���±�
		 * 	3.����choices_weights�ļ�����̣������Ծ��Ǽ���children�������ڵ��UCT����ֵ��
		 * 	
		 * 	����python���Ǹ��﷨���ǲ�֪����ô��ġ�
		 * */
		
		//ģ����������1000��
		Nodes best_node = mcts.best_action(10);
		TicTacToeGameState c_state = best_node.state;
		int c_board[][] = c_state.board;
		
		//������(������Ϸ������)
		try 
		{
			//�������˵�һ����֮���״̬
			ShowState(c_board);
			
			//��Һ͵��Կ�ʼѭ������
			while(true)
			{
				//������壬ֱ������Ҫ�µ�λ�ã�Ȼ�������֮���״̬
				TicTacToeMove move1 = GetAction(c_state);
				c_state = c_state.Move(move1);
				c_board = c_state.board;
				ShowState(c_board);
				
				//�������壬����һ������һ����ģ��1000���ҳ���������
				TicTacToeGameState board_state = new TicTacToeGameState(c_board,1);
				root = new Nodes(board_state,null);
				mcts = new Search(root);
				
				best_node = mcts.best_action(1000);
				c_state = best_node.state;
				c_board = c_state.board;
				ShowState(c_board);
				
				//ÿ������һ���嶼��Ҫ�ж���Ϸ�ǲ��ǿ��Խ�����
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
