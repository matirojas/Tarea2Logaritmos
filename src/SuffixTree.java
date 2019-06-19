import javafx.util.Pair;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

public class SuffixTree {
    private Node root;
    private Node activeNode;
    private char activeEdge;
    private int activelength;
    private int remainder;
    private Node suffixLinkNode = null;

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

    private void insert(String s) {
        int count = 0;//count for values in the leaf.
        for (int i = 0; i < s.length(); i++) {
            remainder++;
            char c = s.charAt(i);
            root.insertChar(c);
            suffixLinkNode = null;
            while (remainder != 0) {
                String u = s.substring(count + remainder - activelength-1, count + remainder);//The whole string we want to insert
                Edge currentEdge = activeNode.searchString(u); //returns the edge that contains the string
                if (currentEdge != null) {
                    if (currentEdge.valueLength() <= activelength) {
                            //Observation2
                            activeNode = currentEdge.getNextNode();
                            activelength -= currentEdge.valueLength();
                            activeEdge = '\0';
                            continue;
                    }
                    u = s.substring(count + remainder - activelength-1, count + remainder);
                    currentEdge = activeNode.searchString(u);
                    if (currentEdge!=null && currentEdge.getCharValue(activelength) == c) {
                        //Observation 1
                        activeEdge = c;
                        activelength++;
                        rule2(activeNode);
                        break;
                    }
                }
                Node newLeaf = new Node(false);
                newLeaf.setNodeValue(count);
                Node currentNode = activeNode.insertLeaf(newLeaf, u);
                count++;
                remainder--;
                //Rule 2
                rule2(currentNode);
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
    }

    /*private void printSuffixTree(int i, int remainder, int activelength, String u, int count) {
        System.out.println("\n-------------------------------------------------------------------------\n");
        System.out.println("i= "+ i);
        System.out.println("remainder= " + remainder);
        System.out.println("activeLength= " + activelength);
        System.out.println("stringInserted= " + u);
        System.out.println("count=" + count);
        root.printear(0);
    }
    */

    public void rule2(Node n){
        if (suffixLinkNode != null) {
            suffixLinkNode.setSuffixLink(n);
        }
        suffixLinkNode = n;
    }

    public int count(String s) {
        int i = 0;
        Node currentNode = root;
        while (i < s.length()) {
            Edge e = currentNode.getEdge(s.charAt(i));
            if (e == null) {
                return 0;
            } else {
                String substring = e.getValue();
                if (substring.contains(s.substring(i))) {
                    return e.getNextNode().getAllLeafs().size();
                } else {
                    i += substring.length();
                    currentNode = e.getNextNode();
                }

            }
        }
        return 0;
    }
    public ArrayList<Integer> locate(String s){
        int i = 0;
        Node currentNode = root;
        ArrayList<Integer> lista=new ArrayList<>();
        while (i < s.length()) {
            Edge e = currentNode.getEdge(s.charAt(i));
            if (e == null) {
                break;
            } else {
                String substring = e.getValue();
                if (substring.contains(s.substring(i))) {
                    ArrayList<Node> leafs= e.getNextNode().getAllLeafs();
                    for (Node leaf :
                            leafs) {
                        lista.add(leaf.getNodeValue());

                    }
                    break;
                } else {
                    i += substring.length();
                    currentNode = e.getNextNode();
                }

            }
        }
        return lista;
    }

    //Retorna la lista con los k strings de largo q que ocurren más veces en
    //T [1, n].
    // q = largo del string
    public ArrayList<String> topKQ(int k, int q){

        Stack<Pair<Node,String>> stack=new Stack<>();
        stack.push(new Pair(root,""));
        PriorityQueue<Pair<String, Integer>> topK = new PriorityQueue<>(new Comparator<Pair<String, Integer>>() {
            @Override
            public int compare(final Pair<String, Integer> o1, final Pair<String, Integer> o2) {
                return o1.getValue()-o2.getValue();
            }
        });

        while(!stack.empty()){
            Pair<Node,String> currentPair= stack.pop();
            Node node=currentPair.getKey();
            String partialString=currentPair.getValue();
            Collection<Edge> edges = node.getEdges();
            for (Edge edge:edges) {

                String completed=edge.getValue().contains("$")?edge.getValue().substring(0,edge.valueLength()-1):edge.getValue();
                if (partialString.length()+completed.length()>=q){
                    topK.add(new Pair(partialString + completed.substring(0, q - partialString.length()), edge.getNextNode().getAllLeafs().size()));
                }
                else if(!edge.getNextNode().isLeaf()){
                    stack.push(new Pair(edge.getNextNode(),partialString+edge.getValue()));
                }
            }
        }
        while(topK.size() > k){
            topK.remove();
        }
        ArrayList<String> result = new ArrayList<>();
        for (Pair<String, Integer> pairSi :
                topK
             ) {
            result.add(pairSi.getKey());
        }
        return result;
    }


    public Node getRoot(){
        return root;
    }

    public static void main(String[] args) throws IOException {
        SuffixTree st = new SuffixTree("owo");
        /*
        String uwu = "GATCAATGAGGTGGACACCAGAGGCGGGGACTTGTAAATAACACTGGGCTGTAGGAGTGATGGGGTTCACCTCTAATTCT" +
                "AAGATGGCTAGATAATGCATCTTTCAGGGTTGTGCTTCTATCTAGAAGGTAGAGCTGTGGTCGTTCAATAAAAGTCCTCA" +
                "AGAGGTTGGTTAATACGCATGTTTAATAGTACAGTATGGTGACTATAGTCAACAATAATTTATTGTACATTTTTAAATAG" +
                "CTAGAAGAAAAGCATTGGGAAGTTTCCAACATGAAGAAAAGATAAATGGTCAAGGGAATGGATATCCTAATTACCCTGAT" +
                "TTGATCATTATGCATTATATACATGAATCAAAATATCACACATACCTTCAAACTATGTACAAATATTATATACCAATAAA" +
                "AAATCATCATCATCATCTCCATCATCACCACCCTCCTCCTCATCACCACCAGCATCACCACCATCATCACCACCACCATC" +
                "ATCACCACCACCACTGCCATCATCATCACCACCACTGTGCCATCATCATCACCACCACTGTCATTATCACCACCACCATC" +
                "ATCACCAACACCACTGCCATCGTCATCACCACCACTGTCATTATCACCACCACCATCACCAACATCACCACCACCATTAT" +
                "CACCACCATCAACACCACCACCCCCATCATCATCATCACTACTACCATCATTACCAGCACCACCACCACTATCACCACCA" +
                "CCACCACAATCACCATCACCACTATCATCAACATCATCACTACCACCATCACCAACACCACCATCATTATCACCACCACC" +
                "ACCATCACCAACATCACCACCATCATCATCACCACCATCACCAAGACCATCATCATCACCATCACCACCAACATCACCAC" +
                "CATCACCAACACCACCATCACCACCACCACCACCATCATCACCACCACCACCATCATCATCACCACCACCGCCATCATCA" +
                "TCGCCACCACCATGACCACCACCATCACAACCATCACCACCATCACAACCACCATCATCACTATCGCTATCACCACCATC" +
                "ACCATTACCACCACCATTACTACAACCATGACCATCACCACCATCACCACCACCATCACAACGATCACCATCACAGCCAC" +
                "CATCATCACCACCACCACCACCACCATCACCATCAAACCATCGGCATTATTATTTTTTTAGAATTTTGTTGGGATTCAGT" +
                "ATCTGCCAAGATACCCATTCTTAAAACATGAAAAAGCAGCTGACCCTCCTGTGGCCCCCTTTTTGGGCAGTCATTGCAGG" +
                "ACCTCATCCCCAAGCAGCAGCTCTGGTGGCATACAGGCAACCCACCACCAAGGTAGAGGGTAATTGAGCAGAAAAGCCAC" +
                "TTCCTCCAGCAGTTCCCTGTCTGAGCTGCTGTCCTTGGACTTGAAGAAGCTTCTGGAACATGCTGGGGAGGAAGGAAGAC" +
                "ATTTCACTTATTGAGTGGCCTGATGCAGAACAGAGACCCAGCTGGTTCACTCTAGTTCGGACTAAAACTCACCCCTGTCT" +
                "ATAAGCATCAGCCTCGGCAGGATGCATTTCACATTTGTGATCTCATTTAACCTCCACAAAGACCCAGAAGGGTTGGTAAC" +
                "ATTATCATACCTAGGCCTACTATTTTAAAAATCTAACACCCATGCAGCCCGGGCACTGAAGTGGAGGCTGGCCACGGAGA$";// GATCAATGAGGTGGA // otro error GATCAATGAGGTGG string con problemas
        st.insert(uwu);
        ArrayList<Integer> count = st.locate("AC");
        //st.root.printear(0);
        count.sort(Integer::compareTo);
        for (Integer i :
                count) {
            System.out.println(i);
        }
        System.out.println(count.size());
        ArrayList<String> owaso = st.topKQ(4,4);
        for (String s :
                owaso) {
            System.out.println(s);
        }
        */


        StringBuilder archivo = new StringBuilder();
        BufferedReader bf=new BufferedReader(new FileReader("/home/emilio/2019-1/dna.50MB"));
        String line=bf.readLine();
        //Textito
        while(line!=null){
            archivo.append(line);

            line=bf.readLine();
        }
        archivo.append("$");
        String nosdfn = archivo.toString();
        line.toLowerCase();
        line.replaceAll(".", "");//TODO signo de puntuacion
        line.replaceAll(",", "");//TODO espacios
        line.replaceAll("\n", " ");//TODO espacios
        line.replaceAll("\\s{2,}", " ");


        st.insert(nosdfn);





    }


}


