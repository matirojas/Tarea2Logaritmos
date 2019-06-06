import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class Node {
    HashMap<Character, Edge> edges;
    private boolean isRoot;
    private Node suffixLink;
    private int nodeValue;

    public Node(boolean isRoot){
        edges=new HashMap<>();
        suffixLink = null;
        this.isRoot = isRoot;
    }

    public boolean isRoot(){
        return this.isRoot;
    }

    public void setNodeValue(int newValue){
        this.nodeValue = newValue;
    }

    public boolean isLeaf(){
        return edges.isEmpty();
    }

    public void insertChar(char character){
        Collection<Edge> edgesList = edges.values();
        for (Edge edge: edgesList) {
            if( edge.hasLeaf()) {
                edge.addChar(character);
            }
            else{
                edge.getNextNode().insertChar(character);
            }
        }
    }

    //Check the pattern in the input string if it exists in one of the edges.

    public void checkStringEdges(String s){
        /*for (Edge edge: edges){

        }
        */

    }

    public void insertLeaf(Node newLeaf, String s) {
        //String w = s.substring(0, s.length()-1);
        Edge e = edges.get(s.charAt(0));
        if (e!= null){
            String rest = e.trimValue(s.length()-1);
            Node newNode = new Node(false);
            Edge newEdge = new Edge(rest, e.getNextNode());
            newNode.insertEdge(rest.charAt(0), newEdge);
            e.setNextNode(newNode);
            Edge leafEdge = new Edge(Character.toString(s.charAt(s.length()-1)), newLeaf);
            newNode.insertEdge(s.charAt(s.length()-1), leafEdge);




            /*
            String rest = e.trimValue(s.length()-1);
            edges.remove(s.charAt(0)); //quitamos arista e
            Node newNode = new Node(false);
            newNode.insertEdge();
            Edge newEdge = new Edge(s.substring(s.length()-1,s.length()),newLeaf);
            newNode.insertEdge(newEdge);
            Edge newEdge2 = new Edge(rest, newNode);
            this.edges.add(newEdge2);
             */

            //e.split(newLeaf, s);
        }
        else{
            Edge newEdge = new Edge(s, newLeaf);
            edges.put(s.charAt(0), newEdge);
        }

        //caso bacan
        //Edge newEdge = new Edge(nodeValue, newLeaf);
        //edges.add(newEdge);
    }

    public int searchString(String s) {
        int value = -1;
        Edge e = edges.get(s.charAt(0));
        if(e != null) {
            if (e.checkString(s)) {
                value = e.valueLength();
            }
        }
        return value;
        /*
        for (Edge edge: edges){
            if (edge.checkString(s)){
                value = edge.valueLength();
                break;
            }
        }
        */

    }

    public void insertEdge(char c, Edge e){
        edges.put(c, e);
    }


    //

}
/**
 * edges=Node.getEgdes
 * for edge in edge{
 *     if edge.isLeaf()
 *     edge.insertChar
 *     else:
 *
 * }
 *
 *
 *
 */