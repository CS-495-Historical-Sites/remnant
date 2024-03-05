import { useState } from "react";

function useToken() {
  function getToken() {
    const userToken = localStorage.getItem("token");
    if (userToken == null) {
      return undefined;
    }
    return userToken;
  }

  const [token, setToken] = useState(getToken());

  function saveToken(userToken: string) {
    localStorage.setItem("token", userToken);
    setToken(userToken);
  }

  function removeToken() {
    localStorage.removeItem("token");
    setToken(undefined);
  }

  return {
    setToken: saveToken,
    token,
    removeToken,
  };
}

export default useToken;    