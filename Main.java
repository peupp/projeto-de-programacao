import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.awt.Color;
import javax.swing.*;
import  java.awt.event.ActionEvent;
import  java.awt.event.ActionListener;


public class Main{
  int QuantidadeFrequencia = 3; 

  private static String valorCSV(String valor) {
    if (valor == null) {
      return "";
    }
    return "\"" + valor.replace("\"", "\"\"") + "\"";
  }

  private static String valorHTML(String valor) {
    if (valor == null) {
      return "";
    }
    return valor
        .replace("&", "&amp;")
        .replace("<", "&lt;")
        .replace(">", "&gt;")
        .replace("\"", "&quot;")
        .replace("'", "&#39;");
  }

  private static void exportarCalculosCSV(
      File arquivo,
      String nomeQuimico,
      double[] luxIniciais,
      double[] luxFinais,
      Quimico[] dadosQuimico,
      double[] absorbancias,
      double[] concentracoes
  ) throws IOException {
    double mediaAbsorbancia = 0;
    for (double absorbancia : absorbancias) {
      mediaAbsorbancia += absorbancia;
    }
    mediaAbsorbancia /= absorbancias.length;

    double mediaConcentracoes = Calculator.MediaDeConcentracao(concentracoes);
    double desvioPadrao = Calculator.desvioPadraoConcetracao(concentracoes);

    try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivo))) {
      writer.write("tipo,nome_quimico,canal,lux_inicial,lux_final,absorbancia,absortividade_molar,caminho_optico,concentracao");
      writer.newLine();

      for (int i = 0; i < dadosQuimico.length; i++) {
        writer.write(
            "canal," + valorCSV(nomeQuimico) + "," + valorCSV(dadosQuimico[i].getCanal()) + ","
                + luxIniciais[i] + "," + luxFinais[i] + "," + absorbancias[i] + ","
                + dadosQuimico[i].getAbsortividade() + "," + dadosQuimico[i].getCaminhoOptico()
                + "," + concentracoes[i]
        );
        writer.newLine();
      }

      writer.write("media_absorbancia," + valorCSV(nomeQuimico) + ",,,,"
          + mediaAbsorbancia + ",,,");
      writer.newLine();
      writer.write("media_concentracao," + valorCSV(nomeQuimico) + ",,,,,,,"
          + mediaConcentracoes);
      writer.newLine();
      writer.write("desvio_padrao_concentracao," + valorCSV(nomeQuimico) + ",,,,,,,"
          + desvioPadrao);
      writer.newLine();
    }
  }

  private static void exportarCalculosHTML(
      File arquivo,
      String nomeQuimico,
      double[] luxIniciais,
      double[] luxFinais,
      Quimico[] dadosQuimico,
      double[] absorbancias,
      double[] concentracoes
  ) throws IOException {
    double mediaAbsorbancia = 0;
    for (double absorbancia : absorbancias) {
      mediaAbsorbancia += absorbancia;
    }
    mediaAbsorbancia /= absorbancias.length;

    double mediaConcentracoes = Calculator.MediaDeConcentracao(concentracoes);
    double desvioPadrao = Calculator.desvioPadraoConcetracao(concentracoes);

    try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivo))) {
      writer.write("<!DOCTYPE html>");
      writer.newLine();
      writer.write("<html lang=\"pt-BR\"><head><meta charset=\"UTF-8\">");
      writer.write("<title>Cálculos - " + valorHTML(nomeQuimico) + "</title>");
      writer.write("<style>");
      writer.write("body{font-family:Arial,sans-serif;margin:32px;color:#222;}");
      writer.write("table{border-collapse:collapse;width:100%;margin-top:16px;}");
      writer.write("th,td{border:1px solid #ccc;padding:8px;text-align:right;}");
      writer.write("th{background:#f2f2f2;} th:first-child,td:first-child{text-align:left;}");
      writer.write(".resumo{margin-top:24px;}");
      writer.write("</style></head><body>");
      writer.write("<h1>Resultados dos cálculos</h1>");
      writer.write("<p><strong>Químico:</strong> " + valorHTML(nomeQuimico) + "</p>");
      writer.write("<table><thead><tr>");
      writer.write("<th>Canal</th><th>Lux inicial</th><th>Lux final</th>");
      writer.write("<th>Absorbância</th><th>Absortividade molar</th>");
      writer.write("<th>Caminho óptico</th><th>Concentração</th>");
      writer.write("</tr></thead><tbody>");

      for (int i = 0; i < dadosQuimico.length; i++) {
        writer.write("<tr><td>" + valorHTML(dadosQuimico[i].getCanal()) + "</td>");
        writer.write("<td>" + luxIniciais[i] + "</td><td>" + luxFinais[i] + "</td>");
        writer.write("<td>" + absorbancias[i] + "</td>");
        writer.write("<td>" + dadosQuimico[i].getAbsortividade() + "</td>");
        writer.write("<td>" + dadosQuimico[i].getCaminhoOptico() + "</td>");
        writer.write("<td>" + concentracoes[i] + "</td></tr>");
      }

      writer.write("</tbody></table><div class=\"resumo\"><h2>Resumo</h2><ul>");
      writer.write("<li>Média da absorbância: " + mediaAbsorbancia + "</li>");
      writer.write("<li>Média das concentrações: " + mediaConcentracoes + "</li>");
      writer.write("<li>Desvio padrão das concentrações: " + desvioPadrao + "</li>");
      writer.write("</ul></div></body></html>");
      writer.newLine();
    }
  }

  // Pegar Io e I $
  // Calcular absorvancia $ 
  // Pegar o resto dos dados do quimico no dataset $ 
  // Calcular concentracao para cada frequencia  
  // Calcular diferenca das respostas de cada frequencia de luz para analisar consistencia e calcular a media da frequencia de luz
  // Calcular a média das concentracoes

  public static List<String> pegarListaTodosQuimicos(String NomeArquivo) throws Exception{
      int contador = 0;
      List<String> nomesQuimicos = new ArrayList<>();
      String ultimoQuimico = "";

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

          if(!nomeQuimico.equals(ultimoQuimico)){
              nomesQuimicos.add(nomeQuimico);
              contador += 1;
          }
          ultimoQuimico = nomeQuimico;
      }
      reader.close();
      String[] lista = {"o"};
      return nomesQuimicos;

  }

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

 public static void min(String[] args) throws Exception {

    String ArquivoDataset = "dataset_quimicos_fotometro.csv";

    List<String> lista = pegarListaTodosQuimicos(ArquivoDataset);
    System.out.println(lista.get(0));
    System.out.println(lista.get(1));
    System.out.println(lista.get(2));

    int[] Io = {1000, 1000, 1000 };
    int[] I = {500, 549, 510}; 
    String NomeQuimico = "Permanganato de Potássio";
    Quimico[] Lista = new Quimico[3];
    double[] concentracoes = new double[3];

    Leitor(ArquivoDataset, NomeQuimico, Lista);

    for(int i = 0; i < 3; i++){
      System.out.println(Lista[i].getNome()); 
      System.out.println(Lista[i].getCanal()); 
      double Absorvancia = Calculator.CalculateAbsorvance(Io[i], I[i]);  
      System.out.print("Absorvancia: ");
      System.out.println(Absorvancia); 
      double Concentracion = Calculator.CalculateConcentration(Lista[i].getAbsortividade(), Lista[i].getCaminhoOptico(), Absorvancia);
      concentracoes[i] = Concentracion;
      System.out.print("Concentracao: ");
      System.out.println(Concentracion);
      System.out.println("==========");
    }
      double media = Calculator.MediaDeConcentracao(concentracoes);
      System.out.println("Média das concetrações: " + media);
      System.out.println("==========");
      double desvioPadrao = Calculator.desvioPadraoConcetracao(concentracoes);
      System.out.println("desvio padrão das concentrações: " + desvioPadrao);

  }

    public static void main() throws Exception {
        String ArquivoDataset = "dataset_quimicos_fotometro.csv";
        Calculator calculadora = new Calculator();

        List<String> nomeQuimicos = pegarListaTodosQuimicos(ArquivoDataset);

        JFrame janela = new JFrame();
        JTextField campoLuxInicialVermelho = new JTextField();
        campoLuxInicialVermelho.setBounds(300, 30, 150, 30);
        JTextField campoLuxFinalVermelho = new JTextField();
        campoLuxFinalVermelho.setBounds(460, 30, 150, 30 );

        JTextField campoLuxInicialAzul = new JTextField();
        campoLuxInicialAzul.setBounds(300, 120, 150, 30);

        JTextField campoLuxFinalAzul = new JTextField();
        campoLuxFinalAzul.setBounds(460, 120, 150, 30);

        JTextField campoLuxInicialVerde = new JTextField();
        campoLuxInicialVerde.setBounds(300, 75, 150, 30 );

        JTextField campoLuxFinalVerde = new JTextField();
        campoLuxFinalVerde.setBounds(460, 75, 150, 30);

        JLabel label = new JLabel("Lux inicial");
        label.setBounds(300, 0, 150, 30);

        JLabel labelDois = new JLabel("Lux final");
        labelDois.setBounds(460, 0, 150, 30);

        JLabel labelCalculos = new JLabel("Calculos");
        labelCalculos.setBounds(650,10 , 150,10);

        JLabel labelSelecionarQuimico = new JLabel("Escolha o quimico que \n quer calcular");
        labelSelecionarQuimico.setBounds(5, 0, 230, 30);

        JPanel quadradoVermelho = new JPanel();
        quadradoVermelho.setBackground(Color.RED);

        JPanel quadradoVerde= new JPanel();
        quadradoVerde.setBackground(Color.GREEN);

        JPanel quadradoAzul = new JPanel();
        quadradoAzul.setBackground(Color.BLUE);

        JButton calcularAbsorbancia = new JButton("Calcular absorbancia");
        JButton calcularConcentracao = new JButton("Calcular concentracao");
        JButton exportarCSV = new JButton("Exportar CSV");
        JButton exportarHTML = new JButton("Exportar HTML");

        JLabel respostaAbsorbancia = new JLabel("Resposta: ");
        JLabel respostaConcentracao = new JLabel("Resposta: ");


        JComboBox<String> dropdown = new JComboBox<>();
        for(String item : nomeQuimicos){
            dropdown.addItem(item);
        }

        dropdown.setBounds(5, 30, 150, 30);

        janela.add(dropdown);
        janela.add(labelSelecionarQuimico);
        janela.add(exportarCSV);
        janela.add(exportarHTML);
        janela.add(respostaAbsorbancia);
        janela.add(respostaConcentracao);
        janela.add(calcularAbsorbancia);
        janela.add(calcularConcentracao);
        janela.add(labelCalculos);
        janela.add(quadradoVermelho);
        janela.add(quadradoVerde);
        janela.add(quadradoAzul);
        janela.add(label);
        janela.add(labelDois);
        janela.add(campoLuxInicialVermelho);
        janela.add(campoLuxFinalVermelho);
        janela.add(campoLuxInicialAzul);
        janela.add(campoLuxFinalAzul);
        janela.add(campoLuxInicialVerde);
        janela.add(campoLuxFinalVerde);

        quadradoVermelho.setBounds(270,30,25,25);
        quadradoVerde.setBounds(270,75,25,25);
        quadradoAzul.setBounds(270,120,25,25);

        JButton button = new JButton("Salve");
        button.setBounds(200,0,40,50);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("TESTeeE");
            }
        });

        calcularAbsorbancia.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                double luxInicialAzul = Double.parseDouble(campoLuxInicialAzul.getText());
                double luxFinalAzul = Double.parseDouble(campoLuxFinalAzul.getText());

                double luxInicialVerde = Double.parseDouble(campoLuxInicialVerde.getText());
                double luxFinalVerde = Double.parseDouble(campoLuxFinalVerde.getText());

                double luxInicialVermelho  = Double.parseDouble(campoLuxInicialVermelho.getText());
                double luxFinalVermelho = Double.parseDouble(campoLuxFinalVermelho.getText());

                double absorbanciaAzul = calculadora.CalculateAbsorvance(luxInicialAzul, luxFinalAzul);
                double absorbanciaVerde = calculadora.CalculateAbsorvance(luxInicialVerde, luxFinalVerde);
                double absorbanciaVermelho = calculadora.CalculateAbsorvance(luxInicialVermelho, luxFinalVermelho);

                double mediaAbsorbancia = (absorbanciaAzul + absorbanciaVerde + absorbanciaVermelho) / 3;

                respostaAbsorbancia.setText("Resposta: " + (mediaAbsorbancia));
            }
        });

        calcularConcentracao.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    double luxInicialAzul = Double.parseDouble(campoLuxInicialAzul.getText());
                    double luxFinalAzul = Double.parseDouble(campoLuxFinalAzul.getText());

                    double luxInicialVerde = Double.parseDouble(campoLuxInicialVerde.getText());
                    double luxFinalVerde = Double.parseDouble(campoLuxFinalVerde.getText());

                    double luxInicialVermelho = Double.parseDouble(campoLuxInicialVermelho.getText());
                    double luxFinalVermelho = Double.parseDouble(campoLuxFinalVermelho.getText());

                    double absorbanciaAzul = calculadora.CalculateAbsorvance(luxInicialAzul, luxFinalAzul);
                    double absorbanciaVerde = calculadora.CalculateAbsorvance(luxInicialVerde, luxFinalVerde);
                    double absorbanciaVermelho = calculadora.CalculateAbsorvance(luxInicialVermelho, luxFinalVermelho);

                    double mediaAbsorbancia = (absorbanciaAzul + absorbanciaVerde + absorbanciaVermelho) / 3;

                    double[] concentracoes = new double[3];

                    String nomeQuimicoSelecionado = (String) dropdown.getSelectedItem();
                    System.out.println(nomeQuimicoSelecionado);

                    Quimico[] quimicoDados = new Quimico[3];
                    Leitor(ArquivoDataset, nomeQuimicoSelecionado, quimicoDados);

                    concentracoes[0] = calculadora.CalculateConcentration(quimicoDados[0].getAbsortividade(), quimicoDados[0].getCaminhoOptico(), absorbanciaAzul );
                    concentracoes[1] = calculadora.CalculateConcentration(quimicoDados[1].getAbsortividade(), quimicoDados[1].getCaminhoOptico(), absorbanciaVerde);
                    concentracoes[2] = calculadora.CalculateConcentration(quimicoDados[2].getAbsortividade(), quimicoDados[2].getCaminhoOptico(), absorbanciaVermelho);

                    double mediaConcentracoes = calculadora.MediaDeConcentracao(concentracoes);
                    respostaConcentracao.setText("Resposta: " + mediaConcentracoes);

                } catch (Exception e) {
                   e.printStackTrace();
                }

            }
        });

        // Area de calculos
        calcularAbsorbancia.setBounds(650, 30, 180, 25);
        respostaAbsorbancia.setBounds(850, 30, 180, 25);

        calcularConcentracao.setBounds(650, 90, 180, 25);
        respostaConcentracao.setBounds(850, 90, 180, 25);

        exportarCSV.setBounds(550, 900, 180, 25);
        exportarHTML.setBounds(750, 900, 180, 25);

        exportarCSV.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    double[] luxIniciais = {
                        Double.parseDouble(campoLuxInicialAzul.getText()),
                        Double.parseDouble(campoLuxInicialVerde.getText()),
                        Double.parseDouble(campoLuxInicialVermelho.getText())
                    };
                    double[] luxFinais = {
                        Double.parseDouble(campoLuxFinalAzul.getText()),
                        Double.parseDouble(campoLuxFinalVerde.getText()),
                        Double.parseDouble(campoLuxFinalVermelho.getText())
                    };
                    double[] absorbancias = new double[3];

                    for (int i = 0; i < absorbancias.length; i++) {
                        absorbancias[i] = calculadora.CalculateAbsorvance(luxIniciais[i], luxFinais[i]);
                    }

                    String nomeQuimicoSelecionado = (String) dropdown.getSelectedItem();
                    Quimico[] quimicoDados = new Quimico[3];
                    Leitor(ArquivoDataset, nomeQuimicoSelecionado, quimicoDados);

                    double[] concentracoes = new double[3];
                    for (int i = 0; i < concentracoes.length; i++) {
                        concentracoes[i] = calculadora.CalculateConcentration(
                            quimicoDados[i].getAbsortividade(),
                            quimicoDados[i].getCaminhoOptico(),
                            absorbancias[i]
                        );
                    }

                    JFileChooser seletorArquivo = new JFileChooser();
                    seletorArquivo.setDialogTitle("Salvar cálculos como CSV");
                    seletorArquivo.setSelectedFile(new File("calculos.csv"));

                    if (seletorArquivo.showSaveDialog(janela) != JFileChooser.APPROVE_OPTION) {
                        return;
                    }

                    File arquivo = seletorArquivo.getSelectedFile();
                    if (!arquivo.getName().toLowerCase().endsWith(".csv")) {
                        arquivo = new File(arquivo.getAbsolutePath() + ".csv");
                    }

                    exportarCalculosCSV(
                        arquivo,
                        nomeQuimicoSelecionado,
                        luxIniciais,
                        luxFinais,
                        quimicoDados,
                        absorbancias,
                        concentracoes
                    );
                    JOptionPane.showMessageDialog(janela, "Cálculos exportados com sucesso.");
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(
                        janela,
                        "Informe valores numéricos válidos para os lux.",
                        "Dados inválidos",
                        JOptionPane.ERROR_MESSAGE
                    );
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(
                        janela,
                        "Não foi possível exportar os cálculos: " + e.getMessage(),
                        "Erro na exportação",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });

        exportarHTML.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    double[] luxIniciais = {
                        Double.parseDouble(campoLuxInicialAzul.getText()),
                        Double.parseDouble(campoLuxInicialVerde.getText()),
                        Double.parseDouble(campoLuxInicialVermelho.getText())
                    };
                    double[] luxFinais = {
                        Double.parseDouble(campoLuxFinalAzul.getText()),
                        Double.parseDouble(campoLuxFinalVerde.getText()),
                        Double.parseDouble(campoLuxFinalVermelho.getText())
                    };
                    double[] absorbancias = new double[3];

                    for (int i = 0; i < absorbancias.length; i++) {
                        absorbancias[i] = calculadora.CalculateAbsorvance(luxIniciais[i], luxFinais[i]);
                    }

                    String nomeQuimicoSelecionado = (String) dropdown.getSelectedItem();
                    Quimico[] quimicoDados = new Quimico[3];
                    Leitor(ArquivoDataset, nomeQuimicoSelecionado, quimicoDados);

                    double[] concentracoes = new double[3];
                    for (int i = 0; i < concentracoes.length; i++) {
                        concentracoes[i] = calculadora.CalculateConcentration(
                            quimicoDados[i].getAbsortividade(),
                            quimicoDados[i].getCaminhoOptico(),
                            absorbancias[i]
                        );
                    }

                    JFileChooser seletorArquivo = new JFileChooser();
                    seletorArquivo.setDialogTitle("Salvar cálculos como HTML");
                    seletorArquivo.setSelectedFile(new File("calculos.html"));

                    if (seletorArquivo.showSaveDialog(janela) != JFileChooser.APPROVE_OPTION) {
                        return;
                    }

                    File arquivo = seletorArquivo.getSelectedFile();
                    if (!arquivo.getName().toLowerCase().endsWith(".html")) {
                        arquivo = new File(arquivo.getAbsolutePath() + ".html");
                    }

                    exportarCalculosHTML(
                        arquivo,
                        nomeQuimicoSelecionado,
                        luxIniciais,
                        luxFinais,
                        quimicoDados,
                        absorbancias,
                        concentracoes
                    );
                    JOptionPane.showMessageDialog(janela, "Cálculos exportados com sucesso.");
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(
                        janela,
                        "Informe valores numéricos válidos para os lux.",
                        "Dados inválidos",
                        JOptionPane.ERROR_MESSAGE
                    );
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(
                        janela,
                        "Não foi possível exportar os cálculos: " + e.getMessage(),
                        "Erro na exportação",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });


        janela.setLayout(null);
        button.setBounds(0,0,40,50);
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.setVisible(true);
    }


}
