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
        "{ $addFields: { 'itemServiceCount': { $size: { $ifNull: ['$services.itemServiceList', []] } } } }",
        "{ $group: { " +
                "_id: '$_id', " +
                "businessName: { $first: '$businessName' }, " +
                "authorizationNumber: { $first: '$authorizationNumber' }, " +
                "services: { $push: '$services' }, " +
                "totalItemServices: { $sum: '$itemServiceCount' } " +
                "} }",
        "{ $sort: { totalItemServices: -1 } }",
        "{ $project: { totalItemServices: 0 } }"
    })
    List<Supplier> getTopNSuppliersInPurchases(Pageable pageable);

    @Aggregation(pipeline = {
        "{ $unwind: '$services' }",
        "{ $addFields: { 'itemServiceCount': { $size: { $ifNull: ['$services.itemServiceList', []] } } } }",
        "{ $group: { " +
                "_id: '$_id', " +
                "businessName: { $first: '$businessName' }, " +
                "authorizationNumber: { $first: '$authorizationNumber' }, " +
                "services: { $push: '$services' }, " +
                "totalItemServices: { $sum: '$itemServiceCount' } " +
                "} }",
        "{ $sort: { totalItemServices: -1 } }",
        "{ $project: { totalItemServices: 0 } }"
    })
    List<Supplier> getTopNSuppliersItemsSold(Pageable pageable);
    
} 
