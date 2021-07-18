package com.assertsl.workshop.routes;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

/**
 * A simple Camel route that triggers from a timer and calls a bean and prints to system out.
 * <p/>
 * Use <tt>@Component</tt> to make Camel auto detect this route when starting.
 */
@Component
public class RestConsumerRoute extends RouteBuilder {

    @Override
    public void configure() {

        rest("/drug/")
                .get("/all").to("direct:getAllDrugs")
                .get("/{ncdCode}").to("direct:getDrug")
                .post("/{ncdCode}").to("direct:createDrug")
                .put("/{ncdCode}").to("direct:updateDrug")
                .delete("/{ncdCode}").to("direct:disableDrug");


    }

}
