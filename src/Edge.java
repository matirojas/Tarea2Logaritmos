public class Edge {
    String nextString;
    Node nextNode;

    public Edge(char Character, int i ){
        this.nextString = String.valueOf(Character);
        nextNode=new Node(i);
    }

    public void addChar(char nextChar){
        nextString+=nextChar;
    }
}
