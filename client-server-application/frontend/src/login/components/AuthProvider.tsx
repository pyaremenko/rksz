import React from "react";
import axios from "axios";
import jwtDecode from "jwt-decode";

interface Token {
  login: string;
  exp: number;
}

export interface Credentials {
  login: string;
  password: string;
}

interface AuthContextType {
  user: Token | null;
  signin: (credentials: Credentials, redirect: VoidFunction) => void;
  logout: (redirect: VoidFunction) => void;
  serverError: string;
  setServerError: Function;
}

export const AuthContext = React.createContext<AuthContextType>(null!);

function AuthProvider({ children }: { children: React.ReactNode }) {
  const getJwt = (): Token | null => {
    const token = sessionStorage.getItem("jwt_token");
    if (token) {
      const decoded: Token = jwtDecode(token);
      axios.defaults.headers.common["Authorization"] =
        "Bearer " + token;
      return decoded;
    }
    return null;
  };
  const [user, setUser] = React.useState<Token | null>(getJwt());
  const [serverError, setServerError] = React.useState<string>("");

  React.useEffect(() => {
    const interval = setInterval(() => {
      if (user && user.exp * 1000 <= Date.now()) {
        localStorage.clear();
        setUser(null);
      }
    }, 1000);
    return () => {
      clearInterval(interval);
    };
  }, [user]);

  const signin = ({ login, password }: Credentials, redirect: VoidFunction) => {
    axios.post("/login", { login, password }).then(
      (res) => {
        sessionStorage.setItem("jwt_token", res.data.token);
        const decoded: Token = jwtDecode(res.data.token);
        setUser(decoded);
        setServerError("");
        axios.defaults.headers.common["Authorization"] =
          "Bearer " + res.data.token;
        redirect();
      },
      (err) => {
        setServerError(err.response.data.message);
      }
    );
  };

  const logout = (redirect: VoidFunction) => {
    setUser(null);
    sessionStorage.clear();
    redirect();
  };

  const value: AuthContextType = {
    user,
    signin,
    logout: logout,
    serverError: serverError,
    setServerError: setServerError,
  };
  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export default AuthProvider;
