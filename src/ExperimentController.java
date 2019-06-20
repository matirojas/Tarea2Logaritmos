import javafx.util.Pair;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Clase con los experimentos realizados para la tarea
 */
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
        double timeI = System.nanoTime();

        st.insert(wholeString.substring(0, n) + "$");
        double timeE = System.nanoTime();
        double time = timeE - timeI;
        return time;

    }

    public Pair<Double,Double> subExperiment(int n, int m, boolean fail) {
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
    public double topkqExperiment(int k,int q){
        double timeI = System.nanoTime();
        st.topKQ(k,q);
        double timeE = System.nanoTime();
        return (timeE-timeI);



    }


    public void Experiment(String pathText, String stringWrite, int r, int w,int[] m,boolean isDNA) throws IOException {

        int[] ks=new int[]{3,5,10};
        int[] qs;
        if(isDNA){
            qs=new int[]{4,8,16,32};
        }
        else{
            qs=new int[]{4,5,6,7};
        }
        ArrayList<Double> setupList=new ArrayList<>();
        ArrayList<Double> locateList=new ArrayList<>();
        ArrayList<Double> CountList=new ArrayList<>();
        ArrayList<Double> missCount=new ArrayList<>();
        ArrayList<Double> missLocate=new ArrayList<>();
        ArrayList<Double> topList=new ArrayList<>();

        FileWriter setup = new FileWriter(stringWrite);
        for (int i = r; i <w; i++) {
            double n = Math.pow(2, i);
            double timeSetup = this.setST(pathText, isDNA, (int) n);
            setupList.add(timeSetup);
            System.out.println("creo el arbol");
            for (int j :
                    m) {
                System.out.println("loop++");
                Pair<Double,Double> time = subExperiment((int) n, j,false);
                Double timeLocate=time.getKey();
                Double timeCount=time.getValue();

                locateList.add(timeLocate);
                CountList.add(timeCount);
                time = subExperiment((int) n, j,true);
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
            for (int k :
                    ks) {
                setup.write("Para k = "+k+":\n");
                for (int q :
                        qs) {
                    topList.add(this.topkqExperiment(k,q));


                }
                setup.write(topList.toString()+"\n");

                topList=new ArrayList<>();
            }

        }
        setup.write("tiempos de construccion: "+setupList.toString());

        setup.flush();

    }


    /**
     * Funcion que realiza los experimentos del informe.
     *
     * En la ultima linea donde dice ec.Experiment, el primer parametro es el path al archivo
     * que se quiere realizar el suffix tree, el segundo el path hacia donde se van a escribir
     * los archivos y el último parametro si es que se trata del archivo de adn (se coloca true)
     * y si es el archivo english (se coloca false).
     * Los valores del for, se coloca desde i=10 hasta 23 para que genere los experimentos para los
     * largos de 2^10 hasta 2^23
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        ExperimentController ec=new ExperimentController();
        int[] m = new int[]{8, 16, 32, 64};
        m=new int[]{10};
        for (int i = 10; i <23 ; i++) {
            ec.Experiment("/home/emilio/2019-1/englishText/english.50MB","/home/emilio/2019-1/results/english"+i+".txt",i,i+1,m,false);

        }

    }
}




