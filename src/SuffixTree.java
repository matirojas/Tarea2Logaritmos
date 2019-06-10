import java.io.*;

public class SuffixTree {
    private Node root;
    private Node activeNode;
    private char activeEdge;
    private int activelength;
    private int remainder;

    public SuffixTree(String fileName){
        this.root = new Node(true);
        this.activeNode = this.root;
        this.activeEdge = '\0';
        this.activelength = 0;
        this.remainder = 0;
        //this.build(this.cleanText(fileName));
    }

    public String cleanText(String fileName){
        String cleaned="";
        
        try {
            BufferedReader br=new BufferedReader(new FileReader(fileName));
            String line=br.readLine();

            while(line!=null){
                line.replaceAll("","");//TODO signo de puntuacion
                line.replaceAll(""," ");//TODO espacios
                cleaned+=line;
                line=br.readLine();
            }
        } catch(IOException e) {
            System.out.println("fail");
        } finally {
            return cleaned;
        }
    }

    /*
    private void insertChar(char substring,int i) {

        root.insertChar(substring,i);

    }
    */
    private void insert(String s) {
        int count = 0;//count for values in the leaf.
        for (int i=0; i<s.length(); i++){
            remainder++;
            char c = s.charAt(i);

            String u = s.substring(count+remainder-activelength-1, count + remainder);//The whole string we want to insert

            Edge currentEdge = activeNode.searchString(u); //returns the edge that contains the string
            root.insertChar(c);
            if (currentEdge != null){ //case it exists


                if(currentEdge.valueLength() == activelength+1){
                    //Observation2
                    //String already exists but is the exact same value of the edge.
                    //We need to update the activeNode, activeLenght and activeEdge
                    //creo que aqui va un while, al menos segun la pregunta de stack overflow
                    activeNode = currentEdge.getNextNode();
                    activelength = 0;
                    activeEdge = '\0';
                }


                else if(activelength == 0){
                    //Observation 1
                    //String already exists and there is no activeEdge defined.
                    //We need to update the activeLenght and activeEdge
                    activelength++;
                    activeEdge = c;
                }
                else if (activelength > 0){
                    //Observation 1
                    //String already exists and there is activeEdge already defined.
                    //We need to update the activeLenght
                    activelength++;
                }

            }

            /*
            else if (activelength == 0){
                //Inserar la hoja.
                Node newLeaf = new Node(false);
                newLeaf.setNodeValue(count);
                activeNode.insertLeaf(newLeaf, Character.toString(s.charAt(count)));
                count++;
                remainder--;
            }
            */

            else{ // Edge not fount
                //Insert leaf while the remainder is >0
                Node suffixLinkNode = null;
                while(remainder != 0) {
                    Node newLeaf = new Node(false);
                    newLeaf.setNodeValue(count);
                    Node currentNode = activeNode.insertLeaf(newLeaf, s.substring(count+remainder-activelength-1, count + remainder));
                    count++;
                    remainder--;

                    //Rule 2
                    if (suffixLinkNode !=null){
                       suffixLinkNode.setSuffixLink(currentNode);
                    }
                    suffixLinkNode = currentNode;
                    //End Rule 2


                    //Rule 3
                    if (activeNode != root){
                        if (activeNode.getSuffixLink()!= null){
                            activeNode = activeNode.getSuffixLink();
                        }
                        else{
                            activeNode = root;
                        }
                    }
                    //End Rule 3

                    //Rule 1
                    else if (activelength >0) {
                        activelength--;
                        activeEdge = s.charAt(count);
                    }

                    else if (activelength == 0) {
                        activeEdge = '\0';
                    }
                    //End Rule 1
                }
            }

        }
    }

    public static void main(String[] args) {
        SuffixTree st = new SuffixTree("owo");
        st.insert("banana$"); //aguagualagua$
        //String a = "uwu";
        //String b = a.substring(0,a.length());
        System.out.println("uwu");

    }


    // Nos falta
    // Hacer los cambios de active point y cosas
    // Regla2 y Regla 3.
    //A priori

}


