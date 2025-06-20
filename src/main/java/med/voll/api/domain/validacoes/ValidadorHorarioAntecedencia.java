package med.voll.api.domain.validacoes;

import med.voll.api.domain.ValidacaoExecption;
import med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import org.hibernate.annotations.Comment;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class ValidadorHorarioAntecedencia implements ValidadorAgendamentoDeConsulta{
    public void validar(DadosAgendamentoConsulta dados) {
        var dataConslta = dados.data();
        var agora = LocalDateTime.now();
        var diferencaEmMinutos = Duration.between(agora,dataConslta).toMinutes();

        if(diferencaEmMinutos < 30){
            throw new ValidacaoExecption("Consulta deve ser agendada com antecedencia minima de 30 minutos");
        }
    }
}
