package com.assertsl.workshop.beans;

import com.assertsl.workshop.domain.DrugStore;
import com.assertsl.workshop.dto.DrugDto;
import org.apache.camel.Header;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TransformationBean {

    public Map getDrugParameters(@Header("ncdCode") String ncdCode){
        Map<String,String> queryParameters = new HashMap<String, String>();
        queryParameters.put("productNdc", ncdCode);
        return queryParameters;
    }


    public Map updateDrugParameters(DrugDto drugDto){
        Map<String,Object> updateParameters = new HashMap<String, Object>();
        //TODO: set parameters from dto and update the entity
        return updateParameters;
    }





}
