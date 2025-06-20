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

	public void agendar(DadosAgendamentoConsulta dados) {
		if(!pacienteRepository.existsById( dados.idPaciente() ) ) {
			throw new ValidacaoExecption("Id do paciente informado não existe!");
		}
		
		if(dados.idMedico() != null && !medicoRepository.existsById( dados.idPaciente() ) ) {
			throw new ValidacaoExecption("Id do paciente informado não existe!");
		}

		validadores.forEach(v -> v.validar(dados));

		var paciente = pacienteRepository.getReferenceById(dados.idPaciente());
		var medico = escolherMedico(dados);
		var consulta = new Consulta(null,medico,paciente,dados.data());
		consultaRepository.save(consulta);
	}
	
	
	private Medico escolherMedico(DadosAgendamentoConsulta dados) {
		if(dados.idMedico() != null) {
			return medicoRepository.getReferenceById(dados.idMedico());
		}
		
		if(dados.especialidade() == null) {
			throw new ValidacaoExecption("Espeialidade é obrigatória quando o médico não for esclolhido!");
		}
		
		return medicoRepository.escolherMedicoAleatorioLivreNaData(dados.especialidade(),dados.data());
	}
}
