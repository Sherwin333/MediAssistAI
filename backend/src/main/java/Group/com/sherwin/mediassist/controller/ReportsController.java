package Group.com.sherwin.mediassist.controller;

import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class ReportsController {

    @GetMapping("/reports")
    public List<String> getReports() {

        String uploadPath =
                System.getProperty("user.dir")
                        + "/uploads/";

        File folder = new File(uploadPath);

        List<String> reports =
                new ArrayList<>();

        File[] files = folder.listFiles();

        if (files != null) {

            for (File file : files) {

                reports.add(file.getName());
            }
        }

        return reports;
    }
}