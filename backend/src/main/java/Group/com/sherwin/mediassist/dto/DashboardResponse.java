package Group.com.sherwin.mediassist.dto;

public class DashboardResponse {

    private String risk;
    private String ldl;
    private String triglycerides;
    private String vitaminD;

    public DashboardResponse() {}

    public DashboardResponse(
            String risk,
            String ldl,
            String triglycerides,
            String vitaminD) {

        this.risk = risk;
        this.ldl = ldl;
        this.triglycerides = triglycerides;
        this.vitaminD = vitaminD;
    }

    public String getRisk() {
        return risk;
    }

    public String getLdl() {
        return ldl;
    }

    public String getTriglycerides() {
        return triglycerides;
    }

    public String getVitaminD() {
        return vitaminD;
    }
}