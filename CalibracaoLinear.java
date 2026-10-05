import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class CalibracaoLinear {
    private double mediaX;
    private double mediaY;
    private ArrayList<DadoQuimico> listaDados = new ArrayList<DadoQuimico>();
    private double inclinacao;

    public CalibracaoLinear(){}

    public void inserirCalibracoesIniciaisNoDataset(String nomeQuimico, double absorbanciaQuimico, double concentracaoQuimico){
        Arquivo arquivo = new Arquivo();
        String linha = nomeQuimico + "," + absorbanciaQuimico + "," + concentracaoQuimico;
        arquivo.adicionarLinha("dados_calibracao_linear.csv",linha);
    }

    public void pegarCalibracoesDoArquivo(String nomeArquivo, String nomeQuimico) throws Exception {

        BufferedReader reader = new BufferedReader(
                new FileReader(nomeArquivo)
        );

        String linha;

        // pula cabeçalho
        reader.readLine();

        while ((linha = reader.readLine()) != null) {

            String[] dados = linha.split(",");

            String nome = dados[0];
            double absortividade = Double.parseDouble(dados[1]);
            double concentracao = Double.parseDouble(dados[2]);

            if(nome.equals(nomeQuimico)){
                DadoQuimico quimico = new DadoQuimico(concentracao, absortividade, nome);
                this.listaDados.add(quimico);
            }
        }
        reader.close();
    }

    public void calcularMedias(){
        this.mediaX = Calculator.calcularMediaX(this.listaDados);
        this.mediaY = Calculator.calcularMediaY(this.listaDados);
        System.out.println(this.mediaX);
        System.out.println(this.mediaY);
    }

    public void calcularCurvaM(){
        double resultado = Calculator.calcularCurvaM(this.listaDados, this.mediaX , this.mediaY );
        this.inclinacao = resultado;
    }

    public double calcularConcentracaoPelaAbsorbancia(double absorbancia){
       return Calculator.calcularConcentracaoPelaAbsorbancia(absorbancia,this.inclinacao, this.mediaX,this.mediaY);
    }
    public void adicionarDado(double concentracao, double absorbancia, String nome) {
        this.listaDados.add(new DadoQuimico(concentracao, absorbancia, nome));
    }

    public double getMediaX() {
        return mediaX;
    }

    public double getMediaY() {
        return mediaY;
    }

    public ArrayList<DadoQuimico> getListaDados() {
        return listaDados;
    }

    public double getInclinacao() {
        return inclinacao;
    }
}
