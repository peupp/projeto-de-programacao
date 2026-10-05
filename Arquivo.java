import java.io.FileWriter;
import java.io.IOException;


public class Arquivo {


    public Arquivo(){}

    public void adicionarLinha(String nomeArquivo, String conteudo) {

        try (FileWriter escritor = new FileWriter(nomeArquivo, true)) {

            escritor.write(conteudo);
            escritor.write(System.lineSeparator());

        } catch (IOException e) {
            System.out.println("Erro ao escrever no arquivo: " + e.getMessage());
        }
    }
}

