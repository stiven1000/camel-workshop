package com.assertsl.workshop.routes;

import com.assertsl.workshop.beans.FdaEnricher;
import com.assertsl.workshop.configuration.DatabaseProperties;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.http.base.HttpOperationFailedException;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransformationRoute extends RouteBuilder {

    @Autowired
    private DatabaseProperties databaseProperties;

    @Autowired
    private FdaEnricher fdaEnricher;

    @Override
    public void configure() throws Exception {

        onException(HttpOperationFailedException.class).handled(true)
                .log(LoggingLevel.ERROR, "error consuming the FDA API: ${exception.message}")
                .log(LoggingLevel.ERROR, "backend response Body: ${exception.responseBody}")
                .setBody(simple("${exception.responseBody}"))
                .unmarshal().json(JsonLibrary.Jackson)
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(502)) //BAD GATEWAY
                .end();

        from("direct:getAllDrugs")
                .log("get all drugs")
                .to("jpa:com.assertsl.workshop.domain.DrugStore?query=" + databaseProperties.getGetAllDrugs())
                .end();

        from("direct:getDrug")
                .log("get drug ${headers.ncdCode}")
                .setHeader("CamelJpaParameters", method("transformationBean","getDrugParameters"))
                .to("jpa:com.assertsl.workshop.domain.DrugStore?query=" + databaseProperties.getGetDrug())
                .end();
        from("direct:createDrug")
                .log("creating drug")
                .enrich("direct:getDrugInformation", fdaEnricher)
                .to("jpa:com.assertsl.workshop.domain.DrugStore")

                .end();
        from("direct:updateDrug")
                .log("updating drug")
                .setHeader("CamelJpaParameters", method("transformationBean","updateDrugParameters"))
                .to("jpa:com.assertsl.workshop.domain.DrugStore?useExecuteUpdate=true&query=" + databaseProperties.getUpdateDrug())
                .end();

        from("direct:disableDrug")
                .log("disabling drug ${headers.ncdCode}")
                //TODO: Update the drug with INACTIVE status
                .to("jpa:com.assertsl.workshop.domain.DrugStore")
                .end();


        from("direct:getDrugInformation")
                .log("Getting drug Information from FDA API")
                .setHeader(Exchange.HTTP_QUERY, simple("search=product_ndc:${body.productNdc}"))
                .setHeader(Exchange.HTTP_URI,  constant("https://api.fda.gov/drug/ndc.json"))
                .setHeader(Exchange.HTTP_METHOD, constant("GET"))
                .setBody(constant(null))
                .to("http:invokeFdaApi")
                .log("Response from FDA API ${body}")
                .log("Getting required fields")
                .setHeader("packageDescription", jsonpath("$.results[0].packaging[0].description"))
                //TODO: get genericName and labelerName fields
                .log("Info obtained packageDescription: ${headers.packageDescription}, labelerName: ${headers.labelerName}, genericName: ${headers.genericName}")
                .end();



    }

}
