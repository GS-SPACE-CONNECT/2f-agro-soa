package br.com.fiap.agro.soa.repository;

import br.com.fiap.agro.soa.domain.Propriedade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositório de {@link Propriedade} via <b>Spring Data JPA</b> (issue #5).
 *
 * <p>Ao estender {@code JpaRepository<Propriedade, Long>}, o Spring gera em tempo de execução
 * a implementação com {@code findAll}, {@code findById}, {@code save}, {@code deleteById} e
 * {@code existsById} — os mesmos métodos já consumidos pelo service desde a issue #4, agora
 * persistindo no H2.</p>
 */
@Repository
public interface PropriedadeRepository extends JpaRepository<Propriedade, Long> {
}
