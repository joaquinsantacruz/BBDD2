package unlp.info.bd2.repositories;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.bson.Document;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import unlp.info.bd2.model.Supplier;

public interface SupplierRepository extends MongoRepository<Supplier, ObjectId> {

    Optional<Supplier> findByAuthorizationNumber(String authorizationNumber);

    @Aggregation(pipeline = {
        "{ $unwind: '$services' }",
        "{ $unwind: '$services.itemServiceList' }",
        "{ $group: { _id: '$_id', totalQuantity: { $sum: '$services.itemServiceList.quantity' }, supplier: { $first: '$$ROOT' } } }",
        "{ $sort: { totalQuantity: -1 } }",
        "{ $replaceRoot: { newRoot: '$supplier' } }"
    })
    List<Supplier> getTopNSuppliersInPurchases(Pageable pageable);

    @Aggregation(pipeline = {
    "{ $unwind: '$services' }",
    "{ $unwind: '$services.itemServiceList' }",
    "{ $group: { _id: '$_id', totalItemsSold: { $sum: '$services.itemServiceList.quantity' }, supplier: { $first: '$$ROOT' } } }",
    "{ $sort: { totalItemsSold: -1 } }",
    "{ $replaceRoot: { newRoot: '$supplier' } }"
    })
    List<Supplier> getTopNSuppliersItemsSold(Pageable pageable);

    @Aggregation(pipeline = {
    // Cuenta cuántos servicios tiene cada proveedor
    "{ $project: { serviceCount: { $size: '$services' } } }",
    // Ordena por cantidad de servicios de mayor a menor
    "{ $sort: { serviceCount: -1 } }",
    // Limita a uno solo (el que más tiene)
    "{ $limit: 1 }"
    })
    Optional<Document> getMaxServicesOfSupplier();
} 
