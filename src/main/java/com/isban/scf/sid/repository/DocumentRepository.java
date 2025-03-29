package com.isban.scf.sid.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.isban.scf.sid.model.Document;

import java.util.Date;
import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

	/**
	* This method return a list of documents calling findFilteredDocuments 
	* with a SQL query that filters between two dates.
	* 
	* @param  startDate A Date which filters removing dates before itself.
	* @param  endDate A Date which filters removing dates after itself.
	* 
	* @return a list of documents of the database in List Document format.
	*/
    @Query(value = "SELECT CLIENT_APP_ID, CLIENT_DOC_ID, PROVIDER_ID, SENT_AT, RECEIVED_AT"
    		    + " FROM documents WHERE SENT_AT > :startDate AND SENT_AT < :endDate", nativeQuery = true)
    List<Document> findFilteredDocuments(@Param("startDate") Date startDate, @Param("endDate") Date endDate); 

    @Query(value = "SELECT * FROM SID_DOCUMENT_ASYNC WHERE RECEIVED_AT IS NULL", nativeQuery = true)
    List<Document> findNotReceivedDocuments();
    
    List<Document> findByReceivedAtIsNull();
    
}

