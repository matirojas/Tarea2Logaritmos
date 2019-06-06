import java.io.*;

public class SuffixTree {
    private Node root;
    private Node actualNode;
    private char activeEdge;
    private int activelength;
    private int remainder;

    public SuffixTree(String fileName){
        this.root = new Node(true);
        this.actualNode = this.root;
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
            //Primer Paso
            remainder++;
            char c = s.charAt(i);
            int rule1 = actualNode.searchString(Character.toString(c));
            root.insertChar(c);
            //Rule 1
            if (rule1 != -1){
                if(rule1 == activelength){
                    //mover el active point

                }
                else if(activelength == 0){
                    //actualNode se mantiene
                    //modificamos length
                    activelength++;
                    activeEdge = c;
                }
                else if (activelength > 0){
                    activelength++;
                }

            }

            else if (activelength == 0){
                //Inserar la hoja.
                Node newLeaf = new Node(false);
                newLeaf.setNodeValue(count);
                actualNode.insertLeaf(newLeaf, Character.toString(s.charAt(count)));
                count++;
                remainder--;
            }

            else{ //==-1
                //Inserar la hoja.
                while(remainder != 0) {
                    Node newLeaf = new Node(false);
                    newLeaf.setNodeValue(count);
                    actualNode.insertLeaf(newLeaf, s.substring(count, count + activelength + 1));
                    count++;
                    remainder--;
                    if (activelength >0) {
                        activelength--;
                        activeEdge = s.charAt(count);
                    }
                    else if (activelength == 0) {
                        activeEdge = '\0';
                    }
                }
            }

        }
    }

    public static void main(String[] args) {
        SuffixTree st = new SuffixTree("owo");
        st.insert("abcabc$");
        //String a = "uwu";
        //String b = a.substring(0,a.length());
        System.out.println("uwu");

    }
}


