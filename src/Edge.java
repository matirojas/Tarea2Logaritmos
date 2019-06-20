/**
 * Implementación de los aristas del suffix tree.
 */
public class Edge {
    private String value;
    private StringBuilder val;
    private Node nextNode;

    public Edge(String value, Node nextNode){
        this.val = new StringBuilder(value);
        this.nextNode= nextNode;
    }

    /**
     * Añade un caracter al final del valor del arista
     * @param nextChar el caracter que se quiere agregar.
     */
    public void addChar(char nextChar){
        this.val.append(nextChar);
    }

    /**
     * Acorta el valor del arista
     * @param i el largo en el que debe quedar el arista
     * @return el largo restante en formato de string del arista
     */
    public String trimValue(int i){
        String value=val.toString();
        String aux = value.substring(i);
        val.delete(i,val.length());
        //System.out.println(aux);
        //System.out.println(this.value);
        return aux;
    }

    /**
     * Returna si el nodo al que esta apuntando el arista es una hoja
     * @return verdadero si es hoja, falso si no.
     */
    public boolean hasLeaf(){
        return nextNode.isLeaf();
    }

    /**
     * Obtiene el nodo que apunta el arista
     * @return el nodo al que apunta el arista
     */
    public Node getNextNode(){
        return nextNode;
    }

    /**
     * Chequea si un string esta contenido en el valor del arista o si el arista esta contenido en el string
     * @param s el string a evaluar
     * @return devuelve verdadero si esta contenido, falso si es que no
     */
    public boolean checkString(String s){
        int length = s.length();

        if (length > valueLength()){
            String s2 = s.substring(0,valueLength());
            return s2.equals(val.toString());
        }
        String subString = val.toString().substring(0,length);
        return subString.equals(s);
    }

    /**
     * Devuelve el largo del valor del arista
     * @return el largo del valor del arista
     */
    public int valueLength(){
        return val.length();
    }

    /**
     * Setea el nodo al cual apunta el arista
     * @param newNode el nuevo nodo seteado
     */
    public void setNextNode(Node newNode){
        this.nextNode = newNode;
    }

    /**
     * Obtiene el valor del arista
     * @return el valor del arista en formato de string
     */
    public String getValue(){
        return val.toString();
    }

    /**
     * Obtiene el caracter del valor del arista en una determinada posición
     * @param i la posición
     * @return el caracter en esa posición
     */
    public char getCharValue(int i) {

        return val.toString().charAt(i);
    }

}
