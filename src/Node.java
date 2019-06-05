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

    //Siempre lo invoca el root
    public void insert(char character,int i){
        for (Edge edge:
             edges) {

            if( edge.hasLeaf()) {
                edge.addChar(character);
            }
            else{
                edge.getNextNode().insert(character, i);
            }
        }
    }

    //Check the pattern in the input string if it exists in one of the edges.

    public void checkStringEdges(String s){
        for (Edge edge: edges){

        }

    }

    //

}
/**
 * edges=Node.getEgdes
 * for edge in edge{
 *     if edge.isLeaf()
 *     edge.insert
 *     else:
 *
 * }
 *
 *
 *
 */