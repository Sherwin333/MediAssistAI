package Group.com.sherwin.mediassist.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class DashboardService {

    public Map<String, String> extractMetrics(String text) {

        Map<String, String> data = new HashMap<>();

        String ldl =
                findValue(text,
                        "LDL Cholesterol|LDL-C|LDL");

        String triglycerides =
                findValue(text,
                        "Triglycerides");

        String vitaminD =
                findValue(text,
                        "Vitamin D");

        data.put("ldl", ldl);
        data.put("triglycerides", triglycerides);
        data.put("vitaminD", vitaminD);

        data.put(
                "risk",
                calculateRisk(ldl)
        );

        System.out.println("LDL = " + ldl);
        System.out.println("Triglycerides = " + triglycerides);
        System.out.println("Vitamin D = " + vitaminD);

        return data;
    }

    private String findValue(
            String text,
            String parameter) {

        Pattern pattern = Pattern.compile(
                "(" + parameter + ")" +
                        "\\s*:?\\s*(\\d+\\.?\\d*)",
                Pattern.CASE_INSENSITIVE
        );

        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {
            return matcher.group(2);
        }

        return "Not Found";
    }

    private String calculateRisk(String ldl) {

        try {

            double value = Double.parseDouble(ldl);

            if (value > 160)
                return "High";

            if (value > 130)
                return "Moderate";

            return "Low";

        } catch (Exception e) {

            return "Unknown";
        }
    }
}