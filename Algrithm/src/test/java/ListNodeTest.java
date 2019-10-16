import org.junit.Test;

public class ListNodeTest {

    @Test
    public void testReverse(){
        Node header = getNodes();
        /*while (header!=null){
            System.out.println(header.value);
            header=header.next;
        }*/

        Node prev = null;
        while(header!=null){
            Node tmp = header.next;
            header.next=prev;
            prev=header;
            header=tmp;
        }

        while (prev!=null){
            System.out.print(prev.value);
            prev=prev.next;
        }
    }

    @Test
    public void getK(){
        Node n1 = getNodes();
        Node n2 =n1;

        int k=2;

        while (n1!=null){
            n1=n1.next;
            if(k!=0){
                k--;
            }else {
                n2=n2.next;
            }
        }

        System.out.println(n2.value);
    }

    Node getNodes(){
        Node header = new Node(1);

        header.addNext(2).addNext(3).addNext(4).addNext(5).addNext(6);
        return header;
    }
}

class Node{
    Node next;
    int value;

    public Node(int v){
       value=v;
       next = null;
    }

    public Node addNext(int v){
        this.next=new Node(v);
        return this.next;
    }
}