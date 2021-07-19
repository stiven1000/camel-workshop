package com.assertsl.workshop.routes;

import com.assertsl.workshop.domain.DrugStore;
import com.assertsl.workshop.dto.DrugDto;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;
import org.springframework.stereotype.Component;

@Component
public class RestConsumerRoute extends RouteBuilder {

    @Override
    public void configure() {
        getContext().setStreamCaching(true);

        restConfiguration()
                .bindingMode(RestBindingMode.json)
                .apiContextPath("/api-doc")
                .apiProperty("api.title", "Drug Store API").apiProperty("api.version", "1.0.0")
                .apiProperty("cors", "true");

        rest("/drug/").description("Drug Store Api")
                .get("/all").outType(DrugStore[].class).description("Get all drugs")
                .responseMessage().code(200).message("operation executed successfully").endResponseMessage()
                .to("direct:getAllDrugs")

                .get("/{ncdCode}").outType(DrugStore.class).description("Get One Existence")
                .param().name("ncdCode").type(RestParamType.path).description("The drug ncd code").dataType("string").endParam()
                .responseMessage().code(200).message("operation executed successfully").endResponseMessage()
                .to("direct:getDrug")

                .post("/create").consumes("application/json").type(DrugDto.class).outType(DrugStore.class).description("Create drug")
                .responseMessage().code(200).message("operation executed successfully").endResponseMessage()
                .to("direct:createDrug")

                .put("/update").consumes("application/json").type(DrugDto.class).description("Update drug")
                .responseMessage().code(200).message("operation executed successfully").endResponseMessage()
                .to("direct:updateDrug")

                .delete("/{ncdCode}").consumes("application/json").outType(DrugStore.class).description("Disable drug")
                .param().name("ncdCode").type(RestParamType.path).description("The drug ncd code").dataType("string").endParam()
                .responseMessage().code(200).message("operation executed successfully").endResponseMessage()
                .to("direct:disableDrug")

                .post("/uploadPdf").bindingMode(RestBindingMode.off).consumes("multipart/form-data").description("Upload pdf invoice for drug")
                .responseMessage().code(200).message("operation executed successfully").endResponseMessage()
                .to("direct:uploadPdf")

                .get("/getPdf/{filename}").bindingMode(RestBindingMode.off).produces("multipart/form-data").description("Download pdf invoice for drug")
                .param().name("filename").type(RestParamType.path).description("File name to download").dataType("string").endParam()
                .responseMessage().code(200).message("operation executed successfully").endResponseMessage()
                .to("direct:downloadPdf");


    }

}
