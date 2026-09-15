
public class Quimico {

  protected String Nome; 
  protected String Canal; 
  protected double ComprimentoOnda; 
  protected double Absortividade;  
  protected double CaminhoOptico;  
  
  
  public Quimico(String NomeQuimico,String CanalFrequenciaCor, 
     double ComprimentoOndaQuimico, double AbsortividadeQuimico, double CaminhoOpticoQuimico){

    Nome = NomeQuimico;  
    Canal = CanalFrequenciaCor;
    ComprimentoOnda = ComprimentoOndaQuimico; 
    Absortividade = AbsortividadeQuimico; 
    CaminhoOptico = CaminhoOpticoQuimico; 
  }

  public String getCanal(){
    return Canal; 
  }

  public double getComprimentoOnda(){
    return ComprimentoOnda;
  }

  public double getAbsortividade(){
    return Absortividade; 
  }

  public double getCaminhoOptico(){
    return CaminhoOptico; 
  }
  
}
