import javafx.util.Pair;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;


/**
 * Clase que representa un suffix tree.
 */
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

    /**
     * Crea el suffix tree a partir de un string, utilizando el algoritmo de ukkonen.
     * @param s el string que se quiere convertir.
     */
    public void insert(String s) {
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

    /**
     * Regla 2 del algoritmo de creación de suffix tree. Crea un suffix link entre el nodo suffixLinkNode y
     * un nodo n, en el caso que suffixLink node sea distinto de nulo. Al final, asigna al nodo n como el nuevo
     * suffixLinkNode.
     * @param n el nodo
     */
    public void rule2(Node n){
        if (suffixLinkNode != null) {
            suffixLinkNode.setSuffixLink(n);
        }
        suffixLinkNode = n;
    }

    /**
     * Implementación de la función count. Retorna la cantidad de ocurrencias del string s en el suffix tree
     * @param s el string buscado
     * @return la cantidad de ocurrencias
     */
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

    /**
     * Implementación de la función locate. Retorna la lista con las posiciones donde ocurre el string s
     * en el suffix tree.
     * @param s el string buscado
     * @return la lista de posiciones donde ocurren el string s.
     */
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

    /**
     * Implementación de top-k-q. Retorna la lista de los k strings de largo q que ocurren más veces
     * en el suffix tree.
     * @param k cantidad top de resultados
     * @param q largo del string
     * @return el arreglo con los k strings más largos.
     */
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


    /**
     * Devuelve el nodo root del suffix tree
     * @return el nodo root
     */
    public Node getRoot(){
        return root;
    }

    public static void main(String[] args) throws IOException {
        SuffixTree st = new SuffixTree("owo");

        String uwu = "GATCAATGAGGTGGACACCAGAGGCGGGGACTTGTAAATAACACTGGGCTGTAGGAGTGATGGGGTTCACCTCTAATTCTAAGATGGCTAGATAATGCATCTTTCAGGGTTGTGCTTCTATCTAGAAGGTAGAGCTGTGGTCGTTCAATAAAAGTCCTCAAGAGGTTGGTTAATACGCATGTTTAATAGTACAGTATGGTGACTATAGTCAACAATAATTTATTGTACATTTTTAAATAGCTAGAAGAAAAGCATTGGGAAGTTTCCAACATGAAGAAAAGATAAATGGTCAAGGGAATGGATATCCTAATTACCCTGATTTGATCATTATGCATTATATACATGAATCAAAATATCACACATACCTTCAAACTATGTACAAATATTATATACCAATAAAAAATCATCATCATCATCTCCATCATCACCACCCTCCTCCTCATCACCACCAGCATCACCACCATCATCACCACCACCATCATCACCACCACCACTGCCATCATCATCACCACCACTGTGCCATCATCATCACCACCACTGTCATTATCACCACCACCATCATCACCAACACCACTGCCATCGTCATCACCACCACTGTCATTATCACCACCACCATCACCAACATCACCACCACCATTATCACCACCATCAACACCACCACCCCCATCATCATCATCACTACTACCATCATTACCAGCACCACCACCACTATCACCACCACCACCACAATCACCATCACCACTATCATCAACATCATCACTACCACCATCACCAACACCACCATCATTATCACCACCACCACCATCACCAACATCACCACCATCATCATCACCACCATCACCAAGACCATCATCATCACCATCACCACCAACATCACCACCATCACCAACACCACCATCACCACCACCACCACCATCATCACCACCACCACCATCATCATCACCACCACCGCCATCATCATCGCCACCACCATGACCACCACCATCACAACCATCACCACCATCACAACCACCATCATCACTATCGCTATCACCACCATCACCATTACCACCACCATTACTACAACCATGACCATCACCACCATCACCACCACCATCACAACGATCACCATCACAGCCACCATCATCACCACCACCACCACCACCATCACCATCAAACCATCGGCATTATTATTTTTTTAGAATTTTGTTGGGATTCAGTATCTGCCAAGATACCCATTCTTAAAACATGAAAAAGCAGCTGACCCTCCTGTGGCCCCCTTTTTGGGCAGTCATTGCAGGACCTCATCCCCAAGCAGCAGCTCTGGTGGCATACAGGCAACCCACCACCAAGGTAGAGGGTAATTGAGCAGAAAAGCCACTTCCTCCAGCAGTTCCCTGTCTGAGCTGCTGTCCTTGGACTTGAAGAAGCTTCTGGAACATGCTGGGGAGGAAGGAAGACATTTCACTTATTGAGTGGCCTGATGCAGAACAGAGACCCAGCTGGTTCACTCTAGTTCGGACTAAAACTCACCCCTGTCTATAAGCATCAGCCTCGGCAGGATGCATTTCACATTTGTGATCTCATTTAACCTCCACAAAGACCCAGAAGGGTTGGTAACATTATCATACCTAGGCCTACTATTTTAAAAATCTAACACCCATGCAGCCCGGGCACTGAAGTGGAGGCTGGCCACGGAGA$";// GATCAATGAGGTGGA // otro error GATCAATGAGGTGG string con problemas

        st.insert(uwu);
        System.out.println(st.remainder);
        System.out.println(st.count("A"));
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
        System.out.println(st.remainder);
        System.out.println(st.activelength);
    }


}


