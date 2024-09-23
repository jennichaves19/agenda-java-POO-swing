package models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Atendimento implements Serializable {
    private Cliente cliente;
    private LocalDateTime dataHora;
    private String servico;

    public Atendimento(Cliente cliente, LocalDateTime dataHora, String servico) {
        this.cliente = cliente;
        this.dataHora = dataHora;
        this.servico = servico;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public String getServico() {
        return servico;
    }

    public void setServico(String servico) {
        this.servico = servico;
    }

    @Override
    public String toString() {
        // Formatação da data para o formato DD/MM/YYYY - HH:MM
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm");
        return "<b>Atendimento</b><br>" +
                "<b>Cliente:</b> " + cliente.getNome() + "<br>" +
                "<b>Telefone:</b> " + cliente.getTelefone() + "<br>" +
                "<b>Serviço:</b> " + servico + "<br>" +
                "<b>Data/Hora:</b> " + dataHora.format(formatter) + "<br><br>";
    }

}
