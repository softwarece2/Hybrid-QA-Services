package in.co.bel.olms.transaction.repository;
/**
 * @author Pooja Kariger
 * @since 16-09-2026
 */

import org.springframework.data.jpa.repository.JpaRepository;

import in.co.bel.olms.transaction.entity.QacDistribution;

public interface QacDistributionRepository  extends JpaRepository<QacDistribution, Integer>{

	QacDistribution findByInoteIdId(Integer id);

}
