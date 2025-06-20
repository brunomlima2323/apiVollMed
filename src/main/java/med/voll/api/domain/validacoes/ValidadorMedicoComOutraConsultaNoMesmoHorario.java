package med.voll.api.domain.validacoes;

import med.voll.api.domain.ValidacaoExecption;
import med.voll.api.domain.consulta.ConsultaRepository;
import med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidadorMedicoComOutraConsultaNoMesmoHorario implements ValidadorAgendamentoDeConsulta {

    @Autowired
    private ConsultaRepository repository;

    public void validar(DadosAgendamentoConsulta dados){
        var medicoPossuiOutraConsultaNoMesmoHorario = repository.existsByMedicoIdAndData(dados.idMedico(),dados.data());
        if(medicoPossuiOutraConsultaNoMesmoHorario){
            throw new ValidacaoExecption("Medico já possui consulta agendada nesse mesmo horário");
        }
    }
}
