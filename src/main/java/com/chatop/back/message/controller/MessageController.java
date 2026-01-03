package com.chatop.back.message.controller;

import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.chatop.back.message.service.MessageService;
import com.chatop.back.message.response.MessageResponse;
import com.chatop.back.message.response.MessagesResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import com.chatop.back.message.request.CreateMessageRequest;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
@Tag(
    name = "Message",
    description = "Endpoints for managing messages"
)
public class MessageController {

    private final MessageService messageService;

    @Operation(summary = "Create a new message", description = "Creates a new message to an owner about a rental")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Message created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid message data"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping("")
    public ResponseEntity<MessageResponse> createMessage(@RequestBody CreateMessageRequest request) {
        MessageResponse created = messageService.createMessage(request);
        return ResponseEntity.status(201).body(created);
    }

    @Operation(summary = "Get all messages", description = "Returns a list of all messages")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Messages retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("")
    public ResponseEntity<MessagesResponse> getMessages() {
        return ResponseEntity.ok(messageService.getMessages());
    }

    @Operation(summary = "Get message by ID", description = "Returns a single message by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Message retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Message not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/{id}")
    public ResponseEntity<MessageResponse> getMessage(@PathVariable Long id) {
        return ResponseEntity.ok(messageService.getMessage(id));
    }

    @Operation(summary = "Delete message", description = "Deletes a message by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Message deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Message not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long id) {
        messageService.deleteMessage(id);
        return ResponseEntity.noContent().build();
    }
}
