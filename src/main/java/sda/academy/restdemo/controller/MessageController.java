package sda.academy.restdemo.controller;

import sda.academy.restdemo.model.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
    private final Map<Integer, Message> messages = new HashMap<>();

    @Value("${message.default}")
    private String defaultMessage;

    @GetMapping()
    public List<Message> getMessages(){
        return new ArrayList<>(messages.values());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMessageById(@PathVariable int id){
        Message message = messages.get(id);

        if(message==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message with id: " + id + " not found");
        }

        return ResponseEntity.ok(message);
    }

    @PostMapping()
    public ResponseEntity<?> addMessage(@RequestBody Message message){
        if(!messages.containsKey(message.getId())){
            if(message.getContentOfMessage() == null || message.getContentOfMessage().isEmpty()){
                message.setContentOfMessage(defaultMessage);
            }

            this.messages.put(message.getId(), message);
            return ResponseEntity.status(HttpStatus.CREATED).body(message);
        }

        return ResponseEntity.status(HttpStatus.CONFLICT).body("Message with id " + message.getId() + " already exists");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMessage(@PathVariable int id, @RequestBody Message message){
        if(!messages.containsKey(id)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message with id: " + id + " not found");
        }

        this.messages.put(id, message);
        return ResponseEntity.status(HttpStatus.OK).body(messages.get(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMessage(@PathVariable int id){
        if(!messages.containsKey(id)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message with id: " + id + " not found");
        }

        this.messages.remove(id);
        return ResponseEntity.status(HttpStatus.OK).body("Message with id " + messages.get(id) + " has been deleted");
    }
}
