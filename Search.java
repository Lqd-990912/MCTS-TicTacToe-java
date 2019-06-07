
/**
 *	Search类: MCTS搜索算法
 * */

public class Search {
	
	//根节点
	public Nodes root;

	//MCTS搜索树的构造方法
	public Search(Nodes node) {
		super();
		this.root = node;
	}

	//算法核心方法
	//找最优落子，simulations_number为模拟的落子次数
	public Nodes best_action(int simulations_number) 
	{
		for(int i = 0; i < simulations_number; i++)
		{
//			System.out.println("best_action方法中的循环");
			Nodes v = this.tree_policy();
			Integer reward = v.rollout();
			v.backpropagate(reward);
		}
		return this.root.best_child(0);// 此处的best_child()传进去的c=0
	}
	
	public Nodes tree_policy()
	{
		Nodes current_node = root;
		while(!current_node.is_termial_node())
		{
			if(!current_node.is_fully_expanded())
			{
				try 
				{
					return current_node.expand();
				} 
				catch (MyException e) 
				{
					e.printStackTrace();
				}
			}
			else
			{
				current_node = current_node.best_child();// 此处的best_child()用默认参数
			}
		}
		return current_node;
	}

		
		
}
