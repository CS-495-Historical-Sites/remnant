import { BrowserRouter, Routes, Route } from "react-router-dom";


import './App.css'

import Home from "./pages/Home";
import Register from "./pages/Register";
import Login from "./pages/Login";
import AdminView from "./pages/AdminView"
import SuggestionsView from "./pages/SuggestionView";
import useToken from "./hooks/useToken";

function App() {
  const { token, setToken, removeToken } = useToken();
  return (
    <div className="App">
      <BrowserRouter>
        <Routes>
          {<Route path="/" element={<Home />} />}AdminView

          {token && <Route path="/register" element={<AdminView />} />}
          {!token && <Route path="/register" element={<Register />} />}
          
          {token && <Route path="/login" element={<AdminView />} />}
          {!token && <Route path="/login" element={<Login setToken={setToken} />} />}

          {token && <Route path="/admin" element={<AdminView />} />}
          {!token && <Route path="/admin" element={<Login setToken={setToken}/>} />}

          {token && <Route path="/admin/suggestions" element={<SuggestionsView />} />}
          {!token && <Route path="/admin/suggestions" element={<Login setToken={setToken}/>} />}
        </Routes>
      </BrowserRouter>
    </div>
  );
}

export default App
