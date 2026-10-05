public class DadoQuimico{
    private double concentracao;
    private double absorbancia;
    private String nome;

    public DadoQuimico(double concentracao, double absorbancia, String nome){
        this.concentracao = concentracao;
        this.absorbancia = absorbancia;
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public double getAbsorbancia() {
        return absorbancia;
    }

    public double getConcentracao() {
        return concentracao;
    }

    public void setConcentracao(double concentracao) {
        this.concentracao = concentracao;
    }

    public void setAbsorbancia(double absorbancia) {
        this.absorbancia = absorbancia;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
