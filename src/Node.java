import java.util.ArrayList;

public class Node {
    ArrayList<Edge> edges;
    private boolean isLeaf;
    private boolean isRoot;
    private Node suffixLink;
    private int indexValue;

    public Node(int i){
        edges=new ArrayList<>();
        isLeaf=true;
        isRoot=false;
        indexValue=i;
        suffixLink.insert('a',i);

    }

    public boolean isRoot(){
        return this.isRoot;
    }

    public boolean isLeaf(){
        return this.isLeaf;
    }

    public void insert(char character,int i){
        for (Edge edge:
             edges) {

            if edge.hasLeaf() {

            }
            else{

            }
        }

        edges.add(new Edge(character,i));
    }
}
