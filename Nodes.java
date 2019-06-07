import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.TreeMap;

/**
 *	Nodes类: 构成了MCTS的节点
 * */

public class Nodes {

	//节点的属性
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

	//节点的方法
	//判断是否为终端节点(就是看游戏是否结束)
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

	//MCTS核心方法
	
	public LinkedList<TicTacToeMove> untried_action() {
			
		this.untried_actions = this.state.get_legal_actions();
		return this.untried_actions;
		
	}
	
	//被访问节点的统计数据: q->总模拟奖励 + n->总访问次数 用于后面的来计算UCT函数
	public int q() {
		int wins = results.get(this.parent.state.next_to_move);
		int loses = results.get(-1*this.parent.state.next_to_move);
		return (wins - loses);
	}
	
	public int n() {
		return this.number_of_visits;
	}
	
	//扩展: 根据已经选择好的未完全展开节点向下扩展
	public Nodes expand() throws MyException {
		TicTacToeMove action = untried_actions.getLast();
		untried_actions.removeLast();
		TicTacToeGameState next_state = this.state.Move(action);
		Nodes child_node = new Nodes(next_state,parent);
		children.add(child_node);
		return child_node;
	}
	
	//回传: rollout策略函数，输入一个状态s,返回一次行动a
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
	
	//反向传播，一层层的更新父节点
	public void backpropagate(Integer reward) {
		this.number_of_visits += 1;
		
		Integer tmp = results.get(reward) + 1;
		results.put(reward,tmp);
		
		if(this.parent != null)
		{
			this.parent.backpropagate(reward);
		}
		
	}
	
	//判断是不是完全展开节点
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
	
	//找出下一步应该走的最优位置(下一个节点应该为哪个)
	//UCT(置信上限函数) = q(总模拟奖励)/n(总访问次数) + c*sqrt(log(n)/n)
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
		 *	bug1: 第一次调用的时候，children(存放Nodes的LinkedList)里面没有值，所以出现了越界异常。
		 *	
		 *	但是python的实现中又是有东西的，那到底它那个choices_weights里面放的是个啥，
		 *	1.children肯定也是一个list<Nodes>，
		 *	2.np.argmax(choices_weights)这个肯定也就是找出choices_weights最大值的下标
		 *	3.根据choices_weights的计算过程，很明显就是计算children里面树节点的UCT函数值。
		 *	
		 *	但是python的那个语法还是不知道怎么搞的。
		 * */
		
		return this.children.get(max_weights_index);
	}
	
	private TicTacToeMove rollout_policy(LinkedList<TicTacToeMove> possible_moves) {
		
		int random = new Random().nextInt(possible_moves.size()-1);
		return possible_moves.get(random);
	}



}
