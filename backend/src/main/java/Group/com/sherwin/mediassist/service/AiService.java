package Group.com.sherwin.mediassist.service;

import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class AiService {

    public String generateSummary(String reportText) throws Exception {

        URL url = new URL("http://localhost:11434/api/generate");

        HttpURLConnection conn =
                (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        String prompt =
                "Analyze this medical report and provide:\n" +
                "1. Key findings\n" +
                "2. Abnormal values\n" +
                "3. Recommendations\n\n" +
                reportText;

        String escapedPrompt = prompt
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "");

        String json =
                "{"
                        + "\"model\":\"llama3.2\","
                        + "\"prompt\":\"" + escapedPrompt + "\","
                        + "\"stream\":false"
                        + "}";

        try (OutputStream os = conn.getOutputStream()) {
            os.write(json.getBytes());
        }

        int responseCode = conn.getResponseCode();

        BufferedReader br;

        if (responseCode >= 400) {
            br = new BufferedReader(
                    new InputStreamReader(conn.getErrorStream())
            );
        } else {
            br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream())
            );
        }

        StringBuilder response = new StringBuilder();
        String line;

        while ((line = br.readLine()) != null) {
            response.append(line);
        }

        System.out.println("OLLAMA RESPONSE:");
        System.out.println(response);

        return response.toString();
    }
}