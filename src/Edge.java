public class Edge {
    private String value;
    private Node nextNode;

    public Edge(String value, Node nextNode){
        this.value = value;
        this.nextNode= nextNode;
    }

    public void addChar(char nextChar){
        this.value+=nextChar;
    }

    public String trimValue(int i){
        String aux = value.substring(i);
        this.value = value.substring(0,i);
        //System.out.println(aux);
        //System.out.println(this.value);
        return aux;
    }
    public boolean hasLeaf(){
        return nextNode.isLeaf();
    }

    public Node getNextNode(){
        return nextNode;
    }

    public boolean checkString(String s){
        int length = s.length();

        if (length > valueLength()){
            String s2 = s.substring(0,valueLength());
            return s2.equals(value);
        }
        String subString = value.substring(0,length);
        return subString.equals(s);
    }

    public int valueLength(){
        return value.length();
    }


    public void setNextNode(Node newNode){
        this.nextNode = newNode;
    }
    public String getValue(){
        return value;
    }


    public char getCharValue(int i) {
        return value.charAt(i);
    }

}
