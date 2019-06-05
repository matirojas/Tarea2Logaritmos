public class Edge {
    private String value;
    private Node nextNode;

    public Edge(char Character, int i ){
        this.value = String.valueOf(Character);
        nextNode=new Node(i);
    }

    public void addChar(char nextChar){
        this.value+=nextChar;
    }

    public String trimValue(int i){
        String aux = value.substring(i+1);
        this.value = value.substring(0,i);
        return aux;
    }
    public boolean hasLeaf(){
        return nextNode.isLeaf();
    }

    public Node getNextNode(){
        return nextNode;
    }

    public boolean checkString(String s){
        return value.contains(s) &&  value.length() == s.length();
    }




}
