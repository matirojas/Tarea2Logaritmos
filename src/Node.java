import java.util.ArrayList;

public class Node {
    ArrayList<Edge> edges;
    boolean isLeaf;
    boolean isRoot;
    Node suffixLink;
    int indexValue;
    public Node(int i){
        edges=new ArrayList<>();
        isLeaf=true;
        isRoot=false;
        indexValue=i;
        suffixLink.insert('a',i);

    }
    public void insert(char character,int i){
        for (Edge edge:
             edges) {
            edge.addChar(character);
        }
        edges.add(new Edge(character,i));
    }
}
