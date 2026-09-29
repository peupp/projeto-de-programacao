import javax.print.attribute.standard.Media;
import java.lang.Math;

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

public static double desvioPadraoConcetracao(double[] concentracoes){
  double somaDosQuadrados = 0;
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

}
