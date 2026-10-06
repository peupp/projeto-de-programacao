import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.axis.NumberAxis;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

public class InterfaceCalibracao extends JFrame {
    private Runnable aoVoltar;



    // Entradas
    private final JTextField campoNome = new JTextField("Analito", 10);
    private final JTextField campoConcentracao = new JTextField(8);
    private final JTextField campoAbsorbancia = new JTextField(8);
    private final DefaultTableModel modeloTabela =
            new DefaultTableModel(new String[]{"Concentração", "Absorbância"}, 0) {
                @Override public boolean isCellEditable(int r, int c) { return false; }
            };
    private final JLabel labelEquacao = new JLabel("y = ?x + ?");

    // Amostra desconhecida
    private final JTextField campoAbsorbanciaAmostra = new JTextField(8);
    private final JTextField campoResultado = new JTextField(10);

    // Gráfico (JFreeChart)
    private final XYSeries serieDados = new XYSeries("Pontos de calibração");
    private final XYSeries serieReta = new XYSeries("Reta de calibração");
    private final XYSeries serieAmostra = new XYSeries("Amostra");
    private final JFreeChart grafico;

    // Dados
    private final ArrayList<double[]> pontos = new ArrayList<>(); // {concentracao, absorbancia}
    private CalibracaoLinear calibracao;
    private double intercepto;

    public InterfaceCalibracao() {
        super("Calibração Linear");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        grafico = criarGrafico();
        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.addActionListener(e -> {
            setVisible(false);
            if (aoVoltar != null) aoVoltar.run();
        });
        JPanel topo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topo.add(btnVoltar);
        add(topo, BorderLayout.NORTH);

        add(montarPainelEsquerdo(), BorderLayout.WEST);
        add(new ChartPanel(grafico), BorderLayout.CENTER);

        setSize(1000, 600);
        setLocationRelativeTo(null);
    }
    public void setAoVoltar(Runnable aoVoltar) {
        this.aoVoltar = aoVoltar;
    }
    private JFreeChart criarGrafico() {
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(serieDados);   // índice 0
        dataset.addSeries(serieReta);    // índice 1
        dataset.addSeries(serieAmostra); // índice 2

        JFreeChart chart = ChartFactory.createScatterPlot(
                "Curva de Calibração",
                "Concentração (mg/L)",
                "Absorbância",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false
        );

        XYPlot plot = chart.getXYPlot();
        XYLineAndShapeRenderer r = new XYLineAndShapeRenderer();

        // pontos: só marcadores
        r.setSeriesLinesVisible(0, false);
        r.setSeriesShapesVisible(0, true);
        r.setSeriesShape(0, new Ellipse2D.Double(-4, -4, 8, 8));
        r.setSeriesPaint(0, new Color(30, 90, 200));

        // reta: só linha
        r.setSeriesLinesVisible(1, true);
        r.setSeriesShapesVisible(1, false);
        r.setSeriesStroke(1, new BasicStroke(2f));
        r.setSeriesPaint(1, new Color(200, 50, 50));

        // amostra: só marcador
        r.setSeriesLinesVisible(2, false);
        r.setSeriesShapesVisible(2, true);
        r.setSeriesShape(2, new Ellipse2D.Double(-6, -6, 12, 12));
        r.setSeriesPaint(2, new Color(40, 150, 60));

        plot.setRenderer(r);
        ((NumberAxis) plot.getDomainAxis()).setAutoRangeIncludesZero(true);
        ((NumberAxis) plot.getRangeAxis()).setAutoRangeIncludesZero(true);
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        return chart;
    }

    private JPanel montarPainelEsquerdo() {
        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setPreferredSize(new Dimension(300, 0));

        // --- Pontos de calibração ---
        JPanel entrada = new JPanel(new GridLayout(0, 2, 5, 5));
        entrada.setBorder(BorderFactory.createTitledBorder("Pontos de calibração"));
        entrada.add(new JLabel("Nome:"));          entrada.add(campoNome);
        entrada.add(new JLabel("Concentração:"));  entrada.add(campoConcentracao);
        entrada.add(new JLabel("Absorbância:"));   entrada.add(campoAbsorbancia);

        JButton btnAdicionar = new JButton("Adicionar ponto");
        btnAdicionar.addActionListener(e -> adicionarPonto());
        JButton btnLimpar = new JButton("Limpar");
        btnLimpar.addActionListener(e -> limpar());
        entrada.add(btnAdicionar);
        entrada.add(btnLimpar);

        JScrollPane tabela = new JScrollPane(new JTable(modeloTabela));
        tabela.setPreferredSize(new Dimension(280, 150));

        JButton btnCalcularReta = new JButton("Calcular reta de calibração");
        btnCalcularReta.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCalcularReta.addActionListener(e -> calcularReta());

        labelEquacao.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- Amostra desconhecida ---
        JPanel amostra = new JPanel(new GridLayout(0, 2, 5, 5));
        amostra.setBorder(BorderFactory.createTitledBorder("Amostra desconhecida"));
        amostra.add(new JLabel("Absorbância:"));
        amostra.add(campoAbsorbanciaAmostra);
        JButton btnConcentracao = new JButton("Calcular concentração");
        btnConcentracao.addActionListener(e -> calcularConcentracao());
        amostra.add(btnConcentracao);
        amostra.add(new JLabel(""));
        amostra.add(new JLabel("Concentração:"));
        campoResultado.setEditable(false);
        amostra.add(campoResultado);

        painel.add(entrada);
        painel.add(Box.createVerticalStrut(8));
        painel.add(tabela);
        painel.add(Box.createVerticalStrut(8));
        painel.add(btnCalcularReta);
        painel.add(Box.createVerticalStrut(5));
        painel.add(labelEquacao);
        painel.add(Box.createVerticalStrut(8));
        painel.add(amostra);
        return painel;
    }

    private void adicionarPonto() {
        try {
            double conc = lerDouble(campoConcentracao);
            double abs = lerDouble(campoAbsorbancia);

            pontos.add(new double[]{conc, abs});
            modeloTabela.addRow(new Object[]{conc, abs});
            serieDados.add(conc, abs); // o gráfico atualiza sozinho

            campoConcentracao.setText("");
            campoAbsorbancia.setText("");
            campoConcentracao.requestFocus();
        } catch (NumberFormatException ex) {
            erro("Digite valores numéricos válidos.");
        }
    }

    private void calcularReta() {
        if (pontos.size() < 2) {
            erro("Adicione pelo menos 2 pontos de calibração.");
            return;
        }
        calibracao = new CalibracaoLinear();
        for (double[] p : pontos) {
            calibracao.adicionarDado(p[0], p[1], campoNome.getText());
        }
        calibracao.calcularMedias();
        calibracao.calcularCurvaM();

        double m = calibracao.getInclinacao();
        intercepto = calibracao.getMediaY() - m * calibracao.getMediaX();

        double xMax = 0;
        for (double[] p : pontos) xMax = Math.max(xMax, p[0]);

        // desenha a reta de x = 0 até x = xMax
        serieReta.clear();
        serieReta.add(0, intercepto);
        serieReta.add(xMax, m * xMax + intercepto);

        labelEquacao.setText(String.format("y = %.4fx %s %.4f",
                m, intercepto < 0 ? "-" : "+", Math.abs(intercepto)));
        grafico.setTitle("Curva de Calibração - " + campoNome.getText());
    }

    private void calcularConcentracao() {
        if (calibracao == null) {
            erro("Calcule a reta de calibração primeiro.");
            return;
        }
        try {
            double abs = lerDouble(campoAbsorbanciaAmostra);
            double conc = calibracao.calcularConcentracaoPelaAbsorbancia(abs);
            campoResultado.setText(String.format("%.4f", conc));

            serieAmostra.clear();
            serieAmostra.add(conc, abs);
        } catch (NumberFormatException ex) {
            erro("Digite um valor numérico válido.");
        }
    }

    private void limpar() {
        pontos.clear();
        modeloTabela.setRowCount(0);
        serieDados.clear();
        serieReta.clear();
        serieAmostra.clear();
        calibracao = null;
        labelEquacao.setText("y = ?x + ?");
        campoResultado.setText("");
        grafico.setTitle("Curva de Calibração");
    }

    // aceita vírgula ou ponto como separador decimal
    private double lerDouble(JTextField campo) {
        return Double.parseDouble(campo.getText().trim().replace(',', '.'));
    }

    private void erro(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new InterfaceCalibracao().setVisible(true));
    }
}