package gwssi.kafka.demo.service;

import java.util.Iterator;
import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.listener.BatchMessageListener;

public class KafkaBatchConsumer  implements BatchMessageListener<String, String> {
	private static Logger logger = LoggerFactory.getLogger(KafkaBatchConsumer.class);
	   
    private final String consumerGroupId;
    public KafkaBatchConsumer(String consumerGroupId) {
        this.consumerGroupId = consumerGroupId;
    }

    @Override
    public void onMessage(List<ConsumerRecord<String, String>> data) {
    	Iterator<ConsumerRecord<String, String>> it = data.iterator();
    	logger.info("[@@@" + consumerGroupId + "] => Batch Size=" + data.size() );
    	while(it.hasNext()) {
    		ConsumerRecord<String, String> record = it.next();
    		logger.info(" Key="  + record.key() + ", Partition=" + record.partition() +
    				", Topic=" + record.topic()+", Value=" + record.value() );    	
    	}
        
    }

}
