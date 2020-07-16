package icecreamPickup;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface SalesRepository extends PagingAndSortingRepository<Sales, Long>{

    Sales findByOrderId(Long orderId);
}