import java.lang.Math;

public class Calculator{

  public static double CalculateAbsorvance(int InicialLux, int FinalLux){

    double num = FinalLux / InicialLux;

    double logarithm = Math.log10(num); 

    return -1*logarithm; 
  }



}
