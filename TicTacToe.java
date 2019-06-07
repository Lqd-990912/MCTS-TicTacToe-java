import java.util.ArrayList;
import java.util.LinkedList;

/**
 *	TicTacToeMove: ��������ƶ����� 
 *	TicTacToeGameState: ���������Ϸ״̬
 */

class TicTacToeMove {
	
	//�ƶ�����������: x���� ��y���ꡢvalue - ��ʾ����˭�µ���
	public int x_coordinate;
	public int y_coordinate;
	public int value;
	
	public TicTacToeMove(int x_coordinate, int y_coordinate, int value) {
		super();
		this.x_coordinate = x_coordinate;
		this.y_coordinate = y_coordinate;
		this.value = value;
	}

//    def __repr__(self): # repr()��������һ�����󣬽�����ת����Ϊ����������ȡ����ʽ
//    return "x:" + str(self.x_coordinate) + " y:" + str(self.y_coordinate) + " v:" + str(self.value)
}

class TicTacToeGameState {
	
	//���ֲ�ͬ�����ӣ�x -> 1,o -> -1
	private static final int x = 1;//����
	private static final int o = -1;//���
	
	//���̵�״̬��Ϣ
	public int [][] board;
	public int next_to_move = 1;
	public int board_size;
	
	//���캯��: �������̲���state + next_to_move
	public TicTacToeGameState(int[][] state, int next_to_move) {
		super();
		this.board = state;
		this.next_to_move = next_to_move;
		this.board_size = state.length;
	}

	//�ж���Ϸ���
	public Integer GameResult() {
		int Diag1Sum = 0;//����Խ���Ԫ��֮��
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
		
		int RowSum = 0;//������֮��
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
		
		//ƽ�ֵĳ���
//		int BoardSum = 0;//����������ȫ���ĺ�
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
		
		//���û���ҵ���\��\�Խ�����3\-3�ģ���˵����Ϸ��û�н���������������Ϸ
		return null;
	}
	
	//�ж���Ϸ�Ƿ����
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

	//�ж��ƶ��Ƿ�Ϸ�
	public boolean IsMoveLegal(TicTacToeMove move) {
		
		//�ж�����ƶ��ǲ�����ȷ����һ��
		if(move.value != this.next_to_move)
		{
			return false;
		}
		
		//�ж��ǲ�������������
		if(!(move.x_coordinate >= 0 && move.x_coordinate < 3))
		{
			return false;
		}
		if(!(move.y_coordinate >= 0 && move.y_coordinate < 3))
		{
			return false;
		}
		
		//�ж��ߵ�λ���ǲ����Ѿ��߹���
		if(this.board[move.x_coordinate][move.y_coordinate] == 0)
		{
			return true;
		}
		else
		{
			return false;
		}
		
	}
	
	//�ƶ�����
	public TicTacToeGameState Move(TicTacToeMove move) throws MyException
	{
		if(this.IsMoveLegal(move) == false)
		{
			throw new MyException("�ô��ƶ����Ϸ�");
		}
		//�ƶ��Ϸ�������һ���µ�״̬���õ���һ���ӵ�״̬(x->o | o->x)
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
	
	//�õ��Ϸ������ļ���
	public LinkedList<TicTacToeMove> get_legal_actions() {
		
		LinkedList<TicTacToeMove> indices = new <TicTacToeMove>LinkedList();//Move��������
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





















