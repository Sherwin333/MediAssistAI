package Group.com.sherwin.mediassist.controller;

import Group.com.sherwin.mediassist.service.DashboardService;
import Group.com.sherwin.mediassist.service.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class DashboardController {

    @Autowired
    private PdfService pdfService;

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/dashboard")
    public Map<String, String> dashboard() {

        try {

            String filePath =
                    System.getProperty("user.dir")
                            + "/uploads/report.pdf";

            String reportText =
                    pdfService.extractText(filePath);

            return dashboardService.extractMetrics(reportText);

        } catch (Exception e) {

            throw new RuntimeException(e);
        }
    }
}