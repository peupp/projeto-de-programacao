import java.io.BufferedReader;
import java.io.FileReader; 


public class Main{
  int QuantidadeFrequencia = 3; 

  // Pegar Io e I $
  // Calcular absorvancia $ 
  // Pegar o resto dos dados do quimico no dataset $ 
  // Calcular concentracao para cada frequencia  
  // Calcular diferenca das respostas de cada frequencia de luz para analisar consistencia e calcular a media da frequencia de luz 

  public static void Leitor(String NomeArquivo, String NomeQuimico, Quimico[] listaQuimicos) throws Exception {
    int contador = 0; 

    BufferedReader reader = new BufferedReader(
       new FileReader(NomeArquivo)
     );

    String linha;

    // pula cabeçalho
    reader.readLine();

    while ((linha = reader.readLine()) != null) {

     String[] dados = linha.split(",");

          String nomeQuimico = dados[0];
          String canal = dados[1];
          double comprimentoOnda = Double.parseDouble(dados[2]);
          double absortividade = Double.parseDouble(dados[3]);
          double caminhoOptico = Double.parseDouble(dados[4]);

          if(nomeQuimico.equals(NomeQuimico)){
            Quimico Quimico = new Quimico(nomeQuimico,canal, comprimentoOnda,absortividade, caminhoOptico);
            listaQuimicos[contador] = Quimico; 
            contador += 1;  
          }
      }
      reader.close();
  }

  public static void main(String[] args) throws Exception {
    int Io = 1000;
    int I = 500; 
    String ArquivoDataset = "dataset_quimicos_fotometro.csv";
    String NomeQuimico = "Permanganato de Potássio"; 
    Quimico[] Lista = new Quimico[3]; 

    Leitor(ArquivoDataset, NomeQuimico, Lista);
    System.out.println(Lista[0].getCanal()); 
    System.out.println(Lista[1].getCanal()); 
    System.out.println(Lista[2].getCanal()); 

    double Absorvancia= Calculator.CalculateAbsorvance(Io, I);  
    System.out.println(Absorvancia); 
    double Concentracion = Calculator.CalculateConcentration(Lista[0].getAbsortividade(), Lista[0].getCaminhoOptico(), Absorvancia); 
    System.out.println(Concentracion);

  }

}



