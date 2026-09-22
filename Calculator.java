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


}
