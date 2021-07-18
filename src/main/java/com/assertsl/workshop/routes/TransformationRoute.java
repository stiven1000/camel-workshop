package com.assertsl.workshop.routes;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.aop.framework.adapter.AfterReturningAdviceInterceptor;
import org.springframework.stereotype.Component;

@Component
public class TransformationRoute extends RouteBuilder {


    //https://api.fda.gov/drug/ndc.json?search=product_ndc:0536-2425&limit=1


    @Override
    public void configure() throws Exception {

        from("direct:getAllDrugs")
                .log("get all drugs")
                .end();

        from("direct:getDrug")
                .log("get drug")
                .end();
        from("direct:createDrug")
                .log("creating drug")
                .end();
        from("direct:updateDrug")
                .log("updating drugs")
                .end();
        from("direct:disableDrug")
                .log("disabling drugs")
                .end();

    }

}
