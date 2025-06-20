package med.voll.api.domain.validacoes;

import med.voll.api.domain.ValidacaoExecption;
import med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import med.voll.api.domain.paciente.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidadorPacienteAtivo implements ValidadorAgendamentoDeConsulta {

    @Autowired
    private PacienteRepository repository;

    public void validar(DadosAgendamentoConsulta dados){
        var pacienteEstadoAtivo = repository.findAtivoById(dados.idPaciente());
        if(!pacienteEstadoAtivo){
            throw new ValidacaoExecption("Consulta não pode ser agendada com paciente excluído");
        }
    }
}
