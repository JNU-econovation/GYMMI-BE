package gymmi.controller;

import gymmi.global.DuplicationCheckType;
import gymmi.response.DuplicationResponse;
import gymmi.service.DuplicationCheckService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DuplicationCheckController {

    private final DuplicationCheckService duplicationCheckService;

    @GetMapping("/check-duplication")
    public ResponseEntity<DuplicationResponse> checkDuplication(
            @RequestParam("type") DuplicationCheckType type,
            @RequestParam("value") String value
    ) {
        DuplicationResponse response = duplicationCheckService.checkDuplication(type, value);
        return ResponseEntity.ok().body(response);
    }
}
