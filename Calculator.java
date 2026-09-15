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



}
