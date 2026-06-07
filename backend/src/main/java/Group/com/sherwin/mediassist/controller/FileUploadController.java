package Group.com.sherwin.mediassist.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class FileUploadController {

    private static final String UPLOAD_DIR =
            System.getProperty("user.dir") + "/uploads/";

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadFile(
            @RequestParam("file") MultipartFile file) {

        try {

            // Create uploads directory if it doesn't exist
            File uploadDir = new File(UPLOAD_DIR);

            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            String fileName = "report.pdf";

            File destination = new File(uploadDir, fileName);

            System.out.println("Uploading file: " + fileName);
            System.out.println("Saving to: " + destination.getAbsolutePath());

            file.transferTo(destination);

            Map<String, String> response = new HashMap<>();
            response.put("fileName", fileName);
            response.put("status", "Uploaded");

            return ResponseEntity.ok(response);

        } catch (Exception e) {

            e.printStackTrace();

            Map<String, String> response = new HashMap<>();
            response.put("status", "Failed");
            response.put("error", e.getMessage());

            return ResponseEntity.internalServerError().body(response);
        }
    }
}