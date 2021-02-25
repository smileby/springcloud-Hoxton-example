package com.baiyun.controller;

import com.baiyun.config.WebSocketServer;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.Map;

/**
 * WebSocketController
 * @author zhengkai.blog.csdn.net
 */
@Controller
@RequestMapping
public class DemoController {

    @GetMapping("/")
    public String index(){
        return "websocket";
    }


    @RequestMapping("/push/{toUserId}/{message}")
    public ResponseEntity<String> pushToWeb(@PathVariable String message, @PathVariable String toUserId) throws IOException {
        WebSocketServer.sendInfo(message,toUserId);
        return ResponseEntity.ok("MSG SEND SUCCESS");
    }

    @RequestMapping("/sendMsg")
    public ResponseEntity<String> pushToWeb(@RequestBody Map<String, Object> map) throws IOException {
        String toUserId = (String)map.get("toUserId");
        String message = (String)map.get("msg");
        WebSocketServer.sendInfo(message,toUserId);
        return ResponseEntity.ok("MSG SEND SUCCESS");
    }
}
