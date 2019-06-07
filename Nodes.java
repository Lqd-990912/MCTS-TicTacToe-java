import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.TreeMap;

/**
 *	Nodes��: ������MCTS�Ľڵ�
 * */

public class Nodes {

	//�ڵ������
	public int number_of_visits = 0;
	public TreeMap<Integer,Integer> results = new<Integer,Integer> TreeMap();
	public TicTacToeGameState state;
	public Nodes parent;
	public LinkedList <Nodes> children = new <Nodes> LinkedList();
	
	LinkedList<TicTacToeMove> untried_actions = new <TicTacToeMove> LinkedList();

	public Nodes(TicTacToeGameState state, Nodes parent) {
		this.state = state;
		this.parent = parent;
		
		results.put(-1,0);
		results.put(0,0);
		results.put(1,0);
	}

	//�ڵ�ķ���
	//�ж��Ƿ�Ϊ�ն˽ڵ�(���ǿ���Ϸ�Ƿ����)
	public boolean is_termial_node() {
		if(this.state.IsGameOver()) 
		{
			return true;
		}
		else 
		{
			return false;
		}
	}

	//MCTS���ķ���
	
	public LinkedList<TicTacToeMove> untried_action() {
			
		this.untried_actions = this.state.get_legal_actions();
		return this.untried_actions;
		
	}
	
	//�����ʽڵ��ͳ������: q->��ģ�⽱�� + n->�ܷ��ʴ��� ���ں����������UCT����
	public int q() {
		int wins = results.get(this.parent.state.next_to_move);
		int loses = results.get(-1*this.parent.state.next_to_move);
		return (wins - loses);
	}
	
	public int n() {
		return this.number_of_visits;
	}
	
	//��չ: �����Ѿ�ѡ��õ�δ��ȫչ���ڵ�������չ
	public Nodes expand() throws MyException {
		TicTacToeMove action = untried_actions.getLast();
		untried_actions.removeLast();
		TicTacToeGameState next_state = this.state.Move(action);
		Nodes child_node = new Nodes(next_state,parent);
		children.add(child_node);
		return child_node;
	}
	
	//�ش�: rollout���Ժ���������һ��״̬s,����һ���ж�a
	public Integer rollout() {
		TicTacToeGameState current_rollout_state = this.state;
		while(!current_rollout_state.IsGameOver())
		{
			LinkedList<TicTacToeMove> possible_moves = current_rollout_state.get_legal_actions();
			TicTacToeMove action = this.rollout_policy(possible_moves);
			try 
			{
				current_rollout_state = current_rollout_state.Move(action);
			} 
			catch (MyException e) {
				e.printStackTrace();
			}
		}
		return current_rollout_state.GameResult();
	}
	
	//���򴫲���һ���ĸ��¸��ڵ�
	public void backpropagate(Integer reward) {
		this.number_of_visits += 1;
		
		Integer tmp = results.get(reward) + 1;
		results.put(reward,tmp);
		
		if(this.parent != null)
		{
			this.parent.backpropagate(reward);
		}
		
	}
	
	//�ж��ǲ�����ȫչ���ڵ�
	public boolean is_fully_expanded() {
		if(this.untried_actions.size() == 0)
		{
			return true;
		}
		else 
		{
			return false;
		}
	}
	
	//�ҳ���һ��Ӧ���ߵ�����λ��(��һ���ڵ�Ӧ��Ϊ�ĸ�)
	//UCT(�������޺���) = q(��ģ�⽱��)/n(�ܷ��ʴ���) + c*sqrt(log(n)/n)
	public Nodes best_child() {

		double c_param = 1.4;
		double max_weights = 0;
		int    max_weights_index = 0;
		double tmp_weights;
		for(int i = 0; i < this.children.size(); i++)
		{
			Nodes tmp = this.children.get(i);
			tmp_weights = tmp.q() / tmp.n();
			tmp_weights += c_param * Math.sqrt((2 * Math.log(this.n())) / tmp.n());
			if(tmp_weights > max_weights)
			{
				max_weights = tmp_weights;
				max_weights_index = i;
			}
		}

		/**
		 *	bug1: ��һ�ε��õ�ʱ��children(���Nodes��LinkedList)����û��ֵ�����Գ�����Խ���쳣��
		 *	
		 *	����python��ʵ���������ж����ģ��ǵ������Ǹ�choices_weights����ŵ��Ǹ�ɶ��
		 *	1.children�϶�Ҳ��һ��list<Nodes>��
		 *	2.np.argmax(choices_weights)����϶�Ҳ�����ҳ�choices_weights���ֵ���±�
		 *	3.����choices_weights�ļ�����̣������Ծ��Ǽ���children�������ڵ��UCT����ֵ��
		 *	
		 *	����python���Ǹ��﷨���ǲ�֪����ô��ġ�
		 * */
		
		return this.children.get(max_weights_index);
	}
	
	private TicTacToeMove rollout_policy(LinkedList<TicTacToeMove> possible_moves) {
		
		int random = new Random().nextInt(possible_moves.size()-1);
		return possible_moves.get(random);
	}



}
