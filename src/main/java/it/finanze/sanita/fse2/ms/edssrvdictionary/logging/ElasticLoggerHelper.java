package it.finanze.sanita.fse2.ms.edssrvdictionary.logging;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import it.finanze.sanita.fse2.ms.edssrvdictionary.enums.ILogEnum;
import net.logstash.logback.argument.StructuredArguments;

/** 
 * 
 * @author: Guido Rocco - IBM 
 */ 
@Service
public class ElasticLoggerHelper {
    
	Logger log = LoggerFactory.getLogger("elastic-logger"); 
	  
	
	/* 
	 * Specify here the format for the dates 
	 */
	private DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS"); 
	private static final String OPERATION_KEY = "operation";
	private static final String OP_RESULT_KEY = "op-result";
	private static final String TS_START_KEY = "op-timestamp-start";
	private static final String TS_END_KEY = "op-timestamp-end";
	
	
	
	/* 
	 * Implements structured logs, at all logging levels
	 */
	public void trace(String message, ILogEnum operation, 
			   ILogEnum result, Date startDateOperation) {
		
		log.trace(message, 
				 StructuredArguments.kv(OPERATION_KEY, operation.getCode()), 
				 StructuredArguments.kv(OP_RESULT_KEY, result.getCode()),
				 StructuredArguments.kv(TS_START_KEY, dateFormat.format(startDateOperation)),
				 StructuredArguments.kv(TS_END_KEY, dateFormat.format(new Date()))); 
	} 
	
	public void debug(String message,  ILogEnum operation,  
			ILogEnum result, Date startDateOperation) {
		
		log.debug(message,  
				 StructuredArguments.kv(OPERATION_KEY, operation.getCode()), 
				 StructuredArguments.kv(OP_RESULT_KEY, result.getCode()),
				 StructuredArguments.kv(TS_START_KEY, dateFormat.format(startDateOperation)),
				 StructuredArguments.kv(TS_END_KEY, dateFormat.format(new Date()))); 
	} 
	 
	public void info(String message, ILogEnum operation,  
			ILogEnum result, Date startDateOperation) {
		
		log.info(message,  
				 StructuredArguments.kv(OPERATION_KEY, operation.getCode()), 
				 StructuredArguments.kv(OP_RESULT_KEY, result.getCode()),
				 StructuredArguments.kv(TS_START_KEY, dateFormat.format(startDateOperation)),
				 StructuredArguments.kv(TS_END_KEY, dateFormat.format(new Date()))); 
	} 
	
	public void warn(String message, ILogEnum operation,  
			ILogEnum result, Date startDateOperation) {
		
		log.warn(message, 
				 StructuredArguments.kv(OPERATION_KEY, operation.getCode()), 
				 StructuredArguments.kv(OP_RESULT_KEY, result.getCode()),
				 StructuredArguments.kv(TS_START_KEY, dateFormat.format(startDateOperation)),
				 StructuredArguments.kv(TS_END_KEY, dateFormat.format(new Date()))); 
	} 
	
	public void error(String message, ILogEnum operation,  
			ILogEnum result, Date startDateOperation,
			   ILogEnum error) {
		
		log.error(message,  
				 StructuredArguments.kv(OPERATION_KEY, operation.getCode()), 
				 StructuredArguments.kv(OP_RESULT_KEY, result.getCode()),
				 StructuredArguments.kv(TS_START_KEY, dateFormat.format(startDateOperation)),
				 StructuredArguments.kv(TS_END_KEY, dateFormat.format(new Date())),
				 StructuredArguments.kv("op-error", error.getCode()),
				 StructuredArguments.kv("op-error-description", error.getDescription())); 
	}
    	
    
}