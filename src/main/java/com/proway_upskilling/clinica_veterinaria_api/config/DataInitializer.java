package com.proway_upskilling.clinica_veterinaria_api.config;

import com.proway_upskilling.clinica_veterinaria_api.model.Cliente;
import com.proway_upskilling.clinica_veterinaria_api.model.Consulta;
import com.proway_upskilling.clinica_veterinaria_api.model.Pet;
import com.proway_upskilling.clinica_veterinaria_api.model.Veterinario;
import com.proway_upskilling.clinica_veterinaria_api.repository.ClienteRepository;
import com.proway_upskilling.clinica_veterinaria_api.repository.ConsultaRepository;
import com.proway_upskilling.clinica_veterinaria_api.repository.PetRepository;
import com.proway_upskilling.clinica_veterinaria_api.repository.VeterinarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initDatabase(PetRepository petRepository,
                                          ClienteRepository clienteRepository,
                                          VeterinarioRepository veterinarioRepository,
                                          ConsultaRepository consultaRepository) {
        return args -> {
            Cliente cliente = new Cliente();
            cliente.setNome("João da Silva");
            cliente.setEmail("teste@local.dev");
            cliente.setEndereco("Rua Teste da Silva");
            cliente.setTelefone("48999999999");
            clienteRepository.save(cliente);

            Pet pet = new Pet();
            pet.setNome("Rex");
            pet.setEspecie("Canina");
            pet.setRaca("Labrador");
            pet.setPeso(25.5);
            pet.setDataNascimento(LocalDate.of(2020, 5, 10));
            pet.setCliente(cliente);
            petRepository.save(pet);

            Veterinario vet = new Veterinario();
            vet.setNome("Dra. Mariana Silva");
            vet.setEspecialidade("Veterinária Geral");
            vet.setCrmv("SP12345678901");
            vet.setDataContratacao(LocalDate.of(2023, 3, 15));
            veterinarioRepository.save(vet);

            Consulta consulta = new Consulta();
            consulta.setPet(pet);
            consulta.setVeterinario(vet);
            consulta.setCliente(cliente);
            consulta.setMotivo("Consulta de rotina");
            consulta.setDiagnostico("Animal saudável");
            consulta.setTratamento("Acompanhamento anual");
            consulta.setStatus("A");
            consultaRepository.save(consulta);
        };
    }
}
