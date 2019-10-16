import org.junit.Test;

import java.util.ArrayList;
import java.util.Stack;

public class binarySearchTest {
    public TreeNode rootNode;

    @Test
    public void test1(){
        addNodes();
        ArrayList list = new ArrayList<Integer>();

        //迭代遍历解法
        Stack<TreeNode> stack = new Stack<>();
        stack.push(rootNode);
        while (!stack.isEmpty()){
            TreeNode node = stack.pop();
            list.add(node.value);
            if(node.right!=null){
                stack.push(node.right);
            }

            if(node.left!=null){
                stack.push(node.left);
            }
        }
        list.forEach(System.out::println);

        System.out.println("max："+maxDeath(rootNode));

    }
    int maxDeath(TreeNode node){
        if(node==null){
            return 0;
        }
        int left = maxDeath(node.left);
        int right = maxDeath(node.right);
        return Math.max(left,right) + 1;
    }
    public void addNodes(){
        rootNode = new TreeNode(5);
        this.add(7);
        this.add(4);
        this.add(3);
        this.add(9);
        this.add(1);
        this.add(6);
    }

    public void add(int value){
        TreeNode node = new TreeNode(value);
        if(rootNode==null){
            rootNode=node;
        }else{
            rootNode.add(node);
        }

    }
}

class TreeNode{
    public TreeNode(int i){
        this.value=i;
    }
    int value;
    TreeNode left;
    TreeNode right;


    void add(TreeNode node){
        if(this.value>=node.value){
            if(this.left==null){
                this.left=node;
            }else{
                this.left.add(node);
            }

        }else{
            if(this.right==null){
                this.right=node;
            }else{
                this.right.add(node);
            }

        }
    }


}