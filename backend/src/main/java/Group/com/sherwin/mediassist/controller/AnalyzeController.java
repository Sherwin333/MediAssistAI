package Group.com.sherwin.mediassist.controller;

import Group.com.sherwin.mediassist.service.PdfService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class AnalyzeController {

    private final PdfService pdfService;

    public AnalyzeController(PdfService pdfService) {
        this.pdfService = pdfService;
    }

    @GetMapping("/analyze")
    public Map<String, String> analyze() throws Exception {

        String path =
                System.getProperty("user.dir")
                + "/uploads/BloodReport.pdf";

        String text = pdfService.extractText(path);

        return Map.of(
                "text",
                text.substring(0, Math.min(text.length(), 2000))
        );
    }
}