package services;

import models.Atendimento;
import models.Cliente;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Agenda implements Serializable {
    private List<Atendimento> atendimentos;

    public Agenda() {
        this.atendimentos = new ArrayList<>();
    }

    // Agendar atendimento
    public void agendarAtendimento(Cliente cliente, LocalDateTime dataHora, String servico) {
        Atendimento atendimento = new Atendimento(cliente, dataHora, servico);
        atendimentos.add(atendimento);
    }

    // Editar atendimento
    public void editarAtendimento(int index, LocalDateTime novaDataHora, String novoServico) {
        if (index >= 0 && index < atendimentos.size()) {
            Atendimento atendimento = atendimentos.get(index);
            atendimento.setDataHora(novaDataHora);
            atendimento.setServico(novoServico);
        }
    }

    // Excluir atendimento
    public void excluirAtendimento(int index) {
        if (index >= 0 && index < atendimentos.size()) {
            atendimentos.remove(index);
        }
    }

    // Listar atendimentos
    public List<Atendimento> getAtendimentos() {
        return atendimentos;
    }

    // Persistência da agenda em arquivo
    public void salvarAgendaEmArquivo(String caminhoArquivo) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(caminhoArquivo))) {
            oos.writeObject(this.atendimentos);
        }
    }

    // Carregar agenda de arquivo
    public void carregarAgendaDeArquivo(String caminhoArquivo) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(caminhoArquivo))) {
            atendimentos = (List<Atendimento>) ois.readObject();
        }
    }
}
