package com.assertsl.workshop.beans;

import com.assertsl.workshop.domain.DrugStore;
import com.assertsl.workshop.dto.DrugDto;
import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;

@Component
public class FdaEnricher implements AggregationStrategy {

    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        DrugDto drugDto = oldExchange.getIn().getBody(DrugDto.class);
        DrugStore drugStore = new DrugStore();

        drugStore.setProductNdc(drugDto.getProductNdc());
        drugStore.setExistences(drugDto.getExistences());
        drugStore.setPrice(drugDto.getPrice());

        //TODO: set fields packageDescription labelerName genericName returned by the query to drugStore


        oldExchange.getIn().setBody(drugStore);
        return oldExchange;
    }
}
