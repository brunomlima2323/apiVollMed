package med.voll.api.domain.consulta;

import med.voll.api.domain.validacoes.ValidadorAgendamentoDeConsulta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import med.voll.api.domain.ValidacaoExecption;
import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.medico.MedicoRepository;
import med.voll.api.domain.paciente.PacienteRepository;

import java.util.List;

@Service
public class AgendaConsultas {
	
	@Autowired
	private ConsultaRepository consultaRepository;
	
	@Autowired
	private MedicoRepository medicoRepository;
	
	@Autowired
	private PacienteRepository pacienteRepository;

	@Autowired
	private List<ValidadorAgendamentoDeConsulta> validadores;

	public DadosDetalhamentoConsulta agendar(DadosAgendamentoConsulta dados) {
		if(!pacienteRepository.existsById( dados.idPaciente() ) ) {
			throw new ValidacaoExecption("Id do paciente informado não existe!");
		}
		
		if(dados.idMedico() != null && !medicoRepository.existsById( dados.idMedico() ) ) {
			throw new ValidacaoExecption("Id do médico informado não existe!");
		}

		validadores.forEach(v -> v.validar(dados));

		var paciente = pacienteRepository.getReferenceById(dados.idPaciente());
		var medico = escolherMedico(dados);
		if (medico == null) {
			throw new ValidacaoExecption("Não existe médico disponível nessa data!");
		}

		var consulta = new Consulta(null, medico, paciente, dados.data());
		consultaRepository.save(consulta);

		return new DadosDetalhamentoConsulta(consulta);
	}
	
	
	private Medico escolherMedico(DadosAgendamentoConsulta dados) {
		if(dados.idMedico() != null) {
			return medicoRepository.getReferenceById(dados.idMedico());
		}
		
		if(dados.especialidade() == null) {
			throw new ValidacaoExecption("Especialidade é obrigatória quando o médico não for esclolhido!");
		}
		
		return medicoRepository.escolherMedicoAleatorioLivreNaData(dados.especialidade(),dados.data());
	}

	public void cancelar(Long id) {
		consultaRepository.findById(id).orElseThrow(() -> new ValidacaoExecption("Agendamento id: "+id + " não existe!"));
	}
}
