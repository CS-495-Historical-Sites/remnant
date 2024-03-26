import { BrowserRouter, Routes, Route } from "react-router-dom";

import "./App.css";

import Home from "./pages/Home";
import Register from "./pages/Register";
import Login from "./pages/Login";
import AdminView from "./pages/AdminView";
import SuggestionsView from "./pages/SuggestionView";
import EditSuggestionPage from "./pages/EditSuggestionPage";
import useToken from "./hooks/useToken";
import AddSuggestionPage from "./pages/AddSuggestionPage";

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
          {!token && (
            <Route path="/login" element={<Login setToken={setToken} />} />
          )}
          {token && <Route path="/admin" element={<AdminView />} />}
          {!token && (
            <Route path="/admin" element={<Login setToken={setToken} />} />
          )}
          {token && (
            <Route
              path="/admin/suggestions"
              element={<SuggestionsView setToken={setToken} token={token} />}
            />
          )}
          {!token && (
            <Route
              path="/admin/suggestions"
              element={<Login setToken={setToken} />}
            />
          )}
          {token && (
            <Route
              path="/admin/add-suggestion/:suggestionId"
              element={<AddSuggestionPage setToken={setToken} token={token} />}
            />
          )}
          {!token && (
            <Route
              path="/admin/add-suggestion/:suggestionId"
              element={<Login setToken={setToken} />}
            />
          )}
          {token && (
            <Route
              path="/admin/edit-suggestion/:suggestionId"
              element={<EditSuggestionPage setToken={setToken} token={token} />}
            />
          )}
          {!token && (
            <Route
              path="/admin/edit-suggestion/:suggestionId"
              element={<Login setToken={setToken} />}
            />
          )}
        </Routes>
      </BrowserRouter>
    </div>
  );
}

export default App;
