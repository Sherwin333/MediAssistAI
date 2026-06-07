import { useState } from "react";
import axios from "axios";
import ReactMarkdown from "react-markdown";

function App() {

  const [file, setFile] = useState(null);
  const [message, setMessage] = useState("");
  const [summary, setSummary] = useState("");

  const [question, setQuestion] = useState("");
  const [chatHistory, setChatHistory] = useState([]);
  const [loading, setLoading] = useState(false);

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
      setChatHistory([]);

    } catch (error) {

      console.error(error);
      setMessage("Upload Failed");
    }
  };

  const analyzeReport = async () => {

    try {

      setSummary("⏳ Analyzing report...");

      const response = await axios.get(
        "http://localhost:8080/api/analyze-ai"
      );

      setSummary(
        response.data.summary ||
        response.data.response ||
        "No summary received"
      );

    } catch (error) {

      console.error(error);
      setSummary("❌ Analysis Failed");
    }
  };

  const askAI = async () => {

  if (!question.trim()) {
    return;
  }

  try {

    setLoading(true);

    const currentQuestion = question;

    setQuestion("");

    const response = await axios.post(
      "http://localhost:8080/api/chat",
      {
        question: currentQuestion
      }
    );

    setChatHistory(prev => [
      ...prev,
      {
        question: currentQuestion,
        answer: response.data.answer
      }
    ]);

  } catch (error) {

    console.error(error);

    setChatHistory(prev => [
      ...prev,
      {
        question: question,
        answer: "❌ Failed to get answer"
      }
    ]);

  } finally {

    setLoading(false);

  }
};
<button
  onClick={() => setChatHistory([])}
  style={{
    marginLeft: "10px"
  }}
>
  Clear Chat
</button>

  return (
    <div
      style={{
        maxWidth: "1000px",
        margin: "40px auto",
        padding: "20px",
        fontFamily: "Arial"
      }}
    >

      <h1 style={{ textAlign: "center" }}>
        🏥 MediAssist AI
      </h1>

      <div style={{ textAlign: "center" }}>

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
          style={{
            marginLeft: "10px"
          }}
        >
          Analyze Report
        </button>

        <br /><br />

        <h3>{message}</h3>

      </div>

      <hr />

      <h2 style={{ textAlign: "center" }}>
        AI Medical Summary
      </h2>

      <div
        style={{
          textAlign: "left",
          border: "1px solid #ddd",
          padding: "20px",
          borderRadius: "12px",
          minHeight: "250px",
          lineHeight: "1.7"
        }}
      >
        <ReactMarkdown>
          {summary}
        </ReactMarkdown>
      </div>

      <hr />

      <h2 style={{ textAlign: "center" }}>
        💬 Ask AI About Your Report
      </h2>

      <div style={{ textAlign: "center" }}>

        <input
          type="text"
          value={question}
          onChange={(e) => setQuestion(e.target.value)}
          placeholder="Why is my LDL high?"
          style={{
            width: "70%",
            padding: "10px"
          }}
        />

        <button
          onClick={askAI}
          style={{
            marginLeft: "10px"
          }}
        >
          Ask AI
        </button>

      </div>

      <br />

      <div
  style={{
    border: "1px solid #ddd",
    padding: "20px",
    borderRadius: "12px",
    minHeight: "200px"
  }}
>

  {chatHistory.length === 0 && (
    <p>No conversation yet.</p>
  )}

  {chatHistory.map((chat, index) => (

    <div
      key={index}
      style={{
        marginBottom: "20px"
      }}
    >

      <div
        style={{
          background: "#f5f5f5",
          padding: "10px",
          borderRadius: "10px"
        }}
      >
        <strong>👤 You</strong>
        <p>{chat.question}</p>
      </div>

      <div
        style={{
          background: "#eef7ff",
          padding: "10px",
          borderRadius: "10px",
          marginTop: "10px"
        }}
      >
        <strong>🤖 MediAssist AI</strong>

        <ReactMarkdown>
          {chat.answer}
        </ReactMarkdown>

      </div>

    </div>

  ))}

  {loading && (
    <p>🤖 Thinking...</p>
  )}

</div>

    </div>
  );
}

export default App;