import java.io.*;
import java.util.LinkedList;
import java.util.Queue;

public class SuffixTree {
    private Node root;
    private Node activeNode;
    private char activeEdge;
    private int activelength;
    private int remainder;

    public SuffixTree(String fileName) {
        this.root = new Node(true);
        this.activeNode = this.root;
        this.activeEdge = '\0';
        this.activelength = 0;
        this.remainder = 0;
        //this.build(this.cleanText(fileName));
    }

    public String cleanText(String fileName) {
        String cleaned = "";

        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line = br.readLine();

            while (line != null) {
                line.replaceAll("", "");//TODO signo de puntuacion
                line.replaceAll("", " ");//TODO espacios
                cleaned += line;
                line = br.readLine();
            }
        } catch (IOException e) {
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
        for (int i = 0; i < s.length(); i++) {
            remainder++;
            activelength++;


            char c = s.charAt(i);
            root.insertChar(c);

            Node suffixLinkNode = null;
            while (remainder != 0) {


                String u = s.substring(count + remainder - activelength, count + remainder);//The whole string we want to insert



                System.out.println("\n-------------------------------------------------------------------------\n");
                System.out.println("i= "+ i);
                System.out.println("remainder= " + remainder);
                System.out.println("activeLength= " + activelength);
                System.out.println("stringInserted= " + u);
                System.out.println("count=" + count);
                root.printear(0);

                System.out.println("Se viene el active node");
                System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
                activeNode.printear(0);
                System.out.println("\n-------------------------------------------------------------------------");

                Edge currentEdge = activeNode.searchString(u); //returns the edge that contains the string

                if(i==15 && remainder==3 && activelength==1){
                    System.out.println("aaa");
                }




                if (currentEdge != null) {
                    if (currentEdge.valueLength() <= activelength) {
                        while (currentEdge != null && currentEdge.valueLength() <= activelength) {
                            //Observation2
                            //String already exists but is the exact same value of the edge.
                            //We need to update the activeNode, activeLenght and activeEdge
                            //creo que aqui va un while, al menos segun la pregunta de stack overflow
                            activeNode = currentEdge.getNextNode();
                            activelength -= currentEdge.valueLength();
                            activeEdge = '\0';
                            u = s.substring(count + remainder - activelength, count + remainder);
                            if (u.equals("")) {
                                currentEdge = null;
                            } else {
                                currentEdge = activeNode.searchString(u);
                            }
                        }
                        if (activelength ==0){
                            break;
                        }
                        else if(currentEdge!=null){
                            break;
                        }
                    } else if (activelength == 1) {
                        //Observation 1
                        //String already exists and there is no activeEdge defined.
                        //We need to update the activeLenght and activeEdge
                        activeEdge = c;
                        break;

                    } else if (activelength > 1) {
                        //Observation 1
                        //String already exists and there is activeEdge already defined.
                        //We need to update the activeLenght
                        break;
                    }

                }
                Node newLeaf = new Node(false);
                newLeaf.setNodeValue(count);
                Node currentNode = activeNode.insertLeaf(newLeaf, u);
                count++;
                remainder--;

                //Rule 2
                if (suffixLinkNode != null) {
                    suffixLinkNode.setSuffixLink(currentNode);
                }
                suffixLinkNode = currentNode;
                //End Rule 2


                //Rule 3
                if (activeNode != root) {
                    if (activeNode.getSuffixLink() != null) {
                        activeNode = activeNode.getSuffixLink();
                    } else {
                        activeNode = root;
                    }
                }
                //End Rule 3

                //Rule 1
                else if (activelength > 1) {
                    activelength--;
                    activeEdge = s.charAt(count);
                } else if (activelength == 1) {
                    activelength--;
                    activeEdge = '\0';
                }
                //End Rule 1
            }
        }

        System.out.println("\n-------------------------------------------------------------------------\n");
        System.out.println("remainder= " + remainder);
        System.out.println("activeLength= " + activelength);
        root.printear(0);

        System.out.println("Se viene el active node");
        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        activeNode.printear(0);
        System.out.println("\n-------------------------------------------------------------------------");

    }


    public int count(String s) {
        int i = 0;
        Node currentNode = root;
        while (i < s.length()) {
            Edge e = currentNode.getEdge(s.charAt(i));
            if (e == null) {
                return 0;
            }
            if (i + e.valueLength() > s.length()) {
                return 0;
            }
            String substring = s.substring(i, i + e.valueLength());
            if (e.checkString(substring)) {
                i += e.valueLength();
                currentNode = e.getNextNode();
            } else {
                return 0;
            }
        }
        return currentNode.getAllLeafs().size();
    }


    public Node getRoot(){
        return root;
    }



    public static void main(String[] args) {
        SuffixTree st = new SuffixTree("owo");
        String uwu = "GATCAATGAGGTGGA$";// GATCAATGAGGTGGA // otro error GATCAATGAGGTGG string con problemas
        st.insert(uwu); //aguagualagua$
        int count = st.count("A");
        System.out.println(count);
        //st.getRoot().printear(0);

        /*
        String a = "uwu";
        String b = a.substring(1,1);
        System.out.println(b);
        System.out.println("uwu");
        */
    }


    // Nos falta
    // Hacer los cambios de active point y cosas
    // Regla2 y Regla 3.
    //A priori

}


