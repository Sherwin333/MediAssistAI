import { useState } from "react";
import axios from "axios";

function App() {

  const [file, setFile] = useState(null);
  const [message, setMessage] = useState("");
  const [summary, setSummary] = useState("");

  const uploadFile = async () => {

    if (!file) {
      setMessage("Please select a file");
      return;
    }

    const formData = new FormData();
    formData.append("file", file);

    try {

      const response = await axios.post(
        "http://localhost:8080/api/upload",
        formData,
        {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        }
      );

      setMessage(
        `${response.data.fileName} - ${response.data.status}`
      );

      setSummary("");

    } catch (error) {

      console.error(error);
      setMessage("Upload Failed");
    }
  };

  const analyzeReport = async () => {

    try {

      setSummary("Analyzing report...");

      const response = await axios.get(
        "http://localhost:8080/api/analyze-ai"
      );

      console.log(response.data);

      setSummary(
        response.data.summary ||
        response.data.response ||
        "No summary received"
      );

    } catch (error) {

      console.error(error);
      setSummary("Analysis Failed");
    }
  };

  return (
    <div
      style={{
        maxWidth: "900px",
        margin: "50px auto",
        padding: "20px",
        fontFamily: "Arial",
        textAlign: "center"
      }}
    >
      <h1>🏥 MediAssist AI</h1>

      <input
        type="file"
        onChange={(e) => setFile(e.target.files[0])}
      />

      <br /><br />

      <button onClick={uploadFile}>
        Upload File
      </button>

      <button
        onClick={analyzeReport}
        style={{ marginLeft: "10px" }}
      >
        Analyze Report
      </button>

      <br /><br />

      <h3>{message}</h3>

      <hr />

      <h2>AI Medical Summary</h2>

      <div
        style={{
          textAlign: "left",
          whiteSpace: "pre-wrap",
          border: "1px solid #ddd",
          padding: "15px",
          borderRadius: "10px",
          minHeight: "200px"
        }}
      >
        {summary}
      </div>

    </div>
  );
}

export default App;