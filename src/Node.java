import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Implementación de los nodos en el suffix tree
 */
public class Node {
    HashMap<Character, Edge> edges;
    private boolean isRoot;
    private Node suffixLink;
    private int nodeValue;

    public Node(boolean isRoot){
        edges=new HashMap<>();
        suffixLink = null;
        this.isRoot = isRoot;
        this.nodeValue = -1;
    }

    /**
     * Devuelve si el nodo es root o no.
     * @return un booleano, verdadero si es root, falso si no
     */
    public boolean isRoot(){
        return this.isRoot;
    }

    /**
     * Setea el valor del nodo
     * @param newValue el nuevo valor del nodo
     */
    public void setNodeValue(int newValue){
        this.nodeValue = newValue;
    }

    /**
     * Devuelve si el nodo es hoja o no.
     * @return un booleano, verdadero si es hoja, falso si no
     */
    public boolean isLeaf(){
        return edges.isEmpty();
    }

    /**
     * En todos sus aristas verifica lo siguiente: Si el nodo al que apunta el arista es una hoja, inserta
     * el caracter al final del valor del arista. En caso que sea un nodo interno, inserta el caracter en todas
     * las hojas a las que llega ese nodo interno.
     * @param character el caracter insertado
     */
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

    /**
     * Inserta un nodo hoja al nodo. El camino que debe tener entre el nodo y la nueva hoja es el string s.
     * Esto puede ocasionar que se tenga que generar un nodo intermedio para conectar la hoja con el nodo.
     * En caso que se cree un nuevo nodo intermedio, se devuelve este. En caso contrario, se devuelve el nodo
     * this.
     * @param newLeaf la hoja que se quiere insertar
     * @param s el camino hacia la hoja
     * @return el nodo que tiene el arista hacia la hoja
     */
    public Node insertLeaf(Node newLeaf, String s) {
        Node newNode = this;
        Edge e = edges.get(s.charAt(0));
        if (e!= null){
            String rest = e.trimValue(s.length()-1);
            newNode = new Node(false);
            Edge newEdge = new Edge(rest, e.getNextNode());
            newNode.insertEdge(rest.charAt(0), newEdge);
            e.setNextNode(newNode);
            Edge leafEdge = new Edge(Character.toString(s.charAt(s.length()-1)), newLeaf);
            newNode.insertEdge(s.charAt(s.length()-1), leafEdge);
        }
        else{
            Edge newEdge = new Edge(s, newLeaf);
            edges.put(s.charAt(0), newEdge);
        }
        return newNode;
    }

    /**
     * Busca si el string s esta contenido en alguno de los aristas del nodo
     * @param s el string buscado
     * @return El arista que contiene al string s. En caso que no  exista devuelve null.
     */
    public Edge searchString(String s) {
        Edge e = edges.get(s.charAt(0));
        if (e!=null){
            if(e.checkString(s)){
                return e;
            }
        }
        return null;
    }

    /**
     * Setea un suffix link al nodo
     * @param node al nodo a donde lleva el sufix link
     */
    public void setSuffixLink(Node node){
        this.suffixLink = node;
    }

    /**
     * Obtiene el nodo al que apunta el suffix link
     * @return el nodo
     */
    public Node getSuffixLink(){
        return this.suffixLink;
    }

    /**
     * Inserta el arista en el hashmap del nodo
     * @param c el primer caracter del valor del arista
     * @param e el arista
     */
    public void insertEdge(char c, Edge e){
        edges.put(c, e);
    }

    /**
     * Obtiene el arista que tenga el primer caracter con valor charAt
     * @param charAt el primer caracter del arista
     * @return el arista si se encuentra o null en caso que no.
     */
    public Edge getEdge(char charAt) {
        return edges.get(charAt);
    }

    /**
     * Obtiene todos las hojas a las que se puede llegar desde este nodo.
     * @return un arraylist con las hojas.
     */
    public ArrayList<Node> getAllLeafs() {
        Collection<Edge> edgesList = edges.values();

        ArrayList<Node> nodes = new ArrayList<>();
        if(this.isLeaf()){
            nodes.add(this);
            return nodes;
        }
        for (Edge edge: edgesList) {
            if( edge.hasLeaf()) {
                nodes.add(edge.getNextNode());
            }
            else{
                nodes.addAll(edge.getNextNode().getAllLeafs());
            }
        }

        return nodes;
    }

    /**
     * Hace un print del nodo
     * @param level la profundidad del nodo
     */
    public void printear(int level){
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<level; i++){
            sb.append("\t\t");
        }
        for (Edge edge :
                edges.values()) {
            if (edge.getNextNode().isLeaf()){
                System.out.println(sb.toString() + "----"+ edge.getValue() + "----" + edge.getNextNode().getNodeValue());
            }
            else {
                System.out.println(sb.toString() + "----" + edge.getValue() + "----");
            }
            edge.getNextNode().printear(level+1);
        }

    }

    /**
     * Retorna el valor del nodo
     * @return el valor del nodo
     */
    public int getNodeValue(){
        return nodeValue;
    }

    /**
     * Retorna los aristas del nodo
     * @return los aristas del nodo
     */
    public Collection<Edge> getEdges(){
        return edges.values();
    }
}
