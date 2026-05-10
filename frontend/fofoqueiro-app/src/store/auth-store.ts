import { create } from 'zustand';
import { User } from '../domain/types';

interface AuthState {
  user: User | null;
  isAuthenticated: boolean;
  token: string | null;
  setUser: (user: User | null) => void;
  setToken: (token: string | null) => void;
  setAuth: (user: User, token: string) => void;
  logout: () => void;
  restoreToken: () => void;
}

const STORAGE_KEY = 'fofoqueiro_token';
const USER_STORAGE_KEY = 'fofoqueiro_user';

export const useAuthStore = create<AuthState>((set) => ({
  user: null,
  isAuthenticated: false,
  token: null,
  
  setUser: (user) => {
    set({ user, isAuthenticated: !!user });
    if (user) {
      localStorage.setItem(USER_STORAGE_KEY, JSON.stringify(user));
    } else {
      localStorage.removeItem(USER_STORAGE_KEY);
    }
  },
  
  setToken: (token) => {
    set({ token });
    if (token) {
      localStorage.setItem(STORAGE_KEY, token);
    } else {
      localStorage.removeItem(STORAGE_KEY);
    }
  },
  
  setAuth: (user, token) => {
    set({ user, token, isAuthenticated: true });
    localStorage.setItem(STORAGE_KEY, token);
    localStorage.setItem(USER_STORAGE_KEY, JSON.stringify(user));
  },
  
  logout: () => {
    set({ user: null, isAuthenticated: false, token: null });
    localStorage.removeItem(STORAGE_KEY);
    localStorage.removeItem(USER_STORAGE_KEY);
  },
  
  restoreToken: () => {
    const token = localStorage.getItem(STORAGE_KEY);
    const userJson = localStorage.getItem(USER_STORAGE_KEY);
    
    if (token && userJson) {
      try {
        const user = JSON.parse(userJson);
        set({ token, user, isAuthenticated: true });
      } catch (e) {
        console.error('Failed to restore token', e);
        localStorage.removeItem(STORAGE_KEY);
        localStorage.removeItem(USER_STORAGE_KEY);
      }
    }
  },
}));
