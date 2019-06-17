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

    /*
    private void insertChar(char substring,int i) {

        root.insertChar(substring,i);

    }
    */
    private void insert(String s) {
        int count = 0;//count for values in the leaf.
        for (int i = 0; i < s.length(); i++) {
            remainder++;
            char c = s.charAt(i);
            root.insertChar(c);
            suffixLinkNode = null;
            if(i==6){
                System.out.println("a");
            }
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

    private void printuwu(int i, int remainder, int activelength, String u, int count) {
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

    }

    public void rule2(Node n){
        if (suffixLinkNode != null) {
            suffixLinkNode.setSuffixLink(n);
        }
        suffixLinkNode = n;
    }

    /*

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


        System.out.println("\n-------------------------------------------------------------------------\n");
        System.out.println("remainder= " + remainder);
        System.out.println("activeLength= " + activelength);
        root.printear(0);

        System.out.println("Se viene el active node");
        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        activeNode.printear(0);
        System.out.println("\n-------------------------------------------------------------------------");

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
        int count = 0;
        ArrayList<Pair<String, Node>> founded = new ArrayList<>();
        String actualString = "";

        Stack<Pair<Node,String>> stack=new Stack<>();
        stack.push(new Pair(root,""));
        while(!stack.empty()){
            Pair<Node,String> currentPair= stack.pop();
            Node node=currentPair.getKey();
            String partialString=currentPair.getValue();
            Collection<Edge> edges = node.getEdges();
            /*
            if(partialString.length()>=q){
                founded.add(new Pair(partialString , node));
                continue;
            }
            */
            for (Edge edge:edges) {

                String completed=edge.getValue().contains("$")?edge.getValue().substring(0,edge.valueLength()-1):edge.getValue();
                if (partialString.length()+completed.length()>=q){
                    founded.add(new Pair(partialString + completed.substring(0, q - partialString.length()), edge.getNextNode()));
                }
                else if(!edge.getNextNode().isLeaf()){
                    stack.push(new Pair(edge.getNextNode(),partialString+edge.getValue()));
                }

            }
        }

        PriorityQueue<Pair<String, Integer>> topK = new PriorityQueue<>(new Comparator<Pair<String, Integer>>() {
            @Override
            public int compare(final Pair<String, Integer> o1, final Pair<String, Integer> o2) {
                // TODO: implement your logic here


                return o1.getValue()-o2.getValue();
            }
        });
        int min= Integer.MAX_VALUE;
        Pair minPair = null;
        for (Pair<String, Node> pair: founded) {
            int o = pair.getValue().getAllLeafs().size();
            if (topK.size() >= k){
                if (o > min){
                    topK.remove(minPair);
                    Pair newPair = new Pair(pair.getKey(),o);
                    topK.add(newPair);
                    min = topK.peek().getValue();
                    minPair =topK.peek();
                }
            }
            else{
                Pair newPair = new Pair(pair.getKey(),o);
                topK.add(newPair);
                if (o< min){
                    min = o;
                    minPair = newPair;
                }
            }
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



    public static void main(String[] args) {
        SuffixTree st = new SuffixTree("owo");
        String uwu = "GATCAATGAGGTGGA$";// GATCAATGAGGTGGA // otro error GATCAATGAGGTGG string con problemas
        st.insert(uwu); //aguagualagua$
        ArrayList<Integer> count = st.locate("c");
        st.root.printear(0);
        count.sort(Integer::compareTo);
        for (Integer i :
                count) {
            System.out.println(i);
        }
        System.out.println(count.size());
        ArrayList<String> owaso = st.topKQ(1,2);
        for (String s :
                owaso) {
            System.out.println(s);
        }
        //st.getRoot().printear(0);

        /*
        String a = "uwu";
        String b = a.substring(1,1);
        System.out.println(b);
        System.out.println("uwu");
        */
    }



}


