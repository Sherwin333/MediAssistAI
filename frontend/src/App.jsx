import { useEffect, useState } from "react";
import axios from "axios";

function App() {
  const [message, setMessage] = useState("Loading...");

  useEffect(() => {
    axios
      .get("http://localhost:8080/health")
      .then((response) => {
        setMessage(response.data);
      })
      .catch((error) => {
        console.error(error);
        setMessage("Backend not connected");
      });
  }, []);

  return (
    <div style={{ textAlign: "center", marginTop: "100px" }}>
      <h1>MediAssist AI</h1>
      <h2>{message}</h2>
    </div>
  );
}

export default App;