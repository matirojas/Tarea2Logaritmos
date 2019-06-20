import javafx.util.Pair;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class ExperimentController {
    private String wholeString;
    private int seed;
    private SuffixTree st;


    public void setSeed(int seed) {
        this.seed = seed;
    }

    public void englishTextCleaner(String filename) throws IOException {
        StringBuilder file = new StringBuilder();
        BufferedReader bf = new BufferedReader(new FileReader(filename));
        String line = bf.readLine();
        while (line != null) {
            file.append(line);
            file.append("\n");
            line = bf.readLine();
        }
        String result = file.toString();
        result = result.replaceAll("\n", " ");//TODO espacios
        result = result.replaceAll("\\s{2,}", " ");
        result = result.replaceAll("\\[", "");//TODO espacios
        result = result.toLowerCase();
        result = result.replaceAll("[.,:'\"()*?!$#&%\\|\\[\\]-]", "");//TODO signo de puntuacion
        result = result + "$";
        this.wholeString = result;
    }

    public void adnTextCleaner(String filename) throws IOException {
        StringBuilder file = new StringBuilder();
        BufferedReader bf = new BufferedReader(new FileReader(filename));
        String line = bf.readLine();
        while (line != null) {
            file.append(line);
            line = bf.readLine();
        }
        String result = file.toString();
        result = result.toLowerCase();
        result = result.replaceAll("$", "");
        result = result + "$";
        this.wholeString = result;
    }


    /**
     * @param path
     * @param n
     */
    public double setST(String path, boolean adn, int n) throws IOException {

        SuffixTree st = new SuffixTree(path);
        this.st=st;
        if (adn) {
            this.adnTextCleaner(path);

        } else {
            this.englishTextCleaner(path);
        }
        double timeI = System.currentTimeMillis();

        st.insert(wholeString.substring(0, n) + "$");
        double timeE = System.currentTimeMillis();
        double time = timeE - timeI;
        return time;

    }

    public Pair<Double,Double> ADNExperiment(int n, int m,boolean fail) {
        double locate=0.0;
        double count=0.0;
        String[] stringGenerados=new String[n/10];
        for (int i = 0; i < n / 10; i++) {
            int indice = new Random().nextInt((n - m));
            String sub;
            if (!fail){
                sub = wholeString.substring(indice, indice + m);

            }
            else{
                sub = wholeString.substring(indice, indice + m-1);
                sub=sub+"?";

            }
            double timeI = System.nanoTime();
            st.locate(sub);
            double timeE = System.nanoTime();
            locate = locate+(timeE - timeI);
            timeI = System.nanoTime();
            st.count(sub);
            timeE = System.nanoTime();
            count=count+(timeE - timeI);
        }


        return new Pair<>(locate,count);

    }

    public double locateEnglishExperiment(int n) {
        double timeI = System.currentTimeMillis();
        for (int i = 0; i < n / 10; i++) {
            int indice = new Random().nextInt(n - 10);
            String sub = wholeString.substring(indice, indice + 10);
            st.locate(sub);


        }
        double timeE = System.currentTimeMillis();
        double time = timeE - timeI;
        return time;

    }

    public void AdnExperiment(String pathText,String stringWrite) throws IOException {
        int[] m = new int[]{8, 16, 32, 64};
        ArrayList<Double> setupList=new ArrayList<>();
        ArrayList<Double> locateList=new ArrayList<>();
        ArrayList<Double> CountList=new ArrayList<>();
        ArrayList<Double> missCount=new ArrayList<>();
        ArrayList<Double> missLocate=new ArrayList<>();
        FileWriter setup = new FileWriter(stringWrite);
        for (int i = 10; i <12; i++) {
            double n = Math.pow(2, i);
            double timeSetup = this.setST(pathText, true, (int) n);
            setupList.add(timeSetup);

            for (int j :
                    m) {
                Pair<Double,Double> time = ADNExperiment((int) n, j,false);
                Double timeLocate=time.getKey();
                Double timeCount=time.getValue();

                locateList.add(timeLocate);
                CountList.add(timeCount);
                time = ADNExperiment((int) n, j,true);
                missLocate.add(time.getKey());
                missCount.add(time.getValue());
            }
            setup.write("\n");
            setup.write("tiempo de creacion del arbol con string de largo: "+n+ " tiempo: "+timeSetup);

            setup.write("\n");
            setup.write("\n");
            setup.write("tiempo de locate:"+locateList.toString());
            setup.write("\n");
            setup.write("tiempo de count:"+CountList.toString());
            setup.write("\n");
            setup.write("tiempo de misslocate:"+missLocate.toString());

            setup.write("\n");
            setup.write("tiempo de misscount:"+missCount.toString());

            setup.write("\n");
            locateList=new ArrayList<>();
            CountList=new ArrayList<>();
            missCount=new ArrayList<>();
            missLocate=new ArrayList<>();



        }
        setup.write("tiempos de construccion: "+setupList.toString());

        setup.flush();

    }

    public void EnglishExperiment(String path) throws IOException {
        FileWriter setup = new FileWriter("D:\\dcc\\2019-1\\loga\\english.txt");//TODO cambiar el filename

        for (int i = 10; i < 24; i++) {
            double n = Math.pow(2, i);
            double timeSetup = this.setST(path, true, (int) n);
            double timeLocate = locateEnglishExperiment((int) n);
            setup.write("tiempo de setup" + Double.toString(n) + ":" + Double.toString(timeSetup));
            setup.write("tiempo de locate" + Double.toString(n) + ":" + Double.toString(timeLocate));
        }
        setup.flush();

    }


    public static void main(String[] args) throws IOException {
        ExperimentController ec=new ExperimentController();
        ec.AdnExperiment("D:\\dcc\\2019-1\\loga\\datasets\\dna.50MB","D:\\dcc\\2019-1\\loga\\adn.txt");

    }
}




