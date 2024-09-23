package interfaceGrafica;

import models.Atendimento;
import models.Cliente;
import services.Agenda;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class InterfaceSalao extends JFrame {
    private Agenda agenda;
    private JPanel panelAtendimentos; // Painel para exibir os atendimentos
    private JFormattedTextField nomeClienteField, telefoneClienteField, dataField, horaField;
    private JComboBox<String> servicoComboBox;

    public InterfaceSalao() {
        agenda = new Agenda();
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setTitle("Agenda do Salão de Beleza");
        setSize(700, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Painel de exibição dos atendimentos
        panelAtendimentos = new JPanel();
        panelAtendimentos.setLayout(new BoxLayout(panelAtendimentos, BoxLayout.Y_AXIS)); // Exibe os atendimentos em coluna
        JScrollPane scrollPane = new JScrollPane(panelAtendimentos);
        add(scrollPane, BorderLayout.CENTER);

        // Painel de entrada de dados
        JPanel panelInput = new JPanel();
        panelInput.setLayout(new GridLayout(6, 2));

        panelInput.add(new JLabel("Nome do Cliente:"));
        nomeClienteField = new JFormattedTextField();
        panelInput.add(nomeClienteField);

        panelInput.add(new JLabel("Telefone (DDD) - 9 Dígitos:"));
        try {
            MaskFormatter telefoneMask = new MaskFormatter("(##) #####-####");
            telefoneMask.setPlaceholderCharacter('_');
            telefoneClienteField = new JFormattedTextField(telefoneMask);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        panelInput.add(telefoneClienteField);

        panelInput.add(new JLabel("Serviço:"));
        String[] servicos = {
                "Unha das mãos",
                "Unha dos pés",
                "Unhas dos pés e mãos",
                "Sobrancelha",
                "Spa dos pés",
                "Limpeza de pele"
        };
        servicoComboBox = new JComboBox<>(servicos);
        panelInput.add(servicoComboBox);

        panelInput.add(new JLabel("Data (DD/MM/YYYY):"));
        try {
            MaskFormatter dateMask = new MaskFormatter("##/##/####");
            dateMask.setPlaceholderCharacter('_');
            dataField = new JFormattedTextField(dateMask);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        panelInput.add(dataField);

        panelInput.add(new JLabel("Hora (HH:mm):"));
        try {
            MaskFormatter horaMask = new MaskFormatter("##:##");
            horaMask.setPlaceholderCharacter('_');
            horaField = new JFormattedTextField(horaMask);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        panelInput.add(horaField);

        JButton botaoAgendar = new JButton("Agendar Atendimento");
        botaoAgendar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agendarAtendimento();
            }
        });
        panelInput.add(botaoAgendar);

        JButton botaoSalvar = new JButton("Salvar Agenda");
        botaoSalvar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salvarAgenda();
            }
        });
        panelInput.add(botaoSalvar);

        add(panelInput, BorderLayout.SOUTH);

        carregarAgenda();
    }

    private void agendarAtendimento() {
        String nome = nomeClienteField.getText();
        String telefone = telefoneClienteField.getText();
        String servico = (String) servicoComboBox.getSelectedItem(); // Pega o serviço selecionado
        String dataStr = dataField.getText();
        String horaStr = horaField.getText();

        try {
            // Converter a data no formato DD/MM/YYYY
            DateTimeFormatter dataFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate data = LocalDate.parse(dataStr, dataFormatter);

            // Converter a hora no formato HH:mm
            DateTimeFormatter horaFormatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime hora = LocalTime.parse(horaStr, horaFormatter);

            LocalDateTime dataHora = LocalDateTime.of(data, hora);

            Cliente cliente = new Cliente(nome, telefone);
            agenda.agendarAtendimento(cliente, dataHora, servico);

            atualizarPainelDeAtendimentos();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao agendar o atendimento: " + e.getMessage());
        }
    }

    private void atualizarPainelDeAtendimentos() {
        panelAtendimentos.removeAll(); // Limpa o painel

        List<Atendimento> atendimentos = agenda.getAtendimentos();
        for (Atendimento atendimento : atendimentos) {
            // Painel principal do atendimento
            JPanel atendimentoPanel = new JPanel();
            atendimentoPanel.setLayout(new BorderLayout()); // Layout para alinhar o botão à direita

            // Painel para o texto do atendimento
            JPanel textPanel = new JPanel();
            textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS)); // Layout vertical para o texto
            JLabel atendimentoLabel = new JLabel(formatarAtendimento(atendimento));
            atendimentoLabel.setHorizontalAlignment(SwingConstants.LEFT);
            textPanel.add(atendimentoLabel);

            // Adiciona o painel de texto ao painel principal
            atendimentoPanel.add(textPanel, BorderLayout.CENTER);

            // Cria um painel para o botão de exclusão
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
            JButton botaoExcluir = new JButton("Excluir");
            botaoExcluir.setPreferredSize(new Dimension(100, 30)); // Define um tamanho fixo para o botão
            botaoExcluir.setMaximumSize(new Dimension(100, 30)); // Garante que o botão não cresça
            botaoExcluir.setMinimumSize(new Dimension(100, 30)); // Garante que o botão não diminua
            botaoExcluir.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    excluirAtendimento(atendimentos.indexOf(atendimento));
                }
            });
            buttonPanel.add(botaoExcluir);
            buttonPanel.add(Box.createVerticalGlue()); // Adiciona um espaço flexível para empurrar o botão para baixo

            // Adiciona o painel do botão ao painel principal
            atendimentoPanel.add(buttonPanel, BorderLayout.EAST);

            panelAtendimentos.add(atendimentoPanel);
        }

        panelAtendimentos.revalidate(); // Revalida o painel para atualizar a visualização
        panelAtendimentos.repaint(); // Repaint para garantir que o painel seja desenhado corretamente
    }

    private String formatarAtendimento(Atendimento atendimento) {
        return "<html>" + atendimento.toString() + "</html>";
    }



    /*
    private String formatarAtendimento(Atendimento atendimento) {
        // Formatação da data para o formato DD/MM/YYYY - HH:mm
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm");
        return "<html><b>Atendimento</b><br>" +
                "<b>Cliente:</b> " + atendimento.getCliente().getNome() + "<br>" +
                "<b>Telefone:</b> " + atendimento.getCliente().getTelefone() + "<br>" +
                "<b>Serviço:</b> " + atendimento.getServico() + "<br>" +
                "<b>Data/Hora:</b> " + atendimento.getDataHora().format(formatter) + "<br><br></html>";
    }


  */
    private void excluirAtendimento(int index) {
        if (JOptionPane.showConfirmDialog(this, "Tem certeza de que deseja excluir este atendimento?", "Confirmação de Exclusão", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            agenda.excluirAtendimento(index);
            atualizarPainelDeAtendimentos();
        }
    }

    private void salvarAgenda() {
        try {
            agenda.salvarAgendaEmArquivo("agenda_salao.ser");
            JOptionPane.showMessageDialog(this, "Agenda salva com sucesso!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar a agenda: " + e.getMessage());
        }
    }

    private void carregarAgenda() {
        try {
            agenda.carregarAgendaDeArquivo("agenda_salao.ser");
            atualizarPainelDeAtendimentos();
        } catch (IOException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Nenhum arquivo de agenda encontrado, iniciando nova agenda.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            InterfaceSalao tela = new InterfaceSalao();
            tela.setVisible(true);
        });
    }
}
