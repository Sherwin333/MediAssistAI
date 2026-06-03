package Group.com.sherwin.mediassist.controller;

import Group.com.sherwin.mediassist.service.AiService;
import Group.com.sherwin.mediassist.service.PdfService;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.Arrays;
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

        File uploadsFolder =
                new File(System.getProperty("user.dir") + "/uploads");

        File[] files = uploadsFolder.listFiles(
                (dir, name) -> name.toLowerCase().endsWith(".pdf")
        );

        if (files == null || files.length == 0) {
            return Map.of("error", "No PDF files found");
        }

        File latestFile = Arrays.stream(files)
                .max((f1, f2) -> Long.compare(
                        f1.lastModified(),
                        f2.lastModified()))
                .orElse(files[0]);

        String text =
                pdfService.extractText(latestFile.getAbsolutePath());

        return Map.of(
                "fileName", latestFile.getName(),
                "text", text.substring(
                        0,
                        Math.min(text.length(), 2000)
                )
        );
    }

    @GetMapping("/analyze-ai")
    public Map<String, String> analyzeAI() throws Exception {

        File uploadsFolder =
                new File(System.getProperty("user.dir") + "/uploads");

        File[] files = uploadsFolder.listFiles(
                (dir, name) -> name.toLowerCase().endsWith(".pdf")
        );

        if (files == null || files.length == 0) {
            return Map.of(
                    "status", "error",
                    "message", "No PDF files found"
            );
        }

        File latestFile = Arrays.stream(files)
                .max((f1, f2) -> Long.compare(
                        f1.lastModified(),
                        f2.lastModified()))
                .orElse(files[0]);

        String extractedText =
                pdfService.extractText(
                        latestFile.getAbsolutePath()
                );

        String rawResponse =
                aiService.generateSummary(extractedText);

        String summary =
                aiService.extractSummary(rawResponse);

        return Map.of(
                "status", "success",
                "fileName", latestFile.getName(),
                "summary", summary
        );
    }
}