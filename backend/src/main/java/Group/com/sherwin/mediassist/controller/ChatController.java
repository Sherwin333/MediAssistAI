package Group.com.sherwin.mediassist.controller;

import Group.com.sherwin.mediassist.dto.ChatRequest;
import Group.com.sherwin.mediassist.service.AiService;
import Group.com.sherwin.mediassist.service.PdfService;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.Arrays;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class ChatController {

    private final AiService aiService;
    private final PdfService pdfService;

    public ChatController(
            AiService aiService,
            PdfService pdfService
    ) {
        this.aiService = aiService;
        this.pdfService = pdfService;
    }

    @PostMapping("/chat")
    public Map<String,String> chat(
            @RequestBody ChatRequest request
    ) throws Exception {

        File uploadsFolder =
                new File(System.getProperty("user.dir")
                        + "/uploads");

        File[] files = uploadsFolder.listFiles(
                (dir,name) ->
                        name.toLowerCase().endsWith(".pdf")
        );

        if(files == null || files.length == 0){
            return Map.of(
                    "answer",
                    "No uploaded report found."
            );
        }

        File latestFile =
                Arrays.stream(files)
                        .max((f1,f2) ->
                                Long.compare(
                                        f1.lastModified(),
                                        f2.lastModified()))
                        .orElse(files[0]);

        String reportText =
                pdfService.extractText(
                        latestFile.getAbsolutePath()
                );

        String response =
                aiService.askQuestion(
                        reportText,
                        request.getQuestion()
                );

        return Map.of(
                "answer",
                aiService.extractSummary(response)
        );
    }
}