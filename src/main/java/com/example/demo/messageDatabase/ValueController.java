package com.example.demo.messageDatabase;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping(value=("/"), method = {RequestMethod.GET, RequestMethod.POST})
public class ValueController {

    @Autowired
    private final ValueRepository valueRepository;

    public ValueController(ValueRepository valueRepository){
        this.valueRepository = valueRepository;
    }
    
    @GetMapping("/get")
    public ResponseEntity getValues() {
        // Get all values from repository/database
        List<Value> allValues = this.valueRepository.findAll();
        int sum = 0;
        // Get total values
        for (int i = 0; i < allValues.size(); i++) {
            sum += allValues.get(0).getValue();
        }
        
        return ResponseEntity.ok(allValues);
    }

    @DeleteMapping("/delete")
    public ResponseEntity deleteValues() {
        // Delete all values from database
        this.valueRepository.deleteAll();
        return ResponseEntity.ok("Values deleted from database");
    }

    @PostMapping("/post")
    public ResponseEntity setValues() throws IOException{
        // Eead json from file
        byte[] jsonData = Files.readAllBytes(Paths.get("values.json"));
        // List of json objects in file
        ObjectMapper objectMapper = new ObjectMapper();
        Object[] values = objectMapper.readValue(jsonData, Object[].class);
        // Entire Json file to jsonnode
        // https://www.digitalocean.com/community/tutorials/jackson-json-java-parser-api-example-tutorial?fbclid=IwAR33S_T47ZAIqm939tFlnivKHitkPtkbicAQ7QpzeEvdE8HiG1oJRwIELc4
        JsonNode node = objectMapper.valueToTree(values);
        // Find all value/child fields from json

        List<JsonNode> valueSum = node.findValues("value");
        List<JsonNode> childs = node.findValues("childs");
        // Delete all data before updating
        this.valueRepository.deleteAll();
        long[] parentIDS;
        long id = 1;
        // Read all values to database
        for (int i = 0; i < valueSum.size(); i++) {
            Value value = new Value();
            // Set values for all entires
            value.setValue(valueSum.get(i).asInt());
            // Set ids for all entries
            value.setId(id);
            id++;
            // Set parent ids for all entries using recursion
            // https://www.baeldung.com/cs/storing-tree-in-rdb?fbclid=IwAR1i07BNjotdC0TsyirKqPzpdQwsJFs7DmWOS500Lsw0ciKMxgOKGXU5l2A
            parentIDS = getParentIdRecursive(value, childs);
            // Second to last element is the parent element of the current id
            value.setParentId(parentIDS[parentIDS.length - 2]);
            
            // Save value to database/repository
            this.valueRepository.save(value);
        }
        return ResponseEntity.ok("Values added to database");
    }

    public long[] getParentIdRecursive(Value value, List<JsonNode> childs){
        long[] newIds = new long[childs.size()];
        if(value.getId() == 1) {
            return newIds;
        }
        for (int i = 0; i < childs.size(); i++){
            if(childs.get(i).isNull()){
                break;
            } else {
                List<JsonNode> subChilds = childs.get(i).findValues("childs");
                newIds[i] = value.getId();
                getParentIdRecursive(value, subChilds);
            }          
        }
        return newIds;
    }

}
