import java.io.*;

public class SuffixTree {
    private Node root;
    public SuffixTree(String fileName){
        this.build(this.cleanText(fileName));
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
    
    public void build(String text){
        for (int i = 0; i < text.length(); i++) {
            this.insert(text.charAt(i),i);
        }
    }

    private void insert(char substring,int i) {
        root.insert(substring,i);
    }
}


