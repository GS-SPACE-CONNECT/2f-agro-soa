package br.com.fiap.agro.soa.repository;

import br.com.fiap.agro.soa.domain.CadastroRural;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório Spring Data JPA do {@link CadastroRural} (usado pelo Web Service SOAP).
 *
 * <p>As consultas por CPF e por CAR são derivadas automaticamente do nome do método.
 * Usamos {@code findFirst...OrderByIdDesc} para retornar sempre o cadastro <b>mais recente</b>,
 * evitando erro de "resultado não único" caso o mesmo CPF/CAR seja registrado mais de uma vez.</p>
 */
@Repository
public interface CadastroRuralRepository extends JpaRepository<CadastroRural, Long> {

    Optional<CadastroRural> findFirstByCpfOrderByIdDesc(String cpf);

    Optional<CadastroRural> findFirstByCarOrderByIdDesc(String car);
}
