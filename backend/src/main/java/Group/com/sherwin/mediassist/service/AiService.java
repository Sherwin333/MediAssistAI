package Group.com.sherwin.mediassist.service;

import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class AiService {

    public String generateSummary(String prompt) throws Exception {

        URL url = new URL("http://localhost:11434/api/generate");

        HttpURLConnection conn =
                (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

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

        return response.toString();
    }

    public String extractSummary(String ollamaResponse) {

        try {

            int start =
                    ollamaResponse.indexOf("\"response\":\"");

            if (start == -1) {
                return ollamaResponse;
            }

            start += 12;

            int end =
                    ollamaResponse.indexOf(
                            "\",\"done\"",
                            start
                    );

            if (end == -1) {
                return ollamaResponse;
            }

            return ollamaResponse
                    .substring(start, end)
                    .replace("\\n", "\n")
                    .replace("\\\"", "\"")
                    .replace("\\u003c", "<")
                    .replace("\\u003e", ">")
                    .replace("\\u0026", "&");

        } catch (Exception e) {
            return ollamaResponse;
        }
    }

    public String analyzeMedicalReport(
            String reportText
    ) throws Exception {

        String prompt =
                "Analyze this medical report and provide:\n" +
                "1. Key findings\n" +
                "2. Abnormal values\n" +
                "3. Recommendations\n\n" +
                reportText;

        return generateSummary(prompt);
    }

    public String askQuestion(
            String reportText,
            String question
    ) throws Exception {

        String prompt =
                "Medical Report:\n"
                        + reportText
                        + "\n\nQuestion:\n"
                        + question
                        + "\n\nInstructions:\n"
                        + "Use the report findings when relevant. "
                        + "If the question asks for medical education, "
                        + "provide a helpful explanation while referring "
                        + "to the report values. "
                        + "Do not invent report values that are not present. "
                        + "Keep the answer concise and easy to understand.";

        return generateSummary(prompt);
    }
}