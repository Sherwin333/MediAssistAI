package Group.com.sherwin.mediassist.service;

import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class AiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    public String generateSummary(String reportText) throws IOException {

        OkHttpClient client = new OkHttpClient();

        String prompt =
                "Analyze this medical report. " +
                "Give key findings and abnormalities:\n\n"
                + reportText;

        String json = """
        {
          "contents": [{
            "parts": [{
              "text": "%s"
            }]
          }]
        }
        """.formatted(prompt.replace("\"", "\\\""));

        RequestBody body =
                RequestBody.create(
                        json,
                        MediaType.parse("application/json")
                );

        Request request = new Request.Builder()
                .url(
                    "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key="
                    + apiKey
                )
                .post(body)
                .build();

        Response response = client.newCall(request).execute();

        return response.body().string();
    }
}