package com.jangmo.web.controller.board;

import com.jangmo.web.service.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RequestMapping("/api/replies")
@RestController
public class ReplyController {

	private final ReplyService replyService;


}
