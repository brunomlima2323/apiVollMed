package med.voll.api.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import med.voll.api.domain.consulta.AgendaConsultas;
import med.voll.api.domain.consulta.DadosAgendamentoConsulta;

@RestController
@RequestMapping("consultas")
@SecurityRequirement(name = "bearer-key")
public class ConsultaController {
	
	@Autowired
	private AgendaConsultas agenda;

	 @PostMapping
	 @Transactional
	 public ResponseEntity agendar(@RequestBody @Valid DadosAgendamentoConsulta dados) {
	     var dto = agenda.agendar(dados);
	     return ResponseEntity.ok(dto);
	 }

	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity cancelar(@PathVariable("id") Long id) {
		agenda.cancelar(id);
		return ResponseEntity.ok("Consulta ID cancelada com sucesso!");
	}

}