
/**
 *	Search��: MCTS�����㷨
 * */

public class Search {
	
	//���ڵ�
	public Nodes root;

	//MCTS�������Ĺ��췽��
	public Search(Nodes node) {
		super();
		this.root = node;
	}

	//�㷨���ķ���
	//���������ӣ�simulations_numberΪģ������Ӵ���
	public Nodes best_action(int simulations_number) 
	{
		for(int i = 0; i < simulations_number; i++)
		{
//			System.out.println("best_action�����е�ѭ��");
			Nodes v = this.tree_policy();
			Integer reward = v.rollout();
			v.backpropagate(reward);
		}
		return this.root.best_child();
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
				current_node = current_node.best_child();
			}
		}
		return current_node;
	}

		
		
}
