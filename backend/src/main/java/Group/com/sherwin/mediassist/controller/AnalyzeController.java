package Group.com.sherwin.mediassist.controller;

import Group.com.sherwin.mediassist.service.AiService;
import Group.com.sherwin.mediassist.service.PdfService;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class AnalyzeController {

    private final PdfService pdfService;
    private final AiService aiService;

    public AnalyzeController(
            PdfService pdfService,
            AiService aiService
    ) {
        this.pdfService = pdfService;
        this.aiService = aiService;
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

    @GetMapping("/analyze-ai")
    public String analyzeAI() throws Exception {

        File uploadsFolder = new File(
                System.getProperty("user.dir") + "/uploads"
        );

        File[] files = uploadsFolder.listFiles();

        if (files == null || files.length == 0) {
            return "No uploaded files found";
        }

        String filePath = files[0].getAbsolutePath();

        String extractedText =
                pdfService.extractText(filePath);

        return aiService.generateSummary(extractedText);
    }
}