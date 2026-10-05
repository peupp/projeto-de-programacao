import javax.print.attribute.standard.Media;
import java.lang.Math;
import java.util.ArrayList;

public class Calculator{

  public static double CalculateAbsorvance(double InicialLux, double FinalLux){

    double num = InicialLux / FinalLux   ;

    double logarithm = Math.log10(num); 

    return logarithm; 
  }

  public static double CalculateConcentration(double AbsortividadeMolar, double CaminhoOptico, double Absorvancia){

    double Concentration = Absorvancia / (AbsortividadeMolar * CaminhoOptico); 

    return Concentration; 
  }

  public static double MediaDeConcentracao(double[] concetracoes){
    double somaConcentracoes = 0;
    for(int i = 0; i < concetracoes.length; i++){
    somaConcentracoes += concetracoes[i];
    }

  return somaConcentracoes / concetracoes.length;
  }

   public static double desvioPadraoConcetracao(double[] concentracoes){ double somaDosQuadrados = 0;
      double media = MediaDeConcentracao(concentracoes);

      for(int i = 0; i < concentracoes.length; i++){
        double diferenca = concentracoes[i] - media;
        double quadrado = diferenca * diferenca;
        somaDosQuadrados += quadrado;

      }
      double divisao = somaDosQuadrados / concentracoes.length;
      double desvioPadrao = Math.sqrt(divisao);
      return desvioPadrao;
    }


  public static double calcularMediaX(ArrayList<DadoQuimico> lista){
    double juncaoX = 0;
    for(int i = 0; i < lista.size(); i++){
      DadoQuimico dadoQuimico = lista.get(i);
      juncaoX += dadoQuimico.getConcentracao();
    }

    return juncaoX / lista.size();
  }

  public static double calcularMediaY(ArrayList<DadoQuimico> lista){
    double juncaoY = 0;
    for(int i = 0; i < lista.size(); i++){
      DadoQuimico dadoQuimico = lista.get(i);
      juncaoY += dadoQuimico.getAbsorbancia();
    }

    return juncaoY / lista.size();
  }

  public static double calcularCurvaM(ArrayList<DadoQuimico> listaDadosQuimico, double mediaX, double mediaY){
    double juncaoY = 0;
    double resultadoFinal = 0.0;

    for(int i = 0; i < listaDadosQuimico.size(); i++){
      DadoQuimico dadoQuimico = listaDadosQuimico.get(i);
      juncaoY += dadoQuimico.getAbsorbancia();
      double soma = (dadoQuimico.getConcentracao() - mediaX) * ( dadoQuimico.getAbsorbancia() - mediaY);
      double divisor = (dadoQuimico.getConcentracao() - mediaX) * (dadoQuimico.getConcentracao() - mediaX);
      resultadoFinal += soma / divisor;

    }

    System.out.print("Resultado: ");
    System.out.println(resultadoFinal);

    return resultadoFinal;
  }

  public static double calcularConcentracaoPelaAbsorbancia(double absorbancia, double inclinacao, double mediaX, double mediaY){
    double interceptoReta = mediaY - mediaX * inclinacao;

    return (absorbancia - interceptoReta) / inclinacao;
  }
}
