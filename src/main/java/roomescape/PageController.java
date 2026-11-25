package roomescape;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/** 브라우저에 HTML 화면을 보여주는 컨트롤러. */
@Controller
public class PageController {

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/reservation")
    public String reservationPage() {
        return "reservation";
    }
}
